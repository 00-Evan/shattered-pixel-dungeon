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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class ChangesScene extends PixelScene {

	private static final String TXT_Update =
			"_v0.6.0:_\n" +
			"New levelgen!\n" +
			"_-_ Level creation algorithm completely overhauled!\n" +
			"_-_ Sewers are now smaller, caves+ are now larger\n" +
			"_-_ Some rooms can now be much larger than before\n" +
			"_-_ Added 8 new standard room types,\n" +
			"\t\t and loads of new standard room layouts\n" +
			"\n"+
			"Environment Balance Changes:\n"+
			"_-_ Falling damage tweaked to be less random\n" +
			"_-_ Reduced number of traps in later chapters\n" +
			"_-_ Floor 2 entry doors only hidden for new players\n" +
			"_-_ Visiting floor 21 before completing the imp quest\n" +
			"\t\t no longer prevents his shop from spawning\n" +
			"_-_ Light sources now grant significantly more vision\n" +
			"_-_ Light from torches now lasts 20% longer\n" +
			"_-_ Slightly increased visibility on floor 22+\n" +
			"_-_ Floor 21 shop now sells 3 torches, up from 2\n" +
			"\n"+
			"Item Balance Changes:\n"+
			"_-_ Meat and small rations are 50% more filling\n" +
			"_-_ Pasties and blandfruit are 12.5% more filling\n" +
			"_-_ Greataxe base damage increased by ~22%\n" +
			"_-_ Greatshield base damage increased by ~17%\n" +
			"_-_ Vampiric enchant lifesteal reduced by 20%\n" +
			"_-_ Lucky enchant rebalanced:\n" +
			"\t\t now deals 2x/0x damage, instead of min/max\n" +
			"\t\t base chance to deal 2x increased by ~10%\n" +
			"_-_ Glyph of Viscosity rebalanced:\n" +
			"\t\t proc chance reduced by ~25% \n" +
			"\t\t damage over time reverted from 15% to 10%\n" +
			"_-_ Glyph of Entanglement root time reduced by 40%\n" +
			"_-_ Glyph of Potential rebalanced:\n" +
			"\t\t self-damage no longer scales with max hp\n" +
			"\t\t grants more charge at higher levels\n" +
			"\n"+
			"_v0.5.0:_ New visual style, shadows and depth!\n" +
			"\n"+
			"_v0.4.3:_ Various utility features and improvements\n" +
			"_v0.4.2:_ Performance and game engine improvements\n" +
			"_v0.4.1:_ Balance adjustments to enemies & armor\n" +
			"_v0.4.0:_ Reworked equips, enchants & curses\n" +
			"\n" +
			"_v0.3.5:_ Reworked Warrior & subclasses\n" +
			"_v0.3.4:_ Multiple language support\n" +
			"_v0.3.3:_ Support for Google Play Games\n" +
			"_v0.3.2:_ Prison Rework & Balance Changes\n" +
			"_v0.3.1:_ Traps reworked & UI upgrades\n" +
			"_v0.3.0:_ Wands & Mage completely reworked\n" +
			"\n" +
			"_v0.2.4:_ Small improvements and tweaks\n" +
			"_v0.2.3:_ Artifact additions & improvements\n" +
			"_v0.2.2:_ Small improvements and tweaks\n" +
			"_v0.2.1:_ Sewer improvements\n" +
			"_v0.2.0:_ Added artifacts, reworked rings\n" +
			"\n" +
			"_v0.1.1:_ Added blandfruit, reworked dew vial\n" +
			"_v0.1.0:_ Improvements to potions/scrolls";

	@Override
	public void create() {
		super.create();

		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedText title = PixelScene.renderText( Messages.get(this, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.x = (w - title.width()) / 2 ;
		title.y = 4;
		align(title);
		add(title);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		RenderedTextMultiline text = renderMultiline(TXT_Update, 6 );

		NinePatch panel = Chrome.get(Chrome.Type.TOAST);

		int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
		int ph = h - 16;

		panel.size( pw, ph );
		panel.x = (w - pw) / 2f;
		panel.y = title.y + title.height();
		align( panel );
		add( panel );

		ScrollPane list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		text.maxWidth((int) panel.innerWidth());

		content.add(text);

		content.setSize( panel.innerWidth(), (int)Math.ceil(text.height()) );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth(),
				panel.innerHeight() + 2);
		list.scrollTo(0, 0);

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}
}


