package com.zhi.pin.model.enums;

/**
 * 表示岗位类型的枚举类
 */
public enum PositionTypeEnum {
    TECHNICAL(1, "技术"),
    MANAGEMENT(2, "管理"),
    MARKETING(3, "市场"),
    SUPPORT(4, "支持");

    private final int value;
    private final String text;

    PositionTypeEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static PositionTypeEnum getEnumByValue(int value) {
        for (PositionTypeEnum type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }
}
