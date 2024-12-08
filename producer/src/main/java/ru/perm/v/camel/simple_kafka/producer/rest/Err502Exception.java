package ru.perm.v.camel.simple_kafka.producer.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class Err502Exception extends RuntimeException {
    public Err502Exception(String message) {
        super(message);
    }
}
