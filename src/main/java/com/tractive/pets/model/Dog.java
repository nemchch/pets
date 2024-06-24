package com.tractive.pets.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a dog, which is a type of {@link Pet}.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
public class Dog extends Pet {

    public Dog(final Integer id, final String name, final String type, final TrackerType trackerType,
               final Integer owner, final Boolean inZone) {
        super(id, name, type, trackerType, owner, inZone);
    }
}
