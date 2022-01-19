/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

public class CurrencyIndicator extends Component {

	private static final float TIME	= 2f;
	
	private int lastGold = 0;
	private int lastEnergy = 0;
	
	private BitmapText gold;
	private BitmapText energy;
	
	private float goldTime;
	private float energyTime;
	
	@Override
	protected void createChildren() {
		gold = new BitmapText( PixelScene.pixelFont);
		add( gold );

		energy = new BitmapText( PixelScene.pixelFont);
		add( energy );
		
		gold.visible = energy.visible = false;
	}
	
	@Override
	protected void layout() {
		energy.x = x + (width - energy.width()) / 2;
		energy.y = bottom() - energy.height();

		gold.x = x + (width - gold.width()) / 2;
		if (energy.visible) {
			gold.y = bottom() - gold.height()- gold.height() + 2;
		} else {
			gold.y = bottom() - gold.height();
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		if (gold.visible) {
			
			goldTime -= Game.elapsed;
			if (goldTime > 0) {
				gold.alpha( goldTime > TIME / 2 ? 1f : goldTime * 2 / TIME );
			} else {
				gold.visible = false;
			}
			
		}

		if (energy.visible) {

			energyTime -= Game.elapsed;
			if (energyTime > 0) {
				energy.alpha( energyTime > TIME / 2 ? 1f : energyTime * 2 / TIME );
			} else {
				energy.visible = false;
			}

		}

		if (Dungeon.gold != lastGold) {
			
			lastGold = Dungeon.gold;
			
			gold.text( Integer.toString(lastGold) );
			gold.measure();
			gold.hardlight( 0xFFFF00 );
			
			gold.visible = true;
			goldTime = TIME;
			
			layout();
		}

		if (Dungeon.energy != lastEnergy) {
			lastEnergy = Dungeon.energy;

			energy.text( Integer.toString(lastEnergy) );
			energy.measure();
			energy.hardlight( 0x44CCFF );

			energy.visible = true;
			energyTime = TIME;

			layout();
		}
	}
}
