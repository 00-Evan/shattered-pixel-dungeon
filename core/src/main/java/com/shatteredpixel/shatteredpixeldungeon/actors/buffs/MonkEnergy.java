/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class MonkEnergy extends Buff implements ActionIndicator.Action {

	{
		type = buffType.POSITIVE;
		revivePersists = true;
	}

	public float energy;
	public int cooldown;

	@Override
	public int icon() {
		return BuffIndicator.MONK_ENERGY;
	}

	@Override
	public void tintIcon(Image icon) {
		if (cooldown > 0){
			icon.hardlight(0.33f, 0.33f, 1f);
		} else {
			icon.resetColor();
		}
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (energyCap() - energy)/ energyCap());
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)energy);
	}

	@Override
	public boolean act() {
		if (cooldown > 0){
			cooldown--;
			if (cooldown == 0 && energy >= 1){
				ActionIndicator.setAction(this);
			}
			BuffIndicator.refreshHero();
		}

		spend(TICK);
		return true;
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc", (int)energy, energyCap());
		if (cooldown > 0){
			desc += "\n\n" + Messages.get(this, "desc_cooldown", cooldown);
		}
		return desc;
	}

	public static String ENERGY = "energy";
	public static String COOLDOWN = "cooldown";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ENERGY, energy);
		bundle.put(COOLDOWN, cooldown);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		energy = bundle.getFloat(ENERGY);
		cooldown = bundle.getInt(COOLDOWN);

		if (energy >= 1 && cooldown == 0){
			ActionIndicator.setAction(this);
		}
	}

	public void gainEnergy(Mob enemy ){
		if (target.buff(LockedFloor.class) != null && !target.buff(LockedFloor.class).regenOn()){
			return; //to prevent farming boss minions
		}

		//bosses and minibosses give extra energy, certain enemies give half, otherwise give 1
		if (Char.hasProp(enemy, Char.Property.BOSS))            energy += 5;
		else if (Char.hasProp(enemy, Char.Property.MINIBOSS))   energy += 3;
		else if (enemy instanceof Ghoul)                        energy += 0.5f;
		else if (enemy instanceof RipperDemon)                  energy += 0.5f;
		else if (enemy instanceof YogDzewa.Larva)               energy += 0.5f;
		else if (enemy instanceof Wraith)                       energy += 0.5f;
		else                                                    energy += 1;

		energy = Math.min(energy, energyCap());

		if (energy > 0 && cooldown == 0){
			ActionIndicator.setAction(this);
		}
	}

	//10 at base, 20 at level 30
	public int energyCap(){
		return Math.max(10, 5 + Dungeon.hero.lvl/2);
	}

	@Override
	public String actionName() {
		return "TODO";
	}

	@Override
	public Image actionIcon() {
		return new HeroIcon(HeroSubClass.MONK);
	}

	@Override
	public void doAction() {
		energy -= 1;
		cooldown = 3;
		ActionIndicator.clearAction(this);
		BuffIndicator.refreshHero();
		//GameScene.show(new WndMonk(this));
	}

	public enum MonkAbility {
		FLURRY_OF_BLOWS, //1
		CLEANSE, //Just part of meditate?
		DASH, //2 or 3 energy?
		UPPERCUT, //2
		FOCUS, //3
		DRAGON_KICK, //5
		MEDITATE //5
	}
}
