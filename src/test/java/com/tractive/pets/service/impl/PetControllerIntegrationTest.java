package com.tractive.pets.service.impl;

import com.tractive.pets.model.*;
import com.tractive.pets.repository.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static com.tractive.pets.service.impl.PetServiceImpl.IN_ZONE_PROPERTY;
import static com.tractive.pets.service.impl.PetServiceImpl.LOST_TRACKER_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetControllerIntegrationTest {

    private static final String CAT_TYPE = "CAT";
    private static final String DOG_TYPE = "DOG";
    private static final String TRACKER_TYPE_PROPERTY = "trackerType";
    private static final String PET_TYPE_PROPERTY = "petType";
    private static final String COUNT_PROPERTY = "count";

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void setup() {
        assertThat(petRepository.count()).isZero();
        template.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @AfterEach
    void teardown() {
        petRepository.deleteAll();
    }

    @Test
    void testSavePet() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Cat(null, "Barsik", CAT_TYPE, TrackerType.SMALL, 1,
                        true, false)), Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Test
    void testSavePetNoTrackerType() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Cat(null, "Barsik", CAT_TYPE, null, 1,
                        true, false)), Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void testSavePetNoType() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Pet(null, "Barsik", null, TrackerType.SMALL, 1,
                        true)), Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void testSaveCatMediumTrackerType() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Cat(null, "Barsik", CAT_TYPE, TrackerType.MEDIUM, 1,
                        true, false)), Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void testSavePetChangeInZone() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Dog(null, "Bobik", DOG_TYPE, TrackerType.SMALL, 2, true)),
                Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        final Integer id = responseEntity.getBody().getId();

        final ResponseEntity<Pet> responseEntityPatch = template.exchange("/api/v1/pet/{id}", HttpMethod.PATCH,
                new HttpEntity<>(Map.of(IN_ZONE_PROPERTY, false)), Pet.class, id);
        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        assertThat(responseEntityPatch.getBody()).isNotNull();
        assertThat(responseEntityPatch.getBody().getInZone()).isFalse();
    }

    @Test
    void testSaveCatChangeLostTracker() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Cat(null, "Barsik", CAT_TYPE, TrackerType.SMALL, 1,
                        true, false)), Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        final Integer id = responseEntity.getBody().getId();

        final ResponseEntity<Pet> responseEntityPatch = template.exchange("/api/v1/pet/{id}", HttpMethod.PATCH,
                new HttpEntity<>(Map.of(LOST_TRACKER_PROPERTY, true)), Pet.class, id);
        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        assertThat(responseEntityPatch.getBody()).isNotNull();
        assertThat(responseEntityPatch.getBody().getType()).isEqualTo(CAT_TYPE);
        assertThat(((Cat)responseEntityPatch.getBody()).getLostTracker()).isTrue();
    }

    @Test
    void testSaveCatChangeLostTrackerAndInZone() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Cat(null, "Barsik", CAT_TYPE, TrackerType.SMALL, 1,
                        true, false)), Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        final Integer id = responseEntity.getBody().getId();

        final ResponseEntity<Pet> responseEntityPatch = template.exchange("/api/v1/pet/{id}", HttpMethod.PATCH,
                new HttpEntity<>(Map.of(LOST_TRACKER_PROPERTY, true, IN_ZONE_PROPERTY, false)),
                Pet.class, id);
        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        assertThat(responseEntityPatch.getBody()).isNotNull();
        assertThat(responseEntityPatch.getBody().getInZone()).isFalse();
        assertThat(responseEntityPatch.getBody().getType()).isEqualTo(CAT_TYPE);
    }

    @Test
    void testSaveDogChangeLostTracker() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Dog(null, "Bobik", DOG_TYPE, TrackerType.SMALL, 2, true)),
                Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        final Integer id = responseEntity.getBody().getId();

        final ResponseEntity<Pet> responseEntityPatch = template.exchange("/api/v1/pet/{id}", HttpMethod.PATCH,
                new HttpEntity<>(Map.of(LOST_TRACKER_PROPERTY, true)), Pet.class, id);
        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void testSaveDogChangeLostTrackerAndInZone() {
        final ResponseEntity<Pet> responseEntity = template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(new Dog(null, "Bobik", DOG_TYPE, TrackerType.SMALL, 2, true)),
                Pet.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        final Integer id = responseEntity.getBody().getId();

        final ResponseEntity<Pet> responseEntityPatch = template.exchange("/api/v1/pet/{id}", HttpMethod.PATCH,
                new HttpEntity<>(Map.of(LOST_TRACKER_PROPERTY, true, IN_ZONE_PROPERTY, false)),
                Pet.class, id);
        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void testUpdatePetNotFound() {
        final ResponseEntity<Pet> responseEntityPatch = template.exchange("/api/v1/pet/{id}", HttpMethod.PATCH,
                new HttpEntity<>(Map.of(LOST_TRACKER_PROPERTY, true, IN_ZONE_PROPERTY, false)),
                Pet.class, 10);
        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }

    @Test
    void testUpdatePetEmptyProperties() {
        final ResponseEntity<Pet> responseEntityPatch = template.exchange("/api/v1/pet/{id}", HttpMethod.PATCH,
                new HttpEntity<>(Map.of()), Pet.class, 1);
        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void testUpdatePetIllegalProperties() {
        final ResponseEntity<Pet> responseEntityPatch = template.exchange("/api/v1/pet/{id}", HttpMethod.PATCH,
                new HttpEntity<>(Map.of("name", "Asya")), Pet.class, 1);
        assertThat(responseEntityPatch.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    void testSave10PetsReturnAllReturnStatistics() {
        createPets().forEach(pet -> template.exchange("/api/v1/pet", HttpMethod.POST,
                new HttpEntity<>(pet), Pet.class));
        final List<Pet> pets = template.getForObject("/api/v1/pet", List.class);
        assertThat(pets).isNotEmpty().hasSize(10);

        final List<Map<String, Object>> statisticsListNotInZone = template.getForObject(
                "/api/v1/pet/statistics?inZone={inZone}", List.class, false);
        final List<PetStatistics> petStatisticsListNotInZone = statisticsListNotInZone.stream()
                .map(map -> new PetStatistics(TrackerType.valueOf((String) map.get(TRACKER_TYPE_PROPERTY)),
                        (String) map.get(PET_TYPE_PROPERTY),
                        Long.valueOf((Integer) map.get(COUNT_PROPERTY)))).toList();

        assertThat(petStatisticsListNotInZone).isNotEmpty().hasSize(3).containsAll(List.of(
                new PetStatistics(TrackerType.BIG, CAT_TYPE, 2L),
                new PetStatistics(TrackerType.BIG, DOG_TYPE, 1L),
                new PetStatistics(TrackerType.MEDIUM, DOG_TYPE, 1L)
        ));

        final List<Map<String, Object>> statisticsListInZone = template.getForObject(
                "/api/v1/pet/statistics?inZone={inZone}", List.class, true);
        final List<PetStatistics> petStatisticsListInZone = statisticsListInZone.stream()
                .map(map -> new PetStatistics(TrackerType.valueOf((String) map.get(TRACKER_TYPE_PROPERTY)),
                        (String) map.get(PET_TYPE_PROPERTY),
                        Long.valueOf((Integer) map.get(COUNT_PROPERTY)))).toList();

        assertThat(petStatisticsListInZone).isNotEmpty().hasSize(5).containsExactly(
                new PetStatistics(TrackerType.BIG, CAT_TYPE, 1L),
                new PetStatistics(TrackerType.BIG, DOG_TYPE, 1L),
                new PetStatistics(TrackerType.MEDIUM, DOG_TYPE, 1L),
                new PetStatistics(TrackerType.SMALL, CAT_TYPE, 1L),
                new PetStatistics(TrackerType.SMALL, DOG_TYPE, 2L)
        );
    }

    private static List<Pet> createPets() {
        return List.of(
                new Cat(null, "Barsik", CAT_TYPE, TrackerType.SMALL, 1, true, false),
                new Cat(null, "Murka", CAT_TYPE, TrackerType.BIG, 1, true, true),
                new Cat(null, "Snezhok", CAT_TYPE, TrackerType.BIG, 2, false, true),
                new Cat(null, "Vasya", CAT_TYPE, TrackerType.BIG, 3, false, false),
                new Dog(null, "Bobik", DOG_TYPE, TrackerType.SMALL, 2, true),
                new Dog(null, "Reks", DOG_TYPE, TrackerType.MEDIUM, 3, true),
                new Dog(null, "Raketa", DOG_TYPE, TrackerType.BIG, 3, false),
                new Dog(null, "Zhuchka", DOG_TYPE, TrackerType.SMALL, 4, true),
                new Dog(null, "Tosya", DOG_TYPE, TrackerType.MEDIUM, 2, false),
                new Dog(null, "Mishka", DOG_TYPE, TrackerType.BIG, 5, true)
        );
    }
}
