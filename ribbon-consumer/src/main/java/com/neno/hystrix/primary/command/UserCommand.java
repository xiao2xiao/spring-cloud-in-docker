package com.neno.hystrix.primary.command;

import com.neno.model.User;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: root
 * @Date: 2019/5/15 19:32
 */
public class UserCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;
    private Long id;

    protected UserCommand(RestTemplate restTemplate, Long id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserGroupKey"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("UserCommandKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserThreadPoolKey")));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected User run() throws Exception {
        return restTemplate.getForObject("http://HELLO-SERVICE/users/{1}", User.class, id);
    }

    /**
     * 降级
     *
     * @return
     */
    @Override
    protected User getFallback() {
        return new User("ERROR", -1);
    }

    /**
     * 缓存
     *
     * @return
     */
    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    /**
     * 清除缓存
     *
     * @param id
     */
    public static void flushCache(Long id) {
        HystrixRequestCache.getInstance(HystrixCommandKey.Factory.asKey("UserCommandKey"),
                HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }
}


