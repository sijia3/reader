package com.ridko.common.enums;

/**
 * 读写器设备状态枚举
 * @author sijia3
 * @date 2021/2/19 14:14
 */
public enum ReaderStatusEnum implements BaseEnum<Integer>{

    OFFLINE(0, "离线"),
    ONLINE(1, "在线");

    private int status;
    private String remark;

    private ReaderStatusEnum(Integer status, String remark){
        this.status = status;
        this.remark = remark;
    }

    @Override
    public Integer getValue() {
        return status;
    }

    @Override
    public String getDesc() {
        return remark;
    }
}
