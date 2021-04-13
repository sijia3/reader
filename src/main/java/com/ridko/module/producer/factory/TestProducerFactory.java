package com.ridko.module.producer.factory;

import com.ridko.common.anno.Producer;
import com.ridko.common.enums.ProducerStorageEnum;
import com.ridko.common.enums.ReaderProcessTypeEnum;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.producer.test.TestMqProducer;
import com.ridko.module.producer.SqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sijia3
 * @date 2021/1/13 15:35
 */
@Producer(value = ReaderProcessTypeEnum.PROCESS1)
@Component
public class TestProducerFactory implements ProducerFactory {

    @Autowired
    private TestMqProducer testMqProducer;

    @Autowired
    private SqProducer sqProducer;

    @Override
    public ReaderProducer createProducer(BaseDevMsg devMsg) {
        String producerType = devMsg.getProducerType();
        if (ProducerStorageEnum.SYSTEM_QUEUE.getValue().equals(producerType)) {
            return sqProducer;
        }else if (ProducerStorageEnum.ACTIVE_MQ.getValue().equals(producerType)) {
            return testMqProducer;
        }
        return null;
    }
}
