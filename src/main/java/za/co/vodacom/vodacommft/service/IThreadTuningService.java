package za.co.vodacom.vodacommft.service;

import java.io.BufferedWriter;
import java.io.IOException;

public interface IThreadTuningService {

    void doFileProcessingWithThreads(String consumerCode, String routeShortName, BufferedWriter bw_del) throws IOException;
}
