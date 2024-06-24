package com.tractive.pets.controller;

import com.tractive.pets.exception.IllegalPropertyException;
import com.tractive.pets.exception.InvalidPropertiesException;
import com.tractive.pets.exception.PetNotFoundException;
import com.tractive.pets.model.Pet;
import com.tractive.pets.model.PetStatistics;
import com.tractive.pets.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing {@link Pet} objects.
 */
@RestController
@RequestMapping("/api/v1/pet")
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(final PetService petService) {
        this.petService = petService;
    }

    /**
     * Retrieves a list of all pets.
     *
     * @return a list of all pets.
     */
    @GetMapping
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    /**
     * Retrieves statistics of pets grouped by tracker type and pets type.
     *
     * @param inZone filter to determine if only pets within a power saving zone should be considered.
     * @return a list of pet statistics grouped by tracker type and type.
     */
    @GetMapping("/statistics")
    public List<PetStatistics> getStatistics(@RequestParam Boolean inZone) {
        return petService.getPetsStatisticsGroupedByTrackerTypeAndType(inZone);
    }

    /**
     * Creates a new pet.
     *
     * @param pet the pet to be created.
     * @throws ResponseStatusException with a response code 400 if the pet contains illegal properties.
     */
    @PostMapping
    public Pet createPet(@RequestBody Pet pet) {
        try {
            return petService.createPet(pet);
        } catch (IllegalPropertyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Updates an existing pet's properties.
     *
     * @param id the ID of the pet to be updated.
     * @param properties the properties to be updated.
     * @throws ResponseStatusException with a response code 404 if the pet is not found
     * and a response code 400 if the properties are illegal or invalid.
     */
    @PatchMapping("/{id}")
    public Pet updatePet(@PathVariable Integer id, @RequestBody Map<String, Object> properties) {
        try {
            return petService.updatePet(id, properties);
        } catch (PetNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalPropertyException | InvalidPropertiesException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
