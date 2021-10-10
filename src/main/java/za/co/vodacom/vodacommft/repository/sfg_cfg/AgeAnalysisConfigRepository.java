package za.co.vodacom.vodacommft.repository.sfg_cfg;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.repository.sfg_cfg
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.vodacom.vodacommft.entity.sfg_cfg.AgeAnalysisConfigEntity;

import java.util.Optional;

@Repository
public interface AgeAnalysisConfigRepository extends JpaRepository<AgeAnalysisConfigEntity, Integer> {
    Optional<AgeAnalysisConfigEntity> findAgeAnalysisConfigEntitiesByRouteCode(String routeCode);
}
