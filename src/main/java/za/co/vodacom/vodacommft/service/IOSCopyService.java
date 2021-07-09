package za.co.vodacom.vodacommft.service;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service
 */

import java.io.IOException;

public interface IOSCopyService {
    void copyToOS(String srcFile, String destFile) throws IOException;
    boolean renameOsCopyTempFile(String srcFile, String destFile) throws IOException;
}
