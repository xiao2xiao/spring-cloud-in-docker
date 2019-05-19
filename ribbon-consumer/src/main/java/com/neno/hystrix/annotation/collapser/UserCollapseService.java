package com.neno.hystrix.annotation.collapser;

import com.google.common.base.Joiner;
import com.neno.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Author: root
 * @Date: 2019/5/16 15:45
 */
@Service
public class UserCollapseService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCollapser(batchMethod = "findAll", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
    })
    public User find(Long id) {
        return null;
    }

    @HystrixCommand
//            (commandProperties = {
//            @HystrixProperty(name = "",value = "")
//    },threadPoolProperties = {
//            @HystrixProperty(name="",value = ""),
//            @HystrixProperty(name="coreSize",value = "20")
//    })
    public List<User> findAll(List<Long> ids) {
        return restTemplate.getForObject("http://USER-SERVICE/users/ids={1}", List.class, Joiner.on(",").join(ids));
    }

}
