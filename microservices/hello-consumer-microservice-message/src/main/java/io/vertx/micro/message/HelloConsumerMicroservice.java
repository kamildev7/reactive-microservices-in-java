package io.vertx.micro.message;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

public class HelloConsumerMicroservice extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer()
            .requestHandler(
                req -> {
                    Single<JsonObject> obs1 = vertx.eventBus()
                        .<JsonObject>rxSend("hello", "Tim")
                        .map(Message::body);
                    Single<JsonObject> obs2 = vertx.eventBus()
                        .<JsonObject>rxSend("hello", "Ann")
                        .map(Message::body);

                    Single
                        .zip(obs1, obs2, (tim, ann) ->
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