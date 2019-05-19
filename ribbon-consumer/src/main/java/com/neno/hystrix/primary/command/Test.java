package com.neno.hystrix.primary.command;

import com.neno.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: root
 * @Date: 2019/5/16 10:58
 */
public  class Test {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 通过UserCommand，我们既可以实现请求的同步执行也可以实现请求的异步执行
     */
    public void fun() {
        /**
         *请求同步执行
         */
        User user = new UserCommand(restTemplate, 1L).execute();
        /**
         *请求异步执行
         */
        Future<User> queueUser = new UserCommand(restTemplate, 1L).queue();
        try {
            User user1 = queueUser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 除了传统的同步执行与异步执行之外， 我们还可以将 HystrixCommand 通过
     * Observable 来实现响应式执行方式。通过调用 observe()和toObservable ()方法可
     * 以返回 Observable 对象
     * Observable 只能发射一次数据
     */
    public void fun2() {
        Observable<User> ho = new UserCommand(restTemplate, 1L).observe();

        Observable<User> co = new UserCommand(restTemplate, 1L).toObservable();
    }
}
