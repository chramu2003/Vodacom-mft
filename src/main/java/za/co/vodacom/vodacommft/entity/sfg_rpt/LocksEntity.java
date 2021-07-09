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
import javax.validation.constraints.Min;
import java.io.Serializable;

@Entity
@Table(name = "LOCKS", schema = "SFG_RPT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocksEntity implements Serializable {

    private static final long serialVersionUID = -4531283580683197811L;

    @Id
    @Column(name = "ITEMNAME")
    private String itemName;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "TIMESTAMP")
    private Long timeStamp;

    @Column(name = "TIMEOUT")
    private Long timeOut;

    @Column(name = "SYSTEMNAME")
    private String systemName;

    //@Size(max = 255)
    @Min(1)
    @Column(name = "CLEARONSTARTUP")
    private Integer clearOnStartUp;

}
