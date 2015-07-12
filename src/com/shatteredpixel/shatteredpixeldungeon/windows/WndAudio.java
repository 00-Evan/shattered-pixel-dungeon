/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

public class WndAudio extends Window {

	private static final int WIDTH		    = 112;
	private static final int SLIDER_HEIGHT	= 25;
	private static final int BTN_HEIGHT	    = 20;
	private static final int GAP_SML 		= 2;
	private static final int GAP_LRG 		= 6;

	public WndAudio(){

		OptionSlider musicVol = new OptionSlider("Music Volume", "0", "10", 0, 10) {
			@Override
			protected void onChange() {
				Music.INSTANCE.volume(getSelectedValue()/10f);
				ShatteredPixelDungeon.musicVol(getSelectedValue());
			}
		};
		musicVol.setSelectedValue(ShatteredPixelDungeon.musicVol());
		musicVol.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
		add(musicVol);

		CheckBox musicMute = new CheckBox("Mute Music"){
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.music(!checked());
			}
		};
		musicMute.setRect(0, musicVol.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
		musicMute.checked(!ShatteredPixelDungeon.music());
		add(musicMute);


		OptionSlider SFXVol = new OptionSlider("SFX Volume", "0", "10", 0, 10) {
			@Override
			protected void onChange() {
				Sample.INSTANCE.volume(getSelectedValue()/10f);
				ShatteredPixelDungeon.SFXVol(getSelectedValue());
			}
		};
		SFXVol.setSelectedValue(ShatteredPixelDungeon.SFXVol());
		SFXVol.setRect(0, musicMute.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
		add(SFXVol);

		CheckBox btnSound = new CheckBox( "Mute SFX" ) {
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.soundFx(!checked());
				Sample.INSTANCE.play( Assets.SND_CLICK );
			}
		};
		btnSound.setRect(0, SFXVol.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
		btnSound.checked(!ShatteredPixelDungeon.soundFx());
		add( btnSound );

		resize( WIDTH, (int)btnSound.bottom());

	}


}
