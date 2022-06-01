package org.doxa.contract.microservices;


import lombok.extern.slf4j.Slf4j;

import org.doxa.contract.DTO.EmailDto;
import org.doxa.contract.microservices.interfaces.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService implements IEmailService {
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private MessageService messageService;

	@Override
	public void sendEmail(EmailDto emailDTO) {
        LOG.info("Start sending email");
        try {
            messageService.sendToEmailQueue(emailDTO);
            LOG.info("Email is sent to : " + emailDTO.getBody().getTo());
        }
        catch (Exception e) {
            LOG.error("Fail to send email to : " + emailDTO.getBody().getTo());
            e.printStackTrace();
        }

	}

}
