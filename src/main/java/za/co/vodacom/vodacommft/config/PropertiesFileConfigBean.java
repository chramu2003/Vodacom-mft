package za.co.vodacom.vodacommft.config;/*
package za.co.vodacom.vodacomMFT.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource(value={"file:${app.SI_INSTALL}/properties/VC_collection.properties",
        "file:${app.SI_INSTALL}/sfg_app/sfg52.6.3/install/properties/vc_delivery.properties"})
public class PropertiesFileConfigBean {
    @Value("${PUBLIC_KEY_FILE}")
    private String public_key_file;
    @Value("${LOCAL_WORKING_DIR}")
    private String local_working_dir;
    @Value("${CLEANUP_CUTOFF}")
    private String cleanup_cutoff;
    @Value("${COLLECTION_ERRORS_LIST}")
    private String  collection_errors_list;
    @Value("${SITE_COMMAND}")
    private String site_command;

    public String getSite_command() {
        return site_command;
    }

    public void setSite_command(String site_command) {
        this.site_command = site_command;
    }

    public String getPublic_key_file() {
        return public_key_file;
    }

    public void setPublic_key_file(String public_key_file) {
        this.public_key_file = public_key_file;
    }

    public String getLocal_working_dir() {
        return local_working_dir;
    }

    public void setLocal_working_dir(String local_working_dir) {
        this.local_working_dir = local_working_dir;
    }

    public String getCleanup_cutoff() {
        return cleanup_cutoff;
    }

    public void setCleanup_cutoff(String cleanup_cutoff) {
        this.cleanup_cutoff = cleanup_cutoff;
    }

    public String getCollection_errors_list() {
        return collection_errors_list;
    }

    public void setCollection_errors_list(String collection_errors_list) {
        this.collection_errors_list = collection_errors_list;
    }
}
*/
