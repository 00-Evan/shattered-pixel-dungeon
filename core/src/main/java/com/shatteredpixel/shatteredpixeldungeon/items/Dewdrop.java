/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Dewdrop extends Item {
	
	{
		image = ItemSpriteSheet.DEWDROP;
		
		stackable = true;
		dropsDownHeap = true;
	}
	
	@Override
	public boolean doPickUp(Hero hero, int pos) {
		
		Waterskin flask = hero.belongings.getItem( Waterskin.class );
		
		if (flask != null && !flask.isFull()){

			flask.collectDew( this );
			GameScene.pickUp( this, pos );

		} else {

			int terr = Dungeon.level.map[pos];
			if (!consumeDew(1, hero, terr == Terrain.ENTRANCE|| terr == Terrain.EXIT || terr == Terrain.UNLOCKED_EXIT)){
				return false;
			}
			
		}
		
		Sample.INSTANCE.play( Assets.Sounds.DEWDROP );
		hero.spendAndNext( TIME_TO_PICK_UP );
		
		return true;
	}

	public static boolean consumeDew(int quantity, Hero hero, boolean force){
		//20 drops for a full heal
		int heal = Math.round( hero.HT * 0.05f * quantity );

		int effect = Math.min( hero.HT - hero.HP, heal );
		int shield = 0;
		if (hero.hasTalent(Talent.SHIELDING_DEW)){
			shield = heal - effect;
			int maxShield = Math.round(hero.HT *0.2f*hero.pointsInTalent(Talent.SHIELDING_DEW));
			int curShield = 0;
			if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
			shield = Math.min(shield, maxShield-curShield);
		}
		if (effect > 0 || shield > 0) {
			hero.HP += effect;
			if (shield > 0) Buff.affect(hero, Barrier.class).incShield(shield);
			hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			if (effect > 0 && shield > 0){
				hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Dewdrop.class, "both", effect, shield) );
			} else if (effect > 0){
				hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Dewdrop.class, "heal", effect) );
			} else {
				hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Dewdrop.class, "shield", shield) );
			}

		} else if (!force) {
			GLog.i( Messages.get(Dewdrop.class, "already_full") );
			return false;
		}

		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	//max of one dew in a stack

	@Override
	public Item merge( Item other ){
		if (isSimilar( other )){
			quantity = 1;
			other.quantity = 0;
		}
		return this;
	}

	@Override
	public Item quantity(int value) {
		quantity = Math.min( value, 1);
		return this;
	}

}
