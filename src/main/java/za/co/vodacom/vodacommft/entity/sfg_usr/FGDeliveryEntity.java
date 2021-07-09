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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "FG_DELIVERY", schema = "SFG_USR")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FGDeliveryEntity implements Serializable {
    private static final long serialVersionUID = -8257134696486481821L;
    @Id
    @Size(max = 24)
    @Column(name = "DELIVERY_KEY")
    private String deliveryKey;
    @Size(max = 24)  //NOT NULL ENABLE
    @Column(name = "ROUTE_KEY")
    private String routeKey;
    @Column(name = "DATA_FLOW_ID") //NOT NULL ENABLE
    private Integer dataFlow;
    @Size(max = 40)
    @Column(name = "STATE")    //NOT NULL ENABLE,
    private String state;
    @Size(max = 24)
    @Column(name = "DELIVCHAN_KEY")
    private String delivChanKey;
    @Size(max = 255)
    @Column(name = "CONSUMER_DOCID")
    private String consumerDocId;
    @Size(max = 100)
    @Column(name = "CONTENT_TYPE")
    private String contentType;
    @Size(max = 255)
    @Column(name = "FILENAME")
    private String fileName;
    @Size(max = 100)
    @Column(name = "CONSDOC_TYPE")
    private String consDocType;
    @Size(max = 255)
    @Column(name = "MAILBOX_PATH")
    private String mailBoxPath;
    @Column(name = "LATE_CREATE_MBX")
    @Size(max = 1)
    private String lateCreateMbx;
    @Column(name = "CONSUMER_MSGID")
    private Integer consumerMsgId;
    @Size(max = 255)
    @Column(name = "ASYNC_XFER_ID")
    private String asyncXferId;
    @Size(max = 5)
    @Column(name = "LOCKID") // DEFAULT 0 NOT NULL ENABLE,
    private Integer lockId;
    @Column(name = "CREATETS")   //DEFAULT sysdate NOT NULL ENABLE,
    private Date createTs;

    @Column(name = "MODIFYTS")
    private Date modifyTs;//DEFAULT sysdate NOT NULL ENABLE,
    @Size(max = 40)
    @Column(name = "CREATEUSERID") // DEFAULT ' ' NOT NULL ENABLE,
    private String createUserId;
    @Size(max = 40)
    @Column(name = "MODIFYUSERID") // DEFAULT ' ' NOT NULL ENABLE,
    private String modifyUserId;
    @Size(max = 40)
    @Column(name = "CREATEPROGID") // DEFAULT ' ' NOT NULL ENABLE,
    private String createPROGId;
    @Size(max = 40)
    @Column(name = "MODIFYPROGID") // DEFAULT ' ' NOT NULL ENABLE,
    private String modifyPROGId;
    @Size(max = 40)

    @Size(max = 255)
    @Column(name = "DIST_CONSUMER_MSGID")
    private String distConsumerMsgId;
}
