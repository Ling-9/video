package com.fgsqw.iservice.redis;

public interface IRedisCacheService {

    void setString(String key, String value);

    void setString(String key, String value, int seconds);

    void setString(String key, String value, Long time);

    String getString(String key);

    void setObject(String key, Object value);

    Object getObject(String key);
}
