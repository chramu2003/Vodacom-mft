package za.co.vodacom.vodacommft.service.impl;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service.impl
 */

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.config.PropertiesFileSysConfig;
import za.co.vodacom.vodacommft.entity.sfg_rpt.LocksEntity;
import za.co.vodacom.vodacommft.repository.sfg_rpt.LocksRepository;
import za.co.vodacom.vodacommft.service.ILockService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Optional;


@Service
public class LockService implements ILockService {

    @Autowired
    private LocksRepository locksRepository;

    @Autowired
    private PropertiesFileSysConfig propertiesFileSysConfig;

    private static final Logger lock_service_logger = LoggerFactory.getLogger(LockService.class);

    @Override
    public boolean releaseSFGLock(String collectionCode) { /*Lets create different collection and Delivery lock service implementation class (releaseCollectLock()) */
        String sfgLockName = "Coll_" + collectionCode + "_lock";
        lock_service_logger.info("Releasing SFG lock : " + sfgLockName);
        boolean isFound = locksRepository.existsById(sfgLockName);
        if(isFound){
            locksRepository.deleteById(sfgLockName);
            return true;
        }else {
            //throw new NotFoundException("Lock Not Found. No Deletion for Lock :- "+ sfgLockName);
            lock_service_logger.info("Lock Not Found. No Deletion for Lock :- " + sfgLockName);
            return false;
        }
    }
//add sync
    public synchronized boolean checkIfLockExits(String consumerCode) {

        boolean recordExist = locksRepository.existsById(consumerCode);
        lock_service_logger.info(" >>>>>> ACheck if log exist:- " + consumerCode + " " + recordExist);
        return recordExist;
    }

    @Override
    public synchronized void releaseLock(String consumerCode){
        lock_service_logger.info(" >>>>>> About to Released Lock For ITEM_NAME:- " + consumerCode);
        if (consumerCode != null || !consumerCode.isEmpty()){
            if (locksRepository.existsById(consumerCode)){
                locksRepository.deleteById(consumerCode);

            }
            /*locksRepository.findById(consumerCode)
                    .map(locksRepository::delete);*/
        }
    }


    @SneakyThrows
    private String getSystemHostName(){ //Where the code is running is important for these

        if (InetAddress.getLocalHost().getHostName().contains("101")){
            return  "node1";
        }else {
            return "node2";
        }
    }

    public synchronized boolean addLock(String consumerCode) {

        lock_service_logger.info(" >>>>>> About to Adding Lock For ITEM_NAME:- " + consumerCode);

        LocksEntity locksEntity = new LocksEntity();
        locksEntity.setItemName(consumerCode);
        locksEntity.setSystemName(getSystemHostName());
        locksEntity.setClearOnStartUp(1);
        locksEntity.setTimeStamp(new Date().getTime());
        locksEntity.setTimeOut(2 * Long.parseLong(propertiesFileSysConfig.getLockTimeOut()));
        locksEntity.setUserName("DeliveryService");

        locksRepository.save(locksEntity); //Lets use exception to check if saved properly try catch (false)
        return true;
    }
}
