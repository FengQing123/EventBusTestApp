package com.fengqing.buslibrary;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author fengqing
 * @date 2019/6/10
 */

public class EventBus {

    private static EventBus eventBus;

    private Map<Object, List<MethodManager>> map;
    /**
     * 定义一个线程服务对象
     */
    private ExecutorService mExecutorService;
    /**
     * 通过handler切换到主线程
     */
    private Handler mHandler;

    private EventBus() {
        map = new HashMap<>();
        mExecutorService = Executors.newCachedThreadPool();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static EventBus getEventBus() {
        if (eventBus == null) {
            synchronized (EventBus.class) {
                if (eventBus == null) {
                    eventBus = new EventBus();
                }
            }
        }
        return eventBus;
    }

    /**
     * 根据传进来的组件找到这个组件中所有的订阅方法
     *
     * @param object
     */
    public void register(Object object) {
        List<MethodManager> methodManagers = map.get(object);
        if (methodManagers == null) {
            methodManagers = findAnnotationMethod(object);
            map.put(object, methodManagers);
        }
    }

    /**
     * 通过传进来的组件寻找符合条件的订阅方法
     *
     * @param object
     * @return
     */
    private List<MethodManager> findAnnotationMethod(Object object) {
        List<MethodManager> methodList = new ArrayList<>();
        //获取组件的类对象
        Class<?> aClass = object.getClass();
        //获取组件的全部方法
        Method[] methods = aClass.getMethods();
        //遍历所有方法
        for (Method method : methods) {
            Subscribe annotation = method.getAnnotation(Subscribe.class);
            //如果注解为空就证明不是订阅方法
            if (annotation == null) {
                continue;
            }

            Type genericReturnType = method.getGenericReturnType();
            if (!genericReturnType.toString().equals("void")) {
                throw new RuntimeException("方法返回值必须是void");
            }

            //验证这个方法是不是只有一个接收参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException("方法必须有且只有一个接收参数");
            }

            //封装订阅方法
            MethodManager methodManager = new MethodManager(parameterTypes[0], annotation.threadMode(), method);
            methodList.add(methodManager);

        }
        return methodList;
    }


    /**
     * 发送消息的方法
     *
     * @param setter
     */
    public void post(final Object setter) {
        //获取到key的集合
        Set<Object> objects = map.keySet();
        //遍历key的集合
        for (final Object object : objects) {
            //通过key获取到订阅方法的集合
            List<MethodManager> methodManagers = map.get(object);
            if (methodManagers != null && methodManagers.size() > 0) {
                //遍历订阅方法的集合
                for (final MethodManager methodManager : methodManagers) {
                    //判断发布者发布的消息类型和订阅方法订阅的类型是否一致，一致则执行方法
                    if (methodManager.getType().isAssignableFrom(setter.getClass())) {
                        switch (methodManager.getThreadMode()) {
                            case BACKGROUND:
                                //判断当前线程是否是主线程
                                if (Looper.myLooper() == Looper.getMainLooper()) {
                                    //是主线程，切换到子线程
                                    mExecutorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke(methodManager, object, setter);
                                        }
                                    });
                                } else {
                                    //当前是子线程
                                    invoke(methodManager, object, setter);
                                }
                                break;
                            case MAIN:
                                //判断当前线程是否是主线程
                                if (Looper.myLooper() == Looper.getMainLooper()) {
                                    //当前是主线程
                                    invoke(methodManager, object, setter);
                                } else {
                                    //当前是子线程
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            invoke(methodManager, object, setter);
                                        }
                                    });
                                }
                                break;
                            case POSTING:
                                invoke(methodManager, object, setter);
                                break;
                            default:
                                invoke(methodManager, object, setter);
                                break;
                        }
                    }
                }
            }

        }
    }

    /**
     * 通过反射执行方法
     *
     * @param methodManager
     * @param object
     * @param setter
     */
    private void invoke(MethodManager methodManager, Object object, Object setter) {
        Method method = methodManager.getMethod();
        try {
            method.invoke(object, setter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销
     *
     * @param object
     */
    public void unregister(Object object) {
        if (object != null && map.get(object) != null) {
            map.remove(object);
        }
    }

}
