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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DirectoryService implements IDirectoryService {

    private static final Logger dir_service_logger = LoggerFactory.getLogger(DirectoryService.class);

    @Override
    public void checkDirectoryExistsAndCreate(String local_dir, BufferedWriter bw_coll) throws IOException {
        Path stage_dir = Paths.get(local_dir + "stageDir");
        Path input_dir =Paths.get(local_dir + "inputDir");

        dir_service_logger.info(new Date().toString()+": Checking if working Directories exist");
        bw_coll.write(new Date().toString()+": Checking if working Directories exist");
        bw_coll.newLine();

        if(!Files.exists(stage_dir)){
            dir_service_logger.info(new Date().toString()+": Stage Directory does not exist. Creating Directory :- " + stage_dir.getFileName());
            bw_coll.write(new Date().toString()+": Stage Directory does not exist. Creating Directory :- " + stage_dir.getFileName());
            bw_coll.newLine();
            Files.createDirectory(stage_dir);  /*NOTES:-  stage_dir.toFile().mkdir() works as well*/
            dir_service_logger.info(new Date().toString()+": Stage Directory created successfully :-  "+ stage_dir.getFileName());
            bw_coll.write(new Date().toString()+": Stage Directory created successfully :-  "+ stage_dir.getFileName());
            bw_coll.newLine();
        }else{
            dir_service_logger.info(new Date().toString()+": Stage Directory : - " +stage_dir.getFileName() +" Exist. No Creation...");
            bw_coll.write(new Date().toString()+": Stage Directory : - " +stage_dir.getFileName() +" Exist. No Creation...");
            bw_coll.newLine();
        }
        if(!Files.exists(input_dir)){
            dir_service_logger.info(new Date().toString()+": Input Directory does not exist. Creating Directory :- " + input_dir.getFileName());
            bw_coll.write(new Date().toString()+": Input Directory does not exist. Creating Directory :- " + input_dir.getFileName());
            bw_coll.newLine();
            Files.createDirectory(input_dir);
            dir_service_logger.info(new Date().toString()+": Input Directory created successfully :-  "+input_dir.getFileName());
            bw_coll.write(new Date().toString()+": Input Directory created successfully :-  "+input_dir.getFileName());
            bw_coll.newLine();
        }else{
            dir_service_logger.info(new Date().toString()+": Input Directory : - " +input_dir.getFileName() +" does exist.");
            bw_coll.write(new Date().toString()+": Input Directory : - " +input_dir.getFileName() +" does exist.");
            bw_coll.newLine();
        }
    }

    public void checkDirectoryExistsAndCreate(String local_dir) throws IOException {
        Path stage_dir = Paths.get(local_dir );
        Path input_dir =Paths.get(local_dir);

        dir_service_logger.info(new Date().toString()+": Checking if working Directories exist");
        //bw_coll.write(new Date().toString()+": Checking if working Directories exist");

        if(!Files.exists(stage_dir)){
            dir_service_logger.info(new Date().toString()+": Local Working  Directory does not exist. Creating Directory :- " + stage_dir.getFileName());
           // bw_coll.write(new Date().toString()+": Checking if working Directories exist");
            Files.createDirectory(stage_dir);  /*NOTES:-  stage_dir.toFile().mkdir() works as well*/
            dir_service_logger.info(new Date().toString()+": Local Working Directory created successfully :-  "+ stage_dir.getFileName());
            //bw_coll.write(new Date().toString()+": Checking if working Directories exist");
        }else{
            dir_service_logger.info(new Date().toString()+": Local Working Directory : - " +stage_dir.getFileName() +" Exist. No Creation...");
            //bw_coll.write(new Date().toString()+": Checking if working Directories exist");
        }
       /* if(!Files.exists(input_dir)){
            dir_service_logger.info(new Date().toString()+": Input Directory does not exist. Creating Directory :- " + input_dir.getFileName());
            //bw_coll.write(new Date().toString()+": Checking if working Directories exist");
            Files.createDirectory(input_dir);
            dir_service_logger.info(new Date().toString()+": Input Directory created successfully :-  "+input_dir.getFileName());
           // bw_coll.write(new Date().toString()+": Checking if working Directories exist");
        }else{
            dir_service_logger.info(new Date().toString()+": Input Directory : - " +input_dir.getFileName() +" does exist.");
            //bw_coll.write(new Date().toString()+": Checking if working Directories exist");
        }*/
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
            /*bw_cdir.write(LocalDateTime.now() +": Working Directory Does Not Exist .. New Delivery Stream . Creating for : - " +create_working_dir);
            bw_cdir.newLine();*/
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
           /* bw_ctwd.write(LocalDateTime.now() +": About to Clean regular Files using Walk Impl.... :- "+ file_path_to_delete);
            bw_ctwd.newLine();*/

            if (checkFileExistence(file_path_to_delete)){
                Stream<Path> walk = Files.walk(Paths.get(file_path_to_delete));
                // If We want to find & Delete  only regular files ONLY using Lambada... lovely
                if (walk != null) {
                    walk.filter(Files::isRegularFile).map(x -> x.toFile()).forEach(File::delete);

                    /*bw_ctwd.write(new Date().toString()+": Done Cleaning Directories :- " + file_path_to_delete);
                    bw_ctwd.newLine();*/
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

        /*bw_fe.write(LocalDateTime.now() +": " + file_to_be_checked + " exists :- "+ file_exist);
        bw_fe.newLine();
*/
        return file_exist;
    }
}
