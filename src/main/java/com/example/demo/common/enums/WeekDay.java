package com.example.demo.common.enums;


public enum WeekDay {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    private final int code;

    WeekDay(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static WeekDay fromCode(int code) {
        for (WeekDay d : values()) {
            if (d.code == code) return d;
        }
        throw new IllegalArgumentException("Invalid WeekDay code: " + code);
    }
}
