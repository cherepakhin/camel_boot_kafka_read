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
@RequestMapping("/api/fortest/")
public class CamelTestController {
    @Autowired
    ApplicationContext context;

    Logger logger = Logger.getLogger(CamelTestController.class.getName());

    @GetMapping("/echo/{param}")
    public String getParam(@PathVariable String param) {
        logger.info(format("Get param: %s", param));
        ProducerTemplate template = context.getBean(ProducerTemplate.class);
        template.sendBody("direct:echo", param);
        return param;
    }
}
