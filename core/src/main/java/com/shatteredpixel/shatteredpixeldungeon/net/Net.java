package com.shatteredpixel.shatteredpixeldungeon.net;

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
        Emitter.Listener onConnected = args -> {
        };

        Emitter.Listener onDisconnected = args -> {
        };

        Emitter.Listener onConnectionError = args -> {
            EngineIOException e = (EngineIOException) args[0];
            error(e.getMessage());
            disconnect();
        };

        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
        socket.on(Socket.EVENT_CONNECT, onConnected);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnected);
    }

    public void endEvents(){
        socket.off();
        System.out.println("Message listening? "+ socket.hasListeners("message"));
        System.out.println("Motd listening? "+ socket.hasListeners("motd"));
        System.out.println("Socket.EVENT_CONNECT_ERROR listening? "+ socket.hasListeners(Socket.EVENT_CONNECT_ERROR));
        System.out.println("Socket.EVENT_CONNECT listening? "+ socket.hasListeners(Socket.EVENT_CONNECT));
        System.out.println("Socket.EVENT_DISCONNECT listening? "+ socket.hasListeners(Socket.EVENT_DISCONNECT));
        System.out.println("Net Events ended");
    }

    public void session(URI address){
        IO.Options options = IO.Options.builder()
                .setForceNew(true)
                .setReconnection(false)
                .build();
        socket = IO.socket(address, options);
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
        socket.disconnect();
    }

    public Socket socket(){
        return this.socket;
    }

    public Boolean connected() {
        return socket != null && socket.connected();
    }

    public void die(){
        System.out.println("Killing net");
        if (socket != null) {
            if(socket.connected()) disconnect();
            endEvents();
            socket = null;
        }
        emitters = null;
        System.out.println("Socket null? "+ (socket == null));
        System.out.println("Emitters null? "+ (emitters == null));
    }

    public long seed() { return this.seed; }
    public void seed(long seed) { this.seed = seed; }
}
