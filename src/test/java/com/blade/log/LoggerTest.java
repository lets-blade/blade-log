package com.blade.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

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
//            TimeUnit.SECONDS.sleep(1);
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

}
