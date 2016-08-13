/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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

package com.watabou.noosa;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import com.watabou.gltextures.SmartTexture;
import com.watabou.glwrap.Matrix;
import com.watabou.glwrap.Texture;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RenderedText extends Image {

	private static Canvas canvas = new Canvas();
	private static Paint painter = new Paint();

	private static Typeface font;

	//this is basically a LRU cache. capacity is determined by character count, not entry count.
	//will attempt to clear oldest, not in use entires until there are 500 characters stored.
	//FIXME: Caching based on words is very inefficient for every language but chinese.
	private static LinkedHashMap<String, CachedText> textCache =
			new LinkedHashMap<String, CachedText>(700, 0.75f, true){
				private int cachedChars = 0;
				private final int MAX_CACHED = 500;

				@Override
				public CachedText put(String key, CachedText value) {
					cachedChars += value.length;
					CachedText added = super.put(key, value);
					runGC();
					return added;
				}

				@Override
				public CachedText remove(Object key) {
					CachedText removed = super.remove(key);
					if (removed != null) {
						cachedChars-= removed.length;
						removed.texture.delete();
					}
					return removed;
				}

				@Override
				public void clear() {
					super.clear();
					cachedChars = 0;
				}

				private void runGC(){
					Iterator<Map.Entry<String, CachedText>> it = this.entrySet().iterator();
					while (cachedChars > MAX_CACHED && it.hasNext()){
						CachedText cached = it.next().getValue();
						if (cached.activeTexts.isEmpty()) it.remove();
					}
				}
	};

	private int size;
	private String text;
	private CachedText cache;

	public RenderedText( ){
		text = null;
	}

	public RenderedText( int size ){
		text = null;
		this.size = size;
	}

	public RenderedText(String text, int size){
		this.text = text;
		this.size = size;

		render();
	}

	public void text( String text ){
		this.text = text;

		render();
	}

	public String text(){
		return text;
	}

	public void size( int size ){
		this.size = size;
		render();
	}

	public float baseLine(){
		return size * scale.y;
	}

	private void render(){
		if ( text == null || text.equals("") ) {
			text = "";
			width=height=0;
			visible = false;
			return;
		} else {
			visible = true;
		}

		if (cache != null)
			cache.activeTexts.remove(this);

		String key = "text:" + size + " " + text;
		if (textCache.containsKey(key)){
			cache = textCache.get(key);
			texture = cache.texture;
			frame(cache.rect);
			cache.activeTexts.add(this);
		} else {

			painter.setTextSize(size);
			painter.setAntiAlias(true);

			if (font != null) {
				painter.setTypeface(font);
			} else {
				painter.setTypeface(Typeface.DEFAULT);
			}

			//paint outer strokes
			painter.setARGB(0xff, 0, 0, 0);
			painter.setStyle(Paint.Style.STROKE);
			painter.setStrokeWidth(size / 5f);

			int right = (int)(painter.measureText(text)+ (size/5f));
			int bottom = (int)(-painter.ascent() + painter.descent()+ (size/5f));

			//bitmap has to be in a power of 2 for some devices (as we're using openGL methods to render to texture)
			Bitmap bitmap = Bitmap.createBitmap(Integer.highestOneBit(right)*2, Integer.highestOneBit(bottom)*2, Bitmap.Config.ARGB_4444);
			bitmap.eraseColor(0x00000000);

			canvas.setBitmap(bitmap);
			canvas.drawText(text, (size/10f), size, painter);

			//paint inner text
			painter.setARGB(0xff, 0xff, 0xff, 0xff);
			painter.setStyle(Paint.Style.FILL);

			canvas.drawText(text, (size/10f), size, painter);

			texture = new SmartTexture(bitmap, Texture.NEAREST, Texture.CLAMP, true);

			RectF rect = texture.uvRect(0, 0, right, bottom);
			frame(rect);

			cache = new CachedText();
			cache.rect = rect;
			cache.texture = texture;
			cache.length = text.length();
			cache.activeTexts = new HashSet<>();
			cache.activeTexts.add(this);
			textCache.put("text:" + size + " " + text, cache);
		}
	}

	@Override
	protected void updateMatrix() {
		super.updateMatrix();
		//the y value is set at the top of the character, not at the top of accents.
		Matrix.translate( matrix, 0, -Math.round((baseLine()*0.15f)/scale.y) );
	}

	@Override
	public void destroy() {
		if (cache != null)
			cache.activeTexts.remove(this);
		super.destroy();
	}

	public static void clearCache(){
		for (CachedText cached : textCache.values()){
			cached.texture.delete();
		}
		textCache.clear();
	}

	public static void reloadCache(){
		for (CachedText txt : textCache.values()){
			txt.texture.reload();
		}
	}

	public static void setFont(String asset){
		font = Typeface.createFromAsset(Game.instance.getAssets(), asset);
		clearCache();
	}

	private class CachedText{
		public SmartTexture texture;
		public RectF rect;
		public int length;
		public HashSet<RenderedText> activeTexts;
	}
}
