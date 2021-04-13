package com.ridko.module.producer;

import com.ridko.common.domain.BaseTagMsg;
import com.ridko.module.process.base.SqProcessThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统队列通用生产者
 * 将标签发送到sqProcess中的队列中
 * sqProcess内部维护一个线程，根据tagMsg.processType获取合适的process去处理
 * @author sijia3
 * @date 2021/1/7 17:05
 */
@Component
public class SqProducer implements ReaderProducer {

    @Autowired
    private SqProcessThread sqProcessThread;

    @Override
    public void addTagMsg(BaseTagMsg tagMsg) {
        sqProcessThread.addQueue(tagMsg);
    }
}
