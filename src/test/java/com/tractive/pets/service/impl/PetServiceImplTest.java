package com.tractive.pets.service.impl;

import com.tractive.pets.exception.IllegalPropertyException;
import com.tractive.pets.exception.InvalidPropertiesException;
import com.tractive.pets.exception.PetNotFoundException;
import com.tractive.pets.model.*;
import com.tractive.pets.repository.PetRepository;
import com.tractive.pets.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tractive.pets.service.impl.PetServiceImpl.IN_ZONE_PROPERTY;
import static com.tractive.pets.service.impl.PetServiceImpl.LOST_TRACKER_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PetServiceImplTest {

    private static final String CAT_TYPE = "CAT";
    private static final String DOG_TYPE = "DOG";

    private PetRepository petRepository;
    private PetService petService;

    @BeforeEach
    void setUp() {
        petRepository = mock();
        petService = new PetServiceImpl(petRepository);
    }

    @Test
    void testGetAllPets() {
        when(petRepository.findAll()).thenReturn(createPets());

        final List<Pet> pets = petService.getAllPets();
        assertThat(pets).contains(createPets().toArray(new Pet[10]));
    }

    @Test
    void testGetPetsStatisticsGroupedByTrackerTypeAndType() {
        final List<PetStatistics> statistics = List.of(
                new PetStatistics(TrackerType.SMALL, CAT_TYPE, 3L),
                new PetStatistics(TrackerType.BIG, DOG_TYPE, 1L)
        );

        when(petRepository.getStatisticsGroupedByTrackerTypeAndType(true)).thenReturn(List.of(
                new PetStatistics(TrackerType.SMALL, CAT_TYPE, 3L),
                new PetStatistics(TrackerType.BIG, DOG_TYPE, 1L)
        ));
        when(petRepository.getStatisticsGroupedByTrackerTypeAndType(false)).thenReturn(List.of());

        final List<PetStatistics> petStatisticsInZone = petService
                .getPetsStatisticsGroupedByTrackerTypeAndType(true);
        assertThat(petStatisticsInZone).contains(statistics.toArray(new PetStatistics[2]));

        final List<PetStatistics> petStatisticsNotInZone = petService
                .getPetsStatisticsGroupedByTrackerTypeAndType(false);
        assertThat(petStatisticsNotInZone).isEmpty();

    }

    @Test
    void testCreatePetNullType() {
        final Pet pet = new Cat(null, "Barsik", CAT_TYPE, null, 1, true,
                false);

        assertThrows(IllegalPropertyException.class, () -> petService.createPet(pet));
    }

    @Test
    void testCreatePetNullTrackerType() {
        final Pet pet = new Cat(null, "Barsik", CAT_TYPE, null, 1, true,
                false);

        assertThrows(IllegalPropertyException.class, () -> petService.createPet(pet));
    }

    @Test
    void testCreatePetWrongTrackerType() {
        final Pet pet = new Cat(null, "Barsik", CAT_TYPE, TrackerType.MEDIUM, 1, true,
                false);

        assertThrows(IllegalPropertyException.class, () -> petService.createPet(pet));
    }

    @Test
    void testCreateCat() {
        final Pet pet = new Cat(null, "Barsik", CAT_TYPE, TrackerType.SMALL, 1, true,
                false);

        assertDoesNotThrow(() -> petService.createPet(pet));
    }

    @Test
    void testCreateDog() {
        final Pet pet = new Dog(null, "Raketa", DOG_TYPE, TrackerType.MEDIUM, 3, false);

        assertDoesNotThrow(() -> petService.createPet(pet));
    }

    @Test
    void testUpdatePetEmptyProperties() {
        assertThrows(InvalidPropertiesException.class, () -> petService.updatePet(1, Map.of()));
    }

    @Test
    void testUpdatePetWrongProperties() {
        assertThrows(InvalidPropertiesException.class,
                () -> petService.updatePet(1, Map.of("name", "Asya")));
    }

    @Test
    void testUpdatePetNotFound() {
        when(petRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class,
                () -> petService.updatePet(1, Map.of(LOST_TRACKER_PROPERTY, true)));
    }

    @Test
    void testUpdateDogInZone() {
        when(petRepository.findById(1)).thenReturn(Optional.of(
                new Dog(1, "Bobik", DOG_TYPE, TrackerType.SMALL, 2, true)));

        assertDoesNotThrow(() -> petService.updatePet(1, Map.of(IN_ZONE_PROPERTY, false)));
    }

    @Test
    void testUpdateCatLostTracker() {
        when(petRepository.findById(1)).thenReturn(Optional.of(
                new Cat(1, "Barsik", CAT_TYPE, TrackerType.SMALL, 1, true, false)));

        assertDoesNotThrow(() -> petService.updatePet(1, Map.of(LOST_TRACKER_PROPERTY, true)));
    }

    @Test
    void testUpdateCatLostTrackerAndInZone() {
        when(petRepository.findById(1)).thenReturn(Optional.of(
                new Cat(1, "Barsik", CAT_TYPE, TrackerType.SMALL, 1, false, false)));

        assertDoesNotThrow(() -> petService.updatePet(1, Map.of(LOST_TRACKER_PROPERTY, true,
                IN_ZONE_PROPERTY, true)));
    }

    @Test
    void testUpdateDogLostTracker() {
        when(petRepository.findById(1)).thenReturn(Optional.of(
                new Dog(1, "Bobik", DOG_TYPE, TrackerType.SMALL, 2, true)));

        assertThrows(IllegalPropertyException.class,
                () -> petService.updatePet(1, Map.of(LOST_TRACKER_PROPERTY, true)));
    }

    private static List<Pet> createPets() {
        return List.of(
                new Cat(1, "Barsik", CAT_TYPE, TrackerType.SMALL, 1, true, false),
                new Cat(2, "Murka", CAT_TYPE, TrackerType.BIG, 1, true, true),
                new Cat(3, "Snezhok", CAT_TYPE, TrackerType.BIG, 2, false, true),
                new Cat(4, "Vasya", CAT_TYPE, TrackerType.BIG, 3, false, false),
                new Dog(5, "Bobik", DOG_TYPE, TrackerType.SMALL, 2, true),
                new Dog(6, "Reks", DOG_TYPE, TrackerType.MEDIUM, 3, true),
                new Dog(7, "Raketa", DOG_TYPE, TrackerType.BIG, 3, false),
                new Dog(8, "Zhuchka", DOG_TYPE, TrackerType.SMALL, 4, true),
                new Dog(9, "Tosya", DOG_TYPE, TrackerType.MEDIUM, 2, false),
                new Dog(10, "Mishka", DOG_TYPE, TrackerType.BIG, 5, true)
        );
    }
}