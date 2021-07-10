package za.co.vodacom.vodacommft.service;

import java.util.List;

public interface IFileDeliveryService {

    void deliveryProcessing(List<String[]> pendingDeliveryList);
}
