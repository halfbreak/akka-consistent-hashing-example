package pt.hlbk;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.*;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class ForwardActor extends AbstractBehavior<String> {

    public static Behavior<String> create() {
        return Behaviors.setup(ForwardActor::new);
    }

    private final ActorRef<String> router;

    public ForwardActor(ActorContext<String> context) {
        super(context);
        int poolSize = 4;
        PoolRouter<String> pool =
                Routers.pool(
                        poolSize,
                        // make sure the workers are restarted if they fail
                        Behaviors.supervise(PrintActor.create()).onFailure(SupervisorStrategy.restart()))
                .withConsistentHashingRouting(2, s -> Hashing.sha256()
                        .hashString(s, StandardCharsets.UTF_8)
                        .toString());
        router = context.spawn(pool, "worker-pool", DispatcherSelector.fromConfig("your-dispatcher"));
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessage(String.class, s -> {
                    router.tell(s);
                    return Behaviors.same();
                })
                .build();
    }
}
