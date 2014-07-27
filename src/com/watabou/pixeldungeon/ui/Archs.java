/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.Game;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.Assets;

public class Archs extends Component {

	private static final float SCROLL_SPEED	= 20f;
	
	private SkinnedBlock arcsBg;
	private SkinnedBlock arcsFg;
	
	public boolean reversed = false;
	
	@Override
	protected void createChildren() {
		arcsBg = new SkinnedBlock( 1, 1, Assets.ARCS_BG );
		add( arcsBg );
		
		arcsFg = new SkinnedBlock( 1, 1, Assets.ARCS_FG );
		add( arcsFg );
	}
	
	@Override
	protected void layout() {
		arcsBg.size( width, height );
		arcsBg.offset( arcsBg.texture.width / 4 - (width % arcsBg.texture.width) / 2, 0 );
		
		arcsFg.size( width, height );
		arcsFg.offset( arcsFg.texture.width / 4 - (width % arcsFg.texture.width) / 2, 0 );
	}
	
	@Override
	public void update() {
		
		super.update();
		
		float shift = Game.elapsed * SCROLL_SPEED;
		if (reversed) {
			shift = -shift;
		}
		
		arcsBg.offset( 0, shift );
		arcsFg.offset( 0, shift * 2 );
	}
}
