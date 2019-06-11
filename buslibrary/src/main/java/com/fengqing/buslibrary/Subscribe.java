package com.fengqing.buslibrary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fengqing
 * @date 2019/6/10
 */
@Target(ElementType.METHOD)//表示注解的存在方式,有类，方法-METHOD
@Retention(RetentionPolicy.RUNTIME)//表示这个注解的生命周期，源码期-SOURCE  编译期-class，运行期-RUNTIME
public @interface Subscribe {
    ThreadMode threadMode() default ThreadMode.POSTING;
}

