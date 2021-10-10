package za.co.vodacom.vodacommft.service;

import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;
import za.co.vodacom.vodacommft.entity.sfg_cfg.DeliveryDetailsEntity;

import java.util.List;
import java.util.Optional;

public interface IDeliveryDetailsService {

    Optional<DeliveryDetailsEntity> findTopByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName);

    DeliveryDetailsDTO getDeliveriesByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName);
}
