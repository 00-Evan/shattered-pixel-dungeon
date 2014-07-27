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
package com.watabou.pixeldungeon.ui;

import java.util.regex.Pattern;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Signal;

public class GameLog extends Component implements Signal.Listener<String> {

	private static final int MAX_MESSAGES = 3;
	
	private static final Pattern PUNCTUATION = Pattern.compile( ".*[.,;?! ]$" );
	
	private BitmapTextMultiline lastEntry;
	private int lastColor;
	
	public GameLog() {
		super();
		GLog.update.add( this );
		
		newLine();
	}
	
	public void newLine() {
		lastEntry = null;
	}

	@Override
	public void onSignal( String text ) {

		int color = CharSprite.DEFAULT;
		if (text.startsWith( GLog.POSITIVE )) {
			text = text.substring( GLog.POSITIVE.length() );
			color = CharSprite.POSITIVE;
		} else 
		if (text.startsWith( GLog.NEGATIVE )) {
			text = text.substring( GLog.NEGATIVE.length() );
			color = CharSprite.NEGATIVE;
		} else 
		if (text.startsWith( GLog.WARNING )) {
			text = text.substring( GLog.WARNING.length() );
			color = CharSprite.WARNING;
		} else
		if (text.startsWith( GLog.HIGHLIGHT )) {
			text = text.substring( GLog.HIGHLIGHT.length() );
			color = CharSprite.NEUTRAL;
		}
		
		text = Utils.capitalize( text ) + 
			(PUNCTUATION.matcher( text ).matches() ? "" : ".");
		
		if (lastEntry != null && color == lastColor) {
			
			String lastMessage = lastEntry.text();
			lastEntry.text( lastMessage.length() == 0 ? text : lastMessage + " " + text );
			lastEntry.measure();
			
		} else {
			
			lastEntry = PixelScene.createMultiline( text, 6 );
			lastEntry.maxWidth = (int)width;
			lastEntry.measure();
			lastEntry.hardlight( color );
			lastColor = color;
			add( lastEntry );
			
		}
		
		if (length > MAX_MESSAGES) {
			remove( members.get( 0 ) );
		}
		
		layout();
	}
	
	@Override
	protected void layout() {	
		float pos = y;
		for (int i=length-1; i >= 0; i--) {
			BitmapTextMultiline entry = (BitmapTextMultiline)members.get( i );
			entry.x = x;
			entry.y = pos - entry.height();
			pos -= entry.height();
		}
	}
	
	@Override
	public void destroy() {
		GLog.update.remove( this );
		super.destroy();
	}
}
