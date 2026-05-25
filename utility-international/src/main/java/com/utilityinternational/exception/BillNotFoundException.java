package com.utilityinternational.exception;

/**
 * Thrown when a bill-related operation cannot be completed, e.g. attempting to
 * dispute a bill past its 30-day window or referencing a bill that does not
 * exist.
 */
public class BillNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BillNotFoundException(String message) {
        super(message);
    }
}
