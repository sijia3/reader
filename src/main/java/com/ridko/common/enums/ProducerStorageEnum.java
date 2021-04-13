package com.ridko.common.enums;

/**
 * 生产者存储介质类型枚举
 * @author sijia3
 * @date 2021/1/7 22:42
 */
public enum ProducerStorageEnum implements BaseEnum<String> {

    SYSTEM_QUEUE("0", "系统队列"),

    ACTIVE_MQ("1", "mq队列"),

    MONGO_DB("2", "mongodb数据库")

    ;

    private String value;
    private String desc;

    ProducerStorageEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static ProducerStorageEnum getProducerTypeEnumByValue(String value) {
        for (ProducerStorageEnum producerTypeEnum : ProducerStorageEnum.values()) {
            if (producerTypeEnum.getValue().equals(value)) {
                return producerTypeEnum;
            }
        }
        return null;
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
