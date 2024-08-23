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

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SupporterScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

public class WndVictoryCongrats extends Window {

	public WndVictoryCongrats(){
		int width = PixelScene.landscape() ? 180 : 120;
		int height = 0;

		IconTitle title = new IconTitle( new ItemSprite(ItemSpriteSheet.AMULET), Messages.get(this, "title"));
		title.setRect( 0, 0, width, 0 );
		add(title);

		RenderedTextBlock text = PixelScene.renderTextBlock( Messages.get(this, "start_text"), 6 );
		text.maxWidth( width );
		text.setPos( 0, title.bottom() + 4 );
		add( text );

		height = (int)text.bottom() + 6;

		Image chalImg = Icons.CHALLENGE_COLOR.get();
		chalImg.y = height;
		chalImg.x = (16-chalImg.width())/2f;
		PixelScene.align(chalImg);
		add(chalImg);

		RenderedTextBlock chalTxt = PixelScene.renderTextBlock(Messages.get(this, "challenges"), 6);
		chalTxt.maxWidth(width - 16);
		chalTxt.setPos(16, height);
		add(chalTxt);

		if (chalTxt.height() > chalImg.height()){
			chalImg.y = chalImg.y + (chalTxt.height() - chalImg.height())/2f;
			PixelScene.align(chalImg);
		}

		height += Math.max(chalImg.height(), chalTxt.height()) + 6;

		Image seedImg = new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS);
		seedImg.y = height;
		seedImg.x = (16-seedImg.width())/2f;
		PixelScene.align(seedImg);
		add(seedImg);

		RenderedTextBlock seedTxt = PixelScene.renderTextBlock(Messages.get(this, "custom_seeds"), 6);
		seedTxt.maxWidth(width - 16);
		seedTxt.setPos(16, height);
		add(seedTxt);

		if (seedTxt.height() > seedImg.height()){
			seedImg.y = seedImg.y + (seedTxt.height() - seedImg.height())/2f;
			PixelScene.align(seedImg);
		}

		height += Math.max(seedImg.height(), seedTxt.height()) + 6;

		Image dailyImg = Icons.CALENDAR.get();
		dailyImg.hardlight(0.5f, 1f, 2f);
		dailyImg.y = height;
		dailyImg.x = (16-dailyImg.width())/2f;
		PixelScene.align(dailyImg);
		add(dailyImg);

		RenderedTextBlock dailyTxt = PixelScene.renderTextBlock(Messages.get(this, "dailies"), 6);
		dailyTxt.maxWidth(width - 16);
		dailyTxt.setPos(16, height);
		add(dailyTxt);

		if (dailyTxt.height() > dailyImg.height()){
			dailyImg.y = dailyImg.y + (dailyTxt.height() - dailyImg.height())/2f;
			PixelScene.align(dailyImg);
		}

		height += Math.max(dailyImg.height(), dailyTxt.height()) + 6;

		RenderedTextBlock finalTxt = PixelScene.renderTextBlock(Messages.get(this, "thank_you") + " "  + Messages.get(this, "support_prompt"), 6);
		finalTxt.maxWidth(width);
		finalTxt.setPos(0, height);
		add(finalTxt);

		height = (int) finalTxt.bottom() + 4;

		RedButton btnSupport = new RedButton(Messages.get(this, "support")) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchScene(SupporterScene.class);
			}
		};
		btnSupport.icon(Icons.GOLD.get());
		btnSupport.setRect(0, height, width / 2, 18);
		add(btnSupport);

		RedButton btnClose = new RedButton(Messages.get(this, "close")) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnClose.icon(Icons.EXIT.get());
		btnClose.setRect(btnSupport.right() + 1, height, width / 2 - 1, 18);
		add(btnClose);

		resize(width, (int)btnClose.bottom());

	}

	@Override
	public void onBackPressed() {
		//do nothing
	}
}
