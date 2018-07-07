package com.aruistar.citylibrary.verticle

import com.diabolicallabs.vertx.cron.CronEventSchedulerVertical
import groovy.util.logging.Slf4j
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.JsonObject

@Slf4j
class CronVerticle extends AbstractVerticle {


    @Override
    void start(Future<Void> startFuture) throws Exception {
        log.info("verticle starting...")

        def cron_expression = config().getString("cron", "0 48 9 ? * *")  //"0 48 9 ? * *" 每天上午9:48 just a example

        JsonObject event = new JsonObject()
                .put("cron_expression", cron_expression)
                .put("address", "scheduled.address")
                .put("message", "squid")
                .put("action", "publish")
                .put("timezone_name", "Asia/Shanghai")


        vertx.deployVerticle(CronEventSchedulerVertical.newInstance(), { result ->
            if (result.succeeded()) {
                log.info("deploy CronEventSchedulerVertical OK")

                vertx.eventBus().send("cron.schedule", event, { handler ->
                    log.info(handler.succeeded().toString())
                    startFuture.complete()
                })

            }
        })
    }
}
