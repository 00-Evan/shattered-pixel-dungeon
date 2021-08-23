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

package com.watabou.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Highlighter {

	private static final Pattern HIGHLIGHTER	= Pattern.compile( "_(.*?)_" );
	private static final Pattern STRIPPER		= Pattern.compile( "[ \n]" );

	public String text;

	public boolean[] mask;

	public Highlighter( String text ) {

		String stripped = STRIPPER.matcher( text ).replaceAll( "" );
		mask = new boolean[stripped.length()];

		Matcher m = HIGHLIGHTER.matcher( stripped );

		int pos = 0;
		int lastMatch = 0;

		while (m.find()) {
			pos += (m.start() - lastMatch);
			int groupLen = m.group( 1 ).length();
			for (int i=pos; i < pos + groupLen; i++) {
				mask[i] = true;
			}
			pos += groupLen;
			lastMatch = m.end();
		}

		m.reset( text );
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement( sb, m.group( 1 ) );
		}
		m.appendTail( sb );

		this.text = sb.toString();
	}

	public boolean[] inverted() {
		boolean[] result = new boolean[mask.length];
		for (int i=0; i < result.length; i++) {
			result[i] = !mask[i];
		}
		return result;
	}

	public boolean isHighlighted() {
		for (int i=0; i < mask.length; i++) {
			if (mask[i]) {
				return true;
			}
		}
		return false;
	}
}