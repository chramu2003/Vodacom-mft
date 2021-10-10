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

@Entity
@Table(name = "AGE_ANALYSIS_IGNORE", schema = "SFG_CFG")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AgeAnalysisIgnoreEntity implements Serializable {
    private static final long serialVersionUID = 8267296293048030091L;

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "AGE_IGNORE_UID")
    private Integer ageIgnoreUid;

    @Column(name = "AGE_UID")
	private Long ageUid;

    @Column(name = "SEQ_NO")
	private Integer seqNumber;

    @Column(name = "LINE_DET")
	private String lineDet;

    @Column(name = "DET_POS")
	private Integer detPosition;

    @Column(name = "DET_LEN")
	private Integer detLen;

    @Column(name = "DET_NAME")
	private String detName;

    @Column(name = "DET_OPERATOR")
	private String detOperator;
}
