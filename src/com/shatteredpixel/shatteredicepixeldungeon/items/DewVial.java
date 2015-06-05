/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.items;

import java.util.ArrayList;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.HeroClass;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredicepixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;

public class DewVial extends Item {

	private static final int MAX_VOLUME	= 10;

	private static final String AC_DRINK	= "DRINK";

	private static final float TIME_TO_DRINK = 1f;

	private static final String TXT_VALUE	= "%+dHP";
	private static final String TXT_STATUS	= "%d/%d";

	private static final String TXT_AUTO_DRINK	= "The dew vial was emptied to heal your wounds.";
	private static final String TXT_COLLECTED	= "You collected a dewdrop into your dew vial.";
	private static final String TXT_FULL		= "Your dew vial is full!";
	private static final String TXT_EMPTY		= "Your dew vial is empty!";

	{
		name = "dew vial";
		image = ItemSpriteSheet.VIAL;

		defaultAction = AC_DRINK;

		unique = true;
	}

	private int volume = 0;

	private static final String VOLUME	= "volume";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( VOLUME, volume );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		volume	= bundle.getInt( VOLUME );
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (volume > 0) {
			actions.add( AC_DRINK );
		}
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {
		if (action.equals( AC_DRINK )) {

			if (volume > 0) {

                int value = 1 + (Dungeon.depth - 1) / 5;
                if (hero.heroClass == HeroClass.HUNTRESS) {
                    value++;
                }
                value *= volume;
                value = (int)Math.max(volume*volume*.01*hero.HT, value);
				int effect = Math.min( hero.HT - hero.HP, value );
				if (effect > 0) {
					hero.HP += effect;
					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), volume > 5 ? 2 : 1 );
					hero.sprite.showStatus( CharSprite.POSITIVE, TXT_VALUE, effect );
				}

                volume = 0;

				hero.spend( TIME_TO_DRINK );
				hero.busy();

				Sample.INSTANCE.play( Assets.SND_DRINK );
				hero.sprite.operate( hero.pos );

				updateQuickslot();


			} else {
				GLog.w( TXT_EMPTY );
			}

		} else {

			super.execute( hero, action );

		}
	}

    public void empty() {volume = 0; updateQuickslot();}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	public boolean isFull() {
		return volume >= MAX_VOLUME;
	}

	public void collectDew( Dewdrop dew ) {

		GLog.i( TXT_COLLECTED );
		volume += dew.quantity;
		if (volume >= MAX_VOLUME) {
			volume = MAX_VOLUME;
			GLog.p( TXT_FULL );
		}

		updateQuickslot();
	}

	public void fill() {
		volume = MAX_VOLUME;
		updateQuickslot();
	}

	//removed as people need a bigger distinction to realize the dew vial doesn't revive.
    /*
	private static final Glowing WHITE = new Glowing( 0xFFFFCC );

	@Override
	public Glowing glowing() {
		return isFull() ? WHITE : null;
	}*/

	@Override
	public String status() {
		return Utils.format( TXT_STATUS, volume, MAX_VOLUME );
	}

	@Override
	public String info() {
		return
			"You can store excess dew in this tiny vessel for drinking it later. " +
			"The more full the vial is, the more each dew drop will heal you. " +
            "A full vial is as strong as a potion of healing." +
            "\n\nVials like this one used to be imbued with revival magic, " +
            "but that power has faded. There still seems to be some residual power " +
            "left, perhaps a full vial can bless another revival item.";
	}
	
	@Override
	public String toString() {
		return super.toString() + " (" + status() +  ")" ;
	}
}
