package com.ankki.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableCaching
@EnableAsync
public class WebAppApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }
}
