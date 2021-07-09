package za.co.vodacom.vodacommft.service.impl;

import com.jcraft.jsch.SftpException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;
import za.co.vodacom.vodacommft.service.IDeliveryDetailsService;
import za.co.vodacom.vodacommft.service.IDeliveryService;
import za.co.vodacom.vodacommft.service.ILockService;
import za.co.vodacom.vodacommft.service.IThreadTuningService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ncubeh on 2020/08/23
 * @package za.co.vodacom.vodacomMFT.service.impl
 */
@Service
public class ThreadTuningService implements IThreadTuningService {
    private final static Logger thread_service_logger = LoggerFactory.getLogger(MFTValidationService.class);

    final static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    @Autowired
    private IDeliveryDetailsService deliveryDetailsService;

    @Autowired
    private IDeliveryService deliveryService;

    @Autowired
    private ILockService lockService;


    @Override
    @SneakyThrows
    public void doFileProcessingWithThreads(String consumerCode, String routeShortName, BufferedWriter bw_del) throws IOException {

        DeliveryDetailsDTO deliveryDetails = deliveryDetailsService.getDeliveriesByConsumerCodeAndRouteShortName(consumerCode, routeShortName);
        if (deliveryDetails != null) {

            for (String fileName : deliveryDetails.getList_of_file_metadata()) {
                threadPool.execute(() -> {
                    try {
                        deliveryService.deliveryFileProcessing(deliveryDetails, fileName);
                    } catch (IOException | SftpException e) {
                        thread_service_logger.error("Failed to process the file " + fileName + " with the following error message", e);
                    } finally {
                        lockService.releaseLock(consumerCode);
                    }
                });
            }

        } else {
            bw_del.write( LocalDateTime.now() + " :- No Processing, Consumer might be Suspended Or Not Found :- ");
            bw_del.newLine();
        }
    }
}
