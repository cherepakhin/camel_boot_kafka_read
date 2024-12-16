package ru.perm.v.camel.simple_kafka.consumer.controller;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/echo/{message}")
    public String getParam(@PathVariable String message) throws Exception {
        if (message == null) {
            String errorMessage = "Echo message is null";
            logger.severe(errorMessage);
            throw new Exception(errorMessage);
        }
        if (message.isEmpty()) {
            String errorMessage = "Echo message is empty";
            logger.severe(errorMessage);
            throw new Exception(errorMessage);
        }

        logger.info(format("Get param: %s", message));

        // send to ru.perm.v.camel.simple_kafka.consumer.route.EchoRoute.java
        producerTemplate.sendBody("direct:echo", message);

        return message;
    }
}
