/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.PHYSICIST;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CrowBar extends MeleeWeapon {

	public static final String AC_PICKLOCK = "PICKLOCK";

	public static final float TIME_TO_PICKLOCK = 1f;


	{
		image = ItemSpriteSheet.CROWBAR;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1f;

		defaultAction = AC_PICKLOCK;

		tier = 2;
		//ACC = 1.32f; 32% boost to accuracy 额外精准
	}



	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //12 base, down from 15	最大伤害从15降低到12
				lvl*(tier+1);   //scaling unchanged	升级增益不变
	}


		//strength req decreases at +1,+3,+6,+10,etc.

	@Override
	public int STRReq(){
		int req = STRReq(level());
		if ( masteryPotionBonus | hero.heroClass == PHYSICIST ) {
			req -= 2;
		}
		return req;
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_PICKLOCK );
		return actions;
	}


/*
	public void onSelect(Integer cell) {

		Door target = null;
		if (cell != null) {

					target =  door ;
			}
		}
	}*/

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

			if ( Random.Int(0,100) < Math.round( 30f+ level()*15 )) {
				if (action.equals(AC_PICKLOCK)) {
					GLog.i(Messages.get(this,"trylock_success"));

					hero.sprite.operate(hero.pos);
					hero.busy();

					hero.spend( TIME_TO_PICKLOCK );
					hero.spendAndNext( TIME_TO_PICKLOCK );

					GameScene.updateKeyDisplay();
				}

			}else {
				GLog.i(Messages.get(this, "trylock_failed"));
		}
	}



	/*@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );
		if(Random.Float()<0.2f){

			if (action.equals( AC_PICKLOCK )) {
				GLog.i(Messages.get(this,"trylock_success"));
				hero.spend(5f);
				hero.busy();
				hero.sprite.operate(hero.pos);
				//Dungeon.level.set(	hero.pos, );
				ItemSprite sprite = Dungeon.level.drop(new Key(), hero.pos).sprite;


			/*
				if (hero.buff(MagicImmune.class) != null){
					GLog.w( Messages.get(this, "no_magic") );
				}  else
				{
				curUser = hero;
				curItem = detach(hero.belongings.backpack );
				//doPickLock();
				}
			} else {
				GLog.i(Messages.get(this,"trylock_failed"));
			}
		}
	}*/

	@Override
	public String info() {
		String info = desc();
		if (cursed && isEquipped( hero )) {
			info += "\n\n" + Messages.get(Armor.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Armor.class, "cursed");
		} else if (seal != null) {
			info += "\n\n" + Messages.get(Armor.class, "seal_attached");
		} else if (!isIdentified() && cursedKnown){
			if (enchantment != null && enchantment.curse()) {
				info += "\n\n" + Messages.get(Armor.class, "weak_cursed");
			} else {
				info += "\n\n" + Messages.get(Armor.class, "not_cursed");
			}
		}
		if( hero.heroClass == PHYSICIST ){
			info += "\n\n" + Messages.get(this, "mastery");
		}
		return info;
	}

//对怪物额外伤害
	@Override
	public int proc(Char attacker, Char ch, int damage ){
		if (ch instanceof Mimic){
			damage =999;
			}
		return super.proc(attacker, ch, damage);
		}


	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Mace.heavyBlowAbility(hero, target, 1.65f, this);
	}

}
