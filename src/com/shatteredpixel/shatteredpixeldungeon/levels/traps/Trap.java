package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrapSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Trap implements Bundlable {

	public String name;

	public int image;
	public int pos;

	public TrapSprite sprite;
	public boolean visible;

	private static final String POS	= "pos";

	public Trap set(int pos){
		this.pos = pos;
		return this;
	}

	public void reveal() {
		visible = true;
		if (sprite != null)
			sprite.visible = true;
	}

	public void hide() {
		visible = false;
		if (sprite != null)
			sprite.visible = false;
	}

	public void trigger() {
		if (Dungeon.visible[pos]){
			Sample.INSTANCE.play(Assets.SND_TRAP);
		}
		activate();
		disarm();
	}

	public abstract void activate();

	protected void disarm(){
		//Dungeon.level.traps.delete( pos );
		Level.set(pos, Terrain.INACTIVE_TRAP);
		sprite.kill();
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos = bundle.getInt( POS );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( POS, pos );
	}

	public String desc() {
		return "";
	}
}
