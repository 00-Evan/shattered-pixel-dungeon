package com.shatteredpixel.shatteredpixeldungeon.net;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.error;

public class Net {
    private Socket socket;
    private Emitters emitters;
    private URI uri;

    public Net(String address, String key){
        host(URI.create(address));
        Settings.auth_key(key);
    }

    public Net(String key){
        host(Settings.uri(false));
        Settings.auth_key(key);
    }

    public Net(){
        host(URI.create("http://127.0.0.1:5500"));
        Settings.auth_key("1234");
    }

    public void host(URI address){
        try {
            socket = IO.socket(address);
            uri = address;
        }catch(Exception e){
            error(e.getMessage());
        }
    }

    public URI uri(){
        return this.uri;
    }

    public void toggle() {
        if(socket == null || !socket.connected()) {
            try {
                emitters = new Emitters(socket);
                this.connect();
            } catch (Exception e){
                error(e.getMessage());
            }
        }else {
            if(socket.connected()) socket.disconnect();
        }
    }

    public void connect() {
        socket.connect();
    }

    public void disconnect(){
        socket.disconnect();
    }

    public Boolean connected() {
        return socket != null && socket.connected();
    }

}
