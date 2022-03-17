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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class ReclaimTrap extends TargetedSpell {
	
	{
		image = ItemSpriteSheet.RECLAIM_TRAP;
	}
	
	private Class<?extends Trap> storedTrap = null;
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		//prevents exploits
		if (storedTrap != null){
			actions.remove(AC_DROP);
			actions.remove(AC_THROW);
		}
		return actions;
	}

	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {
		if (storedTrap == null) {
			quantity++; //storing a trap doesn't consume the spell
			Trap t = Dungeon.level.traps.get(bolt.collisionPos);
			if (t != null && t.active && t.visible) {
				t.disarm(); //even disarms traps that normally wouldn't be
				
				Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
				ScrollOfRecharging.charge(hero);
				storedTrap = t.getClass();
				
			} else {
				GLog.w(Messages.get(this, "no_trap"));
			}
		} else {
			
			Trap t = Reflection.newInstance(storedTrap);
			storedTrap = null;
			
			t.pos = bolt.collisionPos;
			t.activate();
			
		}
	}
	
	@Override
	public String desc() {
		String desc = super.desc();
		if (storedTrap != null){
			desc += "\n\n" + Messages.get(this, "desc_trap", Messages.get(storedTrap, "name"));
		}
		return desc;
	}
	
	private static final ItemSprite.Glowing[] COLORS = new ItemSprite.Glowing[]{
			new ItemSprite.Glowing( 0xFF0000 ),
			new ItemSprite.Glowing( 0xFF8000 ),
			new ItemSprite.Glowing( 0xFFFF00 ),
			new ItemSprite.Glowing( 0x00FF00 ),
			new ItemSprite.Glowing( 0x00FFFF ),
			new ItemSprite.Glowing( 0x8000FF ),
			new ItemSprite.Glowing( 0xFFFFFF ),
			new ItemSprite.Glowing( 0x808080 ),
			new ItemSprite.Glowing( 0x000000 )
	};
	
	@Override
	public ItemSprite.Glowing glowing() {
		if (storedTrap != null){
			return COLORS[Reflection.newInstance(storedTrap).color];
		}
		return null;
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((40 + 50) / 4f));
	}
	
	private static final String STORED_TRAP = "stored_trap";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (storedTrap != null) bundle.put(STORED_TRAP, storedTrap);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(STORED_TRAP)) storedTrap = bundle.getClass(STORED_TRAP);
	}
	
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfMagicMapping.class, MetalShard.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = ReclaimTrap.class;
			outQuantity = 4;
		}
		
	}
	
}
