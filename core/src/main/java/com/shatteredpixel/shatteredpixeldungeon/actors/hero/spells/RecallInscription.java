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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.InventoryStone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

public class RecallInscription extends ClericSpell {

	public static RecallInscription INSTANCE = new RecallInscription();

	@Override
	public int icon() {
		return HeroIcon.RECALL_GLYPH;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", Dungeon.hero.pointsInTalent(Talent.RECALL_INSCRIPTION) == 2 ? 300 : 10) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public void onCast(HolyTome tome, Hero hero) {

		if (hero.buff(UsedItemTracker.class) == null){
			return;
		}

		Item item = Reflection.newInstance(hero.buff(UsedItemTracker.class).item);

		item.setCurrent(hero);

		hero.sprite.operate(hero.pos);
		Enchanting.show(hero, item);

		if (item instanceof Scroll){
			((Scroll) item).anonymize();
			((Scroll) item).doRead();
		} else if (item instanceof Runestone){
			((Runestone) item).anonymize();
			if (item instanceof InventoryStone){
				((InventoryStone) item).directActivate();
			} else {
				//we're already on the render thread, but we want to delay this
				//as things like time freeze cancel can stop stone throwing from working
				ShatteredPixelDungeon.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						item.doThrow(hero);
					}
				});
			}
		}

		onSpellCast(tome, hero);
		if (hero.buff(UsedItemTracker.class) != null){
			hero.buff(UsedItemTracker.class).detach();
		}

	}

	@Override
	public float chargeUse(Hero hero) {
		if (hero.buff(UsedItemTracker.class) != null){
			Class<? extends Item> item = hero.buff(UsedItemTracker.class).item;
			if (ExoticScroll.class.isAssignableFrom(item)){
				if (item == ScrollOfMetamorphosis.class || item == ScrollOfEnchantment.class){
					return 8;
				} else {
					return 4;
				}
			} else if (Scroll.class.isAssignableFrom(item)){
				if (item == ScrollOfTransmutation.class){
					return 6;
				} else {
					return 3;
				}
			} else if (Runestone.class.isAssignableFrom(item)){
				if (item == StoneOfAugmentation.class || item == StoneOfEnchantment.class){
					return 4;
				} else {
					return 2;
				}
			}
		}
		return 0;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero)
				&& hero.hasTalent(Talent.RECALL_INSCRIPTION)
				&& hero.buff(UsedItemTracker.class) != null;
	}

	public static class UsedItemTracker extends FlavourBuff {

		{
			type = buffType.POSITIVE;
		}

		public Class<?extends Item> item;

		@Override
		public int icon() {
			return BuffIndicator.GLYPH_RECALL;
		}

		@Override
		public float iconFadePercent() {
			float duration = Dungeon.hero.pointsInTalent(Talent.RECALL_INSCRIPTION) == 2 ? 300 : 10;
			return Math.max(0, (duration - visualcooldown()) / duration);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", Messages.titleCase(Reflection.newInstance(item).name()), dispTurns());
		}

		private static String ITEM = "item";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(ITEM, item);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			item = bundle.getClass(ITEM);
		}
	}

}
