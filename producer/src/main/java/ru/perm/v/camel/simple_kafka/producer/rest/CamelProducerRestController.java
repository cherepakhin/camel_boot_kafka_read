package ru.perm.v.camel.simple_kafka.producer.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.producer.route.RouteMessagesToKafka;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/producer")
@Validated
public class CamelProducerRestController {

    @Produce(uri = "direct:echo")
    private ProducerTemplate template;

    @Autowired
    RouteMessagesToKafka routeMessagesToKafka;

    private static final Logger logger = LoggerFactory.getLogger(CamelProducerRestController.class);

    ObjectMapper mapper = new ObjectMapper();
    Exchange exchange;

    @GetMapping("/send")
    @Operation(summary = "Send a 'Hello' message", description = "Send a simple 'Hello' message")
    @ApiResponse(responseCode = "200", description = "Message sent successfully")
    public String send() throws ExecutionException, InterruptedException {
        CamelContext context = template.getCamelContext();
        exchange = new DefaultExchange(context);
        exchange.getIn().setBody("Hello");
        return template.send("direct:echo", exchange).getMessage().getBody(String.class);
    }

    //TODO: validate
    @GetMapping("/sendMessageDto_template")
    public ResponseEntity<MessageDTO> sendMessageDtoWithTemplate(MessageDTO dto) throws ExecutionException, InterruptedException, JsonProcessingException {
        logger.info("{}", dto);
        CamelContext context = template.getCamelContext();
        Exchange exchange = new DefaultExchange(context);

        try {
            exchange.getIn().setBody(mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Object body = template.send("direct:messages", exchange);
        logger.info("=======================================");
        logger.info(body.toString());
        MessageDTO receivedDto = mapper.readValue(body.toString(), MessageDTO.class);
        return new ResponseEntity<MessageDTO>(receivedDto, HttpStatus.OK);
    }

    // For send: "http :8081/api/producer/sendMessageDto_route"
    @GetMapping("/sendMessageDto_route")
    public String sendMessageDto() throws ExecutionException, InterruptedException {
        MessageDTO dto = new MessageDTO();
        routeMessagesToKafka.sendDto(dto);
        return dto.toString();
    }

    // For send: "http :8081/api/producer/sendManyMessageDto/12"
    @GetMapping("/sendManyMessageDto/{count}")
    public ResponseEntity<?> sendMessageManyDto(@PathVariable("count") Integer count) {
        if(count< 1) {
            return new ResponseEntity<>("The \"count\" must be more than 0", HttpStatus.BAD_GATEWAY);
        }
        for (int i = 0; i < count; i++) {
            MessageDTO dto = new MessageDTO();
            dto.setName("NAME_" + i);
            dto.setDescription("DESCRIPTION_" + i);
            routeMessagesToKafka.sendDto(dto);
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/stopSheduler")
    public ResponseEntity<String> stopSheduler() {
        logger.info("=======================================");
        logger.info("Stop sheduler");
        if (exchange != null) {
            logger.info("Exchange not null");
            if (!exchange.getContext().isStopped()) {
                exchange.getContext().start();
                return new ResponseEntity<>("Exchange stopped", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Exchange NOT started", HttpStatus.BAD_GATEWAY);
            }
        } else {
            logger.info("Exchange is NULL");
            return new ResponseEntity<>("Exchange is NULL", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/startSheduler")
    public String startSheduler() {
        logger.info("=======================================");
        logger.info("Start sheduler");
        if (exchange != null) {
            logger.info("Exchange not null");
            if (!exchange.getContext().isStopped()) {
                exchange.getContext().start();
                return "Exchange started";
            } else {
                return "Exchange ALREADY started";
            }
        } else {
            logger.info("Exchange is NULL");
            return "Exchange is NULL";
        }
    }

}
