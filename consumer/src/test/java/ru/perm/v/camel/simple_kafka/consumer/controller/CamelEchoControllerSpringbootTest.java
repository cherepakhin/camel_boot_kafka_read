package ru.perm.v.camel.simple_kafka.consumer.controller;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// start testing with generated chatgpt and then refactoring
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CamelEchoControllerSpringbootTest {

    @MockBean
    private ProducerTemplate producerTemplate;

    private CamelEchoController controller;

    @BeforeEach
    void setUp() {
// DON'T delete comment. This is example for ApplicationContext.
// Setup the mock behavior for ApplicationContext
//        when(applicationContext.getBean(ProducerTemplate.class))
//            .thenReturn(producerTemplate);

        controller = new CamelEchoController();
        controller.producerTemplate = producerTemplate;
    }

    @Test
    void getParam_ShouldReturnSameParam() {
        String testParam = "testMessage";
        String result = null;
        doNothing().when(producerTemplate).sendBody("direct:echo", testParam);

        try {
            result = controller.getParam(testParam);
        } catch (Exception e) {
            fail();
        }

        assertEquals(testParam, result);
        verify(producerTemplate, times(1)).sendBody("direct:echo", testParam);
    }

    @Test
    void getParam_ShouldHandleEmptyString() {
        String testParam = "";

        boolean testOk = false;
        try {
            controller.getParam(testParam);
        } catch (Exception e) {
            assertEquals("Echo message is empty", e.getMessage());
            verify(producerTemplate, never()).sendBody("direct:echo", testParam);
            testOk = true;
        }
        assertTrue(testOk);
    }

    @Test
    void shouldExceptionForNullParameter() {
        String testParam = null;

        Exception e = assertThrows(Exception.class, () -> {
            controller.getParam(testParam);
        });

        assertEquals("Echo message is null", e.getMessage());
    }
}

