package com.shatteredpixel.shatteredpixeldungeon.net;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.events.Handler;
import com.watabou.noosa.Game;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.EngineIOException;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.*;

public class Emitters {
    private Socket socket;
    private final Handler handler;
    private final Net net;

    public Emitters(Net n){
        this.net = n;
        this.socket = net.socket();
        this.handler = new Handler(n);
    }

    public void startAll(){
        Emitter.Listener onMessage = args -> Game.runOnRenderThread(() -> {
            int type = (int) args[0];
            String data = (String) args[1];
            handler.handleMessage(type, data);
        });

        Emitter.Listener onMotd = args -> Game.runOnRenderThread(() -> {
            String data = (String) args[0];
            handler.handleMotd(data);
        });
        socket.on("message", onMessage);
        socket.once("motd", onMotd);
    }
    public void cancelAll(){
        socket.off("message");
        socket.off("motd");
    }
}
