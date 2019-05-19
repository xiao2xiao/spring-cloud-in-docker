package com.neno.service;

import com.google.common.base.Joiner;
import com.neno.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Author: root
 * @Date: 2019/5/16 15:09
 */
@Service
public class UserService {
    @Autowired
    private RestTemplate restTemplate;

    public User find(Long id) {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
    }

    public List<User> findAll(List<Long> ids) {
        return restTemplate.getForObject("http://USER-SERVICE/users/ids={1}", List.class, Joiner.on(",").join(ids));
    }
}
