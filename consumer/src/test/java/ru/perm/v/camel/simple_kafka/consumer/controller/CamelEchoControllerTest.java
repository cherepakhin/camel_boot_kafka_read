package ru.perm.v.camel.simple_kafka.consumer.controller;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CamelEchoControllerTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ProducerTemplate producerTemplate;

    private CamelEchoController controller;

    @BeforeEach
    void setUp() {
        // Setup the mock behavior for ApplicationContext
        when(applicationContext.getBean(ProducerTemplate.class))
            .thenReturn(producerTemplate);
        
        controller = new CamelEchoController();
    }

    @Test
    void getParam_ShouldReturnSameParam() {
        // Arrange
        String testParam = "testMessage";

        // Act
        String result = controller.getParam(testParam);

        // Assert
        assertEquals(testParam, result);
        verify(producerTemplate).sendBody("direct:echo", testParam);
    }

    @Test
    void getParam_ShouldHandleEmptyString() {
        // Arrange
        String testParam = "";

        when(applicationContext.getBean(ProducerTemplate.class))
                .thenReturn(producerTemplate);

        controller = new CamelEchoController();

        // Act
        String result = controller.getParam(testParam);

        // Assert
        assertEquals(testParam, result);
        verify(producerTemplate).sendBody("direct:echo", testParam);
    }

    @Test
    void getParam_ShouldHandleSpecialCharacters() {
        // Arrange
        String testParam = "test@#$%^&*()";

        // Act
        String result = controller.getParam(testParam);

        // Assert
        assertEquals(testParam, result);
        verify(producerTemplate).sendBody("direct:echo", testParam);
    }

    @Test
    void getParam_ShouldHandleNullParameter() {
        // Arrange
        String testParam = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            controller.getParam(testParam);
        });
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

