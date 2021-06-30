package com.shatteredpixel.shatteredpixeldungeon.net;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.windows.WndNetSettings;
import com.shatteredpixel.shatteredpixeldungeon.net.windows.WndServerInfo;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.net.windows.WndMessage;
import com.watabou.noosa.Image;


public class Util {

    public static void error(String message){
        message(Icons.get(Icons.ALERT), "Connection Error", message);
    }

    public static void message(Image i, String title, String message){
        ShatteredPixelDungeon.scene().add(new WndMessage(i, title, message));
    }

    public static void message(String title, String message){
        ShatteredPixelDungeon.scene().add(new WndMessage(Icons.get(Icons.NET), title, message));
    }
    public static void message(String message){
        ShatteredPixelDungeon.scene().add(new WndMessage(Icons.get(Icons.NET), "Server Message", message));
    }

    public static void showSettings(){
        ShatteredPixelDungeon.scene().add(new WndNetSettings());
    }

    public static void showServerInfo(){
        ShatteredPixelDungeon.scene().add(new WndServerInfo());
    }

    public static void motd(String message){
        message(Icons.get(Icons.NEWS), "Motd", message);
    }
}
