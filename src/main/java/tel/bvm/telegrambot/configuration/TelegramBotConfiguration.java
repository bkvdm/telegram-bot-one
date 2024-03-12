package tel.bvm.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {

    private final String token;

    public TelegramBotConfiguration(@Value("${telegram.bot.token}") String token) {
        this.token = token;
    }

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot telegramBot = new TelegramBot(token);
        telegramBot.execute(new DeleteMyCommands());
        return telegramBot;
    }
}

//    @Bean
//    public TelegramBot telegramBot(@Value("${telegram.bot.token}") String token) {
//        return new TelegramBot(token);
//    }
