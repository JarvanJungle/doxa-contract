package org.doxa.contract.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EntitiesApiException extends RuntimeException {
	public EntitiesApiException(String message){super(message);}
}
