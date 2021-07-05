package com.shatteredpixel.shatteredpixeldungeon.net;

import com.sun.jndi.toolkit.url.Uri;
import com.watabou.utils.DeviceCompat;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.EngineIOException;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.error;

public class Net {
    private Socket socket;
    private EmittHandler emitter = null;
    private long seed;

    public Net(String address, String key){
        URI url = URI.create(address);
        Settings.scheme(url.getScheme());
        Settings.address(url.getHost());
        Settings.port(url.getPort());
        Settings.auth_key(key);
        session(Settings.uri());
    }

    public Net(String key){
        Settings.address("127.0.0.1");
        Settings.port(5000);
        Settings.auth_key(key);
        session(Settings.uri());
    }

    public Net(){
        Settings.address("127.0.0.1");
        Settings.port(5000);
        Settings.auth_key(DeviceCompat.isDebug() ? "debug": "empty");
        session(Settings.uri());
    }

    public void setupEvents(){
        Emitter.Listener onConnected = args -> {
            emitter.startAll();
        };

        Emitter.Listener onDisconnected = args -> {
            emitter.cancelAll();
        };

        Emitter.Listener onConnectionError = args -> {
            EngineIOException e = (EngineIOException) args[0];
            error(e.getMessage());
            emitter.cancelAll();
            disconnect();
        };

        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.on(Socket.EVENT_CONNECT, onConnected);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnected);
    }

    public void endEvents(){
        socket.off();
    }

    public void session(URI address){
        IO.Options options = IO.Options.builder()
                .setForceNew(true)
                .setReconnection(false)
                .build();
        socket = IO.socket(address, options);
        emitter = new EmittHandler(Net.this);
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
        socket.connect();
    }

    public void disconnect(){
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
            if(socket.connected()) disconnect();
            endEvents();
            socket = null;
        }
        emitter = null;
    }

    public long seed() { return this.seed; }
    public void seed(long seed) { this.seed = seed; }

    public void send(int action, int type, int... data){
        emitter.send(action, type, data);
    }
}
