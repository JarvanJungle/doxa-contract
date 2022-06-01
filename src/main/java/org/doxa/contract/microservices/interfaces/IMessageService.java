package org.doxa.contract.microservices.interfaces;

public interface IMessageService {
    public void sendToEmailQueue(Object event);

    public void sendToUserUpdatedExchange(Object event);
}
