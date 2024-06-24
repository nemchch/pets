package com.tractive.pets.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a pet.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type",
        visible = true, requireTypeIdForSubtypes = OptBoolean.FALSE)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cat.class, name = "CAT"),
        @JsonSubTypes.Type(value = Dog.class, name = "DOG")
})
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Pet {

    /**
     * The unique identifier for the pet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * The name of the pet.
     */
    private String name;

    /**
     * The type of the pet (e.g., CAT, DOG).
     */
    private String type;

    /**
     * The type of tracker associated with the pet.
     */
    @Enumerated(EnumType.STRING)
    private TrackerType trackerType;

    /**
     * The ID of the owner of the pet.
     */
    private Integer owner;

    /**
     * Indicates whether the pet is in a power saving zone.
     */
    private Boolean inZone;
}
