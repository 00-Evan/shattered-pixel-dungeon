/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndLangs;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class LanguageButton extends Button {

	private Image image;

	@Override
	protected void createChildren() {
		super.createChildren();

		image = Icons.get(Icons.LANGS);
		add( image );
		updateIcon();
	}
	
	private boolean flashing;
	private float time = 0;
	
	@Override
	public void update() {
		super.update();
		
		if (flashing){
			image.am = (float)Math.abs(Math.cos( (time += Game.elapsed) ));
			if (time >= Math.PI) {
				time = 0;
			}
		}
		
	}
	
	private void updateIcon(){
		image.resetColor();
		flashing = false;
		switch(Messages.lang().status()){
			case INCOMPLETE:
				image.tint(1, 0, 0, .5f);
				flashing = true;
				break;
			case UNREVIEWED:
				image.tint(1, .5f, 0, .5f);
				break;
		}
	}

	@Override
	protected void layout() {
		super.layout();

		image.x = x + (width - image.width)/2f;
		image.y = y + (height - image.height)/2f;
		PixelScene.align(image);
	}

	@Override
	protected void onTouchDown() {
		image.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.SND_CLICK );
	}

	@Override
	protected void onTouchUp() {
		image.resetColor();
		updateIcon();
	}

	@Override
	protected void onClick() {
		parent.add(new WndLangs());
	}

}
