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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.InventoryScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.audio.Sample;

public class ScrollOfEnchantment extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_ENCHANT;

		unique = true;
	}

	protected static boolean identifiedByUse = false;
	
	@Override
	public void doRead() {
		if (!isKnown()) {
			identify();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}
		GameScene.selectItem( itemSelector );
	}

	public static boolean enchantable( Item item ){
		return (item instanceof MeleeWeapon || item instanceof SpiritBow || item instanceof Armor);
	}

	private void confirmCancelation() {
		GameScene.show( new WndOptions(new ItemSprite(this),
				Messages.titleCase(name()),
				Messages.get(InventoryScroll.class, "warning"),
				Messages.get(InventoryScroll.class, "yes"),
				Messages.get(InventoryScroll.class, "no") ) {
			@Override
			protected void onSelect( int index ) {
				switch (index) {
					case 0:
						curUser.spendAndNext( TIME_TO_READ );
						identifiedByUse = false;
						break;
					case 1:
						GameScene.selectItem(itemSelector);
						break;
				}
			}
			public void onBackPressed() {}
		} );
	}
	
	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(ScrollOfEnchantment.class, "inv_title");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return enchantable(item);
		}

		@Override
		public void onSelect(final Item item) {
			
			if (item instanceof Weapon){
				
				final Weapon.Enchantment enchants[] = new Weapon.Enchantment[3];
				
				Class<? extends Weapon.Enchantment> existing = ((Weapon) item).enchantment != null ? ((Weapon) item).enchantment.getClass() : null;
				enchants[0] = Weapon.Enchantment.randomCommon( existing );
				enchants[1] = Weapon.Enchantment.randomUncommon( existing );
				enchants[2] = Weapon.Enchantment.random( existing, enchants[0].getClass(), enchants[1].getClass());

				GameScene.show(new WndEnchantSelect((Weapon) item, enchants[0], enchants[1], enchants[2]));
			
			} else if (item instanceof Armor) {
				
				final Armor.Glyph glyphs[] = new Armor.Glyph[3];
				
				Class<? extends Armor.Glyph> existing = ((Armor) item).glyph != null ? ((Armor) item).glyph.getClass() : null;
				glyphs[0] = Armor.Glyph.randomCommon( existing );
				glyphs[1] = Armor.Glyph.randomUncommon( existing );
				glyphs[2] = Armor.Glyph.random( existing, glyphs[0].getClass(), glyphs[1].getClass());
				
				GameScene.show(new WndGlyphSelect((Armor) item, glyphs[0], glyphs[1], glyphs[2]));
			} else {
				if (!identifiedByUse){
					curItem.collect();
				} else {
					((ScrollOfEnchantment)curItem).confirmCancelation();
				}
			}
		}
	};

	public static class WndEnchantSelect extends WndOptions {

		private static Weapon wep;
		private static Weapon.Enchantment[] enchantments;

		//used in PixelScene.restoreWindows
		public WndEnchantSelect(){
			this(wep, enchantments[0], enchantments[1], enchantments[2]);
		}

		public WndEnchantSelect(Weapon wep, Weapon.Enchantment ench1,
		                           Weapon.Enchantment ench2, Weapon.Enchantment ench3){
			super(new ItemSprite(new ScrollOfEnchantment()),
					Messages.titleCase(new ScrollOfEnchantment().name()),
					Messages.get(ScrollOfEnchantment.class, "weapon") +
							"\n\n" +
							Messages.get(ScrollOfEnchantment.class, "cancel_warn"),
					ench1.name(),
					ench2.name(),
					ench3.name(),
					Messages.get(ScrollOfEnchantment.class, "cancel"));
			this.wep = wep;
			enchantments = new Weapon.Enchantment[3];
			enchantments[0] = ench1;
			enchantments[1] = ench2;
			enchantments[2] = ench3;
		}

		@Override
		protected void onSelect(int index) {
			if (index < 3) {
				wep.enchant(enchantments[index]);
				GLog.p(Messages.get(StoneOfEnchantment.class, "weapon"));
				((ScrollOfEnchantment)curItem).readAnimation();

				Sample.INSTANCE.play( Assets.Sounds.READ );
				Enchanting.show(curUser, wep);
				Talent.onUpgradeScrollUsed( Dungeon.hero );
			}

			wep = null;
			enchantments = null;
		}

		@Override
		protected boolean hasInfo(int index) {
			return index < 3;
		}

		@Override
		protected void onInfo( int index ) {
			GameScene.show(new WndTitledMessage(
					Icons.get(Icons.INFO),
					Messages.titleCase(enchantments[index].name()),
					enchantments[index].desc()));
		}

		@Override
		public void onBackPressed() {
			//do nothing, reader has to cancel
		}

	}

	public static class WndGlyphSelect extends WndOptions {

		private static Armor arm;
		private static Armor.Glyph[] glyphs;

		//used in PixelScene.restoreWindows
		public WndGlyphSelect(){
			this(arm, glyphs[0], glyphs[1], glyphs[2]);
		}

		public WndGlyphSelect(Armor arm, Armor.Glyph glyph1,
		                      Armor.Glyph glyph2, Armor.Glyph glyph3){
			super(new ItemSprite(new ScrollOfEnchantment()),
					Messages.titleCase(new ScrollOfEnchantment().name()),
					Messages.get(ScrollOfEnchantment.class, "armor") +
							"\n\n" +
							Messages.get(ScrollOfEnchantment.class, "cancel_warn"),
					glyph1.name(),
					glyph2.name(),
					glyph3.name(),
					Messages.get(ScrollOfEnchantment.class, "cancel"));
			this.arm = arm;
			glyphs = new Armor.Glyph[3];
			glyphs[0] = glyph1;
			glyphs[1] = glyph2;
			glyphs[2] = glyph3;
		}

		@Override
		protected void onSelect(int index) {
			if (index < 3) {
				arm.inscribe(glyphs[index]);
				GLog.p(Messages.get(StoneOfEnchantment.class, "armor"));
				((ScrollOfEnchantment)curItem).readAnimation();

				Sample.INSTANCE.play( Assets.Sounds.READ );
				Enchanting.show(curUser, arm);
				Talent.onUpgradeScrollUsed( Dungeon.hero );
			}

			arm = null;
			glyphs = null;
		}

		@Override
		protected boolean hasInfo(int index) {
			return index < 3;
		}

		@Override
		protected void onInfo( int index ) {
			GameScene.show(new WndTitledMessage(
					Icons.get(Icons.INFO),
					Messages.titleCase(glyphs[index].name()),
					glyphs[index].desc()));
		}

		@Override
		public void onBackPressed() {
			//do nothing, reader has to cancel
		}

	}
}
