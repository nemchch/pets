package com.tractive.pets.repository;

import com.tractive.pets.model.Pet;
import com.tractive.pets.model.PetStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for managing {@link Pet} entities.
 */
public interface PetRepository extends JpaRepository<Pet, Integer> {

    /**
     * Retrieves statistics grouped by tracker type and pet type for pets that are in or out of a power saving zone.
     *
     * @param inZone filter to determine if only pets within a certain zone should be considered.
     * @return a list of {@link PetStatistics} objects representing the grouped statistics.
     */
    @Query("select new com.tractive.pets.model.PetStatistics(pet.trackerType, pet.type, count(pet)) " +
            "from Pet pet where pet.inZone = :inZone " +
            "group by pet.trackerType, pet.type")
    List<PetStatistics> getStatisticsGroupedByTrackerTypeAndType(final @Param("inZone") Boolean inZone);
}
