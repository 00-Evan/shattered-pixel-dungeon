package com.shatteredpixel.shatteredicepixeldungeon.scenes;

import com.shatteredpixel.shatteredicepixeldungeon.Badges;
import com.shatteredpixel.shatteredicepixeldungeon.Chrome;
import com.shatteredpixel.shatteredicepixeldungeon.Rankings;
import com.shatteredpixel.shatteredicepixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredicepixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredicepixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class WelcomeScene extends PixelScene {

    private static final String TTL_Welcome = "Welcome!";

    private static final String TTL_Update = "v0.2.4: PD 1.7.5 and Some Extras!";

    private static final String TTL_Future = "Wait What?";

    private static final String TXT_Welcome =
            "Shattered Pixel Dungeon is a rework/expansion of Watabou's Pixel Dungeon.\n\n"+
            "The goal is to enhance the game by improving existing content and adding tonnes of new stuff!\n\n"+
            "Shattered Pixel Dungeon is being constantly updated, so expect more new content soon!\n\n"+
            "Happy Dungeoneering!";

    private static final String TXT_Update =
            "v0.2.4d:\n" +
                    "-Bugfixes and general improvements.\n" +
                    "-Added Merchant's Beacon.\n" +
                    "-Added initials for IDed scrolls/potions.\n" +
                    "-Lloyd's beacon now has a new sprite.\n" +
                    "\n" +
                    "v0.2.4a, v0.2.4b, v0.2.4c:\n" +
                    "-Various Bug fixes and small tweaks.\n" +
                    "\n" +
                    "V0.2.4:\n" +
                    "v1.7.5 Source Implemented, with exceptions:\n" +
                    "\n" +
                    "- Degredation not implemented.\n" +
                    "\n" +
                    "- Badge syncing not implemented.\n" +
                    "\n" +
                    "- Scroll of Weapon Upgrade renamed to Magical Infusion, works on armor.\n" +
                    "\n" +
                    "- Scroll of Enchantment not implemented, Arcane stylus has not been removed.\n" +
                    "\n" +
                    "- Honey pots now shatter in a new item: shattered honeypot. A bee will defend its shattered pot to the death against anything that gets near.\n" +
                    "\n" +
                    "- Bombs have been reworked/nerfed: they explode after a delay, no longer stun, deal more damage at the center of the blast, affect the world (destroy items, blow up other bombs).\n" +
                    "\n" +
                    "\n" +
                    "In addition, this update features the following changes:\n" +
                    "\n" +
                    "- The huntress has been buffed: starts with Potion of Mind Vision identified, now benefits from strength on melee attacks, and has a chance to reclaim a single used ranged weapon from each defeated enemy. \n" +
                    "\n" +
                    "- A new container: The Potion Bandolier! Potions can now shatter from frost, but the bandolier can protect them.\n" +
                    "\n" +
                    "- Shops now stock a much greater variety of items, some item prices have been rebalanced.\n" +
                    "\n" +
                    "- Going down stairs no longer increases hunger, going up still does.\n" +
                    "\n" +
                    "-Many, many bugfixes.\n" +
                    "-Some UI improvements.\n" +
                    "-Ingame audio quality improved.\n" +
                    "-Unstable spellbook buffed.\n" +
                    "-Psionic blasts deal less self-damage.\n" +
                    "-Potions of liquid flame affect a 3x3 grid.";

    private static final String TXT_Future =
            "It seems that your current saves are from a future version of Shattered Pixel Dungeon!\n\n"+
            "Either you're messing around with older versions of the app, or something has gone buggy.\n\n"+
            "Regardless, tread with caution! Your saves may contain things which don't exist in this version, "+
            "this could cause some very weird errors to occur.";

    private static final String LNK = "https://play.google.com/store/apps/details?id=com.shatteredpixel.shatteredicepixeldungeon";

    @Override
    public void create() {
        super.create();

        final int gameversion = ShatteredPixelDungeon.version();

        BitmapTextMultiline title;
        BitmapTextMultiline text;

        if (gameversion == 0) {

            text = createMultiline(TXT_Welcome, 8);
            title = createMultiline(TTL_Welcome, 16);

        } else if (gameversion <= Game.versionCode) {

            text = createMultiline(TXT_Update, 6 );
            title = createMultiline(TTL_Update, 9 );

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


