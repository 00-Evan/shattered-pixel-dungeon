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

package com.saqfish.spdnet.actors.blobs;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.buffs.Hunger;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.effects.BlobEmitter;
import com.saqfish.spdnet.effects.CellEmitter;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.effects.particles.ShadowParticle;
import com.saqfish.spdnet.effects.particles.ShaftParticle;
import com.saqfish.spdnet.items.Ankh;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.Waterskin;
import com.saqfish.spdnet.items.potions.PotionOfHealing;
import com.saqfish.spdnet.items.scrolls.ScrollOfRemoveCurse;
import com.saqfish.spdnet.journal.Notes.Landmark;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class WaterOfHealth extends WellWater {
	
	@Override
	protected boolean affectHero( Hero hero ) {
		
		if (!hero.isAlive()) return false;
		
		Sample.INSTANCE.play( Assets.Sounds.DRINK );

		PotionOfHealing.cure( hero );
		hero.belongings.uncurseEquipped();
		hero.buff( Hunger.class ).satisfy( Hunger.STARVING );

		hero.HP = hero.HT;
		hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
		
		CellEmitter.get( hero.pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

		Dungeon.hero.interrupt();
	
		GLog.p( Messages.get(this, "procced") );
		
		return true;
	}
	
	@Override
	protected Item affectItem( Item item, int pos ) {
		if (item instanceof Waterskin && !((Waterskin)item).isFull()) {
			((Waterskin)item).fill();
			CellEmitter.get( pos ).start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
			return item;
		} else if ( item instanceof Ankh && !(((Ankh) item).isBlessed())){
			((Ankh) item).bless();
			CellEmitter.get( pos ).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
			return item;
		} else if (ScrollOfRemoveCurse.uncursable(item)) {
			if (ScrollOfRemoveCurse.uncurse( null, item )){
				CellEmitter.get( pos ).start( ShadowParticle.UP, 0.05f, 10 );
			}
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
			return item;
		}
		return null;
	}
	
	@Override
	protected Landmark record() {
		return Landmark.WELL_OF_HEALTH;
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.HEALING ), 0.5f, 0 );
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
