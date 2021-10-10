package za.co.vodacom.vodacommft.service.impl;
/**
 * @author jan & modified by mz herbie on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.dto
 */

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.service.ISftpClientService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

@Service
public class SftpClientService implements ISftpClientService {

    private static final Logger sftp_logger = LoggerFactory.getLogger(SftpClientService.class);

    public ChannelSftp sftpDelLogIn(String preferredAuth,
                                    String userName,
                                    String password,
                                    String hostName,
                                    int portNumber,
                                    String publicKey) {
        JSch jsch;
        Session session = null;
        Channel channel = null;
        ChannelSftp sftpClient = null;

        switch(preferredAuth.toUpperCase()){

            case "PASSWORD": {
                sftp_logger.info(new Date().toString() + ": Starting password authentication... About to get SFTP Session..");
                try {
                    jsch = new JSch();
                    session = jsch.getSession(userName, hostName, portNumber);
                    sftp_logger.info(new Date().toString() + ": SFTP Session created...");

                    session.setPassword(password);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.connect();
                    channel = session.openChannel("sftp");
                    channel.connect();
                    sftp_logger.info(new Date().toString() + "shell channel connected....");
                    sftpClient = (ChannelSftp) channel;
                } catch (JSchException pwEx) {

                    if (session != null) session.disconnect();
                    if (channel != null) channel.disconnect();

                    return sftpClient;

                } catch (Exception pwEx) {
                    if (session != null) session.disconnect();
                    if (channel != null) channel.disconnect();

                    return sftpClient;
                }
                break;
            }

            case "PUBLICKEY": {
                boolean alreadyRetried = false;
                try {
                    jsch = new JSch();
                    sftpClient = getSftpChannel(sftpClient, userName, hostName, portNumber, publicKey);
                } catch (JSchException sess) {
                    sftp_logger.error("Session exception:" + sess.getMessage());
                    if (!alreadyRetried) {
                        if (publicKey.endsWith("_dsa")) { //Change dsa to rsa and vice versa if initial authentication fails
                            publicKey = publicKey.replace("_dsa", "_rsa");
                        } else if (publicKey.endsWith("_rsa")) {
                            publicKey = publicKey.replace("_rsa", "_dsa");
                        }
                        //Last try
                        alreadyRetried = true;
                        try {
                            sftpClient = getSftpChannel(sftpClient, userName, hostName, portNumber, publicKey);
                        } catch (JSchException retryEx) {
                            sftp_logger.error("SFTP Connection failed ... with error below:- ", retryEx);
                            return sftpClient;
                        }
                    }
                }
                break;
            }
            default:
                sftp_logger.info(new Date().toString()+ "  NO PREFERRED AUTH SPECIFIED......:- "+ preferredAuth);
                break;
        }
        sftp_logger.info(new Date().toString()+ "  About to Return a Connection.....");
        return sftpClient;
    }

    private ChannelSftp getSftpChannel(ChannelSftp sftpClient,
                                       String userName,
                                       String hostName,
                                       int portNumber,
                                       String publicKey) throws JSchException {
        JSch jsch = new JSch();
        Session session;
        Channel channel;

        session = jsch.getSession(userName, hostName, portNumber);
        session.setPassword("");
        sftp_logger.info("empty password added ");
        session.setConfig("StrictHostKeyChecking", "no");
        jsch.addIdentity(publicKey);
        session.connect();

        if(session.isConnected()){
            channel = session.openChannel("sftp");
            channel.connect();
            sftp_logger.info("shell channel connected....");
            sftpClient = (ChannelSftp) channel;
        }
        return sftpClient;
    }


    public void sftpLogOut(ChannelSftp sftpClient, BufferedWriter sftp_lo_bw) throws IOException {
        try {
            if (sftpClient != null) {
                Channel channel =  sftpClient;
                if (channel != null) {
                    Session session = channel.getSession();
                    if (session != null) session.disconnect();
                    channel.disconnect();
                }
                if (sftpClient.getSession() != null) sftpClient.getSession().disconnect();
                sftpClient.disconnect();
            }
            sftp_lo_bw.write(new Date().toString()+": Sftp client disconnected successfully");
            sftp_lo_bw.newLine();
        } catch (JSchException e) {
            sftp_logger.info("Error closing SFTP connection: "+ e.getCause());
        }
    }
}

