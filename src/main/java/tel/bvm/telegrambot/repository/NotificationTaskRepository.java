package tel.bvm.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tel.bvm.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    List<NotificationTask> findAllByNotificationDateTime(LocalDateTime localDateTime);

}
