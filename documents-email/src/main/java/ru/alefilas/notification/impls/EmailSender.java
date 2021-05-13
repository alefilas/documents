package ru.alefilas.notification.impls;

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

@Service
public class EmailSender implements NotificationService {

    @Autowired
    private Properties emailProperties;

    @Autowired
    private EmailSettings emailSettings;

    @Override
    public void send(User user, Long id, MessageType type) throws MessagingException {

        Session session = Session.getInstance(emailProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSettings.getEmail(), emailSettings.getPassword());
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailSettings.getEmail()));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        msg.setSubject(type.getSubject());
        msg.setText(
                String.format(type.getText(), user.getName(), id)
        );

        Transport.send(msg);
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
