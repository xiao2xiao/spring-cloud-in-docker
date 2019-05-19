package com.neno.hystrix.annotation.command;

import com.neno.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * @Author: root
 * @Date: 2019/5/16 11:09
 */
@Service
public class UserObservableService {

    @Autowired
    private RestTemplate restTemplate;
    private Long id;

    /**
     * 在使用 @HystrixCommand 注解实现响应式命令时，
     * 可以通过 observableExecutionMode 参数来控制是使用 observe()(EAGER,hot data)还是toObservable()(LAZY,cold data)的执行方式。
     *
     * @param id
     * @return
     */
    //@HystrixCommand(observableExecutionMode = ObservableExecutionMode.LAZY)
    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER, fallbackMethod = "getUserByIdFailed")
    public Observable<User> getUserById(final Long id) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        User u = restTemplate.getForObject("", User.class, 1L);
                        observer.onNext(u);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io());
    }

    public User getUserByIdFailed(final Long id) {
        return new User("ERROR,,,,OVER", -1);
    }
}

