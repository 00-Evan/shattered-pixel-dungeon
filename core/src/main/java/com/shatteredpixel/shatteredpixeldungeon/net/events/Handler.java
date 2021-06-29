package com.shatteredpixel.shatteredpixeldungeon.net.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Message;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.MessageTypes;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.message;
import static com.shatteredpixel.shatteredpixeldungeon.net.Util.motd;

public class JsonHelper {
    private final ObjectMapper mapper;

    public JsonHelper() {
        mapper = new ObjectMapper();
    }

    public void handleMessage(int type, String json){
        Message message = null;
        try{
            switch (type) {
                case MessageTypes.Recieve.AUTH:
                    message(message.message);
                    break;
                case MessageTypes.Recieve.MOTD:
                    message = mapper.readValue(json, Message.class);
                    motd(message.message);
                    break;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
