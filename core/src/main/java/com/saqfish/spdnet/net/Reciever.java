package com.saqfish.spdnet.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.effects.Transmuting;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.net.actor.Player;
import com.saqfish.spdnet.net.events.Events;
import com.saqfish.spdnet.net.events.Receive;
import com.saqfish.spdnet.net.events.Send;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class Reciever {
        private ObjectMapper mapper;
        private Net net;

        private boolean newMessage;
        private ArrayList<ChatMessage> messages;

        public Reciever(Net net, ObjectMapper mapper) {
                this.net = net;
                this.mapper = mapper;
                this.messages = new ArrayList<>();
        }

        public void startAll() {

                Emitter.Listener onAction = args -> {
                        int type = (int) args[0];
                        String data = (String) args[1];
                        handleAction(type, data);
                };
                Emitter.Listener onTransfer = args -> {
                        String data = (String) args[0];
                        handleTransfer(data);
                };
                Emitter.Listener onChat = args -> {
                        String id = (String) args[0];
                        String nick = (String) args[1];
                        String message = (String) args[2];
                        handleChat(id, nick, message);
                };
                Emitter.Listener onMotd = args -> {
                        String data = (String) args[0];
                        handleMotd(data);
                };
                net.socket().on(Events.ACTION, onAction);
                net.socket().on(Events.TRANSFER, onTransfer);
                net().socket().on(Events.CHAT, onChat);
                net.socket().once(Events.MOTD, onMotd);
        }

        public void cancelAll() {
                net.socket().off(Events.ACTION);
                net.socket().off(Events.TRANSFER);
                net.socket().off(Events.MOTD);
        }

        // Handlers

        // MOTD & seed handler
        public void handleMotd(String json) {
                try {
                        Receive.Motd motd = mapper.readValue(json, Receive.Motd.class);
                        NetWindow.motd(motd.motd, motd.seed);
                        net.seed(motd.seed);
                } catch (JsonProcessingException e) {
                        e.printStackTrace();
                }
        }

        // Action handler
        public void handleAction(int type, String json) {
                Player player;
                Receive.Join join;
                DeviceCompat.log("Action", "type: "+type);
                try {
                        switch (type) {
                                case Receive.MOVE:
                                        Receive.Move m = mapper.readValue(json, Receive.Move.class);
                                        Player.movePlayer(Player.getPlayer(m.id), m.pos, m.playerClass);
                                        break;
                                case Receive.JOIN:
                                        join = mapper.readValue(json, Receive.Join.class);
                                        Player.addPlayer(join.id, join.nick, join.playerClass, join.pos, join.depth, join.items);
                                        break;
                                case Receive.JOIN_LIST:
                                        Receive.JoinList jl = mapper.readValue(json, Receive.JoinList.class);
                                        for (int i = 0; i < jl.players.length; i++) {
                                                Receive.Join j = jl.players[i];
                                                Player.addPlayer(j.id, j.nick, j.playerClass, j.pos, j.depth, j.items);
                                        }
                                        break;
                                case Receive.LEAVE:
                                        Receive.Leave l = mapper.readValue(json, Receive.Leave.class);
                                        player = Player.getPlayer(l.id);
                                        if (player != null) player.leave();
                                        break;
                                case Receive.DEATH:
                                        Receive.Death d = mapper.readValue(json, Receive.Death.class);
                                        GLog.n(d.nick + " " + d.cause);
                                        GLog.newLine();
                                        break;
                                default:
                                        DeviceCompat.log("Unknown Action", json);
                        }
                } catch (JsonProcessingException e) {
                        e.printStackTrace();
                }
        }

        // Item sharing handler
        public void handleTransfer(String json) {
                try {
                        Receive.Transfer item = mapper.readValue(json, Receive.Transfer.class);
                        Class<?> k = Reflection.forNameUnhandled(addPkgName(item.className));
                        Item i = (Item) Reflection.newInstance(k);
                        i.cursed = item.cursed;
                        i.level(item.level);
                        if(item.identified) i.identify();
                        Dungeon.hero.belongings.backpack.items.add(i);
                        Transmuting.show(Dungeon.hero, i, i);
                        GLog.p("You received a "+i.name());
                } catch (Exception ignored) { }

        }

        // Chat handler

        public static class ChatMessage {
                public String id;
                public String nick;
                public String message;

                public ChatMessage (String id, String nick, String message){
                        this.id = id;
                        this.nick = nick;
                        this.message = message;
                }
        }

        public void handleChat(String id,String nick,String message){
                        messages.add(new ChatMessage(id, nick, message));
                        newMessage = true;
        }

        public void readMessages(){
                newMessage = false;
        }

        public ArrayList<ChatMessage> messages(){
                newMessage = false;
                return messages;
        }

        public List<ChatMessage> messages(int n){
                newMessage = false;
                if(messages.size() > n)
                        messages = new ArrayList(messages.subList(messages.size() - n, messages.size()));
                return messages;
        }

        public ChatMessage lastMessage(){
                newMessage = false;
                return messages.get(messages.size()-1);
        }

        public boolean newMessage(){
                return newMessage;
        }


        // Static helpers
        public static String addPkgName(String c) {
                return Game.pkgName + ".items." + c;
        }
}
