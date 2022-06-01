package org.doxa.contract.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class EmailDto {

	private EmailDtoDetails body ;
	
	//Setup for sending attachment, just temp setup
	private List<EmailDtoAttachment> attachments;
	
	public EmailDto(String to, String subject, String title, String message) {
		this.body = new EmailDtoDetails(to, subject, title, message);
		this.attachments = new ArrayList<EmailDtoAttachment>();
	}
}
