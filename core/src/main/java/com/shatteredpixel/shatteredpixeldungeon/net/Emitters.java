package com.shatteredpixel.shatteredpixeldungeon.net;

import com.shatteredpixel.shatteredpixeldungeon.net.events.JsonHelper;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Message;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.EngineIOException;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.*;

public class Emitters {
    private Socket socket;
    public String lastConnectionErrorMessage;
    private JsonHelper json;

    public Emitters(Socket s){
        this.socket = s;
        this.json = new JsonHelper();

        socket.once(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.once(Socket.EVENT_CONNECT, onConnected);
        socket.once(Socket.EVENT_DISCONNECT, onDisconnected);
        socket.on("message", onMessage);
    }

    private final Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    //TODO: add connect stuff
                }
            });
        }
    };

    private final Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    socket.off("message");
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

    private final Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    int type = (int) args[0];
                    String data = (String) args[1];
                    Message message = json.readMessage(type, data);
                    if(message != null) message(message.message);
                }
            });
        }
    };

}
