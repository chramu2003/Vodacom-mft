package za.co.vodacom.vodacommft.service;

import za.co.vodacom.vodacommft.entity.sfg_cfg.AgeAnalysisConfigEntity;
import za.co.vodacom.vodacommft.entity.sfg_cfg.AgeAnalysisIgnoreEntity;
import za.co.vodacom.vodacommft.entity.sfg_rpt.ProcessedFileEntity;

import java.util.List;

/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.service
 */


public interface IAgeAnalysisService {
    boolean ageAnalyseFileProcessing(String fileName, String routeCode);
    AgeAnalysisConfigEntity findAgeAnalysisConfigEntitiesByRoute_code(String route_code);
    ProcessedFileEntity findProcessedFileEntityByArrivedfilekey(String arrived_file_key);
    AgeAnalysisIgnoreEntity findAgeAnalysisIgnoreEntityByAge_uid(long age_uid);
    List<AgeAnalysisIgnoreEntity> findAgeAnalysisIgnoreEntitiesByAge_uid(long age_config_uid);
}
