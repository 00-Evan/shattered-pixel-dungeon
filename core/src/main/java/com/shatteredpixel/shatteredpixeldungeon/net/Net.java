package com.shatteredpixel.shatteredpixeldungeon.net;

import com.watabou.noosa.Game;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.EngineIOException;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.error;

public class Net {
    private Socket socket;
    private Emitters emitters = null;
    private long seed;

    public Net(String address, String key){
        URI url = URI.create(address);

        Settings.authority(url.getAuthority());
        Settings.address(url.getHost());
        Settings.port(url.getPort());
        Settings.auth_key(key);
        session(Settings.uri());
    }

    public Net(String key){
        Settings.address("127.0.0.1");
        Settings.port(5500);
        Settings.auth_key(key);
        session(Settings.uri());
    }

    public Net(){
        Settings.address("127.0.0.1");
        Settings.port(5500);
        Settings.auth_key("1234");
        session(Settings.uri());
    }

    public void setupEvents(){
        Emitter.Listener onConnected = args -> Game.runOnRenderThread(() -> {
        });

        Emitter.Listener onDisconnected = args -> Game.runOnRenderThread(() -> {
        });

        Emitter.Listener onConnectionError = args -> Game.runOnRenderThread(() -> {
            EngineIOException e = (EngineIOException) args[0];
            error(e.getMessage());
            disconnect();
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.on(Socket.EVENT_CONNECT, onConnected);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnected);
    }

    public void endEvents(){
        socket.off(Socket.EVENT_CONNECT_ERROR);
        socket.off(Socket.EVENT_CONNECT);
        socket.off(Socket.EVENT_DISCONNECT);
    }

    public void session(URI address){
        socket = IO.socket(address);
        emitters = new Emitters(Net.this);
        setupEvents();
    }

    public URI uri(){
        return Settings.uri();
    }

    public void toggle() {
        if(!socket.connected())
            connect();
        else
            disconnect();
    }

    public void connect() {
        emitters.startAll();
        socket.connect();
    }

    public void disconnect(){
        emitters.cancelAll();
        socket.disconnect();
    }

    public Socket socket(){
        return this.socket;
    }

    public Boolean connected() {
        return socket != null && socket.connected();
    }

    public void die(){
        if (socket != null) {
            endEvents();
            disconnect();
            socket = null;
        }
    }

    public long seed() { return this.seed; }
    public void seed(long seed) { this.seed = seed; }
}
