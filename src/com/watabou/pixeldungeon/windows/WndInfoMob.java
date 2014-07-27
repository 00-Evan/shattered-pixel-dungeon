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
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.Utils;

public class WndInfoMob extends WndTitledMessage {
	
	private static final String TXT_SLEEPNIG	= "\n\nThis %s is sleeping.";
	private static final String TXT_HUNTING		= "\n\nThis %s is hunting.";
	private static final String TXT_WANDERING	= "\n\nThis %s is wandering.";
	private static final String TXT_FLEEING		= "\n\nThis %s is fleeing.";
	private static final String TXT_PASSIVE		= "\n\nThe %s is passive.";
	
	public WndInfoMob( Mob mob ) {
		
		super( new MobTitle( mob ), desc( mob ) );
		
	}
	
	private static String desc( Mob mob ) {
		
		StringBuilder builder = new StringBuilder( mob.description() );
		
		switch (mob.state) {
		case SLEEPING:
			builder.append( String.format( TXT_SLEEPNIG, mob.name ) );
			break;
		case HUNTING:
			builder.append( String.format( TXT_HUNTING, mob.name ) );
			break;
		case WANDERING:
			builder.append( String.format( TXT_WANDERING, mob.name ) );
			break;
		case FLEEING:
			builder.append( String.format( TXT_FLEEING, mob.name ) );
			break;
		case PASSIVE:
			builder.append( String.format( TXT_PASSIVE, mob.name ) );
			break;
		}
		
		return builder.toString();
	}
	
	private static class MobTitle extends Component {
		
		private static final int COLOR_BG	= 0xFFCC0000;
		private static final int COLOR_LVL	= 0xFF00EE00;
		
		private static final int BAR_HEIGHT	= 2;
		private static final int GAP	= 2;
		
		private CharSprite image;
		private BitmapText name;
		private ColorBlock hpBg;
		private ColorBlock hpLvl;
		private BuffIndicator buffs;
		
		private float hp;
		
		public MobTitle( Mob mob ) {
			
			hp = (float)mob.HP / mob.HT;
			
			name = PixelScene.createText( Utils.capitalize( mob.name ), 9 );
			name.hardlight( TITLE_COLOR );
			name.measure();	
			add( name );
			
			image = mob.sprite();
			add( image );
			
			hpBg = new ColorBlock( 1, 1, COLOR_BG );
			add( hpBg );
			
			hpLvl = new ColorBlock( 1, 1, COLOR_LVL );
			add( hpLvl );
			
			buffs = new BuffIndicator( mob );
			add( buffs );
		}
		
		@Override
		protected void layout() {
			
			image.x = 0;
			image.y = Math.max( 0, name.height() + GAP + BAR_HEIGHT - image.height );
			
			name.x = image.width + GAP;
			name.y = image.height - BAR_HEIGHT - GAP - name.baseLine();
			
			float w = width - image.width - GAP;
			
			hpBg.size( w, BAR_HEIGHT );
			hpLvl.size( w * hp, BAR_HEIGHT );
			
			hpBg.x = hpLvl.x = image.width + GAP;
			hpBg.y = hpLvl.y = image.height - BAR_HEIGHT;
			
			buffs.setPos( 
				name.x + name.width() + GAP, 
				name.y + name.baseLine() - BuffIndicator.SIZE );
			
			height = hpBg.y + hpBg.height();
		}
	}
}
