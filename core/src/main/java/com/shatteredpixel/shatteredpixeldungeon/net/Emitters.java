package com.shatteredpixel.shatteredpixeldungeon.net;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.events.Handler;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Emitters {
    private Socket socket;
    private final Handler handler;
    private final Net net;

    public Emitters(Net n){
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
}
