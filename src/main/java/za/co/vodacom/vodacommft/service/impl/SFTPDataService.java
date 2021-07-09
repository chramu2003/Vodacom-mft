package za.co.vodacom.vodacommft.service.impl;
/**
 * @author mz herbie on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.entity.sfg_usr.SftpProfDetailsEntity;
import za.co.vodacom.vodacommft.exception.NotFoundException;
import za.co.vodacom.vodacommft.repository.sfg_usr.SftpRepository;
import za.co.vodacom.vodacommft.service.ISFTPDataService;

import java.util.Date;
import java.util.Optional;

@Service
public class SFTPDataService implements ISFTPDataService {

    @Autowired
    private SftpRepository sftpRepository;
    private static final Logger sftp_data_logger = LoggerFactory.getLogger(SFTPDataService.class);

    @Override
    public SftpProfDetailsEntity getValuesOfSFTPEntity(String entity_value) {

        SftpProfDetailsEntity sftpProf = null;
        Optional<SftpProfDetailsEntity> sftp_temp_value = sftpRepository.findBySftpName(entity_value);
        if(sftp_temp_value.isPresent()){
            sftpProf =  sftp_temp_value.get();
            sftp_data_logger.info(":: => " + new Date().toString() + " SFTP PROFILE -ID :- "+ entity_value);
        }else{
            throw new NotFoundException("!!!!! SFTP SSH Remote Profile Not Found. " +
                    "Check if SFTP SSH Remote Profile :- "+ sftp_temp_value + "  Exists");
        }
        return sftpProf;

    }

    @Override
    public SftpProfDetailsEntity getDelValuesOfSFTPEntity(String entity_value) {

        SftpProfDetailsEntity sftpProf = null;
        Optional<SftpProfDetailsEntity> sftp_temp_value = sftpRepository.findById(entity_value);
        if(sftp_temp_value.isPresent()){
            sftpProf =  sftp_temp_value.get();
            sftp_data_logger.info(":: => " + new Date().toString() + " SFTP PROFILE -ID :- "+ entity_value);
        }else{
            throw new NotFoundException("!!!!! SFTP SSH Remote Profile Not Found. " +
                    "Check if SFTP SSH Remote Profile :- "+ sftp_temp_value + "  Exists");
        }
        return sftpProf;

    }
}
