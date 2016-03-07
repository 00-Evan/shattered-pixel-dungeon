package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;

//TODO: add actual item properties here
public class BrokenSigil {

	public static class SigilShield extends Buff {

		private Armor armor;
		private float partialShield;

		@Override
		public boolean act() {
			if (armor == null) detach();
			else if (armor.isEquipped((Hero)target)) {
				//1 + half of your DR, rounded up.
				int maxShield = (int)(armor.DR()/2f + 1.5f);
				if (target.SHLD < maxShield){
					partialShield += (maxShield - target.SHLD)/50f;
				}
			}
			while (partialShield >= 1){
				target.SHLD++;
				partialShield--;
			}
			spend(TICK);
			return true;
		}

		public void setArmor(Armor arm){
			armor = arm;
		}
	}
}
