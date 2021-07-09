package za.co.vodacom.vodacommft.repository.sfg_rpt;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.repository.sfg_rpt
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.vodacom.vodacommft.entity.sfg_rpt.LocksEntity;

@Repository
public interface LocksRepository extends JpaRepository<LocksEntity, String> {
}
