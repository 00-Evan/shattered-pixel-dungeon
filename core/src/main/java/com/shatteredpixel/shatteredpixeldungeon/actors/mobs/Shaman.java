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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

//TODO stats on these might be a bit weak
public abstract class Shaman extends Mob {
	
	{
		HP = HT = 35;
		defenseSkill = 15;
		
		EXP = 8;
		maxLvl = 16;
		
		loot = Generator.Category.WAND;
		lootChance = 0.03f; //initially, see rollToDropLoot
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 5, 10 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 18;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 6);
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	public void rollToDropLoot() {
		//each drop makes future drops 1/3 as likely
		// so loot chance looks like: 1/33, 1/100, 1/300, 1/900, etc.
		lootChance *= Math.pow(1/3f, Dungeon.LimitedDrops.SHAMAN_WAND.count);
		super.rollToDropLoot();
	}

	@Override
	protected Item createLoot() {
		Dungeon.LimitedDrops.SHAMAN_WAND.count++;
		return super.createLoot();
	}

	protected boolean doAttack(Char enemy ) {
		
		if (Dungeon.level.adjacent( pos, enemy.pos )) {
			
			return super.doAttack( enemy );
			
		} else {
			
			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}
		}
	}
	
	//used so resistances can differentiate between melee and magical attacks
	public static class EarthenBolt{}
	
	private void zap() {
		spend( 1f );
		
		if (hit( this, enemy, true )) {
			
			if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
				debuff( enemy );
				Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
			}
			
			int dmg = Random.NormalIntRange( 6, 15 );
			enemy.damage( dmg, new EarthenBolt() );
			
			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( Messages.get(this, "bolt_kill") );
			}
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}
	}
	
	protected abstract void debuff( Char enemy );
	
	public void onZapComplete() {
		zap();
		next();
	}
	
	@Override
	public String description() {
		return super.description() + "\n\n" + Messages.get(this, "spell_desc");
	}
	
	public static class RedShaman extends Shaman {
		{
			spriteClass = ShamanSprite.Red.class;
		}
		
		@Override
		protected void debuff( Char enemy ) {
			Buff.prolong( enemy, Weakness.class, Weakness.DURATION );
		}
	}
	
	public static class BlueShaman extends Shaman {
		{
			spriteClass = ShamanSprite.Blue.class;
		}
		
		@Override
		protected void debuff( Char enemy ) {
			Buff.prolong( enemy, Vulnerable.class, Vulnerable.DURATION );
		}
	}
	
	public static class PurpleShaman extends Shaman {
		{
			spriteClass = ShamanSprite.Purple.class;
		}
		
		@Override
		protected void debuff( Char enemy ) {
			Buff.prolong( enemy, Hex.class, Hex.DURATION );
		}
	}
	
	//TODO a rare variant that helps brutes?
	
	public static Class<? extends Shaman> random(){
		float roll = Random.Float();
		if (roll < 0.4f){
			return RedShaman.class;
		} else if (roll < 0.8f){
			return BlueShaman.class;
		} else {
			return PurpleShaman.class;
		}
	}
}
