package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.service.IOSCopyService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class OSCopyService implements IOSCopyService {

    @Override
    public void copyToOS(String srcFile, String destFile) throws IOException {
        Path source_file = Paths.get(srcFile);
        Path destination_file = Paths.get(destFile);
        Files.copy(source_file, destination_file , StandardCopyOption.REPLACE_EXISTING); /*The copy fails if the target file exists, unless the REPLACE_EXISTING option is specified. No worries we have TS on dest*/
    }

    @Override
    public boolean renameOsCopyTempFile(String srcFile, String destFile) {
        File source = new File(srcFile);
        File destination = new File(destFile);
        return source.renameTo(destination);
    }
}
