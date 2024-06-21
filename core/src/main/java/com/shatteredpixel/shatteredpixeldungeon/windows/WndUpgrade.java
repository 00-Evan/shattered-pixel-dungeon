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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.InventoryScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatshield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Reflection;

public class WndUpgrade extends Window {

	private static final int WIDTH = 120;

	private static final float COL_1 = WIDTH/4f;
	private static final float COL_2 = 5*WIDTH/8f;
	private static final float COL_3 = 7*WIDTH/8f;

	protected static final int GAP	= 2;

	private boolean force;

	private RedButton btnUpgrade;
	private RedButton btnCancel;

	public WndUpgrade( Item upgrader, Item toUpgrade, boolean force){

		this.force = force;

		IconTitle title = new IconTitle( new ItemSprite(upgrader), Messages.get(this, "title") );

		title.setRect(0, 0, WIDTH, 0);
		add(title);

		RenderedTextBlock message = PixelScene.renderTextBlock( 6 );
		message.text( Messages.get(this, "desc"), WIDTH);
		message.setPos(0, title.bottom()+GAP);
		add(message);

		// *** Computing current and next level to display ***

		int levelFrom = toUpgrade.isIdentified() ? toUpgrade.level() : 0;
		int levelTo = levelFrom + 1;

		if (toUpgrade instanceof Wand && ((Wand) toUpgrade).resinBonus > 0){
			levelTo--;
		}

		boolean curseInfused = (toUpgrade instanceof Weapon && ((Weapon) toUpgrade).curseInfusionBonus)
				|| (toUpgrade instanceof Armor && ((Armor) toUpgrade).curseInfusionBonus)
				|| (toUpgrade instanceof Wand && ((Wand) toUpgrade).curseInfusionBonus);

		if (curseInfused){
			if (toUpgrade.trueLevel()/6 < (toUpgrade.trueLevel()+1)/6){
				//new level bracket for curse infusion bonus
				levelTo++;
			}
		}

		// *** Sprites, showing item at current level and with +1 ***

		ItemSprite i1 = new ItemSprite();
		add(i1);
		i1.view(toUpgrade);
		i1.x = COL_2 - i1.width()/2f;
		i1.y = message.bottom() + GAP + (16-i1.height())/2f;
		PixelScene.align(i1);
		add(i1);

		ItemSprite i2 = new ItemSprite();
		add(i2);
		i2.view(toUpgrade);
		i2.x = COL_3 - i2.width()/2f;
		i2.y = i1.y;
		PixelScene.align(i2);
		add(i2);

		BitmapText t1 = new BitmapText(PixelScene.pixelFont);
		BitmapText t2 = new BitmapText(PixelScene.pixelFont);
		if (toUpgrade.isIdentified()){
			if (levelFrom > 0){
				t1.text("+" + levelFrom);
			} else {
				t1.text("");
			}
			t1.hardlight(ItemSlot.UPGRADED);
			t2.text("+" + levelTo);
			t2.hardlight(ItemSlot.UPGRADED);

			if (curseInfused){
				t1.hardlight(ItemSlot.CURSE_INFUSED);
				t2.hardlight(ItemSlot.CURSE_INFUSED);
			}

		} else {
			t1.text("?");
			t1.hardlight(0.6f, 0.3f, 0.6f);
			t2.text("+1?");
			t2.hardlight(0.6f, 0.3f, 0.6f);
		}
		t1.measure();
		t1.x = COL_2 + 8 - t1.width();
		t1.y = message.bottom() + GAP + 16 - t1.baseLine();
		add(t1);

		t2.measure();
		t2.x = COL_3 + 8 - t2.width();
		t2.y = message.bottom() + GAP + 16 - t2.baseLine();
		add(t2);

		float bottom = i1.y + 16 + GAP;

		// *** Various lines for stats, highlighting differences between current level and +1 ***

		//physical damage
		if (toUpgrade instanceof Weapon){
			bottom = fillFields(Messages.get(this, "damage"),
					((Weapon) toUpgrade).min(levelFrom) + "-" + ((Weapon) toUpgrade).max(levelFrom),
					((Weapon) toUpgrade).min(levelTo) + "-" + ((Weapon) toUpgrade).max(levelTo),
					bottom);
		}

		//blocking (armor and shields)
		if (toUpgrade instanceof Armor){
			bottom = fillFields(Messages.get(this, "blocking"),
					((Armor) toUpgrade).DRMin(levelFrom) + "-" + ((Armor) toUpgrade).DRMax(levelFrom),
					((Armor) toUpgrade).DRMin(levelTo) + "-" + ((Armor) toUpgrade).DRMax(levelTo),
					bottom);
		} else if (toUpgrade instanceof RoundShield){
			bottom = fillFields(Messages.get(this, "blocking"),
					0 + "-" + ((RoundShield) toUpgrade).DRMax(levelFrom),
					0 + "-" + ((RoundShield) toUpgrade).DRMax(levelTo),
					bottom);
		} else if (toUpgrade instanceof Greatshield){
			bottom = fillFields(Messages.get(this, "blocking"),
					0 + "-" + ((Greatshield) toUpgrade).DRMax(levelFrom),
					0 + "-" + ((Greatshield) toUpgrade).DRMax(levelTo),
					bottom);
		}

		//weight (i.e. strength requirement)
		if (toUpgrade instanceof Weapon){
			bottom = fillFields(Messages.get(this, "weight"),
					Integer.toString((((Weapon) toUpgrade).STRReq(levelFrom))),
					Integer.toString((((Weapon) toUpgrade).STRReq(levelTo))),
					bottom);
		} else if (toUpgrade instanceof Armor) {
			bottom = fillFields(Messages.get(this, "weight"),
					Integer.toString((((Armor) toUpgrade).STRReq(levelFrom))),
					Integer.toString((((Armor) toUpgrade).STRReq(levelTo))),
					bottom);
		}

		//TODO Duelist weapon abilities

		//durability
		if (toUpgrade instanceof MissileWeapon){
			//missile weapons are always IDed currently, so we always use true level
			int uses1 = (int)Math.ceil(100f/((MissileWeapon) toUpgrade).durabilityPerUse());
			int uses2 = (int)Math.ceil(300f/((MissileWeapon) toUpgrade).durabilityPerUse());
			bottom = fillFields(Messages.get(this, "durability"),
					uses1 >= 100 ? "∞" : Integer.toString(uses1),
					uses2 >= 100 ? "∞" : Integer.toString(uses2),
					bottom);
		}

		//we use a separate reference for wand properties so that mage's staff can include them
		Item wand = toUpgrade;
		if (toUpgrade instanceof MagesStaff && ((MagesStaff) toUpgrade).wandClass() != null){
			wand = Reflection.newInstance(((MagesStaff) toUpgrade).wandClass());
		}

		//Various wand stats (varies by wand)
		if (wand instanceof Wand){
			if (((Wand) wand).upgradeStat1(levelFrom) != null){
				bottom = fillFields(Messages.get(wand, "upgrade_stat_name_1"),
						((Wand) wand).upgradeStat1(levelFrom),
						((Wand) wand).upgradeStat1(levelTo),
						bottom);
			}
			if (((Wand) wand).upgradeStat2(levelFrom) != null){
				bottom = fillFields(Messages.get(wand, "upgrade_stat_name_2"),
						((Wand) wand).upgradeStat2(levelFrom),
						((Wand) wand).upgradeStat2(levelTo),
						bottom);
			}
			if (((Wand) wand).upgradeStat3(levelFrom) != null){
				bottom = fillFields(Messages.get(wand, "upgrade_stat_name_3"),
						((Wand) wand).upgradeStat3(levelFrom),
						((Wand) wand).upgradeStat3(levelTo),
						bottom);
			}
		}

		//max charges
		if (wand instanceof Wand){
			int chargeboost = levelFrom + (toUpgrade instanceof MagesStaff ? 1 : 0);
			bottom = fillFields(Messages.get(this, "charges"),
					Integer.toString(Math.min(10, ((Wand) wand).initialCharges() + chargeboost)),
					Integer.toString(Math.min(10, ((Wand) wand).initialCharges() + chargeboost + 1)),
					bottom);
		}

		//TODO Various ring stats (varies by ring)
		if (toUpgrade instanceof Ring){

		}

		//visual separators for each column
		ColorBlock sep = new ColorBlock(1, 1, 0xFF222222);
		sep.size(1, bottom - message.bottom());
		sep.x = WIDTH/2f;
		sep.y = message.bottom() + GAP;
		add(sep);

		sep = new ColorBlock(1, 1, 0xFF222222);
		sep.size(1, bottom - message.bottom());
		sep.x = 3*WIDTH/4f;
		sep.y = message.bottom() + GAP;
		add(sep);

		// *** Various extra info texts that can appear underneath stats ***

		//warning relating to identification
		if (!toUpgrade.isIdentified()){
			bottom = addMessage(Messages.get(this, "unided"), CharSprite.WARNING, bottom);
		}

		// various messages relating to enchantments and curses
		if (!(upgrader instanceof MagicalInfusion)) {

			if ((toUpgrade instanceof Weapon && ((Weapon) toUpgrade).hasGoodEnchant())
					|| (toUpgrade instanceof Armor && ((Armor) toUpgrade).hasGoodGlyph())) {
				int lossChance;
				if ((toUpgrade instanceof Weapon && ((Weapon) toUpgrade).enchantHardened)
						|| (toUpgrade instanceof Armor && ((Armor) toUpgrade).glyphHardened)) {
					lossChance = Math.min(100, 10 * (int) Math.pow(2, levelFrom - 6));
				} else {
					lossChance = Math.min(100, 10 * (int) Math.pow(2, levelFrom - 4));
				}

				if (lossChance >= 10) {
					String warn;
					if (toUpgrade instanceof Weapon) {
						if (((Weapon) toUpgrade).enchantHardened) {
							warn = Messages.get(this, "harden", lossChance);
						} else {
							warn = Messages.get(this, "enchant", lossChance);
						}
					} else {
						if (((Armor) toUpgrade).glyphHardened) {
							warn = Messages.get(this, "harden", lossChance);
						} else {
							warn = Messages.get(this, "glyph", lossChance);
						}
					}
					bottom = addMessage(warn, CharSprite.WARNING, bottom);
				}
			}

			if ((toUpgrade.cursed
					|| (toUpgrade instanceof Weapon && ((Weapon) toUpgrade).hasCurseEnchant())
					|| (toUpgrade instanceof Armor && ((Armor) toUpgrade).hasCurseGlyph()))
					&& toUpgrade.cursedKnown) {

				if (toUpgrade.cursed && (toUpgrade instanceof Weapon && ((Weapon) toUpgrade).hasCurseEnchant())
						|| (toUpgrade instanceof Armor && ((Armor) toUpgrade).hasCurseGlyph())){
					bottom = addMessage(Messages.get(this, "cursed_weaken"), CharSprite.POSITIVE, bottom);
				} else {
					bottom = addMessage(Messages.get(this, "cursed"), CharSprite.POSITIVE, bottom);
				}

				if (curseInfused) {
					bottom = addMessage(Messages.get(this, "curse_infusion"), CharSprite.WARNING, bottom);
				}
			}
		}

		//warning relating to arcane resin
		if (toUpgrade instanceof Wand && ((Wand) toUpgrade).resinBonus > 0){
			bottom = addMessage(Messages.get(this, "resin"), CharSprite.WARNING, bottom);
		}

		// *** Buttons for confirming/cancelling ***

		btnUpgrade = new RedButton(Messages.get(this, "upgrade")){
			@Override
			protected void onClick() {
				super.onClick();

				ScrollOfUpgrade.upgrade(Dungeon.hero);

				Item upgraded = toUpgrade;
				if (upgrader instanceof ScrollOfUpgrade){
					((ScrollOfUpgrade) upgrader).readAnimation();
					upgraded = ((ScrollOfUpgrade) upgrader).upgradeItem(toUpgrade);
					Sample.INSTANCE.play( Assets.Sounds.READ );
				} else if (upgrader instanceof MagicalInfusion){
					((MagicalInfusion) upgrader).useAnimation();
					upgraded = ((MagicalInfusion) upgrader).upgradeItem(toUpgrade);
				}

				Item moreUpgradeItem = Dungeon.hero.belongings.getItem(upgrader.getClass());

				hide();

				if (moreUpgradeItem != null && toUpgrade.isIdentified()){
					moreUpgradeItem = moreUpgradeItem.detach(Dungeon.hero.belongings.backpack);
					GameScene.show(new WndUpgrade(moreUpgradeItem, upgraded, false));
				}
			}
		};
		btnUpgrade.setRect(0, bottom+2*GAP, WIDTH/2f, 16);
		add(btnUpgrade);

		btnCancel = new RedButton(Messages.get(this, "cancel")){
			@Override
			protected void onClick() {
				super.onClick();
				if (!force) {
					upgrader.collect();
					hide();
				} else {
					GameScene.show( new WndOptions(new ItemSprite(upgrader),
							Messages.titleCase(upgrader.name()),
							Messages.get(InventoryScroll.class, "warning"),
							Messages.get(InventoryScroll.class, "yes"),
							Messages.get(InventoryScroll.class, "no") ) {
						@Override
						protected void onSelect( int index ) {
							if (index == 0){
								WndUpgrade.this.hide();
							}
						}
						public void onBackPressed() {}
					} );
				}
			}

		};
		btnCancel.setRect(btnUpgrade.right()+1, bottom+2*GAP, WIDTH/2f, 16);
		add(btnCancel);

		btnUpgrade.enable(Dungeon.hero.ready);

		bottom = (int)btnCancel.bottom();

		resize(WIDTH, (int)bottom);

	}

	@Override
	public synchronized void update() {
		super.update();
		if (!btnUpgrade.active && Dungeon.hero.ready){
			btnUpgrade.enable(true);
		}
	}

	@Override
	public void onBackPressed() {
		//don't let this window be closed if
		//TODO currently never closes, as the Sou/magic infusion is consumed as it is shown
		//we might want to only have it be pre-consumed when force = true
		//as atm quitting the game with the window open will eat the scroll/infuse when force=false
	}

	private float fillFields(String title, String msg1, String msg2, float bottom){

		//the ~ symbol is more commonly used in Chinese
		if (Messages.lang() == Languages.CHINESE){
			msg1 = msg1.replace('-', '~');
			msg2 = msg2.replace('-', '~');
		}

		RenderedTextBlock ttl = PixelScene.renderTextBlock(6);
		ttl.align(RenderedTextBlock.CENTER_ALIGN);
		ttl.text(title, WIDTH/2);
		ttl.setPos(COL_1 - ttl.width() / 2f, bottom + GAP);
		PixelScene.align(ttl);
		add(ttl);

		RenderedTextBlock m1 = PixelScene.renderTextBlock(msg1, 6);
		m1.setPos(COL_2 - m1.width() / 2f, ttl.top());
		PixelScene.align(m1);
		add(m1);

		RenderedTextBlock m2 = PixelScene.renderTextBlock(msg2, 6);
		m2.setPos(COL_3 - m2.width() / 2f, ttl.top());
		PixelScene.align(m2);
		add(m2);

		return ttl.bottom() + GAP;

	}

	private float addMessage(String text, int color, float bottom){
		RenderedTextBlock message = PixelScene.renderTextBlock(6);
		message.text(text, WIDTH);
		message.setPos(0, bottom + GAP);
		message.hardlight(color);
		add(message);

		return message.bottom();
	}

}
