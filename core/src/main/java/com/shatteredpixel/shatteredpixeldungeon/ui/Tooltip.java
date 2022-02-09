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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;

public class Tooltip extends Component {

	//tooltips require .5 seconds to appear, fade in over .1 second
	//they then persist until none are visible for .25 seconds or more
	private static float tooltipAlpha = -5f;
	private static float lastUsedTime = -1;

	public static void resetLastUsedTime(){
		lastUsedTime = -1;
		tooltipAlpha = -5;
	}

	private NinePatch bg;
	private RenderedTextBlock text;

	public Tooltip(String msg, int maxWidth){
		super();
		text.text(msg, maxWidth);
		layout();

		if (lastUsedTime == -1 || lastUsedTime > Game.timeTotal){
			tooltipAlpha = -5f;

		} else {
			float elapsed = Game.timeTotal - lastUsedTime;
			if (elapsed >= 0.25f || tooltipAlpha < 1f){
				tooltipAlpha = -5f;
			}
		}
		lastUsedTime = Game.timeTotal;
		bg.alpha(GameMath.gate(0, tooltipAlpha, 1));
		text.alpha(GameMath.gate(0, tooltipAlpha, 1));
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		bg = Chrome.get(Chrome.Type.TOAST_TR);
		add(bg);

		text = PixelScene.renderTextBlock(6);
		add(text);
	}

	@Override
	public synchronized void update() {
		super.update();
		tooltipAlpha = Math.min(1f, tooltipAlpha + 10f*Game.elapsed);
		lastUsedTime = Game.timeTotal;

		bg.alpha(GameMath.gate(0, tooltipAlpha, 1));
		text.alpha(GameMath.gate(0, tooltipAlpha, 1));
	}

	@Override
	protected void layout() {

		text.setPos(x + bg.marginLeft(), y + bg.marginTop());
		bg.x = x;
		bg.y = y;
		bg.size(text.width()+bg.marginHor(), text.height()+bg.marginVer());

		width = bg.width;
		height = bg.height;

	}
}
