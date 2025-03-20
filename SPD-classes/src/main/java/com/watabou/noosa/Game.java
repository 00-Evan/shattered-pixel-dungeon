/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.glutils.GLVersion;
import com.badlogic.gdx.utils.TimeUtils;
import com.watabou.glscripts.Script;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.input.ControllerHandler;
import com.watabou.input.InputHandler;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.Reflection;

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

	//number of pixels from bottom of view before rendering starts
	public static int bottomInset;

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
	
	public static float timeScale = 1f;
	public static float elapsed = 0f;
	public static float timeTotal = 0f;
	public static long realTime = 0;

	public static InputHandler inputHandler;
	
	public static PlatformSupport platform;
	
	public Game(Class<? extends Scene> c, PlatformSupport platform) {
		sceneClass = c;
		
		instance = this;
		this.platform = platform;
	}
	
	@Override
	public void create() {
		dispHeight = Gdx.graphics.getDisplayMode().height;
		dispWidth = Gdx.graphics.getDisplayMode().width;

		density = Gdx.graphics.getDensity();
		if (density == Float.POSITIVE_INFINITY){
			density = 100f / 160f; //assume 100PPI if density can't be found
		} else if (DeviceCompat.isDesktop()) {
			float PpiX = Gdx.graphics.getPpiX();
			float PpiY = Gdx.graphics.getPpiY();

			//this exists because Steam deck reports its display size as 4"x6.3" for some reason
			// as if in portrait, instead of 6.3"x4". This results in incorrect PPI measurements.
			// So when the PPIs differ, we assume reported display size is flipped and adjust
			if (PpiX / PpiY > 1.1f || PpiX / PpiY < 0.9f ){
				float reportedDisplayHeightInches = dispHeight / PpiY; //it's actually the width
				float realPpiX = dispWidth / reportedDisplayHeightInches;
				density = realPpiX / 160f;
			}
		}

		inputHandler = new InputHandler( Gdx.input );
		if (ControllerHandler.controllersSupported()){
			Controllers.addListener(new ControllerHandler());
		}

		//refreshes texture and vertex data stored on the gpu
		versionContextRef = Gdx.graphics.getGLVersion();
		Blending.useDefault();
		TextureCache.reload();
		Vertexbuffer.reload();
	}

	private GLVersion versionContextRef;
	
	@Override
	public void resize(int width, int height) {
		if (width == 0 || height == 0){
			return;
		}

		//If the EGL context was destroyed, we need to refresh some data stored on the GPU.
		// This checks that by seeing if GLVersion has a new object reference
		if (versionContextRef != Gdx.graphics.getGLVersion()) {
			versionContextRef = Gdx.graphics.getGLVersion();
			Blending.useDefault();
			TextureCache.reload();
			Vertexbuffer.reload();
		}

		height -= bottomInset;
		if (height != Game.height || width != Game.width) {

			Game.width = width;
			Game.height = height;
			
			//TODO might be better to put this in platform support
			if (Gdx.app.getType() != Application.ApplicationType.Android){
				Game.dispWidth = Game.width;
				Game.dispHeight = Game.height;
			}
			
			resetScene();
		}
	}

	//justResumed is a bit of a hack to improve start time metrics on Android,
	// as texture refreshing leads to slow warm starts. TODO would be nice to fix this properly
	private boolean justResumed = true;

	@Override
	public void render() {
		//prevents weird rare cases where the app is running twice
		if (instance != this){
			finish();
			return;
		}

		if (justResumed){
			justResumed = false;
			if (DeviceCompat.isAndroid()) return;
		}

		NoosaScript.get().resetCamera();
		NoosaScriptNoLighting.get().resetCamera();
		Gdx.gl.glDisable(Gdx.gl.GL_SCISSOR_TEST);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
		draw();

		Gdx.gl.glDisable( Gdx.gl.GL_SCISSOR_TEST );
		
		step();
	}
	
	@Override
	public void pause() {
		if (scene != null) {
			scene.onPause();
		}
		
		Script.reset();
	}
	
	@Override
	public void resume() {
		justResumed = true;
	}
	
	public void finish(){
		Gdx.app.exit();
		
	}
	
	public void destroy(){
		if (scene != null) {
			scene.destroy();
			scene = null;
		}
		
		sceneClass = null;
		Music.INSTANCE.stop();
		Sample.INSTANCE.reset();
	}
	
	@Override
	public void dispose() {
		destroy();
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

	public static boolean switchingScene() {
		return instance.requestedReset;
	}
	
	protected void step() {
		
		if (requestedReset) {
			requestedReset = false;
			
			requestedScene = Reflection.newInstance(sceneClass);
			if (requestedScene != null){
				switchScene();
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
		//clear any leftover vertex buffers
		Vertexbuffer.clear();
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
		Game.elapsed = Game.timeScale * Gdx.graphics.getDeltaTime();
		Game.timeTotal += Game.elapsed;
		
		Game.realTime = TimeUtils.millis();

		inputHandler.processAllEvents();

		Music.INSTANCE.update();
		Sample.INSTANCE.update();
		scene.update();
		Camera.updateAll();
	}
	
	public static void reportException( Throwable tr ) {
		if (instance != null && Gdx.app != null) {
			instance.logException(tr);
		} else {
			//fallback if error happened in initialization
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			tr.printStackTrace(pw);
			pw.flush();
			System.err.println(sw.toString());
		}
	}
	
	protected void logException( Throwable tr ){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		pw.flush();
		Gdx.app.error("GAME", sw.toString());
	}
	
	public static void runOnRenderThread(Callback c){
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				c.call();
			}
		});
	}
	
	public static void vibrate( int milliseconds ) {
		if (platform.supportsVibration()) {
			platform.vibrate(milliseconds);
		}
	}

	public interface SceneChangeCallback{
		void beforeCreate();
		void afterCreate();
	}
	
}
