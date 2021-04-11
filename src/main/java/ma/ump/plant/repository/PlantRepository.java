package ma.ump.plant.repository;

import ma.ump.plant.domain.Plant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Plant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {}
