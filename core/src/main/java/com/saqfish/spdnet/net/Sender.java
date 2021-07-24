package com.saqfish.spdnet.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.mobs.Mob;
import com.saqfish.spdnet.items.Heap;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.net.actor.Player;
import com.saqfish.spdnet.net.events.Events;
import com.saqfish.spdnet.net.events.Send;

import io.socket.client.Ack;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class Sender {
        private ObjectMapper mapper;
        private Net net;

        public Sender(Net net, ObjectMapper mapper){
                this.net = net;
                this.mapper = mapper;
        }

        public void sendPlayerListRequest(){
                net.socket().emit(Events.PLAYERLISTREQUEST, 0);
        }

        public void sendTransfer(Item i, String id, Heap h) {
                Send.Transfer item = new Send.Transfer(i, id);
                net.socket().emit(Events.TRANSFER, map(item), (Ack) args -> {
                    try {
                            Boolean enabled = (Boolean)args[0];
                            if(enabled) h.remove(i);
                    }catch(Exception e){ }
                });
        }

        public void sendAction(int type, String s) {
                if(net.socket().connected()) net.socket().emit(Events.ACTION,type, s);
        }

        public void sendMessage(int type, String data) {
                String message = map(data);
                if(net.socket().connected()&& message != null) net.socket().emit(Events.MESSAGE, type, message);
        }

        public void sendAction(int type, int... data) {
                String json = "";
                switch (type) {
                        case Send.INTERLEVEL:
                                Send.Interlevel d;
                                d = new Send.Interlevel(data[0], data[1], data[2]);
                                json = map(d);
                                break;
                }
                if(net.socket().connected()&& json != null) net.socket().emit(Events.ACTION,type, json);
        }

        public void sendAction(int type, Object o) {
                String json = "";
                switch (type) {
                        case Send.DEATH:
                                Send.Death d = new Send.Death(o);
                                json = map(d);
                                break;
                        case Send.MOVE:
                                Send.Move m = new Send.Move(((Integer)o));
                                json = map(m);
                }
                if(net.socket().connected() && json != null) net.socket().emit(Events.ACTION,type, json);
        }

        public String map(Object o){
                try {
                        return mapper.writeValueAsString(o);
                } catch (JsonProcessingException e) {
                        e.printStackTrace();
                }
                return null;
        }
}
