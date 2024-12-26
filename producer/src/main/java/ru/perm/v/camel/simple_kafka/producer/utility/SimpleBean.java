package ru.perm.v.camel.simple_kafka.producer.utility;

import org.springframework.stereotype.Component;

@Component("simpleBean")
public class SimpleBean {
//    public String name ="NAME";

    public String getMessage() {
        return "Simple message";
    }

    public String name() {
        return "Simple message";
    }

}
