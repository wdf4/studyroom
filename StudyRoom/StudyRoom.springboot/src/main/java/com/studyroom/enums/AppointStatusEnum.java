package com.studyroom.enums;

import java.util.HashMap;

/**
 * 预约状态枚举
 */
public enum AppointStatusEnum {
    /**
     * 待使用
     */
    待使用(1),

    /**
     * 使用中
     */
    使用中(2),

    /**
     * 预约完成
     */
    预约完成(3),

    /**
     * 用户取消
     */
    用户取消(4),

    /**
     * 逾期取消
     */
    逾期取消(5),

    /**
     * 待评论
     */
    待评论(6);


    private final int index;

    AppointStatusEnum(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }

    private static final HashMap<Integer, AppointStatusEnum> MY_MAP = new HashMap<Integer, AppointStatusEnum>();

    static {
        for (AppointStatusEnum myEnum : values()) {
            MY_MAP.put(myEnum.index(), myEnum);
        }
    }

    public static AppointStatusEnum GetEnum(Integer v) {
        if (v == null) {
            return MY_MAP.values().stream().findFirst().get();
        }
        return MY_MAP.get(v);
    }

    }
