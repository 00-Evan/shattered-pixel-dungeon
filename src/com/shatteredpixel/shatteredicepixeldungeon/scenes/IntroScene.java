/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.scenes;

import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredicepixeldungeon.windows.WndStory;

public class IntroScene extends PixelScene {

	private static final String TEXT =
        "Many heroes have ventured into the dungeon before you from the city above. Some " +
        "have returned with treasures and magical artifacts, most have never been heard from again.\n\n" +
        "None, however, have ventured to the bottom and retrieved the Amulet of Yendor, " +
        "which is said to be guarded by an ancient evil in the depths. " +
        "Even now dark energy radiates from below, making its way up into the city.\n\n" +
        "You consider yourself ready for the challenge. Most importantly, " +
        "you feel that fortune smiles upon you. It's time to start your own adventure!";
	
	@Override
	public void create() {
		super.create();
		
		add( new WndStory( TEXT ) {
			@Override
			public void hide() {
				super.hide();
				Game.switchScene( InterlevelScene.class );
			}
		} );
		
		fadeIn();
	}
}
