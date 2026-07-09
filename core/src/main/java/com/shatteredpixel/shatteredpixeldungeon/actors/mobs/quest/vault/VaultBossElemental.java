/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultFinalRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;

public class VaultBossElemental extends Mob {

	{
		spriteClass = ElementalSprite.Fire.class;

		HP = HT = 600;

		properties.add(Property.BOSS);
	}

	public enum ElementalForm {
		FIRE,
		FROST,
		SHOCK,
		UNSTABLE //currently unused
	}
	private ElementalForm form = ElementalForm.FIRE;

	public void setElementalForm( ElementalForm form ){
		//always remove pincushion as we're either leaving or entering frost form
		Buff.affect(this, PinCushionRemover.class);

		this.form = form;
		boolean wasTurned = sprite.flipHorizontal;
		if (form == ElementalForm.FIRE){
			sprite.killAndErase();
			GameScene.addSprite(this);
			sprite.emitter().burst(FlameParticle.FACTORY, 100);

			setupFireWall();

		} else if (form == ElementalForm.FROST){
			sprite.killAndErase();
			GameScene.addSprite(this);
			sprite.emitter().burst(MagicMissile.MagicParticle.FACTORY, 100);

			setupFrostVortex();

		} else if (form == ElementalForm.SHOCK){
			sprite.killAndErase();
			GameScene.addSprite(this);
			sprite.emitter().burst(SparkParticle.FACTORY, 100);

			setupLightningChase();
		}
		sprite.flipHorizontal = wasTurned;
		BossHealthBar.assignBoss(this);
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		if (form == ElementalForm.SHOCK && enemy == Dungeon.hero && !(Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
			enemy.sprite.parent.addToFront( new Lightning( sprite.center(), enemy.sprite.center(), null ) );
			enemy.damage( Random.IntRange(2, 5), new Shocking() ); //TODO final dmg
			Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
			PixelScene.shake( 2, 0.3f );
			enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			enemy.sprite.flash();
			if (enemy == Dungeon.hero && !enemy.isAlive()){
				Dungeon.fail(this);
				//TODO magic death badge?
			}
			GLog.w("Shocked!");
		}
		return super.defenseProc(enemy, damage);
	}

	@Override
	public void damage(int dmg, Object src) {
		//fire form is resistant to magic and weak to thrown weapons
		if (form == ElementalForm.FIRE){
			if (AntiMagic.RESISTS.contains(src.getClass())){
				GLog.w("Resisted!");
				dmg /= 4;
				if (src instanceof Wand || src instanceof ClericSpell){
					//TODO additional penalty, perhaps prompt more fire attacks?
				}
			} else if (src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon){
				GLog.w("Weak!");
				dmg += 10;
			}
		//frost form is resistant to thrown weapons and weak to melee (only from the hero though!)
		} else if ( form == ElementalForm.FROST ){
			if (src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon){
				GLog.w("Resisted!");
				dmg /= 4;
			} else if (src == Dungeon.hero && !(Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				GLog.w("Weak!");
				dmg += 10;
			}
		//shock form is resistant to melee and weak to magic
		//TODO what about magical consumables, mainly retribution?
		} else if ( form == ElementalForm.SHOCK ){
			if (src instanceof Char && !(src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				GLog.w("Resisted!");
				dmg /= 4;
			} else if (AntiMagic.RESISTS.contains(src.getClass())){
				GLog.w("Weak!");
				dmg += 10;
				//TODO slightly charge wands?
			}
		}

		//TODO do we want brackets like Tengu, or maybe just count up to 120 HP lost?
		int hpBracket = HT / 5; //120, is that right?
		int curbracket = (int) Math.ceil(HP / (float)hpBracket);

		super.damage(dmg, src);

		if (HP <= (curbracket-1)*hpBracket){
			//cannot be hit through multiple brackets at a time
			HP = Math.max(HP, (curbracket-2)*hpBracket);

			if (isAlive()) {
				//changes forms!
				ElementalForm newForm;
				do {
					newForm = ElementalForm.values()[Random.Int(3)];
				} while (newForm == form);
				setElementalForm(newForm);
				spend(TICK);
			}
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		if (Dungeon.level instanceof RegularLevel){
			Room r = ((RegularLevel) Dungeon.level).room(pos);
			if (r instanceof VaultFinalRoom){
				((VaultFinalRoom) r).unlock();
			}
		}
	}

	@Override
	public CharSprite sprite() {
		switch (form){
			case FIRE: spriteClass = ElementalSprite.Fire.class; break;
			case FROST: spriteClass = ElementalSprite.Frost.class; break;
			case SHOCK: spriteClass = ElementalSprite.Shock.class; break;
			case UNSTABLE: spriteClass = ElementalSprite.Chaos.class; break;
		}
		CharSprite sprite = super.sprite();
		sprite.scale.set(2f);
		return sprite;
	}

	@Override
	public boolean add(Buff buff) {
		if (buff instanceof PinCushion && form != ElementalForm.FROST){
			Buff.affect(this, PinCushionRemover.class);
		}
		return super.add(buff);
	}

	@Override
	public HashSet<Property> properties() {
		HashSet<Property> props = new HashSet<>(properties);
		if (form == ElementalForm.FIRE){
			props.add(Property.FIERY);
		} else if (form == ElementalForm.FROST){
			props.add(Property.ICY);
		} else if (form == ElementalForm.SHOCK){
			props.add(Property.ELECTRIC);
		}
		return props;
	}

	private static final String FORM = "elemental_form";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(FORM, form);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		form = bundle.getEnum(FORM, ElementalForm.class);
		BossHealthBar.assignBoss(this);
	}

	//used to forceably remove pincushion after its applied
	public static class PinCushionRemover extends Buff{

		{
			actPriority = VFX_PRIO;
		}

		@Override
		public boolean act() {

			PathFinder.buildDistanceMap(target.pos, Dungeon.level.passable, 2);
			ArrayList<Integer> candidates = new ArrayList<>();
			int furthestDist = 1;
			int closestDist = 100;
			for (int i = 0; i < Dungeon.level.length(); i++){
				if (PathFinder.distance[i] == 2){
					int dist = Dungeon.level.distance(i, Dungeon.hero.pos);
					if (dist > 1) {
						if (dist < closestDist){
							closestDist = dist;
						} if (dist > furthestDist){
							furthestDist = dist;
						}
						candidates.add(i);
					}
				}
			}

			//prevent overlap if hero is close
			if (Math.abs(furthestDist - closestDist) <= 1){
				furthestDist++;
				closestDist--;
			}

			for (int i : candidates.toArray(new Integer[0])){
				int dist = Dungeon.level.distance(i, Dungeon.hero.pos);
				if (dist <= closestDist || dist >= furthestDist){
					candidates.remove((Integer)i);
				}
			}

			while (target.buff(PinCushion.class) != null) {
				Item item = target.buff(PinCushion.class).grabOne();

				//TODO drop around, 2 tile distance favouring medium near the hero?
				Dungeon.level.drop(item, Random.element(candidates)).sprite.drop(target.pos);
			}
			detach();
			return true;
		}
	}

	/***************************
	 *** Fire Form Abilities ***
	 **************************/

	public void setupFireWall(){
		FireWall wall = Buff.append(this, FireWall.class);
		wall.cells = new int[10];
		Room r = ((RegularLevel)Dungeon.level).room(pos);
		Point c = r.center();
		int i = 0;

		//TODO currently wall and skip are random, it should vary based on player location and difficulty
		if (Random.Int(2) == 0){
			int y;
			if (Random.Int(2) == 0){
				y = c.y - 6;
				wall.direction = Dungeon.level.width();
			} else {
				y = c.y + 6;
				wall.direction = -Dungeon.level.width();
			}
			int skip = Random.IntRange(c.x-5, c.x+5);
			for (int x = c.x-5; x <= c.x+5; x++){
				if (x == skip) continue;
				wall.cells[i] = x + (y*Dungeon.level.width());
				i++;
			}
		} else {
			int x;
			if (Random.Int(2) == 0){
				x = c.x - 6;
				wall.direction = 1;
			} else {
				x = c.x + 6;
				wall.direction = -1;
			}
			int skip = Random.IntRange(c.y-5, c.y+5);
			for (int y = c.y-5; y <= c.y+5; y++){
				if (y == skip) continue;
				wall.cells[i] = x + (y*Dungeon.level.width());
				i++;
			}
		}
	}

	public static class FireWall extends Buff {

		private int[] cells = new int[0];
		private int direction;

		//variable width maybe? for now it's always 2

		private int left = 11; //always the same amount

		private ArrayList<Emitter> emitters = new ArrayList<>();

		@Override
		public boolean act() {

			for (int i = 0; i < cells.length; i++){

				for (int j = 0; j < 2; j++) {
					if (!Dungeon.level.solid[cells[i]+j*direction]) {
						CellEmitter.get(cells[i]+j*direction).burst(FlameParticle.FACTORY, 20);
						//TODO process damage, score change, etc.
					}
				}

				cells[i] += direction;
			}

			Sample.INSTANCE.play(Assets.Sounds.BURNING);

			if (left-- <= 0){
				detach();
			} else {
				updateFX();
			}

			spend( TICK );
			return true;
		}

		private void updateFX(){
			for (Emitter e : emitters){
				e.on = false;
			}
			emitters.clear();

			for (int cell : cells) {
				boolean oneOpen = false;
				for (int j = 0; j < 2; j++) {
					if (!Dungeon.level.solid[cell + j * direction]) {
						Emitter pour = CellEmitter.get(cell + j * direction);
						pour.pour(FlameParticle.FACTORY, 0.1f);

						emitters.add(pour);
						oneOpen = true;
					}
				}

				if (!oneOpen) {
					//do a tiny flame further ahead to show player the entire wall pattern
					for (int j = 1; j <= left; j++) {
						if (!Dungeon.level.solid[cell + direction * j]) {
							Emitter pour = CellEmitter.center(cell + direction * j);
							pour.pour(FlameParticle.FACTORY, 0.5f);
							emitters.add(pour);
							break;
						}
					}
				}
			}

		}

		@Override
		public void fx(boolean on) {
			if (on) {
				updateFX();
			} else {
				for (Emitter e : emitters){
					e.on = false;
				}
				emitters.clear();
			}
		}

		private static String CELLS = "cells";
		private static String DIRECTION = "direction";
		private static String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(CELLS, cells);
			bundle.put(DIRECTION, direction);
			bundle.put(LEFT, left);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			cells = bundle.getIntArray( CELLS );
			direction = bundle.getInt( DIRECTION );
			left = bundle.getInt(LEFT);
		}
	}

	/****************************
	 *** Frost Form Abilities ***
	 ***************************/

	public void setupFrostVortex(){
		FrostVortex vortex = Buff.append(this, FrostVortex.class);
		vortex.targetCell = Dungeon.hero.pos;

		//TODO no positional or difficulty variance atm
		// also doesn't end up playing as well with melee as I thought it might

	}

	public static class FrostVortex extends Buff {

		private int targetCell = -1;
		private int distance = -1000;

		private ArrayList<Emitter> emitters = new ArrayList<>();

		@Override
		public boolean act() {
			if (distance == -1000){
				int furthestDist = 0;
				boolean valid = true;
				int dist = getFurthestValid(targetCell, -1, -1);
				if (dist > furthestDist) furthestDist = dist;
				dist = getFurthestValid(targetCell, -1, 1);
				if (dist > furthestDist) furthestDist = dist;
				dist = getFurthestValid(targetCell, 1, 1);
				if (dist > furthestDist) furthestDist = dist;
				dist = getFurthestValid(targetCell, 1, -1);
				if (dist > furthestDist) furthestDist = dist;
				distance = Math.min(furthestDist, 5); //won't it always be 4 now?
			}

			HashSet<Integer> cells = new HashSet<>();
			cells.addAll(getCells(targetCell, distance, -1, -1));
			cells.addAll(getCells(targetCell, distance, -1, 1));
			cells.addAll(getCells(targetCell, distance, 1, 1));
			cells.addAll(getCells(targetCell, distance, 1, -1));

			distance--;

			updateFX();

			if (cells.isEmpty()){
				detach();
				return true;
			} else {
				for (Integer cell : cells){
					CellEmitter.get(cell).burst(MagicMissile.WhiteParticle.FACTORY, 20);
				}
				spend(TICK);
				return true;
			}
		}

		private void updateFX(){
			for (Emitter e : emitters){
				e.on = false;
			}
			emitters.clear();

			if (targetCell != -1) {
				HashSet<Integer> cells = new HashSet<>();
				cells.addAll(getCells(targetCell, distance, -1, -1));
				cells.addAll(getCells(targetCell, distance, -1, 1));
				cells.addAll(getCells(targetCell, distance, 1, 1));
				cells.addAll(getCells(targetCell, distance, 1, -1));

				for (Integer cell : cells) {
					Emitter pour = CellEmitter.get(cell);
					pour.pour(SnowParticle.FACTORY, 0.05f);
					emitters.add(pour);
				}
			}
		}

		@Override
		public void fx(boolean on) {
			if (on) {
				updateFX();
			} else {
				for (Emitter e : emitters){
					e.on = false;
				}
				emitters.clear();
			}
		}

		private int getFurthestValid(int start, int dirX, int dirY){
			int dist = 0;
			if (Dungeon.level.solid[start]) return dist;
			do {
				if (dist % 2 == 0){
					start += dirX;
				} else {
					start += dirY*Dungeon.level.width();
				}
				dist++;
			} while (!Dungeon.level.solid[start]);
			return dist;
		}

		private HashSet<Integer> getCells(int start, int dist, int dirX, int dirY){
			dist = Math.abs(dist);
			if (getFurthestValid(start, dirX, dirY) < dist){
				return new HashSet<>();
			}
			int xOfs = (dirX*(dist+1)/2);
			int yOfs = (dirY*dist/2)*Dungeon.level.width();
			int initialOfsCell = start + xOfs + yOfs;
			HashSet<Integer> cells = new HashSet<>();
			int cell = initialOfsCell;
			do {
				cells.add(cell);
				cell += -dirX + dirY*Dungeon.level.width();
			} while (!Dungeon.level.solid[cell]);
			cell = initialOfsCell;
			do {
				cells.add(cell);
				cell += +dirX - dirY*Dungeon.level.width();
			} while (!Dungeon.level.solid[cell]);

			return cells;
		}

		private static String TARGET_CELL = "target_cell";
		private static String DISTANCE = "distance";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TARGET_CELL, targetCell);
			bundle.put(DISTANCE, distance);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			targetCell = bundle.getInt(TARGET_CELL);
			distance = bundle.getInt(DISTANCE);
		}

	}

	/****************************
	 *** Shock Form Abilities ***
	 ***************************/

	//TODO this one isn't really working well at all atm, seems far too confusing
	public void setupLightningChase(){
		Room r = ((RegularLevel)Dungeon.level).room(pos);
		Point c = r.center();
		LightningChase chase = Buff.append(this, LightningChase.class);
		chase.curCell = c.x + (c.y-5)*Dungeon.level.width();

		chase = Buff.append(this, LightningChase.class);
		chase.curCell = c.x + (c.y+5)*Dungeon.level.width();

		chase = Buff.append(this, LightningChase.class);
		chase.curCell = c.x-5 + (c.y)*Dungeon.level.width();

		chase = Buff.append(this, LightningChase.class);
		chase.curCell = c.x+5 + (c.y)*Dungeon.level.width();
	}

	public static class LightningChase extends Buff {

		float direction = -1;

		int curCell = -1;
		int midCell = -1;
		int endCell = -1;

		ArrayList<Emitter> emitters = new ArrayList<>();

		@Override
		public boolean act() {

			PointF curPos;

			if (direction == -1){
				curPos = new PointF(Dungeon.level.cellToPoint(curCell));
				curPos.x += 0.5f;
				curPos.y += 0.5f;

				direction = PointF.angle(curPos, new PointF(Dungeon.level.cellToPoint(Dungeon.hero.pos)));

				//just started, so do initial bolt visuals

				int curCell = Dungeon.level.pointToCell(curPos.floor());
				CellEmitter.get(curCell).burst(SparkParticle.FACTORY, 10);

			} else {

				//TODO process damage for current cells here
				target.sprite.parent.addToFront(new Lightning(DungeonTilemap.tileCenterToWorld(curCell), DungeonTilemap.tileCenterToWorld(endCell), null));
				//TODO curCell damage
				if (!Dungeon.level.solid[midCell]) {
					//TODO midcell damage
					if (!Dungeon.level.solid[endCell]) {
						//TODO dmg
					} else {
						detach();
						return true;
					}
				} else {
					detach();
					return true;
				}

				curPos = new PointF(Dungeon.level.cellToPoint(endCell));
				curPos.x += 0.5f;
				curPos.y += 0.5f;

				float targetAngle = PointF.angle(curPos, new PointF(Dungeon.level.cellToPoint(Dungeon.hero.pos)));

				if (Math.abs(direction - targetAngle) > PointF.PI){
					if (direction > targetAngle){
						targetAngle += PointF.PI2;
					} else {
						targetAngle -= PointF.PI2;
					}
				}

				float maxMove = Random.Float(PointF.PI/10, PointF.PI/5);
				if (direction > targetAngle){
					direction -= Math.min(direction - targetAngle, maxMove);
				} else {
					direction += Math.min(targetAngle - direction, maxMove);
				}

				if (direction > PointF.PI) {
					direction -= PointF.PI2;
				} else if (direction < -PointF.PI){
					direction += PointF.PI2;
				}

			}

			PointF endPos = new PointF(curPos);
			endPos.offset(new PointF(curPos).polar(direction, 2));
			//always snap to the middle of the cell
			endPos.x = Math.round(2 * endPos.x) / 2f;
			endPos.y = Math.round(2 * endPos.y) / 2f;

			PointF midPos = PointF.inter(curPos, endPos, 0.5f);

			endCell = Dungeon.level.pointToCell(endPos.floor());
			midCell = Dungeon.level.pointToCell(midPos.floor());
			curCell = Dungeon.level.pointToCell(curPos.floor());

			updateFX();

			spend(TICK);
			return true;
		}

		private void updateFX(){
			for (Emitter e : emitters){
				e.on = false;
			}
			emitters.clear();

			if (endCell != -1 && !Dungeon.level.solid[endCell]){
				Emitter e = CellEmitter.get(endCell);
				e.pour(SparkParticle.STATIC, 0.1f);
				emitters.add(e);
			}

			if (midCell != -1 && !Dungeon.level.solid[midCell]){
				Emitter e = CellEmitter.get(midCell);
				e.pour(SparkParticle.STATIC, 0.1f);
				emitters.add(e);
			}

			if (curCell != -1 && !Dungeon.level.solid[curCell]){
				Emitter e = CellEmitter.get(curCell);
				e.pour(SparkParticle.STATIC, 0.1f);
				emitters.add(e);
			}

		}

		@Override
		public void fx(boolean on) {
			if (on){
				updateFX();
			} else {
				for (Emitter e : emitters){
					e.on = false;
				}
				emitters.clear();
			}
		}

		private static String DIRECTION = "direction";
		private static String CUR_CELL = "cur_cell";
		private static String MID_CELL = "mid_cell";
		private static String END_CELL = "end_cell";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(DIRECTION, direction);
			bundle.put(CUR_CELL, curCell);
			bundle.put(MID_CELL, midCell);
			bundle.put(END_CELL, endCell);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			direction = bundle.getInt(DIRECTION);
			curCell = bundle.getInt(CUR_CELL);
			midCell = bundle.getInt(MID_CELL);
			endCell = bundle.getInt(END_CELL);
		}
	}

}
