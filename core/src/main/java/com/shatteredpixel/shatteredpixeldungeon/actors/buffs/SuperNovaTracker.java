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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Halo;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class SuperNovaTracker extends Buff {

	public int pos;
	private int depth = Dungeon.depth;
	private int branch = Dungeon.branch;

	private int turnsLeft = 10;
	public boolean harmsAllies = true;

	private boolean[] fieldOfView;
	private NovaVFX halo;

	private static final int DIST = 8;

	@Override
	public boolean act() {

		if (branch != Dungeon.branch || depth != Dungeon.depth){
			spend(TICK);
			return true;
		}

		PointF p = DungeonTilemap.raisedTileCenterToWorld(pos);
		if (fieldOfView == null){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		if (halo == null){
			halo = new NovaVFX();
			halo.point(p.x, p.y);
			halo.hardlight(1, 1, 0f);
			GameScene.effect(halo);
		}

		if (turnsLeft > 0){

			FloatingText.show(p.x, p.y, pos, turnsLeft + "...", CharSprite.WARNING);
			halo.radius(5 + 2*(10-turnsLeft));
			halo.alpha(1.25f - 0.075f*turnsLeft);
			halo.point(p.x, p.y);
		}

		Point c = Dungeon.level.cellToPoint(pos);
		ShadowCaster.castShadow(c.x, c.y, Dungeon.level.width(), fieldOfView, Dungeon.level.solid, Math.min(DIST, 11-turnsLeft));

		if (turnsLeft <= 0){
			detach();
			halo.killAndErase();

			//if positive only, bombs do not harm allies
			if (!harmsAllies) {
				for (Char ch : Actor.chars()) {
					if (ch.alignment == Char.Alignment.ALLY) {
						Buff.affect(ch, NovaBombImmune.class, 0f);
					}
				}
			}

			Sample.INSTANCE.play(Assets.Sounds.BLAST);
			Sample.INSTANCE.playDelayed(Assets.Sounds.BLAST, 0.25f);
			Sample.INSTANCE.playDelayed(Assets.Sounds.BLAST, 0.5f);
			PixelScene.shake( 5, 2f );
			for (int i = 0; i < Dungeon.level.length(); i++){
				if (fieldOfView[i] && !Dungeon.level.solid[i]){
					new Bomb.ConjuredBomb().explode(i); //yes, a bomb at every cell
					//this means that something in the blast effectively takes:
					//9x bomb dmg when fully inside
					//6x when along straight edge
					//3x when outside straight edge
					Dungeon.level.destroy(i);
					if (Actor.findChar(i) == Dungeon.hero){
						GameScene.flash(0x80FFFFFF);
					}
				}
			}
			GameScene.updateMap();

		} else {
			for (int i = 0; i < Dungeon.level.length(); i++){
				if (fieldOfView[i]){
					target.sprite.parent.add(new TargetedCell(i, 0xFF0000));
				}
			}
		}

		turnsLeft--;
		spend(TICK);
		return true;

	}

	public static class NovaBombImmune extends FlavourBuff{
		{
			immunities.add(Bomb.ConjuredBomb.class);
		}
	}

	@Override
	public void fx(boolean on) {
		if (on && depth == Dungeon.depth && branch == Dungeon.branch
				&& (halo == null || halo.parent == null)){
			halo = new NovaVFX();
			PointF p = DungeonTilemap.raisedTileCenterToWorld(pos);
			halo.hardlight(1, 1, 0f);
			halo.radius(5 + 2*(10-turnsLeft));
			halo.alpha(1.25f - 0.075f*turnsLeft);
			halo.point(p.x, p.y);
			GameScene.effect(halo);
		}
		super.fx(on);
	}

	public static final String POS = "pos";
	public static final String DEPTH = "depth";
	public static final String BRANCH = "branch";

	public static final String LEFT = "left";
	public static final String HARMS_ALLIES = "harms_allies";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(POS, pos);
		bundle.put(DEPTH, depth);
		bundle.put(BRANCH, branch);
		bundle.put(LEFT, turnsLeft);
		bundle.put(HARMS_ALLIES, harmsAllies);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		pos = bundle.getInt(POS);
		depth = bundle.getInt(DEPTH);
		branch = bundle.getInt(BRANCH);
		turnsLeft = bundle.getInt(LEFT);
		harmsAllies = bundle.getBoolean(HARMS_ALLIES);
	}

	public class NovaVFX extends Halo {

		@Override
		public void update() {
			am = brightness + 0.1f*(float)Math.cos(20*Game.timeTotal);
			scale.set((radius + (float)Math.cos(20*Game.timeTotal))/RADIUS);
			PointF p = DungeonTilemap.raisedTileCenterToWorld(pos);
			point(p.x, p.y);
			super.update();
		}

	}

}
