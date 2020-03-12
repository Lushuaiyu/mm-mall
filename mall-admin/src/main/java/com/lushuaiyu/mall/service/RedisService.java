package com.lushuaiyu.mall.service;

import javax.validation.constraints.NotNull;

/**
 * @author lushuaiyu
 */
public interface RedisService {
    /**
     * 给 redis 设置值
     * @param key
     * @param token
     * @param time
     */
    void set(String key, String token, long time);

    /**
     * 从 redis 里 取值
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * redis  key 是否过期
     * @param key
     * @return 过期返回 false 没过期返回 true
     */
    boolean hasKey(String key);


    /**
     * 查看某个 key 的剩余生存时间: time to live
     * @param key
     * @return 当 key 不存在或没有设置生存时间时，返回 -1 。否则，返回 key 的剩余生存时间(以秒为单位)。
     */
    Long getExpire( String key);


}
