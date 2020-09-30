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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBadge;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class BadgesScene extends PixelScene {

	@Override
	public void create() {

		super.create();

		Music.INSTANCE.play( Assets.Music.THEME, true );

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		float left = 5;
		float top = 20;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(w - title.width()) / 2f,
				(top - title.height()) / 2f
		);
		align(title);
		add(title);

		Badges.loadGlobal();

		ArrayList<Badges.Badge> lockedBadges = new ArrayList<>();
		for (Badges.Badge badge : Badges.Badge.values()){
			if (badge.image != -1 && !Badges.isUnlocked(badge)){
				lockedBadges.add(badge);
			}
		}
		Badges.filterHigherIncrementalBadges(lockedBadges);

		List<Badges.Badge> badges = Badges.filterReplacedBadges( true );

		int totalBadges = lockedBadges.size() + badges.size();

		//4-5 columns in portrait, 6-8 in landscape
		int nCols = landscape() ? 6 : 4;
		if (!landscape() && totalBadges > 32)   nCols++;
		if (landscape() && totalBadges > 24)    nCols++;
		if (landscape() && totalBadges > 35)    nCols++;

		int nRows = (int) Math.ceil(totalBadges/(float)nCols);

		float badgeWidth = (w - 2*left)/nCols;
		float badgeHeight = (h - top - left)/nRows;

		for (int i = 0; i < totalBadges; i++){
			int row = i / nCols;
			int col = i % nCols;
			Badges.Badge b = i < badges.size() ? badges.get( i ) : lockedBadges.get( i - badges.size() );
			BadgeButton button = new BadgeButton( b, i < badges.size() );
			button.setPos(
					left + col * badgeWidth + (badgeWidth - button.width()) / 2,
					top + row * badgeHeight + (badgeHeight - button.height()) / 2);
			align(button);
			add( button );
		}

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}

	@Override
	public void destroy() {

		Badges.saveGlobal();

		super.destroy();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade( TitleScene.class );
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
	}
}
