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

import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class PotionOfLevitation extends Potion {

	{
		name = "Potion of Levitation";
		initials = "Le";
	}

    @Override
    public void shatter( int cell ) {

	    if (Dungeon.visible[cell]) {
		    setKnown();

		    splash( cell );
		    Sample.INSTANCE.play( Assets.SND_SHATTER );
	    }

	    GameScene.add( Blob.seed( cell, 1000, ConfusionGas.class ) );
    }
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, Levitation.class, Levitation.DURATION );
		GLog.i( "You float into the air!" );
	}
	
	@Override
	public String desc() {
		return
			"Drinking this curious liquid will cause you to hover in the air, " +
			"able to drift effortlessly over traps and pits. Throwing this potion " +
            "will create a cloud of unrefined gas, disorienting anything caught in it.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
