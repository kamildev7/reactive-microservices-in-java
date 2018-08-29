package io.vertx.micro.message;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

import java.util.concurrent.TimeUnit;

public class HelloConsumerMicroservice extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer()
            .requestHandler(
                req -> {
                    EventBus bus = vertx.eventBus();
                    Single<JsonObject> objectSingle1 = bus
                        .<JsonObject>rxSend("hello", "Tim")
                        .subscribeOn(RxHelper.scheduler(vertx))
                        .timeout(3, TimeUnit.SECONDS)
                        .retry((i, t) -> {
                            System.out.println("Retrying...because of " + t.getMessage());
                            return true;
                        })
                        .map(Message::body);

                    Single<JsonObject> objectSingle2 = bus
                        .<JsonObject>rxSend("hello", "Ann")
                        .subscribeOn(RxHelper.scheduler(vertx))
                        .timeout(3, TimeUnit.SECONDS)
                        .retry((i, t) -> {
                            System.out.println("Retrying...because of " + t.getMessage());
                            return true;
                        })
                        .map(Message::body);

                    Single
                        .zip(objectSingle1, objectSingle2, (tim, ann) ->
                            new JsonObject()
                                .put("Tim", tim.getString("message")
                                    + " from " + tim.getString("served-by"))
                                .put("Ann", ann.getString("message")
                                    + " from " + ann.getString("served-by"))
                        )
                        .subscribe(
                            x -> req.response().end(x.encodePrettily()),
                            t -> req.response().setStatusCode(500).end(t.getMessage())
                        );
                })
            .listen(8082);
    }
}
