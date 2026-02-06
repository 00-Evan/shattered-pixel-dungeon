package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WardSprite;
import com.watabou.utils.Bundle;

public class VaultSentry extends NPC {

	{
		spriteClass = WardSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	public float scanWidth;
	public float scanLength;
	public int[][] scanDirs;
	public int scanDirIdx;

	@Override
	protected boolean act() {

		int[] scanDirsThisTurn = scanDirs[scanDirIdx];
		scanDirIdx++;
		if (scanDirIdx >= scanDirs.length){
			scanDirIdx = 0;
		}

		for (int scanDir : scanDirsThisTurn) {
			ConeAOE scan = new ConeAOE(
					new Ballistica(pos, scanDir, Ballistica.STOP_SOLID),
					scanLength,
					scanWidth,
					Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);

			for (int cell : scan.cells) {
				if (Actor.findChar(cell) == Dungeon.hero){
					Dungeon.hero.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
				}
				if (Dungeon.level.heroFOV[cell]) {
					GameScene.effect(new CheckedCell(cell, pos));
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
	}

	@Override
	public CharSprite sprite() {
		WardSprite sprite = (WardSprite) super.sprite();
		sprite.linkVisuals(this);
		return sprite;
	}

}
