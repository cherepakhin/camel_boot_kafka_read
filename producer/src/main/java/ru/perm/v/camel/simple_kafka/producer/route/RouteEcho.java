package ru.perm.v.camel.simple_kafka.producer.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import ru.perm.v.camel.simple_kafka.producer.processor.MessageBodyLogger;

@Component
@RequiredArgsConstructor
public class RouteEcho extends RouteBuilder {
    @Produce(uri = "direct:echo")
    private ProducerTemplate producer;

    private final MessageBodyLogger messageBodyLogger;

    @Override
    public void configure() throws Exception {
        from("direct:echo").process(messageBodyLogger)
                .to("direct:end");

    }
}
