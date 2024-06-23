package com.tractive.pets.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a cat, which is a type of {@link Pet}.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
public class Cat extends Pet {

    /**
     * Indicates whether the cat has lost its tracker.
     */
    private Boolean lostTracker;

    public Cat(final Integer id, final String name, final String type, final TrackerType trackerType,
               final Integer owner, final Boolean inZone, final Boolean lostTracker) {
        super(id, name, type, trackerType, owner, inZone);
        this.lostTracker = lostTracker;
    }
}
