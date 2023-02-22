package com.krllrn.viewed.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotService {
    SendMessage startBot(long chatId, String username);
    SendMessage addFilm(long chatId, String username, String film);
    SendMessage findFilm(long chatId, String film);
    SendMessage deleteFilm(long chatId, String username, String film);
    SendMessage lastN(long chatId, String number);
    SendMessage deleteAll(long chatId, String username);
    SendMessage help(long chatId, String username);
    SendMessage wrongMessage(long chatId);
}
