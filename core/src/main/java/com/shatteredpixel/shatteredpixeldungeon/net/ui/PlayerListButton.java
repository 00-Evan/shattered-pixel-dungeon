package com.shatteredpixel.shatteredpixeldungeon.net.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shatteredpixel.shatteredpixeldungeon.net.Types;
import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.playerlist.PlayerList;
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
            net().send(Types.Recieve.MESSAGE, Types.Send.PLAYERLIISTREQUEST, null);
        }else{
            error("You are not connected!");
            return;
        }
        net().socket().once(Types.Recieve.PLAYERLIST, args -> {
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
