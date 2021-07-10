package com.shatteredpixel.shatteredpixeldungeon.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.net.actor.Player;
import com.shatteredpixel.shatteredpixeldungeon.net.events.Events;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.Recieve;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Join;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.JoinList;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Leave;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.motd.Motd;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.Send;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Ascend;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Descend;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Move;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.auth.Auth;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.message.Message;
import com.shatteredpixel.shatteredpixeldungeon.net.windows.NetWindow;
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
            handleAuth();
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
            NetWindow.message(message.data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void handleAction(int type, String json) {
        Player player;
        Join join;
        try {
            switch (type){
                case Recieve.MOVE:
                    com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Move m = mapper.readValue(json, com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Move.class);
                    Player.movePlayer(Player.getPlayer(m.id), m.pos, m.playerClass);
                    break;
                case Recieve.JOIN:
                    join = mapper.readValue(json, Join.class);
                    Player.addPlayer(join.id,join.nick, join.playerClass, join.pos);
                    break;
                case Recieve.JOIN_LIST:
                    JoinList jl = mapper.readValue(json, JoinList.class);
                    for (int i = 0; i < jl.players.length; i++) {
                        Join j = jl.players[i];
                        Player.addPlayer(j.id,j.nick, j.playerClass, j.pos);
                    }
                    break;
                case Recieve.LEAVE:
                    Leave l = mapper.readValue(json, Leave.class);
                    player = Player.getPlayer(l.id);
                    if(player != null) player.leave();
                    break;
                default:
                    DeviceCompat.log("Unknown Action",json);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void handleAuth(){
        try {
            Auth auth = new Auth(Settings.auth_key());
            String j = mapper.writeValueAsString(auth);
            net.socket().emit(Events.AUTH, j);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void handleMotd(String json){
        try{
            Motd motd = mapper.readValue(json, Motd.class);
            NetWindow.motd(motd.motd, motd.seed);
            net.seed(motd.seed);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(int type, String data) {
        String message = null;
        try {
            message = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        net.socket().emit(Events.MESSAGE, type, message);
    }


    public void sendPlayerListRequest(){
        net.socket().emit(Events.PLAYERLISTREQUEST, 0);
    }

    public void sendAction(int type, int... data) {
        try {
            String json = "";
            switch (type) {
                case Send.ASC:
                    Ascend a = new Ascend(data[0], data[1], data[2]);
                    json = mapper.writeValueAsString(a);
                    break;
                case Send.DESC:
                    Descend d = new Descend(data[0], data[1], data[2]);
                    json = mapper.writeValueAsString(d);
                    break;
                case Send.MOVE:
                    Move m = new Move(data[0], data[1], data[2]);
                    json = mapper.writeValueAsString(m);
            }
            if(net.socket().connected()) net.socket().emit(Events.ACTION,type, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public ObjectMapper mapper() {
        return mapper;
    }
}
