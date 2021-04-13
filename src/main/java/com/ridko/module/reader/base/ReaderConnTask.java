package com.ridko.module.reader.base;

import com.jeesite.common.utils.SpringUtils;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.common.enums.ReaderStatusEnum;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.reader.AbstractReader;
import com.ridko.module.reader.base.domain.ConnTaskMsg;
import com.ridko.module.reader.service.BaseDevMsgService;
import com.ridko.module.reader.service.DevMsgServiceImpl;
import com.ridko.util.RedisUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 读写器连接任务类
 * @author sijia3
 * @date 2021/1/7 20:25
 */
@Slf4j
@Getter
@Setter
public class ReaderConnTask
        implements Runnable, Delayed {

    protected RedisUtil redisUtil = SpringUtils.getBean(RedisUtil.class);
    protected DevMsgServiceImpl devMsgService = SpringUtils.getBean(DevMsgServiceImpl.class);
    protected BaseReaderConnThreadPool readerConnectThreadPool = SpringUtils.getBean(BaseReaderConnThreadPool.class);
    protected BaseDevMsgService baseDevMsgService = new DevMsgServiceImpl();
    protected ConnTaskMsg connTaskMsg;

    public ReaderConnTask(ConnTaskMsg connTaskMsg) {
        this.connTaskMsg = connTaskMsg;
    }


    /**
     * 连接读写器任务启动流程
     * @return
     */
    @Override
    public void run() {
        BaseDevMsg devMsg = connTaskMsg.getDevMsg();
        try {
            boolean noConnect = redisUtil.setIfAbsent(devMsg.getMac(), devMsg.getMac());
            log.info("mac:[{}] connectExist=[{}]", devMsg.getMac(), noConnect);
            if (noConnect) {
                redisUtil.expire(devMsg.getMac(), 3, TimeUnit.SECONDS);

                // 执行连接
                AbstractReader reader = connTaskMsg.getReader();
                ReaderProducer producer = connTaskMsg.getProducer();
                boolean isConnect = reader.start(producer);

                redisUtil.delete(devMsg.getMac());
                if (!isConnect) {
                    readerConnectThreadPool.addTask(reader, producer);
                }else {
                    // 执行成功，更新状态
                    devMsgService.updateOnlineStatusByMac(devMsg.getMac(), ReaderStatusEnum.ONLINE.getValue());
                }
            }else {
                log.warn("mac:[{}] 多线程重连锁被占用情况, 暂时不处理该操作", devMsg.getMac());
            }
        }catch (Exception e) {

        }
    }


    /**
     * 需要实现的接口，获得延迟时间   用过期时间-当前时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        long startTime = connTaskMsg.getStartTime();
        long delayTime = connTaskMsg.getDelayTime();
        return unit.convert((startTime+delayTime) - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "ReaderConnTask{" +
                "connTaskMsg=" + connTaskMsg.toString() +
                '}';
    }
}
