package com.neno.hystrix.annotation.command;

import com.neno.model.User;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * @Author: root
 * @Date: 2019/5/15 19:56
 */
@Service
public class UserService2 {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 同步返回
     *
     * @param id
     * @return
     */
    @CacheResult(cacheKeyMethod = "getById")
    @HystrixCommand(groupKey = "UserGroup", commandKey = "UserCommandKey", threadPoolKey = "UserThreadPoolKey", ignoreExceptions = {HystrixBadRequestException.class, RuntimeException.class}, fallbackMethod = "defaultUserAa")
    public User getUserById(Long id) {
        User user = restTemplate.getForObject("http://HELLO_SERVICE/users/{1}", User.class, id);
        if (user == null) {
            throw new RuntimeException("Failed error......");
        }
        return user;
    }

    public Long getById(Long id) {
        return id;
    }

    @CacheRemove(commandKey = "getUserById", cacheKeyMethod = "getById")
    @HystrixCommand(groupKey = "UserGroup", commandKey = "UserCommandKey", threadPoolKey = "UserThreadPoolKey", ignoreExceptions = {HystrixBadRequestException.class, RuntimeException.class}, fallbackMethod = "defaultUserAa")
    public void update(User user) {
        restTemplate.postForObject("", user, User.class);
    }

    @HystrixCommand(fallbackMethod = "defaultUserBb")
    public User defaultUserAa(Throwable e) {
        /**
         * 其它逻辑处理
         */
        System.out.println(e.getMessage());
        return null;
    }

    public User defaultUserBb() {
        return null;
    }

    /**
     * 异步返回
     *
     * @param id
     * @return
     */
    @HystrixCommand
    public Future<User> getUserByIdAsync(final Long id) {
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                return restTemplate.getForObject("http://HELLO_SERVICE/users/{1}", User.class, id);
            }
//可能报错
//            @Override
//            public User get() {
//                return invoke();
//            }
        };
    }
}
