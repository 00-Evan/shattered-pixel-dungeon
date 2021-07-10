/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.items.spells;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.actors.buffs.ArtifactRecharge;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Recharging;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.items.artifacts.Artifact;
import com.saqfish.spdnet.items.quest.MetalShard;
import com.saqfish.spdnet.items.scrolls.ScrollOfRecharging;
import com.saqfish.spdnet.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.saqfish.spdnet.items.wands.CursedWand;
import com.saqfish.spdnet.mechanics.Ballistica;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WildEnergy extends TargetedSpell {
	
	{
		image = ItemSpriteSheet.WILD_ENERGY;
	}
	
	//we rely on cursedWand to do fx instead
	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		affectTarget(bolt, curUser);
	}
	
	@Override
	protected void affectTarget(Ballistica bolt, final Hero hero) {
		CursedWand.cursedZap(this, hero, bolt, new Callback() {
			@Override
			public void call() {
				Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
				Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
				ScrollOfRecharging.charge(hero);

				hero.belongings.charge(1f);
				for (Buff b : hero.buffs()){
					if (b instanceof Artifact.ArtifactBuff) ((Artifact.ArtifactBuff) b).charge(hero, 4);
				}

				Buff.affect(hero, Recharging.class, 8f);
				Buff.affect(hero, ArtifactRecharge.class).prolong( 8 ).ignoreHornOfPlenty = false;
				
				detach( curUser.belongings.backpack );
				updateQuickslot();
				curUser.spendAndNext( 1f );
			}
		});
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 100) / 5f));
	}
	
	public static class Recipe extends com.saqfish.spdnet.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfMysticalEnergy.class, MetalShard.class};
			inQuantity = new int[]{1, 1};
			
			cost = 8;
			
			output = WildEnergy.class;
			outQuantity = 5;
		}
		
	}
}
