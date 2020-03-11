package chapter7;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

public class HelloWorld extends UntypedActor {
    ActorRef greeter;
    @Override
    public void preStart() {
        greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        System.out.println("Greeter Actor Path: " + greeter.path());
        greeter.tell(Greeter.Msg.GREET, getSelf());
    }
    @Override
    public void onReceive(Object o) throws Exception {
        if (o == Greeter.Msg.DONE) {
            greeter.tell(Greeter.Msg.GREET, getSelf());
            getContext().stop(getSelf());
        } else
            unhandled(o);
    }
}
class Greeter extends UntypedActor {
    public enum Msg {
        GREET, DONE;
    }
    @Override
    public void onReceive(Object o) throws Exception {
        if(o == Msg.GREET) {
            System.out.println("hello world!");
            getSender().tell(Msg.DONE, getSelf());
        } else
            unhandled(o);
    }
}
class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Hello", ConfigFactory.load("sampleHello.conf"));
        ActorRef a = system.actorOf(Props.create(HelloWorld.class), "Helloworld");
        System.out.println("Helloworld Actor Path: " + a.path());
    }
}
