package tel.bvm.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.pengrad.telegrambot.model.Update;
import tel.bvm.telegrambot.model.NotificationTask;
import tel.bvm.telegrambot.repository.NotificationTaskRepository;
import tel.bvm.telegrambot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4} (?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d) ([а-яА-Яа-яА-Я\\\\s]+|\\\\w+)");

    private final TelegramBot telegramBot;
    private final NotificationTaskService notificationTaskService;

    private final NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskService notificationTaskService, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskService = notificationTaskService;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);
                Message message = update.message();
                Long chatId = message.chat().id();
                String notificationUserText = message.text();

                if ("/start".equals(notificationUserText)) {
                    sendMessage(chatId, """
                            Привет! 
                            Чтобы создать напоминание, сообщи мне дату, время (с точностью до минуты) и текст напоминания в формате: 
                            12.03.2024 22:05:09 Выполнить курсовую работу
                            """);
                } else if (notificationUserText != null && !"/allNotificationInfo".equals(notificationUserText)) {
                    Matcher matcher = pattern.matcher(notificationUserText);
                    if (matcher.find()) {
                        LocalDateTime dateTime = parse(matcher.group(1));
                        if (Objects.isNull(dateTime)) {
                            sendMessage(chatId, "Некорректный формат даты или времени события");
                        } else {
                            String notification = matcher.group(2);
                            NotificationTask notificationTask = new NotificationTask();
                            notificationTask.setChatId(chatId);
                            notificationTask.setNotification(notification);
                            notificationTask.setLocalDateTime(dateTime);
                            notificationTaskService.save(notificationTask);
                            sendMessage(chatId, "Напоминание добавлено");
                        }
                    } else {
                        sendMessage(chatId, "Некорректный формат напоминания");
                    }
                } else if ("/allNotificationInfo".equals(notificationUserText)) {
                    notificationTaskRepository.findAll().forEach(
                            notificationTask -> sendMessage(
                                    notificationTask.getChatId(),
                                    notificationTask.getNotification()));
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error sending message {}", sendResponse.description());
        }
    }

    @Nullable
    private LocalDateTime parse(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
