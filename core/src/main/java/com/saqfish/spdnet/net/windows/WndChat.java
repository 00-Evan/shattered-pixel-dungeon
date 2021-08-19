package com.saqfish.spdnet.net.windows;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.saqfish.spdnet.Chrome;
import com.saqfish.spdnet.net.Receiver;
import com.saqfish.spdnet.net.ui.BlueButton;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.ui.ScrollPane;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextInput;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

public class WndChat extends NetWindow {

	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 16;

	public static final float WIDTH_P        = 120;
	public static final float WIDTH_L        = 300;

	public static final float MSGPADDING = 2;

	private TextInput textInput;
	private BlueButton sendBtn;

	private ScrollPane list;
	private Component content;

	private Chat c;
	private Camera cam = camera();

	private int maxMessages = 20;
	private String lastMessage;

	public WndChat() {
		super();
		int width, height;

		if (PixelScene.landscape()) width = (int)WIDTH_L;
		else width = (int)WIDTH_P;

		height = 0;

		c = new Chat();
		c.setSize(width, height);
		c.setPos(0, 1);
		add(c);

		height += c.bottom();
		resize(width, height);

		textInput.setRect(0, sendBtn.top(), width-sendBtn.width(), sendBtn.height());
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

			int textSize = (int)PixelScene.uiCamera.zoom * 8;
			textInput = new TextInput(Chrome.get(Chrome.Type.TOAST), false, textSize);
			textInput.setMaxLength(200);
			textInput.setTextAlignment(Align.left);
			textInput.setTextColor(Color.WHITE);
			add(textInput);

			textInput.addlistener(new InputListener(){
				@Override
				public boolean keyDown(InputEvent event, int keycode) {
					if(keycode == Input.Keys.ENTER ){
						net().sender().sendChat(textInput.getText());
					}
					return super.keyDown(event, keycode);
				}
			});

			list = new ScrollPane( new Component() );
			list.camera = cam;
			add( list );

			content = list.content();
			content.clear();

			sendBtn = new BlueButton(">") {
				@Override
				protected void onClick() {
					String msg = textInput.getText();
					if (!msg.equals(lastMessage)
							&& msg.length() > 0) {
						net().sender().sendChat(msg);
						lastMessage = msg;
					}
				}
			};

			add(sendBtn);

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

			height = sendBtn.bottom();

			resize((int)width, (int)height);
		}
	}

	@Override
	public synchronized void update() {
		super.update();

		if (net().reciever().newMessage()) {
			if(maxMessages != 1)
				for (Receiver.ChatMessage message: net().reciever().messages(maxMessages)) {
					addChatMessage(message);
				}
			else addChatMessage(net().reciever().lastMessage());
			maxMessages = 1;
		}else if(maxMessages != 1){
			for (Receiver.ChatMessage message : net().reciever().messages(maxMessages)) {
				addChatMessage(message);
			}
			maxMessages = 1;
		}
	}

	private void addChatMessage(Receiver.ChatMessage message){
		boolean isSender = message.id.equals(net().socket().id());
		boolean isMobile = DeviceCompat.isAndroid() || DeviceCompat.isiOS();
		RenderedTextBlock r = PixelScene.renderTextBlock(isMobile ? 7: 9);

		String finalNick  = isSender ? "You" :message.nick;

		r.text(finalNick +": "+ message.message, width);

		int nickColor = finalNick.hashCode()+1;
		r.hardlightOnWord(nickColor, 0);

		if (c.lastMessagePos == 0) r.setRect(0, 0, width, 7);
		else r.setRect(0, c.lastMessagePos + MSGPADDING, width, 7);

		c.lastMessagePos = r.bottom();

		if (r.bottom() >= content.bottom())
			content.setSize(content.width(), content.height() + r.height() + MSGPADDING);

		content.add(r);

		if (content.bottom() > list.height())
			list.scrollTo(0, r.bottom() - list.bottom() + 3);

		if(isSender)
			if(textInput != null) {
						textInput.setText("");
			}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
