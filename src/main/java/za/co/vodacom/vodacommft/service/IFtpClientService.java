package za.co.vodacom.vodacommft.service;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;

import java.io.IOException;

/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service
 */


public interface IFtpClientService {

    boolean logIn(FTPClient ftpClient, String host, int port, String user, String password);

    FTPSClient getFTPSClient(String ftpSSL);

    FTPClient openFtpConnection(FTPClient ftpClient, String host, int port, String user, String password);

    void closeFtpConnection(FTPClient ftpClient);

    boolean ftpQuoteCommand(FTPClient ftpClient, String quoteCommand) throws IOException;

    void ftpTransferType(FTPClient ftpClient, String transferType) throws IOException;

    void ftpConnectionType(FTPClient ftpClient, String connectionType) throws IOException;
}
