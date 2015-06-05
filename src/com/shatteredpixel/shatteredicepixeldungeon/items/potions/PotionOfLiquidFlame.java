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
package com.shatteredpixel.shatteredicepixeldungeon.items.potions;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;

public class PotionOfLiquidFlame extends Potion {

	{
		name = "Potion of Liquid Flame";
		initials = "LF";
	}
	
	@Override
	public void shatter( int cell ) {

		if (Dungeon.visible[cell]) {
			setKnown();

			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}

        for (int offset : Level.NEIGHBOURS9){
            if (Level.flamable[cell+offset]
                    || Actor.findChar(cell+offset) != null
                    || Dungeon.level.heaps.get(cell+offset) != null) {

                GameScene.add(Blob.seed(cell + offset, 2, Fire.class));

            } else {

                CellEmitter.get(cell+offset).burst(FlameParticle.FACTORY, 2);

            }
        }
	}
	
	@Override
	public String desc() {
		return
			"This flask contains an unstable compound which will burst " +
			"violently into flame upon exposure to open air.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}
