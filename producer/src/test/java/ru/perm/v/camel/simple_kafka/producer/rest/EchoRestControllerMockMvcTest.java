package ru.perm.v.camel.simple_kafka.producer.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EchoRestControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postMessage() throws Exception {
        String TEST_MESSAGE="TEST_MESSAGE";
        this.mockMvc.perform(post("/api/echo/"+TEST_MESSAGE)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("TEST_MESSAGE")));
    }

    @Test
    void postMessageWithMvcResultStatusIsOk() throws Exception {
        String TEST_MESSAGE="TEST_MESSAGE";

        MvcResult mvcResult = this.mockMvc.perform(get("/api/echo/" + TEST_MESSAGE)).andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void postMessageWithMvcBodyTest() throws Exception {
        String TEST_MESSAGE="TEST_MESSAGE";

        MvcResult mvcResult = this.mockMvc.perform(post("/api/echo/" + TEST_MESSAGE)).andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(TEST_MESSAGE, result);
    }
}