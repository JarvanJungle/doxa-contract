package org.doxa.contract.exceptions;

public class EntityAlreadyExistsException extends RuntimeException{
	
    public EntityAlreadyExistsException(String message) {
        super(message);
    }

}
