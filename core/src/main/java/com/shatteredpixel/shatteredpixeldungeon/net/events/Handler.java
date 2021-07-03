package com.shatteredpixel.shatteredpixeldungeon.net.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.net.Net;
import com.shatteredpixel.shatteredpixeldungeon.net.Settings;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Join;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.JoinList;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Leave;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Move;
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
        try {
            switch (type){
                case Actions.MOVE:
                    Move m = mapper.readValue(json, Move.class);
                    DeviceCompat.log("Move","id:"+m.id +" nick:" +m.nick+ " depth:"+m.depth +" pos:" +m.pos);
                    break;
                case Actions.JOIN:
                    Join j = mapper.readValue(json, Join.class);
                    DeviceCompat.log("Join","id:"+j.id +" nick:" +j.nick+ " depth:"+j.depth +" pos:" +j.pos);
                    break;
                case Actions.JOINLIST:
                    DeviceCompat.log("JoinList",json);
                    JoinList jl = mapper.readValue(json, JoinList.class);
                    for (int i = 0; i < jl.players.length; i++) {
                        Join join = jl.players[i];
                        DeviceCompat.log("Player "+i,"id:"+join.id +" nick:" +join.nick+ " depth:"+join.depth +" pos:" +join.pos);
                    }
                    break;
                case Actions.LEAVE:
                    DeviceCompat.log("Leave",json);
                    Leave l = mapper.readValue(json, Leave.class);
                    DeviceCompat.log("Leave","id:"+l.id +" nick:" +l.nick+ " depth:"+l.depth +" pos:" +l.pos);
                    break;
                default:
                    DeviceCompat.log("Unknown Action",json);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
        try{
            Motd motd = mapper.readValue(json, Motd.class);
            motd(motd.motd);
            net.seed(motd.seed);
            DeviceCompat.log("MOTD","message:"+motd.motd +" seed:" +motd.seed);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public ObjectMapper mapper() {
        return this.mapper;
    }
}
