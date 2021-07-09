package za.co.vodacom.vodacommft.service;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service
 */

public interface IPasswordService {
    String encrypt(String value, String salt) throws Exception;
    String decrypt(String value, String salt) throws Exception;
}
