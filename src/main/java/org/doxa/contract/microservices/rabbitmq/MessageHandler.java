package org.doxa.contract.microservices.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class MessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MessageHandler.class);

//    @RabbitListener(queues="sample_queue")
//    public void handleMessage(@Payload CreateCurrencyDto event) throws JsonProcessingException {
//        LOG.info("EventGetOutlook received" + event.toString());
//
//    }
}