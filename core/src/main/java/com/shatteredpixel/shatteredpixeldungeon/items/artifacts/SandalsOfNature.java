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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SandalsOfNature extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_SANDALS;

		levelCap = 3;

		charge = 0;
		chargeCap = 100;

		defaultAction = AC_ROOT;
	}

	public static final String AC_FEED = "FEED";
	public static final String AC_ROOT = "ROOT";

	public ArrayList<Class> seeds = new ArrayList<>();
	public Class curSeedEffect = null;

	private static final HashMap<Class<? extends Plant.Seed>, Integer> seedColors = new HashMap<>();
	static {
		seedColors.put(Rotberry.Seed.class,     0xCC0022);
		seedColors.put(Firebloom.Seed.class,    0xFF7F00);
		seedColors.put(Swiftthistle.Seed.class, 0xCCBB00);
		seedColors.put(Sungrass.Seed.class,     0x2EE62E);
		seedColors.put(Icecap.Seed.class,       0x66B3FF);
		seedColors.put(Stormvine.Seed.class,    0x195D80);
		seedColors.put(Sorrowmoss.Seed.class,   0xA15CE5);
		seedColors.put(Mageroyal.Seed.class,    0xFF4CD2);
		seedColors.put(Earthroot.Seed.class,    0x67583D);
		seedColors.put(Starflower.Seed.class,   0x404040);
		seedColors.put(Fadeleaf.Seed.class,     0x919999);
		seedColors.put(Blindweed.Seed.class,    0XD9D9D9);
	}

	private static final HashMap<Class<? extends Plant.Seed>, Integer> seedChargeReqs = new HashMap<>();
	static {
		seedChargeReqs.put(Rotberry.Seed.class,     8);
		seedChargeReqs.put(Firebloom.Seed.class,    20);
		seedChargeReqs.put(Swiftthistle.Seed.class, 20);
		seedChargeReqs.put(Sungrass.Seed.class,     80);
		seedChargeReqs.put(Icecap.Seed.class,       20);
		seedChargeReqs.put(Stormvine.Seed.class,    20);
		seedChargeReqs.put(Sorrowmoss.Seed.class,   20);
		seedChargeReqs.put(Mageroyal.Seed.class,    12);
		seedChargeReqs.put(Earthroot.Seed.class,    40);
		seedChargeReqs.put(Starflower.Seed.class,   40);
		seedChargeReqs.put(Fadeleaf.Seed.class,     12);
		seedChargeReqs.put(Blindweed.Seed.class,    12);
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.buff(MagicImmune.class) != null){
			return actions;
		}
		if (isEquipped( hero ) && !cursed) {
			actions.add(AC_FEED);
		}
		if (isEquipped( hero )
				&& !cursed
				&& curSeedEffect != null
				&& charge >= seedChargeReqs.get(curSeedEffect)) {
			actions.add(AC_ROOT);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_FEED)){

			GameScene.selectItem(itemSelector);

		} else if (action.equals(AC_ROOT) && !cursed){

			if (!isEquipped( hero ))                                GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (curSeedEffect == null)                         GLog.i( Messages.get(this, "no_effect") );
			else if (charge < seedChargeReqs.get(curSeedEffect))    GLog.i( Messages.get(this, "low_charge") );
			else {
				GameScene.selectCell(cellSelector);
			}
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Naturalism();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		target.buff(Naturalism.class).charge(amount);
	}

	@Override
	public ItemSprite.Glowing glowing() {
		if (curSeedEffect != null){
			return new ItemSprite.Glowing(seedColors.get(curSeedEffect));
		}
		return null;
	}

	@Override
	public String name() {
		if (level() == 0)   return super.name();
		else                return Messages.get(this, "name_" + level());
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc_" + (level()+1));

		if ( isEquipped ( Dungeon.hero ) ) {
			desc += "\n\n";

			if (!cursed) {
				desc += Messages.get(this, "desc_hint");
			} else {
				desc += Messages.get(this, "desc_cursed");
			}

		}

		if (curSeedEffect != null){
			//TODO fix in v2.0.0 when this line is translated
			if (Messages.lang() == Languages.ENGLISH){
				desc += "\n\n" + Messages.get(this, "desc_ability",
						Messages.titleCase(Messages.get(curSeedEffect, "name")),
						seedChargeReqs.get(curSeedEffect));
			} else {
				desc += "\n\n" + Messages.get(this, "desc_ability", seedChargeReqs.get(curSeedEffect));
			}
		}

		if (!seeds.isEmpty()){
			desc += "\n\n" + Messages.get(this, "desc_seeds", seeds.size());
		}

		return desc;
	}

	@Override
	public Item upgrade() {
		if (level() < 0)        image = ItemSpriteSheet.ARTIFACT_SANDALS;
		else if (level() == 0)  image = ItemSpriteSheet.ARTIFACT_SHOES;
		else if (level() == 1)  image = ItemSpriteSheet.ARTIFACT_BOOTS;
		else if (level() >= 2)  image = ItemSpriteSheet.ARTIFACT_GREAVES;
		return super.upgrade();
	}

	public boolean canUseSeed(Item item){
		return item instanceof Plant.Seed
				&& !seeds.contains(item.getClass())
				&& (level() < 3 || curSeedEffect != item.getClass());
	}

	private static final String SEEDS = "seeds";
	private static final String CUR_SEED_EFFECT = "cur_seed_effect";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(SEEDS, seeds.toArray(new Class[seeds.size()]));
		bundle.put(CUR_SEED_EFFECT, curSeedEffect);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(SEEDS)) {
			Collections.addAll(seeds, bundle.getClassArray(SEEDS));
		}
		curSeedEffect = bundle.getClass(CUR_SEED_EFFECT);

		if (level() == 1)  image = ItemSpriteSheet.ARTIFACT_SHOES;
		else if (level() == 2)  image = ItemSpriteSheet.ARTIFACT_BOOTS;
		else if (level() >= 3)  image = ItemSpriteSheet.ARTIFACT_GREAVES;
	}

	public class Naturalism extends ArtifactBuff{
		public void charge(float amount) {
			if (cursed || target.buff(MagicImmune.class) != null) return;
			if (charge < chargeCap){
				//0.5 charge per grass at +0, up to 1 at +10
				float chargeGain = (3f + level())/6f;
				chargeGain *= amount;
				chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
				partialCharge += Math.max(0, chargeGain);
				while (partialCharge >= 1){
					charge++;
					partialCharge--;
				}
				charge = Math.min(charge, chargeCap);
				updateQuickslot();
			}
		}
	}

	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(SandalsOfNature.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return VelvetPouch.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return canUseSeed(item);
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof Plant.Seed) {
				if (level() < 3) seeds.add(0, item.getClass());
				curSeedEffect = item.getClass();

				Hero hero = Dungeon.hero;
				hero.sprite.operate( hero.pos );
				Sample.INSTANCE.play( Assets.Sounds.PLANT );
				hero.busy();
				hero.spend( Actor.TICK );
				if (seeds.size() >= 3+(level()*3)){
					seeds.clear();
					upgrade();

					if (level() >= 1 && level() <= 3) {
						GLog.p( Messages.get(SandalsOfNature.class, "levelup") );
					}

				} else {
					GLog.i( Messages.get(SandalsOfNature.class, "absorb_seed") );
				}
				item.detach(hero.belongings.backpack);
			}
		}
	};

	protected CellSelector.Listener cellSelector = new CellSelector.Listener(){

		@Override
		public void onSelect(Integer cell) {
			if (cell != null){
				PathFinder.buildDistanceMap(curUser.pos, BArray.not(Dungeon.level.solid,null), 3);

				if (PathFinder.distance[cell] == Integer.MAX_VALUE){
					GLog.w(Messages.get(SandalsOfNature.class, "out_of_range"));
				} else {
					CellEmitter.get( cell ).burst( LeafParticle.GENERAL, 6 );

					Plant plant = ((Plant.Seed) Reflection.newInstance(curSeedEffect)).couch(cell, null);
					plant.activate(Actor.findChar(cell));
					Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float( 0.96f, 1.05f ) );

					charge -= seedChargeReqs.get(curSeedEffect);
					Talent.onArtifactUsed(Dungeon.hero);
					updateQuickslot();
					curUser.spendAndNext(1f);
				}
			}
		}

		@Override
		public String prompt() {
			return Messages.get(SandalsOfNature.class, "prompt_target");
		}
	};

}
