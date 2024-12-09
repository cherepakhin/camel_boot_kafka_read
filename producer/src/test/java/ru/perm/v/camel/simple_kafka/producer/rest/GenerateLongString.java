package ru.perm.v.camel.simple_kafka.producer.rest;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateLongString {
    @Test
    void generate() {
        AtomicInteger current = new AtomicInteger();
        String newString = Stream.generate(() -> String.valueOf(current.getAndIncrement())).limit(10).collect(Collectors.joining());

        assertEquals("0123456789", newString);
    }
}
