package ru.perm.v.camel.simple_kafka.consumer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestEchoCtrlTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void echoRestShouldReturnSended_CONTAINS_Message() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/fortest/echo/MESSAGE",
                String.class)).contains("MESSAGE");
    }

    @Test
    void echoRestShouldReturnSended_EQUAL_Message() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/fortest/echo/MESSAGE",
                String.class)).isEqualTo("MESSAGE");
    }
}
