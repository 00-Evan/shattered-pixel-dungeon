package com.shatteredpixel.shatteredpixeldungeon.net.emit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shatteredpixel.shatteredpixeldungeon.net.Net;
import com.shatteredpixel.shatteredpixeldungeon.net.Types;
import com.shatteredpixel.shatteredpixeldungeon.net.events.Handler;
import com.shatteredpixel.shatteredpixeldungeon.net.events.player.Ascend;
import com.shatteredpixel.shatteredpixeldungeon.net.events.player.Descend;
import com.shatteredpixel.shatteredpixeldungeon.net.events.player.Move;

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
            int type = (int) args[0];
            String data = (String) args[1];
            handler.handleMessage(type, data);
        };

        Emitter.Listener onMotd = args -> {
            String data = (String) args[0];
            handler.handleMotd(data);
        };

        socket.on("message", onMessage);
        socket.once("motd", onMotd);
    }
    public void cancelAll(){
        socket.off("message");
        socket.off("motd");
    }

    public void send(int action, int type, int data) {
        try {
            switch (action) {
            case Types.Send.ACTION:
                String json = "";
                switch (type) {
                    case Actions.ASC:
                        Ascend a = new Ascend(data);
                            json = handler.mapper().writeValueAsString(a);
                        break;
                    case Actions.DESC:
                        Descend d = new Descend(data);
                        json = handler.mapper().writeValueAsString(d);
                        break;
                    case Actions.MOVE:
                        Move m = new Move(data);
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
