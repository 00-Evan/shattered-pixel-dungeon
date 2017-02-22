/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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
import com.watabou.input.Touchscreen;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;

public abstract class OptionSlider extends Component {

	private TouchArea touchArea;

	private RenderedText title;
	private RenderedText minTxt;
	private RenderedText maxTxt;

	//values are expressed internally as ints, but they can easily be interpreted as something else externally.
	private int minVal;
	private int maxVal;
	private int selectedVal;

	private NinePatch sliderNode;
	private NinePatch BG;
	private ColorBlock sliderBG;
	private ColorBlock[] sliderTicks;
	private float tickDist;


	public OptionSlider(String title, String minTxt, String maxTxt, int minVal, int maxVal){
		super();

		//shouldn't function if this happens.
		if (minVal > maxVal){
			minVal = maxVal;
			active = false;
		}

		this.title.text(title);
		this.minTxt.text(minTxt);
		this.maxTxt.text(maxTxt);

		this.minVal = minVal;
		this.maxVal = maxVal;

		sliderTicks = new ColorBlock[(maxVal - minVal) + 1];
		for (int i = 0; i < sliderTicks.length; i++){
			add(sliderTicks[i] = new ColorBlock(1, 11, 0xFF222222));
		}
		add(sliderNode);
	}

	protected abstract void onChange();

	public int getSelectedValue(){
		return selectedVal;
	}

	public void setSelectedValue(int val) {
		this.selectedVal = val;
		sliderNode.x = (int)(x + tickDist*(selectedVal-minVal));
		sliderNode.y = sliderBG.y-4;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		add( BG = Chrome.get(Chrome.Type.BUTTON));
		BG.alpha(0.5f);

		add(title = PixelScene.renderText(9));
		add(this.minTxt = PixelScene.renderText(6));
		add(this.maxTxt = PixelScene.renderText(6));

		add(sliderBG = new ColorBlock(1, 1, 0xFF222222));
		sliderNode = Chrome.get(Chrome.Type.BUTTON);
		sliderNode.size(5, 9);

		touchArea = new TouchArea(0, 0, 0, 0){
			boolean pressed = false;

			@Override
			protected void onTouchDown(Touchscreen.Touch touch) {
				pressed = true;
				PointF p = camera().screenToCamera((int) touch.current.x, (int) touch.current.y);
				sliderNode.x = GameMath.gate(sliderBG.x-2, p.x, sliderBG.x+sliderBG.width()-2);
				sliderNode.brightness(1.5f);
			}

			@Override
			protected void onDrag(Touchscreen.Touch touch) {
				if (pressed) {
					PointF p = camera().screenToCamera((int) touch.current.x, (int) touch.current.y);
					sliderNode.x = GameMath.gate(sliderBG.x - 2, p.x, sliderBG.x + sliderBG.width() - 2);
				}
			}

			@Override
			protected void onTouchUp(Touchscreen.Touch touch) {
				if (pressed) {
					PointF p = camera().screenToCamera((int) touch.current.x, (int) touch.current.y);
					sliderNode.x = GameMath.gate(sliderBG.x - 2, p.x, sliderBG.x + sliderBG.width() - 2);
					sliderNode.resetColor();

					//sets the selected value
					selectedVal = minVal + Math.round(sliderNode.x / tickDist);
					sliderNode.x = (int) (x + tickDist * (selectedVal - minVal));
					onChange();
					pressed = false;
				}
			}
		};
		add(touchArea);

	}

	@Override
	protected void layout() {
		title.x = x + (width-title.width())/2;
		title.y = y+2;
		PixelScene.align(title);
		sliderBG.y = y + height() - 8;
		sliderBG.x = x+2;
		sliderBG.size(width-5, 1);
		tickDist = sliderBG.width()/(maxVal - minVal);
		for (int i = 0; i < sliderTicks.length; i++){
			sliderTicks[i].y = sliderBG.y-5;
			sliderTicks[i].x = (int)(x + 2 + (tickDist*i));
		}

		minTxt.y = maxTxt.y = sliderBG.y-6-minTxt.baseLine();
		minTxt.x = x+1;
		maxTxt.x = x+width()-maxTxt.width()-1;


		sliderNode.x = (int)(x + tickDist*(selectedVal-minVal));
		sliderNode.y = sliderBG.y-4;

		touchArea.x = x;
		touchArea.y = y;
		touchArea.width = width();
		touchArea.height = height();

		BG.size(width(), height());
		BG.x = x;
		BG.y = y;

	}
}
