package za.co.vodacom.vodacommft.service;

import java.io.IOException;
import java.util.List;

public interface IThreadTuningService {

    void doFileProcessingWithThreads(List<String[]> pendingDeliveryList, String workDirectory, String localDirectory) throws IOException;
}
