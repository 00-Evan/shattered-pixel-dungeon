/*
 * Pixel Dungeon
 * Copyright (C) 2021 saqfish
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.saqfish.spdnet.net.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saqfish.spdnet.net.events.Events;
import com.saqfish.spdnet.net.events.Receive;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.ui.Tag;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class NetIndicator extends Tag {

    public static final int COLOR	= 0xFF4C4C;

    private final int PLAYER_LIST = 0;
    private final int CHAT = 1;

    private Image icon;

    public NetIndicator() {
        super( 0xFF4C4C );
        setSize( icon.width()+6, icon.height()+6 );
        visible = true;
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        icon = NetIcons.get(NetIcons.PLAYERS);
        icon.scale.set(0.72f);
        add( icon );
    }

    @Override
    protected void layout() {
        super.layout();
        icon.x = right()-icon.width()-2;
        icon.y = y+3;
    }

    @Override
    public void update() {
        super.update();
        bg.hardlight(net().connected() ? 0x52846b: 0x845252);
        setIcon(net().reciever().newMessage() ? CHAT: PLAYER_LIST);
    }

    @Override
    protected void onClick() {
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

    private void setIcon(int type){
        switch (type) {
            case PLAYER_LIST:
                icon.copy(NetIcons.get(NetIcons.PLAYERS));
                icon.scale.set(0.72f);
                break;
            case CHAT:
                icon.copy(NetIcons.get(NetIcons.CHAT));
                icon.scale.set(0.62f);
                break;
        }
    }
}
