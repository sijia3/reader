package com.ridko.common.anno;

import com.ridko.common.enums.ReaderBrandEnum;
import com.ridko.common.enums.ReaderFunctionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Reader读写器管理对象，定义读写器的brand品牌和开启功能
 * 用于全局对Reader进行管理分配
 * 参考: {@link com.ridko.module.reader.support.RxtxReader}
 * @author sijia3
 * @date 2021/1/19 12:07
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reader {
    /**
     * 读写器品牌
     * @return
     */
    ReaderBrandEnum brand();

    /**
     * 读写器功能，如通用功能，开启读tid功能等操作
     * @return
     */
    ReaderFunctionEnum functionType();
}
