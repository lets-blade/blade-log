package com.blade.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.impl.MDC;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author biezhi
 * @date 2018/3/29
 */
@Slf4j
public class LoggerTest {

    @Test
    public void testLogger() {
        log.debug("Hello World");
        log.info("Hello World");
        log.info("Hello World");
        log.info("Hello World");
        log.warn("Hello World");
        log.error("Hello World");
    }

    @Test
    public void testWhileLog() throws InterruptedException {
        while (true) {
            testLogger();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void testError() {
        try {
            int a = 1 / 0;
            System.out.println(a);
        } catch (Exception e) {
            log.error("eee", e);
        }
    }


    @Test
    public void testMDCLog() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(this::testMDCLogAsync);
        executorService.execute(this::testMDCLogAsync);
        TimeUnit.SECONDS.sleep(10);
        System.out.println("over");
    }

    private void testMDCLogAsync() {
        try {

            log.info("Hello World");
            MDC.put("traceId", UUID.randomUUID().toString());

            new Thread(()->{
                log.info("Thread ,test traceId");
            }).start();

            Executors.newFixedThreadPool(1).execute(()->{
                log.info("newFixedThreadPool,test traceId");
            });

            CompletableFuture.runAsync(()->{
                log.info("CompletableFuture ,test traceId");
            }).join();

            TimeUnit.SECONDS.sleep(5);
            MDC.clear();
            log.info("Hello World");
        }catch (Exception e){
            log.error("eee", e);
        }


    }
}
