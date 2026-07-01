/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultFinalRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class VaultBossElemental extends Mob {

	{
		spriteClass = ElementalSprite.Fire.class;

		HP = HT = 600;

		properties.add(Property.BOSS);
	}

	public enum ElementalForm {
		FIRE,
		FROST,
		SHOCK,
		UNSTABLE //currently unused
	}
	private ElementalForm form = ElementalForm.FIRE;

	public void setElementalForm( ElementalForm form ){
		//always remove pincushion as we're either leaving or entering frost form
		Buff.affect(this, PinCushionRemover.class);

		this.form = form;
		boolean wasTurned = sprite.flipHorizontal;
		if (form == ElementalForm.FIRE){
			spriteClass = ElementalSprite.Fire.class;
			sprite.killAndErase();
			GameScene.addSprite(this);
			sprite.emitter().burst(FlameParticle.FACTORY, 100);
		} else if (form == ElementalForm.FROST){
			spriteClass = ElementalSprite.Frost.class;
			sprite.killAndErase();
			GameScene.addSprite(this);
			sprite.emitter().burst(MagicMissile.MagicParticle.FACTORY, 100);
		} else if (form == ElementalForm.SHOCK){
			spriteClass = ElementalSprite.Shock.class;
			sprite.killAndErase();
			GameScene.addSprite(this);
			sprite.emitter().burst(SparkParticle.FACTORY, 100);
		}
		sprite.flipHorizontal = wasTurned;
		BossHealthBar.assignBoss(this);
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		if (form == ElementalForm.SHOCK && enemy == Dungeon.hero && !(Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
			enemy.sprite.parent.addToFront( new Lightning( sprite.center(), enemy.sprite.center(), null ) );
			enemy.damage( Random.IntRange(2, 5), new Shocking() ); //TODO final dmg
			Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
			PixelScene.shake( 2, 0.3f );
			enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			enemy.sprite.flash();
			if (enemy == Dungeon.hero && !enemy.isAlive()){
				Dungeon.fail(this);
				//TODO magic death badge?
			}
			GLog.w("Shocked!");
		}
		return super.defenseProc(enemy, damage);
	}

	@Override
	public void damage(int dmg, Object src) {
		//fire form is resistant to magic and weak to thrown weapons
		if (form == ElementalForm.FIRE){
			if (AntiMagic.RESISTS.contains(src.getClass())){
				GLog.w("Resisted!");
				dmg /= 4;
				if (src instanceof Wand || src instanceof ClericSpell){
					//TODO additional penalty, perhaps prompt more fire attacks?
				}
			} else if (src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon){
				GLog.w("Weak!");
				dmg += 10;
			}
		//frost form is resistant to thrown weapons and weak to melee (only from the hero though!)
		} else if ( form == ElementalForm.FROST ){
			if (src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon){
				GLog.w("Resisted!");
				dmg /= 4;
			} else if (src == Dungeon.hero && !(Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				GLog.w("Weak!");
				dmg += 10;
			}
		//shock form is resistant to melee and weak to magic
		//TODO what about magical consumables, mainly retribution?
		} else if ( form == ElementalForm.SHOCK ){
			if (src instanceof Char && !(src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				GLog.w("Resisted!");
				dmg /= 4;
			} else if (AntiMagic.RESISTS.contains(src.getClass())){
				GLog.w("Weak!");
				dmg += 10;
				//TODO slightly charge wands?
			}
		}

		//TODO do we want brackets like Tengu, or maybe just count up to 120 HP lost?
		int hpBracket = HT / 5; //120, is that right?
		int curbracket = (int) Math.ceil(HP / (float)hpBracket);

		super.damage(dmg, src);

		if (HP <= (curbracket-1)*hpBracket){
			//cannot be hit through multiple brackets at a time
			HP = Math.max(HP, (curbracket-2)*hpBracket);

			if (isAlive()) {
				//changes forms!
				ElementalForm newForm;
				do {
					newForm = ElementalForm.values()[Random.Int(3)];
				} while (newForm == form);
				setElementalForm(newForm);
				spend(TICK);
			}
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		if (Dungeon.level instanceof RegularLevel){
			Room r = ((RegularLevel) Dungeon.level).room(pos);
			if (r instanceof VaultFinalRoom){
				((VaultFinalRoom) r).unlock();
			}
		}
	}

	@Override
	public CharSprite sprite() {
		CharSprite sprite = super.sprite();
		sprite.scale.set(2f);
		return sprite;
	}

	@Override
	public boolean add(Buff buff) {
		if (buff instanceof PinCushion && form != ElementalForm.FROST){
			Buff.affect(this, PinCushionRemover.class);
		}
		return super.add(buff);
	}

	private static final String FORM = "elemental_form";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(FORM, form);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		form = bundle.getEnum(FORM, ElementalForm.class);
	}

	//used to forceably remove pincushion after its applied
	public static class PinCushionRemover extends Buff{

		{
			actPriority = VFX_PRIO;
		}

		@Override
		public boolean act() {

			PathFinder.buildDistanceMap(target.pos, Dungeon.level.passable, 2);
			ArrayList<Integer> candidates = new ArrayList<>();
			int furthestDist = 1;
			int closestDist = 100;
			for (int i = 0; i < Dungeon.level.length(); i++){
				if (PathFinder.distance[i] == 2){
					int dist = Dungeon.level.distance(i, Dungeon.hero.pos);
					if (dist > 1) {
						if (dist < closestDist){
							closestDist = dist;
						} if (dist > furthestDist){
							furthestDist = dist;
						}
						candidates.add(i);
					}
				}
			}

			//prevent overlap if hero is close
			if (Math.abs(furthestDist - closestDist) <= 1){
				furthestDist++;
				closestDist--;
			}

			for (int i : candidates.toArray(new Integer[0])){
				int dist = Dungeon.level.distance(i, Dungeon.hero.pos);
				if (dist <= closestDist || dist >= furthestDist){
					candidates.remove((Integer)i);
				}
			}

			while (target.buff(PinCushion.class) != null) {
				Item item = target.buff(PinCushion.class).grabOne();

				//TODO drop around, 2 tile distance favouring medium near the hero?
				Dungeon.level.drop(item, Random.element(candidates)).sprite.drop(target.pos);
			}
			detach();
			return true;
		}
	}
}
