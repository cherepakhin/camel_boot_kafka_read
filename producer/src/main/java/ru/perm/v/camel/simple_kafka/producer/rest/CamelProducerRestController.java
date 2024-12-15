package ru.perm.v.camel.simple_kafka.producer.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.perm.v.camel.simple_kafka.producer.dto.MessageDTO;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/producer")
public class CamelProducerRestController {

    @Produce(uri = "direct:echo")
    private ProducerTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(CamelProducerRestController.class);

    ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/send")
    public String send() throws ExecutionException, InterruptedException {
        CamelContext context = template.getCamelContext();
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody("Hello");
        return template.send("direct:echo", exchange).getMessage().getBody(String.class);
    }

    @GetMapping("/sendMessageDto")
    public String sendMessageDto(MessageDTO dto) throws ExecutionException, InterruptedException {
        CamelContext context = template.getCamelContext();
        Exchange exchange = new DefaultExchange(context);

//        exchange.getIn().setBody(mapper.readValue(dto));
        return template.send("direct:messages", exchange).getMessage().getBody(String.class);
    }
}
