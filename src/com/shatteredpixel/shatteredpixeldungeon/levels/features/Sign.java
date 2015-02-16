/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;

public class Sign {

	private static final String TXT_DEAD_END = 
		"What are you doing here?!";

	private static final String[] TIPS = {
		"Almost all equipment has a strength requirement. Don't overestimate your strength, using equipment you can't " +
				"handle has big penalties!\n\nRaising your strength is not the only way to access better equipment, " +
				"you can also lower equipment strength requirements with Scrolls of Upgrade.\n\n\n" +
				"Items found in the dungeon will often be unidentified. Some items will have unknown effects, others " +
				"may be upgraded, or degraded and cursed! Unidentified items are unpredictable, so be careful!",
		"Charging forward recklessly is a great way to get killed.\n\n" +
				"Slowing down a bit to examine enemies and use the environment and items to your advantage can make a" +
				" big difference.\n\nThe dungeon is full of traps and hidden passageways as well, keep your eyes open!",
		"Levelling up is important!\n\nBeing about the same level as the floor you are on is a good idea. " +
				"Hunger may keep you moving in search of more food, but don't be afraid to slow down a little and train." +
				"\n\n\nHunger and health are both resources, and using them well can mean starving yourself in order" +
				" to help conserve food, if you have some health to spare.",
		"The rogue isn't the only character that benefits from being sneaky. You can retreat to the other side of a " +
				"door to ambush a chasing opponent for a guaranteed hit!" +
				"\n\nAny attack on an unaware opponent is guaranteed to hit them.",

		"Note to all sewer maintenance & cleaning crews: TURN BACK NOW. Some sort of sludge monster has made its home" +
				" here and several crews have been lost trying to deal with it.\n\n" +
				"Approval has been given to seal off the lower sewers, this area has been condemned, LEAVE NOW.",

		"Pixel-Mart - all you need for successful adventure!",
		"Identify your potions and scrolls as soon as possible. Don't put it off to the moment " +
				"when you actually need them.",
		"Being hungry doesn't hurt, but starving does hurt.",
		"Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
				"a closed door when you know it is approaching.",

		"Don't let The Tengu out!",

		"Pixel-Mart. Spend money. Live longer.",
		"When you're attacked by several monsters at the same time, try to retreat behind a door.",
		"If you are burning, you can't put out the fire in the water while levitating.",
		"There is no sense in possessing more than one unblessed Ankh at the same time, " +
				"because you will lose them upon resurrecting.",

		"DANGER! Heavy machinery can cause injury, loss of limbs or death!",

		"Pixel-Mart. A safer life in dungeon.",
		"When you upgrade an enchanted weapon, there is a chance to destroy that enchantment.",
		"In a Well of Transmutation you can get an item, that cannot be obtained otherwise.",
		"The only way to enchant a weapon is by upgrading it with a Scroll of Weapon Upgrade.",

		"No weapons allowed in the presence of His Majesty!",

		"Pixel-Mart. Special prices for demon hunters!",

		//hmm.. I wonder what this is?
		"standOfF roW",
		"fraCtion doWnpOur",
		"gaffe MaSts"
	};
	
	private static final String TXT_BURN =
		"As you try to read the sign it bursts into greenish flames.";
	
	public static void read( int pos ) {
		
		if (Dungeon.level instanceof DeadEndLevel) {
			
			GameScene.show( new WndMessage( TXT_DEAD_END ) );
			
		} else {
			
			int index = Dungeon.depth - 1;
			
			if (index < TIPS.length) {
				GameScene.show( new WndMessage( TIPS[index] ) );

                if (index >= 21) {

                    Level.set( pos, Terrain.EMBERS );
                    GameScene.updateMap( pos );
                    GameScene.discoverTile( pos, Terrain.SIGN );

                    GLog.w( TXT_BURN );

                    CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
                    Sample.INSTANCE.play( Assets.SND_BURNING );
                }

			}
		}
	}
}
