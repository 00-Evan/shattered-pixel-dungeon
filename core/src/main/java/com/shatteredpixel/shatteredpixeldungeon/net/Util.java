package com.shatteredpixel.shatteredpixeldungeon.net;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.windows.WndNetSettings;
import com.shatteredpixel.shatteredpixeldungeon.net.windows.WndServerInfo;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;


public class Util {

    public static void error(String message){
        ShatteredPixelDungeon.scene().add(new WndError(message));
    }

    public static void message(String message){
        ShatteredPixelDungeon.scene().add(new WndMessage(message));
    }

    public static void showSettings(){
        ShatteredPixelDungeon.scene().add(new WndNetSettings());
    }

    public static void showServerInfo(){
        ShatteredPixelDungeon.scene().add(new WndServerInfo());
    }

    public static void motd(String message){
        ShatteredPixelDungeon.scene().add(new WndOptions(Icons.get(Icons.NEWS), "Motd", message, "Close"));
    }
}
