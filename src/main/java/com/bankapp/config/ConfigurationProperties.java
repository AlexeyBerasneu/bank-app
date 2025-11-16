package com.bankapp.config;

import com.bankapp.model.Account;
import com.bankapp.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "com.bankapp")
@PropertySource("classpath:application.properties")
public class ConfigurationProperties {

    @Bean
    public SessionFactory sessionFactory() {
        Configuration configuration = new Configuration();
        configuration.
                addAnnotatedClass(Account.class)
                .addAnnotatedClass(User.class)
                .addPackage("com.bankapp.model")
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5434/postgres")
                .setProperty("hibernate.connection.username", "postgres")
                .setProperty("hibernate.connection.password", "admin")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "update");
        return configuration.buildSessionFactory();
    }
}
