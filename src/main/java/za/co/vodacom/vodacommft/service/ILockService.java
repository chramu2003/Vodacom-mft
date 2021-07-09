package za.co.vodacom.vodacommft.service;

public interface ILockService {

    boolean releaseSFGLock(String collectionCode);

    boolean checkIfLockExits(String consumerCode);

    boolean addLock(String cons_coll_code);

    void releaseLock(String consumerCode);
}
