package com.ridko.module.producer;

import com.ridko.common.domain.BaseTagMsg;

/**
 * 读写器生产者接口
 * 允许不同类型（baseDevMsg.getProcessType）的生产者实现该接口
 * @author sijia3
 * @date 2021/1/6 21:03
 */
public interface ReaderProducer {

    /**
     * 添加到队列中
     * @param tagMsg
     */
    void addTagMsg(BaseTagMsg tagMsg);
}

