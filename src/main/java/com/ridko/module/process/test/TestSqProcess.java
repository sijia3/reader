package com.ridko.module.process.test;

import com.ridko.common.anno.SqProcess;
import com.ridko.common.enums.ReaderProcessTypeEnum;
import com.ridko.common.domain.BaseTagMsg;
import com.ridko.module.process.ReaderProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义TestProcess业务的系统队列处理器
 * @author sijia3
 * @date 2021/1/18 11:42
 */

@Slf4j
@SqProcess(value = ReaderProcessTypeEnum.PROCESS1)
@Component
public class TestSqProcess implements ReaderProcess {

    /**
     * 持有实际的通用处理
     */
    @Autowired
    private TestProcess testProcess;

    @Override
    public void process(BaseTagMsg tagMsg) {
        log.info("testSqProcess 正在处理tagMsg:[{}]", tagMsg.getEpc());
        testProcess.process(tagMsg);
    }
}
