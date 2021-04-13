package com.ridko.module.producer.test;

import com.ridko.common.domain.BaseTagMsg;
import com.ridko.module.producer.ReaderProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sijia3
 * @date 2021/1/16 9:52
 */
@Component
public class TestMqProducer implements ReaderProducer {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Resource(name = "testMqQueue")
    private ActiveMQQueue queue;

    @Override
    public void addTagMsg(BaseTagMsg tagMsg) {
        jmsTemplate.convertAndSend(queue, tagMsg);
    }
}
