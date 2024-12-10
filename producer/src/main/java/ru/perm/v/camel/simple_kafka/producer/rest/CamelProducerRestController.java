package ru.perm.v.camel.simple_kafka.producer.rest;

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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/producer")
public class CamelProducerRestController {

    @Produce(uri = "direct:echo")
    private ProducerTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(CamelProducerRestController.class);

    @GetMapping("/send")
    public String sendMessage() throws ExecutionException, InterruptedException {
        CamelContext context = template.getCamelContext();
        Exchange exchange = new DefaultExchange(context);
        exchange.getIn().setBody("Hello");
        return template.send("direct:echo", exchange).getMessage().getBody(String.class);
    }
}
