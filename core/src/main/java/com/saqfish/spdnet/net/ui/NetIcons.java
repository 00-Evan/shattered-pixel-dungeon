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