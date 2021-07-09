package za.co.vodacom.vodacommft.repository.sfg_usr;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.repository.sfg_usr
 */


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.vodacom.vodacommft.entity.sfg_usr.SftpProfDetailsEntity;

import java.util.Optional;

@Repository
public interface SftpRepository extends JpaRepository<SftpProfDetailsEntity, String> {

    Optional<SftpProfDetailsEntity> findBySftpName(String sftpName);
    Optional<SftpProfDetailsEntity> findById(String profileId);

}
