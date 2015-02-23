package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class WelcomeScene extends PixelScene {

    private static final String TTL_Welcome = "Welcome!";

    private static final String TTL_LastVer = "v0.2.3 Update!";

    private static final String TTL_SameVer = "v0.2.3 Patched!";

    private static final String TTL_Future = "Wait What?";

    private static final String TXT_Welcome =
            "Shattered Pixel Dungeon is a rework/expansion of Watabou's Pixel Dungeon.\n\n"+
            "The goal is to enhance the game by improving existing content and adding tonnes of new stuff!\n\n"+
            "Shattered Pixel Dungeon is being constantly updated, so expect more new content soon!\n\n"+
            "Happy Dungeoneering!";

    private static final String TXT_LastVer =
            "The Artifact Booster Pack! This update brings a variety of tweaks, improvements, and new content!\n\n" +
            "-4 new Artifacts!\n-Artifacts are now unique!\n-Artifacts can now be cursed!\n-Artifact balance tweaks\n\n" +
            "-Cloak of Shadows is now Rogue Exclusive\n-Freerunner now runs MUCH faster while invisible\n\n" +
            "-Dangerous blandfruit effects reworked, now more positive.\n\n-Ring of Force buffed, Ring of Evasion reworked\n\n" +
            "-Second QuickSlot!\n\n-Rankings Page Reworked\n\n-You can now donate to support the game, press the donate button for more info!";

    private static final String TXT_SameVer =
            "v0.2.3a and v0.2.3b fix some crash bugs players were experiencing.\n\n" +
            "v0.2.3c fixes numerous issues, and gives some buffs to two of the new artifacts.\n\n" +
            "v0.2.3d fixes numerous bugs, both from this release and older ones.\n\n" +
            "v0.2.3e fixes some bugs, and adds a couple new features!\n-Second QuickSlot!\n-night mode is now a level feel, time of day no longer affects gameplay.\n\n" +
            "v0.2.3f fixes more bugs. Next update is 0.2.4, which will incorporate the new 1.7.5 source, degredation will not be included.";

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

        BitmapTextMultiline title;
        BitmapTextMultiline text;

        if (gameversion == 0) {

            text = createMultiline(TXT_Welcome, 8);
            title = createMultiline(TTL_Welcome, 16);

        } else if (gameversion < 26) {

            text = createMultiline(TXT_LastVer, 6 );
            title = createMultiline(TTL_LastVer, 12 );

        } else if (gameversion <= Game.versionCode) {

            text = createMultiline(TXT_SameVer, 6 );
            title = createMultiline(TTL_SameVer, 12 );

        } else {

            text = createMultiline( TXT_Future, 8 );
            title = createMultiline( TTL_Future, 16 );

        }

        int w = Camera.main.width;
        int h = Camera.main.height;

        int pw = w - 10;
        int ph = h - 50;

        title.maxWidth = pw;
        title.measure();
        title.hardlight(Window.SHPX_COLOR);

        title.x = align( (w - title.width()) / 2 );
        title.y = align( 8 );
        add( title );

        NinePatch panel = Chrome.get(Chrome.Type.WINDOW);
        panel.size( pw, ph );
        panel.x = (w - pw) / 2;
        panel.y = (h - ph) / 2;
        add( panel );

        ScrollPane list = new ScrollPane( new Component() );
        add( list );
        list.setRect(
                panel.x + panel.marginLeft(),
                panel.y + panel.marginTop(),
                panel.innerWidth(),
                panel.innerHeight());
        list.scrollTo( 0, 0 );

        Component content = list.content();
        content.clear();

        text.maxWidth = (int) panel.innerWidth();
        text.measure();

        content.add(text);

        content.setSize( panel.innerWidth(), text.height() );

        RedButton okay = new RedButton("Okay!") {
            @Override
            protected void onClick() {


	            if (gameversion <= 32){
		            //removes all bags bought badge from pre-0.2.4 saves.
		            Badges.disown(Badges.Badge.ALL_BAGS_BOUGHT);
		            Badges.saveGlobal();

		            //imports new ranking data for pre-0.2.3 saves.
		            if (gameversion <= 29){
			            Rankings.INSTANCE.load();
			            Rankings.INSTANCE.save();
		            }
	            }

                ShatteredPixelDungeon.version(Game.versionCode);
                Game.switchScene(TitleScene.class);
            }
        };

        /*
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

        okay.setRect((w - pw) / 2, h - 22, pw, 18);
        add(okay);

        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        fadeIn();
    }
}


