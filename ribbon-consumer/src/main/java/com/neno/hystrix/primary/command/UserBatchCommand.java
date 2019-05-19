package com.neno.hystrix.primary.command;

import com.neno.model.User;
import com.neno.service.UserService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.List;

/**
 * @Author: root
 * @Date: 2019/5/16 15:17
 * <p>
 * 请求批处理命令
 */
public class UserBatchCommand extends HystrixCommand<List<User>> {

    private UserService userService;
    private List<Long> userIds;

    public UserBatchCommand(UserService userService, List<Long> userIds) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserBatchCommand")));
        this.userIds = userIds;
        this.userService = userService;
    }


    @Override
    protected List<User> run() throws Exception {
        return userService.findAll(userIds);
    }
}
