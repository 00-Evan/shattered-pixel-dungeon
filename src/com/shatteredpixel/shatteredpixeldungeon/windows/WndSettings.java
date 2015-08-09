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
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Toolbar;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

public class WndSettings extends WndTabbed {
	private static final String TXT_SWITCH_PORT	= "Switch to portrait";
	private static final String TXT_SWITCH_LAND	= "Switch to landscape";

	private static final int WIDTH		    = 112;
	private static final int HEIGHT         = 112;
	private static final int SLIDER_HEIGHT	= 25;
	private static final int BTN_HEIGHT	    = 20;
	private static final int GAP_SML 		= 2;
	private static final int GAP_LRG 		= 10;

	private ScreenTab screen;
	private UITab ui;
	private AudioTab audio;

	public WndSettings() {
		super();

		screen = new ScreenTab();
		add( screen );

		ui = new UITab();
		add( ui );

		audio = new AudioTab();
		add( audio );

		add( new LabeledTab("Screen"){
			@Override
			protected void select(boolean value) {
				super.select(value);
				screen.visible = screen.active = value;
			}
		});

		add( new LabeledTab("UI"){
			@Override
			protected void select(boolean value) {
				super.select(value);
				ui.visible = ui.active = value;
			}
		});

		add( new LabeledTab("Audio"){
			@Override
			protected void select(boolean value) {
				super.select(value);
				audio.visible = audio.active = value;
			}
		});

		resize(WIDTH, HEIGHT);

		layoutTabs();

		select(0);

	}

	private class ScreenTab extends Group {

		public ScreenTab() {
			super();

			OptionSlider scale = new OptionSlider("Display Scale",
					(int)Math.ceil(2* Game.density)+ "X",
					PixelScene.maxDefaultZoom + "X",
					(int)Math.ceil(2* Game.density),
					PixelScene.maxDefaultZoom ) {
				@Override
				protected void onChange() {
					ShatteredPixelDungeon.scale(getSelectedValue());
					ShatteredPixelDungeon.resetScene();
				}
			};
			scale.setSelectedValue(PixelScene.defaultZoom);
			if ((int)Math.ceil(2* Game.density) < PixelScene.maxDefaultZoom) {
				scale.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
				add(scale);
			} else {
				scale.setRect(0, 0, 0, 0);
			}

			OptionSlider brightness = new OptionSlider("Brightness", "Dark", "Bright", -2, 4) {
				@Override
				protected void onChange() {
					ShatteredPixelDungeon.brightness(getSelectedValue());
				}
			};
			brightness.setSelectedValue(ShatteredPixelDungeon.brightness());
			brightness.setRect(0, scale.bottom() + GAP_SML, WIDTH, SLIDER_HEIGHT);
			add(brightness);

			CheckBox chkImmersive = new CheckBox( "Hide Software Keys" ) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.immerse(checked());
				}
			};
			chkImmersive.setRect( 0, brightness.bottom() + GAP_LRG, WIDTH, BTN_HEIGHT );
			chkImmersive.checked(ShatteredPixelDungeon.immersed());
			chkImmersive.enable(android.os.Build.VERSION.SDK_INT >= 19);
			add(chkImmersive);


			RedButton btnOrientation = new RedButton( ShatteredPixelDungeon.landscape() ? TXT_SWITCH_PORT : TXT_SWITCH_LAND ) {
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.landscape(!ShatteredPixelDungeon.landscape());
				}
			};
			btnOrientation.setRect(0, chkImmersive.bottom() + GAP_LRG, WIDTH, BTN_HEIGHT);
			add( btnOrientation );
		}
	}

	private class UITab extends Group {

		public UITab(){
			super();

			BitmapText barDesc = PixelScene.createText("Toolbar Mode:", 9);
			barDesc.measure();
			barDesc.x = (WIDTH-barDesc.width())/2;
			add(barDesc);

			RedButton btnSplit = new RedButton("Split"){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.SPLIT.name());
					Toolbar.updateLayout();
				}
			};
			btnSplit.setRect( 1, barDesc.y + barDesc.height(), 36, BTN_HEIGHT);
			add(btnSplit);

			RedButton btnGrouped = new RedButton("Group"){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.GROUP.name());
					Toolbar.updateLayout();
				}
			};
			btnGrouped.setRect( btnSplit.right()+1, barDesc.y + barDesc.height(), 36, BTN_HEIGHT);
			add(btnGrouped);

			RedButton btnCentered = new RedButton("Center"){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.CENTER.name());
					Toolbar.updateLayout();
				}
			};
			btnCentered.setRect(btnGrouped.right()+1, barDesc.y + barDesc.height(), 36, BTN_HEIGHT);
			add(btnCentered);

			CheckBox chkFlipToolbar = new CheckBox("Flip Toolbar"){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.flipToolbar(checked());
					Toolbar.updateLayout();
				}
			};
			chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			chkFlipToolbar.checked(ShatteredPixelDungeon.flipToolbar());
			add(chkFlipToolbar);

			CheckBox chkFlipTags = new CheckBox("Flip Indicators"){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.flipTags(checked());
					GameScene.layoutTags();
				}
			};
			chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			chkFlipTags.checked(ShatteredPixelDungeon.flipTags());
			add(chkFlipTags);

			OptionSlider slots = new OptionSlider("Quickslots", "0", "4", 0, 4) {
				@Override
				protected void onChange() {
					ShatteredPixelDungeon.quickSlots(getSelectedValue());
					Toolbar.updateLayout();
				}
			};
			slots.setSelectedValue(ShatteredPixelDungeon.quickSlots());
			slots.setRect(0, chkFlipTags.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(slots);
		}

	}

	private class AudioTab extends Group {

		public AudioTab() {
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
}
