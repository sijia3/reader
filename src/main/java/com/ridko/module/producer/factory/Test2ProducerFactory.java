package com.ridko.module.producer.factory;

import com.ridko.common.anno.Producer;
import com.ridko.common.enums.ProducerStorageEnum;
import com.ridko.common.enums.ReaderProcessTypeEnum;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.producer.SqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sijia3
 * @date 2021/1/14 17:12
 */
@Producer(value = ReaderProcessTypeEnum.PROCESS2)
@Component
public class Test2ProducerFactory implements ProducerFactory {

    @Autowired
    private SqProducer sqProducer;

    @Override
    public ReaderProducer createProducer(BaseDevMsg devMsg) {
        String producerType = devMsg.getProducerType();
        if (ProducerStorageEnum.SYSTEM_QUEUE.getValue().equals(producerType)) {
            return sqProducer;
        }
        return null;
    }
}
