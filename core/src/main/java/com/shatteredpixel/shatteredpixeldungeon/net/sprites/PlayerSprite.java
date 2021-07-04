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

package com.shatteredpixel.shatteredpixeldungeon.net.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.net.actor.Player;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;

public class PlayerSprite extends MobSprite {

	private static final int FRAME_WIDTH	= 12;
	private static final int FRAME_HEIGHT	= 15;
	private static final int RUN_FRAMERATE	= 20;

	public PlayerSprite() {
		super();
		updatePlayerSprite(0);
	}

	public void updatePlayerSprite(int pc){
		texture( getPlayerSpriteSheet(pc) );
		TextureFilm ps = new TextureFilm( texture, texture.width, FRAME_HEIGHT );

		TextureFilm film = new TextureFilm( ps, 1, FRAME_WIDTH, FRAME_HEIGHT );

		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );

		run = new Animation( RUN_FRAMERATE, true );
		run.frames( film, 2, 3, 4, 5, 6, 7 );

		die = new Animation( 20, false );
		die.frames( film, 8, 9, 10, 11, 12, 11 );

		attack = new Animation( 15, false );
		attack.frames( film, 13, 14, 15, 0 );

		play( idle );
	}

	public static String getPlayerSpriteSheet(int playerClass){
		switch (playerClass) {
			case 0:
				return Assets.Sprites.WARRIOR;
			case 1:
				return Assets.Sprites.MAGE;
			case 2:
				return Assets.Sprites.ROGUE;
			case 3:
				return Assets.Sprites.HUNTRESS;
		}
		return Assets.Sprites.ROGUE;
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		Player p = (Player)ch;
		updatePlayerSprite(p.playerClass());
	}
}
