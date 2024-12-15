package ru.perm.v.camel.simple_kafka.producer.utility;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class JsonDataFormatter {

    public static JacksonDataFormat get(final Class<?> T) {
        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(T);
        jsonDataFormat.addModule(new JavaTimeModule());
        return jsonDataFormat;
    }
}
