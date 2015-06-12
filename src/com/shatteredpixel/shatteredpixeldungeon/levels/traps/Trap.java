/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Trap implements Bundlable {

	public String name;

	public int image;
	public int pos;

	public TrapSprite sprite;
	public boolean visible;

	public Trap set(int pos){
		this.pos = pos;
		return this;
	}

	public Trap reveal() {
		visible = true;
		if (sprite != null) {
			sprite.visible = true;
			sprite.alpha( 0 );
			sprite.parent.add( new AlphaTweener( sprite, 1, 0.6f));
		}
		return this;
	}

	public Trap hide() {
		visible = false;
		if (sprite != null)
			sprite.visible = false;
		return this;
	}

	public void trigger() {
		if (Dungeon.visible[pos]){
			Sample.INSTANCE.play(Assets.SND_TRAP);
		}
		disarm();
		activate();
	}

	public abstract void activate();

	protected void disarm(){
		Dungeon.level.disarmTrap(pos);
		if (sprite != null) sprite.kill();
	}

	private static final String POS	= "pos";
	private static final String VISIBLE	= "visible";

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos = bundle.getInt( POS );
		visible = bundle.getBoolean( VISIBLE );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( POS, pos );
		bundle.put( VISIBLE, visible );
	}

	public String desc() {
		return "Stepping onto a hidden pressure plate will activate the trap.";
	}
}
