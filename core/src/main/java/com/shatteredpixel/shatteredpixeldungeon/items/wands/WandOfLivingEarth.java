/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EarthGuardianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
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
		return 3 + lvl;
	}
	
	@Override
	public int max(int lvl) {
		return 6 + 2*lvl;
	}
	
	@Override
	protected void onZap(Ballistica bolt) {
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

		RockArmor buff = curUser.buff(RockArmor.class);
		if (ch == null){
			armorToAdd = 0;
		} else {
			if (buff == null && guardian == null) {
				buff = Buff.affect(curUser, RockArmor.class);
			}
			if (buff != null) {
				buff.addArmor(level(), armorToAdd);
			}
		}

		//shooting at the guardian
		if (guardian != null && guardian == ch){
			guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + level() / 2);
			guardian.setInfo(curUser, level(), armorToAdd);
			processSoulMark(guardian, chargesPerCast());

		//shooting the guardian at a location
		} else if ( guardian == null && buff != null && buff.armor >= buff.armorToGuardian()){

			//create a new guardian
			guardian = new EarthGuardian();
			guardian.setInfo(curUser, level(), buff.armor);

			//if the collision pos is occupied (likely will be), then spawn the guardian in the
			//adjacent cell which is closes to the user of the wand.
			if (ch != null){

				ch.sprite.centerEmitter().burst(MagicMissile.EarthParticle.BURST, 5 + level()/2);

				processSoulMark(ch, chargesPerCast());
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
					curUser.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + level()/2);
					return; //do not spawn guardian or detach buff
				} else {
					guardian.pos = closest;
					GameScene.add(guardian, 1);
					Dungeon.level.press(guardian.pos, guardian);
				}

				if (ch.alignment == Char.Alignment.ENEMY || ch.buff(Amok.class) != null) {
					guardian.aggro(ch);
				}

			} else {
				guardian.pos = bolt.collisionPos;
				GameScene.add(guardian, 1);
				Dungeon.level.press(guardian.pos, guardian);
			}

			guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + level()/2);
			buff.detach();

		//shooting at a location/enemy with no guardian being shot
		} else {

			if (ch != null) {

				ch.sprite.centerEmitter().burst(MagicMissile.EarthParticle.BURST, 5 + level() / 2);

				processSoulMark(ch, chargesPerCast());
				ch.damage(damage, this);
				
				if (guardian == null) {
					curUser.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + level() / 2);
				} else {
					guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + level() / 2);
					guardian.setInfo(curUser, level(), armorToAdd);
					if (ch.alignment == Char.Alignment.ENEMY || ch.buff(Amok.class) != null) {
						guardian.aggro(ch);
					}
				}

			} else {
				Dungeon.level.press(bolt.collisionPos, null, true);
			}
		}

	}
	
	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.EARTH,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
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

		if (guardian != null){
			guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + level() / 2);
			guardian.setInfo(Dungeon.hero, level(), damage);
		} else {
			attacker.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + level() / 2);
			Buff.affect(attacker, RockArmor.class).addArmor(level(), damage);
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

		private int wandLevel;
		private int armor;

		private void addArmor( int wandLevel, int armor ){
			this.wandLevel = Math.max(this.wandLevel, wandLevel);
			this.armor += armor;
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
				BuffIndicator.refreshHero();
				return damage - block;
			}
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get( this, "desc", armor, armorToGuardian());
		}

		private static final String WAND_LEVEL = "wand_level";
		private static final String ARMOR = "armor";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(WAND_LEVEL, wandLevel);
			bundle.put(ARMOR, armor);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			wandLevel = bundle.getInt(WAND_LEVEL);
			armor = bundle.getInt(ARMOR);
		}
	}

	public static class EarthGuardian extends NPC {

		{
			spriteClass = EarthGuardianSprite.class;

			alignment = Alignment.ALLY;
			state = HUNTING;
			intelligentAlly = true;
			WANDERING = new Wandering();

			//before other mobs
			actPriority = MOB_PRIO + 1;

			HP = HT = 0;
		}

		private int wandLevel = -1;

		private void setInfo(Hero hero, int wandLevel, int healthToAdd){
			if (wandLevel > this.wandLevel) {
				this.wandLevel = wandLevel;
				HT = 16 + 8 * wandLevel;
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
			return Random.NormalIntRange(3 + Dungeon.depth/4, 6 + Dungeon.depth/2);
		}

		@Override
		public int drRoll() {
			return Random.NormalIntRange(wandLevel, 3 + 3*wandLevel);
		}

		@Override
		public String description() {
			return Messages.get(this, "desc", wandLevel, 3 + 3*wandLevel);
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
