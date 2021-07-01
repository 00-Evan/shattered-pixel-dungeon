package com.shatteredpixel.shatteredpixeldungeon.net.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.Net;
import com.shatteredpixel.shatteredpixeldungeon.net.Settings;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Auth;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Message;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.MessageTypes;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.message;
import static com.shatteredpixel.shatteredpixeldungeon.net.Util.motd;

public class Handler {
    private final ObjectMapper mapper;
    private Net net;

    public Handler(Net n) {
        mapper = new ObjectMapper();
        this.net = n;
    }

    public void handleMessage(int type, String json){
        Message message = null;
        System.out.println("Message -> "+json);
        try{
            switch (type) {
                case MessageTypes.Recieve.AUTH:
                    Auth auth = new Auth(Settings.auth_key());
                    String j = mapper.writeValueAsString(auth);
                    net.socket().emit("message", MessageTypes.Send.AUTH, j);
                    break;
                case MessageTypes.Recieve.MESSAGE:
                    message = mapper.readValue(json, Message.class);
                    message(message.data);
                    break;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void handleMotd(String json){
        System.out.println("Motd -> "+json);
        try{
            Motd motd = mapper.readValue(json, Motd.class);
            motd(motd.motd);
            net.seed(motd.seed);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
