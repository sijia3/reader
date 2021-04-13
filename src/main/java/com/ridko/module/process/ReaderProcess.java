package com.ridko.module.process;

import com.ridko.common.domain.BaseTagMsg;

/**
 * 标签消费者接口
 * 不同的process的消费者，通过实现该接口，定义真正的业务操作
 * @author sijia3
 * @date 2021/1/7 17:57
 * 参考 {@link com.ridko.module.process.test.TestProcess}
 */
public interface ReaderProcess {

    void process(BaseTagMsg tagMsg);
}
