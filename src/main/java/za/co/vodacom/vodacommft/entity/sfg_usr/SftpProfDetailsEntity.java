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

@Entity
@Table(name = "SFTP_PROF", schema = "SFG_USR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SftpProfDetailsEntity implements Serializable {

    private static final long serialVersionUID = -6705881311061009402L;
    @Id
    @Column(name = "PROFILE_ID")
    private String profileId;

    @Column(name = "NAME")
    private String sftpName;

    @Column(name = "CONNECTION_RETRIES")
    private int connectionRetries;

    @Column(name = "RESPONSE_TIMEOUT")
    private Integer responseTimeout;

    @Column(name = "REMOTE_HOST")
    private String remote_host;

    @Column(name = "REMOTE_PORT")
    private Integer remote_port;

    @Column(name = "REMOTE_USER")
    private String remote_user;

    @Column(name = "KHOST_KEY_ID")
    private String khostKeyId;

    @Column(name = "LUSER_KEY_ID")
    private String luserKeyId;

    @Column(name = "REMOTE_PASSWORD")
    private String remotePassword;

    @Column(name = "RETRY_DELAY")
    private Integer retryDelay;

    @Column(name = "PREFERRED_CIPHER")
    private String preferredCipher;

    @Column(name = "PREFERRED_MAC")
    private String preferredMac;

    @Column(name = "PREFERRED_AUTH")
    private String preferredAuth;

    @Column(name = "COMPRESS_BOOL")
    private String compressBool;

    @Column(name = "LOCAL_PORT_RANGE")
    private String localPortRange;

    @Column(name = "CHANGE_DIRECTORY")
    private String changeDirectory;

    @Column(name = "CHARACTER_ENCODING")
    private String characterEncoding;
}
