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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Sai extends MeleeWeapon {
	public int R;
	{
		image = ItemSpriteSheet.SAI;
		hitSound = Assets.Sounds.HIT_STAB;
		hitSoundPitch = 1.3f;

		tier = 4;
		DLY = 0.5f; //2x speed
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return new ItemSprite.Glowing(0x880000, 6f);
	}

	public String statsInfo(){
			return ("持有这个武器，你在迅猛一击的时候可以吸血!你上次吸血的结果为"+"_"+R+"_"+"点");
	}


	@Override
	public int proc(Char attacker, Char defender, int damage ) {
		switch (Random.Int(7)) {
			case 0:case 1:case 2:case 3:case 4:
			default:
				return Random.NormalIntRange( 5, 12 );
			case 5:case 6:case 7:
				//角色最大血量*0.01+武器等级*0.5+0.8
				if(hero.HP >= hero.HT){
					GLog.p("血量已满！无法回血");
				} else {
					R = (int) (hero.HT * 0.01 + (buffedLvl() * 0.5) + 0.8);
					hero.HP += (int) hero.HT * 0.01 + (buffedLvl()) + 0.8;
					hero.sprite.showStatus(CharSprite.POSITIVE, ("+" + R + "HP"));
					GLog.p("迅猛一击，回血成功！");
				}
				return super.proc(attacker, defender, damage);
		}
	}


	@Override
	public int max(int lvl) {
		return  Math.round(2.5f*(tier+1)) +     //10 base, down from 20
				lvl*Math.round(0.5f*(tier+1));  //+2 per level, down from +4
	}

}
