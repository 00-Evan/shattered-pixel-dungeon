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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TalismanOfForesight extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_TALISMAN;

		exp = 0;
		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 100;

		defaultAction = AC_SCRY;
	}

	public static final String AC_SCRY = "SCRY";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero )
				&& !cursed
				&& hero.buff(MagicImmune.class) == null) {
			actions.add(AC_SCRY);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_SCRY)){
			if (!isEquipped(hero))  GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (charge < 5)    GLog.i( Messages.get(this, "low_charge") );
			else                    GameScene.selectCell(scry);
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Foresight();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;
		if (charge < chargeCap){
			partialCharge += 2*amount;
			while (partialCharge >= 1f){
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap) {
				charge = chargeCap;
				partialCharge = 0;
				GLog.p( Messages.get(TalismanOfForesight.class, "full_charge") );
			}
			updateQuickslot();
		}
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if ( isEquipped( Dungeon.hero ) ){
			if (!cursed) {
				desc += "\n\n" + Messages.get(this, "desc_worn");

			} else {
				desc += "\n\n" + Messages.get(this, "desc_cursed");
			}
		}

		return desc;
	}

	private float maxDist(){
		return Math.min(5 + 2*level(), (charge-3)/1.08f);
	}

	public CellSelector.Listener scry = new CellSelector.Listener(){

		@Override
		public void onSelect(Integer target) {
			if (target != null && target != curUser.pos){

				//enforces at least 2 tiles of distance
				if (Dungeon.level.adjacent(target, curUser.pos)){
					target += (target - curUser.pos);
				}

				float dist = Dungeon.level.trueDistance(curUser.pos, target);

				if (dist >= 3 && dist > maxDist()){
					Ballistica trajectory = new Ballistica(curUser.pos, target, Ballistica.STOP_TARGET);
					int i = 0;
					while (i < trajectory.path.size()
							&& Dungeon.level.trueDistance(curUser.pos, trajectory.path.get(i)) <= maxDist()){
						target = trajectory.path.get(i);
						i++;
					}
					dist = Dungeon.level.trueDistance(curUser.pos, target);
				}

				//starts at 200 degrees, loses 8% per tile of distance
				float angle = Math.round(200*(float)Math.pow(0.92, dist));
				ConeAOE cone = new ConeAOE(new Ballistica(curUser.pos, target, Ballistica.STOP_TARGET), angle);

				int earnedExp = 0;
				boolean noticed = false;
				for (int cell : cone.cells){
					GameScene.effectOverFog(new CheckedCell( cell, curUser.pos ));
					if (Dungeon.level.discoverable[cell] && !(Dungeon.level.mapped[cell] || Dungeon.level.visited[cell])){
						Dungeon.level.mapped[cell] = true;
						earnedExp++;
					}

					if (Dungeon.level.secret[cell]) {
						int oldValue = Dungeon.level.map[cell];
						GameScene.discoverTile(cell, oldValue);
						Dungeon.level.discover( cell );
						ScrollOfMagicMapping.discover(cell);
						noticed = true;

						if (oldValue == Terrain.SECRET_TRAP){
							earnedExp += 10;
						} else if (oldValue == Terrain.SECRET_DOOR){
							earnedExp += 100;
						}
					}

					Char ch = Actor.findChar(cell);
					if (ch != null
							&& (ch.alignment != Char.Alignment.NEUTRAL || ch instanceof Mimic)
							&& ch.alignment != curUser.alignment){
						Buff.append(curUser, CharAwareness.class, 5 + 2*level()).charID = ch.id();

						artifactProc(ch, visiblyUpgraded(), (int)(3 + dist*1.08f));

						if (!curUser.fieldOfView[ch.pos]){
							earnedExp += 10;
						}
					}

					Heap h = Dungeon.level.heaps.get(cell);
					if (h != null){
						Buff.append(curUser, HeapAwareness.class, 5 + 2*level()).pos = h.pos;

						if (!h.seen){
							earnedExp += 10;
						}
					}

				}

				exp += earnedExp;
				if (exp >= 100 + 50*level() && level() < levelCap) {
					exp -= 100 + 50*level();
					upgrade();
					Catalog.countUse(TalismanOfForesight.class);
					GLog.p( Messages.get(TalismanOfForesight.class, "levelup") );
				}
				updateQuickslot();

				//5 charge at 2 tiles, up to 30 charge at 25 tiles
				charge -= 3 + dist*1.08f;
				partialCharge -= (dist*1.08f)%1f;
				if (partialCharge < 0 && charge > 0){
					partialCharge ++;
					charge --;
				}
				while (charge < 0){
					charge++;
					partialCharge--;
				}
				Invisibility.dispel(curUser);
				Talent.onArtifactUsed(Dungeon.hero);
				updateQuickslot();
				Dungeon.observe();
				Dungeon.hero.checkVisibleMobs();
				GameScene.updateFog();

				curUser.sprite.zap(target);
				curUser.spendAndNext(Actor.TICK);
				Sample.INSTANCE.play(Assets.Sounds.SCAN);
				if (noticed) Sample.INSTANCE.play(Assets.Sounds.SECRET);

			}

		}

		@Override
		public String prompt() {
			return Messages.get(TalismanOfForesight.class, "prompt");
		}
	};

	private static final String WARN = "warn";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WARN, warn);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		warn = bundle.getBoolean(WARN);
	}
	
	private boolean warn = false;
	
	public class Foresight extends ArtifactBuff{

		@Override
		public boolean act() {
			spend( TICK );

			checkAwareness();

			if (charge < chargeCap
					&& !cursed
					&& target.buff(MagicImmune.class) == null
					&& Regeneration.regenOn()) {
				//fully charges in 2000 turns at +0, scaling to 1000 turns at +10.
				float chargeGain = (0.05f+(level()*0.005f));
				chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
				partialCharge += chargeGain;

				while (partialCharge >= 1){
					partialCharge--;
					charge++;
					if (charge >= chargeCap) {
						partialCharge = 0;
						GLog.p(Messages.get(TalismanOfForesight.class, "full_charge"));
					}
					updateQuickslot();
				}
			}

			return true;
		}

		public void checkAwareness(){
			boolean smthFound = false;

			int distance = 3;

			int cx = target.pos % Dungeon.level.width();
			int cy = target.pos / Dungeon.level.width();
			int ax = cx - distance;
			if (ax < 0) {
				ax = 0;
			}
			int bx = cx + distance;
			if (bx >= Dungeon.level.width()) {
				bx = Dungeon.level.width() - 1;
			}
			int ay = cy - distance;
			if (ay < 0) {
				ay = 0;
			}
			int by = cy + distance;
			if (by >= Dungeon.level.height()) {
				by = Dungeon.level.height() - 1;
			}

			for (int y = ay; y <= by; y++) {
				for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {

					if (Dungeon.level.heroFOV[p]
							&& Dungeon.level.secret[p]
							&& Dungeon.level.map[p] != Terrain.SECRET_DOOR) {
						if (Dungeon.level.traps.get(p) != null && Dungeon.level.traps.get(p).canBeSearched) {
							smthFound = true;
						}
					}
				}
			}

			if (smthFound
					&& !cursed
					&& target.buff(MagicImmune.class) == null){
				if (!warn){
					GLog.w( Messages.get(this, "uneasy") );
					if (target instanceof Hero){
						((Hero)target).interrupt();
					}
					warn = true;
				}
			} else {
				warn = false;
			}
		}

		public void charge(int boost){
			if (!cursed && target.buff(MagicImmune.class) == null) {
				charge = Math.min((charge + boost), chargeCap);
				updateQuickslot();
			}
		}

		@Override
		public int icon() {
			if (warn)
				return BuffIndicator.FORESIGHT;
			else
				return BuffIndicator.NONE;
		}
	}

	public static class CharAwareness extends FlavourBuff {

		public int charID;

		private static final String CHAR_ID = "char_id";

		@Override
		public void detach() {
			super.detach();
			Dungeon.observe();
			GameScene.updateFog();
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			charID = bundle.getInt(CHAR_ID);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(CHAR_ID, charID);
		}

	}

	public static class HeapAwareness extends FlavourBuff {

		public int pos;
		public int depth = Dungeon.depth;
		public int branch = Dungeon.branch;

		private static final String POS = "pos";
		private static final String DEPTH = "depth";
		private static final String BRANCH = "branch";

		@Override
		public void detach() {
			super.detach();
			Dungeon.observe();
			GameScene.updateFog();
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
			depth = bundle.getInt(DEPTH);
			branch = bundle.getInt(BRANCH);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
			bundle.put(DEPTH, depth);
			bundle.put(BRANCH, branch);
		}
	}

}
