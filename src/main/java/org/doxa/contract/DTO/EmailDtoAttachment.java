package org.doxa.contract.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class EmailDtoAttachment {

	//The Base64 encoded content of the attachment.
	private String content;
	//filename of the attachement.
	private String filename;
	//application/pdf, file type
	private String type;
}
