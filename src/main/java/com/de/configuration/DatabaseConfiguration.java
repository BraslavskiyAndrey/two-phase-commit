package com.de.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Bean(name = "fly_db_config")
    @Primary
    @ConfigurationProperties("database-fly.datasource")
    public DataSourceProperties flySourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "fly_db")
    @Primary
    @ConfigurationProperties("database-fly.datasource.configuration")
    public DataSource flyDbDatasource(@Qualifier("fly_db_config") DataSourceProperties flySourceProperties) {
        return flySourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "hotel_db_config")
    @ConfigurationProperties("database-hotel.datasource")
    public DataSourceProperties hotelSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "hotel_db")
    @ConfigurationProperties("database-hotel.datasource.configuration")
    public DataSource hotelDbDatasource(@Qualifier("hotel_db_config") DataSourceProperties hotelSourceProperties) {
        return hotelSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "account_db_config")
    @ConfigurationProperties("database-account.datasource")
    public DataSourceProperties accountSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "account_db")
    @ConfigurationProperties("database-account.datasource.configuration")
    public DataSource accountDbDatasource(@Qualifier("account_db_config") DataSourceProperties accountSourceProperties) {
        return accountSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

}
