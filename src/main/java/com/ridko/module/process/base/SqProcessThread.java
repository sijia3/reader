package com.ridko.module.process.base;

import com.ridko.common.domain.BaseTagMsg;
import com.ridko.module.process.ReaderProcess;
import com.ridko.module.reader.reflections.ReaderBeanContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 采用系统队列的处理
 * @author sijia3
 * @date 2021/1/18 10:58
 */
@Component
@Slf4j
public class SqProcessThread {

    private static LinkedBlockingQueue<BaseTagMsg> tagMsgsQueue = new LinkedBlockingQueue<>();

    public void addQueue(BaseTagMsg tagMsg) {
        tagMsgsQueue.offer(tagMsg);
    }


    @PostConstruct
    public void init() {
        new ProcessThread().start();
    }

    /**
     * 系统队列内部处理线程
     * 根据tagMsg的processType选择不同的业务逻辑算法process进行处理
     */
    private class ProcessThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    BaseTagMsg baseTagMsg = tagMsgsQueue.take();
                    log.info("processThread正在处理tagMsg...");
                    ReaderProcess readerProcess = ReaderBeanContext.getReaderSqProcess(baseTagMsg.getProcessType());
                    if (Objects.nonNull(readerProcess)) {
                        readerProcess.process(baseTagMsg);
                    }
                }catch (InterruptedException e) {
                    // todo 处理一下
                }
            }

        }
    }
}
