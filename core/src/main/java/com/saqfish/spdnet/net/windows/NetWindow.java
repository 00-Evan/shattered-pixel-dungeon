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

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.net.Settings;
import com.saqfish.spdnet.net.events.Receive;
import com.saqfish.spdnet.net.ui.NetIcons;
import com.saqfish.spdnet.net.ui.UI;
import com.saqfish.spdnet.ui.Window;
import com.saqfish.spdnet.ui.WndTextInput;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.PlatformSupport;

import org.json.JSONObject;

public class NetWindow extends Window {
    public NetWindow(int width, int height){
        super(width, height, UI.get(UI.Type.WINDOW));
    }
    public NetWindow(){
        super(0, 0, UI.get(UI.Type.WINDOW));
    }

    public static void message(Image i, String title, String message){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndMessage(i, title, message)));
    }

    public static void message(String title, String message){
        message(NetIcons.get(NetIcons.GLOBE), title, message);
    }

    public static void message(String message){
        message(NetIcons.get(NetIcons.GLOBE), "Server Message", message);
    }

    public static void error(String message){
        message(NetIcons.get(NetIcons.ALERT), "Connection Error", message);
    }

    public static void error(String title, String message){
        message(NetIcons.get(NetIcons.ALERT), title, message);
    }

    public static void runWindow(Window w){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(w));
    }

    public static void showSettings(){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndNetSettings()));
    }

    public static void showChat(){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndChat()));
    }

    public static void showServerInfo(){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndServerInfo()));
    }

    public static void showKeyInput(){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndTextInput("Enter key", Settings.auth_key(), 30, false, "Set", "Cancel"){
            @Override
            public void onSelect(boolean positive, String text) {
                if(positive){
                    Settings.auth_key(text);
                    net().reset();
                }
            }
        }));
    }

    public static void motd(String motd, long seed){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndMotd(motd,seed)));
    }

    public static void showPlayerList(Receive.PlayerList p){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndPlayerList(p)));
    }

    public static void showRanking(JSONObject recordsData){
        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndNetRanking(recordsData)));
    }
}
