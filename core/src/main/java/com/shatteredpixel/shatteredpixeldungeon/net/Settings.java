package com.shatteredpixel.shatteredpixeldungeon.net;

import com.watabou.utils.GameSettings;

import java.net.URI;
import java.net.URISyntaxException;

public class Settings extends GameSettings {
    public static final String KEY_ADDRESS	= "net_key";
    public static final String KEY_PORT	= "net_port";

    public static void address( String value ) {
        put( KEY_ADDRESS, value );
    }

    public static String address() {
        return getString( KEY_ADDRESS, "127.0.0.1" );
    }

    public static void port( int value ) {
        put( KEY_PORT, value );
    }

    public static int port() {
        return getInt( KEY_PORT, 5500);
    }

    public static URI uri(Boolean https) {
        String address = address();
        int port = port();
        try {
            return new URI(https ? "https": "http", null, address, port, null, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
