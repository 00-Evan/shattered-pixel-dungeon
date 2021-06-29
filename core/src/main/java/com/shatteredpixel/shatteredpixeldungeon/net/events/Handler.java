package com.shatteredpixel.shatteredpixeldungeon.net.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shatteredpixel.shatteredpixeldungeon.net.Settings;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Auth;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.Message;
import com.shatteredpixel.shatteredpixeldungeon.net.events.message.MessageTypes;

import io.socket.client.Socket;

import static com.shatteredpixel.shatteredpixeldungeon.net.Util.message;
import static com.shatteredpixel.shatteredpixeldungeon.net.Util.motd;

public class Handler {
    private final ObjectMapper mapper;
    private Socket socket;

    public Handler(Socket s) {
        mapper = new ObjectMapper();
        this.socket = s;
    }

    public void handleMessage(int type, String json){
        Message message = null;
        System.out.println("Message -> "+json);
        try{
            switch (type) {
                case MessageTypes.Recieve.AUTH:
                    Auth auth = new Auth(Settings.auth_key());
                    String j = mapper.writeValueAsString(auth);
                    socket.emit("message", MessageTypes.Send.AUTH, j);
                    break;
                case MessageTypes.Recieve.MOTD:
                    message = mapper.readValue(json, Message.class);
                    motd(message.message);
                    break;
                case MessageTypes.Recieve.MESSAGE:
                    message = mapper.readValue(json, Message.class);
                    message(message.message);
                    break;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
