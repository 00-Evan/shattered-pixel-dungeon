/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import java.util.ArrayList;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Death;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfDisintegration extends Wand {

	{
		name = "Wand of Disintegration";
		image = ItemSpriteSheet.WAND_DISINTEGRATION;

		collisionProperties = Ballistica.WONT_STOP;
	}
	
	@Override
	protected void onZap( Ballistica beam ) {
		
		boolean terrainAffected = false;
		
		int level = level();
		
		int maxDistance = Math.min(distance(), beam.dist);
		
		ArrayList<Char> chars = new ArrayList<Char>();

		int terrainPassed = 2, terrainBonus = 0;
		for (int c : beam.subPath(1, maxDistance)) {
			
			Char ch;
			if ((ch = Actor.findChar( c )) != null) {

				//we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
				//terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
				terrainBonus += terrainPassed/3;
				terrainPassed = terrainPassed%3;

				chars.add( ch );
			}

			if (Level.flamable[c]) {
				
				Level.set( c, Terrain.EMBERS );
				GameScene.updateMap( c );
				terrainAffected = true;
				
			}

			if (Level.solid[c])
				terrainPassed++;
			
			CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
		}
		
		if (terrainAffected) {
			Dungeon.observe();
		}
		
		int lvl = level + chars.size() + terrainBonus;
		int dmgMin = lvl;
		int dmgMax = (int) (8 + lvl * lvl / 3f);
		for (Char ch : chars) {
			processSoulMark(ch, chargesPerCast());
			ch.damage( Random.NormalIntRange( dmgMin, dmgMax ), this );
			ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
			ch.sprite.flash();
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//less likely Grim proc
		if (Random.Int(3) == 0)
			new Death().proc( staff, attacker, defender, damage);
	}

	private int distance() {
		return level()*2 + 4;
	}
	
	@Override
	protected void fx( Ballistica beam, Callback callback ) {
		
		int cell = beam.path.get(Math.min(beam.dist, distance()));
		curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.tileCenterToWorld( cell )));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0x220022);
		particle.am = 0.6f;
		particle.setLifespan(0.6f);
		particle.acc.set(40, -40);
		particle.setSize(0f, 3f);
		particle.shuffleXY(2f);
	}

	@Override
	public String desc() {
		return
			"This wand is made from a solid smooth chunk of obsidian, with a deep purple light running up its side, " +
			"ending at the tip. It glows with destructive energy, waiting to shoot forward.\n\n" +
			"This wand shoots a beam that pierces any obstacle, and will go farther the more it is upgraded.\n\n" +
			"This wand deals bonus damage the more enemies and terrain it penetrates.";
	}
}
