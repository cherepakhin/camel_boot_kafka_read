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
            throw new IllegalArgumentException("Message is empty");
        }
        return message;
    }
}
