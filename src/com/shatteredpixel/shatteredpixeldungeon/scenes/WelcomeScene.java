//TODO: update this class with relevant info as new versions come out.
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;

public class WelcomeScene extends PixelScene {

    private static final String TTL_Welcome = "Welcome!";

    private static final String TTL_LastVer = "Updated To v0.2.2";

    private static final String TTL_SameVer = "v0.2.2 Patched!";

    private static final String TTL_Future = "Wait What?";

    private static final String TXT_Welcome =
            "Shattered Pixel Dungeon is a rework/expansion of Watabou's Pixel Dungeon.\n\n"+
            "The goal is to enhance the game by improving existing content and adding tonnes of new stuff!\n\n"+
            "Shattered Pixel Dungeon is being constantly updated, so expect more new content soon!\n\n"+
            "Happy Dungeoneering!";

    private static final String TXT_LastVer =
            "This update brings improvements from the Pixel Dungeon source, and various small improvements.\n\n" +
            "There are now two new plants, look out for dreamfoil and stormvine! potion of levitation has been buffed as well.\n\n" +
            "The heroes remains system has been reworked to be more helpful and less exploitable.\n\n" +
            "Weightstones have been added, but they are reworked from the base game.\n\n" +
            "There are various other small tweaks and improvements, for example, falling items will now appear on the next depth!";

    private static final String TXT_SameVer =
            "no patches at present";

    private static final String TXT_Future =
            "It seems that your current saves are from a future version of Shattered Pixel Dungeon.\n\n"+
            "You have either been messing around with backup software, or something has gone buggy.\n\n"+
            "Regardless, tread with caution! Your saves may contain things which don't exist in this version, "+
            "this could cause some very weird errors to occur.";

    private static final String LNK = "https://play.google.com/store/apps/details?id=com.shatteredpixel.shatteredpixeldungeon";

    @Override
    public void create() {
        super.create();

        int gameversion = ShatteredPixelDungeon.version();

        BitmapTextMultiline text;
        BitmapTextMultiline title;

        if (gameversion == 0) {

            text = createMultiline(TXT_Welcome, 8);
            title = createMultiline(TTL_Welcome, 16);

        } else if (gameversion <= Game.versionCode) {

            text = createMultiline(TXT_LastVer, 6 );
            title = createMultiline(TTL_LastVer, 12 );

        } else {

            text = createMultiline( TXT_Future, 8 );
            title = createMultiline( TTL_Future, 16 );

        }


        text.maxWidth = Math.min( Camera.main.width, 120 );
        text.measure();
        add( text );

        text.x = align( (Camera.main.width - text.width()) / 2 );
        text.y = align( (Camera.main.height - text.height()) / 2 );

        title.maxWidth = Math.min( Camera.main.width-50, 120 );
        title.measure();
        title.hardlight(Window.TITLE_COLOR);
        add( title );

        title.x = align( (Camera.main.width - title.width()) / 2 );
        title.y = align( text.y - title.height() - 10 );

        RedButton okay = new RedButton("Okay!") {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.version(Game.versionCode);
                Game.switchScene(TitleScene.class);
            }
        };

        /* to be added in a later update
        okay.setRect(text.x, text.y + text.height() + 5, 55, 18);
        add(okay);

        RedButton changes = new RedButton("Changes") {
            @Override
            protected void onClick() {
                parent.add(new WndChanges());
            }
        };

        changes.setRect(text.x + 65, text.y + text.height() + 5, 55, 18);
        add(changes);*/

        okay.setRect(text.x, text.y + text.height() + 5, 120, 18);
        add(okay);


        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        fadeIn();
    }
}


