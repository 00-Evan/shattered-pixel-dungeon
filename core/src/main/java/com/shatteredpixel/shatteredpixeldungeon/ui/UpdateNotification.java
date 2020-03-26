/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.AvailableUpdateData;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;

public class UpdateNotification extends StyledButton {

	public UpdateNotification(){
		super( Chrome.Type.GREY_BUTTON_TR, Messages.get(UpdateNotification.class, "title") );
		textColor( Window.SHPX_COLOR );
		visible = false;
		Updates.checkForUpdate();
	}

	@Override
	public void update() {
		super.update();

		if (Updates.updateAvailable()){
			bg.alpha((float) (0.7f + Math.sin(Game.timeTotal*2)*0.3f));
			text.alpha((float) (0.7f + Math.sin(Game.timeTotal*2)*0.3f));
			visible = true;
		} else {
			visible = false;
		}

	}

	@Override
	protected void onClick() {
		if (Updates.updateAvailable()){
			ShatteredPixelDungeon.scene().addToFront( new WndUpdate( Updates.updateData() ) );
		}
	}

	public static class WndUpdate extends WndOptions {

		private AvailableUpdateData update;

		public WndUpdate( AvailableUpdateData update ){
			super(
					update.versionName == null ? Messages.get(WndUpdate.class,"title") : Messages.get(WndUpdate.class,"versioned_title", update.versionName),
					update.desc == null ? Messages.get(WndUpdate.class,"desc") : update.desc,
					Messages.get(WndUpdate.class,"button"));
			this.update = update;
		}

		@Override
		protected void onSelect(int index) {
			if (index == 0) {
				Updates.launchUpdate(update);
			}
		}
	}
}
