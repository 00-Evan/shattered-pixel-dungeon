/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.iosrobovm.DefaultIOSInput;
import com.badlogic.gdx.backends.iosrobovm.custom.HWMachine;
import com.badlogic.gdx.backends.iosrobovm.objectal.OALSimpleAudio;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.RectF;

import org.robovm.apple.audiotoolbox.AudioServices;
import org.robovm.apple.systemconfiguration.SCNetworkReachability;
import org.robovm.apple.systemconfiguration.SCNetworkReachabilityFlags;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIInterfaceOrientation;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOSPlatformSupport extends PlatformSupport {

	@Override
	public void updateDisplaySize() {
		UIApplication.getSharedApplication().setStatusBarHidden(true);
	}

	@Override
	public boolean supportsFullScreen() {
		//iOS supports drawing into the gesture safe area
		return Gdx.graphics.getSafeInsetBottom() > 0;
	}

	@Override
	public RectF getDisplayCutout() {
		int topInset = Gdx.graphics.getSafeInsetTop();

		//older device with no cutout, or landscape (we ignore cutouts in this case)
		if (topInset == 0){
			return new RectF();
		}

		//magic number BS for larger status bar caused by dynamic island
		boolean hasDynamicIsland = topInset / Gdx.graphics.getBackBufferScale() >= 51;

		if (!hasDynamicIsland){
			//classic notch, just shrink for the oversized safe are and then return all top.
			// this is inaccurate, as there's space left and right, but we don't care
			return new RectF(0, 0, Game.width, topInset / 1.2f);
		} else {
			//we estimate dynamic island as being 390x120 px, 40px from top.
			// this is mostly accurate, slightly oversized
			RectF cutout = new RectF( Game.width/2 - 195, 40, Game.width/2 + 195, 160);

			//iPhone air specifically has its island a bit lower
			// so we check for its machine string and also simulator with same width
			String machineString = HWMachine.getMachineString();
			if (machineString.equals("iPhone18,4")
					|| (machineString.equals("arm64") && Game.width == 1260)){
				cutout.shift(0, 15);
			}
			return cutout;
		}
	}

	@Override
	public RectF getSafeInsets(int level) {
		RectF insets = super.getSafeInsets(INSET_ALL);

		//magic number BS for larger status bar caused by dynamic island
		boolean hasDynamicIsland = insets.top / Gdx.graphics.getBackBufferScale() >= 51;

		//iOS gives us ALL insets by default, and so we need to filter from there:

		//ignore the home indicator if we're in fullscreen
		if (!supportsFullScreen() || SPDSettings.fullscreen()){
			insets.bottom = 0;
		}

		//only cutouts can be on top/left/right, which are never blocking
		if (level == INSET_BLK){
			insets.left = insets.top = insets.right = 0;
		} else if (level == INSET_LRG && hasDynamicIsland){
			//Dynamic Island counts as a 'small cutout'
			insets.left = insets.top = insets.right = 0;
		}

		//if we are in landscape, the display cutout is only actually on one side, so cancel the other
		if (Game.width > Game.height){
			if (UIApplication.getSharedApplication().getStatusBarOrientation().equals(UIInterfaceOrientation.LandscapeLeft)){
				insets.left = 0;
			} else {
				insets.right = 0;
			}
		}

		//finally iOS is very conservative with these insets, we can shrink them a bit.
		insets.top /= hasDynamicIsland ? 1.2f : 1.4f;
		insets.left /= hasDynamicIsland ? 1.2f : 1.4f;
		insets.right /= hasDynamicIsland ? 1.2f : 1.4f;
		insets.bottom /= 2; //home bar inset is especially big for no reason

		return insets;
	}

	@Override
	public void updateSystemUI() {
		updateDisplaySize();
	}

	@Override
	public boolean connectedToUnmeteredNetwork() {
		SCNetworkReachability test = new SCNetworkReachability("www.apple.com");
		return !test.getFlags().contains(SCNetworkReachabilityFlags.IsWWAN);
	}

	@Override
	public boolean supportsVibration() {
		//Devices with haptics...
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.HapticFeedback)){
			return true;
		};

		//...or with a supported controller connected
		if (ControllerHandler.vibrationSupported()){
			return true;
		}

		//...or with 3d touch
		String machineString = HWMachine.getMachineString();
		if (machineString.equals("iPhone8,4")){ //1st gen SE has no 3D touch specifically
			return false;
		} else { // 6s/7/8/X/XR have 3D touch
			return machineString.contains("iphone8")        //6s
					|| machineString.contains("iphone9")    //7
					|| machineString.contains("iphone10")   //8, and X
					|| machineString.contains("iphone11");  //XS (also XR but that has haptic)
		}
	}

	public void vibrate(int millis ){
		if (ControllerHandler.isControllerConnected()){
			ControllerHandler.vibrate(millis);
		} else if (Gdx.input.isPeripheralAvailable(Input.Peripheral.HapticFeedback)){
			Gdx.input.vibrate( millis );
		} else {
			//devices without haptics but with 3d touch use a short vibrate
			AudioServices.playSystemSound(1520);
			// no vibration otherwise
		}
	}

	@Override
	public void setHonorSilentSwitch( boolean value ) {
		OALSimpleAudio.sharedInstance().setHonorSilentSwitch(value);
	}

	public void setOnscreenKeyboardVisible(boolean value, boolean multiline){
		//iOS keyboard says 'done' even with this change, but the behaviour is correct at least
		((DefaultIOSInput)Gdx.input).setKeyboardCloseOnReturnKey(!multiline);
		super.setOnscreenKeyboardVisible(value, multiline);
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

	//splits on newline (for layout), chinese/japanese (for font choice), and '_'/'**' (for highlighting)
	private Pattern regularsplitter = Pattern.compile(
			"(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})");

	//additionally splits on spaces, so that each word can be laid out individually
	private Pattern regularsplitterMultiline = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
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
