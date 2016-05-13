/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class HornOfPlenty extends Artifact {


	{
		image = ItemSpriteSheet.ARTIFACT_HORN1;

		levelCap = 30;

		charge = 0;
		partialCharge = 0;
		chargeCap = 10;

		defaultAction = AC_EAT;
	}

	private static final float TIME_TO_EAT	= 3f;

	public static final String AC_EAT = "EAT";
	public static final String AC_STORE = "STORE";

	protected WndBag.Mode mode = WndBag.Mode.FOOD;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge > 0)
			actions.add(AC_EAT);
		if (isEquipped( hero ) && level() < 30 && !cursed)
			actions.add(AC_STORE);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)){

			if (!isEquipped(hero)) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (charge == 0)  GLog.i( Messages.get(this, "no_food") );
			else {
				hero.buff(Hunger.class).satisfy((Hunger.STARVING/10) * charge);

				//if you get at least 100 food energy from the horn
				if (charge >= 3) {
					switch (hero.heroClass) {
						case WARRIOR:
							if (hero.HP < hero.HT) {
								hero.HP = Math.min(hero.HP + 5, hero.HT);
								hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
							}
							break;
						case MAGE:
							//1 charge
							Buff.affect( hero, Recharging.class, 4f );
							ScrollOfRecharging.charge(hero);
							break;
						case ROGUE:
						case HUNTRESS:
							break;
					}

					Statistics.foodEaten++;
				}
				charge = 0;

				hero.sprite.operate(hero.pos);
				hero.busy();
				SpellSprite.show(hero, SpellSprite.FOOD);
				Sample.INSTANCE.play(Assets.SND_EAT);
				GLog.i( Messages.get(this, "eat") );

				hero.spend(TIME_TO_EAT);

				Badges.validateFoodEaten();

				image = ItemSpriteSheet.ARTIFACT_HORN1;

				updateQuickslot();
			}

		} else if (action.equals(AC_STORE)){

			GameScene.selectItem(itemSelector, mode, Messages.get(this, "prompt"));

		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new hornRecharge();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if ( isEquipped( Dungeon.hero ) ){
			if (!cursed) {
				if (level() < levelCap)
					desc += "\n\n" +Messages.get(this, "desc_hint");
			} else {
				desc += "\n\n" +Messages.get(this, "desc_cursed");
			}
		}

		return desc;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (charge == chargeCap)image = ItemSpriteSheet.ARTIFACT_HORN4;
		else if (charge >= 7)   image = ItemSpriteSheet.ARTIFACT_HORN3;
		else if (charge >= 3)   image = ItemSpriteSheet.ARTIFACT_HORN2;
	}

	public class hornRecharge extends ArtifactBuff{

		@Override
		public boolean act() {
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {

				//generates 0.25 food value every round, +0.015 value per level
				//to a max of 0.70 food value per round (0.25+0.5, at level 30)
				partialCharge += 0.25f + (0.015f*level());

				//charge is in increments of 36 food value.
				if (partialCharge >= 36) {
					charge++;
					partialCharge -= 36;

					if (charge == chargeCap)image = ItemSpriteSheet.ARTIFACT_HORN4;
					else if (charge >= 7)   image = ItemSpriteSheet.ARTIFACT_HORN3;
					else if (charge >= 3)   image = ItemSpriteSheet.ARTIFACT_HORN2;
					else                    image = ItemSpriteSheet.ARTIFACT_HORN1;

					if (charge == chargeCap){
						GLog.p( Messages.get(HornOfPlenty.class, "full") );
						partialCharge = 0;
					}

					updateQuickslot();
				}
			} else
				partialCharge = 0;

			spend( TICK );

			return true;
		}

	}

	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof Food) {
				if (item instanceof Blandfruit && ((Blandfruit) item).potionAttrib == null){
					GLog.w( Messages.get(HornOfPlenty.class, "reject") );
				} else {
					Hero hero = Dungeon.hero;
					hero.sprite.operate( hero.pos );
					hero.busy();
					hero.spend( TIME_TO_EAT );

					curItem.upgrade(((Food)item).hornValue);
					if (curItem.level() >= 30){
						curItem.level(30);
						GLog.p( Messages.get(HornOfPlenty.class, "maxlevel") );
					} else
						GLog.p( Messages.get(HornOfPlenty.class, "levelup") );
					item.detach(hero.belongings.backpack);
				}

			}
		}
	};
}
