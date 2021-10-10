package za.co.vodacom.vodacommft.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author ncubeh on 2020/11/19
 * @package za.co.vodacom.vodacomMFT
 */
/*We turning off cache release to improve performance,
* */
@Configuration
public class TurnOffEmbeddedTomcatReloadConfig {
    private final static Logger turn_off_tomReload = LoggerFactory.getLogger(TurnOffEmbeddedTomcatReloadConfig.class);
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainerCustomizer() {
        turn_off_tomReload.info(new Date().toString()+ ": About to Customize Embedded Tomcat... >>Dis_Sangomalize<<");
        return container -> {
            turn_off_tomReload.info(new Date().toString()+ ": Customization of Embedded Tomcat. Embedded ClassLoader Reloading will be Disabled by setting to false");
            container.addContextCustomizers(context -> {
                context.setReloadable(false);
                turn_off_tomReload.info(new Date().toString()+ ": >>>>> >>>>> Embedded Tomcat Reloading has been set to False....");
            });
        };
    }
}

/* Convert this Code to Lambada
public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainerCustomizer() {
        turn_off_tomReload.info(new Date().toString()+ ": About to do Customization of Embedded Tomcat... >>Dis_Sangomalize<<");
        return new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {

            @Override
            public void customize(TomcatServletWebServerFactory container) {
                turn_off_tomReload.info(new Date().toString()+ ": Customization of Embedded Tomcat. Embedded ClassLoader Reloading will be Disabled by setting to false");
                container.addContextCustomizers(new TomcatContextCustomizer() {
                    @Override
                    public void customize(Context context) {
                        context.setReloadable(false);
                        turn_off_tomReload.info(new Date().toString()+ ": >>>>> >>>>> Embedded Tomcat Reloading has been set to False....");
                    }
                });
            }
        };
    }
* */