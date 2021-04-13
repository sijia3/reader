package com.ridko.common.domain;

import lombok.*;

/**
 * 基础设备信息，包含mac，ip，port等基础信息
 * @author sijia3
 * @date 2021/1/5 16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseDevMsg {
    /**
     * 设备mac
     */
    private String mac;

    /**
     * 设备ip
     */
    private String ip;

    /**
     * 设备端口port
     */
    private Integer port;

    /**
     * 设备在线状态
     */
    private String online;

    /**
     * 处理业务类型
     */
    private String processType;

    /**
     * 标签生产者类型
     * 系统队列，消息队列，持久化数据库
     */
    private String producerType;

    /**
     * 读写器类型
     * 用于区分不同功能
     * 实例：RxtxReader的readerType=1; RxtxReaderTidReader(开启readerTid功能)的readerType=2
     */
    private String readerType;

    /**
     * 读写器品牌
     */
    private String brand;

    /**
     * 设置功率(0-30)
     * 四天线实例：20,20,20,20
     * 八天线实例：20,20,20,20,20,20,20,20
     */
    private String powers;

    /**
     * 打开天线 0为关闭，1为打开
     * 四天线实例: 1,0,0,0
     * 八天线实例：1,0,0,0,0,0,0,0
     */
    private String openAnts;

}
