package za.co.vodacom.vodacommft.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import java.io.File;

/**
 * @author ncubeh on 2020/07/06
 * @package za.co.vodacom.vodacomMFT.config
 */
@Configuration
public class ReloadablePropertySourceConfig {

    private ConfigurableEnvironment env;

    public ReloadablePropertySourceConfig(@Autowired ConfigurableEnvironment env) {
        this.env = env;
    }

    @Bean
    @ConditionalOnProperty(name = "lookup-file-path", matchIfMissing = false)
    public ReloadablePropertySource reloadablePropertySource(PropertiesConfiguration properties_config) {
        ReloadablePropertySource reloadable_property_source = new ReloadablePropertySource("dynamic", properties_config);
        MutablePropertySources mutable_property_sources = env.getPropertySources();
        mutable_property_sources.addFirst(reloadable_property_source);
        return reloadable_property_source;
    }

    @Bean
    @ConditionalOnProperty(name = "lookup-file-path", matchIfMissing = false)
    public PropertiesConfiguration propertiesConfiguration(
            @Value("${lookup-file-path}") String file_path) throws Exception {
        String filePath = "";
        filePath = new File(file_path).getCanonicalPath();
        PropertiesConfiguration configuration = new PropertiesConfiguration(
                new File(filePath));
        configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
        return configuration;
    }
}
