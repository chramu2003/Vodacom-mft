package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.service.IDirectoryService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class DirectoryService implements IDirectoryService {

    private static final Logger dir_service_logger = LoggerFactory.getLogger(DirectoryService.class);

    @Override
    public void checkDirectoryExistsAndCreate(String local_dir, BufferedWriter bw_coll) throws IOException {
        Path stage_dir = Paths.get(local_dir + "stageDir");
        Path input_dir =Paths.get(local_dir + "inputDir");

        if(!Files.exists(stage_dir)){
            Files.createDirectory(stage_dir);  /*NOTES:-  stage_dir.toFile().mkdir() works as well*/
            dir_service_logger.info(": Stage Directory created successfully :-  "+ stage_dir.getFileName());
            bw_coll.write(LocalDateTime.now() +": Stage Directory created successfully :-  "+ stage_dir.getFileName());
            bw_coll.newLine();
        }

        if(!Files.exists(input_dir)){
            Files.createDirectory(input_dir);
            dir_service_logger.info(": Input Directory created successfully :-  "+input_dir.getFileName());
            bw_coll.write(LocalDateTime.now() +": Input Directory created successfully :-  "+input_dir.getFileName());
            bw_coll.newLine();
        }
    }

    public void checkDirectoryExistsAndCreate(String local_dir) throws IOException {
        Path stage_dir = Paths.get(local_dir );
        dir_service_logger.info(": Checking if working Directories exist");
        if(!Files.exists(stage_dir)){
            Files.createDirectory(stage_dir);  /*NOTES:-  stage_dir.toFile().mkdir() works as well*/
            dir_service_logger.info(": Local Working Directory created successfully :-  "+ stage_dir.getFileName());
        }
    }

    @Override
    public Set<String> listFilesInDirectory(String directoryPath) throws IOException {
        Set<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.getFileName().toString());
                }
            }
        }
        return fileList;
    }

    @Override
    public Set<String> listDirectoriesInDirectory(String directoryPath) throws IOException {
        Set<String> directoryList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    directoryList.add(path.getFileName().toString());
                }
            }
        }
        return directoryList;
    }

    @Override
    public void createDeliveryWorkingDirectories(String full_local_working_dir) throws IOException {

        Path create_working_dir = Paths.get(full_local_working_dir + "errors/delivery");
        if (!Files.exists(create_working_dir)){
            try {
                Files.createDirectories(create_working_dir);
            } catch (IOException e) {
                dir_service_logger.error("Error creating directories", e);
            }
        }
    }

    @Override
    public void cleanTempWorkingDeliveryFiles(String file_path_to_delete)  {
        try{
            if (checkFileExistence(file_path_to_delete)){
                Stream<Path> walk = Files.walk(Paths.get(file_path_to_delete));
                // If We want to find & Delete  only regular files ONLY using Lambada... lovely
                if (walk != null) {
                    walk.filter(Files::isRegularFile).map(x -> x.toFile()).forEach(File::delete);
                }
            }
        } catch (IOException e) {
            dir_service_logger.error("Error cleaning Temporary working directories", e);
        }
    }

    @Override
    public void deleteNonEmptyDirectoryUsingStream(String non_empty_directory) throws IOException {
        Path rootDirectory = Paths.get(non_empty_directory);
        Files.walk(rootDirectory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Override
    public boolean checkFileExistence(String full_file_name) throws IOException {
        Path file_to_be_checked = Paths.get(full_file_name);
        boolean file_exist = Files.exists(file_to_be_checked);

        return file_exist;
    }
}
