package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.config.PropertiesFileSysConfig;
import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;
import za.co.vodacom.vodacommft.entity.sfg_cfg.DeliveryDetailsEntity;
import za.co.vodacom.vodacommft.entity.sfg_rpt.PendingDeliveriesEntity;
import za.co.vodacom.vodacommft.entity.sfg_usr.SftpProfDetailsEntity;
import za.co.vodacom.vodacommft.repository.sfg_cfg.DeliveryDetailsRepository;
import za.co.vodacom.vodacommft.repository.sfg_rpt.PendingDeliveriesRepository;
import za.co.vodacom.vodacommft.service.IDeliveryDetailsService;
import za.co.vodacom.vodacommft.service.IPendingDeliveriesService;
import za.co.vodacom.vodacommft.service.ISFTPDataService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryDetailsService implements IDeliveryDetailsService {

    @Autowired
    PropertiesFileSysConfig propertiesConfig;

    @Autowired
    IPendingDeliveriesService pendingDeliveriesService;

    @Autowired
    ISFTPDataService sftpDataService;

    @Autowired
    private PendingDeliveriesRepository pendingDeliveriesRepository;

    @Autowired
    DeliveryDetailsRepository deliveryDetailsRepository;


    private static final Logger del_detailsService_logger = LoggerFactory.getLogger(DeliveryDetailsService.class);

    @Override
    public Optional<DeliveryDetailsEntity> findTopByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName) {
        return deliveryDetailsRepository.findByConsumerCodeAndRouteShortName(consumerCode, routeShortName);
    }

    @Override
    public DeliveryDetailsDTO getDeliveriesByConsumerCodeAndRouteShortName(String consumerCode, String routeShortName) {

        DeliveryDetailsDTO deliveryDetails = null;
        Optional<DeliveryDetailsEntity> delivery_details = deliveryDetailsRepository.findByConsumerCodeAndRouteShortName(consumerCode, routeShortName);
        if(delivery_details.isPresent()) {

            DeliveryDetailsEntity details_entity_values = delivery_details.get();
            del_detailsService_logger.info(": =====>>>>>>>>>>>>>  USE COMPRESS VALUE for "+ consumerCode + ": "+ routeShortName + " is :- "+ details_entity_values.getUseCompress());

            if (details_entity_values.getStatus().equalsIgnoreCase("2")) { //We will remove status check

                String deliveryCode = consumerCode + "~" + routeShortName;

                deliveryDetails = new DeliveryDetailsDTO();
                deliveryDetails.setConsumerCode(consumerCode);
                deliveryDetails.setRouteShortName(routeShortName);
                deliveryDetails.setDeliveryCode(deliveryCode);
                deliveryDetails.setPublicKeyFile(propertiesConfig.getPublicKey());
                deliveryDetails.setDeliveryDetailsEntity(details_entity_values);
                deliveryDetails.setLocal_working_dir(propertiesConfig.getLocalWorkingDirectory() + deliveryCode + "/");

                Optional<PendingDeliveriesEntity> delivery_details_from_pending_entity = pendingDeliveriesService.findByConsumerCodeAndDeliveryStatus(consumerCode);
                if (delivery_details_from_pending_entity.isPresent()) {

                    deliveryDetails.setPendingDeliveriesEntity(delivery_details_from_pending_entity.get());
                    if (deliveryDetails.getPendingDeliveriesEntity().getConsumerProtocol().equalsIgnoreCase("SFTP")) {
                        del_detailsService_logger.info(LocalDateTime.now() + " " + "Get sftp profile");

                        String sftpProfile = deliveryDetails.getPendingDeliveriesEntity().getSftpProfile();
                        if (!sftpProfile.isEmpty()) {

                            SftpProfDetailsEntity sftpProf_value = sftpDataService.getDelValuesOfSFTPEntity(sftpProfile.trim());
                            deliveryDetails.setSftpProfDetailsEntity(sftpProf_value);
                        }
                    }
                    deliveryDetails.setList_of_file_metadata(populateMetaData(consumerCode, routeShortName));
                }
            }else {
                del_detailsService_logger.info(" :: Consumer Code :- "+ consumerCode + " is Suspended with status of : - " + details_entity_values.getStatus());
            }
        }else{
            del_detailsService_logger.info("Delivery Details  Not Found For Consumer:- " + consumerCode+ " And  Route Short Name :- " + routeShortName + "... This will be deleted from Pending Deliveries");
            deletePendingDeliveriesFilesByConsumerCodeAndRouteShortName(consumerCode, routeShortName);
        }
        return deliveryDetails;
    }


    private List<String> populateMetaData(String consumerCode, String routeShortName) {
        List<PendingDeliveriesEntity> pendingDeliveriesList = pendingDeliveriesService.getAllByConsumerAndRouteForDeliveries(consumerCode, routeShortName);
        List<String> listOfFiles = null;
        if (pendingDeliveriesList.size() > 0) {
            listOfFiles = new ArrayList<>();

            for (PendingDeliveriesEntity pendingDelivery : pendingDeliveriesList) {

                String pid = pendingDelivery.getPdUid();
                String fileNameOnDisk = pendingDelivery.getFilenameOnDisk();
                String destination_file_name = pendingDelivery.getDestinationFilename();
                String data_flow_id = pendingDelivery.getDataFlowId();
                String route_metadata = pendingDelivery.getRouteMetadata();
                String deliver_uid = pendingDelivery.getDeliverUid();

                listOfFiles.add(pid + ";" + fileNameOnDisk + ";" + destination_file_name + ";" + data_flow_id + ";" + route_metadata + ";" + deliver_uid + ";" + fileNameOnDisk);
            }
        }
        return listOfFiles;
    }

    private void deletePendingDeliveriesFilesByConsumerCodeAndRouteShortName(String consumer_code, String route_short_name) {
        pendingDeliveriesRepository.deleteAllByConsumerCodeAndRouteShortName(consumer_code, route_short_name);
        pendingDeliveriesRepository.existsByConsumerCodeAndRouteShortName(consumer_code, route_short_name);
    }

}
