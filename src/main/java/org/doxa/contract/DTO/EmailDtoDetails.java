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
public class EmailDtoDetails {
	
	//At least one email address need to be specified in to, cc or bcc
	//message can accept HTML tags inside the String
	private List<String> to;
	private List<String> cc;
	private List<String> bcc;
	private String subject;
	private String title;
	private String message;

	public EmailDtoDetails(String to, String subject, String title, String message) {
		this.to  = new ArrayList<String> ();
		this.cc = new ArrayList<String> ();
		this.bcc  = new ArrayList<String> ();
		this.to.add(to);
		this.title = title;
		this.subject = subject;
		this.message = message;
	}
}
