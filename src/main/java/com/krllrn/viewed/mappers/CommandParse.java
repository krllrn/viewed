package com.krllrn.viewed.mappers;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandParse {

    public List<String> getCommandAndText(String message) {
        List<String> parse = new ArrayList<>();
        int index = message.indexOf(' ');

        if (index > -1) {
            parse.add(message.substring(0, index));
            parse.add(message.substring(index+1));
        } else {
            parse.add(message);
        }
        return parse;
    }
}
