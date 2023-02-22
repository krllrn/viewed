package com.krllrn.viewed.bot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BotConfig {

    @Value("${bot.username}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;
}
