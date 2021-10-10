package za.co.vodacom.vodacommft.service;

import java.util.List;

/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service
 */

public interface IRenameService {

    String renameByRegEx(String fileName, String renameCases[], String ConsumerHost);

    String[] renamePatternCases(String inputPattern);

    String renameByRegEx(String input, String regEx);

    List<String> buildRenamePatternArray(String renamePattern);
}
