package ru.perm.v.camel.simple_kafka.producer.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EchoRestControllerSpringBootTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void postMessage() {
        String response = this.restTemplate.postForObject("http://localhost:" + port + "/api/echo/TEST_MESSAGE", null, String.class);

        assertEquals("TEST_MESSAGE", response);
    }

    @Test
    void postMessageForEmptyMessage() {
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/api/echo/", null, String.class);

        assertEquals(404, responseEntity.getStatusCode().value());
    }
}