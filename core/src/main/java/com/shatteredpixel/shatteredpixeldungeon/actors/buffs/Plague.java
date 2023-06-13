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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.PlagueGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Plague extends Buff {
	
	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	public static final float DURATION	= 6f;

	protected float left;

	private static final String LEFT	= "left";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		left = bundle.getFloat( LEFT );
	}

	public void set( float duration ) {
		this.left = duration;
	}

	@Override
	public boolean act() {
		int Gas = Dungeon.hero.pointsInTalent(Talent.TOXIC_POWER);
		GameScene.add(Blob.seed(target.pos, 30 + ( 3 * Gas ), PlagueGas.class));
		if (target.buff(Talent.PlagueCounter.class) != null){
			target.buff(Talent.PlagueCounter.class).detach();
		}

		spend(TICK);
		left -= TICK;

		if (left <= 0){
			detach();
		}

		return true;
	}

	public void onAttackProc(Char enemy ){
		//25층에서 50% && 역병 카운터만큼 더 높아지는 확률
		if (enemy.buff(Talent.PlagueCounter.class) != null
				&& Random.Int(11) < Dungeon.scalingDepth()/5 + enemy.buff(Talent.PlagueCounter.class).count()){
			Buff.affect(enemy, Plague.class).set(Plague.DURATION);
			enemy.buff(Talent.PlagueCounter.class).detach();
		}
	}


	@Override
	public int icon() {
		return BuffIndicator.IMBUE;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0.8f, 1.5f, 0f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - left) / DURATION);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)left);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left));
	}

	{
		immunities.add( Talent.PlagueCounter.class );
		//역병 카운터가 역병 상태일때 안쌓이게
	}
}
