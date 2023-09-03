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

package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChooseAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

abstract public class ClassArmor extends Armor {

	private static final String AC_ABILITY = "ABILITY";
	private static final String AC_TRANSFER = "TRANSFER";
	
	{
		levelKnown = true;
		cursedKnown = true;
		defaultAction = AC_ABILITY;

		bones = false;
	}

	private Charger charger;
	public float charge = 0;
	
	public ClassArmor() {
		super( 5 );
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		charger = new Charger();
		charger.attachTo(ch);
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			if (charger != null){
				charger.detach();
				charger = null;
			}
			return true;

		} else {
			return false;

		}
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		return user.armorAbility.targetedPos(user, dst);
	}

	public static ClassArmor upgrade (Hero owner, Armor armor ) {
		
		ClassArmor classArmor = null;
		
		switch (owner.heroClass) {
			case WARRIOR:
				classArmor = new WarriorArmor();
				break;
			case ROGUE:
				classArmor = new RogueArmor();
				break;
			case MAGE:
				classArmor = new MageArmor();
				break;
			case HUNTRESS:
				classArmor = new HuntressArmor();
				break;
			case DUELIST:
				classArmor = new DuelistArmor();
				break;
		}
		
		classArmor.level(armor.trueLevel());
		classArmor.tier = armor.tier;
		classArmor.augment = armor.augment;
		classArmor.inscribe(armor.glyph);
		if (armor.seal != null) {
			classArmor.seal = armor.seal;
		}
		classArmor.cursed = armor.cursed;
		classArmor.curseInfusionBonus = armor.curseInfusionBonus;
		classArmor.masteryPotionBonus = armor.masteryPotionBonus;
		classArmor.identify();

		classArmor.charge = 50;
		
		return classArmor;
	}

	private static final String ARMOR_TIER	= "armortier";
	private static final String CHARGE	    = "charge";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARMOR_TIER, tier );
		bundle.put( CHARGE, charge );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		tier = bundle.getInt( ARMOR_TIER );
		charge = bundle.getFloat(CHARGE);
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero )) {
			actions.add( AC_ABILITY );
		}
		actions.add( AC_TRANSFER );
		return actions;
	}

	@Override
	public String actionName(String action, Hero hero) {
		if (hero.armorAbility != null && action.equals(AC_ABILITY)){
			return Messages.upperCase(hero.armorAbility.name());
		} else {
			return super.actionName(action, hero);
		}
	}

	@Override
	public String status() {
		return Messages.format( "%.0f%%", Math.floor(charge) );
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_ABILITY)){

			if (hero.armorAbility == null){
				GameScene.show(new WndChooseAbility(null, this, hero));
			} else if (!isEquipped( hero )) {
				usesTargeting = false;
				GLog.w( Messages.get(this, "not_equipped") );
			} else if (charge < hero.armorAbility.chargeUse(hero)) {
				usesTargeting = false;
				GLog.w( Messages.get(this, "low_charge") );
			} else  {
				usesTargeting = hero.armorAbility.useTargeting();
				hero.armorAbility.use(this, hero);
			}
			
		} else if (action.equals(AC_TRANSFER)){

			GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.CROWN),
					Messages.get(ClassArmor.class, "transfer_title"),
					Messages.get(ClassArmor.class, "transfer_desc"),
					Messages.get(ClassArmor.class, "transfer_prompt"),
					Messages.get(ClassArmor.class, "transfer_cancel")){
				@Override
				protected void onSelect(int index) {
					if (index == 0){
						GameScene.selectItem(new WndBag.ItemSelector() {
							@Override
							public String textPrompt() {
								return Messages.get(ClassArmor.class, "transfer_prompt");
							}

							@Override
							public boolean itemSelectable(Item item) {
								return item instanceof Armor;
							}

							@Override
							public void onSelect(Item item) {
								if (item == null || item == ClassArmor.this) return;

								Armor armor = (Armor)item;
								armor.detach(hero.belongings.backpack);
								if (hero.belongings.armor == armor){
									hero.belongings.armor = null;
								}
								level(armor.trueLevel());
								tier = armor.tier;
								augment = armor.augment;
								cursed = armor.cursed;
								curseInfusionBonus = armor.curseInfusionBonus;
								masteryPotionBonus = armor.masteryPotionBonus;
								if (armor.checkSeal() != null) {
									inscribe(armor.glyph);
									seal = armor.checkSeal();
									if (seal.level() > 0) {
										int newLevel = trueLevel() + 1;
										level(newLevel);
										Badges.validateItemLevelAquired(ClassArmor.this);
									}
								} else if (checkSeal() != null){
									//automates the process of detaching the glyph manually
									// and re-affixing it to the new armor
									if (seal.level() > 0 && trueLevel() <= armor.trueLevel()){
										int newLevel = trueLevel() + 1;
										level(newLevel);
										Badges.validateItemLevelAquired(ClassArmor.this);
									}

									//if both source and destination armor have glyphs
									// we assume the player wants the glyph on the destination armor
									// they can always manually detach first if they don't.
									// otherwise we automate glyph transfer just like upgrades
									if (armor.glyph == null && seal.canTransferGlyph()){
										//do nothing, keep our glyph
									} else {
										inscribe(armor.glyph);
										seal.setGlyph(null);
									}
								} else {
									inscribe(armor.glyph);
								}

								identify();

								GLog.p( Messages.get(ClassArmor.class, "transfer_complete") );
								hero.sprite.operate(hero.pos);
								hero.sprite.emitter().burst( Speck.factory( Speck.CROWN), 12 );
								Sample.INSTANCE.play( Assets.Sounds.EVOKE );
								hero.spend(Actor.TICK);
								hero.busy();

							}
						});
					}
				}
			});

		}
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if (Dungeon.hero.belongings.contains(this)) {
			ArmorAbility ability = Dungeon.hero.armorAbility;
			if (ability != null) {
				desc += "\n\n" + ability.shortDesc();
				float chargeUse = ability.chargeUse(Dungeon.hero);
				desc += " " + Messages.get(this, "charge_use", Messages.decimalFormat("#.##", chargeUse));
			} else {
				desc += "\n\n" + "_" + Messages.get(this, "no_ability") + "_";
			}
		}

		return desc;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 0;
	}

	public class Charger extends Buff {

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				//if we're loading in and the hero has partially spent a turn, delay for 1 turn
				if (target instanceof Hero && Dungeon.hero == null && cooldown() == 0 && target.cooldown() > 0) {
					spend(TICK);
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean act() {
			if (Regeneration.regenOn()) {
				float chargeGain = 100 / 500f; //500 turns to full charge
				chargeGain *= RingOfEnergy.armorChargeMultiplier(target);
				charge += chargeGain;
				updateQuickslot();
				if (charge > 100) {
					charge = 100;
				}
			}
			spend(TICK);
			return true;
		}
	}
}
