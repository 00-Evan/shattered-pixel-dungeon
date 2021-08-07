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

package com.saqfish.spdnet.net.windows;

import com.saqfish.spdnet.net.Settings;
import com.saqfish.spdnet.net.ui.BlueButton;
import com.saqfish.spdnet.net.ui.LabeledText;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.watabou.noosa.ColorBlock;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class WndServerInfo extends NetWindow {
    private static final int WIDTH_P	    = 122;
    private static final int WIDTH_L	    = 223;

    private static final float GAP          = 2;

    RenderedTextBlock title;
    ColorBlock sep1;
    LabeledText type;
    LabeledText host;
    LabeledText port;
    LabeledText status;
    BlueButton connectBtn;

    WndServerInfo self = this;

    public WndServerInfo() {
        super();

        int height, y = 0;

        int maxWidth = PixelScene.landscape() ? WIDTH_L : WIDTH_P;
        int maxBtnHeight = PixelScene.landscape() ? 18: 14;
        int maxTitleHeight =  PixelScene.landscape() ? 12: 9;
        String maxTitleText = PixelScene.landscape() ? "Server Info": "Server";

        title = PixelScene.renderTextBlock(maxTitleText, maxTitleHeight);
        title.hardlight(TITLE_COLOR);
        add(title);

        sep1 = new ColorBlock(1, 1, 0xFF000000);
        add(sep1);

        type = new LabeledText("Type", Settings.uri().getScheme());
        add(type);

        host = new LabeledText("Host", Settings.uri().getHost());
        add(host);

        port = new LabeledText("Port", String.valueOf(Settings.uri().getPort()));
        add(port);



        connectBtn = new BlueButton("Connect") {
            @Override
            public synchronized void update() {
                super.update();
                text.text(net().connected() ? "Disconnect" : "Connect");
                setSize(net().connected() ? 50 : 40, maxBtnHeight);
                setPos(maxWidth - connectBtn.width(), 0);
            }

            @Override
            protected void onClick() {
                super.onClick();
                net().toggle(self);
            }
        };
        add(connectBtn);
        status = new LabeledText("Status", net().connected() ? "Connected" : "Disconnected") {
            @Override
            public synchronized void update() {
                super.update();
                text().text(net().connected() ? "Connected" : "Disconnected");
                text().hardlight(net().connected() ? 0x00FF00 : 0xFF0000);
            }
        };
        add(status);


        float bottom = y;

        title.setPos(GAP, (PixelScene.landscape() ? 1: 2));
        connectBtn.setSize(40, maxBtnHeight);
        connectBtn.setPos(maxWidth- connectBtn.width(), 0);

        sep1.size(maxWidth, 1);
        sep1.y = connectBtn.bottom() + GAP;

        bottom = sep1.y + 1;

        float right = 0;

        type.setPos(right, bottom + GAP);
        bottom = type.bottom() + GAP;

        host.setPos(right + host.width(), bottom + GAP);
        bottom = host.bottom() + GAP;

        port.setPos(right + port.width(), bottom + GAP);
        bottom = port.bottom() + GAP;

        right = 0;

        status.setPos(right, bottom + GAP);

        height = (int) (status.bottom() + 4 * GAP);

       resize(maxWidth, height);
    }


}


