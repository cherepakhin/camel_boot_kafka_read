package ru.perm.v.camel.simple_kafka.consumer.controller;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// generated chatgpt
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CamelEchoControllerTest {

    @Mock
    private ApplicationContext applicationContext;

    @MockBean
    private ProducerTemplate producerTemplate;

    private CamelEchoController controller;

    @BeforeEach
    void setUp() {
        // Setup the mock behavior for ApplicationContext
//        when(applicationContext.getBean(ProducerTemplate.class))
//            .thenReturn(producerTemplate);
        
        controller = new CamelEchoController();
        controller.producerTemplate = producerTemplate;
    }

    @Test
    void getParam_ShouldReturnSameParam() {
        // Arrange
        String testParam = "testMessage";

        // Act
        String result = null;
        try {
            result = controller.getParam(testParam);
        } catch (Exception e) {
            fail();
        }

        // Assert
        assertEquals(testParam, result);
        verify(producerTemplate).sendBody("direct:echo", testParam);
    }

    @Test
    void getParam_ShouldHandleEmptyString() {
        // Arrange
        String testParam = "";

        // Act
        String result = null;
        try {
            controller.getParam(testParam);
        } catch (Exception e) {
            assertEquals("", e.getMessage());
            verify(producerTemplate).sendBody("direct:echo", testParam);
        }
        fail();
    }

//    @Test
//    void getParam_ShouldHandleSpecialCharacters() {
//        // Arrange
//        String testParam = "test@#$%^&*()";
//
//        // Act
//        String result = controller.getParam(testParam);
//
//        // Assert
//        assertEquals(testParam, result);
//        verify(producerTemplate).sendBody("direct:echo", testParam);
//    }

    @Test
    void shouldBxceptionForNullParameter() {
        String testParam = null;

        Exception e = assertThrows(Exception.class, () -> {
            controller.getParam(testParam);
        });

        assertEquals("Echo message is null", e.getMessage());
    }

    @Test
    void getParam_ShouldHandleProducerTemplateException() {
        // Arrange
        String testParam = "testMessage";
        doThrow(new RuntimeException("Camel route error"))
            .when(producerTemplate)
            .sendBody("direct:echo", testParam);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            controller.getParam(testParam);
        });
    }
}

