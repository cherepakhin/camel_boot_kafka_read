package ru.perm.v.camel.simple_kafka.consumer.controller;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/fortest")
public class CamelEchoController {

    @Autowired
    ProducerTemplate producerTemplate;

    Logger logger = Logger.getLogger(CamelEchoController.class.getName());

    @GetMapping("/echo/{param}")
    public String getParam(@PathVariable String param) {
        logger.info(format("Get param: %s", param));

        // send to ru.perm.v.camel.simple_kafka.consumer.route.EchoRoute.java
        producerTemplate.sendBody("direct:echo", param);

        return param;
    }
}
