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

import android.content.pm.PackageManager.NameNotFoundException;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.watabou.glscripts.Script;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.input.InputHandler;
import com.watabou.input.KeyEvent;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.SystemTime;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class Game extends AndroidApplication implements ApplicationListener {

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
	
	protected GLSurfaceView view;
	//protected SurfaceHolder holder;
	
	protected InputHandler inputHandler;
	
	public Game( Class<? extends Scene> c ) {
		super();
		sceneClass = c;
	}
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		instance = this;

		//FIXME this should be moved into a separate class, once we start to move to multiplatform
		try {
			version = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionName;
		} catch (NameNotFoundException e) {
			version = "???";
		}
		try {
			versionCode = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionCode;
		} catch (NameNotFoundException e) {
			versionCode = 0;
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.depth = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			//use rgb888 on more modern devices for better visuals
			config.r = config.g = config.b = 8;
		} else {
			//and rgb565 (default) on older ones for better performance
		}
		
		config.useCompass = false;
		config.useAccelerometer = false;
		//TODO consider the following additional options, might be better than setting manually
		//config.hideStatusBar
		//config.useImmersiveMode
		
		initialize(this, config);
		
		//FIXME shouldn't have a reference to the view here, remove things which access this
		view = (GLSurfaceView)graphics.getView();
		
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);
		Gdx.input.setCatchKey(KeyEvent.BACK, true);
		Gdx.input.setCatchKey(KeyEvent.MENU, true);
		
		//FIXME this doesn't seem to work quite right. That might not be due to LibGDX though.
		Music.setMuteListener();
		
		//so first call to onstart/onresume calls correct logic.
		paused = true;
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
		long rightNow = SystemClock.elapsedRealtime();
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
		
		//view.onPause();
		Script.reset();
		
		//Music.INSTANCE.pause();
		//Sample.INSTANCE.pause();
	}
	
	@Override
	public void resume() {
		paused = false;
		
		now = 0;
		//view.onResume();
		
		//Music.INSTANCE.resume();
		//Sample.INSTANCE.resume();
	}
	
	@Override
	public void dispose() {
		destroyGame();
		
		//Music.INSTANCE.mute();
		//Sample.INSTANCE.reset();
	}
	
	protected void destroyGame() {
		if (scene != null) {
			scene.destroy();
			scene = null;
		}
		
		//instance = null;
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
