package za.co.vodacom.vodacommft.config;
/**
 * @author ncubeh on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.config
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Component
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"za.co.vodacom.vodacommft.repository.sfg_usr"},
        entityManagerFactoryRef = "usrEntityManager",
        transactionManagerRef = "usrTransactionManager"
)
public class JpaSfgUSRDataSourceConfig {
    @Autowired
    private OracleDbConfigProperties oracle_db_config;

    @Bean(name = "usr_datasource")
    public DataSource getUsrDataSource(){
        DataSourceBuilder datasource_usr_builder = DataSourceBuilder.create();
        datasource_usr_builder.driverClassName("oracle.jdbc.OracleDriver");
        datasource_usr_builder.url(oracle_db_config.getOracleUrl());
        datasource_usr_builder.username(oracle_db_config.getOracleUsernameForUSR());
        datasource_usr_builder.password(oracle_db_config.getOraclePassword());
        return datasource_usr_builder.build();
    }

    @Bean(name = "usrEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder entity_mngr_usr) {
        return entity_mngr_usr.dataSource(getUsrDataSource()).packages(new String[] {"za.co.vodacom.vodacommft.entity.sfg_usr"})
                .build();
    }
    @Bean(name = "usrTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(
            @Qualifier("usrEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
    /*REPLACED THIS CODE WITH THE MODERN WAY OF DOING THINGS*/

   /* @Bean(name = "usrEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entity_mngr_usr = new LocalContainerEntityManagerFactoryBean();
        entity_mngr_usr.setDataSource(getUsrDataSource());
        entity_mngr_usr.setPackagesToScan(new String[] {"za.co.vodacom.vodacomMFT.entity.sfg_usr"});
        JpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        entity_mngr_usr.setJpaVendorAdapter(jpaAdapter);
        entity_mngr_usr.setJpaProperties(jpaProperties());

        return entity_mngr_usr;
    }
    @Bean(name = "usrTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(@Qualifier("usrEntityManager") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }*/

    private final Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");

        return properties;
    }
}
