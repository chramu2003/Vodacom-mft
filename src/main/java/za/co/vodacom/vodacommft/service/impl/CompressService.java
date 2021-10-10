package za.co.vodacom.vodacommft.service.impl;
/**
 * @author Jan & modified by mz-ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.config.PropertiesFileSysConfig;
import za.co.vodacom.vodacommft.entity.sfg_cfg.DeliveryDetailsEntity;
import za.co.vodacom.vodacommft.service.ICompressService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.zip.*;

@Service
public class CompressService implements ICompressService {

    private static final Logger compress_logger = LoggerFactory.getLogger(CompressService.class);

    @Override
    public String compressFile(DeliveryDetailsEntity deliveryDetails,
                               String notificationSourceFile,
                               String localDirectory,
                               String file_value_0,
                               String file_value_1,
                               String file_value_2,
                               String file_value_6,
                               String threadName,
                               BufferedWriter bw_cmp) throws IOException {

        String compressed_file_name = "";
        String file_name_value_2 = "";
        String file_name_value_6 = "";
        String final_compressed_file = "";

        String compress_extension = deliveryDetails.getCompressExt();
        String compress_ext = compress_extension.replaceFirst(".", "");
        Path temp_file_to_compress_dir = createTempDirectoriesForFileArchive(localDirectory + file_value_0);

        Path temp_file_to_compress =  Paths.get(temp_file_to_compress_dir+ "/"+ file_value_2.trim());
        compress_logger.info(new Date().toString()+ ": SFG  File to Compress is : "+temp_file_to_compress.getFileName());

        String source_file_path = file_value_1.trim();
        if (deliveryDetails.getRemoveChars().equalsIgnoreCase("yes") && (Integer.parseInt(deliveryDetails.getRemovePosition().trim()) < Integer.parseInt(deliveryDetails.getCompressPosition().trim()))){
            source_file_path = file_value_6;
        }

        Path source_file = Paths.get(source_file_path);
        if (Files.exists(source_file)){
            Files.copy(source_file, temp_file_to_compress);
        }else {
            Path notify_source_file = Paths.get(notificationSourceFile);
            Files.copy(notify_source_file, temp_file_to_compress);
        }

        switch(compress_ext){
            case "zip": {
                compress_logger.info(LocalDateTime.now() + ": Zip File Compression for Location :- " + temp_file_to_compress.toFile().getAbsolutePath());

                String temp_file_is_compressed = temp_file_to_compress.toFile().getAbsolutePath() + compress_extension;
                zipCompressFile(temp_file_to_compress, temp_file_is_compressed);

                compressed_file_name = temp_file_is_compressed;
                file_name_value_2 = temp_file_to_compress.getFileName() + compress_extension;
                file_name_value_6 = compressed_file_name;
                final_compressed_file = file_name_value_2 + ";" + file_name_value_6;

                break;
            }
            case "gzip": case "gz": {
                compress_logger.info(new Date().toString() + ": GZip File Compression for :- " + temp_file_to_compress + " To  : " + temp_file_to_compress + "." + compress_ext);

                Path gzip_file_is_compressed = Paths.get(temp_file_to_compress.toFile().getAbsolutePath() + compress_extension);
                InputStream fis = Files.newInputStream(temp_file_to_compress);
                GZIPOutputStream gzipOS = new GZIPOutputStream(Files.newOutputStream(gzip_file_is_compressed));
                IOUtils.copy(fis, gzipOS);
                gzipOS.close();

                compress_logger.info(new Date().toString() + ": Zip File Compression Completed... " + gzip_file_is_compressed.toFile().getAbsolutePath() + "  : FileName:- " + gzip_file_is_compressed.getFileName());
                file_name_value_2 = gzip_file_is_compressed.getFileName().toString();
                file_name_value_6 = gzip_file_is_compressed.toFile().getAbsolutePath();
                final_compressed_file = file_name_value_2 + ";" + file_name_value_6;

                break;
            }
            case "bzip": case "bz2": {
                compress_logger.info(new Date().toString() + ": BZip File Compression for :- " + temp_file_to_compress + " To  : " + temp_file_to_compress + "." + compress_ext);

                Path bzip_file_to_compress = Paths.get(temp_file_to_compress.toFile().getAbsolutePath() + compress_extension);
                InputStream fis = Files.newInputStream(temp_file_to_compress);
                BZip2CompressorOutputStream bzipOS = new BZip2CompressorOutputStream(Files.newOutputStream(bzip_file_to_compress));
                IOUtils.copy(fis, bzipOS);
                compress_logger.info(new Date().toString() + ": BZip File Compression Completed...");
                bzipOS.close();

                file_name_value_2 = bzip_file_to_compress.getFileName().toString();
                file_name_value_6 = bzip_file_to_compress.toFile().getAbsolutePath();
                final_compressed_file = file_name_value_2 + ";" + file_name_value_6;

                break;
            }
            case "tar": case "tar.gz": {
                compress_logger.info(": Tar File Compression for :- " + temp_file_to_compress + " To  : " + temp_file_to_compress + "." + compress_ext);

                Path tar_to_compress = Paths.get(temp_file_to_compress.toFile().getAbsolutePath() + compress_extension);
                doCompressATarArchiveFile(temp_file_to_compress.toFile().getAbsolutePath() + compress_extension);

                compress_logger.info(": Tar File Compression Completed...");
                file_name_value_2 = tar_to_compress.getFileName().toString();
                file_name_value_6 = temp_file_to_compress.toFile().getAbsolutePath() + compress_extension;
                final_compressed_file = file_name_value_2 + ";" + file_name_value_6;

                break;
            }
            default: {
                compress_logger.info(": Compress Extension Not Known");
                break;
            }
        }
        return final_compressed_file;
    }


    @Override
    //fileValues contain PD_UID; FILENAME_ON_DISK; DESTINATION_FILENAME; DATA_FLOW_ID; ROUTE_METADATA; DELIVER_UID; COPY_OF_FILENAME_ON_FISK to be replaced with zipped/stripped file
    public String decompressFile(String file_name_on_disc_1,
                                 String file_name_2,
                                 String uncompressDir,
                                 String uncompress_extension,
                                 String thread_name,
                                 BufferedWriter bw_cmp) throws IOException {

        String uncompress_ext = uncompress_extension.replaceFirst(".", "");
        switch(uncompress_ext) {
            case "zip": {
                ZipInputStream zipIS = null;
                FileOutputStream fos = null;
                try {
                    zipIS = new ZipInputStream(new FileInputStream(file_name_on_disc_1));
                    ZipEntry entry = zipIS.getNextEntry();
                    if (entry != null) {
                        fos = new FileOutputStream(uncompressDir + entry.getName());
                        IOUtils.copy(zipIS, fos);
                        file_name_2 = entry.getName();
                    }
                } finally {
                    if (fos != null) fos.close();
                    if (zipIS != null) {
                        zipIS.closeEntry();
                        zipIS.close();
                    }
                }
                break;
            }

            case "gzip":
            case "gz": {
                file_name_2 = file_name_2.substring(0, file_name_2.lastIndexOf('.'));

                GZIPInputStream gzipIS = null;
                FileOutputStream fos = null;
                try {
                    gzipIS = new GZIPInputStream(new FileInputStream(file_name_on_disc_1));
                    fos = new FileOutputStream(uncompressDir + file_name_2);
                    IOUtils.copy(gzipIS, fos);
                } finally {
                    if (fos != null) fos.close();
                    if (gzipIS != null) gzipIS.close();
                }
                break;
            }

            case "bzip":
            case "bz2": {
                file_name_2 = file_name_2.substring(0, file_name_2.lastIndexOf('.'));

                BZip2CompressorInputStream gzipIS = null;
                FileOutputStream fos = null;
                try {
                    gzipIS = new BZip2CompressorInputStream(new FileInputStream(file_name_on_disc_1));
                    fos = new FileOutputStream(uncompressDir + file_name_2);
                    IOUtils.copy(gzipIS, fos);
                } finally {
                    if (fos != null) fos.close();
                    if (gzipIS != null) gzipIS.close();
                }
                break;
            }

            case "tar":
            case "tar.gz": {

                TarArchiveInputStream tarIS = null;
                TarArchiveEntry entry = null;
                FileOutputStream fos = null;

                try {
                    tarIS = new TarArchiveInputStream(new FileInputStream(file_name_on_disc_1));
                    entry = (TarArchiveEntry) tarIS.getNextEntry();
                    if (entry != null) {
                        fos = new FileOutputStream(uncompressDir + entry.getName());
                        IOUtils.copy(tarIS, fos);
                        file_name_2 = entry.getName();
                    }
                } finally {
                    if (fos != null) fos.close();
                    if (tarIS != null) tarIS.close();
                }
                break;
            }
        }
        return file_name_2;
    }

    private void zipCompressFile(Path source, String zipFileName) throws IOException {
        if (!Files.isRegularFile(source)){
            compress_logger.info(new Date().toString()+ ": This is not a regular file, please provide a proper file");
        }else {

            ZipOutputStream zip_out_stream = null;
            FileInputStream file_in_stream = null;
            try {
                zip_out_stream = new ZipOutputStream(new FileOutputStream(zipFileName));
                file_in_stream = new FileInputStream(source.toFile());

                ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
                zip_out_stream.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = file_in_stream.read(buffer)) > 0) {
                    zip_out_stream.write(buffer, 0, len);
                }
            } finally {
                if (file_in_stream != null) file_in_stream.close();
                if (zip_out_stream != null) {
                    zip_out_stream.closeEntry();
                    zip_out_stream.close();
                }
            }
            compress_logger.info(": Done Zipping ....");
        }
    }

    private Path createTempDirectoriesForFileArchive(String path_to_file) throws IOException {
        Path temp_directory = Paths.get(path_to_file);
        if (!Files.exists(temp_directory)){
            compress_logger.info(new Date().toString()+ ": Directory :- "+temp_directory.getFileName() + "  Does not exist... About to Create");
            Files.createDirectory(temp_directory);
        }
        return temp_directory;
    }

    private void doCompressATarArchiveFile(String temp_file_to_compress) throws IOException {
        TarArchiveOutputStream tarArchive = null;
        OutputStream fos = null;
        GZIPOutputStream gzipOS = null;
        InputStream fis = null;
        BufferedInputStream bis = null;

        try {
            Path local_tar_dir = Paths.get(temp_file_to_compress);
            compress_logger.info(": Tar File Name : " + local_tar_dir.getFileName());

            fos = Files.newOutputStream(local_tar_dir);
            gzipOS = new GZIPOutputStream(new BufferedOutputStream(fos));
            tarArchive = new TarArchiveOutputStream(gzipOS);
            /*when i tar a file its throw exception as “is too long ( > 100 bytes) TarArchiveOutputStream”*/
            tarArchive.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);

            Path file = Paths.get(temp_file_to_compress);
            tarArchive.putArchiveEntry(new TarArchiveEntry(file.toFile(), ""));
            fis = Files.newInputStream(file);
            bis = new BufferedInputStream(fis);
            // Write file content to archive
            IOUtils.copy(bis, tarArchive);

        } finally {
            if (bis != null) bis.close();
            if (fis != null) fis.close();
            if (gzipOS != null) gzipOS.close();
            if (fos != null) fos.close();

            if (tarArchive != null) {
                tarArchive.closeArchiveEntry();
                tarArchive.close();
            }
        }
    }
}
