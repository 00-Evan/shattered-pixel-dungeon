package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Greatsword extends MeleeWeapon {

	{
		image = ItemSpriteSheet.GREATSWORD;
	}

	public Greatsword() {
		super( 5, 1f, 1f );
	}

}
