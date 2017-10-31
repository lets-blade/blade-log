package com.blade.log;

import com.blade.Blade;
import lombok.extern.slf4j.Slf4j;

/**
 * @author biezhi
 * @date 2017/9/4
 */
@Slf4j
public class Application {

    public static void main(String[] args) {
        Blade.me().get("/", ((request, response) -> {
            log.info("Hello World");
        })).start(Application.class, args);
    }

}
