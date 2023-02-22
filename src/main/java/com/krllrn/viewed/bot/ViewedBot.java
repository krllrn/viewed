package com.krllrn.viewed.bot;

import com.krllrn.viewed.mappers.CommandParse;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
@PropertySource("config.properties")
public class ViewedBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    BotService botService;

    @Autowired
    CommandParse commandParse;

    public ViewedBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            List<String> cp = commandParse.getCommandAndText(messageText);
            String command = cp.get(0);
            switch (command) {
                case "/start":
                    try {
                        execute(botService.startBot(chatId, username));
                        log.info("Reply sent to /start.");
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                    break;
                case "/add":
                    try {
                        if(cp.size() > 1) {
                            execute(botService.addFilm(chatId, username, cp.get(1)));
                            log.info("Reply sent to /add. Film is: " + cp.get(1));
                        } else {
                            execute(botService.wrongMessage(chatId));
                        }
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                    break;
                case "/find":
                    try {
                        if(cp.size() > 1) {
                            execute(botService.findFilm(chatId, cp.get(1)));
                            log.info("Reply sent to /find. Film is: " + cp.get(1));
                        } else {
                            execute(botService.wrongMessage(chatId));
                        }
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                    break;
                case "/delete":
                    try {
                        if(cp.size() > 1) {
                            execute(botService.deleteFilm(chatId, username, cp.get(1)));
                            log.info("Reply sent to /delete. Film is: " + cp.get(1));
                        } else {
                            execute(botService.wrongMessage(chatId));
                        }
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                    break;
                case "/last":
                    try {
                        if(cp.size() > 1) {
                            execute(botService.lastN(chatId, cp.get(1)));
                            log.info("Reply sent to /last. Number is: " + cp.get(1));
                        } else {
                            execute(botService.wrongMessage(chatId));
                        }
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                    break;
                case "/clear":
                    try {
                        execute(botService.deleteAll(chatId, username));
                        log.info("Reply sent to /clear. User: " + username);
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                    break;
                case "/help":
                    try {
                        execute(botService.help(chatId, username));
                        log.info("Reply sent to /help. User: " + username);
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
                    break;
                default:
                    try {
                        execute(botService.wrongMessage(update.getMessage().getChatId()));
                        log.info("Sent wrong message.");
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                    }
            }
        }
    }
}