/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class SummonElemental extends Spell {

	public static final String AC_IMBUE = "IMBUE";

	{
		image = ItemSpriteSheet.SUMMON_ELE;
	}

	private Class<? extends Elemental> summonClass = Elemental.AllyNewBornElemental.class;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_IMBUE);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_IMBUE)){
			GameScene.selectItem(selector);
		}
	}

	@Override
	protected void onCast(Hero hero) {

		ArrayList<Integer> spawnPoints = new ArrayList<>();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = hero.pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
				spawnPoints.add( p );
			}
		}

		if (!spawnPoints.isEmpty()){

			for (Char ch : Actor.chars()){
				if (ch instanceof Elemental && ch.buff(InvisAlly.class) != null){
					ScrollOfTeleportation.appear( ch, Random.element(spawnPoints) );
					((Elemental) ch).state = ((Elemental) ch).HUNTING;
					curUser.spendAndNext(Actor.TICK);
					return;
				}
			}

			Elemental elemental = Reflection.newInstance(summonClass);
			GameScene.add( elemental );
			Buff.affect(elemental, InvisAlly.class);
			elemental.setSummonedALly();
			elemental.HP = elemental.HT;
			ScrollOfTeleportation.appear( elemental, Random.element(spawnPoints) );
			curUser.spendAndNext(Actor.TICK);

			summonClass = Elemental.AllyNewBornElemental.class;

			detach(Dungeon.hero.belongings.backpack);

		} else {
			GLog.w(Messages.get(SpiritHawk.class, "no_space"));
		}

	}

	@Override
	public ItemSprite.Glowing glowing() {
		if (summonClass == Elemental.FireElemental.class)   return new ItemSprite.Glowing(0xFFBB33);
		if (summonClass == Elemental.FrostElemental.class)  return new ItemSprite.Glowing(0x8EE3FF);
		if (summonClass == Elemental.ShockElemental.class)  return new ItemSprite.Glowing(0xFFFF85);
		if (summonClass == Elemental.ChaosElemental.class)  return new ItemSprite.Glowing(0xE3E3E3, 0.5f);
		return super.glowing();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		desc += "\n\n";

		if (summonClass == Elemental.AllyNewBornElemental.class)    desc += Messages.get(this, "desc_newborn");
		if (summonClass == Elemental.FireElemental.class)           desc += Messages.get(this, "desc_fire");
		if (summonClass == Elemental.FrostElemental.class)          desc += Messages.get(this, "desc_frost");
		if (summonClass == Elemental.ShockElemental.class)          desc += Messages.get(this, "desc_shock");
		if (summonClass == Elemental.ChaosElemental.class)          desc += Messages.get(this, "desc_chaos");

		return desc;
	}

	private static final String SUMMON_CLASS = "summon_class";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SUMMON_CLASS, summonClass);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(SUMMON_CLASS)) summonClass = bundle.getClass(SUMMON_CLASS);
	}

	public WndBag.ItemSelector selector = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(SummonElemental.class, "imbue_prompt");
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item.isIdentified() && (item instanceof PotionOfLiquidFlame
					|| item instanceof PotionOfFrost
					|| item instanceof ScrollOfRecharging
					|| item instanceof ScrollOfTransmutation);
		}

		@Override
		public void onSelect(Item item) {

			if (item == null){
				return;
			}

			item.detach(Dungeon.hero.belongings.backpack);
			if (item instanceof PotionOfLiquidFlame) {
				Sample.INSTANCE.play(Assets.Sounds.BURNING);
				curUser.sprite.emitter().burst( FlameParticle.FACTORY, 12 );
				summonClass = Elemental.FireElemental.class;

			} else if (item instanceof PotionOfFrost){
				Sample.INSTANCE.play(Assets.Sounds.SHATTER);
				curUser.sprite.emitter().burst( MagicMissile.MagicParticle.FACTORY, 12 );
				summonClass = Elemental.FrostElemental.class;

			} else if (item instanceof ScrollOfRecharging){
				Sample.INSTANCE.play(Assets.Sounds.ZAP);
				curUser.sprite.emitter().burst( ShaftParticle.FACTORY, 12 );
				summonClass = Elemental.ShockElemental.class;

			} else if (item instanceof ScrollOfTransmutation){
				Sample.INSTANCE.play(Assets.Sounds.READ);
				curUser.sprite.emitter().burst( RainbowParticle.BURST, 12 );
				summonClass = Elemental.ChaosElemental.class;
			}

			curUser.sprite.operate(curUser.pos);

			updateQuickslot();
		}
	};

	public static class InvisAlly extends AllyBuff{

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add(CharSprite.State.HEARTS);
			else    target.sprite.remove(CharSprite.State.HEARTS);
		}

	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{Embers.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};

			cost = 6;

			output = SummonElemental.class;
			outQuantity = 5;
		}

	}
}
