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

package com.watabou.glwrap;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Vertexbuffer {

	private int id;
	private FloatBuffer vertices;
	private boolean needsUpdate;

	private static ArrayList<Vertexbuffer> buffers = new ArrayList<>();

	public Vertexbuffer( FloatBuffer vertices ) {
		int[] ptr = new int[1];
		GLES20.glGenBuffers( 1, ptr, 0 );
		id = ptr[0];

		this.vertices = vertices;
		buffers.add(this);

		//forces an update so the GL data isn't blank
		needsUpdate = true;
		updateGLData();
	}

	public void updateVertices( FloatBuffer vertices ){
		this.vertices = vertices;
		needsUpdate = true;
	}

	public void updateGLData(){
		if (!needsUpdate) return;

		vertices.position(0);
		bind();
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, (vertices.limit()*4), vertices, GLES20.GL_DYNAMIC_DRAW);
		release();
		needsUpdate = false;
	}

	public void bind(){
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, id);
	}

	public void release(){
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}

	public void delete(){
		GLES20.glDeleteBuffers(1, new int[]{id}, 0);
		buffers.remove(this);
	}

	public static void refreshAllBuffers(){
		for (Vertexbuffer buf : buffers) {
			buf.needsUpdate = true;
			buf.updateGLData();
		}
	}

}
