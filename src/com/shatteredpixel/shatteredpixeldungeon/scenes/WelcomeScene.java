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
            "This update brings improvements from the Pixel Dungeon source, and various small tweaks.\n\n" +
            "There are now two new plants, look out for dreamfoil and stormvine! Potions of levitation has been buffed as well.\n\n" +
            "The heroes remains system has been reworked to be more helpful and less exploitable.\n\n" +
            "Weightstones have been added, but they are reworked from the base game.\n\n" +
            "There are various other small tweaks and improvements, for example, falling items will now appear on the next depth!";

    private static final String TXT_SameVer =
            "v0.2.2a addresses an issue players were having when they tried to start a new game, " +
            "corrects a few small inconsistencies with challenges, and makes a few small improvements to " +
            "level generation.\n\n" +
            "v0.2.2b addresses issues players were having on runs with challenges enabled " +
            "and fixes a variety of other smaller bugs as well.\n\n" +
            "Happy Dungeoneering!";

    private static final String TXT_Future =
            "It seems that your current saves are from a future version of Shattered Pixel Dungeon!\n\n"+
            "Either you're messing around with older versions of the app, or something has gone buggy.\n\n"+
            "Regardless, tread with caution! Your saves may contain things which don't exist in this version, "+
            "this could cause some very weird errors to occur.";

    private static final String LNK = "https://play.google.com/store/apps/details?id=com.shatteredpixel.shatteredpixeldungeon";

    @Override
    public void create() {
        super.create();

        final int gameversion = ShatteredPixelDungeon.version();

        BitmapTextMultiline text;
        BitmapTextMultiline title;

        if (gameversion == 0) {

            text = createMultiline(TXT_Welcome, 8);
            title = createMultiline(TTL_Welcome, 16);

        } else if (gameversion < 13) {

            text = createMultiline(TXT_LastVer, 6 );
            title = createMultiline(TTL_LastVer, 12 );

        } else if (gameversion <= Game.versionCode) {

            text = createMultiline(TXT_SameVer, 6 );
            title = createMultiline(TTL_SameVer, 12 );

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


