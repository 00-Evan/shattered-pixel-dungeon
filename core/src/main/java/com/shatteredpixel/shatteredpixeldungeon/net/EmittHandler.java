package com.shatteredpixel.shatteredpixeldungeon.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shatteredpixel.shatteredpixeldungeon.net.events.Handler;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.auth.Auth;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Actions;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Ascend;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Descend;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.action.Move;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.message.Message;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class EmittHandler {
    private Socket socket;
    private final Handler handler;
    private final Net net;

    public EmittHandler(Net n){
        this.net = n;
        this.socket = net.socket();
        this.handler = new Handler(net);
    }

    public void startAll(){
        Emitter.Listener onMessage = args -> {
            String data = (String) args[0];
            handler.handleMessage(data);
        };
        Emitter.Listener onAuth = args -> {
            String data = (String) args[0];
            handler.handleAuth(data);
        };
        Emitter.Listener onAction = args -> {
            int type = (int) args[0];
            String data = (String) args[1];
            handler.handleAction(type, data);
        };
        Emitter.Listener onMotd = args -> {
            String data = (String) args[0];
            handler.handleMotd(data);
        };
        socket.on(Types.Recieve.ACTION, onAction);
        socket.once(Types.Recieve.AUTH, onAuth);
        socket.on(Types.Recieve.MESSAGE, onMessage);
        socket.once(Types.Recieve.MOTD, onMotd);
    }
    public void cancelAll(){
        socket.off(Types.Recieve.ACTION);
        socket.off(Types.Recieve.AUTH);
        socket.off(Types.Recieve.MESSAGE);
        socket.off(Types.Recieve.MOTD);
    }
    public void send(String event, int type, String data) {
        String message = null;
        try {
            message = handler.mapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        net.socket().emit(event, type, message);
    }

    public Handler handler() {
        return this.handler;
    }
    public void send(int action, int type, int... data) {
        try {
            switch (action) {
            case Types.Send.ACTION:
                String json = "";
                switch (type) {
                    case Actions.ASC:
                        Ascend a = new Ascend(data[0], data[1], data[2]);
                            json = handler.mapper().writeValueAsString(a);
                        break;
                    case Actions.DESC:
                        Descend d = new Descend(data[0], data[1], data[2]);
                        json = handler.mapper().writeValueAsString(d);
                        break;
                    case Actions.MOVE:
                        Move m = new Move(data[0], data[1], data[2]);
                        json = handler.mapper().writeValueAsString(m);
                }
                if(socket.connected()) socket.send(action, json);
                break;
        }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
