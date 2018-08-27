package com.vertx.hello;

import io.vertx.core.AbstractVerticle;

/**
 * @author kamildev7 on 2018-08-27.
 */
public class MyFirstVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
            .requestHandler(req -> {
                req.response().end("Hello from "
                    + Thread.currentThread().getName());
            })
            .listen(8080);
    }
}
