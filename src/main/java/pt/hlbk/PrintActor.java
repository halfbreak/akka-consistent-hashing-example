package pt.hlbk;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class PrintActor extends AbstractBehavior<String> {

    public static Behavior<String> create() {
        return Behaviors.setup(PrintActor::new);
    }

    public PrintActor(ActorContext<String> context) {
        super(context);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessage(String.class, s -> {
                    System.out.printf("[%s] Message in %s: %s %n", Thread.currentThread().getName(), getContext().getSelf(), s);
                    return Behaviors.same();
                })
                .build();
    }
}
