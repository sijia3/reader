package com.ridko.module.producer.test;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sijia3
 * @date 2021/1/16 9:56
 */
@Configuration
public class TestMqConfig {
    @Bean(name = "testMqQueue")
    public ActiveMQQueue testMqQueue(){
        return new ActiveMQQueue("testMqQueue");
    }
}

