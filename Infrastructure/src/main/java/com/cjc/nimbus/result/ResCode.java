package com.cjc.nimbus.result;

/**
 * 错误码枚举接口，所有错误码枚举都应该实现此接口，模块内部定义自己模块的错误码
 * @author xiaogang
 */
public interface ResCode {

    int getCode();

    String getDesc();

    static <T extends ResCode> String getValue(int key, Class<T> enumClass) {
        T t = null;
        for (T each : enumClass.getEnumConstants()) {
            if (each.getCode() == key) {
                t = each;
                break;
            }
        }
        return null != t ? t.getDesc() : null;
    }
}
