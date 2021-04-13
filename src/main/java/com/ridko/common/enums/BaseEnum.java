package com.ridko.common.enums;

/**
 * @author sijia3
 * @date 2021/1/7 22:44
 */
public interface BaseEnum<T> {

    T getValue();

    String getDesc();
}
