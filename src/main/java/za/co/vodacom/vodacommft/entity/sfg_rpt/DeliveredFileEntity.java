package za.co.vodacom.vodacommft.entity.sfg_rpt;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.entity.sfg_rpt
 */

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "DELIVERED_FILE", schema = "SFG_RPT")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeliveredFileEntity implements Serializable {
    private static final long serialVersionUID = 4653723403804690717L;
    @Id
    @GeneratedValue
    @Column(name = "DELIVER_UID") // NOT NULL
    private Integer deliverUid;

    @Column(name = "MBX_MSG_ID") // DEFAULT NULL
    private String mbxMsgId;

    @Column(name ="ARRIVEDFILE_KEY")  // NOT NULL
    private String arrivedFileKey;

    @Column(name ="DELIVERY_CNT")   //NOT NULL
    private String deliveryCnt;

    @Column(name ="ROUTE_UID")    //DEFAULT NULL
    private Integer routeUid;

    @Column(name ="ROUTE_SHORT_NAME")   //DEFAULT NULL
    private String routeShortName;

    @Column(name ="CONSUMER_CODE")   // NOT NULL
    private String consumerCode;

    @Column(name ="CONSUMER_MBX_WRITE_TS")    //DEFAULT NULL
    private Date consumerMbxWriteTs;

    @Column(name ="CONSUMER_DELIVER_TS")   //DEFAULT NULL
    private Date consumerDeliverTs;

    @Column(name ="FILE_NAME")   //DEFAULT NULL
    private String fileName;

    @Column(name ="FILE_SIZE")   //DEFAULT NULL
    private String fileSize;

    @Column(name ="DELIVERY_STATUS")  //NOT NULL
    private String deliveryStatus;

    @Column(name ="AFTER_CENTERA_ADDRESS")  //DEFAULT NULL
    private String AfterCenteraAddress;

    @Column(name ="AFTER_CENTERA_DOC_ID")   //DEFAULT NULL
    private String AfterCenteraDocId;

    @Column(name ="PROCESSOR_UID") //DEFAULT NULL
    private String processorUid;

    @Column(name ="PROCESSING_PRIORITY") //DEFAULT NULL
    private String processingPriority;

    @Column(name ="DELIVERY_PROTOCOL")   // DEFAULT NULL
    private String deliveryProtocol;

    @Column(name ="PROC_TS")  //DEFAULT SYSDATE
    private Date procTs;

    @Column(name ="TAPIN_EXPECTED_CNT")  //DEFAULT NULL
    private Integer tapinExpectedCnt;

    @Column(name ="TAPIN_RECEIVED_CNT") //DEFAULT NULL
    private Integer tapinReceivedCnt;

    @Column(name ="REDELIVER_TS") //DEFAULT NULL
    private Date redeliverTs;

    @Column(name ="REPLAY_IND")  //DEFAULT 'no'
    private String replayInd;

    @Column(name ="REDELIVER_IND") //DEFAULT 'no'
    private String redeliverInd;

    @Column(name ="ERROR")  //DEFAULT NULL
    private String errror;

    @Column(name ="MBX_DEL_IND")
    private  String mbxDelInd;

    @Column(name ="FILE_TYPE") //DEFAULT 'file' NOT NULL
    private String fileType;

    @Column(name ="DELIVERY_KEY" )
    private String deliverKey;

    @Column(name ="DELIVERED_CNT") //DEFAULT 1 NOT NULL
    private Integer deliveredCnt;

    @Column(name ="REDELIVER_CNT")   //DEFAULT 0 NOT NULL
    private Integer redeliverCnt;

    @Column(name ="NOTIFICATION_FILE_NAME")
    private String notificationFileName;

    @Column(name ="DELETED_FROM_CENTERA")
    private String deletedFromCentera;

    @Column(name ="DELETED_FROM_CENTERA_TS")
    private Date deletedFromCenteraTs;

    @Column(name ="FILENAME_ON_DISK")
    private String fileOnDisk;

    @Column(name ="AFTER_S3_VERSION_ID")
    private String afterS3VersionId;

    @Column(name ="AFTER_S3_DOC_ID")
    private String afterS3DocId;

    @Column(name ="DELETED_FROM_S3")
    private String deletedFromS3;

    @Column(name ="DELETED_FROM_S3_TS DATE")
    private Date deletedFromS3Ts;

    @Column(name ="S3_BUCKET_NAME")
    private String s3BucketName;
}
