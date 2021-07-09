package za.co.vodacom.vodacommft.service;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service
 */

import za.co.vodacom.vodacommft.entity.sfg_rpt.PendingDeliveriesEntity;

import java.util.List;
import java.util.Optional;

public interface IPendingDeliveriesService {

    List<String[]> getDistinctConsumerCodeAndRouteShortName();

    List<PendingDeliveriesEntity> findAllByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName);

    List<PendingDeliveriesEntity> getAllByConsumerAndRouteForDeliveries(String consumerCode, String routeShortName);

    Optional<PendingDeliveriesEntity> findByConsumerCodeAndDeliveryStatus(String consumerCode);

    /*Optional<Object> findTopByConsumerCodeAndDeliveryStatus(String consumer_code);*/

    void saveEntity(PendingDeliveriesEntity pendingDeliveriesEntity);

    List<Object[]> getConsumerCodeForDelivery();
}
