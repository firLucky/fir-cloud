package com.fir.gateway.singleton;


import org.springframework.data.redis.core.RedisTemplate;


/**
 * 双检锁/双重校验锁（DCL，即 double-checked locking) 单例模式
 * @author fir
 */
public class Singleton {

    public String fileUrl;

    /**
     * token请求头参数名称
     */
    public final String TOKEN_HEADER_NAME = "Authorization";

    /**
     * redis存储对象
     */
    public RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();


    private volatile static Singleton singleton;  
    private Singleton(){}
    public static Singleton getSingleton() {  
    if (singleton == null) {  
        synchronized (Singleton.class) {  
            if (singleton == null) {  
                singleton = new Singleton();  
            }  
        }  
    }  
    return singleton;  
    }  
}