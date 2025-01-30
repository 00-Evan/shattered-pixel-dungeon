/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class WallOfLight extends TargetedClericSpell {

	public static WallOfLight INSTANCE = new WallOfLight();

	@Override
	public int icon() {
		return HeroIcon.WALL_OF_LIGHT;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 1 + 2*Dungeon.hero.pointsInTalent(Talent.WALL_OF_LIGHT)) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public int targetingFlags(){
		return -1; //auto-targeting behaviour is often wrong, so we don't use it
	}

	@Override
	public float chargeUse(Hero hero) {
		if (Dungeon.level.blobs.get(LightWall.class) != null
			&& Dungeon.level.blobs.get(LightWall.class).volume > 0){
			return 0f;
		}
		return 3f;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero) && hero.hasTalent(Talent.WALL_OF_LIGHT);
	}

	@Override
	public void onCast(HolyTome tome, Hero hero) {
		if (Dungeon.level.blobs.get(LightWall.class) != null
				&& Dungeon.level.blobs.get(LightWall.class).volume > 0){
			Dungeon.level.blobs.get(LightWall.class).fullyClear();
			GLog.i(Messages.get(this, "early_end"));
			return;
		}
		super.onCast(tome, hero);
	}

	@Override
	protected void onTargetSelected(HolyTome tome, Hero hero, Integer target) {
		if (target == null){
			return;
		}

		if (target == hero.pos){
			GLog.w(Messages.get(this, "invalid_target"));
			return;
		}

		int closest = hero.pos;
		int closestIdx = -1;

		for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
			int ofs = PathFinder.CIRCLE8[i];
			if (Dungeon.level.trueDistance(target, hero.pos+ofs) < Dungeon.level.trueDistance(target, closest)){
				closest = hero.pos+ofs;
				closestIdx = i;
			}
		}

		int leftDirX = 0;
		int leftDirY = 0;

		int rightDirX = 0;
		int rightDirY = 0;

		int steps = Dungeon.hero.pointsInTalent(Talent.WALL_OF_LIGHT);

		switch (closestIdx){
			case 0: //top left
				leftDirX = -1;
				leftDirY = 1;
				rightDirX = 1;
				rightDirY = -1;
				break;
			case 1: //top
				leftDirX = -1;
				rightDirX = 1;
				leftDirY = rightDirY = 0;
				break;
			case 2: //top right (left and right DIR are purposefully inverted)
				leftDirX = 1;
				leftDirY = 1;
				rightDirX = -1;
				rightDirY = -1;
				break;
			case 3: //right
				leftDirY = -1;
				rightDirY = 1;
				leftDirX = rightDirX = 0;
				break;
			case 4: //bottom right (left and right DIR are purposefully inverted)
				leftDirX = 1;
				leftDirY = -1;
				rightDirX = -1;
				rightDirY = 1;
				break;
			case 5: //bottom
				leftDirX = 1;
				rightDirX = -1;
				leftDirY = rightDirY = 0;
				break;
			case 6: //bottom left
				leftDirX = -1;
				leftDirY = -1;
				rightDirX = 1;
				rightDirY = 1;
				break;
			case 7: //left
				leftDirY = -1;
				rightDirY = 1;
				leftDirX = rightDirX = 0;
				break;
		}

		if (Dungeon.level.blobs.get(LightWall.class) != null){
			Dungeon.level.blobs.get(LightWall.class).fullyClear();
		}

		boolean placedWall = false;

		int knockBackDir = PathFinder.CIRCLE8[closestIdx];

		//if all 3 tiles infront of Paladin are blocked, assume cast was in error and cancel
		if (Dungeon.level.solid[closest]
				&& Dungeon.level.solid[hero.pos + PathFinder.CIRCLE8[(closestIdx+1)%8]]
				&& Dungeon.level.solid[hero.pos + PathFinder.CIRCLE8[(closestIdx+7)%8]]){
			GLog.w(Messages.get(this, "invalid_target"));
			return;
		}

		//process early so that cost is calculated before walls are added
		onSpellCast(tome, hero);

		placeWall(closest, knockBackDir);

		int leftPos = closest;
		int rightPos = closest;

		//iterate to the left and right, placing walls as we go
		for (int i = 0; i < steps; i++) {
			if (leftDirY != 0) {
				leftPos += leftDirY * Dungeon.level.width();
				if (!Dungeon.level.insideMap(leftPos)){
					break;
				}
				placeWall(leftPos, knockBackDir);
			}
			if (leftDirX != 0) {
				leftPos += leftDirX;
				if (!Dungeon.level.insideMap(leftPos)){
					break;
				}
				placeWall(leftPos, knockBackDir);
			}
		}
		for (int i = 0; i < steps; i++) {
			if (rightDirX != 0) {
				rightPos += rightDirX;
				if (!Dungeon.level.insideMap(rightPos)){
					break;
				}
				placeWall(rightPos, knockBackDir);
			}
			if (rightDirY != 0) {
				rightPos += rightDirY * Dungeon.level.width();
				if (!Dungeon.level.insideMap(rightPos)){
					break;
				}
				placeWall(rightPos, knockBackDir);
			}
		}

		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

		hero.sprite.zap(closest);
		Dungeon.hero.spendAndNext(1f);
	}

	private void placeWall( int pos, int knockbackDIR){
		if (!Dungeon.level.solid[pos]) {
			GameScene.add(Blob.seed(pos, 20, LightWall.class));

			Char ch = Actor.findChar(pos);
			if (ch != null && ch.alignment == Char.Alignment.ENEMY){
				WandOfBlastWave.throwChar(ch, new Ballistica(pos, pos+knockbackDIR, Ballistica.PROJECTILE), 1, false, false, WallOfLight.INSTANCE);
				Buff.affect(ch, Paralysis.class, ch.cooldown());
			}
		}
	}

	public static class LightWall extends Blob {

		@Override
		protected void evolve() {

			int cell;

			Level l = Dungeon.level;
			for (int i = area.left; i < area.right; i++){
				for (int j = area.top; j < area.bottom; j++){
					cell = i + j*l.width();
					off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

					volume += off[cell];

					l.solid[cell] = off[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.SOLID) != 0;
					l.passable[cell] = off[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.PASSABLE) != 0;
					l.avoid[cell] = off[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.AVOID) != 0;
				}
			}
		}

		@Override
		public void seed(Level level, int cell, int amount) {
			super.seed(level, cell, amount);
			level.solid[cell] = cur[cell] > 0 || (Terrain.flags[level.map[cell]] & Terrain.SOLID) != 0;
			level.passable[cell] = cur[cell] == 0 && (Terrain.flags[level.map[cell]] & Terrain.PASSABLE) != 0;
			level.avoid[cell] = cur[cell] == 0 && (Terrain.flags[level.map[cell]] & Terrain.AVOID) != 0;
		}

		@Override
		public void clear(int cell) {
			super.clear(cell);
			if (cur == null) return;
			Level l = Dungeon.level;
			l.solid[cell] = cur[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.SOLID) != 0;
			l.passable[cell] = cur[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.PASSABLE) != 0;
			l.avoid[cell] = cur[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.AVOID) != 0;
		}

		@Override
		public void fullyClear() {
			super.fullyClear();
			Dungeon.level.buildFlagMaps();
		}

		@Override
		public void onBuildFlagMaps(Level l) {
			if (volume > 0){
				for (int i=0; i < l.length(); i++) {
					l.solid[i] = l.solid[i] || cur[i] > 0;
					l.passable[i] = l.passable[i] && cur[i] == 0;
					l.avoid[i] = l.avoid[i] && cur[i] == 0;
				}
			}
		}

		@Override
		public void use(BlobEmitter emitter) {
			super.use( emitter );
			emitter.pour( MagicMissile.WhiteParticle.WALL, 0.02f );
		}

		@Override
		public String tileDesc() {
			return Messages.get(this, "desc");
		}

	}

}
