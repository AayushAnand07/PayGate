package org.gateway.paygate.common.exception;


import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourcename;
    private final Object identifier;

    public ResourceNotFoundException(String resourcename, Object identifier) {
        super("Resource not found: " + resourcename + " with identifier: " + identifier);
        this.resourcename = resourcename;
        this.identifier = identifier;
    }
}
