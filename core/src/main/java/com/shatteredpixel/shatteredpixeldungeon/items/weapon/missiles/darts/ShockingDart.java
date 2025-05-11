/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShockingDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.SHOCKING_DART;
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {

		//when processing charged shot, only shock enemies
		if (!processingChargedShot || attacker.alignment != defender.alignment) {

			//3x3 AoE dmg + mini-stun
			ArrayList<Lightning.Arc> arcs = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS9) {
				Char ch = Actor.findChar(defender.pos + i);

				if(ch != null) {
					ch.damage(Random.NormalIntRange(5 + Dungeon.scalingDepth() / 4, 10 + Dungeon.scalingDepth() / 4), new Electricity());

					//<1 turn stun
					Buff.Polished.affectAligned(ch, Paralysis.class, 0.75f);
					arcs.add(new Lightning.Arc(defender.sprite.center(), ch.sprite.center()));
				}
			}

			CharSprite s = defender.sprite;
			if (s != null && s.parent != null) {
				arcs.add(new Lightning.Arc(new PointF(s.x, s.y + s.height / 2), new PointF(s.x + s.width, s.y + s.height / 2)));
				arcs.add(new Lightning.Arc(new PointF(s.x + s.width / 2, s.y), new PointF(s.x + s.width / 2, s.y + s.height)));

				s.parent.add(new Lightning(arcs, null));
				Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
			}
		}
		
		return super.proc(attacker, defender, damage);
	}
}
