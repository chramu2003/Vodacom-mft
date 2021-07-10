package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.config.PropertiesFileSysConfig;
import za.co.vodacom.vodacommft.service.IDirectoryService;
import za.co.vodacom.vodacommft.service.IFileDeliveryService;
import za.co.vodacom.vodacommft.service.IThreadTuningService;

import java.io.BufferedWriter;
import java.util.List;

@Service
public class FileDeliveryService implements IFileDeliveryService {
    private static final Logger logger = LoggerFactory.getLogger(FileDeliveryService.class);

    @Autowired
    private PropertiesFileSysConfig systemCfgProperties;

    @Autowired
    private IDirectoryService directory_service;

    @Autowired
    private IThreadTuningService threadTuningService;


    @Override
    @SneakyThrows
    public void deliveryProcessing(List<String[]> pendingDeliveryList) {

        try {
            String localDirectory = systemCfgProperties.getLocalWorkingDirectory();
            String workDirectory = localDirectory + "running/";
            directory_service.createDeliveryWorkingDirectories(workDirectory);
            threadTuningService.doFileProcessingWithThreads(pendingDeliveryList, workDirectory, localDirectory);

        } catch (Exception e) {
            logger.error("Error while calling file processing logic :- ", e);
        }
    }
}
