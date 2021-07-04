package com.shatteredpixel.shatteredpixeldungeon.net.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.net.Net;
import com.shatteredpixel.shatteredpixeldungeon.net.Settings;
import com.shatteredpixel.shatteredpixeldungeon.net.actor.Player;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Join;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.JoinList;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Leave;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action.Move;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.auth.Auth;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.message.Message;
import com.shatteredpixel.shatteredpixeldungeon.net.Types;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.motd.Motd;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.Actions;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.message;
import static com.shatteredpixel.shatteredpixeldungeon.net.Util.motd;
import static com.shatteredpixel.shatteredpixeldungeon.net.actor.Player.addPlayer;
import static com.shatteredpixel.shatteredpixeldungeon.net.actor.Player.getPlayer;
import static com.shatteredpixel.shatteredpixeldungeon.net.actor.Player.movePlayer;

public class Handler {
    private final ObjectMapper mapper;
    private Net net;

    public Handler(Net n) {
        mapper = new ObjectMapper();
        this.net = n;
    }

    public void handleMessage(String json){
        try{
            Message message = mapper.readValue(json, Message.class);
            message(message.data);
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
                    Move m = mapper.readValue(json, Move.class);
                    movePlayer(getPlayer(m.id), m.pos, m.playerClass);
                    break;
                case Actions.JOIN:
                    join = mapper.readValue(json, Join.class);
                    addPlayer(join.id,join.nick, join.playerClass, join.pos);
                    break;
                case Actions.JOINLIST:
                    JoinList jl = mapper.readValue(json, JoinList.class);
                    for (int i = 0; i < jl.players.length; i++) {
                        Join j = jl.players[i];
                        addPlayer(j.id,j.nick, j.playerClass, j.pos);
                    }
                    break;
                case Actions.LEAVE:
                    Leave l = mapper.readValue(json, Leave.class);
                    player = getPlayer(l.id);
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public ObjectMapper mapper() {
        return this.mapper;
    }
}
