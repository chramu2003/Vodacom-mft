package za.co.vodacom.vodacommft.dto;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.dto
 */

import lombok.*;
import za.co.vodacom.vodacommft.entity.sfg_cfg.DeliveryDetailsEntity;
import za.co.vodacom.vodacommft.entity.sfg_rpt.PendingDeliveriesEntity;
import za.co.vodacom.vodacommft.entity.sfg_usr.SftpProfDetailsEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class DeliveryDetailsDTO {

    private String consumerCode;
    private String routeShortName ;
    private String consumerProtocol;
    private String deliveryCode;
    private  String ftp_transfer_type;

    private String  ftp_connection_type;
    private DeliveryDetailsEntity deliveryDetailsEntity;
    private PendingDeliveriesEntity pendingDeliveriesEntity;
    private String publicKeyFile;

    private String ftp_username;
    private String ftp_password;
    private int ftp_port;
    private String ftp_ssl;
    private String ftp_host;

    private String ftp_prot_level;
    private String file_names;
    private SftpProfDetailsEntity sftpProfDetailsEntity;
    private List<String> list_of_file_metadata; //100
    private String local_working_dir;
}
