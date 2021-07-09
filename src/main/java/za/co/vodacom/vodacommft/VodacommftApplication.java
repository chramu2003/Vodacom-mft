package za.co.vodacom.vodacommft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/*@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages ="za.co.vodacom")
@EnableAspectJAutoProxy(proxyTargetClass = true)*/
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class VodacommftApplication {

    public static void main(String[] args) {
        SpringApplication.run(VodacommftApplication.class, args);
    }
}
