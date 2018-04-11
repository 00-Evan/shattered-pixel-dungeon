/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.watabou.glscripts.Script;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.glwrap.ScreenConfigChooser;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.input.Keys;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BitmapCache;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.SystemTime;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Game extends Activity implements GLSurfaceView.Renderer, View.OnTouchListener {

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
	protected Class<? extends Scene> sceneClass;
	
	// Current time in milliseconds
	protected long now;
	// Milliseconds passed since previous update
	protected long step;
	
	public static float timeScale = 1f;
	public static float elapsed = 0f;
	public static float timeTotal = 0f;
	
	protected GLSurfaceView view;
	protected SurfaceHolder holder;
	
	// Accumulated touch events
	protected ArrayList<MotionEvent> motionEvents = new ArrayList<MotionEvent>();
	
	// Accumulated key events
	protected ArrayList<KeyEvent> keysEvents = new ArrayList<KeyEvent>();
	
	public Game( Class<? extends Scene> c ) {
		super();
		sceneClass = c;
	}
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		BitmapCache.context = TextureCache.context = instance = this;
		
		DisplayMetrics m = new DisplayMetrics();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
			getWindowManager().getDefaultDisplay().getRealMetrics( m );
		else
			getWindowManager().getDefaultDisplay().getMetrics( m );
		density = m.density;
		dispHeight = m.heightPixels;
		dispWidth = m.widthPixels;
		
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
		
		setVolumeControlStream( AudioManager.STREAM_MUSIC );
		
		view = new GLSurfaceView( this );
		view.setEGLContextClientVersion( 2 );

		//Older devices are forced to RGB 565 for performance reasons.
		//Otherwise try to use RGB888 for best quality, but use RGB565 if it is what's available.
		view.setEGLConfigChooser( new ScreenConfigChooser(
				DeviceCompat.legacyDevice(),
						false ));

		view.setRenderer( this );
		view.setOnTouchListener( this );
		setContentView( view );
		
		//so first call to onstart/onresume calls correct logic.
		paused = true;
	}
	
	private boolean paused;
	
	//Starting with honeycomb, android's lifecycle management changes slightly
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			resumeGame();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (scene != null) {
			scene.onResume();
		}
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			resumeGame();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (scene != null) {
			scene.onPause();
		}
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			pauseGame();
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			pauseGame();
		}
	}
	
	public void pauseGame(){
		if (paused) return;
		
		paused = true;
		view.onPause();
		Script.reset();
		
		Music.INSTANCE.pause();
		Sample.INSTANCE.pause();
	}
	
	public void resumeGame(){
		if (!paused) return;
		
		now = 0;
		paused = false;
		view.onResume();
		
		Music.INSTANCE.resume();
		Sample.INSTANCE.resume();
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		destroyGame();
		
		Music.INSTANCE.mute();
		Sample.INSTANCE.reset();
	}

	@SuppressLint({ "Recycle", "ClickableViewAccessibility" })
	@Override
	public boolean onTouch( View view, MotionEvent event ) {
		synchronized (motionEvents) {
			motionEvents.add( MotionEvent.obtain( event ) );
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown( int keyCode, KeyEvent event ) {
		
		if (keyCode != Keys.BACK &&
				keyCode != Keys.MENU) {
			return false;
		}
		
		synchronized (motionEvents) {
			keysEvents.add( event );
		}
		return true;
	}
	
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ) {

		if (keyCode != Keys.BACK &&
				keyCode != Keys.MENU) {
			return false;
		}
		
		synchronized (motionEvents) {
			keysEvents.add( event );
		}
		return true;
	}
	
	@Override
	public void onDrawFrame( GL10 gl ) {
		
		if (width == 0 || height == 0) {
			return;
		}
		
		NoosaScript.get().resetCamera();
		NoosaScriptNoLighting.get().resetCamera();
		GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		draw();
		
		GLES20.glFlush();
		
		SystemTime.tick();
		long rightNow = SystemClock.elapsedRealtime();
		step = (now == 0 ? 0 : rightNow - now);
		now = rightNow;
		
		step();
	}

	@Override
	public void onSurfaceChanged( GL10 gl, int width, int height ) {

		GLES20.glViewport(0, 0, width, height);

		if (height != Game.height || width != Game.width) {

			Game.width = width;
			Game.height = height;

			resetScene();
		}
	}

	@Override
	public void onSurfaceCreated( GL10 gl, EGLConfig config ) {
		Blending.useDefault();

		//refreshes texture and vertex data stored on the gpu
		TextureCache.reload();
		RenderedText.reloadCache();
		Vertexbuffer.refreshAllBuffers();
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

		synchronized (motionEvents) {
			Touchscreen.processTouchEvents( motionEvents );
			motionEvents.clear();
		}
		synchronized (keysEvents) {
			Keys.processTouchEvents( keysEvents );
			keysEvents.clear();
		}
		
		scene.update();
		Camera.updateAll();
	}
	
	public static void reportException( Throwable tr ) {
		if (instance != null) instance.logException(tr);
	}
	
	protected void logException( Throwable tr ){
		Log.e("GAME", Log.getStackTraceString(tr));
	}
	
	public static void vibrate( int milliseconds ) {
		((Vibrator)instance.getSystemService( VIBRATOR_SERVICE )).vibrate( milliseconds );
	}

	public interface SceneChangeCallback{
		void beforeCreate();
		void afterCreate();
	}
}
