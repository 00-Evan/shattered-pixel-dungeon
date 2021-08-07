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

package com.saqfish.spdnet.net;

import com.watabou.utils.GameSettings;

import java.net.URI;
import java.net.URISyntaxException;

import static com.saqfish.spdnet.net.Net.DEFAULT_HOST;
import static com.saqfish.spdnet.net.Net.DEFAULT_KEY;
import static com.saqfish.spdnet.net.Net.DEFAULT_PORT;
import static com.saqfish.spdnet.net.Net.DEFAULT_SCHEME;

public class Settings extends GameSettings {
    public static final String KEY_AUTHORITY = "net_scheme";
    public static final String KEY_ADDRESS = "net_address";
    public static final String KEY_PORT	= "net_port";
    public static final String KEY_AUTH_KEY	= "net_auth_key";

    public static void scheme( String value ) {
        put( KEY_AUTHORITY, value );
    }
    public static String scheme() {
        return getString(KEY_AUTHORITY, DEFAULT_SCHEME);
    }

    public static void address( String value ) {
        put( KEY_ADDRESS, value );
    }
    public static String address() { return getString( KEY_ADDRESS, DEFAULT_HOST); }

    public static void port( int value ) {
        put( KEY_PORT, value );
    }
    public static int port() {
        return getInt( KEY_PORT, DEFAULT_PORT);
    }

    public static void auth_key( String value ) { put( KEY_AUTH_KEY, value ); }
    public static String auth_key() { return getString(KEY_AUTH_KEY, DEFAULT_KEY); }

    public static void setDefault(){
        scheme(DEFAULT_SCHEME);
        address(DEFAULT_HOST);
        port(DEFAULT_PORT);
        auth_key(DEFAULT_KEY);
    }

    public static URI uri() {
        String scheme = scheme();
        String address = address();
        int port = port();
        try {
            return new URI(scheme, null, address, port, null, null, null);
        } catch (Exception ignored) {

        }
        return URI.create(DEFAULT_SCHEME+"://"+DEFAULT_HOST+":"+DEFAULT_PORT);
    }


}
