package com.saqfish.spdnet.desktop.windows;


import com.saqfish.spdnet.net.Reciever;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.ui.ScrollPane;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ui.Component;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class WndChat extends NetWindow {

	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 14;

	public static final float WIDTH_P        = 120;
	public static final float WIDTH_L        = 300;

	public static final float MSGPADDING = 2;

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

		}

		@Override
		protected void layout() {
			super.layout();

			content.setRect(0,0, width, BOXHEIGHT );
			list.setRect( 0, 0, width, BOXHEIGHT);

			list.scrollTo( 0, 0 );

			height=list.bottom() + (MARGIN * 3);

			resize((int)width, (int)height);
		}
	}

	@Override
	public synchronized void update() {
		super.update();
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
			content.setSize(content.width(), content.height() + r.height() + MSGPADDING);

		content.add(r);

		if (content.bottom() > list.height())
			list.scrollTo(0, r.bottom() - list.bottom() + 3);

	}
}
