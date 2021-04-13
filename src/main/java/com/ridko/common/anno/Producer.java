package com.ridko.common.anno;

import com.ridko.common.enums.ReaderProcessTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Producer注解，定义在ProducerFactory上，用于标识ProducerFactory的业务类型
 * 使用参考：{@link com.ridko.module.producer.factory.TestProducerFactory}
 * @author sijia3
 * @date 2021/1/14 17:19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Producer {

    /**
     * 定义标签生产者的业务类型
     * @return
     */
    ReaderProcessTypeEnum value();
}
