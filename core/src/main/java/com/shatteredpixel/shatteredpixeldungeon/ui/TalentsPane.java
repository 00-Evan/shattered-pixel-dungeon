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

public class TalentsPane extends ScrollPane {

	ArrayList<TalentTierPane> panes = new ArrayList<>();
	ArrayList<ColorBlock> separators = new ArrayList<>();

	ColorBlock sep;
	ColorBlock blocker;
	RenderedTextBlock blockText;

	public TalentsPane( boolean canUpgrade ) {
		this( canUpgrade, Dungeon.hero.talents );
	}

	public TalentsPane( boolean canUpgrade, ArrayList<LinkedHashMap<Talent, Integer>> talents ) {
		super(new Component());

		int tiersAvailable = 1;

		if (!canUpgrade){
			tiersAvailable = Talent.MAX_TALENT_TIERS;
		} else {
			while (tiersAvailable < Talent.MAX_TALENT_TIERS
					&& Dungeon.hero.lvl+1 >= Talent.tierLevelThresholds[tiersAvailable+1]){
				tiersAvailable++;
			}
		}

		for (int i = 0; i < Math.min(tiersAvailable, talents.size()); i++){
			TalentTierPane pane = new TalentTierPane(talents.get(i), i+1, canUpgrade);
			panes.add(pane);
			content.add(pane);

			ColorBlock sep = new ColorBlock(0, 1, 0xFF000000);
			separators.add(sep);
			content.add(sep);
		}

		sep = new ColorBlock(0, 1, 0xFF000000);
		content.add(sep);

		blocker = new ColorBlock(0, 0, 0xFF222222);
		content.add(blocker);

		if (tiersAvailable == 1) {
			blockText = PixelScene.renderTextBlock(Messages.get(this, "unlock_tier2"), 6);
		} else {
			blockText = PixelScene.renderTextBlock(Messages.get(this, "coming_soon"), 6);
		}
		content.add(blockText);
	}

	@Override
	protected void layout() {
		super.layout();

		float top = 0;
		for (int i = 0; i < panes.size(); i++){
			top += 2;
			panes.get(i).setRect(x, top, width, 0);
			top = panes.get(i).bottom();

			separators.get(i).x = 0;
			separators.get(i).y = top + 2;
			separators.get(i).size(width, 1);

			top += 3;

		}

		blocker.x = 0;
		blocker.y = top;
		blocker.size(width, height - top);

		blockText.maxWidth((int)width);
		blockText.align(RenderedTextBlock.CENTER_ALIGN);
		blockText.setPos((width - blockText.width())/2f, blocker.y + (height - blocker.y - blockText.height())/2);
	}

	public static class TalentTierPane extends Component {

		private int tier;

		RenderedTextBlock title;
		ArrayList<TalentButton> buttons;

		ArrayList<Image> stars = new ArrayList<>();

		public TalentTierPane(LinkedHashMap<Talent, Integer> talents, int tier, boolean canUpgrade){
			super();

			this.tier = tier;

			title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(TalentsPane.class, "tier", tier)), 9);
			title.hardlight(Window.TITLE_COLOR);
			add(title);

			if (canUpgrade) setupStars();

			buttons = new ArrayList<>();
			for (Talent talent : talents.keySet()){
				TalentButton btn = new TalentButton(tier, talent, talents.get(talent), canUpgrade){
					@Override
					public void upgradeTalent() {
						super.upgradeTalent();
						if (parent != null) {
							setupStars();
							TalentTierPane.this.layout();
						}
					}
				};
				buttons.add(btn);
				add(btn);
			}

		}

		private void setupStars(){
			if (!stars.isEmpty()){
				for (Image im : stars){
					im.killAndErase();
				}
				stars.clear();
			}

			int totStars = Talent.tierLevelThresholds[tier+1] - Talent.tierLevelThresholds[tier];
			int openStars = Dungeon.hero.talentPointsAvailable(tier);
			int usedStars = Dungeon.hero.talentPointsSpent(tier);
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
			title.setPos(x + (width - titleWidth)/2f, y);

			float left = title.right() + 2;
			for (Image star : stars){
				star.x = left;
				star.y = title.top();
				PixelScene.align(star);
				left += 6;
			}

			float gap = (width - buttons.size()*TalentButton.WIDTH)/(buttons.size()+1);
			left = x + gap;
			for (TalentButton btn : buttons){
				btn.setPos(left, title.bottom() + 4);
				PixelScene.align(btn);
				left += btn.width() + gap;
			}

			height = buttons.get(0).bottom() - y;

		}

	}
}
