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

package net.casiello.pixeldungeonrescue.items.weapon.enchantments;

import net.casiello.pixeldungeonrescue.Badges;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.Statistics;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Corruption;
import net.casiello.pixeldungeonrescue.actors.buffs.PinCushion;
import net.casiello.pixeldungeonrescue.actors.buffs.SoulMark;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.actors.mobs.Mob;
import net.casiello.pixeldungeonrescue.items.weapon.Weapon;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.sprites.CharSprite;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Corrupting extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x440066 );
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
		if (defender.buff(Corruption.class) != null || !(defender instanceof Mob)) return damage;
		
		int level = Math.max( 0, weapon.buffedLvl() );
		
		// lvl 0 - 20%
		// lvl 1 ~ 22.5%
		// lvl 2 ~ 25%
		if (damage >= defender.HP
				&& !defender.isImmune(Corruption.class)
				&& Random.Int( level + 30 ) >= 24){
			
			Mob enemy = (Mob) defender;
			Hero hero = (attacker instanceof Hero) ? (Hero) attacker : Dungeon.hero;
			
			enemy.HP = enemy.HT;
			for (Buff buff : enemy.buffs()) {
				if (buff.type == Buff.buffType.NEGATIVE
						&& !(buff instanceof SoulMark)) {
					buff.detach();
				} else if (buff instanceof PinCushion){
					buff.detach();
				}
			}
			if (enemy.alignment == Char.Alignment.ENEMY){
				enemy.rollToDropLoot();
			}
			
			Buff.affect(enemy, Corruption.class);
			
			Statistics.enemiesSlain++;
			Badges.validateMonstersSlain();
			Statistics.qualifiedForNoKilling = false;
			if (enemy.EXP > 0 && hero.lvl <= enemy.maxLvl) {
				hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(enemy, "exp", enemy.EXP));
				hero.earnExp(enemy.EXP, enemy.getClass());
			} else {
				hero.earnExp(0, enemy.getClass());
			}
			
			return 0;
		}
		
		return damage;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}
}
