package com.shatteredpixel.shatteredpixeldungeon.ios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSGraphics;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;

import org.robovm.apple.audiotoolbox.AudioServices;
import org.robovm.apple.systemconfiguration.SCNetworkReachability;
import org.robovm.apple.systemconfiguration.SCNetworkReachabilityFlags;
import org.robovm.apple.uikit.UIApplication;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOSPlatformSupport extends PlatformSupport {
	@Override
	public void updateDisplaySize() {
		//non-zero safe insets on left/top/right means device has a notch, show status bar
		if (Gdx.graphics.getSafeInsetTop() != 0
				|| Gdx.graphics.getSafeInsetLeft() != 0
				|| Gdx.graphics.getSafeInsetRight() != 0){
			UIApplication.getSharedApplication().setStatusBarHidden(false);
		} else {
			UIApplication.getSharedApplication().setStatusBarHidden(true);
		}

		if (!SPDSettings.fullscreen()) {
			int insetChange = Gdx.graphics.getSafeInsetBottom() - Game.bottomInset;
			Game.bottomInset = Gdx.graphics.getSafeInsetBottom();
			Game.height -= insetChange;
			Game.dispHeight = Game.height;
		} else {
			Game.height += Game.bottomInset;
			Game.dispHeight = Game.height;
			Game.bottomInset = 0;
		}
		Gdx.gl.glViewport(0, Game.bottomInset, Game.width, Game.height);
	}

	@Override
	public void updateSystemUI() {
		int prevInset = Game.bottomInset;
		updateDisplaySize();
		if (prevInset != Game.bottomInset) {
			ShatteredPixelDungeon.seamlessResetScene();
		}
	}

	@Override
	public boolean connectedToUnmeteredNetwork() {
		SCNetworkReachability test = new SCNetworkReachability("www.apple.com");
		return !test.getFlags().contains(SCNetworkReachabilityFlags.IsWWAN);
	}

	public void vibrate( int millis ){
		//gives a short vibrate on iPhone 6+, no vibration otherwise
		AudioServices.playSystemSound(1520);
	}

	/* FONT SUPPORT */

	//custom pixel font, for use with Latin and Cyrillic languages
	private static FreeTypeFontGenerator basicFontGenerator;
	//droid sans fallback, for asian fonts
	private static FreeTypeFontGenerator asianFontGenerator;

	@Override
	public void setupFontGenerators(int pageSize, boolean systemfont) {
		//don't bother doing anything if nothing has changed
		if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
			return;
		}
		this.pageSize = pageSize;
		this.systemfont = systemfont;

		resetGenerators(false);
		fonts = new HashMap<>();

		if (systemfont) {
			basicFontGenerator = asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		} else {
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
			asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		}

		fonts.put(basicFontGenerator, new HashMap<>());
		fonts.put(asianFontGenerator, new HashMap<>());

		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}

	private static final Matcher asianMatcher = Pattern.compile("\\p{InHangul_Syllables}|" +
			"\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}|" +
			"\\p{InHiragana}|\\p{InKatakana}").matcher("");

	@Override
	protected FreeTypeFontGenerator getGeneratorForString( String input ){
		if (asianMatcher.reset(input).find()){
			return asianFontGenerator;
		} else {
			return basicFontGenerator;
		}
	}

	//splits on newlines, underscores, and chinese/japaneses characters
	private Pattern regularsplitter = Pattern.compile(
			"(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");

	//additionally splits on words, so that each word can be arranged individually
	private Pattern regularsplitterMultiline = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");

	@Override
	public String[] splitforTextBlock(String text, boolean multiline) {
		if (multiline) {
			return regularsplitterMultiline.split(text);
		} else {
			return regularsplitter.split(text);
		}
	}
}
