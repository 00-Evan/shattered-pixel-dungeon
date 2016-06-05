package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Flow extends Armor.Glyph {

	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x0000FF );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, see hero.speed for effect.
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLUE;
	}

}
