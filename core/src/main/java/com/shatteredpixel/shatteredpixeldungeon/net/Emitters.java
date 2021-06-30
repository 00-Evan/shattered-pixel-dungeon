package com.shatteredpixel.shatteredpixeldungeon.net;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.events.Handler;
import com.watabou.noosa.Game;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.EngineIOException;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.*;

public class Emitters {
    private final Socket socket;
    public String lastConnectionErrorMessage;
    private final Handler handler;
    private final Net net;

    public Emitters(){
        this.net = ((ShatteredPixelDungeon) ShatteredPixelDungeon.instance).net;
        this.socket = net.socket();
        this.handler = new Handler(this.socket);

        socket.once(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.once(Socket.EVENT_CONNECT, onConnected);
        socket.once(Socket.EVENT_DISCONNECT, onDisconnected);
        socket.on("message", onMessage);
    }

    public void cancelAll(){
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.off(Socket.EVENT_CONNECT, onConnected);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnected);
        socket.off("message", onMessage);
    }

    private final Emitter.Listener onConnected = args -> Game.runOnRenderThread(() -> {
        //TODO: add connect stuff
    });

    private final Emitter.Listener onDisconnected = args -> Game.runOnRenderThread(() -> {
        //TODO: add disconnect stuff
    });

    private final Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(() -> {
                EngineIOException e = (EngineIOException) args[0];
                lastConnectionErrorMessage = e.getMessage();
                error(e.getMessage());
                net.disconnect();
            });
        }
    };

    private final Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(() -> {
                int type = (int) args[0];
                String data = (String) args[1];
                handler.handleMessage(type, data);
            });
        }
    };

}
