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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMonkAbilities;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

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
		BuffIndicator.refreshHero();
	}

	//10 at base, 20 at level 30
	public int energyCap(){
		return Math.max(10, 5 + Dungeon.hero.lvl/2);
	}

	public void abilityUsed( MonkAbility abil ){
		energy -= abil.energyCost();
		cooldown = abil.cooldown();
		if (cooldown > 0 || energy < 1){
			ActionIndicator.clearAction(this);
		}
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
		GameScene.show(new WndMonkAbilities(this));
	}

	public static abstract class MonkAbility {

		public static MonkAbility[] abilities = new MonkAbility[]{
				new Flurry(),
				new Focus(),
				new Dash(),
				new DragonKick(),
				new Meditate()
		};

		public String name(){
			return Messages.get(this, "name");
		}

		public String desc(){
			return Messages.get(this, "desc");
		}

		public abstract int energyCost();
		public abstract int cooldown();

		public String targetingPrompt(){
			return null; //return a string if uses targeting
		}

		public abstract void doAbility(Hero hero, Integer target );

		public static class Flurry extends MonkAbility {

			@Override
			public int energyCost() {
				return 1;
			}

			@Override
			public int cooldown() {
				return 3;
			}

			@Override
			public String desc() {
				//double hero unarmed damage
				//TODO maybe two hits at regular unarmed damage instead?
				return Messages.get(this, "desc", 2, 2*(Dungeon.hero.STR()-8));
			}

			@Override
			public String targetingPrompt() {
				return "choose a target";
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				if (target == null || target == -1){
					return;
				}

				//TODO check for target viability

				//TODO add a buff that forces melee only (and no RoF!)

				//check for can attack

				//clear buff if can't

				//do attack logic
			}
		}

		public static class Focus extends MonkAbility {

			@Override
			public int energyCost() {
				return 2;
			}

			@Override
			public int cooldown() {
				return 4;
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				Buff.prolong(hero, FocusBuff.class, 30f);

				Buff.affect(hero, MonkEnergy.class).abilityUsed(this);
				hero.spendAndNext(1f);
			}

			public static class FocusBuff extends FlavourBuff {

				{
					type = buffType.POSITIVE;
					announced = true;
				}

				@Override
				public int icon() {
					return BuffIndicator.MIND_VISION;
				}

				@Override
				public void tintIcon(Image icon) {
					icon.hardlight(0.25f, 1.5f, 1f);
				}

				@Override
				public float iconFadePercent() {
					return Math.max(0, (30 - visualcooldown()) / 30);
				}
			}

		}

		public static class Dash extends MonkAbility {

			@Override
			public int energyCost() {
				return 3;
			}

			@Override
			public int cooldown() {
				return 3; //1 less turn as no time is spent dashing
			}

			@Override
			public String targetingPrompt() {
				return Messages.get(this, "prompt");
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				if (target == null || target == -1){
					return;
				}

				if (Dungeon.level.distance(hero.pos, target) > 3){
					GLog.w(Messages.get(this, "too_far"));
					return;
				}

				Ballistica dash = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);

				if (!dash.collisionPos.equals(target)
						|| Actor.findChar(target) != null
						|| (Dungeon.level.solid[target] && !Dungeon.level.passable[target])){
					GLog.w(Messages.get(this, "blocked"));
					return;
				}

				hero.busy();
				Sample.INSTANCE.play(Assets.Sounds.MISS);
				hero.sprite.emitter().start(Speck.factory(Speck.JET), 0.01f, Math.round(4 + 2*Dungeon.level.trueDistance(hero.pos, target)));
				hero.sprite.jump(hero.pos, target, 0, 0.1f, new Callback() {
					@Override
					public void call() {
						if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {
							Door.leave( hero.pos );
						}
						hero.pos = target;
						Dungeon.level.occupyCell(hero);
						hero.next();
					}
				});

				Buff.affect(hero, MonkEnergy.class).abilityUsed(this);
			}
		}

		public static class DragonKick extends MonkAbility {

			@Override
			public int energyCost() {
				return 4;
			}

			@Override
			public int cooldown() {
				return 3;
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				//TODO
			}
		}

		public static class Meditate extends MonkAbility {

			@Override
			public int energyCost() {
				return 5;
			}

			@Override
			public int cooldown() {
				return 3;
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				//TODO
			}
		}

	}
}
