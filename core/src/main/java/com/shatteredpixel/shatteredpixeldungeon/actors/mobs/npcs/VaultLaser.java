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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SentrySprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class VaultLaser extends NPC {

	{
		spriteClass = SentrySprite.VaultLaser.class;

		properties.add(Char.Property.IMMOVABLE);
	}

	//turn into int[][] if we ever want one laser to fire multiple shots per turn
	public int[] laserDirs;
	public int laserDirIdx;

	public int curCooldown;
	public int afterShotCooldown;

	public int shotsAfterCooldown = 1;
	private int shotsFired = 0;

	//warning is unnecessary in some configurations where patterns are obvious.
	public boolean giveWarning = true;

	//laser sentries will collectively play a SFX at most every 80 ms
	private static long SFXLastPlayed = 0;

	//to avoid many sentries calling setSeen every turn
	private boolean seen = false;

	@Override
	protected boolean act() {
		if (!seen && Dungeon.level.heroFOV[pos]){
			Bestiary.setSeen(getClass());
			seen = true;
		}

		curCooldown--;
		if (curCooldown <= 0){

			Ballistica beam = new Ballistica(pos, laserDirs[laserDirIdx], Ballistica.STOP_SOLID);
			boolean visible = false;
			boolean observe = false;
			for (int cell : beam.subPath(1, beam.dist)){
				if (Dungeon.level.heroFOV[cell]){
					visible = true;
				}
				if (Dungeon.level.flamable[cell]){
					Dungeon.level.destroy( cell );
					observe = true;
					GameScene.updateMap( cell );
				}
				Char ch = Actor.findChar(cell);
				if (ch != null && ch.alignment == Alignment.ALLY){
					ch.damage(Random.NormalIntRange(10, 20), new Eye.DeathGaze());
					if (ch.sprite.visible){
						ch.sprite.flash();
						CellEmitter.center( pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
					}
					if (ch == Dungeon.hero){
						Sample.INSTANCE.play( Assets.Sounds.RAY );
						SFXLastPlayed = ShatteredPixelDungeon.realTime;
						if (Imp.Quest.hazardFreebies > 0){
							Imp.Quest.hazardFreebies--;
						} else {
							Statistics.questScores[3] -= 100;
						}
						if (!ch.isAlive()){
							Badges.validateDeathFromEnemyMagic();
							Dungeon.fail( this );
							GLog.n( Messages.get(this, "ondeath") );
						}
					}
				}
			}
			if (visible){
				sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
				if (SFXLastPlayed+80 < ShatteredPixelDungeon.realTime) {
					Sample.INSTANCE.play(Assets.Sounds.RAY, 0.5f);
					SFXLastPlayed = ShatteredPixelDungeon.realTime;
				}
			}

			if (observe){
				Dungeon.observe();
			}

			laserDirIdx++;
			if (laserDirIdx >= laserDirs.length){
				laserDirIdx = 0;
			}

			shotsFired++;
			if (shotsFired < shotsAfterCooldown){
				curCooldown = 1;
			} else {
				shotsFired = 0;
				curCooldown = afterShotCooldown;
			}

		}

		if (curCooldown == 1 && giveWarning){

			Ballistica nextBeam = new Ballistica(pos, laserDirs[laserDirIdx], Ballistica.STOP_SOLID);
			boolean visible = false;
			for (int cell : nextBeam.subPath(1, nextBeam.dist)){
				if (Dungeon.level.heroFOV[cell]) {
					visible = true;
					break;
				}
			}
			if (visible){
				for (int cell : nextBeam.subPath(1, nextBeam.dist)) {
					GameScene.targetedCell(cell, 1);
					if (Actor.findChar(cell) == Dungeon.hero){
						//mainly to prevent the hero from auto-picking up items when targeted
						Dungeon.hero.interrupt();
					}
				}
			}

		}

		throwItems();

		spend(TICK);
		return true;
	}

	@Override
	public boolean isImmune(Class effect) {
		return true;
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return true;
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public boolean interact(Char c) {
		return true;
	}

	private static final String LASER_DIRS = "laser_dirs";
	private static final String LASER_DIR_IDX = "laser_dir_idx";

	private static final String AFTER_SHOT_COOLDOWN = "after_shot_cooldown";
	private static final String CUR_COOLDOWN = "cur_cooldown";

	private static final String SHOTS = "shots";
	private static final String SHOTS_FIRED = "shots_fired";

	private static final String WARNING = "warning";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LASER_DIRS, laserDirs);
		bundle.put(LASER_DIR_IDX, laserDirIdx);
		bundle.put(AFTER_SHOT_COOLDOWN, afterShotCooldown);
		bundle.put(CUR_COOLDOWN, curCooldown);
		bundle.put(SHOTS, shotsAfterCooldown);
		bundle.put(SHOTS_FIRED, shotsFired);
		bundle.put(WARNING, giveWarning);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		laserDirs = bundle.getIntArray(LASER_DIRS);
		laserDirIdx = bundle.getInt(LASER_DIR_IDX);
		if (bundle.contains(AFTER_SHOT_COOLDOWN)){
			afterShotCooldown = bundle.getInt(AFTER_SHOT_COOLDOWN);
			curCooldown = bundle.getInt(CUR_COOLDOWN);
			shotsAfterCooldown = bundle.getInt(SHOTS);
			shotsFired = bundle.getInt(SHOTS_FIRED);
			giveWarning = bundle.getBoolean(WARNING);
		//3.3.X saves
		} else {
			afterShotCooldown = Random.IntRange(3, 7);
			curCooldown = Random.IntRange(1, afterShotCooldown);
		}
	}

}
