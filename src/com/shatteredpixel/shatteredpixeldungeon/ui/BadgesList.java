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
package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBadge;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class BadgesList extends ScrollPane {

	private ArrayList<ListItem> items = new ArrayList<ListItem>();
	
	public BadgesList( boolean global ) {
		super( new Component() );
		
		for (Badges.Badge badge : Badges.filtered( global )) {
			
			if (badge.image == -1) {
				continue;
			}
			
			ListItem item = new ListItem( badge );
			content.add( item );
			items.add( item );
		}
	}
	
	@Override
	protected void layout() {
		
		float pos = 0;
		
		int size = items.size();
		for (int i=0; i < size; i++) {
			items.get( i ).setRect( 0, pos, width, ListItem.HEIGHT );
			pos += ListItem.HEIGHT;
		}
		
		content.setSize( width, pos );

		super.layout();
	}
	
	@Override
	public void onClick( float x, float y ) {
		int size = items.size();
		for (int i=0; i < size; i++) {
			if (items.get( i ).onClick( x, y )) {
				break;
			}
		}
	}

	private class ListItem extends Component {
		
		private static final float HEIGHT	= 20;
		
		private Badges.Badge badge;
		
		private Image icon;
		private RenderedText label;
		
		public ListItem( Badges.Badge badge ) {
			super();
			
			this.badge = badge;
			icon.copy( BadgeBanner.image( badge.image ));
			label.text( badge.desc() );
		}
		
		@Override
		protected void createChildren() {
			icon = new Image();
			add( icon );
			
			label = PixelScene.renderText( 6 );
			add( label );
		}
		
		@Override
		protected void layout() {
			icon.x = x;
			icon.y = y + (height - icon.height) / 2;
			PixelScene.align(icon);
			
			label.x = icon.x + icon.width + 2;
			label.y = y + (height - label.baseLine()) / 2;
			PixelScene.align(label);
		}
		
		public boolean onClick( float x, float y ) {
			if (inside( x, y )) {
				Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
				Game.scene().add( new WndBadge( badge ) );
				return true;
			} else {
				return false;
			}
		}
	}
}
