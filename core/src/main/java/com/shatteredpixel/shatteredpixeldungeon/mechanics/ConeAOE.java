/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

//a cone made of up several ballisticas scanning in an arc
public class ConeAOE {

	public ArrayList<Ballistica> rays = new ArrayList<>();
	public HashSet<Integer> cells = new HashSet<>();

	public ConeAOE( int from, int to, float degrees ){
		this(from, to, Float.POSITIVE_INFINITY, degrees, Ballistica.STOP_TARGET);
	}

	public ConeAOE( int from, int to, float maxDist, float degrees, int ballisticaParams ){

		//we want to use true coordinates for our trig functions, not game cells
		// so get the center of from and to as points
		PointF fromP = new PointF(Dungeon.level.cellToPoint(from));
		fromP.x += 0.5f;
		fromP.y += 0.5f;
		PointF toP = new PointF(Dungeon.level.cellToPoint(to));
		toP.x += 0.5f;
		toP.y += 0.5f;

		//clamp distance of cone to maxDist (in true distance, not game distance)
		if (PointF.distance(fromP, toP) > maxDist){
			toP = PointF.inter(fromP, toP, maxDist/PointF.distance(fromP, toP) );
		}

		//now we can get the circle's radius. We bump it by 0.5 as we want the cone to reach
		// The edge of the target cell, not the center.
		float circleRadius = PointF.distance(fromP, toP);
		circleRadius += 0.5f;

		//Now we find every unique cell along the outer arc of our cone.
		PointF scan = new PointF();
		Point scanInt = new Point();
		float initalAngle = PointF.angle(fromP, toP)/PointF.G2R;
		//want to preserve order so that our collection of rays is going clockwise
		LinkedHashSet<Integer> targetCells = new LinkedHashSet<>();

		//cast a ray every 0.5 degrees in a clockwise arc, to find cells along the cone's outer arc
		for (float a = initalAngle+degrees/2f; a >= initalAngle-degrees/2f; a-=0.5f){
			scan.polar(a * PointF.G2R, circleRadius);
			scan.offset(fromP);
			scan.x += (fromP.x > scan.x ? +0.5f : -0.5f);
			scan.y += (fromP.y > scan.y ? +0.5f : -0.5f);
			scanInt.set(
					(int)GameMath.gate(0, (int)Math.floor(scan.x), Dungeon.level.width()-1),
					(int)GameMath.gate(0, (int)Math.floor(scan.y), Dungeon.level.height()-1));
			targetCells.add(Dungeon.level.pointToCell(scanInt));
			//if the cone is large enough, also cast rays to cells just inside of the outer arc
			// this helps fill in any holes when casting rays
			if (circleRadius >= 7) {
				scan.polar(a * PointF.G2R, circleRadius - 1);
				scan.offset(fromP);
				scan.x += (fromP.x > scan.x ? +0.5f : -0.5f);
				scan.y += (fromP.y > scan.y ? +0.5f : -0.5f);
				scanInt.set(
						(int)GameMath.gate(0, (int)Math.floor(scan.x), Dungeon.level.width()-1),
						(int)GameMath.gate(0, (int)Math.floor(scan.y), Dungeon.level.height()-1));
				targetCells.add(Dungeon.level.pointToCell(scanInt));
			}
		}

		//cast a ray to each found cell, these make up the cone
		for( int c : targetCells ){
			Ballistica ray = new Ballistica(from, c, ballisticaParams);
			cells.addAll(ray.subPath(1, ray.dist));
			rays.add(ray);
		}

	}

}
