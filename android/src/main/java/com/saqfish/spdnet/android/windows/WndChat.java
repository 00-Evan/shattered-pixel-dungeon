package com.saqfish.spdnet.android.windows;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.saqfish.spdnet.SPDSettings;
import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.android.AndroidGame;
import com.saqfish.spdnet.net.Reciever;
import com.saqfish.spdnet.net.ui.BlueButton;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.ui.ScrollPane;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class WndChat extends NetWindow {

	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 14;

	public static final float WIDTH_P        = 120;
	public static final float WIDTH_L        = 300;

	public static final float MSGPADDING = 2;

	private EditText textInput;

	private ColorBlock inputBg;
	private RenderedTextBlock inputFg;
	private BlueButton sendBtn;

	private ScrollPane list;
	private Component content;

	private Chat c;
	private Camera cam = camera();

	private int maxMessages = 10;

	public WndChat() {
		super();
		int width, height;

		if (PixelScene.landscape()) width = (int)WIDTH_L;
		else width = (int)WIDTH_P;

		height = 0;

		c = new Chat();
		c.setSize(width, height);
		c.setPos(0, 0);
		add(c);

		height += c.bottom();
		resize(width, height);
	}

	public class Chat extends Component {
		public static final float BOXHEIGHT = 100;

		private float lastMessagePos;

		public Chat() {
			super();
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			list = new ScrollPane( new Component() );
			list.camera = cam;
			add( list );

			content = list.content();
			content.clear();

			inputBg = new ColorBlock(1, 1, 0xFF000000);
			inputFg = PixelScene.renderTextBlock(9);
			sendBtn = new BlueButton(">") {
				@Override
				protected void onClick() {
					net().sender().sendChat(textInput.getText().toString());
				}
			};

			add(inputBg);
			add(inputFg);
			add(sendBtn);

			((AndroidApplication)Gdx.app).runOnUiThread(new Runnable() {
				@Override
				public void run() {

					textInput = new EditText((AndroidApplication) Gdx.app);
					if (!SPDSettings.systemFont()) {
						textInput.setTypeface(Typeface.createFromAsset(AndroidGame.instance.getAssets(), "fonts/pixel_font.ttf"));
					}
					textInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(150)});
					textInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


					final float scaledZoom;
					scaledZoom = cam.zoom * (Game.dispWidth / (float) Game.width);

					final float inputHeight;

					textInput.setSingleLine();

					textInput.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
					textInput.setGravity(Gravity.START);
					textInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, 8 * scaledZoom);
					inputHeight = 2 * textInput.getLineHeight() / scaledZoom;

					final int inputTop = (int) (cam.cameraToScreen(0, MARGIN + (height - inputHeight)).y * (Game.dispWidth / (float) Game.width));
					final int inputLeft = (int) (cam.cameraToScreen(0, 0).x * (Game.dispHeight / (float) Game.height));

					FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
							(int) ((width - 20 - MARGIN * 2) * scaledZoom),
							(int) (inputHeight * scaledZoom),
							Gravity.START);
					layout.setMargins(inputLeft, inputTop, 0, 0);

					textInput.setCursorVisible(false);
					textInput.setBackgroundColor(Color.TRANSPARENT);
					textInput.setTextColor(Color.TRANSPARENT);

					((AndroidApplication) Gdx.app).addContentView(textInput, layout);
				}

			});
		}

		@Override
		protected void layout() {
			super.layout();

			content.setRect(0,0, width, BOXHEIGHT );
			list.setRect( 0, 0, width, BOXHEIGHT);

			list.scrollTo( 0, 0 );

			height=list.bottom() + (MARGIN * 3);

			sendBtn.setPos( width-sendBtn.width()-1, height);
			sendBtn.setSize( 20, BUTTON_HEIGHT );

			inputBg.alpha(0.5f);
			inputBg.size(width, sendBtn.height()+MARGIN);
			inputBg.y = sendBtn.top()-1;

			inputFg.setPos(MARGIN, height+MARGIN);
			inputFg.setSize( 20, inputBg.height);

			height += inputBg.height();

			resize((int)width, (int)height);
		}
	}

	@Override
	public synchronized void update() {
		super.update();
		if (textInput != null) {
			String text = textInput.getText().toString();
			if (text.length() >= 1) {
				if (text.length() > 18)
					inputFg.text(text.substring(text.length() - 18));
				else inputFg.text(text);
			} else inputFg.clear();
		}

		if (net().reciever().newMessage()) {
			if(maxMessages != 1)
				for (Reciever.ChatMessage message: net().reciever().messages(maxMessages)) {
					addChatMessage(message);
				}
			else addChatMessage(net().reciever().lastMessage());
			maxMessages = 1;
		}else if(maxMessages != 1){
			for (Reciever.ChatMessage message : net().reciever().messages(maxMessages)) {
				addChatMessage(message);
			}
			maxMessages = 1;
		}
	}

	private void addChatMessage(Reciever.ChatMessage message){
		boolean isSender = message.id.equals(net().socket().id());
		RenderedTextBlock r = PixelScene.renderTextBlock(7);

		String finalNick  = isSender ? "You" :message.nick;

		r.text(finalNick +": "+ message.message, width);

		int nickColor = finalNick.hashCode();
		r.hardlightOnWord(nickColor, 0);

		if (c.lastMessagePos == 0) r.setRect(0, 0, width, 7);
		else r.setRect(0, c.lastMessagePos + MSGPADDING, width, 7);

		c.lastMessagePos = r.bottom();

		if (r.bottom() >= content.bottom())
			content.setSize(content.width(), content.height() + r.height());

		content.add(r);

		if (content.bottom() > list.height())
			list.scrollTo(0, r.bottom() - list.bottom() + 3);

		if(isSender)
			textInput.setText("");
	}

	@Override
	public void destroy() {
		super.destroy();
		if (textInput != null){
			((AndroidApplication)Gdx.app).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					((ViewGroup) textInput.getParent()).removeView(textInput);

					InputMethodManager imm = (InputMethodManager)((AndroidApplication)Gdx.app).getSystemService(Activity.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(((AndroidGraphics)Gdx.app.getGraphics()).getView().getWindowToken(), 0);

					ShatteredPixelDungeon.updateSystemUI();

					textInput = null;
				}
			});
		}
	}
}
