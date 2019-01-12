/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

import java.util.ArrayList;

public class AlchemistsToolkit extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_TOOLKIT;
		defaultAction = AC_BREW;

		levelCap = 10;
		
		charge = 0;
		partialCharge = 0;
		chargeCap = 100;
	}

	public static final String AC_BREW = "BREW";
	
	protected WndBag.Mode mode = WndBag.Mode.POTION;
	
	private boolean alchemyReady = false;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && !cursed)
			actions.add(AC_BREW);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals(AC_BREW)){
			if (!isEquipped(hero))                                          GLog.i( Messages.get(this, "need_to_equip") );
			else if (cursed)                                                GLog.w( Messages.get(this, "cursed") );
			else if (!alchemyReady)                                         GLog.i( Messages.get(this, "not_ready") );
			else if (hero.visibleEnemies() > hero.mindVisionEnemies.size()) GLog.i( Messages.get(this, "enemy_near") );
			else {
				
				AlchemyScene.setProvider(hero.buff(kitEnergy.class));
				Game.switchScene(AlchemyScene.class);
			}
			
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new kitEnergy();
	}
	
	@Override
	public void charge(Hero target) {
		if (charge < chargeCap){
			partialCharge += 0.5f;
			if (partialCharge >= 1){
				partialCharge--;
				charge++;
				updateQuickslot();
			}
		}
	}
	
	public void absorbEnergy( int energy ){
		
		exp += energy;
		while (exp >= 10 && level() < levelCap){
			upgrade();
			exp -= 10;
		}
		if (level() == levelCap){
			partialCharge += exp;
			energy -= exp;
			exp = 0;
		}
		
		partialCharge += energy/3f;
		while (partialCharge >= 1){
			
			partialCharge -= 1;
			charge++;
			
			if (charge >= chargeCap){
				charge = chargeCap;
				partialCharge = 0;
				break;
			}
		}
		updateQuickslot();
		
	}

	@Override
	public String desc() {
		String result = Messages.get(this, "desc");

		if (isEquipped(Dungeon.hero)) {
			if (cursed)             result += "\n\n" + Messages.get(this, "desc_cursed");
			else if (!alchemyReady) result += "\n\n" + Messages.get(this, "desc_warming");
			else                    result += "\n\n" + Messages.get(this, "desc_hint");
		}
		
		return result;
	}
	
	@Override
	public boolean doEquip(Hero hero) {
		if (super.doEquip(hero)){
			alchemyReady = false;
			return true;
		} else {
			return false;
		}
	}
	
	private static final String READY = "ready";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(READY, alchemyReady);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		alchemyReady = bundle.getBoolean(READY);
	}
	
	public class kitEnergy extends ArtifactBuff implements AlchemyScene.AlchemyProvider {
		
		public void gainCharge(float levelPortion) {
			alchemyReady = true;
			
			if (cursed) return;
			
			if (charge < chargeCap) {
				
				//generates 2 energy every hero level, +0.1 energy per toolkit level
				//to a max of 12 energy per hero level
				//This means that energy absorbed into the kit is recovered in 6.67 hero levels (as 33% of input energy is kept)
				//exp towards toolkit levels is included here
				float effectiveLevel = GameMath.gate(0, level() + exp/10f, 10);
				partialCharge += (2 + (1f * effectiveLevel)) * levelPortion;
				
				//charge is in increments of 1/10 max hunger value.
				while (partialCharge >= 1) {
					charge++;
					partialCharge -= 1;
					
					if (charge == chargeCap){
						GLog.p( Messages.get(AlchemistsToolkit.class, "full") );
						partialCharge = 0;
					}
					
					updateQuickslot();
				}
			} else
				partialCharge = 0;
		}
		
		@Override
		public int getEnergy() {
			return charge;
		}
		
		@Override
		public void spendEnergy(int reduction) {
			charge = Math.max(0, charge - reduction);
		}
	}
	
	public static class upgradeKit extends Recipe {
		
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			return ingredients.get(0) instanceof AlchemistsToolkit
					&& !AlchemyScene.providerIsToolkit();
		}
		
		private static int lastCost;
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return lastCost = Math.max(1, AlchemyScene.availableEnergy());
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			AlchemistsToolkit existing = (AlchemistsToolkit) ingredients.get(0);
			
			existing.absorbEnergy(lastCost);
			
			return existing;
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			AlchemistsToolkit sample = new AlchemistsToolkit();
			sample.identify();
			
			AlchemistsToolkit existing = (AlchemistsToolkit) ingredients.get(0);
			
			sample.charge = existing.charge;
			sample.partialCharge = existing.partialCharge;
			sample.exp = existing.exp;
			sample.level(existing.level());
			sample.absorbEnergy(AlchemyScene.availableEnergy());
			return sample;
		}
	}

}
