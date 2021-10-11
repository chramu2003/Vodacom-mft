package za.co.vodacom.vodacommft.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;
import za.co.vodacom.vodacommft.service.IDeliveryDetailsService;
import za.co.vodacom.vodacommft.service.IDeliveryService;
import za.co.vodacom.vodacommft.service.IDirectoryService;
import za.co.vodacom.vodacommft.service.IThreadTuningService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ncubeh on 2020/08/23
 * @package za.co.vodacom.vodacomMFT.service.impl
 */
@SuppressWarnings("all")
@Service
@Slf4j
public class ThreadTuningService implements IThreadTuningService {

    private ThreadPoolExecutor threadPoolExecutorTen = null;
    private ThreadPoolExecutor threadPoolExecutorFive = null;
    private ThreadPoolExecutor threadPoolExecutorTwo = null;

    @Autowired
    private IDeliveryDetailsService deliveryDetailsService;

    @Autowired
    private IDeliveryService deliveryService;

    @Autowired
    private IDirectoryService directory_service;


    @Override
    public void doFileProcessing(String consumerCode, String routeShortName, String workDirectory, String localDirectory) throws IOException {

        BufferedWriter bw_del = null;
        try {
            String logFile = workDirectory + consumerCode + ".log";
            bw_del = createLogFile(logFile);
            directory_service.cleanTempWorkingDeliveryFiles(logFile);

            String deliveryCode = consumerCode + "~" + routeShortName;
            String workingDirectory = localDirectory + deliveryCode + "/";
            directory_service.createDeliveryWorkingDirectories(workingDirectory);
            directory_service.cleanTempWorkingDeliveryFiles(workingDirectory);

            DeliveryDetailsDTO deliveryDetails = deliveryDetailsService.getDeliveriesByConsumerCodeAndRouteShortName(consumerCode, routeShortName);
            if (deliveryDetails != null) {
                List<String> files = deliveryDetails.getList_of_file_metadata();
                int fileCount = files.size();

                /*if (fileCount > 0 && fileCount <= 100) {
                    doDeliveryWithTwoThreads(deliveryDetails, fileCount);
                } else if (fileCount > 100 && fileCount <= 500) {
                    doDeliveryWithFiveThreads(deliveryDetails, fileCount);
                } else if (fileCount > 500) {
                    doDeliveryWithTenThreads(deliveryDetails, fileCount);
                }*/

                doDeliveryWithTenThreads(deliveryDetails, fileCount);
            }

        }catch (IOException e){
            log.error("Error while routing file processing ", e);
        } finally {
            if (bw_del != null) {
                try {
                    bw_del.close();
                } catch (IOException e) {
                    log.error("Failed to close Buffered Writer", e);
                }
            }
        }
    }

    private void doDeliveryWithTenThreads(DeliveryDetailsDTO deliveryDetails, int fileCount) {
        try {
            if (threadPoolExecutorTen == null || threadPoolExecutorTen.isShutdown()) threadPoolExecutorTen = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            CountDownLatch latch = new CountDownLatch(fileCount);
            for (String fileName : deliveryDetails.getList_of_file_metadata()) {
                threadPoolExecutorTen.execute(() -> {
                    try {
                        deliveryService.deliveryFileProcessing(deliveryDetails, fileName);
                    } catch (Exception e) {
                        log.error("Failed to process the file " + fileName + " with the following error message", e);
                    }
                    latch.countDown();
                });
            }
            latch.await();
        }catch (InterruptedException e) {
            log.error("Failed while processing consumer code " + deliveryDetails.getConsumerCode() + " route short name" + deliveryDetails.getRouteShortName(), e);
        } finally {
            if(!threadPoolExecutorTen.isShutdown())threadPoolExecutorTen.shutdown();
        }
    }

    private void doDeliveryWithFiveThreads(DeliveryDetailsDTO deliveryDetails, int fileCount) {
        try {
            if (threadPoolExecutorFive == null || threadPoolExecutorFive.isShutdown()) threadPoolExecutorFive = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
            CountDownLatch latch = new CountDownLatch(fileCount);
            for (String fileName : deliveryDetails.getList_of_file_metadata()) {
                threadPoolExecutorFive.execute(() -> {
                    try {
                        deliveryService.deliveryFileProcessing(deliveryDetails, fileName);
                    } catch (Exception e) {
                        log.error("Failed to process the file " + fileName + " with the following error message", e);
                    }
                    latch.countDown();
                });
            }
            latch.await();
        }catch (InterruptedException e) {
            log.error("Failed while processing consumer code " + deliveryDetails.getConsumerCode() + " route short name" + deliveryDetails.getRouteShortName(), e);
        } finally {
            if(!threadPoolExecutorFive.isShutdown())threadPoolExecutorFive.shutdown();
        }
    }

    private void doDeliveryWithTwoThreads(DeliveryDetailsDTO deliveryDetails, int fileCount) {
        try {
            if (threadPoolExecutorTwo == null || threadPoolExecutorTwo.isShutdown()) threadPoolExecutorTwo = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
            CountDownLatch latch = new CountDownLatch(fileCount);
            for (String fileName : deliveryDetails.getList_of_file_metadata()) {
                threadPoolExecutorTwo.execute(() -> {
                    try {
                        deliveryService.deliveryFileProcessing(deliveryDetails, fileName);
                    } catch (Exception e) {
                        log.error("Failed to process the file " + fileName + " with the following error message", e);
                    }
                    latch.countDown();
                });
            }
            latch.await();
        }catch (InterruptedException e) {
            log.error("Failed while processing consumer code " + deliveryDetails.getConsumerCode() + " route short name" + deliveryDetails.getRouteShortName(), e);
        } finally {
            if(!threadPoolExecutorTwo.isShutdown())threadPoolExecutorTwo.shutdown();
        }
    }

    private BufferedWriter createLogFile(String logFile) throws IOException {
        BufferedWriter bw_del = new BufferedWriter(new FileWriter(logFile, true));
        bw_del.newLine();

        return bw_del;
    }
}
