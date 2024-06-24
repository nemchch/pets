package com.tractive.pets.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents statistics for {@link Pet}, the number of pets grouped by the type of tracker and the type of pet.
 */
@Data
@AllArgsConstructor
public class PetStatistics {

    /**
     * The type of tracker associated with the pet.
     */
    private TrackerType trackerType;

    /**
     * The type of pet (e.g., CAT, DOG).
     */
    private String petType;

    /**
     * The count of pets of this type with this tracker type.
     */
    private Long count;
}
