package com.ridko.module.reader.factory;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.reader.AbstractReader;

/**
 * 读写器对象泛型工厂接口
 * @author sijia3
 * @date 2021/1/13 15:13
 * @param <T> 读写器管理对象
 */
@Deprecated
public interface ReaderFactory<T extends AbstractReader> {
    /**
     * 生产Reader对象
     * @param devMsg
     * @return
     */
    T getReader(BaseDevMsg devMsg);
}
