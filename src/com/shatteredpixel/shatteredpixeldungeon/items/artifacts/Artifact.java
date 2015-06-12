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
package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Artifact extends KindofMisc {

	private static final float TIME_TO_EQUIP = 1f;

	private static final String TXT_TO_STRING		        = "%s";
	private static final String TXT_TO_STRING_CHARGE		= "%s (%d/%d)";
	private static final String TXT_TO_STRING_LVL	        = "%s%+d";
	private static final String TXT_TO_STRING_LVL_CHARGE	= "%s%+d (%d/%d)";

	protected Buff passiveBuff;
	protected Buff activeBuff;

	//level is used internally to track upgrades to artifacts, size/logic varies per artifact.
	//already inherited from item superclass
	//exp is used to count progress towards levels for some artifacts
	protected int exp = 0;
	//levelCap is the artifact's maximum level
	protected int levelCap = 0;

	//the current artifact charge
	protected int charge = 0;
	//the build towards next charge, usually rolls over at 1.
	//better to keep charge as an int and use a separate float than casting.
	protected float partialCharge = 0;
	//the maximum charge, varies per artifact, not all artifacts use this.
	protected int chargeCap = 0;

	//used by some artifacts to keep track of duration of effects or cooldowns to use.
	protected int cooldown = 0;


	public Artifact(){
		super();
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}

	@Override
	public boolean doEquip( Hero hero ) {

		if (hero.belongings.misc1 != null && hero.belongings.misc2 != null) {

			GLog.w("you can only wear 2 misc items at a time");
			return false;

		} else if ((hero.belongings.misc1 != null && hero.belongings.misc1.getClass() == this.getClass())
				|| (hero.belongings.misc2 != null && hero.belongings.misc2.getClass() == this.getClass())){

			GLog.w("you cannot wear two of the same artifact");
			return false;

		} else {

			if (hero.belongings.misc1 == null) {
				hero.belongings.misc1 = this;
			} else {
				hero.belongings.misc2 = this;
			}

			detach( hero.belongings.backpack );

			activate( hero );

			cursedKnown = true;
			identify();
			if (cursed) {
				equipCursed( hero );
				GLog.n( "the " + this.name + " painfully binds itself to you" );
			}

			hero.spendAndNext( TIME_TO_EQUIP );
			return true;

		}

	}

	public void activate( Char ch ) {
		passiveBuff = passiveBuff();
		passiveBuff.attachTo(ch);
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

		if (hero.belongings.misc1 == this) {
			hero.belongings.misc1 = null;
		} else {
			hero.belongings.misc2 = null;
		}

		passiveBuff.detach();
		passiveBuff = null;

		if (activeBuff != null){
			activeBuff.detach();
			activeBuff = null;
		}

		return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.misc1 == this || hero.belongings.misc2 == this;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public int visiblyUpgraded() {
		return ((level*10)/levelCap);
	}

	//transfers upgrades from another artifact, transfer level will equal the displayed level
	public void transferUpgrade(int transferLvl) {
		upgrade(Math.round((float)(transferLvl*levelCap)/10));
	}

	@Override
	public String info() {
		if (cursed && cursedKnown && !isEquipped( Dungeon.hero )) {

			return desc() + "\n\nYou can feel a malevolent magic lurking within the " + name() + ".";

		} else {

			return desc();

		}
	}

	@Override
	public String toString() {

		if (levelKnown && level/levelCap != 0) {
			if (chargeCap > 0) {
				return Utils.format( TXT_TO_STRING_LVL_CHARGE, name(), visiblyUpgraded(), charge, chargeCap );
			} else {
				return Utils.format( TXT_TO_STRING_LVL, name(), visiblyUpgraded() );
			}
		} else {
			if (chargeCap > 0) {
				return Utils.format( TXT_TO_STRING_CHARGE, name(), charge, chargeCap );
			} else {
				return Utils.format( TXT_TO_STRING, name() );
			}
		}
	}

	@Override
	public String status() {

		//display the current cooldown
		if (cooldown != 0)
			return Utils.format( "%d", cooldown );

		//display as percent
		if (chargeCap == 100)
			return Utils.format( "%d%%", charge );

		//display as #/#
		if (chargeCap > 0)
			return Utils.format( "%d/%d", charge, chargeCap );

		//if there's no cap -
		//- but there is charge anyway, display that charge
		if (charge != 0)
			return Utils.format( "%d", charge );

		//otherwise, if there's no charge, return null.
		return null;
	}

	//converts class names to be more concise and readable.
	protected String convertName(String className){
		//removes known redundant parts of names.
		className = className.replaceFirst("ScrollOf|PotionOf", "");

		//inserts a space infront of every uppercase character
		className = className.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");

		return className;
	};

	@Override
	public Item random() {
		if (Random.Float() < 0.3f) {
			cursed = true;
		}
		return this;
	}

	@Override
	public int price() {
		int price = 100;
		if (level > 0)
			price += 50*((level*10)/levelCap);
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}


	protected ArtifactBuff passiveBuff() {
		return null;
	}

	protected ArtifactBuff activeBuff() {return null; }

	public class ArtifactBuff extends Buff {

		public int level() {
			return level;
		}

		public boolean isCursed() {
			return cursed;
		}

	}

	private static final String IMAGE = "image";
	private static final String EXP = "exp";
	private static final String CHARGE = "charge";
	private static final String PARTIALCHARGE = "partialcharge";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( IMAGE, image );
		bundle.put( EXP , exp );
		bundle.put( CHARGE , charge );
		bundle.put( PARTIALCHARGE , partialCharge );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains( IMAGE )) image = bundle.getInt( IMAGE );
		exp = bundle.getInt( EXP );
		charge = bundle.getInt( CHARGE );
		partialCharge = bundle.getFloat( PARTIALCHARGE );
	}
}
