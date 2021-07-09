package za.co.vodacom.vodacommft.entity.sfg_cfg;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "ROUTE", schema = "SFG_CFG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RouteEntity implements Serializable {

    private static final long serialVersionUID = 143193060766142251L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "ROUTE_UID")
    private Integer routeUid;
    @Size(max = 50)
    @Column(name = "SHORT_NAME")
    private String shortName;
    @Size(max = 50)
    @Column(name = "COLLECTION_CODE")
    private String collectionCode; //default 'Push'
    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description; //default 'NULL'
    @Size(max = 255)
    @Column(name = "FILTER")
    private String filter; //default 'NULL'
    @Size(max = 50)
    @Column(name = "PRODUCER_CODE")
    private String producerCode; //default 'NULL'
    @Size(max = 50)
    @Column(name = "ROUTE_PRIORITY")
    private String routePriority; //default 'Normal'
    @Size(max = 50)
    @Column(name = "ROUTE_STATUS")
    private String routeStatus; //default '2'
    @Size(max = 3)
    @Column(name = "REPLAY_DAYS")
    private String replayDays;
    @Size(max = 3)
    @Column(name = "UNCOMPRESS")
    private String uncompress; //default 'no'
    @Size(max = 10)
    @Column(name = "UNCOMPRESS_TYPE")
    private String uncompressType; //default 'NULL'
    @Size(max = 100)
    @Column(name = "UNCOMPRESS_STRING")
    private String uncompressString;
    @Size(max = 50)
    @Column(name = "ROUTE_DUPLICATE_CHECK")
    private String routeDuplicateCheck; //default 'NULL'
    @Size(max = 2)
    @Column(name = "PROCESSOR_UID")
    private String processorUid; //default '00'
    @Size(max = 3)
    @Column(name = "ROUTE_COMPRESS")
    private String routeCompress; //default 'no'
    @Size(max = 10)
    @Column(name = "ROUTE_COMPRESS_TYPE")
    private String routeCompressType; //default 'NULL'
    @Size(max = 100)
    @Column(name = "ROUTE_COMPRESS_STRING")
    private String routeCompressString;
    @Size(max = 1)
    @Column(name = "ROUTE_COMPRESS_POSITION")
    private String routeCompressPosition; //default 'NULL'
    @Size(max = 3)
    @Column(name = "ROUTE_REMOVE_CHARS")
    private String routeRemoveChars; //default 'no'
    @Size(max = 20)
    @Column(name = "ROUTE_REMOVE_CHARS_STRING")
    private String routeRemoveCharsString; //default 'NULL'
    @Size(max = 1)
    @Column(name = "ROUTE_REMOVE_POSITION")
    private Integer routeRemovePosition; //default 'NULL'
    @Size(max = 3)
    @Column(name = "ROUTE_CHANGE_FILENAME_CASE")
    private String routeChangeFilenameCase; //default 'no'
    @Size(max = 255)
    @Column(name = "ROUTE_NEW_FILENAME_PATTERN")
    private String routeNewFilenamePattern; //default 'NULL'
    @Size(max = 1)
    @Column(name = "ROUTE_RENAME_POSITION")
    private Integer routeRenamePosition; //default 'NULL'
    @Size(max = 50)
    @Column(name = "LAST_CHANGED_USER")
    private String lastChangedUser; //default 'SYSTEM'
    @Size(max = 3)
    @Column(name = "ROUTE_DUP_HISTORY")
    private Integer routeDupHistory;
    @Size(max = 20)
    @Column(name = "ROUTE_HASH_POS")
    private String routeHashPos;
    @Size(max = 255)
    @Column(name = "DL_MAP_NAME")
    private String dlMapName;
    @Size(max = 38)
    @Column(name = "FILE_SIZE_MIN")
    private Integer fileSizeMin;
    @Size(max = 38)
    @Column(name = "FILE_SIZE_MAX")
    private Integer fileSizeMax;
    @Size(max = 500)
    @Column(name = "CENTERA_CLASS")
    private String centeraClass;
    @Size(max = 10)
    @Column(name = "RETENTION_ONLINE")
    private String retentionOnline;
    @Size(max = 10)
    @Column(name = "RETENTION_REPLAY")
    private String retentionReplay;
    @Size(max = 10)
    @Column(name = "RETENTION_REPORT")
    private String retentionReport;
    @Size(max = 10)
    @Column(name = "RETENTION_MBX")
    private String retentionMbx;
    @Size(max = 10)
    @Column(name = "RETENTION_EXTRACTABLE")
    private String retentionExtractable;
    @Size(max = 10)
    @Column(name = "RETENTION_EXTRACTABLE_DAYS")
    private String retentionExtractableDays;
    @Size(max = 10)
    @Column(name = "ROUTE_COMPRESS_EXT")
    private String routeCompressExt; //default '.zip'
    @Size(max = 50)
    @Column(name = "CENTERA_CLASS_NAME")
    private String centeraClassName;
    @Size(max = 50)
    @Column(name = "S3_BUCKET_NAME")
    private String s3BucketName;
    @Size(max = 255)
    @Column(name = "PGP_ENCRYPT_DECRYPT")
    private String pgpEncryptDecrypt;

}
