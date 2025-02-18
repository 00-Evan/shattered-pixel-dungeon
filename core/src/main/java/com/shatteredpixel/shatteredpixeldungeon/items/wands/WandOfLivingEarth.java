/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Stasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EarthGuardianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfLivingEarth extends DamageWand {
	
	{
		image = ItemSpriteSheet.WAND_LIVING_EARTH;
	}
	
	@Override
	public int min(int lvl) {
		return 4;
	}
	
	@Override
	public int max(int lvl) {
		return 6 + 2*lvl;
	}
	
	@Override
	public void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);
		int damage = damageRoll();
		int armorToAdd = damage;

		EarthGuardian guardian = null;
		for (Mob m : Dungeon.level.mobs){
			if (m instanceof EarthGuardian){
				guardian = (EarthGuardian) m;
				break;
			}
		}

		if (Stasis.getStasisAlly() instanceof EarthGuardian){
			guardian = (EarthGuardian)Stasis.getStasisAlly();
		}

		RockArmor buff = curUser.buff(RockArmor.class);
		//only grant armor if we are shooting at an enemy, a hiding mimic, or the guardian
		if ((guardian == null || ch != guardian) && (ch == null
				|| ch.alignment == Char.Alignment.ALLY
				|| ch.alignment == Char.Alignment.NEUTRAL && !(ch instanceof Mimic))){
			armorToAdd = 0;
		} else {
			if (buff == null && guardian == null) {
				buff = Buff.affect(curUser, RockArmor.class);
			}
			if (buff != null) {
				buff.addArmor( buffedLvl(), armorToAdd);
			}
		}

		//shooting at the guardian
		if (guardian != null && guardian == ch){
			guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
			guardian.setInfo(curUser, buffedLvl(), armorToAdd);
			wandProc(guardian, chargesPerCast());
			Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.9f * Random.Float(0.87f, 1.15f) );

		//shooting the guardian at a location
		} else if ( guardian == null && buff != null && buff.armor >= buff.armorToGuardian()){

			//create a new guardian
			guardian = new EarthGuardian();
			guardian.setInfo(curUser, buffedLvl(), buff.armor);

			if (buff.powerOfManyTurns > 0){
				Buff.affect(guardian, PowerOfMany.PowerBuff.class, buff.powerOfManyTurns);
			}

			//if the collision pos is occupied (likely will be), then spawn the guardian in the
			//adjacent cell which is closes to the user of the wand.
			if (ch != null){

				ch.sprite.centerEmitter().burst(MagicMissile.EarthParticle.BURST, 5 + buffedLvl()/2);

				wandProc(ch, chargesPerCast());
				ch.damage(damage, this);

				int closest = -1;
				boolean[] passable = Dungeon.level.passable;

				for (int n : PathFinder.NEIGHBOURS9) {
					int c = bolt.collisionPos + n;
					if (passable[c] && Actor.findChar( c ) == null
						&& (closest == -1 || (Dungeon.level.trueDistance(c, curUser.pos) < (Dungeon.level.trueDistance(closest, curUser.pos))))) {
						closest = c;
					}
				}

				if (closest == -1){
					if (armorToAdd > 0) {
						curUser.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
					}
					return; //do not spawn guardian or detach buff
				} else {
					guardian.pos = closest;
					GameScene.add(guardian, 1);
					Dungeon.level.occupyCell(guardian);
				}

				if (ch.alignment == Char.Alignment.ENEMY || ch.buff(Amok.class) != null) {
					guardian.aggro(ch);
				}

			} else {
				guardian.pos = bolt.collisionPos;
				GameScene.add(guardian, 1);
				Dungeon.level.occupyCell(guardian);
			}

			guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl()/2);
			buff.detach();
			Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.9f * Random.Float(0.87f, 1.15f) );

		//shooting at a location/enemy with no guardian being shot
		} else {

			if (ch != null) {

				ch.sprite.centerEmitter().burst(MagicMissile.EarthParticle.BURST, 5 + buffedLvl() / 2);

				wandProc(ch, chargesPerCast());
				ch.damage(damage, this);
				Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 0.8f * Random.Float(0.87f, 1.15f) );
				
				if (guardian == null) {
					if (armorToAdd > 0) {
						curUser.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
					}
				} else {
					if (guardian.sprite != null) { //may be in stasis
						guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
					}
					guardian.setInfo(curUser, buffedLvl(), armorToAdd);
					if (ch.alignment == Char.Alignment.ENEMY || ch.buff(Amok.class) != null) {
						guardian.aggro(ch);
					}
				}

			} else {
				Dungeon.level.pressCell(bolt.collisionPos);
			}
		}

	}

	@Override
	public String upgradeStat2(int level) {
		return Integer.toString(16 + 8*level);
	}

	@Override
	public String upgradeStat3(int level) {
		if (Dungeon.isChallenged(Challenges.NO_ARMOR)){
			return level + "-" + (2+level);
		} else {
			return level + "-" + (3+(3*level));
		}
	}

	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.EARTH,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}
	
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		EarthGuardian guardian = null;
		for (Mob m : Dungeon.level.mobs){
			if (m instanceof EarthGuardian){
				guardian = (EarthGuardian) m;
				break;
			}
		}
		
		int armor = Math.round(damage*0.33f*procChanceMultiplier(attacker));

		if (guardian != null){
			guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
			guardian.setInfo(Dungeon.hero, buffedLvl(), armor);
		} else {
			attacker.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + buffedLvl() / 2);
			Buff.affect(attacker, RockArmor.class).addArmor( buffedLvl(), armor);
		}
	}
	
	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		if (Random.Int(10) == 0){
			particle.color(ColorMath.random(0xFFF568, 0x80791A));
		} else {
			particle.color(ColorMath.random(0x805500, 0x332500));
		}
		particle.am = 1f;
		particle.setLifespan(2f);
		particle.setSize( 1f, 2f);
		particle.shuffleXY(0.5f);
		float dst = Random.Float(11f);
		particle.x -= dst;
		particle.y += dst;
	}

	public static class RockArmor extends Buff {

		{
			type = buffType.POSITIVE;
		}

		private int wandLevel;
		private int armor;

		private float powerOfManyTurns = 0;

		@Override
		public boolean act() {
			if (powerOfManyTurns > 0){
				powerOfManyTurns--;
				if (powerOfManyTurns <= 0){
					powerOfManyTurns = 0;
					BuffIndicator.refreshHero();
				}
			}
			spend(TICK);
			return true;
		}

		private void addArmor(int wandLevel, int toAdd ){
			this.wandLevel = Math.max(this.wandLevel, wandLevel);
			armor += toAdd;
			armor = Math.min(armor, 2*armorToGuardian());
		}

		private int armorToGuardian(){
			return 8 + wandLevel*4;
		}

		public int absorb( int damage ) {
			int block = damage - damage/2;
			if (armor <= block) {
				detach();
				return damage - armor;
			} else {
				armor -= block;
				return damage - block;
			}
		}

		public boolean isEmpowered(){
			return powerOfManyTurns > 0;
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}

		@Override
		public void tintIcon(Image icon) {
			if (isEmpowered()){
				icon.hardlight(1.8f, 1.8f, 0.6f);
			} else {
				icon.brightness(0.6f);
			}
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (armorToGuardian() - armor) / (float)armorToGuardian());
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(armor);
		}

		@Override
		public String desc() {
			String desc = Messages.get( this, "desc", armor, armorToGuardian());
			if (isEmpowered()){
				desc += "\n\n" + Messages.get(this, "desc_many", (int)powerOfManyTurns);
			}
			return desc;
		}

		private static final String WAND_LEVEL = "wand_level";
		private static final String ARMOR = "armor";

		private static final String POWER_TURNS = "power_turns";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(WAND_LEVEL, wandLevel);
			bundle.put(ARMOR, armor);
			bundle.put(POWER_TURNS, powerOfManyTurns);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			wandLevel = bundle.getInt(WAND_LEVEL);
			armor = bundle.getInt(ARMOR);
			powerOfManyTurns = bundle.getFloat(POWER_TURNS);
		}
	}

	public static class EarthGuardian extends NPC {

		{
			spriteClass = EarthGuardianSprite.class;

			alignment = Alignment.ALLY;
			state = HUNTING;
			intelligentAlly = true;

			properties.add(Property.INORGANIC);

			WANDERING = new Wandering();

			//before other mobs
			actPriority = MOB_PRIO + 1;

			HP = HT = 0;
		}

		private int wandLevel = -1;

		public void setInfo(Hero hero, int wandLevel, int healthToAdd){
			if (wandLevel > this.wandLevel) {
				this.wandLevel = wandLevel;
				HT = 16 + 8 * wandLevel;
			}
			if (HP != 0 && sprite != null){
				sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(healthToAdd), FloatingText.HEALING);
			}
			HP = Math.min(HT, HP + healthToAdd);
			//half of hero's evasion
			defenseSkill = (hero.lvl + 4)/2;
		}

		@Override
		public int attackSkill(Char target) {
			//same as the hero
			return 2*defenseSkill + 5;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			if (enemy instanceof Mob) ((Mob)enemy).aggro(this);
			return super.attackProc(enemy, damage);
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(2, 4 + Dungeon.scalingDepth()/2);
		}

		@Override
		public int drRoll() {
			int dr = super.drRoll();
			if (Dungeon.isChallenged(Challenges.NO_ARMOR)){
				return dr + Random.NormalIntRange(wandLevel, 2 + wandLevel);
			} else {
				return dr + Random.NormalIntRange(wandLevel, 3 + 3 * wandLevel);
			}
		}

		@Override
		public String description() {
			String desc = Messages.get(this, "desc");

			if (Actor.chars().contains(this)) {
				if (Dungeon.isChallenged(Challenges.NO_ARMOR)) {
					desc += "\n\n" + Messages.get(this, "wand_info", wandLevel, 2 + wandLevel);
				} else {
					desc += "\n\n" + Messages.get(this, "wand_info", wandLevel, 3 + 3 * wandLevel);
				}
			}

			return desc;
			
		}
		
		{
			immunities.add( AllyBuff.class );
		}

		private static final String DEFENSE = "defense";
		private static final String WAND_LEVEL = "wand_level";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(DEFENSE, defenseSkill);
			bundle.put(WAND_LEVEL, wandLevel);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			defenseSkill = bundle.getInt(DEFENSE);
			wandLevel = bundle.getInt(WAND_LEVEL);
		}

		private class Wandering extends Mob.Wandering{

			@Override
			public boolean act(boolean enemyInFOV, boolean justAlerted) {
				if (!enemyInFOV){
					Buff.affect(Dungeon.hero, RockArmor.class).addArmor(wandLevel, HP);
					if (buff(PowerOfMany.PowerBuff.class) != null){
						Buff.affect(Dungeon.hero, RockArmor.class).powerOfManyTurns = buff(PowerOfMany.PowerBuff.class).cooldown()+1;
					}
					Dungeon.hero.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + wandLevel/2);
					destroy();
					sprite.die();
					return true;
				} else {
					return super.act(enemyInFOV, justAlerted);
				}
			}

		}

	}
}
