package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Dread extends Buff {

	protected int left = (int)DURATION;
	public int object = 0;

	public static final float DURATION = 20f;

	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	//dread overrides terror
	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)){
			Buff.detach( target, Terror.class );
			return true;
		} else {
			return false;
		}
	}

	{
		immunities.add(Terror.class);
	}

	@Override
	public boolean act() {

		if (!Dungeon.level.heroFOV[target.pos]
				&& Dungeon.level.distance(target.pos, Dungeon.hero.pos) >= 6) {
			Actor.remove( target );
			target.sprite.killAndErase();
			Dungeon.level.mobs.remove(target);
		} else {
			left--;
			if (left <= 0){
				detach();
			}
		}

		spend(TICK);
		return true;
	}

	private static final String LEFT	= "left";
	private static final String OBJECT    = "object";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, left);
		bundle.put(OBJECT, object);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		object = bundle.getInt( OBJECT );
		left = bundle.getInt( LEFT );
	}

	@Override
	public int icon() {
		return BuffIndicator.TERROR;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - left) / DURATION);
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1, 0, 0);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", left);
	}

	public void recover() {
		left -= 5;
		if (left <= 0){
			detach();
		}
	}

}
