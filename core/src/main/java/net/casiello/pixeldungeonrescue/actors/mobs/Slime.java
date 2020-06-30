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

package net.casiello.pixeldungeonrescue.actors.mobs;

import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Weakness;
import net.casiello.pixeldungeonrescue.items.Generator;
import net.casiello.pixeldungeonrescue.items.Item;
import net.casiello.pixeldungeonrescue.items.weapon.melee.MeleeWeapon;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.sprites.SlimeSprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public abstract class Slime extends Mob {
	
	{
		HP = HT = 20;
		defenseSkill = 5;
		
		EXP = 4;
		maxLvl = 9;
		
		lootChance = 0.1f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public void damage(int dmg, Object src) {
		if (dmg >= 5){
			//takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
			dmg = 4 + (int)(Math.sqrt(8*(dmg - 4) + 1) - 1)/2;
		}
		super.damage(dmg, src);
	}
	
	@Override
	protected Item createLoot() {
		Generator.Category c = Generator.Category.WEP_T2;
		MeleeWeapon w = (MeleeWeapon) Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		w.level(0);
		return w;
	}

	@Override
	public String description() {
		return super.description() + "\n\n" + Messages.get(this, "spell_desc");
	}

	public static class LimeSlime extends Slime {
		{
			spriteClass = SlimeSprite.Lime.class;
		}
	}
	public static class LemonSlime extends Slime {
		{
			spriteClass = SlimeSprite.Lemon.class;
		}
	}
	public static class StrawberrySlime extends Slime {
		{
			spriteClass = SlimeSprite.Strawberry.class;
		}
	}
	public static class GrapeSlime extends Slime {
		{
			spriteClass = SlimeSprite.Grape.class;
		}
	}
	public static class OrangeSlime extends Slime {
		{
			spriteClass = SlimeSprite.Orange.class;
		}
	}

	public static Class<? extends Slime> random(){
		int roll = Random.Int(5);
		if (roll == 0){
			return Slime.LimeSlime.class;
		} else if (roll == 1){
			return Slime.LemonSlime.class;
		} else if (roll == 2){
			return Slime.StrawberrySlime.class;
		} else if (roll == 3){
			return Slime.GrapeSlime.class;
		} else {
			return Slime.OrangeSlime.class;
		}
	}
}
