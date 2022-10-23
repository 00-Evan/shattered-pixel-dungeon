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

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.TextInput;
import com.watabou.utils.DeviceCompat;

public class WndTextInput extends Window {

	private static final int WIDTH = 135;
	private static final int W_LAND_EXTRA = 220; //extra width is sometimes used in landscape
	private static final int MARGIN = 1;
	private static final int BUTTON_HEIGHT = 16;

	protected TextInput textBox;

	protected RedButton btnCopy;
	protected RedButton btnPaste;

	public WndTextInput(final String title, final String body, final String initialValue, final int maxLength,
	                           final boolean multiLine, final String posTxt, final String negTxt) {
		super();

		//need to offset to give space for the soft keyboard
		if (PixelScene.landscape()) {
			offset(0, -45);
		} else {
			offset(0, multiLine ? -60 : -45);
		}

		final int width;
		if (PixelScene.landscape() && (multiLine || body != null)) {
			width = W_LAND_EXTRA; //more space for landscape users
		} else {
			width = WIDTH;
		}

		float pos = 2;

		if (title != null) {
			final RenderedTextBlock txtTitle = PixelScene.renderTextBlock(title, 9);
			txtTitle.maxWidth(width);
			txtTitle.hardlight(Window.TITLE_COLOR);
			txtTitle.setPos((width - txtTitle.width()) / 2, 2);
			add(txtTitle);

			pos = txtTitle.bottom() + 4 * MARGIN;
		}

		if (body != null) {
			final RenderedTextBlock txtBody = PixelScene.renderTextBlock(body, 6);
			txtBody.maxWidth(width);
			txtBody.setPos(0, pos);
			add(txtBody);

			pos = txtBody.bottom() + 2 * MARGIN;
		}

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

		//sets different height depending on whether this is a single or multi line input.
		final float inputHeight;
		if (multiLine) {
			inputHeight = 64; //~8 lines of text
		} else {
			inputHeight = 16;
		}

		float textBoxWidth = width-3*MARGIN-BUTTON_HEIGHT;

		add(textBox);
		textBox.setRect(MARGIN, pos, textBoxWidth, inputHeight);

		btnCopy = new RedButton(""){
			@Override
			protected void onPointerDown() {
				super.onPointerDown();
				PointerEvent.clearKeyboardThisPress = false;
			}

			@Override
			protected void onPointerUp() {
				super.onPointerUp();
				PointerEvent.clearKeyboardThisPress = false;
			}

			@Override
			protected void onClick() {
				super.onClick();
				textBox.copyToClipboard();
			}
		};
		btnCopy.icon(Icons.COPY.get());
		add(btnCopy);

		btnPaste = new RedButton(""){
			@Override
			protected void onPointerDown() {
				super.onPointerDown();
				PointerEvent.clearKeyboardThisPress = false;
			}

			@Override
			protected void onPointerUp() {
				super.onPointerUp();
				PointerEvent.clearKeyboardThisPress = false;
			}

			@Override
			protected void onClick() {
				super.onClick();
				textBox.pasteFromClipboard();
			}

		};
		btnPaste.icon(Icons.PASTE.get());
		add(btnPaste);

		btnCopy.setRect(textBoxWidth + 2*MARGIN, pos, BUTTON_HEIGHT, BUTTON_HEIGHT);
		btnPaste.setRect(textBoxWidth + 2*MARGIN, btnCopy.bottom()+MARGIN, BUTTON_HEIGHT, BUTTON_HEIGHT);

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

		float btnWidth = multiLine ? width-2*MARGIN : textBoxWidth;
		if (negTxt != null) {
			positiveBtn.setRect(MARGIN, pos, (btnWidth - MARGIN) / 2, BUTTON_HEIGHT);
			add(positiveBtn);
			negativeBtn.setRect(positiveBtn.right() + MARGIN, pos, (btnWidth - MARGIN) / 2, BUTTON_HEIGHT);
			add(negativeBtn);
		} else {
			positiveBtn.setRect(MARGIN, pos, btnWidth, BUTTON_HEIGHT);
			add(positiveBtn);
		}

		pos += BUTTON_HEIGHT;

		//need to resize first before laying out the text box, as it depends on the window's camera
		resize(width, (int) pos);

		textBox.setRect(MARGIN, textBox.top(), textBoxWidth, inputHeight);

		PointerEvent.clearKeyboardThisPress = false;

	}

	@Override
	public synchronized void update() {
		super.update();
		btnCopy.enable(!textBox.getText().isEmpty());
		btnPaste.enable(Gdx.app.getClipboard().hasContents());
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
