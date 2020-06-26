/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.Point;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.util.HashMap;
import java.util.regex.Pattern;

public class DesktopPlatformSupport extends PlatformSupport {
	
	@Override
	public void updateDisplaySize() {
		if (!SPDSettings.fullscreen()) {
			SPDSettings.windowResolution( new Point( Game.width, Game.height ) );
		}
	}
	
	@Override
	public void updateSystemUI() {
		Gdx.app.postRunnable( new Runnable() {
			@Override
			public void run () {
				if (SPDSettings.fullscreen()){
					Gdx.graphics.setFullscreenMode( Gdx.graphics.getDisplayMode() );
				} else {
					Point p = SPDSettings.windowResolution();
					Gdx.graphics.setWindowedMode( p.x, p.y );
				}
			}
		} );
	}
	
	@Override
	public boolean connectedToUnmeteredNetwork() {
		return true; //no easy way to check this in desktop, just assume user doesn't care
	}

	@Override
	//FIXME tinyfd_inputBox isn't a full solution for this. No support for multiline, looks ugly. Ideally we'd have an opengl-based input box
	public void promptTextInput(String title, String hintText, int maxLen, boolean multiLine, String posTxt, String negTxt, TextCallback callback) {
		String result = TinyFileDialogs.tinyfd_inputBox(title, title, hintText);
		if (result == null){
			callback.onSelect(false, "");
		} else {
			if (result.contains("\r\n"))    result = result.substring(0, result.indexOf("\r\n"));
			if (result.contains("\n"))      result = result.substring(0, result.indexOf("\n"));
			if (result.length() > maxLen)   result = result.substring(0, maxLen);
			callback.onSelect(true, result.replace("\r\n", "").replace("\n", ""));
		}
	}
	
	private int pageSize;
	private PixmapPacker packer;
	private boolean systemfont;
	
	//custom pixel font, for use with Latin and Cyrillic languages
	private static FreeTypeFontGenerator basicFontGenerator;
	private static HashMap<Integer, BitmapFont> basicFonts = new HashMap<>();
	
	//droid sans fallback, for asian fonts
	private static FreeTypeFontGenerator asianFontGenerator;
	private static HashMap<Integer, BitmapFont> asianFonts = new HashMap<>();
	
	private static HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>> fonts;
	
	@Override
	public void setupFontGenerators(int pageSize, boolean systemfont) {
		//don't bother doing anything if nothing has changed
		if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
			return;
		}
		this.pageSize = pageSize;
		this.systemfont = systemfont;
		
		if (fonts != null){
			for (FreeTypeFontGenerator generator : fonts.keySet()){
				for (BitmapFont f : fonts.get(generator).values()){
					f.dispose();
				}
				fonts.get(generator).clear();
				generator.dispose();
			}
			fonts.clear();
			if (packer != null){
				for (PixmapPacker.Page p : packer.getPages()){
					p.getTexture().dispose();
				}
				packer.dispose();
			}
		}
		fonts = new HashMap<>();

		if (systemfont) {
			basicFontGenerator = asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		} else {
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
			asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		}
		
		fonts.put(basicFontGenerator, basicFonts);
		fonts.put(asianFontGenerator, asianFonts);
		
		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}
	
	@Override
	public void resetGenerators() {
		if (fonts != null) {
			for (FreeTypeFontGenerator generator : fonts.keySet()) {
				for (BitmapFont f : fonts.get(generator).values()) {
					f.dispose();
				}
				fonts.get(generator).clear();
				generator.dispose();
			}
			fonts.clear();
			if (packer != null) {
				for (PixmapPacker.Page p : packer.getPages()) {
					p.getTexture().dispose();
				}
				packer.dispose();
			}
			fonts = null;
		}
		setupFontGenerators(pageSize, systemfont);
	}
	
	private static Pattern asianMatcher = Pattern.compile("\\p{InHangul_Syllables}|" +
			"\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}|" +
			"\\p{InHiragana}|\\p{InKatakana}");
	
	private static FreeTypeFontGenerator getGeneratorForString( String input ){
		if (asianMatcher.matcher(input).find()){
			return asianFontGenerator;
		} else {
			return basicFontGenerator;
		}
	}
	
	
	@Override
	public BitmapFont getFont(int size, String text) {
		FreeTypeFontGenerator generator = getGeneratorForString(text);
		
		if (generator == null){
			return null;
		}
		
		if (!fonts.get(generator).containsKey(size)) {
			FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameters.size = size;
			parameters.flip = true;
			parameters.borderWidth = parameters.size / 10f;
			parameters.renderCount = 3;
			parameters.hinting = FreeTypeFontGenerator.Hinting.None;
			parameters.spaceX = -(int) parameters.borderWidth;
			parameters.incremental = true;
			if (generator == basicFontGenerator){
				//if we're using latin/cyrillic, we can safely pre-generate some common letters
				//(we define common as >4% frequency in english)
				parameters.characters = "�etaoinshrdl";
			} else {
				parameters.characters = "�";
			}
			parameters.packer = packer;
			
			try {
				BitmapFont font = generator.generateFont(parameters);
				font.getData().missingGlyph = font.getData().getGlyph('�');
				fonts.get(generator).put(size, font);
			} catch ( Exception e ){
				Game.reportException(e);
				return null;
			}
		}
		
		return fonts.get(generator).get(size);
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
