package com.ridko.common.anno;

import com.ridko.common.enums.ReaderProcessTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统队列的业务处理方式
 * 定义某种业务的系统队列接收方式处理
 * 参考：{@link com.ridko.module.process.test.TestSqProcess}
 * @author sijia3
 * @date 2021/1/18 11:40
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SqProcess {

    /**
     * 读写器业务处理类型
     * @return
     */
    ReaderProcessTypeEnum value();
}
