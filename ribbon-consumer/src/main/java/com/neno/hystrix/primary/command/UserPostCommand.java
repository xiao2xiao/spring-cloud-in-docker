package com.neno.hystrix.primary.command;

import com.neno.model.User;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: root
 * @Date: 2019/5/15 19:32
 */
public class UserPostCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;
    private User user;

    protected UserPostCommand(RestTemplate restTemplate, User user) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserGroupKey"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("UserCommandKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserThreadPoolKey")));
        this.restTemplate = restTemplate;
        this.user = user;
    }

    @Override
    protected User run() throws Exception {
        User r = restTemplate.postForObject("http://HELLO-SERVICE/users/post", user, User.class);
        /**
         *清除缓存
         */
        UserCommand.flushCache(user.getId());
        return r;
    }
}


