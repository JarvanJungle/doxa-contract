package org.doxa.contract.microservices.rabbitmq;

public class Queues {
    public static final String ENV = System.getenv().getOrDefault("ENV", "local");

    // Exchange
    public static final String USER_DETAILS_UPDATED_EXCHANGE = ENV + ".exchange.819.user_details.updated";

    // Queues
    public static final String SENDING_EMAIL = ENV + ".queue.email-service.new_email.sent";
    public static final String USER_UPDATED_REQUISITION = ENV + ".queue.requisition.user.updated";
    public static final String USER_UPDATED_ORDERS = ENV + ".queue.orders.user.updated";

}
