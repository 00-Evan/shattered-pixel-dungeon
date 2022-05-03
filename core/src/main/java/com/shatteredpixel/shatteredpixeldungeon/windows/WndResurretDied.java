package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;

public class WndResurretDied extends Window {

    private static final int WIDTH		= 120;
    private static final int BTN_HEIGHT	= 20;
    private static final float GAP		= 2;

    public static WndResurretDied instance;
    public static Object causeOfDeath;

    public WndResurretDied(final Ankh ankhs, Object causeOfDeath ) {

        super();

        instance = this;
        WndResurretDied.causeOfDeath = causeOfDeath;

        IconTitle titlebar = new IconTitle();
        titlebar.icon( new ItemSprite(ItemSpriteSheet.DG12));
        titlebar.label( Messages.titleCase(ankhs.name()) );
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "message"), 6 );
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );

        RedButton btnYes = new RedButton( Messages.get(this, "yes") ) {
            @Override
            protected void onClick() {
                hide();
                Statistics.ankhsUsed++;
                Dungeon.deleteGame( GamesInProgress.curSlot, true );
                Game.switchScene( TitleScene.class );
            }
        };
        btnYes.setRect( 0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT );
        add( btnYes );

        resize( WIDTH, (int)btnYes.bottom() );
    }

    @Override
    public void destroy() {
        super.destroy();
        instance = null;
    }

    @Override
    public void onBackPressed() {
    }
}

