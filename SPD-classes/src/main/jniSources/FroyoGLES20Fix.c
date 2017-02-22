/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

#include <jni.h>
#include <GLES2/gl2.h>

void Java_com_watabou_glwrap_FroyoGLES20Fix_glVertexAttribPointer
        (JNIEnv *env, jclass c, jint index, jint size, jint type, jboolean normalized, jint stride, jint offset)
{
    glVertexAttribPointer(index, size, type, normalized, stride, (const void*)offset);
}

void Java_com_watabou_glwrap_FroyoGLES20Fix_glDrawElements
        (JNIEnv *env, jclass c, jint mode, jint count, jint type, jint offset)
{
    glDrawElements(mode, count, type, (const void*)offset);
}
