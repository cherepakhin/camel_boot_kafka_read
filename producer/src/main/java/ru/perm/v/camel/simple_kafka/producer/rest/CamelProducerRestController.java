package ru.perm.v.camel.simple_kafka.producer.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String send() throws ExecutionException, InterruptedException {
        CamelContext context = template.getCamelContext();
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody("Hello");
        return template.send("direct:echo", exchange).getMessage().getBody(String.class);
    }

    @GetMapping("/sendMessageDto_template")
    public String sendMessageDtoWithTemplate(MessageDTO dto) throws ExecutionException, InterruptedException {
        CamelContext context = template.getCamelContext();
        Exchange exchange = new DefaultExchange(context);

        try {
            exchange.getIn().setBody(mapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return template.send("direct:messages", exchange).getMessage().getBody(String.class);
    }

    @GetMapping("/sendMessageDto_route")
    public String sendMessageDto() throws ExecutionException, InterruptedException {
        MessageDTO dto = new MessageDTO();
        routeMessagesToKafka.sendDto(dto);
        return dto.toString();
    }

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
