package za.co.vodacom.vodacommft.repository.sfg_rpt;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.repository.sfg_rpt
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.vodacom.vodacommft.entity.sfg_rpt.DeliveredFileEntity;

import java.util.Date;

@Repository
public interface DeliveredFileEntityRepository extends JpaRepository<DeliveredFileEntity, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE DELIVERED_FILE DF set DF.CONSUMER_DELIVER_TS = null, DF.DELIVERY_STATUS = '92', DF.DELIVERY_KEY = :deliveryKey " +
            "where DF.DELIVER_UID = :deliveryUid AND DF.DELIVERY_STATUS <> '92'", nativeQuery = true)
    void updateDeliveredFileTsStatusAndKeyByUid(/*@Param("deliveryStatus") String deliveryStatus,*/ @Param("deliveryKey") String deliveryKey,
                                                                                                    @Param("deliveryUid") int deliveryUid/*, @Param("deliveryNotStatus") String deliveryNotStatus*/);

    @Transactional
    @Modifying
    @Query(value = "UPDATE DELIVERED_FILE DF set DF.CONSUMER_DELIVER_TS = :consumerDeliveryTs, DF.DELIVERY_STATUS = :deliveryStatus, DF.DELIVERY_KEY = :deliveryKey " +
            "where DF.DELIVER_UID = :deliveryUid ", nativeQuery = true)
    void updateDeliveredFileTsDeliverStatusAndKeyByDeliverUid(@Param("consumerDeliveryTs") Date consumerDeliveryTs, @Param("deliveryStatus") String deliveryStatus,
                                                              @Param("deliveryKey") String deliveryKey, @Param("deliveryUid") int deliveryUid);





}
