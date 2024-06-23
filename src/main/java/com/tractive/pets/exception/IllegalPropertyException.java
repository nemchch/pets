package com.tractive.pets.exception;

import java.io.Serial;

public class IllegalPropertyException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public IllegalPropertyException(final Object property, final Object value, final String type) {
        super(String.format("Property '%s' with the value '%s' for the type '%s' is illegal", property, value, type));
    }
}
