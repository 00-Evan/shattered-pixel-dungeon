package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.TextInput;
import com.watabou.utils.DeviceCompat;

public class WndTextInput extends Window {

	private static final int WIDTH = 120;
	private static final int W_LAND_MULTI = 200; //in the specific case of multiline in landscape
	private static final int MARGIN = 2;
	private static final int BUTTON_HEIGHT = 16;

	public WndTextInput(final String title, final String initialValue, final int maxLength,
	                           final boolean multiLine, final String posTxt, final String negTxt) {
		super();

		//need to offset to give space for the soft keyboard
		if (!DeviceCompat.isDesktop()) {
			if (PixelScene.landscape()) {
				offset(-45);
			} else {
				offset(multiLine ? -60 : -45);
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
		final TextInput textBox = new TextInput(Chrome.get(Chrome.Type.TOAST_WHITE), multiLine, textSize){
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

	public void onSelect(boolean positive, String text){ }

	@Override
	public void onBackPressed() {
		//Do nothing, prevents accidentally losing writing
	}
}
