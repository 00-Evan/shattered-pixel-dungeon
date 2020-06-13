package net.casiello.pixeldungeonrescue.scenes;

import com.watabou.noosa.Game;

import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.windows.WndStory;


public class IntroScene2 extends PixelScene {

	@Override
	public void create() {
		super.create();

		add( new WndStory( Messages.get(this, "text") ) {
			@Override
			public void hide() {
				super.hide();
				Game.switchScene( IntroScene3.class );
			}
		} );

		fadeIn();
	}
}
