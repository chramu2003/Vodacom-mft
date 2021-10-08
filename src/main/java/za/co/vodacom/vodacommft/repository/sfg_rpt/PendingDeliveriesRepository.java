package za.co.vodacom.vodacommft.repository.sfg_rpt;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.repository.sfg_rpt
 */

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.vodacom.vodacommft.entity.sfg_rpt.PendingDeliveriesEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PendingDeliveriesRepository extends JpaRepository<PendingDeliveriesEntity, String> {

    @Query(value="SELECT  PD.CONSUMER_CODE, PD.ROUTE_SHORT_NAME FROM PENDING_DELIVERIES PD WHERE PD.CONSUMER_CODE IS NOT NULL AND PD.ROUTE_SHORT_NAME IS NOT NULL GROUP BY PD.CONSUMER_CODE, PD.ROUTE_SHORT_NAME ORDER BY PD.CONSUMER_CODE, PD.ROUTE_SHORT_NAME", nativeQuery = true)
    List<String[]> getDistinctConsumerCodeAndRouteShortName();

    @Query(value="select pd.consumer_code, pd.route_short_name from pending_deliveries pd " +
            "join sfg_cfg.delivery_details dd on dd.consumer_code = pd.consumer_code " +
            "where dd.status = 2 group by pd.consumer_code, pd.route_short_name order by pd.consumer_code, pd.route_short_name", nativeQuery = true)
    List<Object[]> getConsumerCodeAndRouteShortName();

    List<PendingDeliveriesEntity> findAllByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName);
    List<PendingDeliveriesEntity> findAllByConsumerCodeAndRouteShortNameOrderByConsumerCode(String consumerCode, String routeShortName, Pageable pageable);

    @Query(value = "SELECT * FROM PENDING_DELIVERIES p WHERE p.CONSUMER_CODE = :consumerCode AND p.DELIVERY_STATUS <> :deliveryStatus AND ROWNUM = 1 Order By p.CONSUMER_CODE," +
            " Substr(TO_CHAR(p.CREATE_TS, 'yyyy-mm-dd hh24mi'),1,14) || '0', p.ROUTE_SHORT_NAME", nativeQuery = true)
    Optional<PendingDeliveriesEntity> findAllByConsumerCodeAndDeliveryStatus(@Param("consumerCode") String consumerCode, @Param("deliveryStatus") String deliveryStatus);

    Optional<PendingDeliveriesEntity> findTopByConsumerCodeAndDeliveryStatusIsNotOrderByConsumerCode(String consumerCode, String deliveryStatus);

    @Transactional
    void deleteByPdUid(String pdUid);

    boolean existsByPdUid(String pdUid);

    @Transactional
    void deleteAllByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName);

    boolean existsByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName);

    int removePendingDeliveriesEntitiesByConsumerCode(String consumerCode);

    int deleteByConsumerCode(String consumerCode);
}
