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
@EnableJpaRepositories(
        basePackages = {"za.co.vodacom.vodacommft.repository.sfg_rpt"},
        entityManagerFactoryRef = "rptEntityManager",
        transactionManagerRef = "rptTransactionManager"
)

public class JpaSfgRPTDataSourceConfig {
    @Autowired
    private OracleDbConfigProperties oracle_db_config;

    @Bean(name = "rpt_datasource")
    public DataSource getRptDataSource() {
        DataSourceBuilder datasource_rpt_builder = DataSourceBuilder.create();
        datasource_rpt_builder.driverClassName("oracle.jdbc.OracleDriver");
        datasource_rpt_builder.url(oracle_db_config.getOracleUrl());
        datasource_rpt_builder.username(oracle_db_config.getOracleUsernameForRPT());
        datasource_rpt_builder.password(oracle_db_config.getOraclePassword());
        return datasource_rpt_builder.build();
    }
    @Bean(name = "rptEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder.dataSource(getRptDataSource()).packages(new String[] {"za.co.vodacom.vodacommft.entity.sfg_rpt"})
                .build();
    }
    @Bean(name = "rptTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(
            @Qualifier("rptEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
 /*REPLACED THIS CODE WITH THE MODERN WAY OF DOING THINGS
 KEEPING IT SO WE KNOW OUR ANCESTORS--*/

    /*@Bean(name = "rptEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entity_mngr_rpt = new LocalContainerEntityManagerFactoryBean();
        entity_mngr_rpt.setDataSource(getRptDataSource());
        entity_mngr_rpt.setPackagesToScan(new String[] {"za.co.vodacom.vodacomMFT.entity.sfg_rpt"});
        JpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        entity_mngr_rpt.setJpaVendorAdapter(jpaAdapter);
        entity_mngr_rpt.setJpaProperties(jpaProperties());

        return entity_mngr_rpt;
    }
    @Bean(name = "rptTransactionManager")
    public PlatformTransactionManager jpaTransactionManager(@Qualifier("rptEntityManager") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;
    }*/

    private final Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");

        return properties;
    }
   /* @Bean(name = "sfg_rpt1_EntityManager")
    public LocalContainerEntityManagerFactoryBean ColFentityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder.dataSource(getRptDataSource()).packages(CollectedFileEntity.class)
                .build();
    }*/

}
