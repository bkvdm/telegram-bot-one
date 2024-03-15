package tel.bvm.telegrambot.service;

import org.springframework.stereotype.Service;
import tel.bvm.telegrambot.model.NotificationTask;
import tel.bvm.telegrambot.repository.NotificationTaskRepository;

@Service
public class NotificationTaskService {

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void save(NotificationTask notificationTask) {
        notificationTaskRepository.save(notificationTask);
    }
}
