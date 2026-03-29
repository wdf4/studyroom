package com.studyroom.enums;

import java.util.HashMap;

/**
 * 预约时间段枚举
 */
public enum AppointDateTypeEnum {

    /**
     * 上午
     */
    上午(1),

    /**
     * 下午
     */
    下午(2),

    /**
     * 夜晚
     */
    夜晚(3);




    private final int index;

    AppointDateTypeEnum(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }

    private static final HashMap<Integer, AppointDateTypeEnum> MY_MAP = new HashMap<Integer, AppointDateTypeEnum>();

    static {
        for (AppointDateTypeEnum myEnum : values()) {
            MY_MAP.put(myEnum.index(), myEnum);
        }
    }

    public static AppointDateTypeEnum GetEnum(Integer v) {
        if (v == null) {
            return MY_MAP.values().stream().findFirst().get();
        }
        return MY_MAP.get(v);
    }
}
