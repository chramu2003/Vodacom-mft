package za.co.vodacom.vodacommft.entity.sfg_rpt;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.entity.sfg_rpt
 */

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "PROCESSED_FILE", schema = "SFG_RPT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessedFileEntity implements Serializable {

    private static final long serialVersionUID = -3599856225400080895L;
    @Id
    @NotNull
    @Column(name = "FILE_UID")
    private Integer fileUid;

    @Column(name = "ARRIVEDFILE_KEY")
    //@NotNull
    private String arrivedFileKey;

    @Column(name = "ROUTE_UID")
	private Integer routeUid;

    @Column(name = "ROUTE_SHORT_NAME")
	private String routeShortName;

    @Column(name = "FILE_NAME")
    @NotNull
	private String fileName;

    @Column(name = "FILE_STATUS")
	private String fileStatus;

    @Column(name = "FILE_SIZE")
	private String fileSize;

    @Column(name = "PRODUCER_CODE")
	private String producerCode;

    @Column(name = "MBX_MSG_ID")
	private String mbxMsgId;

    @Column(name = "PRODUCER_MBX_NAME")
	private String producerMbxName;

    @Column(name = "PRODUCER_MBX_WRITE_TS")
	private String producerMbxWriteTs;

    @Column(name = "USE_UNCOMPRESS")
    private String useUncompress; //DEFAULT 'None',

    @Column(name = "CENTERA_ADDRESS")
    private String centeraAddress;

    @Column(name = "FILE_HASH")
	private String fileHash;

    @Column(name = "SFG_PROC_START_TS")
	private Date sfgProcStartTs;

    @Column(name = "SFG_PROC_END_TS")
    private Date sfgProcEndTs;

    @Column(name = "CONSUMER_CNT")
    private Integer consumerCnt;

    @Column(name = "DF_NAME")
	private String dfName;

    @Column(name = "DF_ITEM_CNT")
	private Integer dfItemCnt;

    @Column(name = "DF_SIZE")
	private Integer dfSize;

    @Column(name = "DF_ITEM_CNT_AFTER")
	private Integer dfItemCntAfter;

    @Column(name = "DF_RETRIEVE_TS")
	private Date dfRetrieveTs;

    @Column(name = "DF_CENTERA_ADDRESS")
    private String dfCenteraAddress;

    @Column(name = "DF_MBX_MSG_ID")
	private String dfMbxMsgId;

    @Column(name = "DF_PROD_MBX_NAME")
	private String dfProdMbxName;

    @Column(name = "DF_PROD_MBX_WRITE_TS")
	private Date dfProdMbxWriteTs;

    @Column(name = "CONCAT_TEMP_FILENAME")
    private String concatTempFilename;

    @Column(name = "PROC_TS")//DEFAULT SYSDATE
	private Date procTs;

    @Column(name = "REDELIVER_IND")//DEFAULT 'no'
    private String redeliverInd;

    @Column(name = "REPLAYED_IND")//DEFAULT 'no'
    private String replayedInd;

    @Column(name = "ERROR_DESCRIPTION")
    private String errorDescription;

    @Column(name = "FILENAME_ON_DISK")
	private String fileNameOnDisk;

    @Column(name = "DFNAME_ON_DISK")
	private String dfnameOnDisk;

    @Column(name = "PROCESS_CNT")
	private Integer processCnt;

    @Column(name = "MBX_DEL_IND")
	private String mbxDelInd;

    @Column(name = "DF_MBX_DEL_IND")
	private String dfMbxDelInd;

    @Column(name = "FILE_DOC_ID")
	private String fileDocId;

    @Column(name = "DF_DOC_ID")
	private String dfDocId;

    @Column(name = "ARCHIVE_STATUS")
	private String archiveStatus;

    @Column(name = "DELETED_FROM_CENTERA_TS")
	private Date deletedFromCenteraTs;

    @Column(name = "DELETED_FROM_CENTERA")
    private String deletedFromCentera;

    @Column(name = "CENTERA_CLASS")
	private String centeraClass;

    @Column(name = "S3_VERSION_ID")
	private String s3VersionId;

    @Column(name = "DF_S3_VERSION_ID")
	private String dfS3VersionId;

    @Column(name = "DELETED_FROM_S3")
	private String deletedFromS3;

    @Column(name = "DELETED_FROM_S3_TS")
	private Date deletedFromS3Ts;

    @Column(name = "S3_CLASS")
     private String s3Class;

    @Column(name = "S3_BUCKET_NAME")
	private String s3BucketName;
}
