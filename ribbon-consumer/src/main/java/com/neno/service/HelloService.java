package com.neno.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: root
 * @Date: 2019/5/15 18:37
 */
@Service
public class HelloService {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallback")
    public String hello() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class);
        String result = forEntity.getBody();
        return result;
    }

    public String helloFallback() {
        return "error";
    }
}
