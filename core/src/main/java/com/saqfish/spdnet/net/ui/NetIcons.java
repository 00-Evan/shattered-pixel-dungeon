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

import com.saqfish.spdnet.Assets;
import com.watabou.noosa.Image;

public enum NetIcons {
    GLOBE,
    ALERT,
    NEWS,
    PLAYERS,
    CHAT;


    public Image get() {
        return get( this );
    }

    public static Image get( NetIcons type ) {
        Image icon = new Image(Assets.Interfaces.NETICONS);

        switch (type) {
            case GLOBE:
                icon.frame(icon.texture.uvRect(0, 0, 15, 16));
                break;
            case ALERT:
                icon.frame( icon.texture.uvRect( 16, 0, 31, 16 ) );
                break;
            case NEWS:
                icon.frame( icon.texture.uvRect( 32, 0, 47, 16 ) );
                break;
            case PLAYERS:
                icon.frame( icon.texture.uvRect( 48, 0, 63, 16 ) );
                break;
            case CHAT:
                icon.frame(icon.texture.uvRect(0, 15, 15, 32));
        }
        return icon;
    }
}