package com.shatteredpixel.shatteredpixeldungeon.net;

import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.EngineIOException;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.*;

public class Emitters {
    private Socket socket;
    public String lastConnectionErrorMessage;

    public Emitters(Socket s){
        this.socket = s;
        socket.once(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.once(Socket.EVENT_CONNECT, onConnected);
    }

    private final Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    message("Connected");
                }
            });
        }
    };

    private final Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    EngineIOException e = (EngineIOException) args[0];
                    lastConnectionErrorMessage = e.getMessage();
                    error(e.getMessage());
                    socket.disconnect();
                }
            });
        }
    };
}
