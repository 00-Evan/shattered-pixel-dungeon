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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.effects.CircleArc;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Cursor;
import com.watabou.utils.PointF;

public class RadialMenu extends Window {

	int slots;
	float targetAngle;
	PointF center;

	CircleArc selectionArc;
	RenderedTextBlock titleTxt;
	RenderedTextBlock descTxt;

	String[] texts;
	Image[] icons;
	int selectedIdx = -1;

	public RadialMenu(String title, String desc, String[] optionTexts, Image[] optionIcons){
		super(0, 0, Chrome.get(Chrome.Type.BLANK));
		remove(shadow);

		int size = SPDSettings.interfaceSize() == 0 ? 140 : 200;
		resize(Game.width, Game.height);

		slots = optionTexts.length;
		center = new PointF(width/2, height/2);
		int length = SPDSettings.interfaceSize() == 0 ? 57 : 80;

		selectionArc = new CircleArc(120/slots, size/2 - 1);
		selectionArc.color(0xFFFFFF, false);
		selectionArc.alpha(0.6f);
		selectionArc.setSweep(1f/slots);
		selectionArc.point(center);
		selectionArc.visible = false;
		add(selectionArc);

		Image outerBG = getBGTexture(size, false);
		outerBG.x = (width - outerBG.width) / 2;
		outerBG.y = (height - outerBG.height) / 2;
		PixelScene.align(outerBG);
		add(outerBG);

		texts = optionTexts;
		icons = optionIcons;

		for (int i = 0; i < slots; i++){

			PointF iconCenter = new PointF().polar((PointF.PI2/slots * i)-PointF.PI/2, length);
			iconCenter.offset(center);
			optionIcons[i].x = iconCenter.x - optionIcons[i].width()/2f;
			optionIcons[i].y = iconCenter.y - optionIcons[i].height()/2f;
			PixelScene.align(optionIcons[i]);
			optionIcons[i].alpha(0.4f);
			add(optionIcons[i]);

			ColorBlock sep = new ColorBlock(size/2 - 2, 1, 0xFF000000);
			sep.x = center.x;
			sep.y = center.y;
			sep.angle = (360f/slots * i) + selectionArc.getSweep()*180 - 90;
			addToFront(sep);

		}

		Image innerBG = getBGTexture(size, true);
		innerBG.x = (width - innerBG.width) / 2;
		innerBG.y = (height - innerBG.height) / 2;
		PixelScene.align(innerBG);
		add(innerBG);

		descTxt = PixelScene.renderTextBlock(desc, 6);
		descTxt.align(RenderedTextBlock.CENTER_ALIGN);
		descTxt.maxWidth(SPDSettings.interfaceSize() == 0 ? 80 : 100);
		descTxt.setPos(center.x - descTxt.width()/2, center.y - descTxt.height()/4);
		add(descTxt);

		titleTxt = PixelScene.renderTextBlock(title, 9);
		titleTxt.setPos(center.x - titleTxt.width()/2f, descTxt.top() - titleTxt.height() - 6);
		PixelScene.align(titleTxt);
		titleTxt.hardlight(TITLE_COLOR);
		add(titleTxt);

		Cursor.captureCursor(true);

		Button selector = new Button(){
			@Override
			protected void onClick() {
				super.onClick();
				if (selectedIdx != -1){
					hide();
					onSelect(selectedIdx, false);
				}
			}

			@Override
			protected boolean onLongClick() {
				if (selectedIdx != -1){
					hide();
					onSelect(selectedIdx, true);
				}
				return true;
			}

			@Override
			protected void onRightClick() {
				super.onRightClick();
				if (selectedIdx != -1){
					hide();
					onSelect(selectedIdx, true);
				}
			}
		};
		selector.setRect(0, 0, width, height);
		add(selector);

	}

	public void onSelect(int idx, boolean alt){}

	@Override
	public void destroy() {
		super.destroy();
		Cursor.captureCursor(false);
	}

	//the mouse has a hidden position in a 20-pixel radius circle, helps make selection feel more natural
	private PointF mousePos = new PointF();
	private boolean first = true; //ignore the first mouse input as it's caused by hiding mouse

	@Override
	public synchronized void update() {
		super.update();

		PointF movement = Cursor.getCursorDelta();
		//40% deadzone for controller input here
		if (Math.abs(ControllerHandler.rightStickPosition.x) >= 0.4f
				|| Math.abs(ControllerHandler.rightStickPosition.y) >= 0.4f){
			movement.x = ControllerHandler.rightStickPosition.x;
			movement.y = ControllerHandler.rightStickPosition.y;
			first = false;
		} else if (movement.length() != 0 && !first) {
			mousePos.offset(movement);
			if (mousePos.length() > PixelScene.defaultZoom*20) {
				mousePos.invScale(mousePos.length() / (PixelScene.defaultZoom*20));
			}
			movement = mousePos;
		}

		if (movement.length() != 0) {
			if (first){
				first = false;
				return;
			}

			targetAngle = PointF.angle(movement.x, movement.y) / PointF.G2R + 90;
			if (targetAngle < 0) targetAngle += 360f;

			selectionArc.visible = true;
			selectionArc.angle = targetAngle + selectionArc.getSweep()*180;

			int newSelect = (int) Math.round((targetAngle) / (360f/slots));
			if (newSelect >= slots) newSelect = 0;

			if (newSelect != selectedIdx) {
				selectedIdx = newSelect;
				for (int i = 0; i < slots; i++) {
					if (i == selectedIdx) {
						icons[i].alpha(1f);
						titleTxt.text(texts[i]);
						titleTxt.setPos(center.x - titleTxt.width()/2f, descTxt.top() - titleTxt.height() - 6);
						PixelScene.align(titleTxt);
					} else {
						icons[i].alpha(0.4f);
					}
				}
			}
		}

	}

	private static Image getBGTexture(int size, boolean inner){
		if (size >= 200){
			if (!inner)  return new Image(Assets.Interfaces.RADIAL_MENU, 0, 0, 200, 200);
			else        return new Image(Assets.Interfaces.RADIAL_MENU, 340, 0, 120, 120);
		} else {
			if (!inner)  return new Image(Assets.Interfaces.RADIAL_MENU, 200, 0, 140, 140);
			else        return new Image(Assets.Interfaces.RADIAL_MENU, 340, 120, 90, 90);
		}
	}

}
