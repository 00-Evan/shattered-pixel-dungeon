/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class ClothStrip extends Item {

	public static final String AC_BANDAGE	= "BANDAGED";
	
	public static final float TIME_TO_BANDAGE = 1;
	
	{
		image = ItemSpriteSheet.CLOTHSTRIP;
		
		stackable = true;
		
		defaultAction = AC_BANDAGE;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_BANDAGE );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );
		
		if (action.equals( AC_BANDAGE )) {
			
			hero.spend( TIME_TO_BANDAGE );
			hero.busy();
			
			hero.sprite.operate( hero.pos );
			
			detach( hero.belongings.backpack );

			float healing = Math.min((3 + 0.1f * hero.HT),(hero.HT - hero.HP));
			hero.HP += healing;

			Buff.detach(hero, Bleeding.class);
			Sample.INSTANCE.play(Assets.Sounds.GRASS);
			

			//Emitter emitter = hero.sprite.centerEmitter();
			//emitter.start( FlameParticle.FACTORY, 0.2f, 3 );

			
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 8 * quantity;
	}

}
