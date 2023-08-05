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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.Brew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.Elixir;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfPolarity;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CthulhuGirlStatue extends Artifact {

	//private static final int MAX_charge	= 100;
	private static final int MAX_LUCKY	= 30;//0-10霉运，11-20中等幸运，21-30幸运

	private static final String AC_SACRIFICE	= "SACRIFICE";
	private static final String AC_SWITCH	= "SWITCH";

	private static final float TIME_TO_SACRIFICE = 1f;

	private static final String TXT_STATUS	= "%d/%d";

	{
		switch ( maptype ){
			case 1:image = ItemSpriteSheet.CTHULHUGIRLSTATUE;
				break;
			case 2:image = ItemSpriteSheet.CTHULHUGIRLSTATUE2;
				break;
			case 3:image = ItemSpriteSheet.CTHULHUGIRLSTATUE3;
				break;
			case 4: default:image = ItemSpriteSheet.CTHULHUGIRLSTATUE4;
				break;
		}

		exp = 0;
		levelCap = 1;

		charge = 0;
		partialCharge = 0;
		chargeCap = 100;

		defaultAction = AC_SACRIFICE;

		bones = false;
		unique = true;

		cursed = false;
	}

	public int charge = 0;
	protected int lucky = 15;
	protected int accumulation = 0;
	protected int almostdie = 0;
	public boolean elementpower = false;

	private static int depthValue = 1; // 初始化为1
	private static int mobcountValue = 0; // 初始化为0
	private static int herolevel = 1; // 初始化为1
	private static int maptype = 4;
	private static int stengthchanged = 2;

	private static final String LUCKY			= "lucky";
	private static final String ACCUMULATION	= "accumulation";
	private static final String ALMOSTDIE	= "almostdie";
	private static final String ELEMENTPOWER    = "elementpower";

	private static final String MAPTYPE    = "maptype";
	private static final String STENGTHCHANGED = "stengthchanged" ;

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		//bundle.put( volume, volume );
		bundle.put( LUCKY, lucky );
		bundle.put( ACCUMULATION, accumulation );
		bundle.put( ALMOSTDIE, almostdie );
		bundle.put( ELEMENTPOWER, elementpower );
		bundle.put( MAPTYPE, maptype );
		bundle.put( STENGTHCHANGED, stengthchanged );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		//volume	= bundle.getInt( volume );
		lucky			= bundle.getInt( LUCKY );
		accumulation	= bundle.getInt( ACCUMULATION );
		almostdie		= bundle.getInt( ALMOSTDIE );
		elementpower	= bundle.getBoolean( ELEMENTPOWER );
		maptype			= bundle.getInt( MAPTYPE );
		stengthchanged	= bundle.getInt( STENGTHCHANGED );
	}

	@Override
	protected Artifact.ArtifactBuff passiveBuff() {
		return new statueRecharge();
	}

	public class statueRecharge extends ArtifactBuff {

		@Override
		public boolean act() {
			int chargeTarget = 5+(level()*2);
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeTarget
					&& !cursed
					&& target.buff(MagicImmune.class) == null
					&& (lock == null || lock.regenOn())) {
				//gains a charge in 40 - 2*missingCharge turns
				float chargeGain = (1 / (40f - (chargeTarget - charge)*2f));
				chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
				partialCharge += chargeGain;
			} else if (cursed && Random.Int(100) == 0){
				Buff.prolong( target, Cripple.class, 10f);
			}
			if (partialCharge >= 1) {
				partialCharge --;
				charge ++;
			}
			updateQuickslot();
			spend( TICK );
			return true;
		}

		public void gainExp(float levelPortion) {
			if ( target.buff(MagicImmune.class) != null || levelPortion == 0)
				return;

			exp += Math.round(levelPortion * 1000);

			//past the soft charge cap, gaining  charge from leveling is slowed.


			partialCharge += levelPortion * 6f;

		}
	}


	@Override
	public ArrayList<String> actions( Hero hero ) {

		ArrayList<String> actions = super.actions( hero );
		/*	代码移动到了Hero里
			if( hero.curAction instanceof HeroAction.OpenChest && hero.belongings.getItem( CthulhuGirlStatue.class ) != null ){
				CthulhuGirlStatue cthulhustatue = hero.belongings.getItem( CthulhuGirlStatue.class );
				cthulhustatue.charge += 10;
				MysteriousPowers( hero );
				GLog.w("enchantment");
			}

			if( hero.curAction instanceof HeroAction.Unlock && hero.belongings.getItem( CthulhuGirlStatue.class ) != null ){
				CthulhuGirlStatue cthulhustatue = hero.belongings.getItem( CthulhuGirlStatue.class );
				cthulhustatue.charge += 20;
				MysteriousPowers( hero );
				GLog.w("enchantment");
			}

			if( hero.curAction instanceof HeroAction.Buy && hero.belongings.getItem( CthulhuGirlStatue.class ) != null ){
				CthulhuGirlStatue cthulhustatue = hero.belongings.getItem( CthulhuGirlStatue.class );
				cthulhustatue.charge += 10;
				GLog.w("enchantment");
			}

			if( hero.curAction instanceof HeroAction.Interact && hero.belongings.getItem( CthulhuGirlStatue.class ) != null ){
				CthulhuGirlStatue cthulhustatue = hero.belongings.getItem( CthulhuGirlStatue.class );
				cthulhustatue.charge += 10;
				MysteriousPowers( hero );
				GLog.w("enchantment");
			}

			if( hero.curAction instanceof HeroAction.Alchemy && hero.belongings.getItem( CthulhuGirlStatue.class ) != null ){
				CthulhuGirlStatue cthulhustatue = hero.belongings.getItem( CthulhuGirlStatue.class );
				cthulhustatue.charge += 5;
				MysteriousPowers( hero );
				GLog.w("enchantment");
			}
		 */

			CthulhuGirlStatue cthulhustatue = hero.belongings.getItem( CthulhuGirlStatue.class );
			if (Dungeon.depth == depthValue) {
				cthulhustatue.charge += 10;
				MysteriousPowers(hero);
				depthValue = Dungeon.depth;
			}
			if (Dungeon.level.mobCount() != mobcountValue) {
				cthulhustatue.charge += 10;
				mobcountValue = Dungeon.level.mobCount();
			}
			if (Dungeon.hero.lvl != herolevel) {
				cthulhustatue.charge += 10;
				MysteriousPowers(hero);
				herolevel = hero.lvl;
			}


		actions.add( AC_SACRIFICE );
		actions.add( AC_SWITCH );
		actions.remove( AC_THROW );
		actions.remove( AC_DROP );

		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_SACRIFICE )) {
			curItem = this;
			GameScene.selectItem(sacrificeSelector);
			MysteriousPowers( hero );
			Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
		}

		if (action.equals( AC_SWITCH )) {
			maptype += 1;
			if( maptype >= 5 )maptype = 1;
			GLog.i(Messages.get(this, "switch"));
			GameScene.flash( 0x80FFFFFF );

			Sample.INSTANCE.play( Assets.Sounds.SCAN );

			switch ( maptype ){
				case 1:image = ItemSpriteSheet.CTHULHUGIRLSTATUE;
					break;
				case 2:image = ItemSpriteSheet.CTHULHUGIRLSTATUE2;
					break;
				case 3:image = ItemSpriteSheet.CTHULHUGIRLSTATUE3;
					break;
				case 4: default:image = ItemSpriteSheet.CTHULHUGIRLSTATUE4;
					break;
			}

		}

	}

	protected static WndBag.ItemSelector sacrificeSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return  Messages.get(this, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return (item instanceof Potion//药水
				| item instanceof Scroll//卷轴
				| item instanceof Starflower.Seed//星陨花
				| item instanceof CorpseDust//尸尘
				| item instanceof Rotberry.Seed//腐莓
				| item instanceof Embers//元素余烬
				| item instanceof StoneOfEnchantment//附魔符石
				| item instanceof StoneOfAugmentation//强化符石
				)& item.isIdentified()
			;
		}

		@Override
		public void onSelect( Item item ) {
			CthulhuGirlStatue cthulhustatue = (CthulhuGirlStatue) curItem;

			if ( item != null ) {
				Dungeon.hero.spend( TIME_TO_SACRIFICE );
				item.detach(Dungeon.hero.belongings.backpack);
				GLog.w(Messages.get(CthulhuGirlStatue.class, "sacrifice"));

			if ( item instanceof Starflower.Seed ){
				Buff.affect( Dungeon.hero, Bless.class,Bless.DURATION );
				cthulhustatue.charge += 20;
				cthulhustatue.lucky += 15;
				GLog.w(Messages.get(CthulhuGirlStatue.class, "starflower"));
			}
			if ( item instanceof CorpseDust ){
				cthulhustatue.charge += 20;
				cthulhustatue.lucky += 30;
			}
			if ( item instanceof Rotberry.Seed ){
				Dungeon.hero.STR += 2;
				GLog.w(Messages.get(CthulhuGirlStatue.class, "rotberry"));
			}
			if ( item instanceof Embers ){
				cthulhustatue.elementpower = true;
				GLog.w(Messages.get(CthulhuGirlStatue.class, "embers"));
			}
			if ( item instanceof StoneOfEnchantment ){
				cthulhustatue.lucky += 3;
				((Weapon)Dungeon.hero.belongings.weapon).enchant();
				((Armor)Dungeon.hero.belongings.armor).inscribe();
				GLog.w(Messages.get(CthulhuGirlStatue.class, "enchantment"));
			}
			if ( item instanceof StoneOfAugmentation ){
				cthulhustatue.lucky -= Math.min(5, cthulhustatue.lucky);
				StoneOfPolarity polarity = new StoneOfPolarity();
				polarity.quantity(1).collect();
				GLog.w(Messages.get(CthulhuGirlStatue.class, "augmentation"));
			}
			if ( item instanceof Potion ){
				cthulhustatue.charge += 10;
				cthulhustatue.lucky += 5;


				if (item instanceof ExoticPotion) {
					cthulhustatue.charge += 5;
					cthulhustatue.lucky += 3;
				}
				if (item instanceof PotionOfExperience) {
					cthulhustatue.lucky += 25;
					Buff.affect( Dungeon.hero, Bless.class,100f );
					GLog.w(Messages.get(CthulhuGirlStatue.class, "experience"));
				}
				if (item instanceof PotionOfPurity) {
					if( cthulhustatue.lucky <= 15 ) cthulhustatue.lucky = 15;
					PotionOfHealing.cure(Dungeon.hero);
					GLog.w(Messages.get(CthulhuGirlStatue.class, "purity"));
				}
				if (item instanceof PotionOfParalyticGas) {
					cthulhustatue.lucky -= 5;
					Buff.affect(Dungeon.hero, Barkskin.class).set( 2 + Dungeon.hero.lvl/6, 20 );
				}
				if (item instanceof PotionOfLevitation) {
					cthulhustatue.lucky += 2;
				}

			}

			if ( item instanceof Scroll ){
				cthulhustatue.charge += 10;
				cthulhustatue.lucky += 5;

				if ( item instanceof ExoticScroll ) {
					cthulhustatue.charge += 5;
					cthulhustatue.lucky += 5;
				}
				if ( item instanceof ScrollOfTransmutation ) {

					GLog.w(Messages.get(CthulhuGirlStatue.class, "transmutation"));
				}
				if ( item instanceof ScrollOfUpgrade ) {
					cthulhustatue.lucky -= 10;
					int upgrade;
					ArrayList<Item> upgradeable = new ArrayList<>();
					for (Item i : Dungeon.hero.belongings.backpack.items) {
						if ( (i instanceof MeleeWeapon || i instanceof Ring || i instanceof Armor || i instanceof Wand )) {
							upgradeable.add(i);
						}
					}
					if ( Dungeon.hero.belongings.armor != null) upgradeable.add(Dungeon.hero.belongings.armor);
					if ( Dungeon.hero.belongings.weapon != null) upgradeable.add(Dungeon.hero.belongings.weapon);
					if ( Dungeon.hero.belongings.ring != null ) upgradeable.add(Dungeon.hero.belongings.ring);
					if ( Dungeon.hero.belongings.ring != null && Dungeon.hero.belongings.misc instanceof Ring ) upgradeable.add(Dungeon.hero.belongings.misc);
					if ( !upgradeable.isEmpty() ){
						for( upgrade = Math.round( upgradeable.size()/3); upgrade !=0; upgrade -= 1) Random.element( upgradeable ).upgrade();
					}
					GLog.w(Messages.get(CthulhuGirlStatue.class, "upgrade"));
				}
				if ( item instanceof ScrollOfLullaby ) {
					cthulhustatue.lucky += 10;
					Buff.affect( Dungeon.hero,Drowsy.class );
					GLog.w(Messages.get(CthulhuGirlStatue.class, "lullaby"));
				}
				if ( item instanceof ScrollOfMagicMapping ) {
					cthulhustatue.lucky += 5;
				}
				if ( item instanceof ScrollOfRetribution ) {
					cthulhustatue.lucky += 3;
				}
				if ( item instanceof ScrollOfTerror ) {
					cthulhustatue.lucky += 3;
				}
				if ( item instanceof ScrollOfRage ) {
					cthulhustatue.lucky += 3;
				}



			}


				if ( cthulhustatue.lucky > 30 ){
					int healing = cthulhustatue.lucky - 30;
					GLog.w(Messages.get(CthulhuGirlStatue.class, "happy"));
					Dungeon.hero.HP += Math.min( healing,Dungeon.hero.HT-Dungeon.hero.HP );
					cthulhustatue.lucky = 30;
					Sample.INSTANCE.play(Assets.Sounds.MASTERY);
				}
				if ( cthulhustatue.lucky < 0 ){
					GLog.w(Messages.get(CthulhuGirlStatue.class, "mad"));
					int damage = 20 + Dungeon.hero.lvl;
					if( cthulhustatue.almostdie > 2 ){
						Dungeon.hero.HP -= Math.min( damage,Dungeon.hero.HP - 1 );
						if( damage > Dungeon.hero.HP - 1 ){
							Dungeon.hero.die( cthulhustatue );
						}
					}else{
						Dungeon.hero.HP -= Math.min( damage,Dungeon.hero.HP - 1 );
						if( damage > Dungeon.hero.HP - 1 ){
							cthulhustatue.almostdie += 1;
							GLog.w(Messages.get(CthulhuGirlStatue.class, "forgive"));
						}
					}
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
					cthulhustatue.lucky = 0;
				}

			}
		}
	};

	enum Luck{
		VERYLUCKY,
		QUITELUCKY,
		LUCKY,
		UNLUCKY,
		QUITEUNLUCKY,
		VERYUNLUCKY,
		NULL
	}
	private static Luck luck;

	public static boolean MysteriousPowers( Char ch ) {
		return cthulhu( ch, CthulhuGirlStatue.class );
	}

	public static boolean cthulhu(Char ch, Class source ) {
		CthulhuGirlStatue cthulhustatue = hero.belongings.getItem( CthulhuGirlStatue.class );
		int lucky = cthulhustatue.lucky;
		int NUMBER = Random.Int( 1,5 );
		int LUCKY  = Random.Int( 0,30 );
		int volume = Random.Int( 0,100 );

			if( lucky-LUCKY >= 25 ){
				luck = Luck.VERYLUCKY;
			} else if ( lucky-LUCKY >= 10 ) {
				luck = Luck.QUITELUCKY;
			} else if ( lucky >= LUCKY  ) {
				luck = Luck.LUCKY;
			} else if ( LUCKY-lucky >= 25 ) {
				luck = Luck.VERYUNLUCKY;
			} else if ( LUCKY-lucky >= 10 ) {
				luck = Luck.QUITEUNLUCKY;
			} else if( lucky <= LUCKY ){
				luck = Luck.UNLUCKY;
			}


		ArrayList<Item> identifyable = new ArrayList<>();
		ArrayList<Item> identifydisable = new ArrayList<>();
		ArrayList<Item> IDed = new ArrayList<>();
		for ( Item i : Dungeon.hero.belongings.backpack.items ) {
			if ( i.isIdentified() ) {
				identifydisable.add(i);
			}else{
				identifyable.add(i);
			}
		}
		if( !hero.belongings.weapon.isIdentified() )identifyable.add( hero.belongings.weapon );
		if( !hero.belongings.armor.isIdentified() )identifyable.add( hero.belongings.armor );
		if( hero.belongings.ring !=null && !hero.belongings.ring.isKnown() )identifyable.add( hero.belongings.ring );
		if( hero.belongings.misc !=null && !hero.belongings.misc.isIdentified() )identifyable.add( hero.belongings.misc );

		ArrayList<Item> transable = new ArrayList<>();
		ArrayList<Item> multitransable = new ArrayList<>();
		for ( Item i : Dungeon.hero.belongings.backpack.items ) {
			if ( i instanceof MeleeWeapon || i instanceof Ring  || i instanceof Wand || i instanceof Artifact ) {
				transable.add(i);
			} else if ( (i instanceof MissileWeapon && (!(i instanceof Dart) || i instanceof TippedDart) )
					|| (i instanceof Potion && !(i instanceof Elixir || i instanceof Brew || i instanceof AlchemicalCatalyst || i instanceof PotionOfStrength ))
					|| (i instanceof Scroll && ! (i instanceof ScrollOfUpgrade) )
					|| i instanceof Plant.Seed
					|| i instanceof Runestone ) {
				multitransable.add(i);
			}
		}



		//LUCKY <= lucky幸运 LUCKY <= lucky不幸
		if( volume <= cthulhustatue.charge ){

		switch ( NUMBER ){
			case 1: default://召唤怪物，如果特别好运，召唤金宝箱怪，特别霉运，召唤下一层的怪物
				switch ( luck ){
					case VERYLUCKY: default:
						int spawnCell = 1;
						if (ch != null){
							for (int n : PathFinder.NEIGHBOURS8) {
								int cell = n;
								if (Dungeon.level.passable[cell] && Actor.findChar( cell ) == null) {
									spawnCell = cell ;
								}
							}
						}

						Mimic mimic = Mimic.spawnAt(spawnCell, new ArrayList<Item>(), GoldenMimic.class);
						mimic.stopHiding();
						mimic.alignment = Char.Alignment.ENEMY;
						Item reward;
						do {
							reward = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
								Generator.Category.RING, Generator.Category.WAND));
						} while (reward.level() < 1);
						//play vfx/sfx manually as mimic isn't in the scene yet
						Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 0.85f);
						CellEmitter.get(mimic.pos).burst(Speck.factory(Speck.STAR), 10);
						mimic.items.clear();
						mimic.items.add(reward);
						GameScene.add(mimic);
						cthulhustatue.lucky -= 15;
						return true;

					case QUITELUCKY:
						for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
							if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
								Buff.affect( mob, Weakness.class,Weakness.DURATION  );
								Buff.affect( mob, Vulnerable.class,Vulnerable.DURATION  );
							}
						}
						break;
					case LUCKY:
						SummoningTrap st = new SummoningTrap();
						st.pos = ch.pos;
						st.activate();
					case VERYUNLUCKY:
						DistortionTrap dt = new DistortionTrap();
						dt.pos = ch.pos;
						dt.activate();
						break;
					case QUITEUNLUCKY:
						SummoningTrap st2 = new SummoningTrap();
						st2.pos = ch.pos;
						st2.activate();
						Buff.affect( hero, Weakness.class,Weakness.DURATION  );
						cthulhustatue.lucky += 5;
						break;
					case UNLUCKY:
						SummoningTrap st3 = new SummoningTrap();
						st3.pos = ch.pos;
						st3.activate();
						break;

				}
				cthulhustatue.charge -= 20;
				GLog.w(Messages.get(CthulhuGirlStatue.class, "summering"));
				break;
			case 2:
				HashSet<Class<? extends Potion>> potions = Potion.getUnknown();
				HashSet<Class<? extends Scroll>> scrolls = Scroll.getUnknown();
				HashSet<Class<? extends Ring>> rings = Ring.getUnknown();
				int total = potions.size() + scrolls.size() + rings.size();
				switch (luck){
					case VERYLUCKY:
					if( total != 0 ){//预知密卷方法
						int left = 3;
						float[] baseProbs = new float[]{3, 3, 3};
						float[] probs = baseProbs.clone();
						while (left > 0 && total > 0) {
							switch (Random.chances(probs)) {
								default:
									probs = baseProbs.clone();
									continue;
								case 0:
									if ( potions.isEmpty() ) {
										probs[0] = 0;
										continue;
									}
									probs[0]--;
									Potion p = Reflection.newInstance(Random.element(potions));
									p.identify();
									IDed.add(p);
									potions.remove(p.getClass());
									break;
								case 1:
									if (scrolls.isEmpty()) {
										probs[1] = 0;
										continue;
									}
								probs[1]--;
								Scroll s = Reflection.newInstance(Random.element(scrolls));
								s.identify();
								IDed.add(s);
								scrolls.remove(s.getClass());
								break;
							case 2:
								if (rings.isEmpty()) {
									probs[2] = 0;
									continue;
								}
								probs[2]--;
								Ring r = Reflection.newInstance(Random.element(rings));
								r.setKnown();
								IDed.add(r);
								rings.remove(r.getClass());
								break;
							}
							left --;
							total --;
						}
					}
					break;
					case LUCKY:
					if ( !identifyable.isEmpty() ) {
						Random.element( identifyable ).identify();
					}
					case QUITEUNLUCKY:
						int left = 2;
						if( LUCKY-lucky >= 25 ){ left = 3; }
						float[] baseProbs = new float[]{3, 3, 3};
						float[] probs = baseProbs.clone();
						while (left > 0 && total > 0) {
							switch (Random.chances(probs)) {
							default:
								probs = baseProbs.clone();
								continue;
							case 0:
								if (potions.isEmpty()) {
									probs[0] = 0;
									continue;
								}
								probs[0]--;
								Potion p = Reflection.newInstance(Random.element(potions));
								p.disidentify();
								IDed.add(p);
								potions.remove(p.getClass());
								break;
							case 1:
								if (scrolls.isEmpty()) {
									probs[1] = 0;
									continue;
								}
								probs[1]--;
								Scroll s = Reflection.newInstance(Random.element(scrolls));
								s.disidentify();
								IDed.add(s);
								scrolls.remove(s.getClass());
								break;
							}
							left --;
							total --;
						}
						break;
					case UNLUCKY:
					if ( !identifydisable.isEmpty() ) {
						Random.element( identifydisable ).disidentify();
					}
					break;
				}
				cthulhustatue.charge -= 20;
				GLog.w(Messages.get(CthulhuGirlStatue.class, "identify"));
				break;
			case 3:
				Item item1 = Random.element( transable );
				Item item2 = Random.element( multitransable );
/*if (item instanceof MagesStaff) {
								return ScrollOfTransmutation.changeStaff((MagesStaff) item);
							}else*/
				switch ( Random.Int(2) ){
					case 0: default:
						if( item1 != null ){
							ScrollOfTransmutation.changeItem( item1 );
						}	break;
					case 1:
						if( item2 != null ){
							ScrollOfTransmutation.changeItem( item2 );
						}	break;
				}
				cthulhustatue.charge -= 20;
				GLog.w(Messages.get(CthulhuGirlStatue.class, "trans"));
				break;
			case 4:
				switch (luck){
					case VERYLUCKY:
						if( stengthchanged >= 4 ){
							hero.HT += 5;
							GLog.w(Messages.get(CthulhuGirlStatue.class, "STRMAX"));
						}else{
							hero.STR += 1;
							stengthchanged += 1;
						}
						break;
					case QUITELUCKY:
						hero.HT += hero.lvl;
						break;
					case LUCKY: default:
						hero.lvl += 1;
						break;
					case UNLUCKY:
						hero.lvl -= 1;
						break;
					case QUITEUNLUCKY:
						hero.HT -= Dungeon.depth;
						break;
					case VERYUNLUCKY:
						if( stengthchanged <= 0 ){
							hero.HT -= 5;
							GLog.w(Messages.get(CthulhuGirlStatue.class, "STRMIN"));
						}else{
							hero.STR -= 1;
							stengthchanged -= 1;
						}
						break;
				}
				GLog.w(Messages.get(CthulhuGirlStatue.class, "attribute"));
				break;
			case 5:
				switch (luck){
					case VERYLUCKY:
						break;
					case QUITELUCKY:
						break;
					case LUCKY: default:
						break;
					case UNLUCKY:
						break;
					case QUITEUNLUCKY:
						break;
					case VERYUNLUCKY:
						for (int i = 0; i < (Dungeon.depth - 5)/5; i++){
							GuardianTrap.Guardian guardian = new GuardianTrap.Guardian();
							guardian.state = guardian.WANDERING;
							guardian.pos = Dungeon.level.randomRespawnCell( guardian );
							if (guardian.pos != -1) {
								GameScene.add(guardian);
								guardian.beckon(Dungeon.hero.pos);
							}
						}
						break;
				}
				break;

			case 6:

				Item i = new Item();
				switch (luck){
					case VERYLUCKY:

						break;
					case QUITELUCKY:
						i = Generator.randomUsingDefaults(Generator.Category.WEAPON);
						break;
					case LUCKY: default:

						break;
					case UNLUCKY:

						break;
					case QUITEUNLUCKY:

						break;
					case VERYUNLUCKY:

						break;
				}
				i.quantity(1).collect();
				break;
/*
case 7:
				switch (luck){
					case VERYLUCKY:
						break;
					case QUITELUCKY:
						break;
					case LUCKY: default:
						break;
					case UNLUCKY:
						break;
					case QUITEUNLUCKY:
						break;
					case VERYUNLUCKY:
						break;
				}
				break;


			if( lucky-LUCKY >= 25 ){
				} else if ( lucky-LUCKY >= 10 ) {
				} else if ( lucky >= LUCKY  ) {
				} else if ( LUCKY-lucky >= 25 ) {
				} else if ( LUCKY-lucky >= 10 ) {
				} else{
				}*/

			}
		}
		return true;
	}


	@Override
	public String info() {
		String info = desc();

		if (lucky <= 5){
			info += "\n\n" + Messages.get(this, "desc_veryunlucky");
		} else if (lucky <= 15){
			info += "\n\n" + Messages.get(this, "desc_unlucky");
		} else if (lucky >= 25){
			info += "\n\n" + Messages.get(this, "desc_verylucky");
		}else if (lucky >= 15){
			info += "\n\n" + Messages.get(this, "desc_lucky");
		}

		return info;
	}

	public void empty() {
		lucky = 0;
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
		return lucky >= MAX_LUCKY;
	}


	public void fill() {
		lucky = MAX_LUCKY;
		//charge = MAX_CHARGE;
		updateQuickslot();
	}

	@Override
	public String status() {
		return Messages.format( TXT_STATUS, lucky, MAX_LUCKY );
	}

}
