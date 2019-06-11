package com.fengqing.buslibrary;

import java.lang.reflect.Method;

/**
 * @author fengqing
 * @date 2019/6/10
 */

class MethodManager {

    /**
     * 方法接收的成员变量的类型
     */
    private Class<?> type;

    /**
     * 方法上面注解的值
     */
    private ThreadMode threadMode;

    /**
     * 方法本身
     */
    private Method method;

    public MethodManager(Class<?> type, ThreadMode threadMode, Method method) {
        this.type = type;
        this.threadMode = threadMode;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
