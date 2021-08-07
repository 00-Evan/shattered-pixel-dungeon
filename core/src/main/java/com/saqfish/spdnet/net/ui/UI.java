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
import com.watabou.noosa.NinePatch;

public class UI {
    public enum Type {
        WINDOW,
        BUTTON
    }

    public static NinePatch get(Type type ) {
        String Asset = Assets.Interfaces.NETUI;
        switch (type) {
            case WINDOW:
                return new NinePatch(Asset, 0, 0, 20, 20, 6);
            case BUTTON:
                return new NinePatch( Asset, 20, 0, 6, 6, 2 );
            default:
                return null;
        }
    }
}
