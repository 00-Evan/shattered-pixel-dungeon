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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Random;

public abstract class NPC extends Mob {

    {
        HP = HT = 1;
        EXP = 0;

        hostile = false;
        state = PASSIVE;
    }

    protected void throwItem() {
        Heap heap = Dungeon.level.heaps.get( pos );
        if (heap != null) {
            int n;
            do {
                n = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
            } while (!Level.passable[n] && !Level.avoid[n]);
            Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
        }
    }

    @Override
    public void beckon( int cell ) {
    }

    abstract public void interact();
}