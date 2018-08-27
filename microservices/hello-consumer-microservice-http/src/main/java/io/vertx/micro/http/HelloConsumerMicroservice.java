package io.vertx.micro.http;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.*;
import io.vertx.rxjava.ext.web.client.*;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;

public class HelloConsumerMicroservice extends AbstractVerticle {

    private WebClient client;

    @Override
    public void start() {
        client = WebClient.create(vertx);

        Router router = Router.router(vertx);
        router.get("/").handler(this::invokeMyFirstMicroservice);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8081);
    }

    private void invokeMyFirstMicroservice(RoutingContext routingContext) {

        HttpRequest<JsonObject> request1 = client
            .get(8080, "localhost", "/Tim")
            .as(BodyCodec.jsonObject());

        HttpRequest<JsonObject> request2 = client
            .get(8080, "localhost", "/Ann")
            .as(BodyCodec.jsonObject());

        Single<JsonObject> single1 = request1.rxSend()
            .map(HttpResponse::body);

        Single<JsonObject> single2 = request2.rxSend()
            .map(HttpResponse::body);

        Single
            .zip(single1, single2, (tim, ann) -> {
                // We have the results of both requests in Time and Ann
                return new JsonObject()
                    .put("Tim", tim.getString("message"))
                    .put("Ann", ann.getString("message"));
            })
            .subscribe(
                result -> routingContext.response().end(result.encodePrettily()),
                error -> {
                    error.printStackTrace();
                    routingContext.response().setStatusCode(500).end(error.getMessage());
                }
            );
    }
}
