package com.shatteredpixel.shatteredpixeldungeon.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.net.actor.Player;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Join;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.JoinList;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Leave;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.motd.Motd;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.Actions;
import com.shatteredpixel.shatteredpixeldungeon.net.events.Events;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.Send;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Ascend;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Descend;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Move;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.auth.Auth;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.message.Message;
import com.watabou.utils.DeviceCompat;

import io.socket.emitter.Emitter;

public class Handler {
    private final ObjectMapper mapper;
    private final Net net;

    public Handler(Net n){
        this.net = n;
        mapper = new ObjectMapper();
    }

    public void startAll(){
        Emitter.Listener onMessage = args -> {
            String data = (String) args[0];
            handleMessage(data);
        };
        Emitter.Listener onAuth = args -> {
            String data = (String) args[0];
            handleAuth(data);
        };
        Emitter.Listener onAction = args -> {
            int type = (int) args[0];
            String data = (String) args[1];
            handleAction(type, data);
        };
        Emitter.Listener onMotd = args -> {
            String data = (String) args[0];
            handleMotd(data);
        };
        net.socket().on(Events.ACTION, onAction);
        net.socket().once(Events.AUTH, onAuth);
        net.socket().on(Events.MESSAGE, onMessage);
        net.socket().once(Events.MOTD, onMotd);
    }
    public void cancelAll(){
        net.socket().off(Events.ACTION);
        net.socket().off(Events.AUTH);
        net.socket().off(Events.MESSAGE);
        net.socket().off(Events.MOTD);
    }


    public void handleMessage(String json){
        DeviceCompat.log("MESSAGE", json);
        try{
            Message message = mapper.readValue(json, Message.class);
            Util.message(message.data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void handleAction(int type, String json) {
        Player player;
        Join join;
        try {
            switch (type){
                case Actions.MOVE:
                    com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Move m = mapper.readValue(json, com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Move.class);
                    Player.movePlayer(Player.getPlayer(m.id), m.pos, m.playerClass);
                    break;
                case Actions.JOIN:
                    join = mapper.readValue(json, Join.class);
                    Player.addPlayer(join.id,join.nick, join.playerClass, join.pos);
                    break;
                case Actions.JOINLIST:
                    JoinList jl = mapper.readValue(json, JoinList.class);
                    for (int i = 0; i < jl.players.length; i++) {
                        Join j = jl.players[i];
                        Player.addPlayer(j.id,j.nick, j.playerClass, j.pos);
                    }
                    break;
                case Actions.LEAVE:
                    Leave l = mapper.readValue(json, Leave.class);
                    player = Player.getPlayer(l.id);
                    if(player != null) {
                        player.die(this);
                    }
                    break;
                default:
                    DeviceCompat.log("Unknown Action",json);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void handleAuth(String json){
        try    {
            Auth auth = new Auth(Settings.auth_key());
            String j = mapper.writeValueAsString(auth);
            net.socket().emit(Events.MESSAGE, Send.AUTH, j);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void handleMotd(String json){
        try{
            Motd motd = mapper.readValue(json, Motd.class);
            Util.motd(motd.motd, motd.seed);
            net.seed(motd.seed);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void send(String event, int type, String data) {
        String message = null;
        try {
            message = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        net.socket().emit(event, type, message);
    }

    public void send(int action, int type, int... data) {
        try {
            switch (action) {
            case Send.ACTION:
                String json = "";
                switch (type) {
                    case Actions.ASC:
                        Ascend a = new Ascend(data[0], data[1], data[2]);
                            json = mapper.writeValueAsString(a);
                        break;
                    case Actions.DESC:
                        Descend d = new Descend(data[0], data[1], data[2]);
                        json = mapper.writeValueAsString(d);
                        break;
                    case Actions.MOVE:
                        Move m = new Move(data[0], data[1], data[2]);
                        json = mapper.writeValueAsString(m);
                }
                if(net.socket().connected()) net.socket().send(action, json);
                break;
        }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public ObjectMapper mapper() {
        return mapper;
    }
}
