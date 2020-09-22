/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

//TODO some stuff here is currently coded without accounting for tiers
public class TalentsPane extends ScrollPane {

	RenderedTextBlock title;
	ArrayList<TalentButton> buttons;

	ArrayList<Image> stars = new ArrayList<>();

	ColorBlock sep;
	ColorBlock blocker;
	RenderedTextBlock blockText;

	public TalentsPane( boolean canUpgrade ) {
		this( canUpgrade, Dungeon.hero.talents );
	}

	public TalentsPane( boolean canUpgrade, ArrayList<LinkedHashMap<Talent, Integer>> talents ) {
		super(new Component());

		title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "tier", 1)), 9);
		title.hardlight(Window.TITLE_COLOR);
		content.add(title);

		if (canUpgrade) setupStars();

		buttons = new ArrayList<>();
		for (Talent talent : talents.get(0).keySet()){
			TalentButton btn = new TalentButton(talent, talents.get(0).get(talent), canUpgrade){
				@Override
				public void upgradeTalent() {
					super.upgradeTalent();
					setupStars();
					TalentsPane.this.layout();
				}
			};
			buttons.add(btn);
			content.add(btn);
		}

		sep = new ColorBlock(0, 1, 0xFF000000);
		content.add(sep);

		blocker = new ColorBlock(0, 0, 0xFF222222);
		content.add(blocker);

		blockText = PixelScene.renderTextBlock(Messages.get(this, "coming_soon"), 6);
		content.add(blockText);
	}

	private void setupStars(){
		if (!stars.isEmpty()){
			for (Image im : stars){
				im.killAndErase();
			}
			stars.clear();
		}

		int totStars = 5;
		int openStars = Dungeon.hero.talentPointsAvailable();
		int usedStars = Dungeon.hero.talentPointsSpent();
		for (int i = 0; i < totStars; i++){
			Image im = new Speck().image(Speck.STAR);
			stars.add(im);
			add(im);
			if (i >= openStars && i < (openStars + usedStars)){
				im.tint(0.75f, 0.75f, 0.75f, 0.9f);
			} else if (i >= (openStars + usedStars)){
				im.tint(0f, 0f, 0f, 0.9f);
			}
		}
	}

	@Override
	protected void layout() {
		super.layout();

		float titleWidth = title.width();
		titleWidth += 2 + stars.size()*6;
		title.setPos((width - titleWidth)/2f, 2);

		float left = title.right() + 2;
		for (Image star : stars){
			star.x = left;
			star.y = title.top();
			PixelScene.align(star);
			left += 6;
		}

		float gap = (width - buttons.size()*TalentButton.WIDTH)/(buttons.size()+1);
		left = gap;
		for (TalentButton btn : buttons){
			btn.setPos(left, title.bottom() + 4);
			PixelScene.align(btn);
			left += btn.width() + gap;
		}

		sep.x = 0;
		sep.y = buttons.get(0).bottom() + 2;
		sep.size(width, 1);

		blocker.x = 0;
		blocker.y = sep.y + 1;
		blocker.size(width, height - sep.y - 1);

		blockText.setPos((width - blockText.width())/2f, blocker.y + 10);
	}
}
