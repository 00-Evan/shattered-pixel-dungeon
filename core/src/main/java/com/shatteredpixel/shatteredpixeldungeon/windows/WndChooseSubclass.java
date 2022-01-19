/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndChooseSubclass extends Window {
	
	private static final int WIDTH		= 130;
	private static final float GAP		= 2;
	
	public WndChooseSubclass(final TengusMask tome, final Hero hero ) {
		
		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tome.image(), null ) );
		titlebar.label( tome.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextBlock message = PixelScene.renderTextBlock( 6 );
		message.text( Messages.get(this, "message"), WIDTH );
		message.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( message );

		float pos = message.bottom() + 3*GAP;

		for (HeroSubClass subCls : hero.heroClass.subClasses()){
			RedButton btnCls = new RedButton( subCls.shortDesc(), 6 ) {
				@Override
				protected void onClick() {
					GameScene.show(new WndOptions(new HeroIcon(subCls),
							Messages.titleCase(subCls.title()),
							Messages.get(WndChooseSubclass.this, "are_you_sure"),
							Messages.get(WndChooseSubclass.this, "yes"),
							Messages.get(WndChooseSubclass.this, "no")){
						@Override
						protected void onSelect(int index) {
							hide();
							if (index == 0 && WndChooseSubclass.this.parent != null){
								WndChooseSubclass.this.hide();
								tome.choose( subCls );
							}
						}
					});
				}
			};
			btnCls.leftJustify = true;
			btnCls.multiline = true;
			btnCls.setSize(WIDTH-20, btnCls.reqHeight()+2);
			btnCls.setRect( 0, pos, WIDTH-20, btnCls.reqHeight()+2);
			add( btnCls );

			IconButton clsInfo = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					GameScene.show(new WndInfoSubclass(Dungeon.hero.heroClass, subCls));
				}
			};
			clsInfo.setRect(WIDTH-20, btnCls.top() + (btnCls.height()-20)/2, 20, 20);
			add(clsInfo);

			pos = btnCls.bottom() + GAP;
		}

		RedButton btnCancel = new RedButton( Messages.get(this, "cancel") ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, pos, WIDTH, 18 );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
}
