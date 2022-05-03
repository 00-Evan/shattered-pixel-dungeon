package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;

public class WndGoShop extends Window {
    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 32;
    private static final int BTN_GAP	= 5;
    private static final int GAP		= 4;
    int w = Camera.main.width;

    public WndGoShop(RedButton callback) {
        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, -4, WIDTH, 0);
        titlebar.icon(new ShopkeeperSprite());
        titlebar.label(Messages.get(this, "szo"));
        add(titlebar);
        RenderedTextBlock message = PixelScene.renderTextBlock((Messages.get(this, "ary")), 6);
        message.maxWidth(WIDTH);

        message.setPos(0, titlebar.bottom() + GAP);
        add(message);

        RedButton btnBuy = new RedButton( Messages.get(this, "yes") ) {
            @Override
            protected void onClick() {
                hide();
                for (Mob mob : Dungeon.level.mobs) {
                    if ( mob instanceof Shopkeeper) {
                        mob.yell(Messages.get(mob, "thief"));
                        ((Shopkeeper) mob).flee();
                        break;
                    }
                }
            }
        };
        btnBuy.setRect( (WIDTH - BTN_GAP) / 2 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
        btnBuy.textColor(Window.ANSDO_COLOR);
        add( btnBuy );

        RedButton btnNo = new RedButton( Messages.get(this, "no") ) {
            @Override
            protected void onClick() {
                hide();
                GLog.n( Messages.get(WndGoShop.class, "notbad") );
            }
        };
        btnNo.setRect( btnBuy.right() + BTN_GAP, btnBuy.top(), BTN_SIZE, BTN_SIZE );
        add( btnNo );
        btnNo.textColor(Window.CYELLOW);
        resize( WIDTH, (int)btnNo.bottom() );

        }

    }
