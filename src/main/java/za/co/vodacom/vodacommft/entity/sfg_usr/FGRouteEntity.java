package za.co.vodacom.vodacommft.entity.sfg_usr;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.entity.sfg_usr
 */

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "FG_ROUTE", schema = "SFG_USR")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FGRouteEntity implements Serializable {

    private static final long serialVersionUID = -6563804231267695910L;
    @Id
    @Column(name = "ROUTE_KEY")  //NOT NULL ENABLE,
    private String routeKey;

    @Column(name = "DATA_FLOW_ID")
    private Integer dataFlowId; //NOT NULL ENABLE,

    @Column(name = "ARRIVEDFILE_KEY")
    private String arrivedFileKey;

    @Column(name = "ROUTCHAN_KEY")  // NOT NULL ENABLE,
    private String routChanKey;

    @Column(name = "CONS_ORG_KEY")  // NOT NULL ENABLE,
    private String consOrgKey;

    @Column(name = "CONS_ORG_NAME")
    private String consOrgName;

    @Column(name = "P_FSTRUCT_KEY") //NOT NULL ENABLE,
    private String pFSTructKey;

    @Column(name =  "STATE") //NOT NULL ENABLE,
    private String state;

    @Column(name =  "START_TIME") //NOT NULL ENABLE,
    private Date startTime;

    @Column(name = "COMPLETE_TIME")
    private Date completeTime;

    @Column(name =  "DELIVS_REMAIN")  // NOT NULL ENABLE,
    private Integer delivsRemain;

    @Column(name = "LOCKID" )   //DEFAULT 0 NOT NULL ENABLE,
    private Integer lockId;

    @Column(name = "CREATETS") //DEFAULT sysdate NOT NULL ENABLE,
    private Date createTs;

    @Column(name = "MODIFYTS")
    private Date modifyTs;//DEFAULT sysdate NOT NULL ENABLE,

    @Column(name = "CREATEUSERID") // DEFAULT ' ' NOT NULL ENABLE,
    private String createUserId;

    @Column(name = "MODIFYUSERID") // DEFAULT ' ' NOT NULL ENABLE,
    private String modifyUserId;

    @Column(name = "CREATEPROGID") // DEFAULT ' ' NOT NULL ENABLE,
    private String createPROGId;

    @Column(name = "MODIFYPROGID") // DEFAULT ' ' NOT NULL ENABLE,
    private String modifyPROGId;
}
