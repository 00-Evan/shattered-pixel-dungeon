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

package com.saqfish.spdnet.items.weapon.missiles.darts;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.effects.Lightning;
import com.saqfish.spdnet.sprites.CharSprite;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShockingDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.SHOCKING_DART;
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		
		defender.damage(Random.NormalIntRange(8, 12), this);
		
		CharSprite s = defender.sprite;
		if (s != null && s.parent != null) {
			ArrayList<Lightning.Arc> arcs = new ArrayList<>();
			arcs.add(new Lightning.Arc(new PointF(s.x, s.y + s.height / 2), new PointF(s.x + s.width, s.y + s.height / 2)));
			arcs.add(new Lightning.Arc(new PointF(s.x + s.width / 2, s.y), new PointF(s.x + s.width / 2, s.y + s.height)));
			s.parent.add(new Lightning(arcs, null));
			Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
		}
		
		return super.proc(attacker, defender, damage);
	}
}
