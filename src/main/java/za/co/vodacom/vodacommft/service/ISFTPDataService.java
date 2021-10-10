package za.co.vodacom.vodacommft.service;

import za.co.vodacom.vodacommft.entity.sfg_usr.SftpProfDetailsEntity;

/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service
 */


public interface ISFTPDataService {

    SftpProfDetailsEntity getValuesOfSFTPEntity(String entity_value);

    SftpProfDetailsEntity getDelValuesOfSFTPEntity(String entity_value);
}
