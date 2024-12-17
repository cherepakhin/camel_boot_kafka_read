package ru.perm.v.camel.simple_kafka.producer.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;
import ru.perm.v.camel.simple_kafka.producer.route.RouteMessagesToKafka;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/producer")
public class CamelProducerRestController {

    @Produce(uri = "direct:echo")
    private ProducerTemplate template;

    @Autowired
    RouteMessagesToKafka routeMessagesToKafka;

    private static final Logger logger = LoggerFactory.getLogger(CamelProducerRestController.class);

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/send")
    @Operation(summary = "Send a 'Hello' message", description = "Send a simple 'Hello' message")
    @ApiResponse(responseCode = "200", description = "Message sent successfully")
    public String send() throws ExecutionException, InterruptedException {
        CamelContext context = template.getCamelContext();
        Exchange exchange = new DefaultExchange(context);
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

    @GetMapping("/sendMessageDto_route")
    public String sendMessageDto() throws ExecutionException, InterruptedException {
        MessageDTO dto = new MessageDTO();
        routeMessagesToKafka.sendDto(dto);
        return dto.toString();
    }

    //TODO: validate
    @GetMapping("/sendManyMessageDto/{count}")
    public Integer sendMessageManyDto(@PathVariable("count") Integer count) throws ExecutionException, InterruptedException {
        for (int i = 0; i < count; i++) {
            MessageDTO dto = new MessageDTO();
            dto.setName("NAME_" + i);
            dto.setDescription("DESCRIPTION_" + i);
            routeMessagesToKafka.sendDto(dto);
        }
        return count;
    }
}
