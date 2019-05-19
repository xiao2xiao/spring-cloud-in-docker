package com.neno.hystrix.primary.command;

import com.neno.model.User;
import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @Author: root
 * @Date: 2019/5/15 20:27
 */

/**
 * 如果使用 HystrixObservableCommand 来实现命令封装，需要将命令的执行逻辑在
 * construct 方法中重载，这样 Hystrix 才能将具体逻辑包装到 Observable 内
 */
public class UserObservableCommand extends HystrixObservableCommand<User> {

    private RestTemplate restTemplate;
    private Long id;

    protected UserObservableCommand(RestTemplate restTemplate, Long id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserGroupKey"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("UserCommandKey")));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected Observable<User> construct() {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        User user = null;
                        observer.onNext(user);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 降级
     *
     * @return
     */
    @Override
    protected Observable<User> resumeWithFallback() {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        User user = new User("ERROR,,,,OVER", -1);
                        observer.onNext(user);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
