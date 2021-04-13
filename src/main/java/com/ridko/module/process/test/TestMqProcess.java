package com.ridko.module.process.test;

import com.ridko.common.domain.BaseTagMsg;
import com.ridko.module.process.ReaderProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 组合，通过包装TestProcess来实现业务操作
 * @author sijia3
 * @date 2021/1/18 11:43
 */
@Component
public class TestMqProcess implements ReaderProcess {

    @Autowired
    private TestProcess testProcess;

    @JmsListener(destination="testMqQueue")
    @Override
    public void process(BaseTagMsg tagMsg) {
        testProcess.process(tagMsg);
    }
}
