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

package com.watabou.glscripts;

import com.watabou.glwrap.Program;
import com.watabou.glwrap.Shader;
import com.watabou.utils.Reflection;

import java.util.HashMap;

public class Script extends Program {

	private static final HashMap<Class<? extends Script>,Script> all =
			new HashMap<>();
	
	private static Script curScript = null;
	private static Class<? extends Script> curScriptClass = null;
	
	@SuppressWarnings("unchecked")
	public synchronized static<T extends Script> T use( Class<T> c ) {

		if (c != curScriptClass) {
			
			Script script = all.get( c );
			if (script == null) {
				script = Reflection.newInstance( c );
				all.put( c, script );
			}

			curScript = script;
			curScriptClass = c;
			curScript.use();

		}
		
		return (T)curScript;
	}

	public synchronized static void unuse(){
		curScript = null;
		curScriptClass = null;
	}
	
	public synchronized static void reset() {
		for (Script script:all.values()) {
			script.delete();
		}
		all.clear();
		
		curScript = null;
		curScriptClass = null;
	}
	
	public void compile( String src ) {

		String[] srcShaders = src.split( "//\n" );
		attach( Shader.createCompiled( Shader.VERTEX, srcShaders[0] ) );
		attach( Shader.createCompiled( Shader.FRAGMENT, srcShaders[1] ) );
		link();

	}

}
