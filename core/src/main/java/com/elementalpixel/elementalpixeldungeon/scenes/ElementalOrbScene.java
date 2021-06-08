/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.elementalpixel.elementalpixeldungeon.scenes;

import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.effects.Flare;
import com.elementalpixel.elementalpixeldungeon.effects.Speck;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.ui.RedButton;
import com.elementalpixel.elementalpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class ElementalOrbScene extends PixelScene {

    private static final int WIDTH			= 120;
    private static final int BTN_HEIGHT		= 18;
    private static final float SMALL_GAP	= 2;
    private static final float LARGE_GAP	= 8;

    public static boolean noText = false;

    @Override
    public void create() {
        super.create();

        RenderedTextBlock text = null;
        if (!noText) {
            text = renderTextBlock( Messages.get(this, "text"), 8 );
            text.maxWidth(WIDTH);
            add( text );
        }

        RedButton btnFire = new RedButton( Messages.get(this, "fire") ) {
            @Override
            protected void onClick() {
                Dungeon.depth = 30;
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                Game.switchScene( InterlevelScene.class);
            }
        };
        btnFire.setSize( WIDTH, BTN_HEIGHT );
        add( btnFire );

        RedButton btnAir = new RedButton( Messages.get(this, "air") ) {
            @Override
            protected void onClick() {
                Dungeon.depth = 27;
                //InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene( InterlevelScene.class);
            }
        };
        btnAir.setSize( WIDTH, BTN_HEIGHT );
        add( btnAir );

        RedButton btnWater = new RedButton( Messages.get(this, "water") ) {
            @Override
            protected void onClick() {
                Dungeon.depth = 27;
                //InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene( InterlevelScene.class);
            }
        };
        btnWater.setSize( WIDTH, BTN_HEIGHT );
        add( btnWater );

        RedButton btnEarth = new RedButton( Messages.get(this, "earth") ) {
            @Override
            protected void onClick() {
                Dungeon.depth = 27;
                //InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene( InterlevelScene.class);
            }
        };
        btnEarth.setSize( WIDTH, BTN_HEIGHT );
        add( btnEarth );

        RedButton btnStay = new RedButton( Messages.get(this, "stay") ) {
            @Override
            protected void onClick() {
                onBackPressed();
            }
        };
        btnStay.setSize( WIDTH, BTN_HEIGHT );
        add( btnStay );

        RedButton btnReturn = new RedButton( Messages.get(this, "return") ) {
            @Override
            protected void onClick() {
                Dungeon.depth = 25;
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                Game.switchScene( InterlevelScene.class);
            }
        };
        btnReturn.setSize( WIDTH, BTN_HEIGHT);
        add( btnReturn );

        if (noText) {
            btnFire.setPos( (Camera.main.width - btnFire.width()) / 2, LARGE_GAP * 3 );
            btnAir.setPos( btnFire.left(), btnFire.bottom() + SMALL_GAP);
            btnWater.setPos( btnAir.left(), btnAir.bottom() + SMALL_GAP);
            btnEarth.setPos( btnWater.left(), btnWater.bottom() + SMALL_GAP);

            btnStay.setPos( btnEarth.left(), btnEarth.bottom() + (SMALL_GAP + 2) );
            btnReturn.setPos( btnStay.left(), btnStay.bottom() + SMALL_GAP);

        } else {
            text.setPos((Camera.main.width - text.width()) / 2, LARGE_GAP * 3);
            align(text);

            btnFire.setPos( (Camera.main.width - btnFire.width()) / 2, text.top() + text.height() + LARGE_GAP );
            btnAir.setPos( btnFire.left(), btnFire.bottom() + SMALL_GAP);
            btnWater.setPos( btnAir.left(), btnAir.bottom() + SMALL_GAP);
            btnEarth.setPos( btnWater.left(), btnWater.bottom() + SMALL_GAP);

            btnStay.setPos( btnEarth.left(), btnEarth.bottom() + (SMALL_GAP + 2) );
            btnReturn.setPos( btnStay.left(), btnStay.bottom() + SMALL_GAP);
        }

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene( InterlevelScene.class );
    }

    private float timer = 0;

    @Override
    public void update() {
        super.update();

        if ((timer -= Game.elapsed) < 0) {
            timer = Random.Float( 0.5f, 5f );
        }
    }
}
