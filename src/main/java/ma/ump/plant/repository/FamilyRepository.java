package ma.ump.plant.repository;

import ma.ump.plant.domain.Family;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Family entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {}
