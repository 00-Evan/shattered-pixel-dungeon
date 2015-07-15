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

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;

public class WndDisplay extends Window {

	private static final int WIDTH		    = 112;
	private static final int SLIDER_HEIGHT	= 25;
	private static final int BTN_HEIGHT	    = 20;
	private static final int GAP_SML 		= 2;
	private static final int GAP_LRG 		= 10;

	public WndDisplay() {

		//***Screen Options***

		OptionSlider scale = new OptionSlider("UI Scale",
				(int)Math.ceil(2* Game.density)+ "X",
				PixelScene.maxDefaultZoom + "X",
				(int)Math.ceil(2* Game.density),
				PixelScene.maxDefaultZoom ) {
			@Override
			protected void onChange() {
				ShatteredPixelDungeon.scale(getSelectedValue());
			}
		};
		scale.setSelectedValue(PixelScene.defaultZoom);
		if ((int)Math.ceil(2* Game.density) < PixelScene.maxDefaultZoom) {
			scale.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
			add(scale);
		} else {
			scale.setRect(0, 0, 0, 0);
		}

		CheckBox chkImmersive = new CheckBox( "Hide Software Keys" ) {
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.immerse(checked());
			}
		};
		chkImmersive.setRect( 0, scale.bottom() + GAP_SML, WIDTH, BTN_HEIGHT );
		chkImmersive.checked( ShatteredPixelDungeon.immersed() );
		chkImmersive.enable( android.os.Build.VERSION.SDK_INT >= 19 );
		add(chkImmersive);

		//***Brightness Option***

		OptionSlider brightness = new OptionSlider("Brightness", "Dark", "Bright", -2, 4) {
			@Override
			protected void onChange() {
				ShatteredPixelDungeon.brightness(getSelectedValue());
			}
		};
		brightness.setSelectedValue(ShatteredPixelDungeon.brightness());
		brightness.setRect(0, chkImmersive.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
		add(brightness);

		//***Interface Options***


		BitmapText barDesc = PixelScene.createText("Toolbar Mode:", 9);
		barDesc.measure();
		barDesc.x = (WIDTH-barDesc.width())/2;
		barDesc.y = brightness.bottom() + GAP_LRG;
		add(barDesc);

		RedButton btnSplit = new RedButton("Split"){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.SPLIT.name());
				Toolbar.updateLayout();
			}
		};
		btnSplit.setRect( 1, barDesc.y + barDesc.height(), 36, BTN_HEIGHT);
		add(btnSplit);

		RedButton btnGrouped = new RedButton("Group"){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.GROUP.name());
				Toolbar.updateLayout();
			}
		};
		btnGrouped.setRect( btnSplit.right()+1, barDesc.y + barDesc.height(), 36, BTN_HEIGHT);
		add(btnGrouped);

		RedButton btnCentered = new RedButton("Center"){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.CENTER.name());
				Toolbar.updateLayout();
			}
		};
		btnCentered.setRect(btnGrouped.right()+1, barDesc.y + barDesc.height(), 36, BTN_HEIGHT);
		add(btnCentered);

		OptionSlider slots = new OptionSlider("Quickslots", "0", "4", 0, 4) {
			@Override
			protected void onChange() {
				ShatteredPixelDungeon.quickSlots( getSelectedValue() );
				Toolbar.updateLayout();
			}
		};
		slots.setSelectedValue(ShatteredPixelDungeon.quickSlots());
		slots.setRect(0, btnGrouped.bottom() + GAP_SML, WIDTH, SLIDER_HEIGHT);
		add(slots);

		CheckBox chkFlip = new CheckBox("Flip Toolbar"){
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.flippedUI(checked());
				Toolbar.updateLayout();
			}
		};
		chkFlip.setRect(0, slots.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
		chkFlip.checked(ShatteredPixelDungeon.flippedUI());
		add(chkFlip);

		resize(WIDTH, (int) chkFlip.bottom());

	}
}
