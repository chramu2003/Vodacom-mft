package za.co.vodacom.vodacommft.config;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.config
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource(value={"file:${app.SFG_HOME}/si_globals/si_global.properties",
        "file:${app.SI_INSTALL}/properties/sandbox.cfg",
        "file:${app.SI_INSTALL}/properties/jdbc_customer.properties"})
public class OracleDbConfigProperties {

    @Autowired
    private Environment environment;

    public String getOracleUserNameforCFG(){
        String oracle_username = environment.getProperty("vodaPool.user");
        return oracle_username;
    }
    public String getOracleUsernameForRPT(){
        String ora_username_rpt = environment.getProperty("vodaReportPool.user");
        return ora_username_rpt;
    }
    public String getOracleUsernameForUSR(){
        String ora_username_usr = environment.getProperty("DB_USER");
        return ora_username_usr;
    }
    public String getOraclePassword(){
        String oracle_password = environment.getProperty("vodaPool.password");

        return oracle_password;
    }
    public String getOracleUrl(){
        String oracle_url = environment.getProperty("DB_URL");
        return oracle_url;
    }

}
