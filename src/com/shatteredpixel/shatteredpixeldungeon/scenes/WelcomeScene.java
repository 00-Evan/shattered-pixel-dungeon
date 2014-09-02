//TODO: update this class with relevant info as new versions come out.
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import android.content.Intent;
import android.net.Uri;

import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WelcomeScene extends PixelScene {

    private static final String TTL_Welcome = "Welcome!";

    private static final String TTL_LastVer = "Updated To v0.2.0";

    private static final String TTL_Future = "Wait What?";

    private static final String TXT_Welcome =
            "Shattered Pixel Dungeon is a rework/expansion of Watabou's Pixel Dungeon.\n\n"+
            "The goal is to enhance the game by improving existing content and adding tonnes of new stuff!\n\n"+
            "Shattered Pixel Dungeon is being constantly updated, so expect more new content soon!\n\n"+
            "Happy Dungeoneering!";

    private static final String TXT_LastVer =
            "Hello early adopter, thank you so much for giving Shattered PD a try in its early stages!\n\n"+
            "This update completely overhauls rings, and as such any existing saves will have their rings removed.\n\n"+
            "If you have a game in progress with rings that you'd rather not lose, please revert to v0.1.1a.\n\n"+
            "You can simply reinstall the 0.1.1a APK from the button on the right, your saves will not be affected.";

    private static final String TXT_Future =
            "It seems that your current saves are from a future version of Shatterd Pixel Dungeon.\n\n"+
            "If haven't been reverting to previous versions, please send a bug report as this shouldn't happen.\n\n"+
            "Regardless, tread with caution! Your saves may contain things which don't exist in this version, "+
            "this could cause some very weird errors to occur.";


    private static final String LNK = "goo.gl/CwrJd6";

    @Override
    public void create() {
        super.create();

        boolean fromAlpha = false;

        int gameversion = ShatteredPixelDungeon.version();

        BitmapTextMultiline text;
        BitmapTextMultiline title;

        if (gameversion == 0){

            GamesInProgress.Info warrior = GamesInProgress.check( HeroClass.values()[0] );
            GamesInProgress.Info mage = GamesInProgress.check( HeroClass.values()[1] );
            GamesInProgress.Info rouge = GamesInProgress.check( HeroClass.values()[2] );
            GamesInProgress.Info huntress = GamesInProgress.check( HeroClass.values()[3] );

            if (warrior != null || mage != null || rouge != null || huntress != null){
                text = createMultiline( TXT_LastVer, 8 );
                title = createMultiline( TTL_LastVer, 12 );
                fromAlpha = true;
            } else {
                text = createMultiline( TXT_Welcome, 8 );
                title = createMultiline( TTL_Welcome, 16 );
            }
        } else if (gameversion <= Game.versionCode) {
            text = createMultiline( TXT_LastVer, 8 );
            title = createMultiline( TTL_LastVer, 12 );
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

        RedButton okay = new RedButton("Okay") {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.version(Game.versionCode);
                Game.switchScene(TitleScene.class);
            }
        };
        if (fromAlpha) {
            okay.setRect(text.x, text.y + text.height() + 5, 55, 18);
            add(okay);

            RedButton revert = new RedButton("Revert") {
                @Override
                protected void onClick() {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + LNK));
                    Game.instance.startActivity(intent);
                }
            };
            revert.setRect(text.x + 65, text.y + text.height() + 5, 55, 18);
            add(revert);
        } else {
            okay.setRect(text.x, text.y + text.height() + 5, 120, 18);
            add(okay);
        }

        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        //ShatteredPixelDungeon.version(Game.versionCode);
        //Game.switchScene( TitleScene.class );
    }
}


