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

    @Test
    void postForEmptyMessageCheckException() {
        EchoRestController controllerTest = new EchoRestController();
        assertThrows(IllegalArgumentException.class, () -> controllerTest.postMessage(""));
    }

    @Test
    void postForEmptyMessageCheckExceptionMessage() {
        EchoRestController controllerTest = new EchoRestController();
        boolean exception = false;

        try {
            controllerTest.postMessage("");
        } catch (IllegalArgumentException e) {
            assertEquals("Message is empty", e.getMessage());
            exception = true;
        }

        assertTrue(exception);
    }
}