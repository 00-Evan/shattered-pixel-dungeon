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

package com.watabou.glwrap;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class ScreenConfigChooser implements GLSurfaceView.EGLConfigChooser {
	
	//array of corresponding EGL attributes for each array index
	private int[] attribEGLconsts = new int[]{
			EGL10.EGL_RED_SIZE,
			EGL10.EGL_GREEN_SIZE,
			EGL10.EGL_BLUE_SIZE,
			EGL10.EGL_ALPHA_SIZE,
			EGL10.EGL_DEPTH_SIZE,
			EGL10.EGL_STENCIL_SIZE
	};
	
	private int[] desiredAttribVals = new int[attribEGLconsts.length]; //desired attribute values
	private int[] attribPrefs = new int[attribEGLconsts.length]; //attribute preferences types
	private int[] prefWeights = new int[attribEGLconsts.length]; //weights for preferences
	
	//attributes with this preference are ignored
	public static final int DONT_CARE = 0;

	//attributes with this preference must be present in the config at exactly the given value
	public static final int EXACTLY = 1;

	//attributes with this preference must be present in the config with at least the given value
	// In the case of multiple valid configs, chooser will prefer higher values for these attributes
	public static final int PREF_LOW = 2;

	//attributes with this preference must be present in the config with at least the given value
	// In the case of multiple valid configs, chooser will prefer lower values for these attributes
	public static final int PREF_HIGH = 3;


	private EGL10 egl;
	private EGLDisplay display;

	public ScreenConfigChooser(){
		this( false );
	}

	public ScreenConfigChooser( boolean depth ){
		this( false, depth );
	}

	//helper constructor for a basic config with or without depth
	//and whether or not to prefer RGB565 for performance reasons
	//On many devices RGB565 gives slightly better performance for a minimal quality tradeoff.
	public ScreenConfigChooser( boolean prefRGB565, boolean depth ){
		this(
				new int[]{     5    ,     6    ,     5    ,    0    , depth ? 16 : 0,     0    } ,
				prefRGB565 ?
				new int[]{ PREF_LOW , PREF_LOW , PREF_LOW , EXACTLY ,   PREF_LOW    , PREF_LOW } :
				new int[]{ PREF_HIGH, PREF_HIGH, PREF_HIGH, EXACTLY ,   PREF_LOW    , PREF_LOW },
				new int[]{     2    ,     2    ,     2    ,    1    ,      1        ,     1    }
		);
	}

	public ScreenConfigChooser( int[] vals, int[] prefs, int[] weights){
		if (vals.length != desiredAttribVals.length
				|| prefs.length != attribPrefs.length
				|| weights.length != prefWeights.length)
			throw new IllegalArgumentException("incorrect array lengths!");

		desiredAttribVals = vals;
		attribPrefs = prefs;
		prefWeights = weights;
	}

	private int[] eglPrefs = new int[]{
			EGL10.EGL_RENDERABLE_TYPE, 4, //same as EGL_OPENGL_ES2_BIT. config must support GLES 2.0
			EGL10.EGL_SURFACE_TYPE, EGL10.EGL_WINDOW_BIT,
			EGL10.EGL_NONE
	};

	@Override
	public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {

		this.egl = egl;
		this.display = display;

		int[] num = new int[1];
		if (!egl.eglChooseConfig(display, eglPrefs, null, 0, num)) {
			throw new IllegalArgumentException("eglChooseConfig failed");
		}

		EGLConfig[] configs = new EGLConfig[num[0]];
		if (!egl.eglChooseConfig(display, eglPrefs, configs, num[0], num)) {
			throw new IllegalArgumentException("eglChooseConfig failed");
		}

		EGLConfig config = chooseConfig(configs);
		if (config == null) {
			throw new IllegalArgumentException("No config chosen");
		}
		return config;

	}

	private EGLConfig chooseConfig( EGLConfig[] configs ){
		EGLConfig bestConfig = null;
		int bestConfigValue = Integer.MIN_VALUE;
		for (EGLConfig curConfig : configs){

			int curConfigValue = 0;

			for (int i = 0; i < attribEGLconsts.length; i++){
				int val = findConfigAttrib(curConfig, attribEGLconsts[i]);

				if (attribPrefs[i] == EXACTLY) {
					
					if (desiredAttribVals[i] != val) {
						curConfigValue = Integer.MIN_VALUE;
						break;
					}
					
				} else if (attribPrefs[i] == PREF_HIGH) {
					
					if (desiredAttribVals[i] > val) {
						curConfigValue = Integer.MIN_VALUE;
						break;
					} else {
						curConfigValue += prefWeights[i]*(val - desiredAttribVals[i]);
					}
					
				} else if (attribPrefs[i] == PREF_LOW) {
					
					if (desiredAttribVals[i] > val) {
						curConfigValue = Integer.MIN_VALUE;
						break;
					} else {
						curConfigValue -= prefWeights[i]*(val - desiredAttribVals[i]);
					}
					
				}
			}
			
			if (curConfigValue > bestConfigValue){
				bestConfigValue = curConfigValue;
				bestConfig = curConfig;
			}

		}
		return bestConfig;
	}

	private int[] value = new int[1];

	private int findConfigAttrib(EGLConfig config, int attribute) {

		if (egl.eglGetConfigAttrib(display, config, attribute, value)) {
			return value[0];
		} else {
			throw new IllegalArgumentException("eglGetConfigAttrib failed");
		}
	}
}
