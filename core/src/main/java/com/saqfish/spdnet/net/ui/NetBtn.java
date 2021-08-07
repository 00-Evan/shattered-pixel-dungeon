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

import com.saqfish.spdnet.Chrome;
import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.net.Settings;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.ui.StyledButton;
import com.watabou.utils.PlatformSupport;

import java.net.URI;
import java.net.URISyntaxException;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;
import static com.watabou.noosa.Game.platform;

public class NetBtn extends StyledButton {
    public static final int HEIGHT = 24;
    public static final int MIN_WIDTH = 30;

    private ShatteredPixelDungeon instance = ((ShatteredPixelDungeon) ShatteredPixelDungeon.instance);

    public NetBtn() {
        super(Chrome.Type.GREY_BUTTON_TR, "");
        icon(NetIcons.get(NetIcons.GLOBE));
    }

    @Override
    public void update() {
        super.update();
        icon.brightness(instance.net.connected() ? 0.8f : 0.2f );
    }

    @Override
    protected void onClick() {
        super.onClick();
        NetWindow.showServerInfo();
    }

    @Override
    protected boolean onLongClick() {
        platform.promptTextInput("Enter key", Settings.auth_key(), 30, false, "Set", "Cancel", new PlatformSupport.TextCallback() {
            @Override
            public void onSelect(boolean positive, String text) {
                if (positive) {
                    Settings.auth_key(text);
                    net().reset();
                }
            }
        });
        return true;
    }
}
