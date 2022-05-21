package com.kwrobot.config;

import com.kwrobot.OSSService;
import org.springframework.context.annotation.Bean;


public class OSSServiceConfiguration {

    @Bean
    public OSSService ossService(){
        return new OSSService();
    }
}
