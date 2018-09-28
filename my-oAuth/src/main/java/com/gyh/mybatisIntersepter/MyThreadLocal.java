package com.gyh.mybatisIntersepter;

public class MyThreadLocal {

    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();

    public static ThreadLocal<Object> getThreadLocal() {
        return threadLocal;
    }

    public String get(){
        return threadLocal.get().toString();
    }
    public void set(String tend){
        threadLocal.set(tend);
    }
}
