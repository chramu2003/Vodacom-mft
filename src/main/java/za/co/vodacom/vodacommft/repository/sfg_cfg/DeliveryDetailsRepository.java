package za.co.vodacom.vodacommft.repository.sfg_cfg;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.repository.sfg_cfg
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.vodacom.vodacommft.entity.sfg_cfg.DeliveryDetailsEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryDetailsRepository extends JpaRepository<DeliveryDetailsEntity, Integer> {

   Optional<DeliveryDetailsEntity> findTopByConsumerCodeAndRouteShortNameAndStatus(String consumerCode, String routeShortName, String status);

   Optional<DeliveryDetailsEntity> findByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName); //create custom query that only bring active Records

   boolean existsByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName);

   Optional<DeliveryDetailsEntity> findByConsumerCode(String consumerCode);

   DeliveryDetailsEntity findFirstByRouteShortName(String routeShortName);

   @Transactional
   @Modifying
   @Query(value = "UPDATE DELIVERY_DETAILS DF SET DF.STATUS = '5' WHERE DF.CONSUMER_CODE = :consumerCode AND DF.ROUTE_SHORT_NAME = :routeShortName", nativeQuery = true)
   int updateStatusByConsumerCodeAndRouteShortName(@Param("consumerCode") String consumerCode, @Param("routeShortName") String routeShortName);

   @Query(value="SELECT CONSUMER_CODE, ROUTE_SHORT_NAME FROM DELIVERY_DETAILS WHERE CONSUMER_CODE = :consumerCode and STATUS = 2", nativeQuery = true)
   List<DeliveryDetailsEntity> getActiveConsumerCodeAndRouteShortName(@Param("consumerCode") String consumerCode);

}
