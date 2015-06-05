/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.ui;

import com.watabou.noosa.Game;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.ui.Component;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;

public class Archs extends Component {

    private static final float SCROLL_SPEED	= 20f;

    private SkinnedBlock arcsBg;
    private SkinnedBlock arcsFg;
    private SkinnedBlock arcsSn;
    private SkinnedBlock arcsSn2;

    private static float offsB = 0;
    private static float offsF = 0;
    private static float offsSn = 0;
    private static float offsSn2 = 0;

    public boolean reversed = false;

    @Override
    protected void createChildren() {
        arcsBg = new SkinnedBlock( 1, 1, Assets.ARCS_BG );
        arcsBg.offsetTo( 0,  offsB );
        add( arcsBg );

        arcsFg = new SkinnedBlock( 1, 1, Assets.ARCS_FG );
        arcsFg.offsetTo( 0,  offsF );
        add( arcsFg );

        arcsSn = new SkinnedBlock( 1, 1, Assets.ARCS_SN );
        arcsSn.offsetTo( 0,  offsSn );
        add( arcsSn );

        arcsSn2 = new SkinnedBlock( 1, 1, Assets.ARCS_SN );
        arcsSn2.offsetTo( 0,  offsSn2 );
        add( arcsSn2 );
    }

    @Override
    protected void layout() {
        arcsBg.size( width, height );
        arcsBg.offset(arcsBg.texture.width / 4 - (width % arcsBg.texture.width) / 2, 0);

        arcsFg.size(width, height);
        arcsFg.offset(arcsFg.texture.width / 4 - (width % arcsFg.texture.width) / 2, 0);

        arcsSn.size(width, height);
        arcsSn.offset(arcsSn.texture.width / 4 - (width % arcsSn.texture.width) / 2, 0);

        arcsSn2.size(width, height);
        arcsSn2.offset(arcsSn2.texture.width / 4 - (width % arcsSn2.texture.width) / 2, 0);
    }

    @Override
    public void update() {

        super.update();

        float shift = Game.elapsed * SCROLL_SPEED;
        if (reversed) {
            shift = -shift;
        }

        arcsBg.offset(0, shift);
        arcsFg.offset( 0, shift * 2 );
        arcsSn.offset( 0, shift * -2 );
        arcsSn2.offset( shift * -1 , shift * -1 );

        offsB = arcsBg.offsetY();
        offsF = arcsFg.offsetY();
        offsSn = arcsSn.offsetY();
        offsSn2 = arcsSn.offsetY();
        offsSn = arcsSn.offsetX();
    }
}
