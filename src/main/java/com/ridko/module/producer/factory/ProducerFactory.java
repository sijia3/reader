package com.ridko.module.producer.factory;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.producer.ReaderProducer;

/**
 * 生产者工厂接口
 * 允许不同类型（baseDevMsg.getProcessType）的生产者实现该接口
 * 并根据producerStorageEnum选择返回不同的生产者
 * @author sijia3
 * @date 2021/1/13 15:34
 */
public interface ProducerFactory {

    ReaderProducer createProducer(BaseDevMsg devMsg);
}
