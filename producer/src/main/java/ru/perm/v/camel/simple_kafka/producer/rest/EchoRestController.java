package ru.perm.v.camel.simple_kafka.producer.rest;

import org.springframework.web.bind.annotation.*;

/**
 * For checking the REST API
 * $ http POST :8081/api/echo/ANY_MESSAGE
 */
@RestController
@RequestMapping("/api/echo")
public class EchoRestController {
    @GetMapping("/{message}")
    public String getMessage(@PathVariable String message) {
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
