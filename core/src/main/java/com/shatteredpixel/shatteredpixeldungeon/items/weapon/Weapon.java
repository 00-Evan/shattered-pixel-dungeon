/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Berserk;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.BodyForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyWeapon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Smite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ParchmentScrap;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Annoying;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Dazzling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Displacing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Explosive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Friendly;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Polarized;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Sacrificial;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Wayward;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
// 加入在 import 區域
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;

import java.util.ArrayList;
import java.util.Arrays;
import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
// 特效與文字相關
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
// T3 Buff 檢查 (用於觸發特效)
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.T3MasteryBuff;

abstract public class Weapon extends KindOfWeapon {

	public float    ACC = 1f;	// Accuracy modifier
	public float	DLY	= 1f;	// Speed modifier
	public int      RCH = 1;    // Reach modifier (only applies to melee hits)

	public enum Augment {
		SPEED   (0.7f, 2/3f),
		DAMAGE  (1.5f, 5/3f),
		NONE	(1.0f, 1f);

		private float damageFactor;
		private float delayFactor;

		Augment(float dmg, float dly){
			damageFactor = dmg;
			delayFactor = dly;
		}

		public int damageFactor(int dmg){
			return Math.round(dmg * damageFactor);
		}

		public float delayFactor(float dly){
			return dly * delayFactor;
		}
	}
	
	public Augment augment = Augment.NONE;
    // --- [NEW] 熟練度系統變數 ---
    public int masteryLevel = 0; // 當前熟練等級
    public int killCount = 0;    // 當前擊殺數
    // --------------------------

	protected int usesToID(){
		return 20;
	}
	protected float usesLeftToID = usesToID();
	protected float availableUsesToID = usesToID()/2f;
	
	public Enchantment enchantment;
	public boolean enchantHardened = false;
	public boolean curseInfusionBonus = false;
	public boolean masteryPotionBonus = false;
	
	@Override
	public int proc( Char attacker, Char defender, int damage ) {

        // --- [NEW] 熟練度特效與爆擊系統 ---
        if (usesMasterySystem() && attacker == Dungeon.hero) {
            int tier = getWeaponTier();
            boolean triggerVisuals = false;
            String hitText = "";
            int textColor = 0xFFFFFF; // 預設白色

            // 1. T1/T2 爆擊邏輯 (機率觸發)
            if (tier <= 2 && masteryLevel >= 5) {
                // 機率: (等級 - 3) * 2% (例如 Lv5 = 4%, Lv10 = 14%)
                float chance = (masteryLevel - 3) * 0.02f;
                if (chance > 0.5f) chance = 0.5f;
                if (Random.Float() < chance) {
                    damage = (int)(damage * 1.5f); // 傷害 1.5 倍
                    triggerVisuals = true;
                    hitText = "CRITICAL!";
                    textColor = 0xFF4444; // 紅色
                }
            }
            // 2. T3 穿透邏輯 (檢查是否有 Buff)
            else if (tier == 3 && attacker.buff(T3MasteryBuff.class) != null) {
                // 傷害邏輯已經在 Char.java 處理了(降防)，這裡只處理特效
                triggerVisuals = true;
                hitText = "PIERCE!";
                textColor = 0xFFAA00; // 金黃色
            }
            // 3. T4/T5 邏輯 (可以在此擴充)

            // --- 播放特效 ---
            if (triggerVisuals) {
                // A. 畫面震動 (強度 2, 時間 0.2秒)
                PixelScene.shake(2, 0.2f);

                // B. 播放重擊音效
                Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 1.2f, 1.2f);

                // C. 飄出文字 (在怪物頭頂)
                if (defender.sprite != null) {
                    FloatingText.show(defender.sprite.x, defender.sprite.y - 4, hitText, textColor);
                }

                // D. 噴出粒子特效 (星星)
                CellEmitter.center(defender.pos).burst(Speck.factory(Speck.STAR), 5);
            }
        }
        // ---------------------------------------------

		boolean becameAlly = false;
		boolean wasAlly = defender.alignment == Char.Alignment.ALLY;
		if (attacker.buff(MagicImmune.class) == null) {
			Enchantment trinityEnchant = null;
			//only when it's the hero or a char that uses the hero's weapon
			if (Dungeon.hero.buff(BodyForm.BodyFormBuff.class) != null && this instanceof MeleeWeapon
					&& (attacker == Dungeon.hero || attacker instanceof MirrorImage || attacker instanceof ShadowClone.ShadowAlly)){
				trinityEnchant = Dungeon.hero.buff(BodyForm.BodyFormBuff.class).enchant();
				if (enchantment != null && trinityEnchant != null && trinityEnchant.getClass() == enchantment.getClass()){
					trinityEnchant = null;
				}
			}

			if (attacker instanceof Hero && isEquipped((Hero) attacker)
					&& attacker.buff(HolyWeapon.HolyWepBuff.class) != null){
				if (enchantment != null &&
						(((Hero) attacker).subClass == HeroSubClass.PALADIN || hasCurseEnchant())){
					damage = enchantment.proc(this, attacker, defender, damage);
					if (defender.alignment == Char.Alignment.ALLY && !wasAlly){
						becameAlly = true;
					}
				}
				if (defender.isAlive() && !becameAlly && trinityEnchant != null){
					damage = trinityEnchant.proc(this, attacker, defender, damage);
				}
				if (defender.isAlive() && !becameAlly) {
					int dmg = ((Hero) attacker).subClass == HeroSubClass.PALADIN ? 6 : 2;
					defender.damage(Math.round(dmg * Enchantment.genericProcChanceMultiplier(attacker)), HolyWeapon.INSTANCE);
				}

			} else {
				if (enchantment != null) {
					damage = enchantment.proc(this, attacker, defender, damage);
					if (defender.alignment == Char.Alignment.ALLY && !wasAlly) {
						becameAlly = true;
					}
				}

				if (defender.isAlive() && !becameAlly && trinityEnchant != null){
					damage = trinityEnchant.proc(this, attacker, defender, damage);
				}
			}

			if (attacker instanceof Hero && isEquipped((Hero) attacker) &&
					attacker.buff(Smite.SmiteTracker.class) != null && !becameAlly){
				defender.damage(Smite.bonusDmg((Hero) attacker, defender), Smite.INSTANCE);
			}
		}

		//do not progress toward ID in the specific case of a missile weapon with no parent using
		// up it's last shot, as in this case there's nothing left to ID anyway
		if (this instanceof MissileWeapon
				&& ((MissileWeapon) this).durabilityLeft() <= ((MissileWeapon) this).durabilityPerUse()
				&& ((MissileWeapon) this).parent == null){
			return damage;
		}
		
		if (!levelKnown && attacker == Dungeon.hero) {
			float uses = Math.min( availableUsesToID, Talent.itemIDSpeedFactor(Dungeon.hero, this) );
			availableUsesToID -= uses;
			usesLeftToID -= uses;
			if (usesLeftToID <= 0) {
				if (ShardOfOblivion.passiveIDDisabled()){
					if (usesLeftToID > -1){
						GLog.p(Messages.get(ShardOfOblivion.class, "identify_ready"), name());
					}
					setIDReady();
				} else {
					identify();
					GLog.p(Messages.get(Weapon.class, "identify"));
					Badges.validateItemLevelAquired(this);
				}
			}
		}

		return damage;
	}
	
	public void onHeroGainExp( float levelPercent, Hero hero ){
		levelPercent *= Talent.itemIDSpeedFactor(hero, this);
		if (!levelKnown && (isEquipped(hero) || this instanceof MissileWeapon)
				&& availableUsesToID <= usesToID()/2f) {
			//gains enough uses to ID over 0.5 levels
			availableUsesToID = Math.min(usesToID()/2f, availableUsesToID + levelPercent * usesToID());
		}
	}
	
	private static final String USES_LEFT_TO_ID = "uses_left_to_id";
	private static final String AVAILABLE_USES  = "available_uses";
	private static final String ENCHANTMENT	    = "enchantment";
	private static final String ENCHANT_HARDENED = "enchant_hardened";
	private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
	private static final String MASTERY_POTION_BONUS = "mastery_potion_bonus";
	private static final String AUGMENT	        = "augment";
    // --- [NEW] 存檔 Key ---
    private static final String MASTERY_LEVEL = "mastery_level";
    private static final String KILL_COUNT = "kill_count";
    // ---------------------

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( USES_LEFT_TO_ID, usesLeftToID );
		bundle.put( AVAILABLE_USES, availableUsesToID );
		bundle.put( ENCHANTMENT, enchantment );
		bundle.put( ENCHANT_HARDENED, enchantHardened );
		bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
		bundle.put( MASTERY_POTION_BONUS, masteryPotionBonus );
		bundle.put( AUGMENT, augment );
        // --- [NEW] 儲存熟練度 ---
        bundle.put( MASTERY_LEVEL, masteryLevel );
        bundle.put( KILL_COUNT, killCount );
        // ----------------------
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		usesLeftToID = bundle.getFloat( USES_LEFT_TO_ID );
		availableUsesToID = bundle.getFloat( AVAILABLE_USES );
		enchantment = (Enchantment)bundle.get( ENCHANTMENT );
		enchantHardened = bundle.getBoolean( ENCHANT_HARDENED );
		curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );
		masteryPotionBonus = bundle.getBoolean( MASTERY_POTION_BONUS );

		augment = bundle.getEnum(AUGMENT, Augment.class);
        // --- [NEW] 讀取熟練度 ---
        masteryLevel = bundle.getInt( MASTERY_LEVEL );
        killCount = bundle.getInt( KILL_COUNT );
        // ----------------------
	}
	
	@Override
	public void reset() {
		super.reset();
		usesLeftToID = usesToID();
		availableUsesToID = usesToID()/2f;
	}

	@Override
	public boolean collect(Bag container) {
		if(super.collect(container)){
			if (Dungeon.hero != null && Dungeon.hero.isAlive() && isIdentified() && enchantment != null){
				Catalog.setSeen(enchantment.getClass());
				Statistics.itemTypesDiscovered.add(enchantment.getClass());
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Item identify(boolean byHero) {
		if (enchantment != null && byHero && Dungeon.hero != null && Dungeon.hero.isAlive()){
			Catalog.setSeen(enchantment.getClass());
			Statistics.itemTypesDiscovered.add(enchantment.getClass());
		}
		return super.identify(byHero);
	}

	public void setIDReady(){
		usesLeftToID = -1;
	}

	public boolean readyToIdentify(){
		return !isIdentified() && usesLeftToID <= 0;
	}
	
	@Override
	public float accuracyFactor(Char owner, Char target) {
		
		int encumbrance = 0;
		
		if( owner instanceof Hero ){
			encumbrance = STRReq() - ((Hero)owner).STR();
		}

		float ACC = this.ACC;

		if (owner.buff(Wayward.WaywardBuff.class) != null && enchantment instanceof Wayward){
			ACC /= 5;
		}

		return encumbrance > 0 ? (float)(ACC / Math.pow( 1.5, encumbrance )) : ACC;
	}
	
	@Override
	public float delayFactor( Char owner ) {
		return baseDelay(owner) * (1f/speedMultiplier(owner));
	}

	protected float baseDelay( Char owner ){
		float delay = augment.delayFactor(this.DLY);
		if (owner instanceof Hero) {
			int encumbrance = STRReq() - ((Hero)owner).STR();
			if (encumbrance > 0){
				delay *= Math.pow( 1.2, encumbrance );
			}
		}

		return delay;
	}

	protected float speedMultiplier(Char owner ){
		float multi = RingOfFuror.attackSpeedMultiplier(owner);

		if (owner.buff(Scimitar.SwordDance.class) != null){
			multi += 0.6f;
		}

		return multi;
	}

	@Override
	public int reachFactor(Char owner) {
		int reach = RCH;
		if (owner instanceof Hero && RingOfForce.fightingUnarmed((Hero) owner)){
			reach = 1; //brawlers stance benefits from enchantments, but not innate reach
			if (!RingOfForce.unarmedGetsWeaponEnchantment((Hero) owner)){
				return reach;
			}
		}
		if (owner instanceof Hero && owner.buff(AscendedForm.AscendBuff.class) != null){
			reach += 2;
		}
		if (hasEnchant(Projecting.class, owner)){
			return reach + Math.round(Enchantment.genericProcChanceMultiplier(owner));
		} else {
			return reach;
		}
	}

	public int STRReq(){
		return STRReq(level());
	}

	public abstract int STRReq(int lvl);

	protected static int STRReq(int tier, int lvl){
		lvl = Math.max(0, lvl);

		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}

	@Override
	public int level() {
		int level = super.level();
		if (curseInfusionBonus) level += 1 + level/6;
		return level;
	}
	
	@Override
	public Item upgrade() {
		return upgrade(false);
	}
	
	public Item upgrade(boolean enchant ) {

		if (enchant){
			if (enchantment == null){
				enchant(Enchantment.random());
			}
		} else if (enchantment != null) {
			//chance to lose harden buff is 10/20/40/80/100% when upgrading from +6/7/8/9/10
			if (enchantHardened){
				if (level() >= 6 && Random.Float(10) < Math.pow(2, level()-6)){
					enchantHardened = false;
				}

			//chance to remove curse is a static 33%
			} else if (hasCurseEnchant()) {
				if (Random.Int(3) == 0) enchant(null);

			//otherwise chance to lose enchant is 10/20/40/80/100% when upgrading from +4/5/6/7/8
			} else if (level() >= 4 && Random.Float(10) < Math.pow(2, level()-4)){
				enchant(null);
			}
		}
		
		cursed = false;

		return super.upgrade();
	}
	
	@Override
	public String name() {
		if (isEquipped(Dungeon.hero) && !hasCurseEnchant() && Dungeon.hero.buff(HolyWeapon.HolyWepBuff.class) != null
			&& (Dungeon.hero.subClass != HeroSubClass.PALADIN || enchantment == null)){
				return Messages.get(HolyWeapon.class, "ench_name", super.name());
			} else {
				return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name(super.name()) : super.name();

		}
	}
	
	@Override
	public Item random() {
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		int n = 0;
		if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}
		level(n);

		//we use a separate RNG here so that variance due to things like parchment scrap
		//does not affect levelgen
		Random.pushGenerator(Random.Long());

			//30% chance to be cursed
			//10% chance to be enchanted
			float effectRoll = Random.Float();
			if (effectRoll < 0.3f * ParchmentScrap.curseChanceMultiplier()) {
				enchant(Enchantment.randomCurse());
				cursed = true;
			} else if (effectRoll >= 1f - (0.1f * ParchmentScrap.enchantChanceMultiplier())){
				enchant();
			}

		Random.popGenerator();

		return this;
	}
	
	public Weapon enchant( Enchantment ench ) {
		if (ench == null || !ench.curse()) curseInfusionBonus = false;
		enchantment = ench;
		updateQuickslot();
		if (ench != null && isIdentified() && Dungeon.hero != null
				&& Dungeon.hero.isAlive() && Dungeon.hero.belongings.contains(this)){
			Catalog.setSeen(ench.getClass());
			Statistics.itemTypesDiscovered.add(ench.getClass());
		}
		return this;
	}

	public Weapon enchant() {

		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random( oldEnchantment );

		return enchant( ench );
	}

	public boolean hasEnchant(Class<?extends Enchantment> type, Char owner) {
		if (owner.buff(MagicImmune.class) != null) {
			return false;
		} else if (enchantment != null
				&& !enchantment.curse()
				&& owner instanceof Hero
				&& isEquipped((Hero) owner)
				&& owner.buff(HolyWeapon.HolyWepBuff.class) != null
				&& ((Hero) owner).subClass != HeroSubClass.PALADIN) {
			return false;
		} else if (owner.buff(BodyForm.BodyFormBuff.class) != null
				&& owner.buff(BodyForm.BodyFormBuff.class).enchant() != null
				&& owner.buff(BodyForm.BodyFormBuff.class).enchant().getClass().equals(type)){
			return true;
		} else if (enchantment != null) {
			return enchantment.getClass() == type;
		} else {
			return false;
		}
	}
	
	//these are not used to process specific enchant effects, so magic immune doesn't affect them
	public boolean hasGoodEnchant(){
		return enchantment != null && !enchantment.curse();
	}

	public boolean hasCurseEnchant(){
		return enchantment != null && enchantment.curse();
	}

	private static ItemSprite.Glowing HOLY = new ItemSprite.Glowing( 0xFFFF00 );

	@Override
	public ItemSprite.Glowing glowing() {
		if (isEquipped(Dungeon.hero) && !hasCurseEnchant() && Dungeon.hero.buff(HolyWeapon.HolyWepBuff.class) != null
				&& (Dungeon.hero.subClass != HeroSubClass.PALADIN || enchantment == null)){
			return HOLY;
		} else {
			return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.glowing() : null;
		}
	}
    // --- [NEW] 判斷是否啟用熟練度 ---
    // 只有「近戰武器 (MeleeWeapon)」且「不是法師之杖 (MagesStaff)」才啟用
    public boolean usesMasterySystem() {
        return (this instanceof MeleeWeapon) && !(this instanceof MagesStaff);
    }
    // --- [NEW] 取得武器階級 (Tier) ---
    public int getWeaponTier() {
        // 利用基礎力量需求反推 Tier
        // T1=10, T2=12, T3=14, T4=16, T5=18
        int baseStr = STRReq(0);
        int tier = (baseStr - 8) / 2;

        // 限制範圍在 1~5 之間 (防呆)
        if (tier < 1) return 1;
        if (tier > 5) return 5;
        return tier;
    }
    // ------------------------------

    // --- [NEW] 熟練度核心邏輯 ---
    // 1. 計算下一級需要的擊殺數
    public int killsNeededForMastery() {
        int baseKills;
        int tier = getWeaponTier(); // 呼叫上面的新方法

        if (tier <= 2) {
            baseKills = 5; // T1, T2
        } else if (tier <= 4) {
            baseKills = 15; // T3, T4
        } else {
            baseKills = 30; // T5
        }

        return (int) (baseKills * Math.pow(1.2, masteryLevel));
    }

    // 2. 增加擊殺數 (這要在 Hero.java 裡呼叫)
    public void incrementKillCount() {
        // [修改] 如果不適用熟練度系統，直接離開
        if (!usesMasterySystem()) return;
        // --- [NEW] 防刷怪限制：樓層上限 ---
        // 如果熟練度等級已經 >= 當前樓層數，就不再增加擊殺數
        // Dungeon.depth 是當前樓層 (1, 2, 3...)
        if (masteryLevel >= Dungeon.depth) {
            return;
        }
        killCount++;
        if (killCount >= killsNeededForMastery()) {
            levelUpMastery();
        }
    }

    // 3. 升級執行
    public void levelUpMastery() {
        killCount = 0; // 歸零 (或者你可以設計保留溢出的擊殺數)
        masteryLevel++;
        // 可以在這裡加入 GLog.p("你的武器變得更順手了！");
        GLog.p( "Weapon mastery increased to level " + masteryLevel + "!" );
    }

    // 4. 傷害加成 (覆寫 max 方法)
    // 這裡我們簡單設定：每 1 級熟練度 +1 最大傷害 (你可以改強一點)
    @Override
    public int min() {
        int damage = super.min();

        // [修正] 這裡也要加上判斷，確保只有近戰武器生效
        if (usesMasterySystem()) {
            // 每 2 級熟練度 +1 最小傷害
            damage += masteryLevel / 2;
        }

        return damage;
    }

    @Override
    public int max() {
        int damage = super.max();

        // 只有適用系統的武器才加傷害
        if (usesMasterySystem()) {
            // 每 1 級熟練度 +1 最大傷害
            damage += masteryLevel;
        }

        return damage;
    }

    // 5. 修改描述 (讓玩家看得到進度 + T3 狀態)
    @Override
    public String desc() {
        String originalDesc = super.desc();
        if (!usesMasterySystem()) return originalDesc;

        StringBuilder sb = new StringBuilder(originalDesc);

        sb.append("\n\n");
        sb.append("Mastery Level: ").append(masteryLevel).append("\n");
        sb.append("Progress: ").append(killCount).append(" / ").append(killsNeededForMastery());

        // --- [NEW] T3 專屬顯示邏輯 ---
        if (getWeaponTier() == 3) {
            sb.append("\n"); // 換行

            // 檢查冷卻時間
            // 注意：這裡假設你在 Hero.java 已經定義了 t3CooldownTime 和 t3ComboCounter
            if (Dungeon.hero.t3CooldownTime > Actor.now()) {
                int turnsLeft = (int)(Dungeon.hero.t3CooldownTime - Actor.now());
                sb.append("Cooldown: ").append(turnsLeft).append(" turns");
            } else {
                // 顯示連擊數
                sb.append("Combo: ");
                if (Dungeon.hero.t3ComboCounter >= 10) {
                    sb.append("READY (Use weapon to activate)");
                } else {
                    sb.append(Dungeon.hero.t3ComboCounter).append(" / 10");
                }
            }
        }
        return sb.toString();
    }
    // --------------------------

    // --- [NEW] 自訂按鈕系統 ---

    // 1. 定義按鈕名稱
    public static final String AC_ACTIVATE = "ACTIVATE";

    // 2. 告訴遊戲：這把武器有哪些操作選項？
    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        // 只有當：是 T3 武器 + 已裝備 + 連擊滿了 + 沒有冷卻
        if (getWeaponTier() == 3 && isEquipped(hero)
                && hero.t3ComboCounter >= 10
                && hero.t3CooldownTime <= Actor.now()) {

            // 在選單中加入 "ACTIVATE" 按鈕
            actions.add(AC_ACTIVATE);
        }

        return actions;
    }

    // 3. 當玩家點擊按鈕時，執行什麼？
    @Override
    public void execute(Hero hero, String action) {

        if (action.equals(AC_ACTIVATE)) {
            // --- 執行 T3 技能邏輯 ---

            // A. 設定冷卻 (60回合)
            hero.t3CooldownTime = Actor.now() + 60;

            // B. 重置連擊
            hero.t3ComboCounter = 0;

            // C. 顯示訊息 (測試用)
            GLog.p("Controlled Fury Activated!");

            // D. 播放音效
            // Sample.INSTANCE.play(Assets.Sounds.CLICK);

            // E. 施加 Buff (等你寫好 T3MasteryBuff 後再取消註解下面這行)
            com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.affect(hero, T3MasteryBuff.class).setup(hero, masteryLevel);

            // 關閉背包視窗，回到遊戲
            hero.spendAndNext(1f); // 花費 1 回合時間

        } else {
            // 如果不是我們的按鈕，就執行預設動作 (例如裝備/卸下)
            super.execute(hero, action);
        }
    }

	public static abstract class Enchantment implements Bundlable {

		public static final Class<?>[] common = new Class<?>[]{
				Blazing.class, Chilling.class, Kinetic.class, Shocking.class};

		public static final Class<?>[] uncommon = new Class<?>[]{
				Blocking.class, Blooming.class, Elastic.class,
				Lucky.class, Projecting.class, Unstable.class};

		public static final Class<?>[] rare = new Class<?>[]{
				Corrupting.class, Grim.class, Vampiric.class};

		public static final float[] typeChances = new float[]{
				50, //12.5% each
				40, //6.67% each
				10  //3.33% each
		};

		public static final Class<?>[] curses = new Class<?>[]{
				Annoying.class, Displacing.class, Dazzling.class, Explosive.class,
				Sacrificial.class, Wayward.class, Polarized.class, Friendly.class
		};
		
			
		public abstract int proc( Weapon weapon, Char attacker, Char defender, int damage );

		protected float procChanceMultiplier( Char attacker ){
			return genericProcChanceMultiplier( attacker );
		}

		public static float genericProcChanceMultiplier( Char attacker ){
			float multi = RingOfArcana.enchantPowerMultiplier(attacker);
			Berserk rage = attacker.buff(Berserk.class);
			if (rage != null) {
				multi = rage.enchantFactor(multi);
			}

			if (attacker.buff(RunicBlade.RunicSlashTracker.class) != null){
				multi += attacker.buff(RunicBlade.RunicSlashTracker.class).boost;
				attacker.buff(RunicBlade.RunicSlashTracker.class).detach();
			}

			if (attacker.buff(Smite.SmiteTracker.class) != null){
				multi += 3f;
			}

			if (attacker.buff(ElementalStrike.DirectedPowerTracker.class) != null){
				multi += attacker.buff(ElementalStrike.DirectedPowerTracker.class).enchBoost;
				attacker.buff(ElementalStrike.DirectedPowerTracker.class).detach();
			}

			if (attacker.buff(Talent.SpiritBladesTracker.class) != null
					&& ((Hero)attacker).pointsInTalent(Talent.SPIRIT_BLADES) == 4){
				multi += 0.1f;
			}
			if (attacker.buff(Talent.StrikingWaveTracker.class) != null
					&& ((Hero)attacker).pointsInTalent(Talent.STRIKING_WAVE) == 4){
				multi += 0.2f;
			}

			return multi;
		}

		public String name() {
			if (!curse())
				return name( Messages.get(this, "enchant"));
			else
				return name( Messages.get(Item.class, "curse"));
		}

		public String name( String weaponName ) {
			return Messages.get(this, "name", weaponName);
		}

		public String desc() {
			return Messages.get(this, "desc");
		}

		public boolean curse() {
			return false;
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}
		
		public abstract ItemSprite.Glowing glowing();
		
		@SuppressWarnings("unchecked")
		public static Enchantment random( Class<? extends Enchantment> ... toIgnore ) {
			switch(Random.chances(typeChances)){
				case 0: default:
					return randomCommon( toIgnore );
				case 1:
					return randomUncommon( toIgnore );
				case 2:
					return randomRare( toIgnore );
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomCommon( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(common));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomUncommon( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(uncommon));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment randomRare( Class<? extends Enchantment> ... toIgnore ) {
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(rare));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}

		@SuppressWarnings("unchecked")
		public static Enchantment randomCurse( Class<? extends Enchantment> ... toIgnore ){
			ArrayList<Class<?>> enchants = new ArrayList<>(Arrays.asList(curses));
			enchants.removeAll(Arrays.asList(toIgnore));
			if (enchants.isEmpty()) {
				return random();
			} else {
				return (Enchantment) Reflection.newInstance(Random.element(enchants));
			}
		}
		
	}
}
