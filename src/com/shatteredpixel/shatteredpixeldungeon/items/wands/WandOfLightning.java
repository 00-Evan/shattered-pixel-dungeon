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
import java.util.HashSet;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shock;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfLightning extends Wand {

	{
		name = "Wand of Lightning";
		image = ItemSpriteSheet.WAND_LIGHTNING;
	}
	
	private ArrayList<Char> affected = new ArrayList<>();

	ArrayList<Lightning.Arc> arcs = new ArrayList<>();
	
	@Override
	protected void onZap( Ballistica bolt ) {

		//lightning deals less damage per-target, the more targets that are hit.
		float multipler = (0.6f + 0.4f*affected.size())/affected.size();
		if (Level.water[bolt.collisionPos]) multipler *= 1.5f;

		int min = 5+level;
		int max = Math.round(10 + (level * level / 4f));

		for (Char ch : affected){
			ch.damage(Math.round(Random.NormalIntRange(min, max) * multipler), LightningTrap.LIGHTNING);

			if (ch == Dungeon.hero) Camera.main.shake( 2, 0.3f );
			ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
			ch.sprite.flash();
		}

		if (!curUser.isAlive()) {
			Dungeon.fail( Utils.format( ResultDescriptions.ITEM, name ) );
			GLog.n( "You killed yourself with your own Wand of Lightning..." );
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//acts like shocking enchantment
		new Shock().proc(staff, attacker, defender, damage);
	}

	private void arc( Char ch ) {
		
		affected.add( ch );

		for (int i : Level.NEIGHBOURS8) {
			int cell = ch.pos + i;

			Char n = Actor.findChar( cell );
			if (n != null && !affected.contains( n )) {
				arcs.add(new Lightning.Arc(ch.pos, n.pos));
				arc(n);
			}
		}

		if (Level.water[ch.pos] && !ch.flying){
			for (int i : Level.NEIGHBOURS8DIST2) {
				int cell = ch.pos + i;
				//player can only be hit by lightning from an adjacent enemy.
				if (!Level.insideMap(cell) || Actor.findChar(cell) == Dungeon.hero) continue;

				Char n = Actor.findChar( ch.pos + i );
				if (n != null && !affected.contains( n )) {
					arcs.add(new Lightning.Arc(ch.pos, n.pos));
					arc(n);
				}
			}
		}
	}
	
	@Override
	protected void fx( Ballistica bolt, Callback callback ) {

		affected.clear();
		arcs.clear();
		arcs.add( new Lightning.Arc(bolt.sourcePos, bolt.collisionPos));

		int cell = bolt.collisionPos;

		Char ch = Actor.findChar( cell );
		if (ch != null) {
			arc(ch);
		} else {
			CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
		}

		//don't want to wait for the effect before processing damage.
		curUser.sprite.parent.add( new Lightning( arcs, null ) );
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0xFFFFFF);
		particle.am = 0.6f;
		particle.setLifespan(0.6f);
		particle.acc.set(0, +10);
		particle.speed.polar(-Random.Float(3.1415926f), 6f);
		particle.setSize(0f, 1.5f);
		particle.sizeJitter = 1f;
		particle.shuffleXY(2f);
		float dst = Random.Float(2f);
		particle.x -= dst;
		particle.y += dst;
	}

	@Override
	public String desc() {
		return
			"This wand is made out of solid metal, making it surprisingly heavy. " +
			"Two prongs curve together at the top, and electricity arcs between them.\n\n" +
			"This wand sends powerful lightning arcing through whatever it is shot at. " +
			"This electricity can bounce between many adjacent foes, and is more powerful in water. " +
			"If you're too close, you may get shocked as well.";
	}
}
