package com.ridko.module.reader;


import com.ridko.common.domain.BaseDevMsg;

/**
 * 设备接口
 * 读写器，打印机都可拓展，暂时只用在手持机上
 * @author sijia3
 * @date 2021/1/5 16:40
 */
public interface Device<T> {

    /**
     * 设备连接
     * @param devMsg
     * @return
     */
    T connect(BaseDevMsg devMsg);

    /**
     * 设备断开
     */
    void disconnect();
}
