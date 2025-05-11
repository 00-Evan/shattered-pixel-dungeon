/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CircleArc;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends WndTitledMessage {
	
	public WndInfoMob( Mob mob ) {

		super( new MobTitle( mob ), mob.info() );
		
	}
	
	private static class MobTitle extends Component {

		private static final int GAP	= 2;
		
		private CharSprite image;
		private RenderedTextBlock name;
		private HealthBar health;
		private BuffIndicator buffs;
		private CircleArc counter;
		
		public MobTitle( Mob mob ) {
			
			name = PixelScene.renderTextBlock( Messages.titleCase( mob.name() ), 9 );
			name.hardlight( TITLE_COLOR );
			add( name );
			
			image = mob.sprite();
			add( image );

			health = new HealthBar();
			health.level(mob);
			add( health );

			buffs = new BuffIndicator( mob, false );
			add( buffs );

			counter = new CircleArc(18, 3.5f);
			counter.color( mob.cooldown() >= 1f ? 0xFF0000 : 0x808080, true );

			float cd = mob.cooldown();
			if(Math.abs(cd % 1) < 0.01) cd = Math.round(cd);
			float partial = (1f-cd%1f)%1f;

			counter.setSweep(partial);
			add(counter);
		}
		
		@Override
		protected void layout() {
			
			image.x = 0;
			image.y = Math.max( 0, name.height() + health.height() - image.height() );

			counter.x = width() - 3.5f - GAP;
			counter.y = Math.max( 0, name.height() + health.height() - 3.5f );

			float w = width - image.width() - GAP;
			int extraBuffSpace = 0;

			//Tries to make space for up to 10 visible buffs
			do {
				name.maxWidth((int)w - extraBuffSpace);
				buffs.setSize(w - name.width() - 3.5f - 14, 8);
				extraBuffSpace += 8;
			} while (extraBuffSpace <= 48 && !buffs.allBuffsVisible());

			name.setPos(x + image.width() + GAP,
					image.height() > name.height() ? y +(image.height() - name.height()) / 2 : y);

			health.setRect(image.width() + GAP, name.bottom() + GAP, w, health.height());

			buffs.setPos(name.right(), name.bottom() - BuffIndicator.SIZE_SMALL-2);

			height = Math.max(image.y + image.height(), health.bottom());
		}
	}
}
