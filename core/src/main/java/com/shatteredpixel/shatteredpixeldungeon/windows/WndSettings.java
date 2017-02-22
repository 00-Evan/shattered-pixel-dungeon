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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Toolbar;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

public class WndSettings extends WndTabbed {

	private static final int WIDTH		    = 112;
	private static final int HEIGHT         = 138;
	private static final int SLIDER_HEIGHT	= 24;
	private static final int BTN_HEIGHT	    = 18;
	private static final int GAP_TINY 		= 2;
	private static final int GAP_SML 		= 6;
	private static final int GAP_LRG 		= 18;

	private DisplayTab display;
	private UITab ui;
	private AudioTab audio;

	private static int last_index = 0;

	public WndSettings() {
		super();

		display = new DisplayTab();
		add( display );

		ui = new UITab();
		add( ui );

		audio = new AudioTab();
		add( audio );

		add( new LabeledTab(Messages.get(this, "display")){
			@Override
			protected void select(boolean value) {
				super.select(value);
				display.visible = display.active = value;
				if (value) last_index = 0;
			}
		});

		add( new LabeledTab(Messages.get(this, "ui")){
			@Override
			protected void select(boolean value) {
				super.select(value);
				ui.visible = ui.active = value;
				if (value) last_index = 1;
			}
		});

		add( new LabeledTab(Messages.get(this, "audio")){
			@Override
			protected void select(boolean value) {
				super.select(value);
				audio.visible = audio.active = value;
				if (value) last_index = 2;
			}
		});

		resize(WIDTH, HEIGHT);

		layoutTabs();

		select(last_index);

	}

	private class DisplayTab extends Group {

		public DisplayTab() {
			super();

			OptionSlider scale = new OptionSlider(Messages.get(this, "scale"),
					(int)Math.ceil(2* Game.density)+ "X",
					PixelScene.maxDefaultZoom + "X",
					(int)Math.ceil(2* Game.density),
					PixelScene.maxDefaultZoom ) {
				@Override
				protected void onChange() {
					if (getSelectedValue() != ShatteredPixelDungeon.scale()) {
						ShatteredPixelDungeon.scale(getSelectedValue());
						ShatteredPixelDungeon.switchNoFade((Class<? extends PixelScene>) ShatteredPixelDungeon.scene().getClass(), new Game.SceneChangeCallback() {
							@Override
							public void beforeCreate() {
								//do nothing
							}

							@Override
							public void afterCreate() {
								Game.scene().add(new WndSettings());
							}
						});
					}
				}
			};
			if ((int)Math.ceil(2* Game.density) < PixelScene.maxDefaultZoom) {
				scale.setSelectedValue(PixelScene.defaultZoom);
				scale.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
				add(scale);
			}

			CheckBox chkSaver = new CheckBox( Messages.get(this, "saver") ) {
				@Override
				protected void onClick() {
					super.onClick();
					if (checked()) {
						checked(!checked());
						ShatteredPixelDungeon.scene().add(new WndOptions(
								Messages.get(DisplayTab.class, "saver"),
								Messages.get(DisplayTab.class, "saver_desc"),
								Messages.get(DisplayTab.class, "okay"),
								Messages.get(DisplayTab.class, "cancel")) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									checked(!checked());
									ShatteredPixelDungeon.powerSaver(checked());
								}
							}
						});
					} else {
						ShatteredPixelDungeon.powerSaver(checked());
					}
				}
			};
			if (PixelScene.maxScreenZoom >= 2) {
				chkSaver.setRect(0, scale.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
				chkSaver.checked(ShatteredPixelDungeon.powerSaver());
				add(chkSaver);
			}

			RedButton btnOrientation = new RedButton( ShatteredPixelDungeon.landscape() ?
					Messages.get(this, "portrait")
					: Messages.get(this, "landscape") ) {
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.landscape(!ShatteredPixelDungeon.landscape());
				}
			};
			btnOrientation.setRect(0, chkSaver.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			add( btnOrientation );


			OptionSlider brightness = new OptionSlider(Messages.get(this, "brightness"),
					Messages.get(this, "dark"), Messages.get(this, "bright"), -2, 2) {
				@Override
				protected void onChange() {
					ShatteredPixelDungeon.brightness(getSelectedValue());
				}
			};
			brightness.setSelectedValue(ShatteredPixelDungeon.brightness());
			brightness.setRect(0, btnOrientation.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(brightness);

			OptionSlider tileGrid = new OptionSlider(Messages.get(this, "visual_grid"),
					Messages.get(this, "off"), Messages.get(this, "high"), -1, 3) {
				@Override
				protected void onChange() {
					ShatteredPixelDungeon.visualGrid(getSelectedValue());
				}
			};
			tileGrid.setSelectedValue(ShatteredPixelDungeon.visualGrid());
			tileGrid.setRect(0, brightness.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(tileGrid);


		}
	}

	private class UITab extends Group {

		public UITab(){
			super();

			RenderedText barDesc = PixelScene.renderText(Messages.get(this, "mode"), 9);
			barDesc.x = (WIDTH-barDesc.width())/2;
			PixelScene.align(barDesc);
			add(barDesc);

			RedButton btnSplit = new RedButton(Messages.get(this, "split")){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.SPLIT.name());
					Toolbar.updateLayout();
				}
			};
			btnSplit.setRect( 0, barDesc.y + barDesc.baseLine()+GAP_TINY, 36, 16);
			add(btnSplit);

			RedButton btnGrouped = new RedButton(Messages.get(this, "group")){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.GROUP.name());
					Toolbar.updateLayout();
				}
			};
			btnGrouped.setRect( btnSplit.right()+GAP_TINY, barDesc.y + barDesc.baseLine()+GAP_TINY, 36, 16);
			add(btnGrouped);

			RedButton btnCentered = new RedButton(Messages.get(this, "center")){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.toolbarMode(Toolbar.Mode.CENTER.name());
					Toolbar.updateLayout();
				}
			};
			btnCentered.setRect(btnGrouped.right()+GAP_TINY, barDesc.y + barDesc.baseLine()+GAP_TINY, 36, 16);
			add(btnCentered);

			CheckBox chkFlipToolbar = new CheckBox(Messages.get(this, "flip_toolbar")){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.flipToolbar(checked());
					Toolbar.updateLayout();
				}
			};
			chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFlipToolbar.checked(ShatteredPixelDungeon.flipToolbar());
			add(chkFlipToolbar);

			final CheckBox chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.flipTags(checked());
					GameScene.layoutTags();
				}
			};
			chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFlipTags.checked(ShatteredPixelDungeon.flipTags());
			add(chkFlipTags);

			OptionSlider slots = new OptionSlider(Messages.get(this, "quickslots"), "0", "4", 0, 4) {
				@Override
				protected void onChange() {
					ShatteredPixelDungeon.quickSlots(getSelectedValue());
					Toolbar.updateLayout();
				}
			};
			slots.setSelectedValue(ShatteredPixelDungeon.quickSlots());
			slots.setRect(0, chkFlipTags.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(slots);

			CheckBox chkImmersive = new CheckBox( Messages.get(this, "soft_keys") ) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.immerse(checked());
				}
			};
			chkImmersive.setRect( 0, slots.bottom() + GAP_SML, WIDTH, BTN_HEIGHT );
			chkImmersive.checked(ShatteredPixelDungeon.immersed());
			chkImmersive.enable(android.os.Build.VERSION.SDK_INT >= 19);
			add(chkImmersive);

			CheckBox chkFont = new CheckBox(Messages.get(this, "system_font")){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.switchNoFade((Class<? extends PixelScene>) ShatteredPixelDungeon.scene().getClass(), new Game.SceneChangeCallback() {
						@Override
						public void beforeCreate() {
							ShatteredPixelDungeon.classicFont(!checked());
						}

						@Override
						public void afterCreate() {
							Game.scene().add(new WndSettings());
						}
					});
				}
			};
			chkFont.setRect(0, chkImmersive.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFont.checked(!ShatteredPixelDungeon.classicFont());
			add(chkFont);
		}

	}

	private class AudioTab extends Group {

		public AudioTab() {
			OptionSlider musicVol = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					Music.INSTANCE.volume(getSelectedValue()/10f);
					ShatteredPixelDungeon.musicVol(getSelectedValue());
				}
			};
			musicVol.setSelectedValue(ShatteredPixelDungeon.musicVol());
			musicVol.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
			add(musicVol);

			CheckBox musicMute = new CheckBox(Messages.get(this, "music_mute")){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.music(!checked());
				}
			};
			musicMute.setRect(0, musicVol.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			musicMute.checked(!ShatteredPixelDungeon.music());
			add(musicMute);


			OptionSlider SFXVol = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					Sample.INSTANCE.volume(getSelectedValue()/10f);
					ShatteredPixelDungeon.SFXVol(getSelectedValue());
				}
			};
			SFXVol.setSelectedValue(ShatteredPixelDungeon.SFXVol());
			SFXVol.setRect(0, musicMute.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(SFXVol);

			CheckBox btnSound = new CheckBox( Messages.get(this, "sfx_mute") ) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.soundFx(!checked());
					Sample.INSTANCE.play( Assets.SND_CLICK );
				}
			};
			btnSound.setRect(0, SFXVol.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			btnSound.checked(!ShatteredPixelDungeon.soundFx());
			add( btnSound );

			resize( WIDTH, (int)btnSound.bottom());
		}

	}
}
