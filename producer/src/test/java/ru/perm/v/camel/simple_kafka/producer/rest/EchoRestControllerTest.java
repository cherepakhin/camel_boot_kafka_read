package ru.perm.v.camel.simple_kafka.producer.rest;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EchoRestControllerTest {

    @Test
    void getMessage() {
        EchoRestController controllerTest = new EchoRestController();
        String message = controllerTest.getMessage("test");

        assertEquals("test", message);
    }

    @Test
    void getForEmptyMessageCheckClassException() {
        EchoRestController controllerTest = new EchoRestController();
        assertThrows(Err502Exception.class, () -> controllerTest.getMessage(""));
    }

    @Test
    void getForEmptyMessageCheckExceptionMessage() {
        EchoRestController controllerTest = new EchoRestController();
        boolean exception = false;

        try {
            controllerTest.getMessage("");
        } catch (Exception e) {
            assertEquals(Err502Exception.class, e.getClass());
            assertEquals("Message is empty", e.getMessage());
            exception = true;
        }

        assertTrue(exception);
    }

    @Test
    void getForLongMessageCheckExceptionMessage() {
        EchoRestController controllerTest = new EchoRestController();
        boolean exception = false;
        // build long message
        AtomicInteger current = new AtomicInteger();
        String longString = Stream.generate(() -> String.valueOf(current.getAndIncrement())).limit(101).collect(Collectors.joining());

        try {
            controllerTest.getMessage(longString);
        } catch (Exception e) {
            assertEquals(Err502Exception.class, e.getClass());
            assertEquals("Size of message is greater than 100", e.getMessage());
            exception = true;
        }

        assertTrue(exception);
    }
}