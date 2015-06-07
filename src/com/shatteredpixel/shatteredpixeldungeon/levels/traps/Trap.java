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
