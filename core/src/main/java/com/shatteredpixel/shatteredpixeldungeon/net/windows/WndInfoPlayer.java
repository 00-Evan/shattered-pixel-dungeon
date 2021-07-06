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

package com.shatteredpixel.shatteredpixeldungeon.net.windows;

import com.shatteredpixel.shatteredpixeldungeon.net.actor.Player;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class WndInfoPlayer extends WndTitledMessage {

	public WndInfoPlayer(Player player ) {
		super( new PlayerTitle( player ), player.info() );
	}
	
	private static class PlayerTitle extends Component {

		private static final int GAP = 2;
		
		private Image image;
		private RenderedTextBlock name;

		public PlayerTitle( Player player ) {
			
			name = PixelScene.renderTextBlock( player.nick(), 9 );
			add( name );
			
			image = HeroSprite.avatar(WndPlayerList.playerClassToHeroClass(player.playerClass()), 0);
			add( image );
		}
		
		@Override
		protected void layout() {
			
			image.x = 0;
			image.y = Math.max( 0, name.height() - image.height() );

			name.setPos(x + image.width + GAP,
					image.height() > name.height() ? y +(image.height() - name.height()) / 2 : y);

			height = name.bottom();
		}
	}
}
