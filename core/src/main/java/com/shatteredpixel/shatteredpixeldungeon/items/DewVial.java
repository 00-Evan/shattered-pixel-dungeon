/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DewVial extends MeleeWeapon {

	private static final int MAX_VOLUME	= 10;

	private static final String AC_DRINK	= "DRINK";
	private static final String AC_DRINK_TWO	= "DRINK_TWO";
	private static final String AC_DRINK_THREE	= "DRINK_THREE";
	private static final String AC_DRINK_THREEPALF	= "DRINK_THREEPALF";
	private static final String AC_DRINK_FOUR	= "DRINK_FOUR";
	private static final String AC_DRINK_FOURSOUL	= "DRINK_FOURSOUL";

	private static final float TIME_TO_DRINK = 1f;

	private static final String TXT_STATUS	= "%d/%d";

	public DewVial() {
		super.image = ItemSpriteSheet.VIAL;
		defaultAction = AC_DRINK;
		super.tier = 3;
		unique = true;
		//不会被移除
	}

	public int image() {
		if (this.level() == 1 ) {
			return ItemSpriteSheet.BLUEDEVIAL;
		} else if  (this.level() == 2 ) {
			return ItemSpriteSheet.PINKDEVIAL;
		} else if  (this.level() == 3 ) {
			return ItemSpriteSheet.PINKDEVIAL;
		} else if  (this.level() == 4) {
			return ItemSpriteSheet.REDDEVIAL;
		} else if  (this.level() == 5) {
			return ItemSpriteSheet.REDDEVIAL;
		} else if  (this.level() >= 6) {
			return ItemSpriteSheet.REDDEVIAL;
		}
		return image;
	}

	public int proc(Char Dewvial_One, Char Dewvial_Two, int Dewvial_Three) {
			//仙露明珠信赖度评价
			if (this.level() >= 1 && View == 3) {
				GLog.n(Messages.get(this, "dew_rk1"));
				//super.image = ItemSpriteSheet.BLUEDEVIAL;
				View = 2;
			} else if (this.level() >= 2 && View == 2) {
				GLog.p(Messages.get(this, "dew_rk2"));
				//super.image = ItemSpriteSheet.PINKDEVIAL;
				View = 1;
			} else if (this.level() >= 4 && View == 1) {
				GLog.b(Messages.get(this, "dew_rk3"));
				//super.image = ItemSpriteSheet.REDDEVIAL;
				//蓝色文本渲染
				View = 0;
			}
		return super.proc(Dewvial_One, Dewvial_Two, Dewvial_Three);
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
	public int STRReq(int lvl) {
		return 12 + hero.lvl/5;
	}
	@Override
	public int max(int lvl) {
		return  1*(tier+1) +    //12 base, down from 20
				lvl*(tier);     //+3 per level, down from +4
	}

	@Override
	public int damageRoll(Char owner) {
		if (owner instanceof Hero) {
			Hero hero = (Hero)owner;
			Char enemy = hero.enemy();
			if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
				//deals 67% toward max to max on surprise, instead of min to max.
				int diff = max() - min();
				int damage = augment.damageFactor(Random.NormalIntRange(
						min() + Math.round(diff*0.67f),
						max()));
				int exStr = hero.STR() - STRReq();
				if (exStr > 0) {
					damage += Random.IntRange(0, exStr);
				}
				return damage;
			}
		}
		return super.damageRoll(owner);
	}
	//按钮控件逻辑
	public static int View=3;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (volume > 0) {
			actions.add( AC_DRINK );
		}
		//常规检测
		if (volume >= 3 && DewVial.View==2 && this.level() >= 1 ){
			actions.add( AC_DRINK_TWO );
		}
		//如果露珠大于或等于3个，私有short变量=2，且武器等级大于等于1，则显示按钮
		if (volume >= 5 && DewVial.View==1 && this.level() >= 2) {
			actions.add( AC_DRINK_THREE );
		}
		//如果露珠大于或等于5个，私有short变量=1，且武器等级大于等于2，则显示按钮
		if (volume >= 6 && DewVial.View==1 && this.level() >= 2) {
			actions.add( AC_DRINK_THREEPALF );
		}
		//如果露珠大于或等于6个，私有short变量=1，且武器等级大于等于2，则显示按钮
		if (volume >= 7 && DewVial.View==0 && this.level() >= 4) {
			actions.add( AC_DRINK_FOUR );
		}
		//如果露珠大于或等于7个，私有short变量=0，且武器等级大于等于4，则显示按钮
		if (volume >= 8 && DewVial.View==0 && this.level() >= 4) {
			actions.add( AC_DRINK_FOURSOUL );
		}
		//如果露珠大于或等于8个，私有short变量=0，且武器等级大于等于4，则显示按钮
		return actions;
	}



	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		//仙露审判II
		if (action.equals( AC_DRINK_FOURSOUL )) {
			if (volume > 0) {

				float missingHealthPercent = 1f - (hero.HP / (float)hero.HT);

				int curShield = 0;
				if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
				int maxShield = Math.round(hero.HT *0.01f*hero.pointsInTalent(Talent.SHIELDING_DEW));
				if (hero.hasTalent(Talent.SHIELDING_DEW)){
					float missingShieldPercent = 1f - (curShield / (float)maxShield);
					missingShieldPercent *= 0.2f*hero.pointsInTalent(Talent.SHIELDING_DEW);
					if (missingShieldPercent > 0){
						missingHealthPercent += missingShieldPercent;
					}
				}

				//trimming off 0.01 drops helps with floating point errors
				int dropsNeeded = 8;
				dropsNeeded = (int)GameMath.gate(1, dropsNeeded, volume);

				if (Dewdrop.consumeDew(dropsNeeded, hero)){
					volume -= dropsNeeded;
					GLog.b( Messages.get(this, "dead") );
					for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
						Buff.affect(mob, Burning.class).reignite(mob, 8f);
						Buff.prolong(mob, Vertigo.class, 9f);
						Buff.affect( mob, Levitation.class, Levitation.DURATION/5 );
						new Flare( 5, 32 ).color( 0x00FFFF, true ).show( curUser.sprite, 2f );
					}
					//点燃 飘浮 眩晕 2

					hero.spend(TIME_TO_DRINK);
					hero.busy();

					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					hero.sprite.operate(hero.pos);

					updateQuickslot();
				}


			}
		}

		//仙露审判 I
		if (action.equals( AC_DRINK_THREEPALF )) {
			if (volume > 0) {

				float missingHealthPercent = 1f - (hero.HP / (float)hero.HT);

				int curShield = 0;
				if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
				int maxShield = Math.round(hero.HT *0.01f*hero.pointsInTalent(Talent.SHIELDING_DEW));
				if (hero.hasTalent(Talent.SHIELDING_DEW)){
					float missingShieldPercent = 1f - (curShield / (float)maxShield);
					missingShieldPercent *= 0.2f*hero.pointsInTalent(Talent.SHIELDING_DEW);
					if (missingShieldPercent > 0){
						missingHealthPercent += missingShieldPercent;
					}
				}

				//trimming off 0.01 drops helps with floating point errors
				int dropsNeeded = 6;
				dropsNeeded = (int)GameMath.gate(1, dropsNeeded, volume);

				if (Dewdrop.consumeDew(dropsNeeded, hero)){
					GLog.p( Messages.get(this, "eye") );
					for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
						Buff.affect(mob, Terror.class, 6f);
						Buff.affect(mob, Ooze.class).set(5.0f);
						new Flare( 5, 32 ).color( 0xFF0000, true ).show( curUser.sprite, 2f );
					}
					//恐惧+GOO
					hero.spend(TIME_TO_DRINK);
					hero.busy();

					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					hero.sprite.operate(hero.pos);

					updateQuickslot();
				}


			}
		}

		//守护魔力III
		if (action.equals( AC_DRINK_FOUR )) {
			if (volume > 0) {

				float missingHealthPercent = 1f - (hero.HP / (float)hero.HT);

				int curShield = 0;
				if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
				int maxShield = Math.round(hero.HT *0.01f*hero.pointsInTalent(Talent.SHIELDING_DEW));
				if (hero.hasTalent(Talent.SHIELDING_DEW)){
					float missingShieldPercent = 1f - (curShield / (float)maxShield);
					missingShieldPercent *= 0.2f*hero.pointsInTalent(Talent.SHIELDING_DEW);
					if (missingShieldPercent > 0){
						missingHealthPercent += missingShieldPercent;
					}
				}

				//trimming off 0.01 drops helps with floating point errors
				int dropsNeeded = 7;
				dropsNeeded = (int)GameMath.gate(1, dropsNeeded, volume);

				if (Dewdrop.consumeDew(dropsNeeded, hero)){
					GLog.b( Messages.get(this, "healing_3") );
					volume -= dropsNeeded;
					Buff.affect(hero, Healing.class).setHeal((int) (0.3f * hero.HT + 7), 0.25f, 0);
					Buff.affect(hero, Haste.class, 10f);
					Buff.affect(hero, HaloFireImBlue.class).set( HaloFireImBlue.DURATION*0.8f );
					Buff.prolong(hero, RoseShiled.class, RoseShiled.DURATION/10f-3+depth/5);
					Buff.affect(hero, WellFed.class).dewial();
					Buff.affect( hero, MindVision.class, MindVision.DURATION/5f+depth/5 );
					//极速+火焰之力+玫瑰结界+灵视+饱腹+治疗
					//10+25+4+
					hero.spend(TIME_TO_DRINK);
					hero.busy();

					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					hero.sprite.operate(hero.pos);

					updateQuickslot();
				}


			}
		}

		//守护魔力 II
		if (action.equals( AC_DRINK_THREE )) {
			if (volume > 0) {

				float missingHealthPercent = 1f - (hero.HP / (float)hero.HT);

				int curShield = 0;
				if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
				int maxShield = Math.round(hero.HT *0.01f*hero.pointsInTalent(Talent.SHIELDING_DEW));
				if (hero.hasTalent(Talent.SHIELDING_DEW)){
					float missingShieldPercent = 1f - (curShield / (float)maxShield);
					missingShieldPercent *= 0.2f*hero.pointsInTalent(Talent.SHIELDING_DEW);
					if (missingShieldPercent > 0){
						missingHealthPercent += missingShieldPercent;
					}
				}

				//trimming off 0.01 drops helps with floating point errors
				int dropsNeeded = 5;
				dropsNeeded = (int)GameMath.gate(1, dropsNeeded, volume);

				if (Dewdrop.consumeDew(dropsNeeded, hero)){
					volume -= dropsNeeded;
					Buff.affect(hero, Haste.class, 15f);
					Buff.affect(hero, FireImbue.class).set( FireImbue.DURATION*0.5f );
					//极速+火焰之力 15回合+25回合
					hero.spend(TIME_TO_DRINK);
					hero.busy();

					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					hero.sprite.operate(hero.pos);

					updateQuickslot();
				}


			}
		}

		if (action.equals( AC_DRINK_TWO )) {
			if (volume > 0) {

				float missingHealthPercent = 1f - (hero.HP / (float)hero.HT);

				int curShield = 0;
				if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
				int maxShield = Math.round(hero.HT *0.01f*hero.pointsInTalent(Talent.SHIELDING_DEW));
				if (hero.hasTalent(Talent.SHIELDING_DEW)){
					float missingShieldPercent = 1f - (curShield / (float)maxShield);
					missingShieldPercent *= 0.2f*hero.pointsInTalent(Talent.SHIELDING_DEW);
					if (missingShieldPercent > 0){
						missingHealthPercent += missingShieldPercent;
					}
				}

				//trimming off 0.01 drops helps with floating point errors
				int dropsNeeded = 2;
				dropsNeeded = (int)GameMath.gate(1, dropsNeeded, volume);

				if (Dewdrop.consumeDew(dropsNeeded, hero)){
					volume -= dropsNeeded;
					Buff.affect(hero, Haste.class, 10f);
					//极速BUFF10回合
					hero.spend(TIME_TO_DRINK);
					hero.busy();

					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					hero.sprite.operate(hero.pos);

					updateQuickslot();
				}


			}
		}

		if (action.equals( AC_DRINK )) {

			if (volume > 0) {

				float missingHealthPercent = 1f - (hero.HP / (float)hero.HT);

				int curShield = 0;
				if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
				int maxShield = Math.round(hero.HT *0.01f*hero.pointsInTalent(Talent.SHIELDING_DEW));
				if (hero.hasTalent(Talent.SHIELDING_DEW)){
					float missingShieldPercent = 1f - (curShield / (float)maxShield);
					missingShieldPercent *= 0.2f*hero.pointsInTalent(Talent.SHIELDING_DEW);
					if (missingShieldPercent > 0){
						missingHealthPercent += missingShieldPercent;
					}
				}

				//trimming off 0.01 drops helps with floating point errors
				int dropsNeeded = (int)Math.ceil((missingHealthPercent / 0.05f) - 0.01f);
				dropsNeeded = (int)GameMath.gate(1, dropsNeeded, volume);

				if (Dewdrop.consumeDew(dropsNeeded, hero)){
					volume -= dropsNeeded;

					hero.spend(TIME_TO_DRINK);
					hero.busy();

					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					hero.sprite.operate(hero.pos);

					updateQuickslot();
				}


			} else {
				GLog.w( Messages.get(this, "empty") );
			}

		}
	}


	@Override
	public String info() {
		String info = desc();

		if (volume == 0){
			info += "\n\n" + Messages.get(this, "desc_water");
		} else {
			info += "\n\n" + Messages.get(this, "desc_heal");
		}

		if (isFull()){
			info += "\n\n" + Messages.get(this, "desc_full");
		}

		if(View == 3 && this.level() >= 0){
			info += "\n\n" + Messages.get(this, "magic_0");
		} else if (View == 2 && this.level() >= 1 ){
			info += "\n\n" + Messages.get(this, "magic_1");
		} else if (View == 1 && this.level() >= 2) {
			info += "\n\n" + Messages.get(this, "magic_2");
		}	else if (View == 0 && this.level() >= 4) {
			info += "\n\n" + Messages.get(this, "magic_3");
		}

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
			if (STRReq() > hero.STR()) {
				info += " " + Messages.get(Weapon.class, "too_heavy");
			} else if (hero.STR() > STRReq()){
				info += " " + Messages.get(Weapon.class, "excess_str", hero.STR() - STRReq());
			}
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
			if (STRReq(0) > hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
		}

		return info;
	}

	public void empty() {
		volume = 0;
		updateQuickslot();
	}

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

		GLog.i( Messages.get(this, "collected") );
		volume += dew.quantity;
		if (volume >= MAX_VOLUME) {
			volume = MAX_VOLUME;
			GLog.p( Messages.get(this, "full") );
		}

		updateQuickslot();
	}

	@Override
	public int level() {
			return (hero == null ? 0 : hero.lvl / 5) + (curseInfusionBonus ? 1 : 0);
	}
	//5级升1级
	public void fill() {
		volume = MAX_VOLUME;
		updateQuickslot();
	}

	@Override
	public String status() {
		return Messages.format( TXT_STATUS, volume, MAX_VOLUME );
	}

}
