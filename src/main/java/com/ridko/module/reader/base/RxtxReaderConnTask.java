package com.ridko.module.reader.base;

import com.jeesite.common.utils.SpringUtils;
import com.ridko.module.reader.base.domain.ConnTaskMsg;
import lombok.extern.slf4j.Slf4j;

/**
 * 罗丹贝尔读写器连接任务
 * @author sijia3
 * @date 2021/1/13 20:41
 */
@Slf4j
@Deprecated
public class RxtxReaderConnTask
        extends ReaderConnTask {

    private BaseReaderConnThreadPool readerConnectThreadPool = SpringUtils.getBean(BaseReaderConnThreadPool.class);

    public RxtxReaderConnTask(ConnTaskMsg connTaskMsg) {
        super(connTaskMsg);
    }

    /**
     * 连接读写器任务启动流程
     * @return
     */
    @Override
    public void run() {
//        BaseDevMsg devMsg = connTaskMsg.getDevMsg();
//        try {
//            boolean noConnect = redisUtil.setIfAbsent(devMsg.getMac(), devMsg.getMac());
//            log.info("mac:[{}] connectExist=[{}]", devMsg.getMac(), noConnect);
//            if (noConnect) {
//                redisUtil.expire(devMsg.getMac(), 3, TimeUnit.SECONDS);
//
//                // 执行连接
//                AbstractReader reader = connTaskMsg.getReader();
//                ReaderProducer producer = connTaskMsg.getProducer();
//                boolean isConnect = reader.start(producer);
//
//                redisUtil.delete(devMsg.getMac());
//                if (!isConnect) {
//                    readerConnectThreadPool.addTask(reader, producer);
//                }else {
//                    // 执行成功，更新状态
//                }
//            }else {
//                log.warn("mac:[{}] 多线程重连锁被占用情况, 暂时不处理该操作", devMsg.getMac());
//            }
//        }catch (Exception e) {
//
//        }
    }


}
