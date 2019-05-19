package com.neno.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @Author: root
 * @Date: 2019/5/15 15:32
 */
@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Value("${server.port}")
    private int port;

    @RequestMapping("hello")
    public String hello() {
        Random random = new Random();
        int m = random.nextInt(20);
        LOGGER.info("m = " + m + " ,port = " + port);
        return "success m = " + m + " , port = " + port;
    }

}
