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

package com.saqfish.spdnet.items.weapon.missiles.darts;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.PinCushion;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.actors.hero.Talent;
import com.saqfish.spdnet.items.Generator;
import com.saqfish.spdnet.items.wands.WandOfRegrowth;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.plants.Blindweed;
import com.saqfish.spdnet.plants.Dreamfoil;
import com.saqfish.spdnet.plants.Earthroot;
import com.saqfish.spdnet.plants.Fadeleaf;
import com.saqfish.spdnet.plants.Firebloom;
import com.saqfish.spdnet.plants.Icecap;
import com.saqfish.spdnet.plants.Plant;
import com.saqfish.spdnet.plants.Rotberry;
import com.saqfish.spdnet.plants.Sorrowmoss;
import com.saqfish.spdnet.plants.Starflower;
import com.saqfish.spdnet.plants.Stormvine;
import com.saqfish.spdnet.plants.Sungrass;
import com.saqfish.spdnet.plants.Swiftthistle;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.sprites.ItemSprite;
import com.saqfish.spdnet.windows.WndOptions;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TippedDart extends Dart {
	
	{
		tier = 2;

		baseUses = 1f;
	}
	
	private static final String AC_CLEAN = "CLEAN";
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.remove( AC_TIP );
		actions.add( AC_CLEAN );
		return actions;
	}
	
	@Override
	public void execute(final Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals( AC_CLEAN )){
			
			GameScene.show(new WndOptions(new ItemSprite(this),
					Messages.titleCase(name()),
					Messages.get(this, "clean_desc"),
					Messages.get(this, "clean_all"),
					Messages.get(this, "clean_one"),
					Messages.get(this, "cancel")){
				@Override
				protected void onSelect(int index) {
					if (index == 0){
						detachAll(hero.belongings.backpack);
						new Dart().quantity(quantity).collect();
						
						hero.spend( 1f );
						hero.busy();
						hero.sprite.operate(hero.pos);
					} else if (index == 1){
						detach(hero.belongings.backpack);
						if (!new Dart().collect()) Dungeon.level.drop(new Dart(), hero.pos).sprite.drop();
						
						hero.spend( 1f );
						hero.busy();
						hero.sprite.operate(hero.pos);
					}
				}
			});
			
		}
	}
	
	//exact same damage as regular darts, despite being higher tier.

	@Override
	protected void rangedHit(Char enemy, int cell) {
		targetPos = cell;
		super.rangedHit( enemy, cell);
		
		//need to spawn a dart
		if (durability <= 0){
			//attempt to stick the dart to the enemy, just drop it if we can't.
			Dart d = new Dart();
			if (enemy.isAlive() && sticky) {
				PinCushion p = Buff.affect(enemy, PinCushion.class);
				if (p.target == enemy){
					p.stick(d);
					return;
				}
			}
			Dungeon.level.drop( d, enemy.pos ).sprite.drop();
		}
	}

	private static int targetPos = -1;

	@Override
	protected float durabilityPerUse() {
		float use = super.durabilityPerUse();
		
		use /= (1 + Dungeon.hero.pointsInTalent(Talent.DURABLE_TIPS));

		//checks both destination and source position
		float lotusPreserve = 0f;
		if (targetPos != -1){
			for (Char ch : Actor.chars()){
				if (ch instanceof WandOfRegrowth.Lotus){
					WandOfRegrowth.Lotus l = (WandOfRegrowth.Lotus) ch;
					if (l.inRange(targetPos)){
						lotusPreserve = Math.max(lotusPreserve, l.seedPreservation());
					}
				}
			}
			targetPos = -1;
		}
		int p = curUser == null ? Dungeon.hero.pos : curUser.pos;
		for (Char ch : Actor.chars()){
			if (ch instanceof WandOfRegrowth.Lotus){
				WandOfRegrowth.Lotus l = (WandOfRegrowth.Lotus) ch;
				if (l.inRange(p)){
					lotusPreserve = Math.max(lotusPreserve, l.seedPreservation());
				}
			}
		}
		use *= (1f - lotusPreserve);
		
		return use;
	}
	
	@Override
	public int value() {
		//value of regular dart plus half of the seed
		return 8 * quantity;
	}
	
	private static HashMap<Class<?extends Plant.Seed>, Class<?extends TippedDart>> types = new HashMap<>();
	static {
		types.put(Blindweed.Seed.class,     BlindingDart.class);
		types.put(Dreamfoil.Seed.class,     SleepDart.class);
		types.put(Earthroot.Seed.class,     ParalyticDart.class);
		types.put(Fadeleaf.Seed.class,      DisplacingDart.class);
		types.put(Firebloom.Seed.class,     IncendiaryDart.class);
		types.put(Icecap.Seed.class,        ChillingDart.class);
		types.put(Rotberry.Seed.class,      RotDart.class);
		types.put(Sorrowmoss.Seed.class,    PoisonDart.class);
		types.put(Starflower.Seed.class,    HolyDart.class);
		types.put(Stormvine.Seed.class,     ShockingDart.class);
		types.put(Sungrass.Seed.class,      HealingDart.class);
		types.put(Swiftthistle.Seed.class,  AdrenalineDart.class);
	}
	
	public static TippedDart getTipped( Plant.Seed s, int quantity ){
		return (TippedDart) Reflection.newInstance(types.get(s.getClass())).quantity(quantity);
	}
	
	public static TippedDart randomTipped( int quantity ){
		Plant.Seed s;
		do{
			s = (Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED);
		} while (!types.containsKey(s.getClass()));
		
		return getTipped(s, quantity );
		
	}
	
}
