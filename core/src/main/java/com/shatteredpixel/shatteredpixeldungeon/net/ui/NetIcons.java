package com.shatteredpixel.shatteredpixeldungeon.net.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;

public enum NetIcons {
    GLOBE,
    ALERT,
    NEWS,
    PLAYERS;

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
        }
        return icon;
    }
}