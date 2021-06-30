package com.shatteredpixel.shatteredpixeldungeon.net;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Net {
    private Socket socket;
    private Emitters emitters = null;
    private String seed;

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

    public void session(URI address){
            socket = IO.socket(address);
    }

    public URI uri(){
        return Settings.uri();
    }

    public void toggle() {
        if(socket != null && !socket.connected())
            connect();
        else
            disconnect();
    }

    public void connect() {
        socket.connect();
        if(emitters == null) emitters = new Emitters();
    }

    public void disconnect(){
        socket.disconnect();
        if(emitters != null) {
            emitters.cancelAll();
            emitters = null;
        }
    }

    public Socket socket(){
        return this.socket;
    }

    public Boolean connected() {
        return socket != null && socket.connected();
    }

    public String seed() { return this.seed; }
    public void seed(String seed) { this.seed = seed; }
}
