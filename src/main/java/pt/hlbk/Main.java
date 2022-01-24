package pt.hlbk;


import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        ActorRef<String> testSystem = ActorSystem.create(ForwardActor.create(), "testSystem");
        testSystem.tell("start");

        IntStream
                .range(0, 100)
                .boxed()
                .forEach(index -> {
                    if (index % 3 == 0) {
                        testSystem.tell("3");
                    } else if (index % 2 == 0) {
                        testSystem.tell("2");
                    } else {
                        testSystem.tell("impar");
                    }
                });
    }
}
