package ru.perm.v.camel.simple_kafka.consumer.controller;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.perm.v.camel.simple_kafka.consumer.processor.MyMessageDatasourceProcessor;
import ru.perm.v.camel.simple_kafka.consumer.route.MessageInformationConsumptionRoute;

import java.util.logging.Logger;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/fortest/")
public class ForTestController {
    @Autowired
    MessageInformationConsumptionRoute route;
    @Autowired
    private MyMessageDatasourceProcessor myProcessor;

    @Autowired
    ApplicationContext context;

    Logger logger = Logger.getLogger(ForTestController.class.getName());

    @GetMapping("/echo/{param}")
    public String getParam(@PathVariable String param) {
        logger.info(format("Get param: %s", param));
        logger.info(format("MessageInformationConsumptionRoute: %s", route));
//        MessageInformationConsumptionRoute.toString():
//          MessageInformationConsumptionRoute{
//              topic='camel-integration',
//              kafkaConfigurationProperties=
//                  KafkaConfigurationProperties(topicName=camel-integration),
//                  myMessageBodyLogger=ru.perm.v.camel.simple_kafka.consumer.processor.MyMessageBodyLogger@2e03bb74,
//                  myProcessor=ru.perm.v.camel.simple_kafka.consumer.processor.MyMessageDatasourceProcessor@1ac71b87
//          }


//        CamelContext camelContext=(CamelContext) context.getBean("mainCamelContext");
//        logger.info(format("camelContext: %s", camelContext));

        return param;
    }
}
