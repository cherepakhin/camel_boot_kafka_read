package ru.perm.v.camel.simple_kafka.consumer.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EchoRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
// for send message to other route
//        from("direct:echo").routeId("echo").log(format("Message: %s", param)).to(otherRoute);
        from("direct:echo").routeId("echo").log("Body:${body}");
    }
}
