package za.co.vodacom.vodacommft.service;

import java.io.IOException;
import java.util.List;

public interface IThreadTuningService {

    void doFileProcessing(String consumerCode, String routeShortName, String workDirectory, String localDirectory) throws IOException;
}
