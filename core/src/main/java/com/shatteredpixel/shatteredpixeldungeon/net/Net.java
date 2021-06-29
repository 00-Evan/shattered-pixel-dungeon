package com.shatteredpixel.shatteredpixeldungeon.net;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.error;

public class Net {
    private Socket socket;
    private Emitters emitters;

    public Net(){
        host(Settings.uri(false).toString());
    }

    public void host(String address){
        System.out.println(address);
        try {
            socket = IO.socket(address);
        }catch(Exception e){
            error(e.getMessage());
        }
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

    public void connect() throws URISyntaxException {
        socket.connect();
    }

    public void disconnect(){
        socket.disconnect();
    }

    public Boolean connected() {
        return socket != null && socket.connected();
    }

}
