package za.co.vodacom.vodacommft.repository.sfg_cfg;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.repository.sfg_cfg
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.vodacom.vodacommft.entity.sfg_cfg.AgeAnalysisIgnoreEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgeAnalysisIgnoreEntityRepository extends JpaRepository<AgeAnalysisIgnoreEntity, Integer> {
    Optional<AgeAnalysisIgnoreEntity> findAgeAnalysisIgnoreEntityByAgeUid(long ageUid);
    List<AgeAnalysisIgnoreEntity> findAgeAnalysisIgnoreEntitiesByAgeUid(long ageUid);
}
