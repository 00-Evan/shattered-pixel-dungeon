package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Net {
    private Socket socket;

    public Net(){

    }

    public void toggle() throws URISyntaxException {
        if(this.socket == null || !socket.connected()) {
            this.connect();
        }else {
            if(this.socket.connected()) this.socket.disconnect();
        }
    }

    public void connect() throws URISyntaxException {
        this.socket = IO.socket("http://127.0.0.1:5500");
        this.socket.connect();
    }

    public void disconnect(){
        this.socket.disconnect();
    }

    public Boolean connected() {
        return socket != null && this.socket.connected();
    }

}
