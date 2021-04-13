package com.ridko.common.enums;

/**
 * 读写器业务操作类型枚举
 * @author sijia3
 * @date 2021/1/7 22:42
 */
public enum ReaderProcessTypeEnum implements BaseEnum<String> {
    PROCESS1("0", "操作1"),

    PROCESS2("1", "操作2"),

    ;

    private String value;
    private String desc;

    ReaderProcessTypeEnum(String value, String desc) {
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
