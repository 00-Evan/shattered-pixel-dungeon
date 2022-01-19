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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;

public class SandalsOfNature extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_SANDALS;

		levelCap = 3;

		charge = 0;

		defaultAction = AC_ROOT;
	}

	public static final String AC_FEED = "FEED";
	public static final String AC_ROOT = "ROOT";

	public ArrayList<Class> seeds = new ArrayList<>();

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && level() < 3 && !cursed)
			actions.add(AC_FEED);
		if (isEquipped( hero ) && charge > 0)
			actions.add(AC_ROOT);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		super.execute(hero, action);

		if (action.equals(AC_FEED)){

			GameScene.selectItem(itemSelector);

		} else if (action.equals(AC_ROOT) && level() > 0){

			if (!isEquipped( hero )) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (charge == 0)    GLog.i( Messages.get(this, "no_charge") );
			else {
				Buff.prolong(hero, Roots.class, Roots.DURATION);
				Buff.affect(hero, Earthroot.Armor.class).level(charge);
				CellEmitter.bottom(hero.pos).start(EarthParticle.FACTORY, 0.05f, 8);
				Camera.main.shake(1, 0.4f);
				charge = 0;
				Talent.onArtifactUsed(Dungeon.hero);
				updateQuickslot();
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
	public String name() {
		if (level() == 0)   return super.name();
		else                return Messages.get(this, "name_" + level());
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc_" + (level()+1));

		if ( isEquipped ( Dungeon.hero ) ){
			desc += "\n\n";

			if (!cursed)
				desc += Messages.get(this, "desc_hint");
			else
				desc += Messages.get(this, "desc_cursed");

			if (level() > 0)
				desc += "\n\n" + Messages.get(this, "desc_ability");
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
		return item instanceof Plant.Seed && !seeds.contains(item.getClass());
	}


	private static final String SEEDS = "seeds";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(SEEDS, seeds.toArray(new Class[seeds.size()]));
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(SEEDS))
			Collections.addAll(seeds , bundle.getClassArray(SEEDS));
		if (level() == 1)  image = ItemSpriteSheet.ARTIFACT_SHOES;
		else if (level() == 2)  image = ItemSpriteSheet.ARTIFACT_BOOTS;
		else if (level() >= 3)  image = ItemSpriteSheet.ARTIFACT_GREAVES;
	}

	public class Naturalism extends ArtifactBuff{
		public void charge(float amount) {
			if (level() > 0 && charge < target.HT){
				//gain 1+(1*level)% of the difference between current charge and max HP.
				float chargeGain = (target.HT-charge) * (.01f+ level()*0.01f);
				chargeGain *= amount;
				chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
				partialCharge += Math.max(0, chargeGain);
				while (partialCharge > 1){
					charge++;
					partialCharge--;
				}
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
				if (seeds.contains(item.getClass())){
					GLog.w( Messages.get(SandalsOfNature.class, "already_fed") );
				} else {
					seeds.add(item.getClass());

					Hero hero = Dungeon.hero;
					hero.sprite.operate( hero.pos );
					Sample.INSTANCE.play( Assets.Sounds.PLANT );
					hero.busy();
					hero.spend( 2f );
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
		}
	};

}
