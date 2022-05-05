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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndBadge extends Window {
	
	private static final int MAX_WIDTH = 125;
	private static final int MARGIN = 4;
	
	public WndBadge( Badges.Badge badge, boolean unlocked ) {
		
		super();
		
		Image icon = BadgeBanner.image( badge.image );
		icon.scale.set( 2 );
		if (!unlocked) icon.brightness(0.4f);
		add( icon );

		RenderedTextBlock title = PixelScene.renderTextBlock( badge.title(), 9 );
		title.maxWidth(MAX_WIDTH - MARGIN * 2);
		title.align(RenderedTextBlock.CENTER_ALIGN);
		title.hardlight(TITLE_COLOR);
		if (!unlocked) title.hardlight( 0x888822 );
		add(title);

		String desc = badge.desc();
		String unlock = Badges.showCompletionProgress(badge);

		if (unlock != null){
			desc += unlock;
		}

		RenderedTextBlock info = PixelScene.renderTextBlock( desc, 6 );
		info.maxWidth(MAX_WIDTH - MARGIN * 2);
		info.align(RenderedTextBlock.CENTER_ALIGN);
		if (!unlocked) {
			info.hardlight( 0x888888 );
			info.setHightlighting( true, 0x888822 );
		}
		add(info);
		
		float w = Math.max( icon.width(), Math.max(title.width(), info.width()) ) + MARGIN * 2;
		
		icon.x = (w - icon.width()) / 2f;
		icon.y = MARGIN;
		PixelScene.align(icon);

		title.setPos((w - title.width()) / 2, icon.y + icon.height() + MARGIN);
		PixelScene.align(title);

		info.setPos((w - info.width()) / 2, title.bottom() + MARGIN);
		PixelScene.align(info);
		resize( (int)w, (int)(info.bottom() + MARGIN) );
		
		if (unlocked) BadgeBanner.highlight( icon, badge.image );
	}
}
