package com.shatteredpixel.shatteredpixeldungeon.net.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Message;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.MessageTypes;

public class JsonHelper {
    private final ObjectMapper mapper;

    public JsonHelper() {
        mapper = new ObjectMapper();
    }

    public Message readMessage(int type, String json){
        Message message = null;
        try{
            switch (type) {
                case MessageTypes.MOTD:
                message = mapper.readValue(json, Message.class);
                break;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return message;
    }
}
