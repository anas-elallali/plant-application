package ma.ump.plant.repository;

import ma.ump.plant.domain.EcologicalStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the EcologicalStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EcologicalStatusRepository extends JpaRepository<EcologicalStatus, Long> {

    Optional<EcologicalStatus> findTop1ByName(String name);
}
