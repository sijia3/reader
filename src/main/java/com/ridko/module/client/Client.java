package com.ridko.module.client;

import com.alibaba.fastjson.JSON;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.common.domain.BaseTagMsg;
import com.ridko.util.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author sijia3
 * @date 2021/1/15 16:27
 */
@Component
@Order(2)
@Slf4j
public class Client implements CommandLineRunner {

    @Autowired
    private ReaderBootstrap starter;


    @SneakyThrows
    @Override
    public void run(String... args)  {

        // todo step1: 初始化变量，和读写器数据库数据状态

        // step2: 创建连接
//        redisTemplate.opsForValue().setIfAbsent("k", "v", 1, TimeUnit.SECONDS);
//        return;
//        BaseDevMsg build = BaseDevMsg.builder()
//                .brand("1")
//                .readerType("0")
//                .producerType("0")
//                .processType("0")
//                .mac("222-222-222-222")
//                .ip("222.222.242.234")
//                .port(4001)
//                .powers("14,10,10,10,10,10,10,10")
//                .openAnts("1,0,0,0,0,0,0,0")
//                .build();
//        log.info("开始连接--");
//        starter.connectReader(build);
    }


}
