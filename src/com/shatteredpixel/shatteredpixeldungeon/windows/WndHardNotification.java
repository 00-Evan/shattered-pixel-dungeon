/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

//a notification window that the player can't get rid of quickly, good for forcibly telling a message
//USE THIS SPARINGLY
public class WndHardNotification extends WndTitledMessage{

	RedButton btnOkay;

	private double timeLeft;
	private String btnMessage;

	public WndHardNotification( Image icon, String title, String message, String btnMessage, int time) {
		this(new IconTitle(icon, title), message, btnMessage, time);
	}

	public WndHardNotification(Component titlebar, String message, String btnMessage, int time) {
		super(titlebar, message);

		timeLeft = time;
		this.btnMessage = btnMessage;

		btnOkay = new RedButton(btnMessage + " (" + time +")"){
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnOkay.setRect(0, height + GAP, width, 16);
		btnOkay.enable(false);
		add(btnOkay);

		resize(width, (int) btnOkay.bottom());
	}

	@Override
	public void update() {
		super.update();

		timeLeft -= Game.elapsed;
		if (timeLeft <= 0 ){
			btnOkay.enable(true);
			btnOkay.text(btnMessage);
		} else {
			btnOkay.text(btnMessage + " (" + (int)Math.ceil(timeLeft) + ")");
		}

	}

	@Override
	public void onBackPressed() {
		if (timeLeft <= 0 ) super.onBackPressed();
	}
}
