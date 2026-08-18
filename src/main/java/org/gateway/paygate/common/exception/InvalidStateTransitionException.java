package org.gateway.paygate.common.exception;

import lombok.Getter;

@Getter
public class InvalidStateTransitionException extends RuntimeException{

    private final String fromState;
    private final String toState;


    public InvalidStateTransitionException(String fromState, String toEvent) {
        super("Invalid state transition: " + fromState + " -> " + toEvent);
        this.fromState = fromState;
        this.toState = toEvent;
    }
}
