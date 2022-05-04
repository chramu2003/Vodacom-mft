package za.co.vodacom.vodacommft.service.impl;

import com.jcraft.jsch.SftpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;
import za.co.vodacom.vodacommft.service.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * @author rchennupati on 2020/08/23
 * @package za.co.vodacom.vodacomMFT.service.impl
 */
@SuppressWarnings("all")
@Service
@Slf4j
@RequiredArgsConstructor
public class ThreadTuningService1 implements IThreadTuningService1 {

    final private IDeliveryDetailsService deliveryDetailsService;
    final private IDeliveryService deliveryService;
    final private IDirectoryService directory_service;
    final private ILockService lockService;

    @Override
    public void doFileProcessingWithThreads(String consumerCode, String routeShortName, String workDirectory, String localDirectory) throws IOException {

        if (!lockService.checkIfLockExits(consumerCode)) {

            if (lockService.addLock(consumerCode)) {
                DeliveryDetailsDTO deliveryDetails = deliveryDetailsService.getDeliveriesByConsumerCodeAndRouteShortName(consumerCode, routeShortName);

                if (deliveryDetails != null) {
                    try {
                        String deliveryCode = consumerCode + "~" + routeShortName;
                        String workingDirectory = localDirectory + deliveryCode + "/";
                        directory_service.createDeliveryWorkingDirectories(workingDirectory);
                        directory_service.cleanTempWorkingDeliveryFiles(workingDirectory);

                        List<String> files = deliveryDetails.getList_of_file_metadata();
                        String[] fileNames = files.toArray(new String[0]);

                        FileProcessor calculator = new FileProcessor(fileNames, deliveryDetails);
                        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

                        pool.execute(calculator);
                        calculator.join();
                    } finally {
                        lockService.releaseLock(deliveryDetails.getConsumerCode());
                    }
                }
            }
        }
    }

    class FileProcessor extends RecursiveAction
    {
        private final String[] fileNames;
        private final DeliveryDetailsDTO deliveryDetails;

        public FileProcessor(String[] fileNames, DeliveryDetailsDTO deliveryDetails)
        {
            this.fileNames = fileNames;
            this.deliveryDetails = deliveryDetails;
        }

        @Override
        protected void compute()
        {
            if (this.fileNames.length == 1) {
                this.doFileProcessing();
                return;
            }

            Integer halfway = this.fileNames.length / 2;
            ForkJoinTask.invokeAll(
                    new FileProcessor(Arrays.copyOfRange(this.fileNames, 0, halfway), deliveryDetails),
                    new FileProcessor(Arrays.copyOfRange(this.fileNames, halfway, this.fileNames.length), deliveryDetails)
            );
        }

        private void doFileProcessing()
        {
            String fileName = fileNames[0];
            try {
                deliveryService.deliveryFileProcessing(deliveryDetails, fileName);
            } catch (IOException | SftpException e) {
                log.error("Failed to process the file " + fileName + " with the following error message", e);
            }
        }
    }
}
