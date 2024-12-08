package ru.perm.v.camel.simple_kafka.producer.rest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EchoRestControllerTest {

    @Test
    void postMessage() {
        EchoRestController controllerTest = new EchoRestController();
        String message = controllerTest.postMessage("test");

        assertEquals("test", message);
    }
}