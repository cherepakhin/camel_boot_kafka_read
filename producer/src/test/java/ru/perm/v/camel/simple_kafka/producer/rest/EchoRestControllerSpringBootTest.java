package ru.perm.v.camel.simple_kafka.producer.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EchoRestControllerSpringBootTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getMessage() {
        String response = this.restTemplate.getForObject("http://localhost:" + port + "/api/echo/TEST_MESSAGE", String.class, String.class);

        assertEquals("TEST_MESSAGE", response);
    }

    @Test
    void getMessageForEmptyMessage() {
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/api/echo/", String.class, String.class);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}