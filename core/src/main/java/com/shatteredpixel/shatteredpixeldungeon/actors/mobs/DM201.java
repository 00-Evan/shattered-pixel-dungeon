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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM201Sprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DM201 extends DM200 {

	{
		spriteClass = DM201Sprite.class;

		HP = HT = 120;
		MIN_ATT=15;
		MAX_ATT=25;

		properties.add(Property.IMMOVABLE);

		HUNTING = new Mob.Hunting();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( MIN_ATT, MAX_ATT );
	}

	private boolean threatened = false;

	@Override
	protected boolean act() {

		//in case DM-201 hasn't been able to act yet
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}

		if (paralysed <= 0 && state == HUNTING && enemy != null && enemySeen
				&& threatened && !Dungeon.level.adjacent(pos, enemy.pos) && fieldOfView[enemy.pos]){
			enemySeen = enemy.isAlive() && fieldOfView[enemy.pos] && enemy.invisible <= 0;
			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}
		}
		return super.act();
	}

	@Override
	public void damage(int dmg, Object src) {
		if ((src instanceof Char && !Dungeon.level.adjacent(pos, ((Char)src).pos))
				|| enemy == null || !Dungeon.level.adjacent(pos, enemy.pos)){
			threatened = true;
		}
		super.damage(dmg, src);
	}

	public void onZapComplete(){
		zap();
		next();
	}

	private void zap( ){
		threatened = false;
		spend(TICK);

		GameScene.add(Blob.seed(enemy.pos, 15, CorrosiveGas.class).setStrength(8));
		for (int i : PathFinder.NEIGHBOURS8){
			if (!Dungeon.level.solid[enemy.pos+i]) {
				GameScene.add(Blob.seed(enemy.pos + i, 5, CorrosiveGas.class).setStrength(8));
			}
		}

	}

	@Override
	protected boolean getCloser(int target) {
		return true;
	}

	@Override
	protected boolean getFurther(int target) {
		return true;
	}

	@Override
	public void rollToDropLoot() {
		if (Dungeon.hero.lvl > maxLvl + 2) return;

		super.rollToDropLoot();

		int ofs;
		do {
			ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (Dungeon.level.solid[pos + ofs] && !Dungeon.level.passable[pos + ofs]);
		Dungeon.level.drop( new MetalShard(), pos + ofs ).sprite.drop( pos );
	}

}
