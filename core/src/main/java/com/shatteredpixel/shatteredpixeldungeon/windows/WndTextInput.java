package com.shatteredpixel.shatteredpixeldungeon.windows;

import android.app.Activity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.RenderedText;

//This class makes use of the android EditText component to handle text input
public class WndTextInput extends Window {

	private EditText textInput;

	private static final int WIDTH 			= 120;
	private static final int W_LAND_MULTI 	= 200; //in the specific case of multiline in landscape
	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 16;

	//default maximum lengths for inputted text
	private static final int MAX_LEN_SINGLE = 20;
	private static final int MAX_LEN_MULTI 	= 2000;

	public WndTextInput( String title, String initialValue, boolean multiLine, String posTxt, String negTxt){
		this( title, initialValue, multiLine ? MAX_LEN_MULTI : MAX_LEN_SINGLE, multiLine, posTxt, negTxt);
	}

	public WndTextInput(String title, String initialValue, int maxLength, final boolean multiLine, String posTxt, String negTxt){
		super();

		//need to offset to give space for the soft keyboard
		if (ShatteredPixelDungeon.landscape()) {
			offset( multiLine ? -45 : -45 );
		} else {
			offset( multiLine ? -60 : -45 );
		}

		final int width;
		if (ShatteredPixelDungeon.landscape() && multiLine){
			width = W_LAND_MULTI; //more editing space for landscape users
		} else {
			width = WIDTH;
		}

		RenderedTextMultiline txtTitle = PixelScene.renderMultiline( title, 9 );
		txtTitle.maxWidth( width );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.setPos( (width - txtTitle.width()) /2, 0);
		add(txtTitle);

		float pos = txtTitle.bottom() + MARGIN;

		textInput = new EditText(ShatteredPixelDungeon.instance);
		textInput.setText( initialValue );
		textInput.setTypeface( RenderedText.getFont() );
		textInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
		textInput.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES );

		//sets different visual style depending on whether this is a single or multi line input.
		final float inputHeight;
		if (multiLine) {

			textInput.setSingleLine(false);
			//This is equivalent to PixelScene.renderText(6)
			textInput.setTextSize( TypedValue.COMPLEX_UNIT_PX, 6*camera.zoom);
			//8 lines of text (+1 line for padding)
			inputHeight = 9*textInput.getLineHeight() / camera.zoom;

		} else {

			//sets to single line and changes enter key input to be the same as the positive button
			textInput.setSingleLine();
			textInput.setOnEditorActionListener( new EditText.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					onSelect(true);
					hide();
					return true;
				}
			});

			//doesn't let the keyboard take over the whole UI
			textInput.setImeOptions( EditorInfo.IME_FLAG_NO_EXTRACT_UI );

			//centers text
			textInput.setGravity(Gravity.CENTER);

			//This is equivalent to PixelScene.renderText(9)
			textInput.setTextSize( TypedValue.COMPLEX_UNIT_PX, 9*camera.zoom);
			//1 line of text (+1 line for padding)
			inputHeight = 2*textInput.getLineHeight() / camera.zoom;

		}

		//We haven't added the textInput yet, but we can anticipate its height at this point.
		pos += inputHeight + MARGIN;

		RedButton positiveBtn = new RedButton( posTxt ) {
			@Override
			protected void onClick() {
				onSelect( true );
				hide();
			}
		};
		if (negTxt != null)
			positiveBtn.setRect( MARGIN, pos, (width - MARGIN * 3) / 2, BUTTON_HEIGHT );
		else
			positiveBtn.setRect( MARGIN, pos, width - MARGIN * 2, BUTTON_HEIGHT );
		add( positiveBtn );

		if (negTxt != null){
			RedButton negativeBtn = new RedButton( negTxt ) {
				@Override
				protected void onClick() {
					onSelect( false );
					hide();
				}
			};
			negativeBtn.setRect( positiveBtn.right() + MARGIN, pos, (width - MARGIN * 3) / 2, BUTTON_HEIGHT );
			add( negativeBtn );
		}

		pos += BUTTON_HEIGHT + MARGIN;

		//The layout of the TextEdit is in display pixel space, not ingame pixel space
		// resize the window first so we can know the screen-space coordinates for the text input.
		resize( width, (int)pos );
		final int inputTop = camera.cameraToScreen(0, txtTitle.bottom() + MARGIN).y;

		//The text input exists in a separate view ontop of the normal game view.
		// It visually appears to be a part of the game window but is infact a separate
		// UI element from the game entirely.
		ShatteredPixelDungeon.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
						(int)((width - MARGIN*2)*camera.zoom),
						(int)(inputHeight * camera.zoom),
						Gravity.CENTER_HORIZONTAL);
				layout.setMargins(0, inputTop, 0, 0);
				ShatteredPixelDungeon.instance.addContentView(textInput, layout);
			}
		});
	}

	public String getText(){
		return textInput.getText().toString().trim();
	}

	protected void onSelect( boolean positive ) {};

	@Override
	public void destroy() {
		super.destroy();
		if (textInput != null){
			ShatteredPixelDungeon.instance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//make sure we remove the edit text and soft keyboard
					((ViewGroup) textInput.getParent()).removeView(textInput);

					InputMethodManager imm = (InputMethodManager)ShatteredPixelDungeon
									.instance.getSystemService(Activity.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(textInput.getWindowToken(), 0);

					//Soft keyboard sometimes triggers software buttons, so make sure to reassert immersive
					ShatteredPixelDungeon.updateImmersiveMode();

					textInput = null;
				}
			});
		}
	}
}
