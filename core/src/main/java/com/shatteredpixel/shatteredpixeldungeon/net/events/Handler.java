package com.shatteredpixel.shatteredpixeldungeon.net.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.net.Net;
import com.shatteredpixel.shatteredpixeldungeon.net.Settings;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.auth.Auth;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.message.Message;
import com.shatteredpixel.shatteredpixeldungeon.net.Types;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.motd.Motd;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.Actions;
import com.watabou.utils.DeviceCompat;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.message;
import static com.shatteredpixel.shatteredpixeldungeon.net.Util.motd;

public class Handler {
    private final ObjectMapper mapper;
    private Net net;

    public Handler(Net n) {
        mapper = new ObjectMapper();
        this.net = n;
    }

    public void handleMessage(String json){
        DeviceCompat.log("Message",json);
        try{
            Message message = mapper.readValue(json, Message.class);
            message(message.data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void handleAction(int type, String json) {
            switch (type){
                case Actions.MOVE:
                    DeviceCompat.log("Move",json);
                    break;
                case Actions.JOIN:
                    DeviceCompat.log("Join", json);
                    break;
                case Actions.JOINLIST:
                    DeviceCompat.log("JoinList",json);
                    break;
                case Actions.LEAVE:
                    DeviceCompat.log("Leave",json);
                    break;
                default:
                    DeviceCompat.log("Unknown Action",json);
            }

    }
    public void handleAuth(String json){
        DeviceCompat.log("Auth",json);
        try    {
            Auth auth = new Auth(Settings.auth_key());
            String j = mapper.writeValueAsString(auth);
            net.socket().emit("message", Types.Send.AUTH, j);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
}

    public void handleMotd(String json){
        DeviceCompat.log("MOTD",json);
        try{
            Motd motd = mapper.readValue(json, Motd.class);
            motd(motd.motd);
            net.seed(motd.seed);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public ObjectMapper mapper() {
        return this.mapper;
    }
}
