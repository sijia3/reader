package com.ridko.module.process.test;

import com.ridko.common.domain.BaseTagMsg;
import com.ridko.module.process.ReaderProcess;
import org.springframework.stereotype.Component;

/**
 * 允许子类包装特定类型的处理，实现不同方式的数据接收的处理
 * @author sijia3
 * @date 2021/1/18 10:57
 * 包装方式参考 {@link com.ridko.module.process.test.TestSqProcess}
 */
@Component
public class TestProcess implements ReaderProcess {

    @Override
    public void process(BaseTagMsg tagMsg) {
        // 实现真正的业务逻辑
        // step1:
        // step2:
        // ....
    }

}
