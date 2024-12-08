package ru.perm.v.camel.simple_kafka.producer.rest;

import org.apache.camel.Handler;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/producer")
public class CamelProducerRestController {

    @Produce(uri = "direct:echo")
    private ProducerTemplate producer;

    @Handler
    @GetMapping("/send")
    public String sendToKafka() {
        producer.sendBody("hello");
        return "OK";
    }
}
