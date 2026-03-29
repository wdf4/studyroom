package com.studyroom.enums;

import java.util.HashMap;

public enum RoleTypeEnum
{
    /**
     * 管理员
     */
    管理员(1),

    /**
     * 用户
     */
    用户(2);


    private final int index;

    RoleTypeEnum(int index)
    {
        this.index = index;
    }

    public int index() {
        return index;
    }


    private static final HashMap<Integer, RoleTypeEnum> MY_MAP = new HashMap<Integer, RoleTypeEnum>();
    static {
        for (RoleTypeEnum myEnum : values()) {
            MY_MAP.put(myEnum.index(), myEnum);
        }
    }

    public String toString() {

        return name();
    }
    public static RoleTypeEnum GetEnum(Integer v)
    {
        if(v==null){
            return MY_MAP.values().stream().findFirst().get();
        }
        return MY_MAP.get(v);
    }
}
