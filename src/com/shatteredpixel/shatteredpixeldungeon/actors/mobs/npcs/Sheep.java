package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.sprites.SheepSprite;
import com.watabou.utils.Random;

public class Sheep extends NPC {

	private static final String[] QUOTES = {"Baa!", "Baa?", "Baa.", "Baa..."};

	{
		name = "sheep";
		spriteClass = SheepSprite.class;
	}

	public float lifespan;

	private boolean initialized = false;

	@Override
	protected boolean act() {
		if (initialized) {
			HP = 0;

			destroy();
			sprite.die();

		} else {
			initialized = true;
			spend( lifespan + Random.Float(2) );
		}
		return true;
	}

	@Override
	public void damage( int dmg, Object src ) {
	}

	@Override
	public String description() {
		return
				"This is a magic sheep. What's so magical about it? You can't kill it. " +
						"It will stand there until it magcially fades away, all the while chewing cud with a blank stare.";
	}

	@Override
	public void interact() {
		yell( Random.element( QUOTES ) );
	}
}