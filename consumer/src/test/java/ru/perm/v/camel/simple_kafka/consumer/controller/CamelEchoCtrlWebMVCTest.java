package ru.perm.v.camel.simple_kafka.consumer.controller;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CamelEchoController.class)
public class CamelEchoCtrlWebMVCTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProducerTemplate producerTemplate;

    @Test
    void getParam() throws Exception {
        String MESSAGE = "MESSAGE";
        doNothing().when(producerTemplate).sendBody("direct:echo", MESSAGE);

        this.mockMvc.perform(get("/api/fortest/echo/" + MESSAGE)
                .contentType("application/text")).andDo(print()).andExpect(status().isOk());

        verify(producerTemplate, times(1))
                .sendBody("direct:echo", MESSAGE);
    }
}
