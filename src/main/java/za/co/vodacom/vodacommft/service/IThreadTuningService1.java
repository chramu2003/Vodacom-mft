package za.co.vodacom.vodacommft.service;

import java.io.IOException;

public interface IThreadTuningService1 {

    void doFileProcessingWithThreads(String consumerCode, String routeShortName, String workDirectory, String localDirectory) throws IOException;
}
