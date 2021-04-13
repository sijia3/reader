package com.ridko.module.reader.base;

import com.jeesite.common.lang.ExceptionUtils;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.reader.AbstractReader;
import com.ridko.module.reader.base.domain.ConnTaskMsg;
import com.ridko.module.reader.support.RxtxReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 暂为罗丹贝尔读写器的连接线程池
 * @author sijia3
 * @date 2021/1/9 22:06
 */
@Slf4j
@Component
public class BaseReaderConnThreadPool {
    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    private static DelayQueue<ReaderConnTask> readerConnTaskQueue = new DelayQueue<>();     // 延迟执行任务队列

    @PostConstruct
    public void init() {
        new ReaderConnectThead().start();
    }

    /**
     *  step1: 创建连接任务需要的对象
     *  step2: 将其添加到执行任务中
     * @param reader
     * @param producer
     */
    public void addTask(AbstractReader reader, ReaderProducer producer) {
        ConnTaskMsg connTaskMsg = connTaskMsgBuild(reader, producer);
        this.addTask(connTaskMsg);
    }

    public void addTask(ConnTaskMsg connTaskMsg) {
        ReaderConnTask task = new ReaderConnTask(connTaskMsg);
        log.info("创建任务:[{}]", task.toString());
        readerConnTaskQueue.offer(task);
    }


    /**
     * 构建连接实体对象connTaskMsg
     * 根据connectCount判断重连时间，并设置延时策略，添加进任务
     * @param reader
     * @param producer
     * @return
     */
    private ConnTaskMsg connTaskMsgBuild(AbstractReader reader, ReaderProducer producer) {
        BaseDevMsg devMsg = reader.getDevMsg();
        int connectCount = reader.getConnectCount();
        reader.setConnectCount(connectCount+1);
        ConnTaskMsg connTaskMsg = new ConnTaskMsg();
        if (connectCount > 10) {
            log.info("mac: [{}] 连接次数超过10次, 重连时间统一设置为5分钟一次", devMsg.getMac());
            connTaskMsg.setDelayTime(5*60*1000);
        }else {
            log.info("mac: [{}]加入连接队列中, 当前次数为: [{}], 等待时间:[{}]s", devMsg.getMac(), connectCount, (connectCount)*30);
            connTaskMsg.setDelayTime((connectCount) * 30 * 1000+ 2*1000);
        }
        String taskName = devMsg.getMac()+": connect count:"+ connectCount;
        connTaskMsg.setDevMsg(devMsg);
        connTaskMsg.setProducer(producer);
        connTaskMsg.setReader(reader);
        connTaskMsg.setTaskName(taskName);
        return connTaskMsg;
    }

    /**
     * 线程池内部连接线程
     * 监听readerConnTaskQueue
     * 如果有，取出并提交
     */
    private class ReaderConnectThead extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    ReaderConnTask task = readerConnTaskQueue.take();
                    executorService.submit(task);
                }catch (Exception e) {
                    log.info("连接任务task执行发生异常:[{}]", ExceptionUtils.getStackTraceAsString(e));
                }
            }
        }
    }



}
