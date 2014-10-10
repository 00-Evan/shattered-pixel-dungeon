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
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStory;

public class IntroScene extends PixelScene {

	private static final String TEXT =
		"Ever since the fall of the Dwarven Metropolis below, this place has been more of a dungeon than a trade route. " +
        "Many heroes of all kinds have ventured into the dungeon before you from the human city above. Some of them " +
        "have returned with treasures and magical artifacts, most have never been heard from again.\n\n" +
        "None, however, have dared to venture all the way down and retrieved the Amulet of Yendor, " +
        "which is said to be hidden in the depths. Others speak of a mad dwarvern king who’s desire for power " +
        "awoke an ancient evil. Even now dark energy radiates from the dungeon, slowly making its way up into the city.\n\n" +
        "You’re not sure how true the stories are, but you consider yourself ready for the challenge. Most importantly, " +
        "you feel that fortune smiles upon you. It’s time to start your own adventure in Pixel Dungeon!";
	
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
