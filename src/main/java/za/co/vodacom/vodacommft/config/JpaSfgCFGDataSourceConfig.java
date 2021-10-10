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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Component
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"za.co.vodacom.vodacommft.repository.sfg_cfg"},
        entityManagerFactoryRef = "cfgEntityManager",
        transactionManagerRef = "cfgTransactionManager"
)
public class JpaSfgCFGDataSourceConfig {

    @Autowired
    private OracleDbConfigProperties oracle_db_config;

    @Bean(name ="cfg_datasource")
    @Primary
    public DataSource getCfgDataSource() {
        DataSourceBuilder dataSource_cfg_builder = DataSourceBuilder.create();
        dataSource_cfg_builder.driverClassName("org.postgresql.Driver");
        dataSource_cfg_builder.url(oracle_db_config.getOracleUrl());
        dataSource_cfg_builder.username(oracle_db_config.getOracleUserNameforCFG());
        dataSource_cfg_builder.password(oracle_db_config.getOraclePassword());
        return dataSource_cfg_builder.build();

    }
    @Bean(name = "cfgEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder.dataSource(getCfgDataSource()).packages(new String[] {"za.co.vodacom.vodacommft.entity.sfg_cfg"})
                .build();
    }
    @Bean(name = "cfgTransactionManager")
    @Primary
    public PlatformTransactionManager jpaTransactionManager(
            @Qualifier("cfgEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
    /*
        REPLACED THIS CODE WITH THE MODERN WAY OF DOING THINGS *** I will keep this code here for reference. Its Legacy code
    */

    /*@Bean(name = "cfgEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entity_mngr_cfg = new LocalContainerEntityManagerFactoryBean();
        entity_mngr_cfg.setDataSource(getCfgDataSource());
        entity_mngr_cfg.setPackagesToScan(new String[] {"za.co.vodacom.vodacomMFT.entity.sfg_cfg"});
        JpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        entity_mngr_cfg.setJpaVendorAdapter(jpaAdapter);
        entity_mngr_cfg.setJpaProperties(jpaProperties());
        return entity_mngr_cfg;
    }

    @Bean(name = "cfgTransactionManager")
    @Primary
    public PlatformTransactionManager jpaTransactionManager(@Qualifier("cfgEntityManager") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;

    }*/
    private final Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }

}
