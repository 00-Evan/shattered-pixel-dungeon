/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import com.badlogic.gdx.Gdx;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Vertexbuffer {

	private int id;
	private FloatBuffer vertices;
	private int updateStart, updateEnd;

	private static final ArrayList<Vertexbuffer> buffers = new ArrayList<>();

	public Vertexbuffer( FloatBuffer vertices ) {
		synchronized (buffers) {
			id = Gdx.gl.glGenBuffer();

			this.vertices = vertices;
			buffers.add(this);

			updateStart = 0;
			updateEnd = vertices.limit();
		}
	}

	//For flagging the buffer for a full update without changing anything
	public void updateVertices(){
		updateVertices(vertices);
	}

	//For flagging an update with a full set of new data
	public void updateVertices( FloatBuffer vertices ){
		updateVertices(vertices, 0, vertices.limit());
	}

	//For flagging an update with a subset of data changed
	public void updateVertices( FloatBuffer vertices, int start, int end){
		this.vertices = vertices;

		if (updateStart == -1)
			updateStart = start;
		else
			updateStart = Math.min(start, updateStart);

		if (updateEnd == -1)
			updateEnd = end;
		else
			updateEnd = Math.max(end, updateEnd);
	}

	public void updateGLData(){
		if (updateStart == -1) return;

		((Buffer)vertices).position(updateStart);
		bind();

		if (updateStart == 0 && updateEnd == vertices.limit()){
			Gdx.gl.glBufferData(Gdx.gl.GL_ARRAY_BUFFER, vertices.limit()*4, vertices, Gdx.gl.GL_DYNAMIC_DRAW);
		} else {
			Gdx.gl.glBufferSubData(Gdx.gl.GL_ARRAY_BUFFER, updateStart*4, (updateEnd - updateStart)*4, vertices);
		}

		release();
		updateStart = updateEnd = -1;
	}

	public void bind(){
		Gdx.gl.glBindBuffer(Gdx.gl.GL_ARRAY_BUFFER, id);
	}

	public void release(){
		Gdx.gl.glBindBuffer(Gdx.gl.GL_ARRAY_BUFFER, 0);
	}

	public void delete(){
		synchronized (buffers) {
			Gdx.gl.glDeleteBuffer( id );
			buffers.remove(this);
		}
	}

	public static void clear(){
		synchronized (buffers) {
			for (Vertexbuffer buf : buffers.toArray(new Vertexbuffer[0])) {
				buf.delete();
			}
		}
	}

	public static void reload(){
		synchronized (buffers) {
			for (Vertexbuffer buf : buffers) {
				buf.updateVertices();
				buf.updateGLData();
			}
		}
	}

}
