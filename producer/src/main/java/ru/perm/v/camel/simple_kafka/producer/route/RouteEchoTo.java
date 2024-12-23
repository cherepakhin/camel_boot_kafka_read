package ru.perm.v.camel.simple_kafka.producer.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import ru.perm.v.camel.simple_kafka.producer.processor.MessageBodyLogger;

@Component
@RequiredArgsConstructor
public class RouteEchoTo  extends RouteBuilder {

    private final MessageBodyLogger messageBodyLogger;

    @Override
    public void configure() {
        from("direct:echoto").log("Echo body: ${body.name}");
    }
}
