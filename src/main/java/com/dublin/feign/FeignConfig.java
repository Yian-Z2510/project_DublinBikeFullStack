package com.dublin.feign;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    
    @Bean
    Logger.Level feignLoggerLevel() {
        // API credentials are query parameters, so request URLs must not be logged.
        return Logger.Level.NONE;
    }
    
    @Bean
    public Request.Options options() {
        return new Request.Options(10000, 60000);
    }
}
