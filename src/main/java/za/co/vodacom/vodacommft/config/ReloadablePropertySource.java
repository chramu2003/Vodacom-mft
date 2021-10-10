package za.co.vodacom.vodacommft.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * @author ncubeh on 2020/07/06
 * @package za.co.vodacom.vodacomMFT.config
 */
public class ReloadablePropertySource extends PropertySource {

    PropertiesConfiguration propertiesConfiguration;

    public ReloadablePropertySource(String property_source_name, PropertiesConfiguration propertiesConfiguration) {
        super(property_source_name);
        this.propertiesConfiguration = propertiesConfiguration;
    }

    public ReloadablePropertySource(String file_name, String file_path) throws Exception {
        super(StringUtils.isEmpty(file_name) ? file_path : file_name);
        try {
            this.propertiesConfiguration = new PropertiesConfiguration(file_path);
            this.propertiesConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Object getProperty(String property_source) {
        return propertiesConfiguration.getProperty(property_source);
    }
}
