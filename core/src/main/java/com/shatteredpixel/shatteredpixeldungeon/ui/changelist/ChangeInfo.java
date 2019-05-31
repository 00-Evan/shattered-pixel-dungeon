/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class ChangeInfo extends Component {
	
	protected ColorBlock line;
	
	private RenderedText title;
	public boolean major;
	
	private RenderedTextMultiline text;
	
	private ArrayList<ChangeButton> buttons = new ArrayList<>();
	
	public ChangeInfo( String title, boolean majorTitle, String text){
		super();
		
		if (majorTitle){
			this.title = PixelScene.renderText( title, 9 );
			line = new ColorBlock( 1, 1, 0xFF222222);
			add(line);
		} else {
			this.title = PixelScene.renderText( title, 6 );
			line = new ColorBlock( 1, 1, 0xFF333333);
			add(line);
		}
		major = majorTitle;
		
		add(this.title);
		
		if (text != null && !text.equals("")){
			this.text = PixelScene.renderMultiline(text, 6);
			add(this.text);
		}
		
	}
	
	public void hardlight( int color ){
		title.hardlight( color );
	}
	
	public void addButton( ChangeButton button ){
		buttons.add(button);
		add(button);
		
		button.setSize(16, 16);
		layout();
	}
	
	public boolean onClick( float x, float y ){
		for( ChangeButton button : buttons){
			if (button.inside(x, y)){
				button.onClick();
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void layout() {
		float posY = this.y + 2;
		if (major) posY += 2;
		
		title.x = x + (width - title.width()) / 2f;
		title.y = posY;
		PixelScene.align( title );
		posY += title.baseLine() + 2;
		
		if (text != null) {
			text.maxWidth((int) width());
			text.setPos(x, posY);
			posY += text.height();
		}
		
		float posX = x;
		float tallest = 0;
		for (ChangeButton change : buttons){
			
			if (posX + change.width() >= right()){
				posX = x;
				posY += tallest;
				tallest = 0;
			}
			
			//centers
			if (posX == x){
				float offset = width;
				for (ChangeButton b : buttons){
					offset -= b.width();
					if (offset <= 0){
						offset += b.width();
						break;
					}
				}
				posX += offset / 2f;
			}
			
			change.setPos(posX, posY);
			posX += change.width();
			if (tallest < change.height()){
				tallest = change.height();
			}
		}
		posY += tallest + 2;
		
		height = posY - this.y;
		
		if (major) {
			line.size(width(), 1);
			line.x = x;
			line.y = y+2;
		} else if (x == 0){
			line.size(1, height());
			line.x = width;
			line.y = y;
		} else {
			line.size(1, height());
			line.x = x;
			line.y = y;
		}
	}
}