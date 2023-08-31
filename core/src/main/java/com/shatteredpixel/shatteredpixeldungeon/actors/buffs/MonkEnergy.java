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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMonkAbilities;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
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

	private static final float MAX_COOLDOWN = 5;

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
		return Math.max(0, cooldown/MAX_COOLDOWN);
	}

	@Override
	public String iconTextDisplay() {
		if (cooldown > 0){
			return Integer.toString(cooldown);
		} else {
			return "";
		}
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
		if (target == null) return;

		if (!Regeneration.regenOn()){
			return; //to prevent farming boss minions
		}

		float energyGain;

		//bosses and minibosses give extra energy, certain enemies give half, otherwise give 1
		if (Char.hasProp(enemy, Char.Property.BOSS))            energyGain = 5;
		else if (Char.hasProp(enemy, Char.Property.MINIBOSS))   energyGain = 3;
		else if (enemy instanceof Ghoul)                        energyGain = 0.5f;
		else if (enemy instanceof RipperDemon)                  energyGain = 0.5f;
		else if (enemy instanceof YogDzewa.Larva)               energyGain = 0.5f;
		else if (enemy instanceof Wraith)                       energyGain = 0.5f;
		else                                                    energyGain = 1;

		float enGainMulti = 1f;
		if (target instanceof Hero) {
			Hero hero = (Hero) target;
			if (hero.hasTalent(Talent.UNENCUMBERED_SPIRIT)) {
				int points = hero.pointsInTalent(Talent.UNENCUMBERED_SPIRIT);

				if (hero.belongings.armor() != null){
					if (hero.belongings.armor().tier <= 1 && points >= 3){
						enGainMulti += 1.20f;
					} else if (hero.belongings.armor().tier <= 2 && points >= 2){
						enGainMulti += 0.80f;
					} else if (hero.belongings.armor().tier <= 3 && points >= 1){
						enGainMulti += 0.40f;
					}
				}

				if (hero.belongings.weapon() instanceof MeleeWeapon
						&& hero.buff(RingOfForce.BrawlersStance.class) == null){
					if (((MeleeWeapon) hero.belongings.weapon()).tier <= 1 && points >= 3){
						enGainMulti += 1.20f;
					} else if (((MeleeWeapon) hero.belongings.weapon()).tier <= 2 && points >= 2){
						enGainMulti += 0.80f;
					} else if (((MeleeWeapon) hero.belongings.weapon()).tier <= 3 && points >= 1){
						enGainMulti += 0.40f;
					}
				}

			}
		}
		energyGain *= enGainMulti;

		energy = Math.min(energy+energyGain, energyCap());

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

		if (target instanceof Hero && ((Hero) target).hasTalent(Talent.COMBINED_ENERGY)
				&& abil.energyCost() >= 5-((Hero) target).pointsInTalent(Talent.COMBINED_ENERGY)) {
			Talent.CombinedEnergyAbilityTracker tracker = target.buff(Talent.CombinedEnergyAbilityTracker.class);
			if (tracker == null || tracker.wepAbilUsed == false){
				Buff.prolong(target, Talent.CombinedEnergyAbilityTracker.class, target.cooldown()).energySpent = abil.energyCost();
			} else {
				tracker.energySpent = abil.energyCost();
				processCombinedEnergy(tracker);
			}
		}

		if (cooldown > 0 || energy < 1){
			ActionIndicator.clearAction(this);
		} else {
			ActionIndicator.refresh();
		}
		BuffIndicator.refreshHero();
	}

	public boolean abilitiesEmpowered( Hero hero ){
		//100%/80%/60% energy at +1/+2/+3
		return energy/energyCap() >= 1.2f - 0.2f*hero.pointsInTalent(Talent.MONASTIC_VIGOR);
	}

	public void processCombinedEnergy(Talent.CombinedEnergyAbilityTracker tracker){
		energy = Math.min(energy+tracker.energySpent/2f, energyCap());
		cooldown = 0;
		tracker.detach();
		if (energy >= 1){
			ActionIndicator.setAction(this);
		}
		BuffIndicator.refreshHero();
	}

	@Override
	public String actionName() {
		return Messages.get(this, "action");
	}

	@Override
	public int actionIcon() {
		return HeroIcon.MONK_ABILITIES;
	}

	@Override
	public Visual secondaryVisual() {
		BitmapText txt = new BitmapText(PixelScene.pixelFont);
		txt.text( Integer.toString((int)energy) );
		txt.hardlight(CharSprite.POSITIVE);
		txt.measure();
		return txt;
	}

	@Override
	public int indicatorColor() {
		if (abilitiesEmpowered(Dungeon.hero)){
			return 0x99CC33;
		} else {
			return 0xA08840;
		}
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

		public static class UnarmedAbilityTracker extends FlavourBuff{};
		public static class JustHitTracker extends FlavourBuff{};

		public static class FlurryEmpowerTracker extends FlavourBuff{};

		public static class Flurry extends MonkAbility {

			@Override
			public int energyCost() {
				return 1;
			}

			@Override
			public int cooldown() {
				//1 less turn as no time is spent flurrying
				return Dungeon.hero.buff(JustHitTracker.class) != null ? 0 : 5;
			}

			@Override
			public String desc() {
				//hero unarmed damage
				return Messages.get(this, "desc", 1, Dungeon.hero.STR()-8);
			}

			@Override
			public String targetingPrompt() {
				return Messages.get(MeleeWeapon.class, "prompt");
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				if (target == null || target == -1){
					return;
				}

				Char enemy = Actor.findChar(target);
				if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
					GLog.w(Messages.get(MeleeWeapon.class, "ability_no_target"));
					return;
				}

				UnarmedAbilityTracker tracker = Buff.affect(hero, UnarmedAbilityTracker.class);
				if (!hero.canAttack(enemy)){
					GLog.w(Messages.get(MeleeWeapon.class, "ability_bad_position"));
					tracker.detach();
					return;
				}

				if (Buff.affect(hero, MonkEnergy.class).abilitiesEmpowered(hero)){
					Buff.affect(hero, FlurryEmpowerTracker.class, 0f);
				}

				hero.sprite.attack(enemy.pos, new Callback() {
					@Override
					public void call() {
						AttackIndicator.target(enemy);
						hero.attack(enemy, 1, 0, Char.INFINITE_ACCURACY);

						if (enemy.isAlive()){
							hero.sprite.attack(enemy.pos, new Callback() {
								@Override
								public void call() {
									hero.attack(enemy, 1, 0, Char.INFINITE_ACCURACY);
									Invisibility.dispel();
									hero.next();
									tracker.detach();
									Buff.affect(hero, MonkEnergy.class).abilityUsed(Flurry.this);
									if (hero.buff(JustHitTracker.class) != null) {
										hero.buff(JustHitTracker.class).detach();
									}
									if (hero.buff(FlurryEmpowerTracker.class) != null){
										hero.buff(FlurryEmpowerTracker.class).detach();
									}
								}
							});
						} else {
							Invisibility.dispel();
							hero.next();
							tracker.detach();
							Buff.affect(hero, MonkEnergy.class).abilityUsed(Flurry.this);
							if (hero.buff(JustHitTracker.class) != null) {
								hero.buff(JustHitTracker.class).detach();
							}
							if (hero.buff(FlurryEmpowerTracker.class) != null){
								hero.buff(FlurryEmpowerTracker.class).detach();
							}
						}
					}
				});
			}
		}

		public static class Focus extends MonkAbility {

			@Override
			public int energyCost() {
				return 2;
			}

			@Override
			public int cooldown() {
				//1 less if focus was instant
				return Buff.affect(Dungeon.hero, MonkEnergy.class).abilitiesEmpowered(Dungeon.hero) ? 5 : 6;
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				Buff.prolong(hero, FocusBuff.class, 30f);

				if (Buff.affect(hero, MonkEnergy.class).abilitiesEmpowered(hero)){
					hero.next();
				} else {
					hero.spendAndNext(1f);
				}
				Buff.affect(hero, MonkEnergy.class).abilityUsed(this);
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

			//tracks just the activation of focus, needed as magical attacks do not trigger it
			// but may be dodged normally
			public static class FocusActivation extends FlavourBuff {

				{
					actPriority = VFX_PRIO;
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
				return 5; //1 less turn as no time is spent dashing
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

				int range = 3;
				if (Buff.affect(hero, MonkEnergy.class).abilitiesEmpowered(hero)){
					range += 3;
				}

				if (Dungeon.hero.rooted){
					PixelScene.shake( 1, 1f );
					GLog.w(Messages.get(MeleeWeapon.class, "ability_bad_position"));
					return;
				}

				if (Dungeon.level.distance(hero.pos, target) > range){
					GLog.w(Messages.get(MeleeWeapon.class, "ability_bad_position"));
					return;
				}

				Ballistica dash = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);

				if (!dash.collisionPos.equals(target)
						|| Actor.findChar(target) != null
						|| (Dungeon.level.solid[target] && !Dungeon.level.passable[target])){
					GLog.w(Messages.get(MeleeWeapon.class, "ability_bad_position"));
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
				return 6;
			}

			@Override
			public String desc() {
				//3x hero unarmed damage
				return Messages.get(this, "desc", 3, 3*(Dungeon.hero.STR()-8));
			}

			@Override
			public String targetingPrompt() {
				return Messages.get(MeleeWeapon.class, "prompt");
			}

			@Override
			public void doAbility(Hero hero, Integer target) {
				if (target == null || target == -1){
					return;
				}

				Char enemy = Actor.findChar(target);
				if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
					GLog.w(Messages.get(MeleeWeapon.class, "ability_no_target"));
					return;
				}

				UnarmedAbilityTracker tracker = Buff.affect(hero, UnarmedAbilityTracker.class);
				if (!hero.canAttack(enemy)){
					GLog.w(Messages.get(MeleeWeapon.class, "ability_bad_position"));
					tracker.detach();
					return;
				}

				hero.sprite.attack(enemy.pos, new Callback() {
					@Override
					public void call() {
						AttackIndicator.target(enemy);
						boolean empowered = Buff.affect(hero, MonkEnergy.class).abilitiesEmpowered(hero);

						int oldPos = enemy.pos;
						if (hero.attack(enemy, empowered ? 4.5f : 3f, 0, Char.INFINITE_ACCURACY)){
							Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
						}

						if (enemy.isAlive() && oldPos == enemy.pos){
							//trace a ballistica to our target (which will also extend past them
							Ballistica trajectory = new Ballistica(hero.pos, enemy.pos, Ballistica.STOP_TARGET);
							//trim it to just be the part that goes past them
							trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
							//knock them back along that ballistica
							WandOfBlastWave.throwChar(enemy, trajectory, 6, true, false, hero);

							if (trajectory.dist > 0) {
								Buff.affect(enemy, Paralysis.class, Math.min( 6, trajectory.dist));
							}
						}
						Invisibility.dispel();
						hero.spendAndNext(hero.attackDelay());
						tracker.detach();
						Buff.affect(hero, MonkEnergy.class).abilityUsed(DragonKick.this);

						if (empowered){
							for (Char ch : Actor.chars()){
								if (ch != enemy
										&& ch.alignment == Char.Alignment.ENEMY
										&& Dungeon.level.adjacent(ch.pos, hero.pos)){
									//trace a ballistica to our target (which will also extend past them
									Ballistica trajectory = new Ballistica(hero.pos, ch.pos, Ballistica.STOP_TARGET);
									//trim it to just be the part that goes past them
									trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
									//knock them back along that ballistica
									WandOfBlastWave.throwChar(ch, trajectory, 6, true, false, hero);

									if (trajectory.dist > 0) {
										Buff.affect(ch, Paralysis.class, Math.min( 6, trajectory.dist));
									}
								}
							}
						}
					}
				});
			}
		}

		public static class Meditate extends MonkAbility {

			@Override
			public int energyCost() {
				return 5;
			}

			@Override
			//longer to account for turns spent meditating
			public int cooldown() {
				return 10;
			}

			@Override
			public void doAbility(Hero hero, Integer target) {

				hero.sprite.operate(hero.pos);
				GameScene.flash(0x88000000, false);
				Sample.INSTANCE.play(Assets.Sounds.SCAN);

				for (Buff b : hero.buffs()){
					if (b.type == Buff.buffType.NEGATIVE
							&& !(b instanceof AllyBuff)
							&& !(b instanceof LostInventory)){
						b.detach();
					}
				}

				//we process this as 5x wait actions instead of one 5 tick action to prevent
				// effects like time freeze from eating the whole action duration
				for (int i = 0; i < 5; i++) hero.spendConstant(Actor.TICK);

				if (Buff.affect(hero, MonkEnergy.class).abilitiesEmpowered(hero)){
					int toHeal = Math.round((hero.HT - hero.HP)/5f);
					if (toHeal > 0) {
						Buff.affect(hero, Healing.class).setHeal(toHeal, 0, 1);
					}
					Buff.affect(hero, MeditateResistance.class, hero.cooldown());
				}

				Actor.addDelayed(new Actor() {
					@Override
					protected boolean act() {
						Buff.affect(hero, Recharging.class, 8f);
						Buff.affect(hero, ArtifactRecharge.class).prolong(8f).ignoreHornOfPlenty = false;
						Actor.remove(this);
						return true;
					}
				}, hero.cooldown()-1);

				hero.next();
				Buff.affect(hero, MonkEnergy.class).abilityUsed(this);
			}

			public static class MeditateResistance extends FlavourBuff{
				{
					actPriority = HERO_PRIO+1; //ends just before the hero acts
				}
			};
		}

	}
}
