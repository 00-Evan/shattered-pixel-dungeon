/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.watabou.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Game;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public abstract class PlatformSupport {
	
	public abstract void updateDisplaySize();

	public boolean supportsFullScreen(){
		return true; //default
	}

	public static final int INSET_ALL = 3; //All insets, from hole punches to nav bars
	public static final int INSET_LRG = 2; //Only big insets, full size notches and nav bars
	public static final int INSET_BLK = 1; //only complete blocker assets like navbars

	public RectF getSafeInsets( int level ){
		return new RectF(
				Gdx.graphics.getSafeInsetLeft(),
				Gdx.graphics.getSafeInsetTop(),
				Gdx.graphics.getSafeInsetRight(),
				Gdx.graphics.getSafeInsetBottom()
		);
	}

	//returns a display cutout (if one is present) in device pixels, or empty if none is present
	public RectF getDisplayCutout(){
		return new RectF();
	}
	
	public abstract void updateSystemUI();

	public abstract boolean connectedToUnmeteredNetwork();

	public abstract boolean supportsVibration();

	public void vibrate( int millis ){
		if (ControllerHandler.isControllerConnected()) {
			ControllerHandler.vibrate(millis);
		} else {
			Gdx.input.vibrate( millis );
		}
	}

	public void setHonorSilentSwitch( boolean value ){
		//does nothing by default
	}

	public boolean openURI( String uri ){
		return Gdx.net.openURI( uri );
	}

	public void setOnscreenKeyboardVisible(boolean value, boolean multiline){
		//by default ignore multiline
		Gdx.input.setOnscreenKeyboardVisible(value, Input.OnscreenKeyboardType.Default);
	}

	//leave string blank to return any folder we have access to, or specify a specific folder
	public String getExternalDataFolder(String folderName){
		//assume no permissions are needed by default, also we have to specify a folder here
		FileHandle file = Gdx.files.absolute(folderName);
		if (file.exists() && file.isDirectory()){
			return file.pathWithoutExtension();
		} else {
			return null;
		}
	}

	public interface FolderCreationCallback {
		//foldername is null if creation failed
		void call(String foldername);
	}

	//Note that depending on the platform the given folder name may just be a suggestion!
	public void createExternalDataFolder(String folderName, FolderCreationCallback callback){
		//assume no permissions are needed by default
		FileHandle folder = Gdx.files.absolute(folderName);
		if (folder.exists() && folder.isDirectory()){
			callback.call(folder.path());
			return;
		} else {
			folder.mkdirs();
			if (folder.exists()) {
				callback.call(folder.path());
				return;
			}
		}
		callback.call(null);
	}

	// For platform compat reasons (i.e. Android bullshit) these return streams and not files =S

	public InputStream readExternalDataFile(String folderName, String fileName){
		FileHandle file = Gdx.files.absolute(folderName + "/" + fileName);
		return file.read();
	}

	public OutputStream writeExternalDataFile(String folderName, String fileName){
		FileHandle file = Gdx.files.absolute(folderName + "/" + fileName);
		return file.write(false);
	}

	//TODO should consider spinning this into its own class, rather than platform support getting ever bigger
	protected static HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>> fonts;

	protected int pageSize;
	protected PixmapPacker packer;
	protected boolean systemfont;
	
	public abstract void setupFontGenerators(int pageSize, boolean systemFont );

	protected abstract FreeTypeFontGenerator getGeneratorForString( String input );

	public abstract String[] splitforTextBlock( String text, boolean multiline );

	public void resetGenerators(){
		resetGenerators( true );
	}

	public void resetGenerators( boolean setupAfter ){
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
		if (setupAfter) setupFontGenerators(pageSize, systemfont);
	}

	public void reloadGenerators(){
		if (packer != null) {
			for (FreeTypeFontGenerator generator : fonts.keySet()) {
				for (BitmapFont f : fonts.get(generator).values()) {
					f.dispose();
				}
				fonts.get(generator).clear();
			}
			if (packer != null) {
				for (PixmapPacker.Page p : packer.getPages()) {
					p.getTexture().dispose();
				}
				packer.dispose();
			}
			packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
		}
	}

	//flipped is needed because Shattered's graphics are y-down, while GDX graphics are y-up.
	//this is very confusing, I know.
	public BitmapFont getFont(int size, String text, boolean flipped, boolean border) {
		FreeTypeFontGenerator generator = getGeneratorForString(text);

		if (generator == null){
			return null;
		}

		int key = size;
		if (border) key += Short.MAX_VALUE; //surely we'll never have a size above 32k
		if (flipped) key = -key;
		if (!fonts.get(generator).containsKey(key)) {
			FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameters.size = size;
			parameters.flip = flipped;
			parameters.borderWidth = parameters.size / 10f;
			if (!border) {
				//we draw a border here and just have it be clear so that border and no border font have the same spacing
				//this is needed for Arabic, which overlays border and no border font
				parameters.borderColor = Color.CLEAR;
			}
			if (size >= 20){
				parameters.renderCount = 2;
			} else {
				parameters.renderCount = 3;
			}
			parameters.hinting = FreeTypeFontGenerator.Hinting.None;
			parameters.spaceX = -(int) parameters.borderWidth;
			parameters.incremental = true;
			parameters.characters = "�";
			parameters.packer = packer;

			try {
				BitmapFont font = generator.generateFont(parameters);
				font.getData().missingGlyph = font.getData().getGlyph('�');
				fonts.get(generator).put(key, font);
			} catch ( Exception e ){
				Game.reportException(e);
				return null;
			}
		}

		return fonts.get(generator).get(key);
	}

}
