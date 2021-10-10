package za.co.vodacom.vodacommft.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;

public interface IDirectoryService {

    void checkDirectoryExistsAndCreate(String local_dir,  BufferedWriter bw_coll) throws IOException;

    void checkDirectoryExistsAndCreate(String local_dir) throws IOException;;

    Set<String> listFilesInDirectory(String directoryPath) throws IOException;

    Set<String> listDirectoriesInDirectory(String directoryPath) throws  IOException;

    void createDeliveryWorkingDirectories(String full_local_working_dir) throws IOException;

    void cleanTempWorkingDeliveryFiles(String file_path_to_delete);

    boolean checkFileExistence(String full_path_to_file) throws IOException;

    void deleteNonEmptyDirectoryUsingStream(String non_empty_directory) throws IOException;
}
