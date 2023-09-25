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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalSpireSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CrystalSpire extends Mob {

	{
		//this translates to roughly 33/27/23/20/18/16 pickaxe hits at +0/1/2/3/4/5
		HP = HT = 300;
		spriteClass = CrystalSpireSprite.class;

		state = PASSIVE;

		alignment = Alignment.NEUTRAL;

		properties.add(Property.IMMOVABLE);
		properties.add(Property.MINIBOSS);
		properties.add(Property.INORGANIC);
	}

	//TODO this fight needs some mechanics and balance tuning now

	private float abilityCooldown;
	private static final int ABILITY_CD = 15;

	private ArrayList<ArrayList<Integer>> targetedCells = new ArrayList<>();

	@Override
	protected boolean act() {
		//char logic
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );

		throwItems();

		sprite.hideAlert();
		sprite.hideLost();

		//mob logic
		enemy = Dungeon.hero;

		//crystal can still track an invisible hero
		enemySeen = enemy.isAlive() && fieldOfView[enemy.pos];
		//end of char/mob logic

		if (hits < 3 || !enemySeen){
			spend(TICK);
			return true;
		} else {

			if (!targetedCells.isEmpty()){

				ArrayList<Integer> cellsToAttack = targetedCells.remove(0);

				for (int i : cellsToAttack){

					Char ch = Actor.findChar(i);
					if (ch instanceof CrystalGuardian || ch instanceof CrystalSpire){
						continue; //don't spawn crystals on these chars
					}

					Level.set(i, Terrain.MINE_CRYSTAL);
					GameScene.updateMap(i);

					Splash.at(i, 0xFFFFFF, 5);
				}

				for (int i : cellsToAttack){
					Char ch = Actor.findChar(i);

					if (ch != null && !(ch instanceof CrystalWisp || ch instanceof CrystalGuardian || ch instanceof CrystalSpire)){
						ch.damage(10, CrystalSpire.this);

						int movePos = i;
						for (int j : PathFinder.NEIGHBOURS8){
							if (!Dungeon.level.solid[i+j] && Actor.findChar(i+j) == null &&
									Dungeon.level.trueDistance(i+j, pos) > Dungeon.level.trueDistance(movePos, pos)){
								movePos = i+j;
							}
						}

						if (movePos != i){
							Actor.add(new Pushing(ch, i, movePos));
							ch.pos = movePos;
							Dungeon.level.occupyCell(ch);
						}
					}
				}

				//PixelScene.shake( 3, 0.7f );
				Sample.INSTANCE.play( Assets.Sounds.SHATTER );

				if (!targetedCells.isEmpty()){
					for (int i : targetedCells.get(0)){
						sprite.parent.add(new TargetedCell(i, 0xFF0000));
					}
				}

			}

			if (abilityCooldown <= 0){

				if (Random.Int(2) == 0) {
					diamondAOEAttack();
				} else {
					lineAttack();
				}

				for (int i : targetedCells.get(0)){
					sprite.parent.add(new TargetedCell(i, 0xFF0000));
				}

				abilityCooldown += ABILITY_CD;

				spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 3*TICK));
			} else {
				abilityCooldown -= 1;
				spend(TICK);
			}

		}

		return true;
	}

	private void diamondAOEAttack(){
		targetedCells.clear();

		ArrayList<Integer> aoeCells = new ArrayList<>();
		aoeCells.add(pos);
		aoeCells.addAll(spreadDiamondAOE(aoeCells));
		targetedCells.add(new ArrayList<>(aoeCells));

		if (HP < 2*HT/3f){
			aoeCells.addAll(spreadDiamondAOE(aoeCells));
			targetedCells.add(new ArrayList<>(aoeCells));
			if (HP < HT/3f) {
				aoeCells.addAll(spreadDiamondAOE(aoeCells));
				targetedCells.add(aoeCells);
			}
		}
	}

	private ArrayList<Integer> spreadDiamondAOE(ArrayList<Integer> currentCells){
		ArrayList<Integer> spreadCells = new ArrayList<>();
		for (int i : currentCells){
			for (int j : PathFinder.NEIGHBOURS4){
				if (!Dungeon.level.solid[i+j] && !spreadCells.contains(i+j) && !currentCells.contains(i+j)){
					spreadCells.add(i+j);
				}
			}
		}
		for (int i : spreadCells.toArray(new Integer[0])){
			for (int j : PathFinder.NEIGHBOURS4){
				if ((!Dungeon.level.solid[i+j] || Dungeon.level.map[i+j] == Terrain.MINE_CRYSTAL)
						&& !spreadCells.contains(i+j) && !currentCells.contains(i+j)){
					spreadCells.add(i+j);
				}
			}
		}
		return spreadCells;
	}

	private void lineAttack(){
		targetedCells.clear();

		ArrayList<Integer> lineCells = new ArrayList<>();
		Ballistica aim = new Ballistica(pos, Dungeon.hero.pos, Ballistica.WONT_STOP);
		for (int i : aim.subPath(1, 7)){
			if (!Dungeon.level.solid[i] || Dungeon.level.map[i] == Terrain.MINE_CRYSTAL){
				lineCells.add(i);
			} else {
				break;
			}
		}

		targetedCells.add(new ArrayList<>(lineCells));
		if (HP < 2*HT/3f){
			lineCells.addAll(spreadAOE(lineCells));
			targetedCells.add(new ArrayList<>(lineCells));
			if (HP < HT/3f) {;
				lineCells.addAll(spreadAOE(lineCells));
				targetedCells.add(lineCells);
			}
		}
	}

	private ArrayList<Integer> spreadAOE(ArrayList<Integer> currentCells){
		ArrayList<Integer> spreadCells = new ArrayList<>();
		for (int i : currentCells){
			for (int j : PathFinder.NEIGHBOURS8){
				if ((!Dungeon.level.solid[i+j] || Dungeon.level.map[i+j] == Terrain.MINE_CRYSTAL)
						&& !spreadCells.contains(i+j) && !currentCells.contains(i+j)){
					spreadCells.add(i+j);
				}
			}
		}
		return spreadCells;
	}

	@Override
	public void beckon(int cell) {
		//do nothing
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public void damage(int dmg, Object src) {
		if (!(src instanceof Pickaxe) ){
			dmg = 0;
		}
		super.damage(dmg, src);
	}

	@Override
	public boolean add( Buff buff ) {
		return false; //immune to all buffs and debuffs
	}

	int hits = 0;

	@Override
	public boolean interact(Char c) {
		if (c == Dungeon.hero){
			final Pickaxe p = Dungeon.hero.belongings.getItem(Pickaxe.class);

			if (p == null){
				//maybe a game log entry here?
				return true;
			}

			Dungeon.hero.sprite.attack(pos, new Callback() {
				@Override
				public void call() {
					//does its own special damage calculation that's only influenced by pickaxe level and augment
					//we pretend the spire is the owner here so that properties like hero str or or other equipment do not factor in
					int dmg = p.damageRoll(CrystalSpire.this);

					damage(dmg, p);
					abilityCooldown -= dmg/10f;
					sprite.bloodBurstA(Dungeon.hero.sprite.center(), dmg);
					sprite.flash();

					if (isAlive()) {
						Sample.INSTANCE.play(Assets.Sounds.SHATTER, 1f, Random.Float(1.15f, 1.25f));
						((CrystalSpireSprite) sprite).updateIdle();
					} else {
						Sample.INSTANCE.play(Assets.Sounds.SHATTER);
						Sample.INSTANCE.playDelayed(Assets.Sounds.ROCKS, 0.1f);
						PixelScene.shake( 3, 0.7f );
						Blacksmith.Quest.beatBoss();

						for (int i = 0; i < Dungeon.level.length(); i++){
							if (fieldOfView[i] && Dungeon.level.map[i] == Terrain.MINE_CRYSTAL){
								Level.set(i, Terrain.EMPTY);
								GameScene.updateMap(i);
								Splash.at(i, 0xFFFFFF, 5);
							}
						}

						for (Char ch : Actor.chars()){
							if (ch instanceof CrystalGuardian){
								ch.damage(ch.HT, this);
							}
						}

					}

					hits++;

					if (hits == 1){
						GLog.w(Messages.get(CrystalSpire.class, "warning"));
						PixelScene.shake( 1, 0.7f );
						Sample.INSTANCE.play( Assets.Sounds.MINE );
					} else if (hits >= 3) {

						if (hits == 3){
							Sample.INSTANCE.play( Assets.Sounds.ROCKS );
							PixelScene.shake( 3, 0.7f );
							GLog.n(Messages.get(CrystalSpire.class, "alert"));
							BossHealthBar.assignBoss(CrystalSpire.this);
						}

						for (Char ch : Actor.chars()) {
							if (ch instanceof CrystalWisp) {
								((CrystalWisp) ch).beckon(pos);
							} else if (ch instanceof CrystalGuardian) {
								((CrystalGuardian) ch).beckon(pos);
								if (((CrystalGuardian) ch).state != HUNTING) {
									((CrystalGuardian) ch).aggro(Dungeon.hero);
								}
							}
						}
					}

					Invisibility.dispel(Dungeon.hero);
					Dungeon.hero.spendAndNext(p.delayFactor(CrystalSpire.this));
				}
			});
			return false;

		}
		return true;
	}

	public CrystalSpire(){
		super();
		switch (Random.Int(3)){
			case 0: default:
				spriteClass = CrystalSpireSprite.Blue.class;
				break;
			case 1:
				spriteClass = CrystalSpireSprite.Green.class;
				break;
			case 2:
				spriteClass = CrystalSpireSprite.Red.class;
				break;
		}
	}

	@Override
	public float spawningWeight() {
		return 0;
	}

	public static final String SPRITE = "sprite";
	public static final String HITS = "hits";

	public static final String ABILITY_COOLDOWN = "ability_cooldown";
	public static final String TARGETED_CELLS = "targeted_cells";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SPRITE, spriteClass);
		bundle.put(HITS, hits);

		bundle.put(ABILITY_COOLDOWN, abilityCooldown);
		for (int i = 0; i < targetedCells.size(); i++){
			int[] bundleArr = new int[targetedCells.get(i).size()];
			for (int j = 0; j < targetedCells.get(i).size(); j++){
				bundleArr[j] = targetedCells.get(i).get(j);
			}
			bundle.put(TARGETED_CELLS+i, bundleArr);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		spriteClass = bundle.getClass(SPRITE);
		hits = bundle.getInt(HITS);

		abilityCooldown = bundle.getFloat(ABILITY_COOLDOWN);
		targetedCells.clear();
		int i = 0;
		while (bundle.contains(TARGETED_CELLS+i)){
			ArrayList<Integer> targets = new ArrayList<>();
			for (int j : bundle.getIntArray(TARGETED_CELLS+i)){
				targets.add(j);
			}
			targetedCells.add(targets);
			i++;
		}
	}

	{
		immunities.add( Blindness.class );

		immunities.add( Paralysis.class );
		immunities.add( Amok.class );
		immunities.add( Sleep.class );
		immunities.add( Terror.class );
		immunities.add( Dread.class );
		immunities.add( Vertigo.class );
	}

}
