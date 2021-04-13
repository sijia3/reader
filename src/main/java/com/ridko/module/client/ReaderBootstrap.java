package com.ridko.module.client;


import com.ridko.common.domain.BaseDevMsg;
import com.ridko.common.enums.ReaderStatusEnum;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.producer.factory.ProducerFactory;
import com.ridko.module.reader.AbstractReader;
import com.ridko.module.reader.base.BaseReaderConnThreadPool;
import com.ridko.module.reader.container.ReaderContainer;
import com.ridko.module.reader.reflections.ReaderBeanContext;
import com.ridko.module.reader.service.DevMsgServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 读写器启动类
 * @author sijia3
 * @date 2021/1/5 16:37
 */
@Component
@Slf4j
public class ReaderBootstrap {

    @Autowired
    private DevMsgServiceImpl devMsgService;

    @Autowired
    private BaseReaderConnThreadPool connectThreadPool;

    @Autowired
    private ReaderContainer readerConn;


    /**
     * 重连任务
     * 1. 根据mac获取设备信息并断开设备连接
     * 2. 重新创建连接
     * @param mac
     */
    public void addReconnectTask(String mac) {
        // 断开连接
        AbstractReader reader = readerConn.getReader(mac);
        reader.disconnect();

        // 修改状态
        devMsgService.updateOnlineStatusByMac(mac, ReaderStatusEnum.OFFLINE.getValue());

        // 重新创建连接
        BaseDevMsg devMsg = reader.getDevMsg();
        connectReader(devMsg);
    }

    /**
     * 单个devMsg连接
     * @param devMsg
     */
    public void connectReader(BaseDevMsg devMsg) {
        List<BaseDevMsg> devMsgs = Collections.singletonList(devMsg);
        connectReaderList(devMsgs);
    }

    /**
     * 连接设备列表
     * step1: 让客户端自己根据devMsg在ReaderBeanContext中选择reader和processFactory，再由producerFactory生产出特定的ReaderProducer
     * step2: 将reader和producer加进连接任务中
     */
    public void connectReaderList(List<BaseDevMsg> devMsgs) {
        for (BaseDevMsg devMsg : devMsgs) {
            AbstractReader reader = ReaderBeanContext.newReader(devMsg);
            ProducerFactory producerFactory = ReaderBeanContext.getProducerFactory(devMsg.getProcessType());
            ReaderProducer producer = producerFactory.createProducer(devMsg);
            connectThreadPool.addTask(reader, producer);
        }
    }

}
