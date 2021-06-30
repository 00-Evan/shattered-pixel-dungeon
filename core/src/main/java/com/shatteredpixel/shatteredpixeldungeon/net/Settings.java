package com.shatteredpixel.shatteredpixeldungeon.net;

import com.watabou.utils.GameSettings;

import java.net.URI;
import java.net.URISyntaxException;

public class Settings extends GameSettings {
    public static final String KEY_AUTHORITY	= "net_authority";
    public static final String KEY_ADDRESS	= "net_address";
    public static final String KEY_PORT	= "net_port";
    public static final String KEY_AUTH_KEY	= "net_auth_key";

    public static void authority( String value ) {
        put( KEY_AUTHORITY, value );
    }

    public static String authority() {
        return getString(KEY_AUTHORITY, "http");
    }

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

    public static URI uri() {
        String authority = authority();
        String address = address();
        int port = port();
        try {
            return new URI(authority, null, address, port, null, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void auth_key( String value ) {
        put( KEY_AUTH_KEY, value );
    }

    public static String auth_key() {
        return getString(KEY_AUTH_KEY, "00000000");
    }
}
