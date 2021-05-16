package ru.alefilas.notification;

import ru.alefilas.model.user.User;
import ru.alefilas.notification.model.MessageType;
import ru.alefilas.notification.model.Settings;

import javax.mail.MessagingException;
import java.util.Properties;

public interface NotificationService {

    void send(User user, Long id, MessageType type);

    void setConnectionSettings(Properties properties);

    void setAccountSettings(Settings settings);

    Settings getAccountSettings();

    Properties getConnectionSettings();

}
