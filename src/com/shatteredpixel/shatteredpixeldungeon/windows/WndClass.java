/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Group;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;

public class WndClass extends WndTabbed {

	private static final String TXT_MASTERY	= "Mastery";

	private static final int WIDTH			= 110;

	private static final int TAB_WIDTH	= 50;

	private HeroClass cl;

	private PerksTab tabPerks;
	private MasteryTab tabMastery;

	public WndClass( HeroClass cl ) {

		super();

		this.cl = cl;

		tabPerks = new PerksTab();
		add( tabPerks );

		Tab tab = new RankingTab( Utils.capitalize( cl.title() ), tabPerks );
		tab.setSize( TAB_WIDTH, tabHeight() );
		add( tab );

		if (Badges.isUnlocked( cl.masteryBadge() )) {
			tabMastery = new MasteryTab();
			add( tabMastery );

			tab = new RankingTab( TXT_MASTERY, tabMastery );
			add( tab );

			resize(
					(int)Math.max( tabPerks.width, tabMastery.width ),
					(int)Math.max( tabPerks.height, tabMastery.height ) );
		} else {
			resize( (int)tabPerks.width, (int)tabPerks.height );
		}

		layoutTabs();

		select( 0 );
	}

	private class RankingTab extends LabeledTab {

		private Group page;

		public RankingTab( String label, Group page ) {
			super( label );
			this.page = page;
		}

		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}

	private class PerksTab extends Group {

		private static final int MARGIN	= 4;
		private static final int GAP	= 4;

		private static final String DOT	= "\u007F";

		public float height;
		public float width;

		public PerksTab() {
			super();

			float dotWidth = 0;

			String[] items = cl.perks();
			float pos = MARGIN;

			for (int i=0; i < items.length; i++) {

				if (i > 0) {
					pos += GAP;
				}

				BitmapText dot = PixelScene.createText( DOT, 6 );
				dot.x = MARGIN;
				dot.y = pos;
				if (dotWidth == 0) {
					dot.measure();
					dotWidth = dot.width();
				}
				add( dot );

				BitmapTextMultiline item = PixelScene.createMultiline( items[i], 6 );
				item.x = dot.x + dotWidth;
				item.y = pos;
				item.maxWidth = (int)(WIDTH - MARGIN * 2 - dotWidth);
				item.measure();
				add( item );

				pos += item.height();
				float w = item.width();
				if (w > width) {
					width = w;
				}
			}

			width += MARGIN + dotWidth;
			height = pos + MARGIN;
		}
	}

	private class MasteryTab extends Group {

		private static final int MARGIN	= 4;

		private BitmapTextMultiline normal;
		private BitmapTextMultiline highlighted;

		public float height;
		public float width;

		public MasteryTab() {
			super();

			String text = null;
			switch (cl) {
				case WARRIOR:
					text = HeroSubClass.GLADIATOR.desc() + "\n\n" + HeroSubClass.BERSERKER.desc();
					break;
				case MAGE:
					text = HeroSubClass.BATTLEMAGE.desc() + "\n\n" + HeroSubClass.WARLOCK.desc();
					break;
				case ROGUE:
					text = HeroSubClass.FREERUNNER.desc() + "\n\n" + HeroSubClass.ASSASSIN.desc();
					break;
				case HUNTRESS:
					text = HeroSubClass.SNIPER.desc() + "\n\n" + HeroSubClass.WARDEN.desc();
					break;
			}

			Highlighter hl = new Highlighter( text );

			normal = PixelScene.createMultiline( hl.text, 6 );
			normal.maxWidth = WIDTH - MARGIN * 2;
			normal.measure();
			normal.x = MARGIN;
			normal.y = MARGIN;
			add( normal );

			if (hl.isHighlighted()) {
				normal.mask = hl.inverted();

				highlighted = PixelScene.createMultiline( hl.text, 6 );
				highlighted.maxWidth = normal.maxWidth;
				highlighted.measure();
				highlighted.x = normal.x;
				highlighted.y = normal.y;
				add( highlighted );

				highlighted.mask = hl.mask;
				highlighted.hardlight( TITLE_COLOR );
			}

			height = normal.y + normal.height() + MARGIN;
			width = normal.x + normal.width() + MARGIN;
		}
	}
}