package ru.alefilas.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.alefilas.notification.model.EmailSettings;

import java.util.Properties;

@Configuration
@ComponentScan("ru.alefilas.notification")
public class EmailConfig {

    @Bean
    public Properties emailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.yandex.ru");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.auth", "true");

        return properties;
    }

    @Bean
    public EmailSettings settings() {
        EmailSettings settings = new EmailSettings();
        settings.setEmail("documentsApp@yandex.ru");
        settings.setPassword("Test123");
        return settings;
    }
}
