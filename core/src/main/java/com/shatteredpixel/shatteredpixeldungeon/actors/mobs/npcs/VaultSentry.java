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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WardSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class VaultSentry extends NPC {

	{
		spriteClass = WardSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	public float scanWidth;
	public float scanLength;
	public int[][] scanDirs;
	public int scanDirIdx;

	//defaults to scanning every turn
	public int curCooldown = 1;
	public int afterScanCooldown = 1;

	public int scansAfterCooldown = 1;
	private int scansMade = 0;

	//warning is unnecessary in some configurations where patterns are obvious.
	public boolean giveWarning = false;

	//scan sentries will collectively play a SFX at most every 80 ms
	private static long SFXLastPlayed = 0;

	@Override
	protected boolean act() {

		curCooldown--;

		if (curCooldown <= 0) {
			int[] scanDirsThisTurn = scanDirs[scanDirIdx];

			boolean visible = false;
			for (int scanDir : scanDirsThisTurn) {
				ConeAOE scan = new ConeAOE(
						new Ballistica(pos, scanDir, Ballistica.STOP_SOLID),
						scanLength,
						scanWidth,
						Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);

				for (int cell : scan.cells) {
					if (Actor.findChar(cell) == Dungeon.hero && Dungeon.hero.invisible == 0) {
						Dungeon.hero.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
						Sample.INSTANCE.play(Assets.Sounds.ZAP);
						SFXLastPlayed = ShatteredPixelDungeon.realTime;
					}
					if (Dungeon.level.heroFOV[cell]) {
						GameScene.checkedCell(cell, pos);
						visible = true;
					}
				}
			}

			if (visible && SFXLastPlayed+80 < ShatteredPixelDungeon.realTime) {
				Sample.INSTANCE.play(Assets.Sounds.ZAP, 0.5f);
				SFXLastPlayed = ShatteredPixelDungeon.realTime;
			}

			scanDirIdx++;
			if (scanDirIdx >= scanDirs.length) {
				scanDirIdx = 0;
			}

			scansMade++;
			if (scansMade < scansAfterCooldown){
				curCooldown = 1;
			} else {
				scansMade = 0;
				curCooldown = afterScanCooldown;
			}

		}

		if (curCooldown == 1 && giveWarning){
			int[] scanDirsNextTurn = scanDirs[scanDirIdx];
			for (int scanDir : scanDirsNextTurn) {
				ConeAOE scan = new ConeAOE(
						new Ballistica(pos, scanDir, Ballistica.STOP_SOLID),
						scanLength,
						scanWidth,
						Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);

				for (int cell : scan.cells) {
					if (Dungeon.level.heroFOV[cell]) {
						sprite.parent.add(new TargetedCell(cell, 0xFF0000));
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

	private static final String SCAN_WIDTH = "scan_width";
	private static final String SCAN_LENGTH = "scan_length";
	private static final String SCAN_DIR_IDX = "scan_dir_idx";
	private static final String SCAN_DIRS = "scan_dirs_";
	private static final String SCAN_DIRS_LEN = "scan_dirs_len";

	private static final String AFTER_SCAN_COOLDOWN = "after_shot_cooldown";
	private static final String CUR_COOLDOWN = "cur_cooldown";

	private static final String SCANS = "shots";
	private static final String SCANS_MADE = "shots_fired";

	private static final String WARNING = "warning";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SCAN_WIDTH, scanWidth);
		bundle.put(SCAN_LENGTH, scanLength);
		bundle.put(SCAN_DIR_IDX, scanDirIdx);
		bundle.put(SCAN_DIRS_LEN, scanDirs.length);
		for (int i = 0; i < scanDirs.length; i++){
			bundle.put(SCAN_DIRS+i, scanDirs[i]);
		}

		bundle.put(AFTER_SCAN_COOLDOWN, afterScanCooldown);
		bundle.put(CUR_COOLDOWN, curCooldown);
		bundle.put(SCANS, scansAfterCooldown);
		bundle.put(SCANS_MADE, scansMade);
		bundle.put(WARNING, giveWarning);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		scanWidth = bundle.getFloat(SCAN_WIDTH);
		scanLength = bundle.getFloat(SCAN_LENGTH);
		scanDirIdx = bundle.getInt(SCAN_DIR_IDX);
		scanDirs = new int[bundle.getInt(SCAN_DIRS_LEN)][];
		for (int i = 0; i < scanDirs.length; i++){
			scanDirs[i] = bundle.getIntArray(SCAN_DIRS+i);
		}

		//3.3.X saves
		if (bundle.contains(AFTER_SCAN_COOLDOWN)){
			afterScanCooldown = bundle.getInt(AFTER_SCAN_COOLDOWN);
			curCooldown = bundle.getInt(CUR_COOLDOWN);
			scansAfterCooldown = bundle.getInt(SCANS);
			scansMade = bundle.getInt(SCANS_MADE);
			giveWarning = bundle.getBoolean(WARNING);
		}
	}

	@Override
	public CharSprite sprite() {
		WardSprite sprite = (WardSprite) super.sprite();
		sprite.linkVisuals(this);
		return sprite;
	}

}
