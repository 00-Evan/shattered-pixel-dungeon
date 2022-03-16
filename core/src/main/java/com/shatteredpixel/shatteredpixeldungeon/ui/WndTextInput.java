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

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.TextInput;
import com.watabou.utils.DeviceCompat;

public class WndTextInput extends Window {

	private static final int WIDTH = 120;
	private static final int W_LAND_MULTI = 200; //in the specific case of multiline in landscape
	private static final int MARGIN = 2;
	private static final int BUTTON_HEIGHT = 16;

	private TextInput textBox;

	public WndTextInput(final String title, final String initialValue, final int maxLength,
	                           final boolean multiLine, final String posTxt, final String negTxt) {
		super();

		//need to offset to give space for the soft keyboard
		if (!DeviceCompat.isDesktop()) {
			if (PixelScene.landscape()) {
				offset(0, -45);
			} else {
				offset(0, multiLine ? -60 : -45);
			}
		}

		final int width;
		if (PixelScene.landscape() && multiLine) {
			width = W_LAND_MULTI; //more editing space for landscape users
		} else {
			width = WIDTH;
		}

		final RenderedTextBlock txtTitle = PixelScene.renderTextBlock(title, 9);
		txtTitle.maxWidth(width);
		txtTitle.hardlight(Window.TITLE_COLOR);
		txtTitle.setPos((width - txtTitle.width()) / 2, 2);
		add(txtTitle);

		int textSize = (int)PixelScene.uiCamera.zoom * (multiLine ? 6 : 9);
		textBox = new TextInput(Chrome.get(Chrome.Type.TOAST_WHITE), multiLine, textSize){
			@Override
			public void enterPressed() {
				//triggers positive action on enter pressed, only with non-multiline though.
				onSelect(true, getText());
				hide();
			}
		};
		if (initialValue != null) textBox.setText(initialValue);
		textBox.setMaxLength(maxLength);

		float pos = txtTitle.bottom() + 2 * MARGIN;

		//sets different height depending on whether this is a single or multi line input.
		final float inputHeight;
		if (multiLine) {
			inputHeight = 64; //~8 lines of text
		} else {
			inputHeight = 16;
		}
		add(textBox);
		textBox.setRect(MARGIN, pos, width-2*MARGIN, inputHeight);

		pos += inputHeight + MARGIN;

		final RedButton positiveBtn = new RedButton(posTxt) {
			@Override
			protected void onClick() {
				onSelect(true, textBox.getText());
				hide();
			}
		};

		final RedButton negativeBtn;
		if (negTxt != null) {
			negativeBtn = new RedButton(negTxt) {
				@Override
				protected void onClick() {
					onSelect(false, textBox.getText());
					hide();
				}
			};
		} else {
			negativeBtn = null;
		}

		if (negTxt != null) {
			positiveBtn.setRect(MARGIN, pos, (width - MARGIN * 3) / 2, BUTTON_HEIGHT);
			add(positiveBtn);
			negativeBtn.setRect(positiveBtn.right() + MARGIN, pos, (width - MARGIN * 3) / 2, BUTTON_HEIGHT);
			add(negativeBtn);
		} else {
			positiveBtn.setRect(MARGIN, pos, width - MARGIN * 2, BUTTON_HEIGHT);
			add(positiveBtn);
		}

		pos += BUTTON_HEIGHT + MARGIN;

		//need to resize first before laying out the text box, as it depends on the window's camera
		resize(width, (int) pos);

		textBox.setRect(MARGIN, textBox.top(), width-2*MARGIN, inputHeight);

	}

	@Override
	public void offset(int xOffset, int yOffset) {
		super.offset(xOffset, yOffset);
		if (textBox != null){
			textBox.setRect(textBox.left(), textBox.top(), textBox.width(), textBox.height());
		}
	}

	public void onSelect(boolean positive, String text){ }

	@Override
	public void onBackPressed() {
		//Do nothing, prevents accidentally losing writing
	}
}
