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
import za.co.vodacom.vodacommft.service.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class FileDeliveryService implements IFileDeliveryService {
    private static final Logger logger = LoggerFactory.getLogger(FileDeliveryService.class);

    @Autowired
    private PropertiesFileSysConfig systemCfgProperties;

    @Autowired
    private ILockService lockService;

    @Autowired
    private IDirectoryService directory_service;

    @Autowired
    private IConsumerCustomPropertiesService consumerCustomPropertiesService;

    @Autowired
    private IThreadTuningService threadTuningService;


    @Override
    @SneakyThrows
    public void deliveryProcessing(String consumerCode, String routeShortName) {
        BufferedWriter bw_del = null;
        try {
            logger.info("Preparing to do Lock_Exist Verification For Consumer Code :- " + consumerCode);


            String workDirectory = systemCfgProperties.getLocalWorkingDirectory() + "running/";

            String logFile = workDirectory + consumerCode + ".log";
            //create running Directory for delivery streams
            directory_service.createDeliveryWorkingDirectories(workDirectory);
            directory_service.cleanTempWorkingDeliveryFiles(logFile);

            bw_del = new BufferedWriter(new FileWriter(logFile, true));
            bw_del.newLine();



            logger.info("Created Log File  :- " + logFile);

            if (!lockService.checkIfLockExits(consumerCode)){
                bw_del.write(LocalDateTime.now() + "  No Locks For Delivery ITEM_NAME :- " + consumerCode + "\nLocking And Delivery File Processing.....");
                bw_del.newLine();
                if(lockService.addLock(consumerCode)){

                    //Delivery details is not null ... lets check and create working directories, if they does not exist ..Earlier the better.
                    String deliveryCode = consumerCode + "~" + routeShortName;
                    String workingDirectory = systemCfgProperties.getLocalWorkingDirectory() + deliveryCode + "/";
                    directory_service.createDeliveryWorkingDirectories(workingDirectory);
                    directory_service.cleanTempWorkingDeliveryFiles(workingDirectory);

                    threadTuningService.doFileProcessingWithThreads(consumerCode, routeShortName, bw_del);

                }else {
                    bw_del.write(LocalDateTime.now() + "  Add Lock for ITEM_NAME :- " + consumerCode + "Failed.... Investigate");;
                    bw_del.newLine();
                }
            }else {
                logger.info(">>>>>>>>>>Tooo Chacharakiiiiiiiiii  ");
            }
        } catch (IOException e) {
            logger.error("Error capturing logs :- ", e);
        }finally {
            bw_del.close();
        }
    }
}
