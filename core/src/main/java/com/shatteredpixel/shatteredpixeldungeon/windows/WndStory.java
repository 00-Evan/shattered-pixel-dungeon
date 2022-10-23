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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.SparseArray;

public class WndStory extends Window {

	private static final int WIDTH_P = 125;
	private static final int WIDTH_L = 180;
	private static final int MARGIN = 2;

	private IconTitle ttl;
	private RenderedTextBlock tf;
	
	private float appearDelay;
	private float disappearDelay;

	public WndStory( String text ) {
		this( null, null, text );
	}
	
	public WndStory(Image icon, String title, String text ) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );

		int width = PixelScene.landscape() ? WIDTH_L - MARGIN * 2: WIDTH_P - MARGIN *2;

		float y = MARGIN;
		if (icon != null && title != null){
			ttl = new IconTitle(icon, title);
			ttl.setRect(MARGIN, y, width-2*MARGIN, 0);
			y = ttl.bottom()+MARGIN;
			add(ttl);
			ttl.tfLabel.invert();
		}
		
		tf = PixelScene.renderTextBlock( text, 6 );
		tf.maxWidth(width);
		tf.invert();
		tf.setPos(MARGIN, y);
		add( tf );

		PointerArea blocker = new PointerArea( 0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height ) {
			@Override
			protected void onClick( PointerEvent event ) {
				onBackPressed();
			}
		};
		blocker.camera = PixelScene.uiCamera;
		add(blocker);
		
		resize( width + 2*MARGIN, (int)(tf.bottom()+MARGIN) );
	}

	public WndStory setDelays(float appearDelay, float disappearDelay){
		this.appearDelay = appearDelay;
		if (appearDelay > 0){
			shadow.visible = chrome.visible = tf.visible = false;
			if (ttl != null) ttl.visible = false;
		}

		this.disappearDelay = disappearDelay;
		return this;
	}

	@Override
	public void onBackPressed() {
		if (appearDelay <= 0 && disappearDelay <= 0) {
			super.onBackPressed();
		}
	}

	@Override
	public void update() {
		super.update();
		
		if (appearDelay > 0) {
			appearDelay -= Game.elapsed;
			if (appearDelay <= 0) {
				shadow.visible = chrome.visible = tf.visible = true;
				if (ttl != null) ttl.visible = true;
			}
		} else if (disappearDelay > 0){
			disappearDelay -= Game.elapsed;
		}
	}
}
