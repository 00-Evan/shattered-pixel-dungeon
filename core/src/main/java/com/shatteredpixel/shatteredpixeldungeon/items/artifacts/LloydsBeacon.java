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

package com.elementalpixel.elementalpixeldungeon.items.artifacts;


import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.Actor;
import com.elementalpixel.elementalpixeldungeon.actors.Char;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Invisibility;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.LockedFloor;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.actors.mobs.Mob;
import com.elementalpixel.elementalpixeldungeon.effects.MagicMissile;
import com.elementalpixel.elementalpixeldungeon.items.Item;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.elementalpixel.elementalpixeldungeon.mechanics.Ballistica;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.plants.Swiftthistle;
import com.elementalpixel.elementalpixeldungeon.scenes.CellSelector;
import com.elementalpixel.elementalpixeldungeon.scenes.GameScene;
import com.elementalpixel.elementalpixeldungeon.scenes.InterlevelScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSprite;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.ui.QuickSlotButton;
import com.elementalpixel.elementalpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class LloydsBeacon extends Artifact {

	public static final float TIME_TO_USE = 1;

	public static final String AC_ZAP       = "ZAP";
	public static final String AC_SET		= "SET";
	public static final String AC_RETURN	= "RETURN";
	
	public int returnDepth	= -1;
	public int returnPos;
	
	{
		image = ItemSpriteSheet.ARTIFACT_BEACON;

		levelCap = 3;

		charge = 0;
		chargeCap = 3+level();

		defaultAction = AC_ZAP;
		usesTargeting = true;
	}
	
	private static final String DEPTH	= "depth";
	private static final String POS		= "pos";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DEPTH, returnDepth );
		if (returnDepth != -1) {
			bundle.put( POS, returnPos );
		}
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		returnDepth	= bundle.getInt( DEPTH );
		returnPos	= bundle.getInt( POS );
	}
	
	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_ZAP );
		actions.add( AC_SET );
		if (returnDepth != -1) {
			actions.add( AC_RETURN );
		}
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action == AC_SET || action == AC_RETURN) {
			
			if (Dungeon.bossLevel()) {
				hero.spend( LloydsBeacon.TIME_TO_USE );
				GLog.w( Messages.get(this, "preventing") );
				return;
			}
			
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				Char ch = Actor.findChar(hero.pos + PathFinder.NEIGHBOURS8[i]);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
					GLog.w( Messages.get(this, "creatures") );
					return;
				}
			}
		}

		if (action == AC_ZAP ){

			curUser = hero;
			int chargesToUse = Dungeon.depth > 20 ? 2 : 1;

			if (!isEquipped( hero )) {
				GLog.i( Messages.get(Artifact.class, "need_to_equip") );
				QuickSlotButton.cancel();

			} else if (charge < chargesToUse) {
				GLog.i( Messages.get(this, "no_charge") );
				QuickSlotButton.cancel();

			} else {
				GameScene.selectCell(zapper);
			}

		} else if (action == AC_SET) {
			
			returnDepth = Dungeon.depth;
			returnPos = hero.pos;
			
			hero.spend( LloydsBeacon.TIME_TO_USE );
			hero.busy();
			
			hero.sprite.operate( hero.pos );
			Sample.INSTANCE.play( Assets.Sounds.BEACON );
			
			GLog.i( Messages.get(this, "return") );
			
		} else if (action == AC_RETURN) {
			
			if (returnDepth == Dungeon.depth) {
				ScrollOfTeleportation.appear( hero, returnPos );
				for(Mob m : Dungeon.level.mobs){
					if (m.pos == hero.pos){
						//displace mob
						for(int i : PathFinder.NEIGHBOURS8){
							if (Actor.findChar(m.pos+i) == null && Dungeon.level.passable[m.pos + i]){
								m.pos += i;
								m.sprite.point(m.sprite.worldToCamera(m.pos));
								break;
							}
						}
					}
				}
				Dungeon.level.occupyCell(hero );
				Dungeon.observe();
				GameScene.updateFog();
			} else {

				Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null) buff.detach();
				buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
				if (buff != null) buff.detach();

				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnDepth = returnDepth;
				InterlevelScene.returnPos = returnPos;
				Game.switchScene( InterlevelScene.class );
			}
			
			
		}
	}

	protected CellSelector.Listener zapper = new  CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {

			if (target == null) return;

			Invisibility.dispel();
			charge -= Dungeon.depth > 20 ? 2 : 1;
			updateQuickslot();

			if (Actor.findChar(target) == curUser){
				ScrollOfTeleportation.teleportHero(curUser);
				curUser.spendAndNext(1f);
			} else {
				final Ballistica bolt = new Ballistica( curUser.pos, target, Ballistica.MAGIC_BOLT );
				final Char ch = Actor.findChar(bolt.collisionPos);

				if (ch == curUser){
					ScrollOfTeleportation.teleportHero(curUser);
					curUser.spendAndNext( 1f );
				} else {
					Sample.INSTANCE.play( Assets.Sounds.ZAP );
					curUser.sprite.zap(bolt.collisionPos);
					curUser.busy();

					MagicMissile.boltFromChar(curUser.sprite.parent,
							MagicMissile.BEACON,
							curUser.sprite,
							bolt.collisionPos,
							new Callback() {
								@Override
								public void call() {
									if (ch != null) {

										int count = 10;
										int pos;
										do {
											pos = Dungeon.level.randomRespawnCell( ch );
											if (count-- <= 0) {
												break;
											}
										} while (pos == -1);

										if (pos == -1 || Dungeon.bossLevel()) {

											GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );

										} else if (ch.properties().contains(Char.Property.IMMOVABLE)) {

											GLog.w( Messages.get(LloydsBeacon.class, "tele_fail") );

										} else  {

											ch.pos = pos;
											if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING){
												((Mob) ch).state = ((Mob) ch).WANDERING;
											}
											ch.sprite.place(ch.pos);
											ch.sprite.visible = Dungeon.level.heroFOV[pos];

										}
									}
									curUser.spendAndNext(1f);
								}
							});

				}


			}

		}

		@Override
		public String prompt() {
			return Messages.get(LloydsBeacon.class, "prompt");
		}
	};

	@Override
	protected ArtifactBuff passiveBuff() {
		return new beaconRecharge();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (charge < chargeCap){
			partialCharge += 0.25f*amount;
			if (partialCharge >= 1){
				partialCharge--;
				charge++;
				updateQuickslot();
			}
		}
	}

	@Override
	public Item upgrade() {
		if (level() == levelCap) return this;
		chargeCap ++;
		GLog.p( Messages.get(this, "levelup") );
		return super.upgrade();
	}

	@Override
	public String desc() {
		String desc = super.desc();
		if (returnDepth != -1){
			desc += "\n\n" + Messages.get(this, "desc_set", returnDepth);
		}
		return desc;
	}
	
	private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
	
	@Override
	public ItemSprite.Glowing glowing() {
		return returnDepth != -1 ? WHITE : null;
	}

	public class beaconRecharge extends ArtifactBuff{
		@Override
		public boolean act() {
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
				partialCharge += 1 / (100f - (chargeCap - charge)*10f);

				if (partialCharge >= 1) {
					partialCharge --;
					charge ++;

					if (charge == chargeCap){
						partialCharge = 0;
					}
				}
			}

			updateQuickslot();
			spend( TICK );
			return true;
		}
	}
}
