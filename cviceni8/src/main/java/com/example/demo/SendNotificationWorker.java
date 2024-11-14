package com.example.demo;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationWorker {
    private final static Logger LOG = LoggerFactory.getLogger(ChargeCreditCardWorker.class);

    @JobWorker(type = "send-notification")
    public void sendNotification(@Variable(name = "amountCharged") Double amountCharged) {
        LOG.info("sending email promotion to the client because he spent too little money: {}", String.format("%,.2f", amountCharged));
    }
}
