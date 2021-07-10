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

package com.saqfish.spdnet.items.quest;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.actors.mobs.Mob;
import com.saqfish.spdnet.actors.mobs.Wraith;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CorpseDust extends Item {
	
	{
		image = ItemSpriteSheet.DUST;
		
		cursed = true;
		cursedKnown = true;
		
		unique = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		return new ArrayList<>(); //yup, no dropping this one
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
	public boolean doPickUp(Hero hero) {
		if (super.doPickUp(hero)){
			GLog.n( Messages.get( this, "chill") );
			Buff.affect(hero, DustGhostSpawner.class);
			return true;
		}
		return false;
	}

	@Override
	protected void onDetach() {
		DustGhostSpawner spawner = Dungeon.hero.buff(DustGhostSpawner.class);
		if (spawner != null){
			spawner.dispel();
		}
	}

	public static class DustGhostSpawner extends Buff {

		int spawnPower = 0;

		@Override
		public boolean act() {
			spawnPower++;
			int wraiths = 1; //we include the wraith we're trying to spawn
			for (Mob mob : Dungeon.level.mobs){
				if (mob instanceof Wraith){
					wraiths++;
				}
			}

			int powerNeeded = Math.min(25, wraiths*wraiths);

			if (powerNeeded <= spawnPower){
				spawnPower -= powerNeeded;
				int pos = 0;
				int tries = 20;
				do{
					pos = Random.Int(Dungeon.level.length());
					tries --;
				} while (tries > 0 && (!Dungeon.level.heroFOV[pos] || Dungeon.level.solid[pos] || Actor.findChar( pos ) != null));
				if (tries > 0) {
					Wraith.spawnAt(pos);
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
				}
			}

			spend(TICK);
			return true;
		}

		public void dispel(){
			detach();
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
				if (mob instanceof Wraith){
					mob.die(null);
				}
			}
		}

		private static String SPAWNPOWER = "spawnpower";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( SPAWNPOWER, spawnPower );
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			spawnPower = bundle.getInt( SPAWNPOWER );
		}
	}

}
