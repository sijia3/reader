package com.ridko.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础标签数据
 * @author sijia3
 * @date 2021/1/5 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseTagMsg implements Serializable {

    /**
     * mac地址
     */
    private String mac;

    /**
     * epc数据内容
     */
    private String epc;

    /**
     * 标签接收时间
     */
    public Date receiveTime;

    /**
     * 接收设备的天线号
     */
    private byte ant;

    /**
     * 信号强度
     */
    private int rssi;

    /**
     * 业务操作类型
     */
    private String processType;
}
