package ru.Nikita.NauJava;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.Nikita.NauJava.console.CommandProcessor;
import ru.Nikita.NauJava.model.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Configuration
public class Config
{
    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public List<Storage> storageContainer() {
        return new ArrayList<>();
    }

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }




}