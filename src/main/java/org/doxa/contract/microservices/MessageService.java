package org.doxa.contract.microservices;

import lombok.extern.slf4j.Slf4j;
import org.doxa.contract.microservices.interfaces.IMessageService;
import org.doxa.contract.microservices.rabbitmq.MessageSender;
import org.doxa.contract.microservices.rabbitmq.Queues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService implements IMessageService {
    @Autowired
    MessageSender messageSender;

    @Override
    public void sendToEmailQueue(Object event) {
        try{
            messageSender.sendMessage(Queues.SENDING_EMAIL, event);
        } catch (Exception e) {
            log.error("Unable to execute command: sendToEmailQueue" );
            e.printStackTrace();
        }
    }

    @Override
    public void sendToUserUpdatedExchange(Object event) {

    }
}
