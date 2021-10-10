package za.co.vodacom.vodacommft.entity.sfg_cfg;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.entity.sfg_cfg
 */

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "AGE_ANALYSIS_CONFIG", schema = "SFG_CFG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AgeAnalysisConfigEntity implements Serializable {

    private static final long serialVersionUID = 3143577813269844423L;
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "AGE_CONFIG_UID" )
    private Integer age_config_uid;

    @Column(name = "ROUTE_CODE")
    private String routeCode;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FILE_TYPE") //DEFAULT 'NON'
    private String file_type;

    @Column(name = "FILE_DELIMITER")
    private String file_delimiter;

    @Column(name = "FILE_SEPARATOR")
    private String fileSeparator;

    @Column(name = "DATA_ID_CHAR")
    private String dataIdChar;

    @Column(name = "HDR_ID")
    private String hdrId;

    @Column(name = "HDR_ID_POS")
    private int hdrIdPos;

    @Column(name = "HDR_ID_LEN")
    private int hdrIdLen;

    @Column(name = "HDR_ID_NAME")
	private String hdrIdName;

    @Column(name = "TR_ID")
    private String trailerId;

    @Column(name = "TR_ID_POS")
	private int trIdPos;

    @Column(name = "TR_ID_LEN")
    private int trIdLen;

    @Column(name = "TR_ID_NAME")
	private String trIdName;

    @Column(name = "EVENT_ID")
	private String eventId;

    @Column(name = "EVENT_START_TIME_POS")
	private int eventStartTimePos;

    @Column(name = "EVENT_START_TIME_LEN")
    private int eventStartTimeLen;

    @Column(name = "EVENT_START_TIME_NAME")
    private String eventStartTimeName;

    @Column(name = "EVENT_START_TIME_FORMAT")
    private String eventStartTimeFormat;

    @Column(name = "EVENT_END_TIME_POS") //bakhuselelwa usizo labantu... basize nkosi yami....beat 5
	private int eventEndTimePos;

    @Column(name = "EVENT_END_TIME_LEN")
    private int eventEndTimeLen;

    @Column(name = "EVENT_END_TIME_NAME")
	private String eventEndTimeName;

    @Column(name = "EVENT_END_TIME_FORMAT")
    private String eventEndTimeFormat;

    @Column(name = "EVENT_DUR_POS")
	private int eventDurationPos;

    @Column(name = "EVENT_DUR_LEN")
    private int eventDurationLen;

    @Column(name = "EVENT_DUR_NAME")
	private String eventDurationName;

    @Column(name = "EVENT_DUR_FORMAT")
    private String eventDurationFormat;

    @Column(name = "EVENT_DUR_UNIT")
    private int eventDurationUnit;

    @Column(name = "EVENT_TYPE_POS")
    private int eventTypePos;

    @Column(name = "EVENT_TYPE_LEN")
    private int eventTypeLen;

    @Column(name = "EVENT_TYPE_NAME")
	private String eventTypeName;

    @Column(name = "TIMEZONE_OFFSET_POS")
	private int timezoneOffsetPos;

    @Column(name = "TIMEZONE_OFFSET_LEN")
    private int timezoneOffsetLen;

    @Column(name = "TIMEZONE_OFFSET_NAME")
    private String timezoneOffsetName;

    @Column(name = "LOOKUP_FIELD_POS")
	private int lookupFieldPos;

    @Column(name = "LOOKUP_FIELD_LEN")
    private int lookupFieldLen;

    @Column(name = "LOOKUP_FIELD_NAME")
	private String lookupFieldName;

    @Column(name = "LOOKUP_TABLE_NAME")
    private String lookupTableName;

    @Column(name = "LOOKUP_COLUMN")
    private String lookupColumn;

    @Column(name = "RETURN_COLUMN")
	private String returnColumn;

    @Column(name = "LAST_CHANGED_USER")
	private String lastChangedUser;

    @Column(name = "LAST_CHANGED_TS")
	private Date lastChangedTs;

    @Column(name = "NRTDE_IND")
    @NotNull
    private String nrtdeIndicator;

    @Column(name = "NETWORK_POS")
    private int networkPos;

    @Column(name = "NETWORK_LEN")
    private int networkLen;

    @Column(name = "NETWORK_NAME")
	private String networkName;

    @Column(name = "LOGICAL_CONSUMER_NAME")
	private String logicalConsumerName;
}
