/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
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
	public boolean doPickUp(Hero hero, int pos) {
		if (super.doPickUp(hero, pos)){
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

		{
			//not cleansed by reviving, but does check to ensure the dust is still present
			revivePersists = true;
		}

		@Override
		public boolean act() {
			if (target instanceof Hero && ((Hero) target).belongings.getItem(CorpseDust.class) == null){
				spawnPower = 0;
				spend(TICK);
				return true;
			}

			spawnPower++;
			int wraiths = 1; //we include the wraith we're trying to spawn
			for (Mob mob : Dungeon.level.mobs){
				if (mob instanceof DustWraith){
					wraiths++;
				}
			}

			//summoning a new wraith requires 1/4/9/16/25/36/49/49/... turns of energy
			//note that logic for summoning wraiths kind of has an odd, undocumented balance history:
			//v0.3.1-v0.6.5: wraith every 1/4/9/16/25/25... turns, basically guaranteed
			//v0.7.0-v2.1.4: bugged, same rate as above but high (often >50%) chance that spawning fails. failed spawning resets delay!
			//v2.2.0+: fixed bug, increased summon delay cap to counteract a bit, wraiths also now have to spawn at a slight distance
			int powerNeeded = Math.min(49, wraiths*wraiths);
			if (powerNeeded <= spawnPower){
				ArrayList<Integer> candidates = new ArrayList<>();
				//min distance scales based on hero's view distance
				// wraiths must spawn at least 4/3/2/1 tiles away at view distance of 8(default)/7/4/1
				int minDist = Math.round(Dungeon.hero.viewDistance/3f);
				for (int i = 0; i < Dungeon.level.length(); i++){
					if (Dungeon.level.heroFOV[i]
							&& !Dungeon.level.solid[i]
							&& Actor.findChar( i ) == null
							&& Dungeon.level.distance(i, Dungeon.hero.pos) > minDist){
						candidates.add(i);
					}
				}
				if (!candidates.isEmpty()){
					Wraith.spawnAt(Random.element(candidates), DustWraith.class);
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
					spawnPower -= powerNeeded;
				} else {
					//prevents excessive spawn power buildup
					spawnPower = Math.min(spawnPower, 2*wraiths);
				}
			}

			spend(TICK);
			return true;
		}

		public void dispel(){
			detach();
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
				if (mob instanceof DustWraith){
					mob.die(null);
				}
			}
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Music.INSTANCE.fadeOut(1f, new Callback() {
						@Override
						public void call() {
							if (Dungeon.level != null) {
								Dungeon.level.playLevelMusic();
							}
						}
					});
				}
			});
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

	public static class DustWraith extends Wraith{};

}
