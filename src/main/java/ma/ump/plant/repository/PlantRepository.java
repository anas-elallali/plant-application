package ma.ump.plant.repository;

import ma.ump.plant.domain.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Plant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    Page<Plant> findByFamilyId(Long id, Pageable pageable);

    Page<Plant> findByFamilyIdAndLocalNameContainingIgnoreCase(Long id, String localName,Pageable pageable);

    Page<Plant> findByFamilyIdAndVoucherNumberContainingIgnoreCase(Long id, String voucherNumber, Pageable pageable);

    Page<Plant> findByFamilyIdAndEnglishNameContainingIgnoreCase(Long id, String englishName, Pageable pageable);

    Page<Plant> findByFamilyIdAndScientificNameContainingIgnoreCase(Long id, String scientificName, Pageable pageable);

}
