package com.ridko.module.reader.config.setpower;

import com.ridko.module.reader.AbstractReader;

/**
 * 读写器功率配置接口
 * 交由不同读写器实现不同的读写器配置
 * @author sijia3
 * @date 2021/1/7 10:04
 * @param <T> 读写器连接器 reader
 */
public interface SetPowerConfig<T extends AbstractReader> {

    /**
     * 设置功率
     * @param reader
     * @param powerStr 功率配置参数 实例：10,10,10,10
     */
    void setPower(T reader, String powerStr);
}
