package ru.alefilas.notification.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alefilas.model.user.User;
import ru.alefilas.notification.NotificationService;
import ru.alefilas.notification.model.EmailSettings;
import ru.alefilas.notification.model.MessageType;
import ru.alefilas.notification.model.Settings;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class EmailSender implements NotificationService {

    @Autowired
    private Properties emailProperties;

    @Autowired
    private EmailSettings emailSettings;

    @Autowired
    private ExecutorService executorService;

    @Override
    public void send(User user, Long id, MessageType type) {

        Session session = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSettings.getEmail(), emailSettings.getPassword());
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(emailSettings.getEmail()));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            msg.setSubject(type.getSubject());
            msg.setText(
                    String.format(type.getText(), user.getUsername(), id)
            );

            executorService.submit(
                    () -> {
                        try {
                            Transport.send(msg);
                        } catch (MessagingException e) {
                            log.error("Can't send email", e);
                        }
                    }
            );
        } catch (MessagingException e) {
            log.error("Can't send email", e);
        }
    }

    @Override
    public void setConnectionSettings(Properties properties) {
        properties.forEach((key, value) -> {
            Object obj = emailProperties.replace(key, value);
            if (obj == null) {
                emailProperties.setProperty(key.toString(), value.toString());
            }
        });
    }

    @Override
    public void setAccountSettings(Settings settings) {
        this.emailSettings = (EmailSettings) settings;
    }

    @Override
    public Settings getAccountSettings() {
        return emailSettings;
    }

    @Override
    public Properties getConnectionSettings() {
        return emailProperties;
    }
}
