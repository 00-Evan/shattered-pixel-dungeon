package com.saqfish.spdnet.net;
import java.net.URI;

import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.net.windows.WndServerInfo;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.EngineIOException;

import static java.util.Collections.singletonMap;

public class Net {
    public static String DEFAULT_SCHEME = "http";
    public static String DEFAULT_HOST = "saqfish.com";
    public static int DEFAULT_PORT = 5800;
    public static String DEFAULT_KEY = "debug";

    private Socket socket;
    private Handler handler = null;
    private long seed;

    private NetWindow w;

    public Net(String address, String key){
        URI url = URI.create(address);
        Settings.scheme(url.getScheme());
        Settings.address(url.getHost());
        Settings.port(url.getPort());
        Settings.auth_key(key);
        session(Settings.uri(), key);
    }

    public Net(String key){
        Settings.auth_key(key);
        session(Settings.uri(), key);
    }

    public Net(){
        session(Settings.uri(), Settings.auth_key());
    }

    public void reset(){
        session(Settings.uri(), Settings.auth_key());
    }

    public void session(URI url, String key){
        IO.Options options = IO.Options.builder()
                .setAuth(singletonMap("token", key))
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
            if(w != null) {
                Game.runOnRenderThread( () -> w.destroy());
            }
        };

        Emitter.Listener onDisconnected = args -> {
            handler.cancelAll();
        };

        Emitter.Listener onConnectionError = args -> {
            try {
                JSONObject json = (JSONObject)args[0];
                Error e = mapper().readValue(json.toString(), Error.class);
                NetWindow.error(e.message);
            }catch(Exception e){
                e.printStackTrace();
                NetWindow.error("Connection could not be established!");
            }
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

    public void toggle(WndServerInfo w) {
        this.w = w;
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

    public void sendMessage(int type, String data) {handler.sendMessage(type, data);};
    public void sendAction(int type, int... data) { handler.sendAction(type,data); }
    public void sendAction(int type, String data) { handler.sendAction(type,data); }
    public void sendPlayerListRequest() {handler.sendPlayerListRequest();};

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
