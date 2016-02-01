package com.shatteredpixel.shatteredpixeldungeon.scenes;

import android.opengl.GLES20;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedTextMultiline;
import com.watabou.noosa.audio.Sample;

import javax.microedition.khronos.opengles.GL10;

public class WelcomeScene extends PixelScene {

	private static final String TXT_Welcome =
			"Shattered Pixel Dungeon is a roguelike RPG, with randomly generated enemies, levels, items, and traps!\n" +
					"\n" +
					"Each run is a new challenging experience, but be careful, death is permanent!\n" +
					"\n" +
					"Happy Dungeoneering!";

	private static final String TXT_Update =
			"add something here on next update.";

	@Override
	public void create() {
		super.create();

		final int previousVersion = ShatteredPixelDungeon.version();

		if (ShatteredPixelDungeon.versionCode == previousVersion) {
			ShatteredPixelDungeon.switchNoFade(TitleScene.class);
			return;
		}

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		title.brightness(0.5f);
		add( title );

		float height = title.height +
				(ShatteredPixelDungeon.landscape() ? 48 : 96);

		title.x = (w - title.width()) / 2;
		title.y = (h - height) / 2;

		placeTorch(title.x + 18, title.y + 20);
		placeTorch(title.x + title.width - 18, title.y + 20);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = (float)Math.sin( -(time += Game.elapsed) );
			}
			@Override
			public void draw() {
				GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
				super.draw();
				GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			}
		};
		signs.x = title.x;
		signs.y = title.y;
		add( signs );

		RedButton okay = new RedButton("Continue"){

			{
				bg.brightness(0.4f);
			}

			@Override
			protected void onTouchDown() {
				bg.brightness(0.5f);
				Sample.INSTANCE.play( Assets.SND_CLICK );
			}

			@Override
			protected void onClick() {
				super.onClick();

				if (previousVersion <= 32){
					//removes all bags bought badge from pre-0.2.4 saves.
					Badges.disown(Badges.Badge.ALL_BAGS_BOUGHT);
					Badges.saveGlobal();

					//imports new ranking data for pre-0.2.3 saves.
					if (previousVersion <= 29){
						Rankings.INSTANCE.load();
						Rankings.INSTANCE.save();
					}
				}


				ShatteredPixelDungeon.version(ShatteredPixelDungeon.versionCode);
				ShatteredPixelDungeon.switchScene(TitleScene.class);
			}

			@Override
			protected void onTouchUp() {
				super.onTouchUp();
				bg.brightness(0.4f);
			}
		};

		okay.setRect(title.x, h-22, title.width(), 20);
		okay.textColor(0xBBBB33);
		add(okay);

		RenderedTextMultiline text = PixelScene.renderMultiline(6);
		if (previousVersion == 0) {
			text.text(TXT_Welcome, w - 20);
		} else {
			text.text(TXT_Update, w - 20);
		}
		float textSpace = h - title.y - (title.height() - 10) - okay.height() - 2;
		text.setPos(10, title.y+(title.height() - 10) + ((textSpace - text.height()) / 2));
		text.hardlight(0xAAAAAA);
		add(text);

	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}
}
