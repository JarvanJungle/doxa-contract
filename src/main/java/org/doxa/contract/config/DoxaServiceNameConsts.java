package org.doxa.contract.config;

public enum DoxaServiceNameConsts {

    AUTHORIZATION_SERVICE ("privilege-service"),
    ENTITIES_SERVICE ("doxa-entity-service"),
    OAUTH_SERVICE("oauth-service"),
    MEDIA_SERVICE ("media-service");

    private final String config;

    DoxaServiceNameConsts(String config) {
        this.config=config;
    }

    public String getValue() {
        return this.config;
    }

}
