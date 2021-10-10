package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.config.PropertiesFileSysConfig;
import za.co.vodacom.vodacommft.entity.sfg_rpt.PendingDeliveriesEntity;
import za.co.vodacom.vodacommft.repository.sfg_rpt.PendingDeliveriesRepository;
import za.co.vodacom.vodacommft.service.IPendingDeliveriesService;

import java.util.List;
import java.util.Optional;

@Service
public class PendingDeliveriesService implements IPendingDeliveriesService {

    @Autowired
    PendingDeliveriesRepository pendingDeliveriesRepository;

    @Autowired
    private PropertiesFileSysConfig propertiesCfg;

    @Override
    public Optional<PendingDeliveriesEntity> findByConsumerCodeAndDeliveryStatus(String consumer_code) {

        return pendingDeliveriesRepository.findAllByConsumerCodeAndDeliveryStatus(consumer_code, "48");
    }

    @Override
    public List<PendingDeliveriesEntity> getAllByConsumerAndRouteForDeliveries(String consumerCode, String routeShortName) {
        int batch_size = propertiesCfg.getDeliveryBatchSize();
        List<PendingDeliveriesEntity> pendingDeliveriesEntityList = pendingDeliveriesRepository.
                findAllByConsumerCodeAndRouteShortNameOrderByConsumerCode(consumerCode, routeShortName, PageRequest.of(0, batch_size));

        return pendingDeliveriesEntityList;
    }

    @Override
    public List<String[]> getDistinctConsumerCodeAndRouteShortName() {
        return pendingDeliveriesRepository.getDistinctConsumerCodeAndRouteShortName();
    }

    @Override
    public List<PendingDeliveriesEntity> findAllByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName) {
        return pendingDeliveriesRepository.findAllByConsumerCodeAndRouteShortName(consumerCode, routeShortName);
    }

    @Override
    public void saveEntity(PendingDeliveriesEntity pendingDeliveriesEntity) {
        pendingDeliveriesRepository.save(pendingDeliveriesEntity);
    }

    @Override
    public  List<Object[]> getConsumerCodeForDelivery(){
        return null;
    }
}
