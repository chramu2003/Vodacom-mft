package za.co.vodacom.vodacommft.repository.sfg_usr;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.repository.sfg_usr
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.vodacom.vodacommft.entity.sfg_usr.FGRouteEntity;

@Repository
public interface FGRouteEntityRepository extends JpaRepository<FGRouteEntity, String> {

    @Transactional
    @Modifying
    @Query(value = "update FG_ROUTE set STATE = 'Failed' where ROUTE_KEY = :routeKey AND STATE <> 'Failed'" , nativeQuery = true)
    void updateStateByRouteKeyAndState(@Param("routeKey") String routeKey);
}
