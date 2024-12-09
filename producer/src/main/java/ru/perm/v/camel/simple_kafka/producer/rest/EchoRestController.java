package ru.perm.v.camel.simple_kafka.producer.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * For checking the REST API
 */
@RestController
@RequestMapping("/api/echo")
public class EchoRestController {
    @PostMapping("/{message}")
    public String postMessage(@PathVariable String message) {
        if (message == null || message.isEmpty()) {
            throw new Err502Exception("Message is empty");
        }
        // for example length check
        if (message.length() > 100) {
            throw new Err502Exception("Size of message is greater than 100");
        }
        return message;
    }
}
