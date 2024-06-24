package com.tractive.pets.exception;

import java.io.Serial;
import java.util.Set;


public class InvalidPropertiesException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidPropertiesException(final Set<String> notSupportedProperties,
                                      final Set<String> supportedProperties) {
        super(String.format("The following properties '%s' are not supported in the current API version, " +
                "please specify one of the properties '%s'", notSupportedProperties, supportedProperties));
    }
}
