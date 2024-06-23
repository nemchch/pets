package com.tractive.pets.service.impl;

import com.tractive.pets.exception.IllegalPropertyException;
import com.tractive.pets.exception.InvalidPropertiesException;
import com.tractive.pets.exception.PetNotFoundException;
import com.tractive.pets.model.Cat;
import com.tractive.pets.model.Pet;
import com.tractive.pets.model.PetStatistics;
import com.tractive.pets.model.TrackerType;
import com.tractive.pets.repository.PetRepository;
import com.tractive.pets.service.PetService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    static final String LOST_TRACKER_PROPERTY = "lostTracker";
    static final String IN_ZONE_PROPERTY = "inZone";

    public PetServiceImpl(final PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Transactional
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    @Transactional
    public List<PetStatistics> getPetsStatisticsGroupedByTrackerTypeAndType(final Boolean inZone) {
        return petRepository.getStatisticsGroupedByTrackerTypeAndType(inZone);
    }

    @Override
    @Transactional
    public Pet createPet(final Pet pet) throws IllegalPropertyException {
        if (pet.getType() == null) {
            throw new IllegalPropertyException("type", null, "CAT || DOG");
        }

        if (pet.getTrackerType() == null) {
            throw new IllegalPropertyException("trackerType", null, "CAT || DOG");
        }

        if (pet instanceof final Cat cat && cat.getTrackerType().equals(TrackerType.MEDIUM)) {
            throw new IllegalPropertyException("trackerType", cat.getTrackerType(), cat.getType());
        }

        return petRepository.save(pet);
    }

    @Override
    @Transactional
    public Pet updatePet(final Integer id, final Map<String, Object> properties) throws PetNotFoundException,
            IllegalPropertyException, InvalidPropertiesException {
        if (CollectionUtils.isEmpty(properties)) {
            throw new InvalidPropertiesException(Set.of(), Set.of(LOST_TRACKER_PROPERTY, IN_ZONE_PROPERTY));
        }

        if (!properties.containsKey(IN_ZONE_PROPERTY) && !properties.containsKey(LOST_TRACKER_PROPERTY)) {
            throw new InvalidPropertiesException(properties.keySet(),
                    Set.of(LOST_TRACKER_PROPERTY, IN_ZONE_PROPERTY));
        }

        final Optional<Pet> optionalPet = petRepository.findById(id);
        if (optionalPet.isEmpty()) {
            throw new PetNotFoundException(id);
        }

        final Pet pet = optionalPet.get();

        if (properties.containsKey(IN_ZONE_PROPERTY)
                && properties.get(IN_ZONE_PROPERTY) instanceof final Boolean inZone) {
            pet.setInZone(inZone);
        }

        if (properties.containsKey(LOST_TRACKER_PROPERTY)
                && properties.get(LOST_TRACKER_PROPERTY) instanceof Boolean lostTracker) {
            if (pet instanceof final Cat cat) {
                cat.setLostTracker(lostTracker);
                return petRepository.save(cat);
            } else {
                throw new IllegalPropertyException(LOST_TRACKER_PROPERTY, lostTracker, pet.getType());
            }
        } else {
            return petRepository.save(pet);
        }
    }
}

