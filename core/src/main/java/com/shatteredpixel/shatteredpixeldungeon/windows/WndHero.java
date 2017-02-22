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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Button;

import java.util.Locale;

public class WndHero extends WndTabbed {
	
	private static final int WIDTH		= 115;
	
	private StatsTab stats;
	private BuffsTab buffs;
	
	private SmartTexture icons;
	private TextureFilm film;
	
	public WndHero() {
		
		super();
		
		icons = TextureCache.get( Assets.BUFFS_LARGE );
		film = new TextureFilm( icons, 16, 16 );
		
		stats = new StatsTab();
		add( stats );
		
		buffs = new BuffsTab();
		add( buffs );
		
		add( new LabeledTab( Messages.get(this, "stats") ) {
			protected void select( boolean value ) {
				super.select( value );
				stats.visible = stats.active = selected;
			};
		} );
		add( new LabeledTab( Messages.get(this, "buffs") ) {
			protected void select( boolean value ) {
				super.select( value );
				buffs.visible = buffs.active = selected;
			};
		} );

		resize( WIDTH, (int)Math.max( stats.height(), buffs.height() ) );

		layoutTabs();
		
		select( 0 );
	}
	
	private class StatsTab extends Group {
		
		private static final int GAP = 5;
		
		private float pos;
		
		public StatsTab() {
			
			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar(hero.heroClass, hero.tier()) );
			if (hero.givenName().equals(hero.className()))
				title.label( Messages.get(this, "title", hero.lvl, hero.className() ).toUpperCase( Locale.ENGLISH ) );
			else
				title.label((hero.givenName() + "\n" + Messages.get(this, "title", hero.lvl, hero.className())).toUpperCase(Locale.ENGLISH));
			title.color(Window.SHPX_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add(title);

			pos = title.bottom() + 2*GAP;

			statSlot( Messages.get(this, "str"), hero.STR() );
			if (hero.SHLD > 0) statSlot( Messages.get(this, "health"), hero.HP + "+" + hero.SHLD + "/" + hero.HT );
			else statSlot( Messages.get(this, "health"), (hero.HP) + "/" + hero.HT );
			statSlot( Messages.get(this, "exp"), hero.exp + "/" + hero.maxExp() );

			pos += GAP;

			statSlot( Messages.get(this, "gold"), Statistics.goldCollected );
			statSlot( Messages.get(this, "depth"), Statistics.deepestFloor );

			pos += GAP;
		}

		private void statSlot( String label, String value ) {

			RenderedText txt = PixelScene.renderText( label, 8 );
			txt.y = pos;
			add( txt );

			txt = PixelScene.renderText( value, 8 );
			txt.x = WIDTH * 0.6f;
			txt.y = pos;
			PixelScene.align(txt);
			add( txt );
			
			pos += GAP + txt.baseLine();
		}
		
		private void statSlot( String label, int value ) {
			statSlot( label, Integer.toString( value ) );
		}
		
		public float height() {
			return pos;
		}
	}
	
	private class BuffsTab extends Group {
		
		private static final int GAP = 2;
		
		private float pos;
		
		public BuffsTab() {
			for (Buff buff : Dungeon.hero.buffs()) {
				if (buff.icon() != BuffIndicator.NONE) {
					BuffSlot slot = new BuffSlot(buff);
					slot.setRect(0, pos, WIDTH, slot.icon.height());
					add(slot);
					pos += GAP + slot.height();
				}
			}
		}
		
		public float height() {
			return pos;
		}

		private class BuffSlot extends Button{

			private Buff buff;

			Image icon;
			RenderedText txt;

			public BuffSlot( Buff buff ){
				super();
				this.buff = buff;
				int index = buff.icon();

				icon = new Image( icons );
				icon.frame( film.get( index ) );
				icon.y = this.y;
				add( icon );

				txt = PixelScene.renderText( buff.toString(), 8 );
				txt.x = icon.width + GAP;
				txt.y = this.y + (int)(icon.height - txt.baseLine()) / 2;
				add( txt );

			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.x = icon.width + GAP;
				txt.y = pos + (int)(icon.height - txt.baseLine()) / 2;
			}

			@Override
			protected void onClick() {
				GameScene.show( new WndInfoBuff( buff ));
			}
		}
	}
}
