package com.shatteredpixel.shatteredpixeldungeon.net;

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

    public Emitters(Socket s){
        this.socket = s;
        this.handler = new Handler(s);

        socket.once(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.once(Socket.EVENT_CONNECT, onConnected);
        socket.once(Socket.EVENT_DISCONNECT, onDisconnected);
        socket.on("message", onMessage);
    }

    private final Emitter.Listener onConnected = args -> Game.runOnRenderThread(() -> {
        //TODO: add connect stuff
    });

    private final Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(() -> socket.off("message"));
        }
    };

    private final Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(() -> {
                EngineIOException e = (EngineIOException) args[0];
                lastConnectionErrorMessage = e.getMessage();
                error(e.getMessage());
                socket.disconnect();
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
