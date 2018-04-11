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

//This class exists because the back-end OpenGL implementation (written in C)
// supports VBO operations (along with the rest of GLES 2.0) since android 2.2 (Froyo, api 8),
// but for some reason the Java calls for these methods were only added in 2.3 (Gingerbread, api 9)

//So this class is here specifically to reference an armabi/x86 compiled binary
// which gives us Java hooks for VBOs on android 2.2

//...I don't know what google engineer forgot to put the java hooks in android 2.2 back in 2010,
// but you know who you are and this class is your fault.

//Compiled binaries for the FroyoGLES20Fix lib are included in this project, which means that
// the android NDK is not required for building Shattered Pixel Dungeon.
// see SPD-classes/src/main/jniSources/README.txt for more details.

//DO NOT REFERENCE THIS CLASS ON DEVICES API 9 AND ABOVE, use android.opengl.GLES20 instead.
@SuppressWarnings("JniMissingFunction")
public class FroyoGLES20Fix {

	static
	{
		System.loadLibrary("FroyoGLES20Fix");
	}

	native public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset);

	native public static void glDrawElements(int mode, int count, int type, int offset);

}
