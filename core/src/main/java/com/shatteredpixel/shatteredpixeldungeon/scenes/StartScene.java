/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites.Type;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndClass;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.BitmaskEmitter;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class StartScene extends PixelScene {

	private static final float BUTTON_HEIGHT	= 24;
	private static final float GAP				= 2;

	private static final float WIDTH_P    = 116;
	private static final float HEIGHT_P    = 220;

	private static final float WIDTH_L    = 224;
	private static final float HEIGHT_L    = 124;

	private static HashMap<HeroClass, ClassShield> shields = new HashMap<>();

	private float buttonX;
	private float buttonY;

	private GameButton btnLoad;
	private GameButton btnNewGame;

	private boolean huntressUnlocked;
	private Group unlock;

	public static HeroClass curClass;

	@Override
	public void create() {

		super.create();

		Badges.loadGlobal();

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		float width, height;
		if (ShatteredPixelDungeon.landscape()) {
			width = WIDTH_L;
			height = HEIGHT_L;
		} else {
			width = WIDTH_P;
			height = HEIGHT_P;
		}

		float left = (w - width) / 2;
		float top = (h - height) / 2;
		float bottom = h - top;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Image title = BannerSprites.get( Type.SELECT_YOUR_HERO );
		title.x = (w - title.width()) / 2;
		title.y = top;
		align( title );
		add( title );

		buttonX = left;
		buttonY = bottom - BUTTON_HEIGHT;

		btnNewGame = new GameButton( Messages.get(this, "new") ) {
			@Override
			protected void onClick() {
				if (GamesInProgress.check( curClass ) != null) {
					StartScene.this.add( new WndOptions(
							Messages.get(StartScene.class, "really"),
							Messages.get(StartScene.class, "warning"),
							Messages.get(StartScene.class, "yes"),
							Messages.get(StartScene.class, "no") ) {
						@Override
						protected void onSelect( int index ) {
							if (index == 0) {
								startNewGame();
							}
						}
					} );

				} else {
					startNewGame();
				}
			}
		};
		add( btnNewGame );

		btnLoad = new GameButton( Messages.get(this, "load") ) {
			@Override
			protected void onClick() {
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				Game.switchScene( InterlevelScene.class );
			}
		};
		add( btnLoad );

		float centralHeight = buttonY - title.y - title.height();

		HeroClass[] classes = {
				HeroClass.WARRIOR, HeroClass.MAGE, HeroClass.ROGUE, HeroClass.HUNTRESS
		};
		for (HeroClass cl : classes) {
			ClassShield shield = new ClassShield( cl );
			shields.put( cl, shield );
			add( shield );
		}
		if (ShatteredPixelDungeon.landscape()) {
			float shieldW = width / 4;
			float shieldH = Math.min( centralHeight, shieldW );
			top = title.y + title.height + (centralHeight - shieldH) / 2;
			for (int i=0; i < classes.length; i++) {
				ClassShield shield = shields.get( classes[i] );
				shield.setRect( left + i * shieldW, top, shieldW, shieldH );
				align(shield);
			}

			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos(
					w/2 - challenge.width()/2,
					top + shieldH/2 - challenge.height()/2 );
			add( challenge );

		} else {
			float shieldW = width / 2;
			float shieldH = Math.min( centralHeight / 2, shieldW * 1.2f );
			top = title.y + title.height() + centralHeight / 2 - shieldH;
			for (int i=0; i < classes.length; i++) {
				ClassShield shield = shields.get( classes[i] );
				shield.setRect(
						left + (i % 2) * shieldW,
						top + (i / 2) * shieldH,
						shieldW, shieldH );
				align(shield);
			}

			ChallengeButton challenge = new ChallengeButton();
			challenge.setPos(
					w/2 - challenge.width()/2,
					top + shieldH - challenge.height()/2 );
			align(challenge);
			add( challenge );

		}

		unlock = new Group();
		add( unlock );

		if (!(huntressUnlocked = Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_3 ))) {

			RenderedTextMultiline text = PixelScene.renderMultiline( Messages.get(this, "unlock"), 9 );
			text.maxWidth((int)width);
			text.hardlight( 0xFFFF00 );
			text.setPos(w / 2 - text.width() / 2, (bottom - BUTTON_HEIGHT) + (BUTTON_HEIGHT - text.height()) / 2);
			align(text);
			unlock.add(text);

		}

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		curClass = null;
		updateClass( HeroClass.values()[ShatteredPixelDungeon.lastClass()] );

		fadeIn();

		Badges.loadingListener = new Callback() {
			@Override
			public void call() {
				if (Game.scene() == StartScene.this) {
					ShatteredPixelDungeon.switchNoFade( StartScene.class );
				}
			}
		};
	}

	@Override
	public void destroy() {

		Badges.saveGlobal();
		Badges.loadingListener = null;

		super.destroy();

	}

	private void updateClass( HeroClass cl ) {

		if (curClass == cl) {
			add( new WndClass( cl ) );
			return;
		}

		if (curClass != null) {
			shields.get( curClass ).highlight( false );
		}
		shields.get( curClass = cl ).highlight( true );

		if (cl != HeroClass.HUNTRESS || huntressUnlocked) {

			unlock.visible = false;

			GamesInProgress.Info info = GamesInProgress.check( curClass );
			if (info != null) {

				btnLoad.visible = true;
				btnLoad.secondary( Messages.format( Messages.get(this, "depth_level"), info.depth, info.level ), info.challenges );
				btnNewGame.visible = true;
				btnNewGame.secondary( Messages.get(this, "erase"), false );

				float w = (Camera.main.width - GAP) / 2 - buttonX;

				btnLoad.setRect(
						buttonX, buttonY, w, BUTTON_HEIGHT );
				btnNewGame.setRect(
						btnLoad.right() + GAP, buttonY, w, BUTTON_HEIGHT );

			} else {
				btnLoad.visible = false;

				btnNewGame.visible = true;
				btnNewGame.secondary( null, false );
				btnNewGame.setRect( buttonX, buttonY, Camera.main.width - buttonX * 2, BUTTON_HEIGHT );
			}

		} else {

			unlock.visible = true;
			btnLoad.visible = false;
			btnNewGame.visible = false;

		}
	}

	private void startNewGame() {

		Dungeon.hero = null;
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

		if (ShatteredPixelDungeon.intro()) {
			ShatteredPixelDungeon.intro( false );
			Game.switchScene( IntroScene.class );
		} else {
			Game.switchScene( InterlevelScene.class );
		}
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade( TitleScene.class );
	}

	private static class GameButton extends RedButton {

		private static final int SECONDARY_COLOR_N    = 0xCACFC2;
		private static final int SECONDARY_COLOR_H    = 0xFFFF88;

		private RenderedText secondary;

		public GameButton( String primary ) {
			super( primary );

			this.secondary.text( null );
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			secondary = renderText( 6 );
			add( secondary );
		}

		@Override
		protected void layout() {
			super.layout();

			if (secondary.text().length() > 0) {
				text.y = y + (height - text.height() - secondary.baseLine()) / 2;

				secondary.x = x + (width - secondary.width()) / 2;
				secondary.y = text.y + text.height();
			} else {
				text.y = y + (height - text.baseLine()) / 2;
			}
			align(text);
			align(secondary);
		}

		public void secondary( String text, boolean highlighted ) {
			secondary.text( text );

			secondary.hardlight( highlighted ? SECONDARY_COLOR_H : SECONDARY_COLOR_N );
		}
	}

	private class ClassShield extends Button {

		private static final float MIN_BRIGHTNESS	= 0.6f;

		private static final int BASIC_NORMAL        = 0x444444;
		private static final int BASIC_HIGHLIGHTED    = 0xCACFC2;

		private static final int MASTERY_NORMAL        = 0x666644;
		private static final int MASTERY_HIGHLIGHTED= 0xFFFF88;

		private static final int WIDTH	= 24;
		private static final int HEIGHT	= 32;
		private static final float SCALE	= 1.75f;

		private HeroClass cl;

		private Image avatar;
		private RenderedText name;
		private Emitter emitter;

		private float brightness;

		private int normal;
		private int highlighted;

		public ClassShield( HeroClass cl ) {
			super();

			this.cl = cl;

			avatar.frame( cl.ordinal() * WIDTH, 0, WIDTH, HEIGHT );
			avatar.scale.set( SCALE );

			if (Badges.isUnlocked( cl.masteryBadge() )) {
				normal = MASTERY_NORMAL;
				highlighted = MASTERY_HIGHLIGHTED;
			} else {
				normal = BASIC_NORMAL;
				highlighted = BASIC_HIGHLIGHTED;
			}

			name.text( cl.title().toUpperCase() );
			name.hardlight( normal );

			brightness = MIN_BRIGHTNESS;
			updateBrightness();
		}

		@Override
		protected void createChildren() {

			super.createChildren();

			avatar = new Image( Assets.AVATARS );
			add( avatar );

			name = PixelScene.renderText( 9 );
			add( name );

			emitter = new BitmaskEmitter( avatar );
			add( emitter );
		}

		@Override
		protected void layout() {

			super.layout();

			avatar.x = x + (width - avatar.width()) / 2;
			avatar.y = y + (height - avatar.height() - name.height()) / 2;
			align(avatar);

			name.x = x + (width - name.width()) / 2;
			name.y = avatar.y + avatar.height() + SCALE;
			align(name);

		}

		@Override
		protected void onTouchDown() {

			emitter.revive();
			emitter.start( Speck.factory( Speck.LIGHT ), 0.05f, 7 );

			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 1.2f );
			updateClass( cl );
		}

		@Override
		public void update() {
			super.update();

			if (brightness < 1.0f && brightness > MIN_BRIGHTNESS) {
				if ((brightness -= Game.elapsed) <= MIN_BRIGHTNESS) {
					brightness = MIN_BRIGHTNESS;
				}
				updateBrightness();
			}
		}

		public void highlight( boolean value ) {
			if (value) {
				brightness = 1.0f;
				name.hardlight( highlighted );
			} else {
				brightness = 0.999f;
				name.hardlight( normal );
			}

			updateBrightness();
		}

		private void updateBrightness() {
			avatar.gm = avatar.bm = avatar.rm = avatar.am = brightness;
		}
	}

	private class ChallengeButton extends Button {

		private Image image;

		public ChallengeButton() {
			super();

			width = image.width;
			height = image.height;

			image.am = Badges.isUnlocked( Badges.Badge.VICTORY ) ? 1.0f : 0.5f;
		}

		@Override
		protected void createChildren() {

			super.createChildren();

			image = Icons.get( ShatteredPixelDungeon.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF );
			add( image );
		}

		@Override
		protected void layout() {

			super.layout();

			image.x = x;
			image.y = y;
		}

		@Override
		protected void onClick() {
			if (Badges.isUnlocked( Badges.Badge.VICTORY )) {
				StartScene.this.add(new WndChallenges(ShatteredPixelDungeon.challenges(), true) {
					public void onBackPressed() {
						super.onBackPressed();
						image.copy( Icons.get( ShatteredPixelDungeon.challenges() > 0 ?
								Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF ) );
					}
				} );
			} else {
				StartScene.this.add( new WndMessage( Messages.get(StartScene.class, "need_to_win") ) );
			}
		}

		@Override
		protected void onTouchDown() {
			Sample.INSTANCE.play( Assets.SND_CLICK );
		}
	}
}