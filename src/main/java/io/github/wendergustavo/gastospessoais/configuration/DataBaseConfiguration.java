package io.github.wendergustavo.gastospessoais.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfiguration {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String user;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;


    @Bean
    public DataSource hikariDataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driver);

        hikariConfig.setMaximumPoolSize(10); //maximo de conexões liberadas
        hikariConfig.setMinimumIdle(1); // tamanho inicial do pool
        hikariConfig.setPoolName("gasto-db-pool");
        hikariConfig.setMaxLifetime(600000);// 600 mil ms (10 min)
        hikariConfig.setConnectionTimeout(100000);// timeout para conseguir conexão
        hikariConfig.setConnectionTestQuery("select 1");// query de teste

        return new HikariDataSource(hikariConfig);

    }
}
