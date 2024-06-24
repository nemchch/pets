package com.tractive.pets.service;

import com.tractive.pets.exception.IllegalPropertyException;
import com.tractive.pets.exception.PetNotFoundException;
import com.tractive.pets.exception.InvalidPropertiesException;
import com.tractive.pets.model.Pet;
import com.tractive.pets.model.PetStatistics;

import java.util.List;
import java.util.Map;

/**
 * Service for managing {@link Pet} objects.
 */
public interface PetService {

    /**
     * Retrieves a list of all pets.
     *
     * @return a list of all pets.
     */
    List<Pet> getAllPets();

    /**
     * Retrieves statistics of pets grouped by tracker type and pets type.
     *
     * @param inZone filter to determine if only pets within a power saving zone should be considered.
     * @return a list of pet statistics grouped by tracker type and pets type.
     */
    List<PetStatistics> getPetsStatisticsGroupedByTrackerTypeAndType(final Boolean inZone);

    /**
     * Creates a new pet.
     *
     * @param pet the pet to be created.
     * @throws IllegalPropertyException if the pet contains illegal properties, e.g. ,"Medium" trackers for cats.
     */
    Pet createPet(final Pet pet) throws IllegalPropertyException;

    /**
     * Updates an existing pet's properties.
     *
     * @param id         the ID of the pet to be updated.
     * @param properties the properties to be updated.
     * @throws PetNotFoundException       if the pet with the specified ID is not found.
     * @throws IllegalPropertyException   if any of the properties are illegal, e.g., "lostTracker" property for dogs.
     * @throws InvalidPropertiesException if the properties are invalid.
     *                                    Supported properties: "inZone" and "lostTracker"
     */
    Pet updatePet(final Integer id, final Map<String, Object> properties) throws PetNotFoundException,
            IllegalPropertyException, InvalidPropertiesException;
}
