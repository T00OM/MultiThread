package chapter7;

import akka.actor.*;
import akka.japi.Function;
import com.sun.corba.se.impl.encoding.CDROutputObject;
import com.typesafe.config.ConfigFactory;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Supervisor extends UntypedActor {

    private static SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES),
            new Function<Throwable, SupervisorStrategy.Directive>() {
                @Override
                public SupervisorStrategy.Directive apply(Throwable throwable) throws Exception {
                    if(throwable instanceof ArithmeticException) {
                        System.out.println("meet ArithmeticException, just resume");
                        return SupervisorStrategy.resume();
                    } else if(throwable instanceof NullPointerException) {
                        System.out.println("meet NullPointerException, restart");
                        return SupervisorStrategy.restart();
                    } else if(throwable instanceof IllegalArgumentException) {
                        return SupervisorStrategy.stop();
                    } else
                        return SupervisorStrategy.escalate();
                }
            });
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof Props)
//            创建被监督的子Actor
            getContext().actorOf((Props) o, "restartActor");
        else
            unhandled(o);
    }
}
class RestartActor extends UntypedActor {
    public enum Msg {
        DONE, RESTART
    }

    @Override
    public void preStart() {
        System.out.println("preStart hashcode: " + this.hashCode());
    }

    @Override
    public void postStop() {
        System.out.println("postStop hashcode: " + this.hashCode());
    }

    @Override
    public void postRestart(Throwable throwable) throws Exception {
        super.postRestart(throwable);
        System.out.println("postRestart hashcode: " + this.hashCode());
    }

    @Override
    public void preRestart(Throwable throwable, Option option) {
        System.out.println("preRestart hashcode: " + this.hashCode());
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if(o == Msg.DONE) {
            getContext().stop(getSelf());
        } else if(o == Msg.RESTART) {
            System.out.println(((Object) null).toString());
            double a = 0 / 0;
        }
        unhandled(o);
    }
}
class SupervisorMain {
    public static void customStrategy(ActorSystem system) {
        ActorRef a = system.actorOf(Props.create(Supervisor.class), "Supervisor");
        a.tell(Props.create(RestartActor.class), ActorRef.noSender());
        ActorSelection sel = system.actorSelection("akka://lifecycle/user/Supervisor/restartActor");
        for(int i = 0; i < 100; i++) {
            sel.tell(RestartActor.Msg.RESTART, ActorRef.noSender());
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("lifecycle", ConfigFactory.load("lifecycle.conf"));
        customStrategy(system);
    }
}
