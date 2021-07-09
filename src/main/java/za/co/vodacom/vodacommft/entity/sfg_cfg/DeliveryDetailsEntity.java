package za.co.vodacom.vodacommft.entity.sfg_cfg;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.entity.sfg_cfg
 */

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DELIVERY_DETAILS", schema = "SFG_CFG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeliveryDetailsEntity implements Serializable {
    private static final long serialVersionUID = 1590816754033551746L;
    @Id
    @GeneratedValue //(strategy= GenerationType.IDENTITY)
    @Column(name = "DELIVERY_UID")
    private Integer deliveryUid;

    @Column(name = "ROUTE_SHORT_NAME")
    private String routeShortName;

    @Column(name = "CONSUMER_CODE")
    private String consumerCode;

    @Column(name = "PRIORITY")
    private String priority;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DUPLICATE_CHECK")
    private String duplicateCheck; //DEFAULT none

    @Column(name = "USE_COMPRESS")
    private String useCompress; //DEFAULT no

    @Column(name = "COMPRESS_TYPE")
    private String compressionType;

    @Column(name = "COMPRESS_POSITION")
    private String compressPosition;

    @Column(name = "COMPRESS_STRING")
    private String compressString;

    @Column(name = "REMOVE_CHARS")
    private String removeChars; //DEFAULT no

    @Column(name = "REMOVE_CHARS_STRING")
    private String removeCharsString;

    @Column(name = "REMOVE_POSITION")
    private String removePosition;

    @Column(name = "CHANGE_FILENAME_CASE")
    private String changeFilenameCase; //DEFAULT no

    @Column(name = "NEW_FILENAME_PATTERN")
    private String newFileNamePattern;

    @Column(name = "TAP_IN_VALIDATION")
    private String tapInValidation;

    @Column(name = "RENAME_POSITION")
    private String renamePosition;

    @Column(name = "USE_TEMP_NAME")
    private String useTempName; //DEFAULT no

    @Column(name = "TEMP_NAME_PATTERN")
    private String tempNamePattern; //DEFAULT wfid

    @Column(name = "CHANGE_FILE_PERMISSIONS")
    private String changeFilePermissions; //DEFAULT no

    @Column(name = "NEW_PERMISSION_STRING")
    private String newPermissionString;

    @Column(name = "CALCULATE_CHECKSUM")
    private String calculateChecksum; //DEFAULT no

    @Column(name = "CALC_CHECKSUM_OPTION")
    private String calcChecksumOpion;

    @Column(name = "DEST_FILE_ACTION")
    private String destFileAction; //DEFAULT rename

    @Column(name = "AGE_ANALYSIS")
    private String ageAnalysis; //DEFAULT no

    @Column(name = "DELIVERY_NOTIFICATION")
    private String deliveryNotification; //DEFAULT no

    @Column(name = "DELIVERY_NOTIF_ADDRESS")
    private String deliveryNotifAddress;

    @Column(name = "LAST_CHANGED_USER")
    private String lastChangedUser; //DEFAULT SYSTEM

    @Column(name = "LAST_CHANGED_TS")
    @Temporal(TemporalType.DATE)
    private Date lastChangedTs;

    @Column(name = "TIMEBASED_DELIVERY")
    private String timebasedDelivery;

    @Column(name = "TIMEBASED_OPTION")
    private String timebasedOption;

    @Column(name = "TIMEBASED_DAY_START")
    private String timebasedDayStart;

    @Column(name = "TIMEBASED_DAY_END")
    private String timebasedDayEnd;

    @Column(name = "TIMEBASED_TIME_START")
    private String timebasedTimeStart;

    @Column(name = "TIMEBASED_TIME_END")
    private String timebasedTimeEnd;

    @Column(name = "DELIVER_NOT_FILE")
    private String deliverNotFile;

    @Column(name = "NOTIFICATION_FILE_EXT")
    private String notificationFileExt;

    @Column(name = "COMPRESS_EXT")
    private String compressExt;  //DEFAULT .zip

    @Column(name = "QUOTE_CMD")
    private String quoteCommand;

    @Column(name = "ADDITIONAL_IP")
    private String additionalIp;

    @Column(name = "DELIVERY_FAILURE_RATE")
    private Integer deliveryFailureRate;  //DEFAULT 20

    @Column(name = "CD_RUNTASK")
    private String cdRuntask;

    @Column(name = "CD_RUNJOB")
    private String cdRunjob;

    @Column(name = "USE_UNCOMPRESS")
    private String useUncompress;

    @Column(name = "UNCOMPRESS_POSITION")
    private String uncompressPosition;

    @Column(name = "UNCOMPRESS_TYPE")
    private String uncompressType;

    @Column(name = "CD_SUBMIT_PROC")
    private String cdSubmitProc;

    @Column(name = "CD_SUBMIT_PARMS")
    private String cdSubmitParms;

    @Column(name = "CD_DEST_DIR")
    private String cdDestDir;
}
