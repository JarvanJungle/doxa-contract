package org.doxa.contract.enums;

public enum Roles {
	DOXA_ADMIN ("DOXA_ADMIN"),
	COMPANY_ADMIN ("COMPANY_ADMIN"),
	ENTITY_ADMIN ("ENTITY_ADMIN");

	private final String role;

	Roles(String constMessages) {
		this.role=constMessages;
	}
	
	public String getValue() {
		return this.role;
	}

}
