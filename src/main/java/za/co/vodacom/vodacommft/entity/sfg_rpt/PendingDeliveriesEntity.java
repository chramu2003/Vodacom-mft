package za.co.vodacom.vodacommft.entity.sfg_rpt;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.entity.sfg_rpt
 */

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PENDING_DELIVERIES", schema = "SFG_RPT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PendingDeliveriesEntity implements Serializable {

    private static final long serialVersionUID = 4764688972894467956L;
    @Id
    @Column(name = "DELIVER_UID")
    private String deliverUid;

    @Column(name = "CONSUMER_CODE")
    private String consumerCode;

    @Column(name = "ROUTE_SHORT_NAME")
    private String routeShortName;

    @Column(name = "FILENAME_ON_DISK")
    private String filenameOnDisk;

    @Column(name = "SFG_FILENAME")
    private String sfgFilename;

    @Column(name = "DESTINATION_FILENAME")
    private String destinationFilename;

    @Column(name = "DELIVERY_STATUS")
    private String deliveryStatus;

    @Column(name = "CONSUMER_USER_NAME")
    private String consumerUserName;

    @Column(name = "CONSUMER_PASSWORD")
    private String consumerPassword;

    @Column(name = "PD_UID")
    private String pdUid;

    @Column(name = "WF_ID")
    private String wfId;

    @Column(name = "DATA_FLOW_ID")
    private String dataFlowId;

    @Column(name = "ROUTE_METADATA")
    private String routeMetadata;

    @Column(name = "CONSUMER_PROTOCOL")
    private String consumerProtocol;

    @Column(name = "CONSUMER_HOST")
    private String consumerHost;

    @Column(name = "CONSUMER_PREF_AUTH")
    private String consumerPrefAuth;

    @Column(name = "CONSUMER_REMOTE_DIR")
    private String consumerRemoteDir;

    @Column(name = "FTP_PORT")
    private Integer ftpPort;

    @Column(name = "FTP_CONNECTION_TYPE")
    private String ftpConnectionType;

    @Column(name = "FTP_TRANSFER_TYPE")
    private String ftpTransferType;

    @Column(name = "FTP_CCC")
    private String ftpCCC;

    @Column(name = "FTP_SSL")
    private String ftpSSL;

    @Column(name = "FTP_RETRIES")
    private Integer ftpRetries;

    @Column(name = "FTP_WAIT_INTERVAL")
    private Integer ftpWaitInterval;

    @Column(name = "FTP_CIPHER_STRENGTH")
    private String ftpCipherStrength;

    @Column(name = "SFTP_PROFILE")
    private String sftpProfile;

    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.DATE)
    private Date createTs;

    @Column(name = "PROT_LEVEL")
    private String protLevel;

    @Column(name = "AWS_S3_ACCESS_KEY")
    private String awsS3AccessKey;

    @Column(name = "AWS_S3_SECRET_KEY")
    private String awsS3SecretKey;

    @Column(name = "AWS_S3_REGION")
    private String awsS3Region;

    @Column(name = "AWS_S3_BUCKET_NAME")
    private String awsS3BucketName;

    @Column(name = "AWS_S3_BUCKET_FOLDER_NAME")
    private String awsS3BucketFolderName;

    @Column(name = "AWS_S3_STORAGE_CLASS")
    private String awsS3StorageClass;
}
