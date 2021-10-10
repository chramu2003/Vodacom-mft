package za.co.vodacom.vodacommft.service;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service
 */

import com.jcraft.jsch.ChannelSftp;
import za.co.vodacom.vodacommft.dto.DeliveryDetailsDTO;

import java.io.BufferedWriter;
import java.io.IOException;

public interface ISftpClientService {
    ChannelSftp sftpDelLogIn(String preferredAuth,
                             String userName,
                             String password,
                             String hostName,
                             int portNumber,
                             String publicKey);

    void sftpLogOut(ChannelSftp sftpClient, BufferedWriter sftp_lo_bw) throws IOException;
}

