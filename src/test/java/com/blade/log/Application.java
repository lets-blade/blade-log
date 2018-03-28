package com.blade.log;

import lombok.extern.slf4j.Slf4j;

/**
 * @author biezhi
 * @date 2017/9/4
 */
@Slf4j
public class Application {

    public static void main(String[] args) {
        for (int i = 30; i < 106; i++) {
            if(i > 48 && i < 89){
                continue;
            }
            System.out.println("\033[" + i + "m INFO -> "+ i +" 你好世界 \033[0m");
            System.out.println("echo -e \"\\033[" + i + "m INFO -> "+ i +" 你好世界 \\033[0m\"");
        }
        log.debug("Hello World");
        log.info("Hello World");
        log.info("Hello World");
        log.info("Hello World");
        log.warn("Hello World");
        log.error("Hello World");
    }

}
