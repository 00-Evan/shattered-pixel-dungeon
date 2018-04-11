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

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Quad {

	// 0---1
	// | \ |
	// 3---2
	public static final short[] VALUES = {0, 1, 2, 0, 2, 3};
	
	public static final int SIZE = VALUES.length;
	
	private static ShortBuffer indices;
	private static int indexSize = 0;
	private static int bufferIndex = -1;
	
	public static FloatBuffer create() {
		return ByteBuffer.
			allocateDirect( 16 * Float.SIZE / 8 ).
			order( ByteOrder.nativeOrder() ).
			asFloatBuffer();
	}
	
	public static FloatBuffer createSet( int size ) {
		return ByteBuffer.
			allocateDirect( size * 16 * Float.SIZE / 8 ).
			order( ByteOrder.nativeOrder() ).
			asFloatBuffer();
	}

	//sets up for drawing up to 32k quads in one command, shouldn't ever need to exceed this
	public static void setupIndices(){
		ShortBuffer indices = getIndices( Short.MAX_VALUE );
		if (bufferIndex == -1){
			int[] buf = new int[1];
			GLES20.glGenBuffers(1, buf, 0);
			bufferIndex = buf[0];
		}
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferIndex);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, (indices.capacity()*2), indices, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public static void bindIndices(){
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferIndex);
	}

	public static void releaseIndices(){
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public static ShortBuffer getIndices( int size ) {
		
		if (size > indexSize) {
			
			indexSize = size;
			indices = ByteBuffer.
				allocateDirect( size * SIZE * Short.SIZE / 8 ).
				order( ByteOrder.nativeOrder() ).
				asShortBuffer();
			
			short[] values = new short[size * 6];
			int pos = 0;
			int limit = size * 4;
			for (int ofs=0; ofs < limit; ofs += 4) {
				values[pos++] = (short)(ofs + 0);
				values[pos++] = (short)(ofs + 1);
				values[pos++] = (short)(ofs + 2);
				values[pos++] = (short)(ofs + 0);
				values[pos++] = (short)(ofs + 2);
				values[pos++] = (short)(ofs + 3);
			}
			
			indices.put( values );
			indices.position( 0 );
		}
		
		return indices;
	}
	
	public static void fill( float[] v,
		float x1, float x2, float y1, float y2,
		float u1, float u2, float v1, float v2 ) {
		
		v[0] = x1;
		v[1] = y1;
		v[2] = u1;
		v[3] = v1;
		
		v[4] = x2;
		v[5] = y1;
		v[6] = u2;
		v[7] = v1;
		
		v[8] = x2;
		v[9] = y2;
		v[10]= u2;
		v[11]= v2;
		
		v[12]= x1;
		v[13]= y2;
		v[14]= u1;
		v[15]= v2;
	}
	
	public static void fillXY( float[] v, float x1, float x2, float y1, float y2 ) {
		
		v[0] = x1;
		v[1] = y1;
		
		v[4] = x2;
		v[5] = y1;
		
		v[8] = x2;
		v[9] = y2;
		
		v[12]= x1;
		v[13]= y2;
	}
	
	public static void fillUV( float[] v, float u1, float u2, float v1, float v2 ) {
		
		v[2] = u1;
		v[3] = v1;
		
		v[6] = u2;
		v[7] = v1;
		
		v[10]= u2;
		v[11]= v2;
		
		v[14]= u1;
		v[15]= v2;
	}
}
