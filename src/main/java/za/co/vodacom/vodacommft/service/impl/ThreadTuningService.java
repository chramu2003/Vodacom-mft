package za.co.vodacom.vodacommft.service.impl;

import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;
import za.co.vodacom.vodacommft.service.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ncubeh on 2020/08/23
 * @package za.co.vodacom.vodacomMFT.service.impl
 */
@Service
public class ThreadTuningService implements IThreadTuningService {
    private final static Logger thread_service_logger = LoggerFactory.getLogger(MFTValidationService.class);

    private static ThreadPoolExecutor threadPoolExecutorSplitter = null;
    private static ThreadPoolExecutor threadPoolExecutorTen = null;
    private static ThreadPoolExecutor threadPoolExecutorFive = null;
    private static ThreadPoolExecutor threadPoolExecutorTwo = null;

    @Autowired
    private IDeliveryDetailsService deliveryDetailsService;

    @Autowired
    private IDeliveryService deliveryService;

    @Autowired
    private IDirectoryService directory_service;

    @Autowired
    private ILockService lockService;


    @Override
    public void doFileProcessingWithThreads(List<String[]> pendingDeliveryList, String workDirectory, String localDirectory) throws IOException {

        int totalActiveThreads = 0;
        if (threadPoolExecutorSplitter != null && !threadPoolExecutorSplitter.isShutdown()) totalActiveThreads += threadPoolExecutorSplitter.getActiveCount();
        if (threadPoolExecutorTen != null && !threadPoolExecutorTen.isShutdown()) totalActiveThreads += threadPoolExecutorTen.getActiveCount();
        if (threadPoolExecutorFive != null && !threadPoolExecutorFive.isShutdown()) totalActiveThreads += threadPoolExecutorFive.getActiveCount();
        if (threadPoolExecutorTwo != null && !threadPoolExecutorTwo.isShutdown()) totalActiveThreads += threadPoolExecutorTwo.getActiveCount();

        if (totalActiveThreads < 30) {
            if (pendingDeliveryList != null && pendingDeliveryList.size() > 0) {
                if (threadPoolExecutorSplitter == null || threadPoolExecutorSplitter.isShutdown()) threadPoolExecutorSplitter = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
                CountDownLatch latch = new CountDownLatch(pendingDeliveryList.size());
                for (String[] pendingDelivery : pendingDeliveryList) {
                    threadPoolExecutorSplitter.execute(() -> {
                        String consumerCode = pendingDelivery[0];
                        String routeShortName = pendingDelivery[1];
                        BufferedWriter bw_del = null;
                        try {
                            String logFile = workDirectory + consumerCode + ".log";
                            bw_del = createLogFile(logFile);
                            directory_service.cleanTempWorkingDeliveryFiles(logFile);

                            if (!lockService.checkIfLockExits(consumerCode)) {
                                bw_del.write(LocalDateTime.now() + "  No Locks For Delivery ITEM_NAME :- " + consumerCode + "\nLocking And Delivery File Processing.....");
                                bw_del.newLine();

                                if (lockService.addLock(consumerCode)) {

                                    String deliveryCode = consumerCode + "~" + routeShortName;
                                    String workingDirectory = localDirectory + deliveryCode + "/";
                                    directory_service.createDeliveryWorkingDirectories(workingDirectory);
                                    directory_service.cleanTempWorkingDeliveryFiles(workingDirectory);

                                    DeliveryDetailsDTO deliveryDetails = deliveryDetailsService.getDeliveriesByConsumerCodeAndRouteShortName(consumerCode, routeShortName);
                                    if (deliveryDetails != null) {
                                        List<String> files = deliveryDetails.getList_of_file_metadata();
                                        int fileCount = files.size();

                                        if (fileCount > 0 && fileCount <= 20) {
                                            doDeliveryWithTwoThreads(deliveryDetails, fileCount);
                                            latch.countDown();
                                        } else if (fileCount > 20 && fileCount <= 50) {
                                            doDeliveryWithFiveThreads(deliveryDetails, fileCount);
                                            latch.countDown();
                                        } else if (fileCount > 50) {
                                            doDeliveryWithTenThreads(deliveryDetails, fileCount);
                                            latch.countDown();
                                        } else {
                                            latch.countDown();
                                        }
                                    }
                                } else {
                                    latch.countDown();
                                }
                            } else {
                                latch.countDown();
                            }
                        }catch (IOException e){
                            thread_service_logger.error("Error while routing file processing ", e);
                        } finally {
                            if(!threadPoolExecutorSplitter.isShutdown())threadPoolExecutorSplitter.shutdown();
                            if (bw_del != null) {
                                try {
                                    bw_del.close();
                                } catch (IOException e) {
                                    thread_service_logger.error("Failed to close Buffered Writer", e);
                                }
                            }
                        }
                    });
                }

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    thread_service_logger.error("Failed to wait for threads doing files routing", e);
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
                        latch.countDown();
                    } catch (IOException | SftpException e) {
                        thread_service_logger.error("Failed to process the file " + fileName + " with the following error message", e);
                    }
                });
            }
            latch.await();
        }catch (InterruptedException e) {
            thread_service_logger.error("Failed while processing consumer code " + deliveryDetails.getConsumerCode() + " route short name" + deliveryDetails.getRouteShortName(), e);
        } finally {
            lockService.releaseLock(deliveryDetails.getConsumerCode());
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
                        latch.countDown();
                    } catch (IOException | SftpException e) {
                        thread_service_logger.error("Failed to process the file " + fileName + " with the following error message", e);
                    }
                });
            }
            latch.await();
        }catch (InterruptedException e) {
            thread_service_logger.error("Failed while processing consumer code " + deliveryDetails.getConsumerCode() + " route short name" + deliveryDetails.getRouteShortName(), e);
        } finally {
            lockService.releaseLock(deliveryDetails.getConsumerCode());
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
                        latch.countDown();
                    } catch (IOException | SftpException e) {
                        thread_service_logger.error("Failed to process the file " + fileName + " with the following error message", e);
                    }
                });
            }
            latch.await();
        }catch (InterruptedException e) {
            thread_service_logger.error("Failed while processing consumer code " + deliveryDetails.getConsumerCode() + " route short name" + deliveryDetails.getRouteShortName(), e);
        } finally {
            lockService.releaseLock(deliveryDetails.getConsumerCode());
            if(!threadPoolExecutorTwo.isShutdown())threadPoolExecutorTwo.shutdown();
        }
    }

    private BufferedWriter createLogFile(String logFile) throws IOException {
        BufferedWriter bw_del = new BufferedWriter(new FileWriter(logFile, true));
        bw_del.newLine();

        return bw_del;
    }
}
