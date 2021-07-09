package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.config.PropertiesFileSysConfig;
import za.co.vodacom.vodacommft.service.IPasswordService;
import za.co.vodacom.vodacommft.service.IRenameService;

import java.time.temporal.ValueRange;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class MFTValidationService {
    private final static Logger mft_validate_logger = LoggerFactory.getLogger(MFTValidationService.class);
    final static int min_0_100 = 0;
    final static int max_0_100 = 100;

    final static int min_100_200 = 100;
    final static int max_100_200 = 200;

    final static int min_200_5000 = 200;
    final static int max_200_5000 = 5000;

    final static int min_5000_99999999 = 5000;
    final static int max_5000_99999999 = 99999999;

    @Autowired
    IRenameService renameService;

    @Autowired
    private PropertiesFileSysConfig propertiesFileSysConfig;

    @Autowired
    private IPasswordService passwordService;



    protected String getCTLCollection(String ctl_collection) {
        if (isEmpty(ctl_collection) || ctl_collection.trim().equalsIgnoreCase("no") || ctl_collection.trim().equalsIgnoreCase("n")) {
            ctl_collection = "N";
        }

        if (ctl_collection.trim().equalsIgnoreCase("yes") || ctl_collection.trim().equalsIgnoreCase("y")) {
            ctl_collection = "Y";
        }
        if (!ctl_collection.trim().equals("Y") && !ctl_collection.trim().equals("N")){
            ctl_collection = "N";
        }
        return ctl_collection;
    }

    protected String getLocalFolder(String local_folder) {
        if (isEmpty(local_folder)) {
            local_folder = "/";
        }
        if (!local_folder.endsWith("/"))
            local_folder += "/";
        return local_folder.trim();
    }

    protected String getRetrieveFiles(String retrieve_files) {
        if (retrieve_files == null || !retrieve_files.trim().equalsIgnoreCase("all") && !retrieve_files.trim().equalsIgnoreCase("selected")) {
            retrieve_files = "ALL";
        }
       return retrieve_files.trim().toUpperCase();
    }

    protected static boolean isEmpty(String value_to_validate) {
        return value_to_validate == null || value_to_validate.trim().equalsIgnoreCase("");
    }
    protected static boolean isNull(String value_to_validate) {
        return value_to_validate == null;
    }

    protected String RetrieveOption(String retrieve_option) {
        if (retrieve_option == null || !retrieve_option.trim().equalsIgnoreCase("new") && !retrieve_option.trim().equalsIgnoreCase("index")) {
            retrieve_option = "NEW";
        }
        return retrieve_option.trim().toUpperCase();
    }

    protected String getSFGHost(String inSFGHost) {
        if (isEmpty(inSFGHost)) {
            inSFGHost = propertiesFileSysConfig.getSfgHost();
        }
        return inSFGHost.trim();
    }

    protected int getSFGPort(int inSFGPort) {
        if (inSFGPort == 0){
            inSFGPort = propertiesFileSysConfig.getSfgPort();
        }
        return inSFGPort;
    }

    protected String getCollectionCode(String collection_code) {
        if (collection_code == null) {
            collection_code = "";
        }
        return collection_code.trim();
    }

    protected String getSiteCommand(String site_command) {
        if (isNull(site_command)) {
            site_command = "";
        }
        return site_command.trim();
    }

    protected String getCollectOnRemoteTime(String collect_on_remote_time) {
        if (isNull(collect_on_remote_time)) {
            collect_on_remote_time = "N";
        }
        return collect_on_remote_time.trim();
    }

    protected int getCollectionBatchSize(int in_collection_batch_size) {
        if (in_collection_batch_size < 1){
            in_collection_batch_size = 9999;
        }
        return in_collection_batch_size;
    }

    protected String getprotectedKeyFile(String protected_key_file) {
        if (isNull(protected_key_file)) {
            protected_key_file = "";
        }
        return protected_key_file.trim();
    }

    protected String getProducerCode(String producer_code) {
        if (isNull(producer_code)) {
            producer_code = "";
        }
        return producer_code.trim();
    }

    protected String getExclusionCollectionCode(String exclusion_collection_code) {
        if (isNull(exclusion_collection_code)) {
            exclusion_collection_code = "";
        }
        return exclusion_collection_code.trim();
    }

    protected String getRemoteAction(String remote_action) {
        if (isNull(remote_action) || remote_action.equalsIgnoreCase("NONE")) {
            remote_action = "NONE";
        }
        if (remote_action.trim().equalsIgnoreCase("DELETE")) {
            remote_action = "REMOVE";
        }
        return remote_action.trim().toUpperCase();
    }

    protected String getSFGRename(String sfg_rename) {
        if (isNull(sfg_rename)) {
            sfg_rename = "NO";
        }
        return sfg_rename.trim().toUpperCase();
    }

    protected List<String> getSFGRenamePattern(String sfg_rename_pattern) {
        if (isNull(sfg_rename_pattern)) {
            sfg_rename_pattern = "";
        }
        return buildRenamePatternArray(sfg_rename_pattern.trim());
    }

    protected List<String> getSourceFileRenamePattern(String source_file_rename_pattern) {
        if (isNull(source_file_rename_pattern)) {
            source_file_rename_pattern = "";
        }
        return buildRenamePatternArray(source_file_rename_pattern.trim());
    }

    public String [] getServerFileRenameCases(String newFileNamePattern) {
        String [] server_file_name_case = null;
        if (!isEmpty(newFileNamePattern)){
          server_file_name_case = renameService.renamePatternCases(newFileNamePattern);
        }
        return server_file_name_case;
    }

    public List<String> getServerFileRenameCasesByList(String newFileNamePattern) {
        List <String> server_file_name_case = null;
        if (!isEmpty(newFileNamePattern)){
            server_file_name_case = renameService.buildRenamePatternArray(newFileNamePattern.trim());
        }
        return server_file_name_case;
    }

    protected List<String> getSourceFileRenamePatternList(String source_file_rename_pattern){
       if (isEmpty(source_file_rename_pattern)){
           source_file_rename_pattern = "";
       }
        List<String> sourceFile_rename_pattern_list = buildRenamePatternArray(source_file_rename_pattern.trim());

       return sourceFile_rename_pattern_list;
    }

    protected List<String> getSFGRenamePatternList(String sfg_rename_pattern){
        if (isEmpty(sfg_rename_pattern)){
            sfg_rename_pattern = "";
        }
        List<String> sfg_rename_pattern_list =  buildRenamePatternArray(sfg_rename_pattern);
        return sfg_rename_pattern_list;
    }

    protected String getConnectionType(String connection_type) {
        if (isNull(connection_type)) {
            connection_type = "";
        }
        return connection_type.toUpperCase().trim();
    }

    protected String getFTPTransferType(String ftp_ransfer_type) {
        if (isNull(ftp_ransfer_type)) {
            ftp_ransfer_type = "";
        }
        return ftp_ransfer_type.toLowerCase().trim();
    }

    protected String getHost(String host) {
        if (isNull(host)) {
            host = "";
        }
        return host.trim();
    }

    protected String getUsername(String user_name) {
        if (isNull(user_name)) {
            user_name = "";
        }
       return user_name.trim();
    }

    protected String getPassword(String password) {
        if (isNull(password)) {
            password = "";
        }
        return password.trim();
    }

    protected List<Pattern> getFilter(String filter) {
        if (isEmpty(filter)) {
            filter = ".+";
        }
        String[] values = filter.split(";");
        List<Pattern> includeFilterPattern = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {

            includeFilterPattern.add(Pattern.compile(values[i]));
        }
        return includeFilterPattern;
    }

    protected Integer getFtpPort(Integer ftp_port) {
        if (ftp_port == null) {
            ftp_port = 21;
        }
        return ftp_port;
    }

    protected List<String> buildRenamePatternArray(String renamePattern) {
        List<String> renamePatternArray = new ArrayList<String>();
        String[] renamePatternValues = renamePattern.split(";");
        Pattern patt1 = Pattern.compile("((([UL][\\[][0-9]+[\\-][0-9]+[\\]][*]*))*)");
        Pattern patt2 = Pattern.compile("(([UL])([*]))");
        Pattern patt3 = Pattern.compile("(([UL])([*][\\.][*]))");
        Pattern patt4 = Pattern.compile("[*]");
        Pattern patt5 = Pattern.compile("(([*])([\\.])([UL])([*]))");
        Pattern patt6 = Pattern.compile("(([R])[\\[]([0-9])[\\-]([0-9])[\\]])");
        Pattern patt7 = Pattern.compile("(([A])[\\[](START)[\\]][\\']([0-9a-zA-Z \\_\\-\\.]*)[\\'])");
        Pattern patt8 = Pattern.compile("(([A])[\\[](END)[\\]][\\']([0-9a-zA-Z\\_\\-\\.]*)[\\'])");
        Pattern patt9 = Pattern.compile("(([A])[\\[]([0-9]+)[\\]][\\']([0-9a-zA-Z \\_\\-\\.]*)[\\'])");
        Pattern patt10 = Pattern.compile("(([X])[\\[][\\']([0-9a-zA-Z]*)[\\'][\\]])");

        Matcher matcher1 = null; Matcher matcher2 = null; Matcher matcher3 = null; Matcher matcher4 = null; Matcher matcher5 = null; Matcher matcher6 = null; Matcher matcher7 = null; Matcher matcher8 = null; Matcher matcher9 = null; Matcher matcher10 = null;
        System.out.println(">>>>>>> Length of RenamePatternValues <<<<< " + renamePatternValues.length);
        if (renamePatternValues[0] != "") {
            System.out.println(">>>>>>> RenamePatternValues is not Empty 1 or more elements exist<<<<  " + renamePatternValues.length);
            System.out.println("======= Pattern Value : " + renamePatternValues[0]);
            for (int i = 0; i < renamePatternValues.length; i++)
            {
                System.out.println("======= Looping through the Regex elements :"  +renamePatternValues[i]);

                if ((renamePatternValues[i].substring(0, 2).equalsIgnoreCase("C[")) || (renamePatternValues[i].contains("$Hostname")) || (renamePatternValues[i].contains("$CurrDate")) || (renamePatternValues[i].contains("$PrevDate"))) {
                    System.out.println(">>>>>  Date Regex or CL or $hostname or $CurrDate or $PrevDate Found::-"  +renamePatternValues[i]);
                    renamePatternArray.add("0|" + renamePatternValues[i]);

                    System.out.println(">>>> Added regex to List :"  +renamePatternArray);
                }
                else
                {
                    System.out.println("Mtach patterns values  1- 10  :"  +renamePatternValues[i]);
                    matcher1 = patt1.matcher(renamePatternValues[i]);
                    matcher2 = patt2.matcher(renamePatternValues[i]);
                    matcher3 = patt3.matcher(renamePatternValues[i]);
                    matcher4 = patt4.matcher(renamePatternValues[i]);
                    matcher5 = patt5.matcher(renamePatternValues[i]);
                    matcher6 = patt6.matcher(renamePatternValues[i]);
                    matcher7 = patt7.matcher(renamePatternValues[i]);
                    matcher8 = patt8.matcher(renamePatternValues[i]);
                    matcher9 = patt9.matcher(renamePatternValues[i]);
                    matcher10 = patt10.matcher(renamePatternValues[i]);

                    if (matcher1.matches()) {
                        renamePatternArray.add("1|" + renamePatternValues[i]);
                    }
                    if (matcher2.matches()) {
                        renamePatternArray.add("2|" + renamePatternValues[i]);
                    }
                    if (matcher3.matches()) {
                        renamePatternArray.add("3|" + renamePatternValues[i]);
                    }
                    if (matcher4.matches()) {
                        renamePatternArray.add("4|" + renamePatternValues[i]);
                    }
                    if (matcher5.matches()) {
                        renamePatternArray.add("5|" + renamePatternValues[i]);
                    }
                    if (matcher6.matches()) {
                        renamePatternArray.add("6|" + renamePatternValues[i]);
                    }
                    if (matcher7.matches()) {
                        renamePatternArray.add("7|" + renamePatternValues[i]);
                    }
                    if (matcher8.matches()) {
                        renamePatternArray.add("8|" + renamePatternValues[i]);
                    }
                    if (matcher9.matches()) {
                        renamePatternArray.add("9|" + renamePatternValues[i]);
                    }
                    if (matcher10.matches())
                        renamePatternArray.add("10|" + renamePatternValues[i]);
                }
            }
        }

        return renamePatternArray;
    }

    protected String getQuoteCommand(String quoteCommand) {
        if (isEmpty(quoteCommand) || quoteCommand.toLowerCase().contains("ull")) {
            quoteCommand = "";
        }
        return quoteCommand.trim();
    }

    protected String getFTPSSL(String ftpSSL) {
        if (isEmpty(ftpSSL ) || ftpSSL.toLowerCase().contains("ull") || ftpSSL.equalsIgnoreCase("NONE")){
            ftpSSL = "SSL_NONE";
        }
        return ftpSSL.trim();
    }

    public boolean checkSftpFileExists(ChannelSftp channelSftp, String path) {
        Vector check_results = null;
        try {
            check_results = channelSftp.ls(path);
        } catch (SftpException e) {
            if (e.id == channelSftp.SSH_FX_NO_SUCH_FILE) {
                mft_validate_logger.info(": Check Results is  Empty or Null .. ..this will return a false. SFTP file does not Exist");
                return false;
            }
        }
        return check_results != null && !check_results.isEmpty();
    }

    protected String getDecyrptedPassword(String passwd){
        String theSalt = propertiesFileSysConfig.getSalt();
        String decrypted_psswd = "";
        if (passwd == null) {
            decrypted_psswd = "";
        }
        if ((passwd != null) && (passwd != "")){
            try {
                decrypted_psswd =  passwordService.decrypt(passwd.trim(), theSalt);
            } catch (Exception ex) {
                mft_validate_logger.error("Unable to process password in getDecryptedPassword() method : " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return decrypted_psswd;
    }

    protected boolean rangeValidationOf(int value_to_validate, int min_value, int max_value){
        boolean value_in_range = false;
        if(ValueRange.of(min_value, max_value).isValidIntValue(value_to_validate)) {
            value_in_range = true;
        }
        return value_in_range;
    }

    protected int getPartitionSize(int total_count_of_files){
        int partition_size = 0;

        if (ValueRange.of(min_0_100, max_0_100).isValidIntValue(total_count_of_files)) {
            mft_validate_logger.info(total_count_of_files + " is in Range of 0 -100");
            partition_size = Math.round(total_count_of_files / propertiesFileSysConfig.getThreadTuneValueFor200rLess());

        } else if (ValueRange.of(min_100_200, max_100_200).isValidIntValue(total_count_of_files)) {
            mft_validate_logger.info(total_count_of_files + " is in Range 100 -200");
            partition_size = Math.round(total_count_of_files / propertiesFileSysConfig.getThreadTuneValueFor200rLess());
        } else if (ValueRange.of(min_200_5000, max_200_5000).isValidIntValue(total_count_of_files)) {
            mft_validate_logger.info(total_count_of_files + " :is in Range 200 -5000");
            partition_size = Math.round(total_count_of_files / propertiesFileSysConfig.getThreadTuneValueFor200rLess());

        } else if (ValueRange.of(min_5000_99999999, max_5000_99999999).isValidIntValue(total_count_of_files)) {
            mft_validate_logger.info(total_count_of_files + " :Its 5k to 99M");
            partition_size = Math.round(total_count_of_files / propertiesFileSysConfig.getThreadTuneValueFor200rLess());
        }
        return partition_size;
    }
}
