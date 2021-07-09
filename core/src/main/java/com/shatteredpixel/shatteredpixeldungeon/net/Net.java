package com.shatteredpixel.shatteredpixeldungeon.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watabou.utils.DeviceCompat;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.EngineIOException;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.error;

public class Net {
    private Socket socket;
    private Handler handler = null;
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
        Settings.port(5800);
        Settings.auth_key(key);
        session(Settings.uri());
    }

    public Net(){
        Settings.address("127.0.0.1");
        Settings.port(5800);
        Settings.auth_key(DeviceCompat.isDebug() ? "debug": "empty");
        session(Settings.uri());
    }

    public void session(URI url){
        IO.Options options = IO.Options.builder()
                .setForceNew(true)
                .setReconnection(false)
                .build();
        socket = IO.socket(url, options);
        handler = new Handler(Net.this);
        setupEvents();
    }

    public void setupEvents(){
        Emitter.Listener onConnected = args -> {
            handler.startAll();
        };

        Emitter.Listener onDisconnected = args -> {
            handler.cancelAll();
        };

        Emitter.Listener onConnectionError = args -> {
            EngineIOException e = (EngineIOException) args[0];
            error(e.getMessage());
            handler.cancelAll();
            disconnect();
        };

        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.on(Socket.EVENT_CONNECT, onConnected);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnected);
    }

    public void endEvents(){
        socket.off();
    }

    public void connect() {
        socket.connect();
    }

    public void disconnect(){
        socket.disconnect();
    }

    public void toggle() {
        if(!socket.connected())
            connect();
        else
            disconnect();
    }

    public void die(){
        if (socket != null) {
            if(socket.connected()) disconnect();
            endEvents();
            socket = null;
        }
        handler = null;
    }

    public void send(String event, int type, String data){
        handler.send(event, type, data);
    }

    public void send(int action, int type, int... data){
        handler.send(action, type, data);
    }

    public long seed() { return this.seed; }

    public void seed(long seed) { this.seed = seed; }

    public Socket socket(){
        return this.socket;
    }

    public Boolean connected() {
        return socket != null && socket.connected();
    }

    public URI uri(){
        return Settings.uri();
    }

    public ObjectMapper mapper() { return handler.mapper();}
}
