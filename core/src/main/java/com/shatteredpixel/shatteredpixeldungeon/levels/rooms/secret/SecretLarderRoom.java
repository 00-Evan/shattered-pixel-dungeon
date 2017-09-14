/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SecretLarderRoom extends SecretRoom {
	
	@Override
	public boolean canConnect(Point p) {
		//refuses connections from the top
		return super.canConnect(p) && p.y > top+1;
	}
	
	@Override
	public int maxWidth() {
		return 8;
	}
	
	@Override
	public int maxHeight() {
		return 8;
	}
	
	@Override
	public void paint(Level level) {
		super.paint(level);
		
		Painter.fill(level, this, 1, Terrain.EMPTY_SP);
		
		int nFood = width()-2;
		ArrayList<Item> foodItems = new ArrayList<>();
		
		//generates 900 food value, spread out over 3-6 pieces of food
		if (nFood <= 4){
			foodItems.add(Random.Int(2) == 0 ? new Blandfruit() : new Pasty());
			nFood--;
		}
		
		if (nFood == 3 || nFood == 5){
			foodItems.add(new Food());
			nFood--;
		}
		
		while (nFood > 0){
			foodItems.add(Random.Int(2) == 0 ? new ChargrilledMeat() : new SmallRation());
			nFood--;
		}
		
		Random.shuffle(foodItems);
		
		int topLeft = level.pointToCell(new Point(left+1, top+1));
		for(int i = 0; i < foodItems.size(); i++){
			level.drop( foodItems.get(i), topLeft+i );
			Painter.set(level, topLeft+i, Terrain.PEDESTAL);
		}
		
		entrance().set(Door.Type.HIDDEN);
	}
	
	
}
