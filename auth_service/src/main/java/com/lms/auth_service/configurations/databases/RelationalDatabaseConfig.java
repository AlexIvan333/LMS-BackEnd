package com.lms.auth_service.configurations.databases;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.lms.auth_service.repositories.relational",
        entityManagerFactoryRef = "relationalEntityManagerFactory",
        transactionManagerRef = "relationalTransactionManager"
)
@EntityScan(basePackages = "com.lms.auth_service.entities.relational")
public class RelationalDatabaseConfig {

    private final DataSource dataSource;

    public RelationalDatabaseConfig(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean relationalEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.lms.auth_service.entities.relational")
                .persistenceUnit("relational")
                .build();
    }

    @Bean
    public PlatformTransactionManager relationalTransactionManager(
            @Qualifier("relationalEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
