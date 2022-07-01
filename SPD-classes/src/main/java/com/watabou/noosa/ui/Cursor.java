/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.watabou.noosa.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PointF;

public class Cursor {

	private static com.badlogic.gdx.graphics.Cursor currentCursor;

	public enum Type {

		//TODO if we ever add more cursors, should cache their pixmaps rather than always remaking
		DEFAULT("gdx/cursor_mouse.png"),
		CONTROLLER("gdx/cursor_controller.png");

		public final String file;

		Type(String file){
			this.file = file;
		}

	}

	private static Type lastType;
	private static int lastZoom;

	public static void setCustomCursor(Type type, int zoom){

		if (currentCursor != null){
			if (lastType == type && lastZoom == zoom){
				return;
			}

			currentCursor.dispose();
			currentCursor = null;
		}

		Pixmap cursorImg = new Pixmap(FileUtils.getFileHandle(Files.FileType.Internal, type.file));

		int scaledWidth = cursorImg.getWidth()*zoom;
		int width2 = 2;
		while (width2 < scaledWidth) {
			width2 <<= 1;
		}

		int scaledHeight = cursorImg.getHeight()*zoom;
		int height2 = 2;
		while (height2 < scaledHeight) {
			height2 <<= 1;
		}

		Pixmap scaledImg = new Pixmap(width2, height2, cursorImg.getFormat());
		scaledImg.setFilter(Pixmap.Filter.NearestNeighbour);
		scaledImg.drawPixmap(cursorImg, 0, 0, cursorImg.getWidth(), cursorImg.getHeight(), 0, 0, scaledWidth, scaledHeight);

		currentCursor = Gdx.graphics.newCursor(scaledImg, 0, 0);
		Gdx.graphics.setCursor(currentCursor);
		scaledImg.dispose();
		cursorImg.dispose();

		lastType = type;
		lastZoom = zoom;

	}

	private static boolean cursorCaptured = false;

	public static void captureCursor(boolean captured){
		cursorCaptured = captured;

		if (captured) {
			Gdx.input.setCursorCatched(true);
		} else {
			if (ControllerHandler.controllerPointerActive()) {
				ControllerHandler.setControllerPointer(true);
				ControllerHandler.updateControllerPointer(new PointF(Game.width/2, Game.height/2), false);
			} else {
				Gdx.input.setCursorCatched(false);
				Gdx.input.setCursorPosition(Game.width/2, Game.height/2);
			}
		}
	}

	public static PointF getCursorDelta(){
		return new PointF(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
	}

	public static boolean isCursorCaptured(){
		return cursorCaptured;
	}

}
