package tel.bvm.telegrambot.model;

import com.pengrad.telegrambot.model.Chat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Chat chat;

    private LocalDateTime localDateTime;

    private String notification;

    public NotificationTask() {

    }

    public NotificationTask(long id, Chat chat, LocalDateTime localDateTime, String notification) {
        this.id = id;
        this.chat = chat;
        this.localDateTime = localDateTime;
        this.notification = notification;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return id == that.id && Objects.equals(chat, that.chat) && Objects.equals(localDateTime, that.localDateTime) && Objects.equals(notification, that.notification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat, localDateTime, notification);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chat=" + chat +
                ", localDateTime=" + localDateTime +
                ", notification='" + notification + '\'' +
                '}';
    }
}
