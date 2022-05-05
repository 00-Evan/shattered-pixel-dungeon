/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBadge;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BadgesGrid extends Component {

	ArrayList<BadgeButton> badgeButtons;

	public BadgesGrid( boolean global ){
		super();
		badgeButtons = new ArrayList<>();

		for (Badges.Badge badge : Badges.filterReplacedBadges( global )) {

			if (badge.image == -1) {
				continue;
			}

			BadgeButton button = new BadgeButton( badge, true );
			add( button );
			badgeButtons.add(button);
		}

		if (global) {

			ArrayList<Badges.Badge> lockedBadges = new ArrayList<>();
			for (Badges.Badge badge : Badges.Badge.values()) {
				if (badge.image != -1 && !Badges.isUnlocked(badge)) {
					lockedBadges.add(badge);
				}
			}
			Badges.filterBadgesWithoutPrerequisites(lockedBadges);

			for (Badges.Badge badge : lockedBadges) {
				BadgeButton button = new BadgeButton( badge, false );
				add(button);
				badgeButtons.add(button);
			}

		}

	}

	@Override
	protected void layout() {
		super.layout();

		//2-5 columns in portrait, 5-8 in landscape
		int nCols;
		if (width() > height()){
			if (badgeButtons.size() > 35)       nCols = 8;
			else if (badgeButtons.size() > 24)  nCols = 7;
			else if (badgeButtons.size() > 15)  nCols = 6;
			else                                nCols = 5;
		} else {
			if (badgeButtons.size() > 32)       nCols = 5;
			else if (badgeButtons.size() > 21)  nCols = 4;
			else if (badgeButtons.size() > 10)  nCols = 3;
			else                                nCols = 2;
		}

		int nRows = (int) Math.ceil(badgeButtons.size()/(float)nCols);

		float badgeWidth = width()/nCols;
		float badgeHeight = height()/nRows;

		for (int i = 0; i < badgeButtons.size(); i++){
			int row = i / nCols;
			int col = i % nCols;
			BadgeButton button = badgeButtons.get(i);
			button.setPos(
					left() + col * badgeWidth + (badgeWidth - button.width()) / 2,
					top() + row * badgeHeight + (badgeHeight - button.height()) / 2);
			PixelScene.align(button);
		}
	}

	private static class BadgeButton extends Button {

		private Badges.Badge badge;
		private boolean unlocked;

		private Image icon;

		public BadgeButton( Badges.Badge badge, boolean unlocked ) {
			super();

			this.badge = badge;
			this.unlocked = unlocked;

			icon = BadgeBanner.image(badge.image);
			if (!unlocked) {
				icon.brightness(0.4f);
			}
			add(icon);

			setSize( icon.width(), icon.height() );
		}

		@Override
		protected void layout() {
			super.layout();

			icon.x = x + (width - icon.width()) / 2;
			icon.y = y + (height - icon.height()) / 2;
		}

		@Override
		public void update() {
			super.update();

			if (unlocked && Random.Float() < Game.elapsed * 0.1) {
				BadgeBanner.highlight( icon, badge.image );
			}
		}

		@Override
		protected void onClick() {
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
			Game.scene().add( new WndBadge( badge, unlocked ) );
		}

		@Override
		protected String hoverText() {
			return badge.title();
		}
	}

}