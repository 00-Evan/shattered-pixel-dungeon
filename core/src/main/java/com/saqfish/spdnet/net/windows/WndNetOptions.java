package com.saqfish.spdnet.net.windows;

import com.saqfish.spdnet.net.ui.BlueButton;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.windows.IconTitle;
import com.watabou.noosa.Image;

public class WndNetOptions extends NetWindow {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;

	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 18;

	public WndNetOptions(Image icon, String title, String message, String... options) {
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		float pos = 0;
		if (title != null) {
			IconTitle tfTitle = new IconTitle(icon, title);
			tfTitle.setRect(0, pos, width, 0);
			add(tfTitle);

			pos = tfTitle.bottom() + 2*MARGIN;
		}

		layoutBody(pos, message, options);
	}

	private void layoutBody(float pos, String message, String... options){
		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		RenderedTextBlock tfMesage = PixelScene.renderTextBlock( 6 );
		tfMesage.text(message, width);
		tfMesage.setPos( 0, pos );
		add( tfMesage );

		pos = tfMesage.bottom() + 2*MARGIN;

		for (int i=0; i < options.length; i++) {
			final int index = i;
			BlueButton btn = new BlueButton( options[i] ) {
				@Override
				protected void onClick() {
					hide();
					onSelect( index );
				}
			};
			btn.enable(enabled(i));
			btn.setRect( 0, pos, width, BUTTON_HEIGHT );
			add( btn );

			pos += BUTTON_HEIGHT + MARGIN;
		}

		resize( width, (int)(pos - MARGIN) );
	}

	protected boolean enabled( int index ){
		return true;
	}
	
	protected void onSelect( int index ) {}
}
