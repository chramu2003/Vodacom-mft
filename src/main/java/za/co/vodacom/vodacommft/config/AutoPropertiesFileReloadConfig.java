package za.co.vodacom.vodacommft.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Date;

/**
 * @author ncubeh on 2020/07/06
 * @package za.co.vodacom.vodacomMFT.config
 */
@Configuration
public class AutoPropertiesFileReloadConfig {
    private final static Logger auto_load_log = LoggerFactory.getLogger(AutoPropertiesFileReloadConfig.class);

    private PropertiesConfiguration config;

    @Value("${lookup-polling}")
    private long lookupPropertiesPollingInterval;

    @Value("${lookup-file-path}")
    private String lookupPropertiesFilePath;

    @PostConstruct
    public void configureObservableProperties() throws ConfigurationException
    {
        File lookupPropertiesFile = new File(lookupPropertiesFilePath);
        auto_load_log.info(new Date().toString()+ " : Look up for properties file path : "+ lookupPropertiesFilePath);
        config = new PropertiesConfiguration(lookupPropertiesFile);
        FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
        strategy.setRefreshDelay(lookupPropertiesPollingInterval);
        auto_load_log.info(new Date().toString()+ " : Look up properties polling interval is set : "+ lookupPropertiesPollingInterval);
        config.setReloadingStrategy(strategy);
        auto_load_log.info(new Date().toString()+ " : Strategy has been set....");
    }

}
