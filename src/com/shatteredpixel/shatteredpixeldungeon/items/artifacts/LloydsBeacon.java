/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class LloydsBeacon extends Artifact {

	public static final float TIME_TO_USE = 1;

	public static final String AC_ZAP       = "ZAP";
	public static final String AC_SET		= "SET";
	public static final String AC_RETURN	= "RETURN";
	
	private int returnDepth	= -1;
	private int returnPos;
	
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
	public ArrayList<String> actions( Hero hero ) {
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
			
			for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
				if (Actor.findChar( hero.pos + Level.NEIGHBOURS8[i] ) != null) {
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
			Sample.INSTANCE.play( Assets.SND_BEACON );
			
			GLog.i( Messages.get(this, "return") );
			
		} else if (action == AC_RETURN) {
			
			if (returnDepth == Dungeon.depth) {
				ScrollOfTeleportation.appear( hero, returnPos );
				Dungeon.level.press( returnPos, hero );
				Dungeon.observe();
			} else {

				Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null) buff.detach();

				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] ))
					if (mob instanceof DriedRose.GhostHero) mob.destroy();

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
					Sample.INSTANCE.play( Assets.SND_ZAP );
					curUser.sprite.zap(bolt.collisionPos);
					curUser.busy();

					MagicMissile.force(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, new Callback() {
						@Override
						public void call() {
							if (ch != null) {

								int count = 10;
								int pos;
								do {
									pos = Dungeon.level.randomRespawnCell();
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
									ch.sprite.place(ch.pos);
									ch.sprite.visible = Dungeon.visible[pos];

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
	
	private static final Glowing WHITE = new Glowing( 0xFFFFFF );
	
	@Override
	public Glowing glowing() {
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
