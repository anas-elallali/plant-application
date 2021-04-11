package ma.ump.plant.repository;

import ma.ump.plant.domain.EcologicalStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EcologicalStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EcologicalStatusRepository extends JpaRepository<EcologicalStatus, Long> {}
