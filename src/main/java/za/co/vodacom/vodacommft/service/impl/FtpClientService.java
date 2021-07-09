package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;
import za.co.vodacom.vodacommft.service.IFtpClientService;

import java.io.IOException;
import java.io.PrintWriter;

@Service
public class FtpClientService implements IFtpClientService {


    private static final Logger ftp_client_logger = LoggerFactory.getLogger(FtpClientService.class);

    @Override
    public FTPSClient getFTPSClient(String ftpSSL) {
        boolean sslProtocol = false;
        switch(ftpSSL){
            case "IMPLICIT":
                ftp_client_logger.info("Implicit FTPS");
                sslProtocol = true;
                break;
            case "EXPLICIT":
                ftp_client_logger.info("Explicit FTPS");
                sslProtocol = false;
                break;
        }
        FTPSClient ftpsClient = new FTPSClient("SSL", sslProtocol);
        return ftpsClient;
    }

    @Override
    public boolean logIn(FTPClient ftpClient, String host, int port, String user, String password){
        try{
            // suppress login details
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            ftpClient.setBufferSize(1024000);
            //try to connect
            ftpClient.connect(host, port);
            ftp_client_logger.info("FTP Client connected. about to login with user/pw:" + user + "/" + password);
            //login to server
            if(!ftpClient.login(user, password)) {
                ftpClient.logout();
                return false;
            }
            int reply = ftpClient.getReplyCode();
            //FTPReply stores a set of constants for FTP reply codes.
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return false;
            }
            //get system name
            ftp_client_logger.info("Remote system is " + ftpClient.getSystemType());
            return true;
        }catch(Exception loginEx){
            ftp_client_logger.error("FTP Login Error", loginEx);
            return false;
        }
    }

    @Override
    public FTPClient openFtpConnection(FTPClient ftpClient, String host, int port, String user, String password){
        try{
            // suppress login details
            ftp_client_logger.info("About to pass FTP Client connection Details.....");
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            ftpClient.setBufferSize(1024000);
            //try to connect
            ftp_client_logger.info("About to do FTP Connection....");
            ftpClient.connect(host, port);
            ftp_client_logger.info("FTP Client connected. About to login with Username:- " + user + "/" + password);
            //login to server
            if(!ftpClient.login(user, password)) {
                ftpClient.logout();
                return null;
            }
            int reply = ftpClient.getReplyCode();
            //FTPReply stores a set of constants for FTP reply codes.
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return null;
            }
            //get system name
            ftp_client_logger.info("Remote system is " + ftpClient.getSystemType());
            return ftpClient;
        }catch(Exception loginEx){
            ftp_client_logger.error("FTP Login Error", loginEx);

            return null;
        }
    }

    @Override
    public void closeFtpConnection(FTPClient ftpClient) {

        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            ftp_client_logger.error("FTP Logout Error", e.getCause());
        }
    }

    public void ftpProtectionLevel (FTPSClient ftpsClient, DeliveryDetailsDTO deliveryDetailsDTO){
        try {
            if(!deliveryDetailsDTO.getFtp_ssl().equals("NONE")){
                if(!deliveryDetailsDTO.getFtp_prot_level().equals("NONE")){
                    ftpsClient.execPROT(deliveryDetailsDTO.getFtp_prot_level());
                }else{
                    ftp_client_logger.info("No protection level specified for FTPS transfer. Please investigate.");
                }
            }else{
                ftp_client_logger.info("Not setting protection level as this is a standard FTP transfer.");
            }
        } catch (IOException ex) {
            ftp_client_logger.error("Set FTPS protection level Error", ex);
        }
    }


    @Override
    public boolean ftpQuoteCommand(FTPClient ftpClient, String quoteCommand) throws IOException {
        int serverReply = ftpClient.sendCommand(quoteCommand);
        ftp_client_logger.info("FTP Client executed command " + quoteCommand + "\nServer Reply on executed command: " + serverReply);
        return true;
    }

    @Override
    public void ftpTransferType(FTPClient ftpClient, String transferType) throws IOException {
        switch (transferType.toUpperCase()) {
            case "ASCII":
                ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
                ftp_client_logger.info("FTP Transfer Type set to ASCII");
                break;
            case "BINARY":
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftp_client_logger.info("FTP Transfer Type set to BINARY");
                break;
            default:
                ftp_client_logger.info("No TransferType specified.");
                break;
        }
    }

    public void ftpConnectionType(FTPClient ftpClient, String connectionType){
        switch (connectionType.toUpperCase()) {
            case "ACTIVE":
                ftpClient.enterLocalActiveMode();
                ftp_client_logger.info("FTP Connection Type set to Active");
                break;
            case "PASSIVE":
                ftpClient.enterLocalPassiveMode();
                ftp_client_logger.info("FTP Connection Type set to Passive");
                break;
            default:
                ftp_client_logger.info("No ConnectionType specified.");
                break;
        }
    }

    protected void logOut(FTPClient ftpClient) {
        try {
            ftp_client_logger.info("Attempting to Log out and disconnect FTP Client");
            if(ftpClient.isConnected()){
                ftpClient.logout();
                ftpClient.disconnect();
                ftp_client_logger.info("Logged out and disconnected FTP Client successfully");
            }
        } catch (IOException ex) {
            ftp_client_logger.info("FTP Logout Error" + ex);
        }
    }
}
