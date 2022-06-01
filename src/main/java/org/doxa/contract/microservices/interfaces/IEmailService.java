package org.doxa.contract.microservices.interfaces;


import org.doxa.contract.DTO.EmailDto;

public interface IEmailService {

	public void sendEmail(EmailDto emailDTO);
}
