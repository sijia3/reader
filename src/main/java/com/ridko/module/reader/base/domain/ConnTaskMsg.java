package com.ridko.module.reader.base.domain;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.producer.ReaderProducer;
import com.ridko.module.reader.AbstractReader;
import lombok.Data;

/**
 * 读写器连接任务实体
 * @author sijia3
 * @date 2021/1/13 21:12
 */
@Data
public class ConnTaskMsg {
    private AbstractReader reader;
    private ReaderProducer producer;
    private BaseDevMsg devMsg;
    private String taskName;
    private long delayTime;
    private long startTime = System.currentTimeMillis();        //开始时间

    @Override
    public String toString() {
        return "ConnTaskMsg{" +
                "taskName='" + taskName + '\'' +
                ", delayTime=" + delayTime +
                ", startTime=" + startTime +
                '}';
    }
}
