package com.saqfish.spdnet.net.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saqfish.spdnet.net.events.Events;
import com.saqfish.spdnet.net.events.Receive;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.watabou.noosa.Game;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class PlayerListButton extends BlueButton {
    public PlayerListButton(){
        super("Players");
    }
    @Override
    protected void onClick() {
        super.onClick();
        if (net().connected()) {
            net().sender().sendPlayerListRequest();
        }else{
            NetWindow.error("Not connected", "You must connect before viewing players");
            return;
        }
        net().socket().once(Events.PLAYERLISTREQUEST, args -> {
            String data = (String) args[0];
            try {
                final Receive.PlayerList pl = net().mapper().readValue(data, Receive.PlayerList.class);
                Game.runOnRenderThread(() -> NetWindow.showPlayerList(pl));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
