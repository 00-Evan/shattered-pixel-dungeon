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
package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class WndInfoBuff extends Window {

	private static final float GAP	= 2;

	private static final int WIDTH = 120;

	private SmartTexture icons;
	private TextureFilm film;

	public WndInfoBuff(Buff buff){
		super();

		IconTitle titlebar = new IconTitle();

		icons = TextureCache.get( Assets.BUFFS_LARGE );
		film = new TextureFilm( icons, 16, 16 );

		Image buffIcon = new Image( icons );
		buffIcon.frame( film.get(buff.icon()) );

		titlebar.icon( buffIcon );
		titlebar.label( Utils.capitalize(buff.toString()), Window.TITLE_COLOR );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		BitmapTextMultiline txtInfo = PixelScene.createMultiline(buff.desc(), 6);
		txtInfo.maxWidth = WIDTH;
		txtInfo.measure();
		txtInfo.x = titlebar.left();
		txtInfo.y = titlebar.bottom() + GAP;
		add( txtInfo );

		resize( WIDTH, (int)(txtInfo.y + txtInfo.height()) );
	}
}
