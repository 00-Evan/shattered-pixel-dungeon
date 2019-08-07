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

package com.watabou.noosa;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.watabou.glscripts.Script;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.input.InputHandler;
import com.watabou.input.KeyEvent;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.SystemTime;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Game implements ApplicationListener {

	public static Game instance;

	//actual size of the display
	public static int dispWidth;
	public static int dispHeight;
	
	// Size of the EGL surface view
	public static int width;
	public static int height;
	
	// Density: mdpi=1, hdpi=1.5, xhdpi=2...
	public static float density = 1;
	
	public static String version;
	public static int versionCode;
	
	// Current scene
	protected Scene scene;
	// New scene we are going to switch to
	protected Scene requestedScene;
	// true if scene switch is requested
	protected boolean requestedReset = true;
	// callback to perform logic during scene change
	protected SceneChangeCallback onChange;
	// New scene class
	protected static Class<? extends Scene> sceneClass;
	
	// Current time in milliseconds
	protected long now;
	// Milliseconds passed since previous update
	protected long step;
	
	public static float timeScale = 1f;
	public static float elapsed = 0f;
	public static float timeTotal = 0f;
	
	protected static InputHandler inputHandler;
	
	protected static PlatformSupport platform;
	
	public Game(Class<? extends Scene> c, PlatformSupport platform) {
		sceneClass = c;
		
		instance = this;
		this.platform = platform;
	}
	
	private boolean paused;
	
	public boolean isPaused(){
		return paused;
	}
	
	@Override
	public void create() {
		density = Gdx.graphics.getDensity();
		dispHeight = Gdx.graphics.getDisplayMode().height;
		dispWidth = Gdx.graphics.getDisplayMode().width;
		
		Blending.useDefault();
		
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);
		Gdx.input.setCatchKey(KeyEvent.BACK, true);
		Gdx.input.setCatchKey(KeyEvent.MENU, true);
		
		//refreshes texture and vertex data stored on the gpu
		TextureCache.reload();
		RenderedText.reloadCache();
		Vertexbuffer.refreshAllBuffers();
	}
	
	@Override
	public void resize(int width, int height) {
		Gdx.gl.glViewport(0, 0, width, height);
		
		if (height != Game.height || width != Game.width) {
			
			Game.width = width;
			Game.height = height;
			
			resetScene();
		}
	}
	
	@Override
	public void render() {
		NoosaScript.get().resetCamera();
		NoosaScriptNoLighting.get().resetCamera();
		Gdx.gl.glDisable(Gdx.gl.GL_SCISSOR_TEST);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
		draw();
		
		Gdx.gl.glFlush();
		
		SystemTime.tick();
		long rightNow = SystemTime.now;
		step = (now == 0 ? 0 : rightNow - now);
		now = rightNow;
		
		step();
	}
	
	@Override
	public void pause() {
		paused = true;
		
		if (scene != null) {
			scene.onPause();
		}
		
		Script.reset();
	}
	
	@Override
	public void resume() {
		paused = false;
		
		now = 0;
	}
	
	public void finish(){
		Gdx.app.exit();
	}
	
	@Override
	public void dispose() {
		if (scene != null) {
			scene.destroy();
			scene = null;
		}
		
		sceneClass = null;
		Music.INSTANCE.stop();
		Sample.INSTANCE.reset();
	}
	
	public static void resetScene() {
		switchScene( instance.sceneClass );
	}

	public static void switchScene(Class<? extends Scene> c) {
		switchScene(c, null);
	}
	
	public static void switchScene(Class<? extends Scene> c, SceneChangeCallback callback) {
		instance.sceneClass = c;
		instance.requestedReset = true;
		instance.onChange = callback;
	}
	
	public static Scene scene() {
		return instance.scene;
	}
	
	protected void step() {
		
		if (requestedReset) {
			requestedReset = false;

			try {
				requestedScene = sceneClass.newInstance();
				switchScene();
			} catch (InstantiationException e){
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		
		update();
	}
	
	protected void draw() {
		if (scene != null) scene.draw();
	}
	
	protected void switchScene() {

		Camera.reset();
		
		if (scene != null) {
			scene.destroy();
		}
		scene = requestedScene;
		if (onChange != null) onChange.beforeCreate();
		scene.create();
		if (onChange != null) onChange.afterCreate();
		onChange = null;
		
		Game.elapsed = 0f;
		Game.timeScale = 1f;
		Game.timeTotal = 0f;
	}

	protected void update() {
		Game.elapsed = Game.timeScale * step * 0.001f;
		Game.timeTotal += Game.elapsed;

		inputHandler.processAllEvents();
		
		scene.update();
		Camera.updateAll();
	}
	
	public static void reportException( Throwable tr ) {
		if (instance != null) instance.logException(tr);
	}
	
	protected void logException( Throwable tr ){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		pw.flush();
		Gdx.app.error("GAME", sw.toString());
	}
	
	public static void vibrate( int milliseconds ) {
		Gdx.input.vibrate(milliseconds);
	}

	public interface SceneChangeCallback{
		void beforeCreate();
		void afterCreate();
	}
	
}
