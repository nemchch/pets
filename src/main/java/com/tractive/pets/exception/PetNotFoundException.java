package com.tractive.pets.exception;

import java.io.Serial;

public class PetNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public PetNotFoundException(final Integer id) {
        super(String.format("Pet with id %s not found", id));
    }
}
