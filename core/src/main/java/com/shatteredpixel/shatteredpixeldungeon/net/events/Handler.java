package com.shatteredpixel.shatteredpixeldungeon.net.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.net.Net;
import com.shatteredpixel.shatteredpixeldungeon.net.Settings;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Auth;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Message;
import com.shatteredpixel.shatteredpixeldungeon.net.Types;
import com.shatteredpixel.shatteredpixeldungeon.net.events.player.recieve.Motd;
import com.shatteredpixel.shatteredpixeldungeon.net.events.player.recieve.Move;

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
                case Types.Recieve.AUTH:
                    Auth auth = new Auth(Settings.auth_key());
                    String j = mapper.writeValueAsString(auth);
                    net.socket().emit("message", Types.Send.AUTH, j);
                    break;
                case Types.Recieve.MESSAGE:
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

    public void handleAction(String json){
        System.out.println("Action -> "+json);
        try{
            Move m = mapper.readValue(json, Move.class);
            Move.Player p = m.player;
            Move.Data d = m.data;
            System.out.println("Move -> "+p.nick+" dst: "+d.dst);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public ObjectMapper mapper() {
        return this.mapper;
    }
}
