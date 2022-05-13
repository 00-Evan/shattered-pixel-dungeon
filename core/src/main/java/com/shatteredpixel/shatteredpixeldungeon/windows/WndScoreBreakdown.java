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
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Group;

import java.text.NumberFormat;
import java.util.Locale;

public class WndScoreBreakdown extends Window {

	private static final int WIDTH			= 115;

	private int GAP	= 4;

	public WndScoreBreakdown(){

		IconTitle title = new IconTitle( Icons.get(Icons.INFO), Messages.get(this, "title"));
		title.setRect(0, 0, WIDTH, 16);
		add(title);

		float pos = title.bottom()+2;

		NumberFormat num = NumberFormat.getInstance(Locale.US);
		if (Dungeon.initialVersion > ShatteredPixelDungeon.v1_2_3) {
			pos = statSlot(this, Messages.get(this, "progress_title"),
					num.format(Statistics.progressScore), pos, Statistics.progressScore >= 50_000);
			pos = addInfo(this, Messages.get(this, "progress_desc"), pos);
			pos = statSlot(this, Messages.get(this, "treasure_title"),
					num.format(Statistics.treasureScore), pos, Statistics.treasureScore >= 20_000);
			pos = addInfo(this, Messages.get(this, "treasure_desc"), pos);
			pos = statSlot(this, Messages.get(this, "explore_title"),
					num.format(Statistics.exploreScore), pos, Statistics.exploreScore >= 20_000);
			pos = addInfo(this, Messages.get(this, "explore_desc"), pos);
			pos = statSlot(this, Messages.get(this, "bosses_title"),
					num.format(Statistics.totalBossScore), pos, Statistics.totalBossScore >= 15_000);
			pos = addInfo(this, Messages.get(this, "bosses_desc"), pos);
			pos = statSlot(this, Messages.get(this, "quests_title"),
					num.format(Statistics.totalQuestScore), pos, Statistics.totalQuestScore >= 10_000);
			pos = addInfo(this, Messages.get(this, "quests_desc"), pos);
		} else {
			pos = statSlot(this, Messages.get(this, "progress_title"),
					num.format(Statistics.progressScore), pos, Statistics.progressScore >= 78_000);
			pos = addInfo(this, Messages.get(this, "progress_desc"), pos);
			pos = statSlot(this, Messages.get(this, "treasure_title"),
					num.format(Statistics.treasureScore), pos, Statistics.treasureScore >= 30_000);
			pos = addInfo(this, Messages.get(this, "treasure_desc_old"), pos);
		}

		if (Statistics.winMultiplier > 1) {
			pos = statSlot(this, Messages.get(this, "win_multiplier"), Statistics.winMultiplier + "x", pos, false);
		}
		if (Statistics.chalMultiplier > 1) {
			pos = statSlot(this, Messages.get(this, "challenge_multiplier"), Statistics.chalMultiplier + "x", pos, false);
		}
		pos = statSlot(this, Messages.get(this, "total"), num.format(Statistics.totalScore), pos, false);

		if (Dungeon.initialVersion <= ShatteredPixelDungeon.v1_2_3){
			pos = addInfo(this, Messages.get(this, "old_score_desc"), pos);
		}

		resize(WIDTH, (int)pos);

	}

	private float statSlot(Group parent, String label, String value, float pos, boolean highlight ) {

		RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
		if (highlight) txt.hardlight(Window.TITLE_COLOR);
		txt.setPos(0, pos);
		parent.add( txt );

		txt = PixelScene.renderTextBlock( value, 7 );
		if (highlight) txt.hardlight(Window.TITLE_COLOR);
		txt.setPos(WIDTH * 0.7f, pos);
		PixelScene.align(txt);
		parent.add( txt );

		return pos + GAP + txt.height();
	}

	private float addInfo(Group parent, String info, float pos){

		RenderedTextBlock txt = PixelScene.renderTextBlock( info, 5 );
		txt.maxWidth(WIDTH);
		txt.hardlight(0x999999);
		txt.setPos(0, pos-2);
		parent.add( txt );

		return pos - 2 + GAP + txt.height();

	}


}
