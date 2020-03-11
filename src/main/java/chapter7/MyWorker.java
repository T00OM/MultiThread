package chapter7;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;

public class MyWorker extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public static enum Msg {
        WORKING, DONE, CLOSE
    }
    @Override
    public void preStart() {
        System.out.println("MyWorkder is starting");
    }
    @Override
    public void postStop() {
        System.out.println("MyWorkder is stopping");
    }
    @Override
    public void onReceive(Object o) throws Exception {
        if (o == Msg.WORKING) {
            System.out.println("I am working");
        }
        if (o == Msg.DONE) {
            System.out.println("Stop working");
        }
        if (o == Msg.CLOSE) {
            System.out.println("I will shutdown");
            getSender().tell(Msg.CLOSE, getSelf());
            getContext().stop(getSelf());
        } else
            unhandled(o);
    }
}
class WatchActor extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public WatchActor(ActorRef ref) {
        getContext().watch(ref);
    }
    @Override
    public void onReceive(Object o) {
        if (o instanceof Terminated) {
            System.out.println(String.format("%s has terminated, shutting down system",
                    ((Terminated) o).getActor().path()));
            getContext().system().shutdown();
        } else {
            unhandled(o);
        }
    }
}
class DeadMain {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("deadmain", ConfigFactory.load("sampleHello.conf"));
        ActorRef worker = actorSystem.actorOf(Props.create(MyWorker.class), "worker");
        actorSystem.actorOf(Props.create(WatchActor.class, worker), "watcher");
        worker.tell(MyWorker.Msg.WORKING, ActorRef.noSender());
        worker.tell(MyWorker.Msg.DONE, ActorRef.noSender());
        worker.tell(MyWorker.Msg.CLOSE, ActorRef.noSender());
        worker.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
