package com.ridko.common.enums;

/**
 * 读写器品牌枚举
 * @author sijia3
 * @date 2021/1/8 21:20
 */
public enum  ReaderBrandEnum implements BaseEnum<String>{
    RODIN_BELL("1", "罗丹贝尔"),

    DA_TANG("2", "大唐读写器"),

    ;

    private String value;
    private String desc;

    ReaderBrandEnum(String value, String desc) {
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
