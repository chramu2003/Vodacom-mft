package za.co.vodacom.vodacommft.entity.sfg_rpt;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.entity.sfg_rpt
 */

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "AGE_ANALYSIS", schema = "SFG_RPT")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AgeAnalysisEntity implements Serializable {

    private static final long serialVersionUID = 617146099792927891L;

    @Id
    @GeneratedValue //To verify
    @Column(name = "AGE_UID")
    private Integer ageUid;

    @NotNull
    @Column(name = "DELIVEREDFILE_KEY")
    private String deliveredFileKey;

    @Column(name = "EVENT_AGE_TYPE")
    private String eventAgeType;

    @Column(name = "TOTAL_EVENTS")
    private long totalEvents;

    @Column(name = "LONGEST_DELIVERY_AGE")
    private long longestDeliveryAge;

    @Column(name = "MEDIAN_DELIVERY_AGE")
    private long medianDeliveryAge;

    @Column(name = "SHORTEST_DELIVERY_AGE")
    private long shortestDeliveryAge;

    @Column(name = "BUCKET_1")
    private long bucket1;

    @Column(name = "BUCKET_2")
    private long bucket2;

    @Column(name = "BUCKET_3")
    private long bucket3;

    @Column(name = "BUCKET_4")
    private long bucket4;

    @Column(name = "BUCKET_5")
    private long bucket5;

    @Column(name = "BUCKET_6")
    private long bucket6;

    @Column(name = "BUCKET_7")
    private long bucket7;

    @Column(name = "BUCKET_8")
    private long bucket8;

    @Column(name = "BUCKET_9")
    private long bucket9;

    @Column(name = "MEDIATION_TIME")
    private long mediationTime;

    @Column(name = "SFG_WF_ID")
    private String sfgWfId;

    @Column(name = "NETWORK")
    private String ageANetwork;

    @Column(name = "COUNTRY")
    private String ageACountry;

    @Column(name = "ERROR")
    private String ageAError;

    @Column(name = "LOGICAL_CONSUMER_NAME")//DEFAULT 'ERROR'
    private String logicalConsumerName;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "EVENT_TYPE")
    private String eventType;
}
