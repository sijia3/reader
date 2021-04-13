package com.ridko.common.enums;

/**
 * 读写器功能类型枚举
 * 如普通功能，开启读tid功能，可以从该类型区分
 * @author sijia3
 * @date 2021/1/8 21:12
 */
public enum ReaderFunctionEnum implements BaseEnum<String>{
    UNKNOWN("0", "普通功能"),
    ANO_UNKNOWN("1", "读tid功能"),

    ;

    private String value;
    private String desc;

    ReaderFunctionEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
