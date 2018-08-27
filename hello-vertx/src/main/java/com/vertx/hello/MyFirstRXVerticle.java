package com.vertx.hello;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;

/**
 * @author kamildev7 on 2018-08-27.
 */
public class MyFirstRXVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        // We get the stream of request as Observable
        server.requestStream().toObservable()
            .subscribe(req -> {
                // for each HTTP request, this method is called
                req.response().end("Hello from " +
                    Thread.currentThread().getName());
            });
        // We start the server using rxListen returning a
        // Single of HTTP server. We need to subscribe to
        // trigger the operation
        server
            .rxListen(8080)
            .subscribe();
    }
}
