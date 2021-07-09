package com.shatteredpixel.shatteredpixeldungeon.net.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shatteredpixel.shatteredpixeldungeon.net.events.Events;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.playerlist.PlayerList;
import com.shatteredpixel.shatteredpixeldungeon.net.events.send.Send;
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;

import static com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon.net;
import static com.shatteredpixel.shatteredpixeldungeon.net.Util.error;
import static com.shatteredpixel.shatteredpixeldungeon.net.Util.showPlayerList;

public class PlayerListButton extends BlueButton {
    public PlayerListButton(){
        super("Players");
    }
    @Override
    protected void onClick() {
        super.onClick();
        if (net().connected()) {
            net().send(Events.MESSAGE, Send.PLAYERLIISTREQUEST, null);
        }else{
            error("Not connected", "You must connect before viewing players");
            return;
        }
        net().socket().once(Events.PLAYERLIST, args -> {
            String data = (String) args[0];
            DeviceCompat.log("PLIST", data);
            try {
                final PlayerList pl = net().mapper().readValue(data, PlayerList.class);
                Game.runOnRenderThread(() -> showPlayerList(pl));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
