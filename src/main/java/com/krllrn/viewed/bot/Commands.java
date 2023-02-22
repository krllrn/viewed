package com.krllrn.viewed.bot;

import lombok.Getter;

@Getter
public enum Commands {
    START("/start"),
    ADD("/add"),
    FIND("/find"),
    LAST_FIVE("/last5"),
    SHOW_ALL("/all"),
    DELETE("/delete"),
    DELETE_ALL("/delete_all"),
    HELP("/help");

    private final String commandName;

    Commands(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}

