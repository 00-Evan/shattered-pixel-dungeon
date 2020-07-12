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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Toolbar;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

public class WndSettings extends WndTabbed {

	private static final int WIDTH		    = 112;
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

		float height;

		display = new DisplayTab();
		display.setSize(WIDTH, 0);
		height = display.height();
		add( display );

		add( new IconTab(Icons.get(Icons.DISPLAY)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				display.visible = display.active = value;
				if (value) last_index = 0;
			}
		});

		ui = new UITab();
		ui.setSize(WIDTH, 0);
		height = Math.max(height, ui.height());
		add( ui );

		add( new IconTab(Icons.get(Icons.PREFS)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				ui.visible = ui.active = value;
				if (value) last_index = 1;
			}
		});

		audio = new AudioTab();
		audio.setSize(WIDTH, 0);
		height = Math.max(height, audio.height());
		add( audio );

		add( new IconTab(Icons.get(Icons.AUDIO)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				audio.visible = audio.active = value;
				if (value) last_index = 2;
			}
		});

		resize(WIDTH, (int)Math.ceil(height));

		layoutTabs();

		select(last_index);

	}

	private class DisplayTab extends Component {

		OptionSlider optScale;
		CheckBox chkSaver;
		RedButton btnOrientation;

		OptionSlider optBrightness;
		OptionSlider optVisGrid;

		@Override
		protected void createChildren() {
			if ((int)Math.ceil(2* Game.density) < PixelScene.maxDefaultZoom) {
				optScale = new OptionSlider(Messages.get(this, "scale"),
						(int)Math.ceil(2* Game.density)+ "X",
						PixelScene.maxDefaultZoom + "X",
						(int)Math.ceil(2* Game.density),
						PixelScene.maxDefaultZoom ) {
					@Override
					protected void onChange() {
						if (getSelectedValue() != SPDSettings.scale()) {
							SPDSettings.scale(getSelectedValue());
							ShatteredPixelDungeon.seamlessResetScene();
						}
					}
				};
				add(optScale);
			}

			if (!DeviceCompat.isDesktop() && PixelScene.maxScreenZoom >= 2) {
				chkSaver = new CheckBox(Messages.get(this, "saver")) {
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
										SPDSettings.powerSaver(checked());
									}
								}
							});
						} else {
							SPDSettings.powerSaver(checked());
						}
					}
				};
				chkSaver.checked( SPDSettings.powerSaver() );
				add( chkSaver );
			}

			if (!DeviceCompat.isDesktop()) {
				btnOrientation = new RedButton(PixelScene.landscape() ?
						Messages.get(this, "portrait")
						: Messages.get(this, "landscape")) {
					@Override
					protected void onClick() {
						SPDSettings.landscape(!PixelScene.landscape());
					}
				};
				add(btnOrientation);
			}

			optBrightness = new OptionSlider(Messages.get(this, "brightness"),
					Messages.get(this, "dark"), Messages.get(this, "bright"), -1, 1) {
				@Override
				protected void onChange() {
					SPDSettings.brightness(getSelectedValue());
				}
			};
			optBrightness.setSelectedValue(SPDSettings.brightness());
			add(optBrightness);

			optVisGrid = new OptionSlider(Messages.get(this, "visual_grid"),
					Messages.get(this, "off"), Messages.get(this, "high"), -1, 2) {
				@Override
				protected void onChange() {
					SPDSettings.visualGrid(getSelectedValue());
				}
			};
			optVisGrid.setSelectedValue(SPDSettings.visualGrid());
			add(optVisGrid);

		}

		@Override
		protected void layout() {

			float bottom = 0;

			if (optScale != null){
				optScale.setRect(0, 0, width, SLIDER_HEIGHT);
				bottom = optScale.bottom();
			}

			if (chkSaver != null){
				chkSaver.setRect( 0, bottom + GAP_TINY, width, BTN_HEIGHT );
				bottom = chkSaver.bottom();
			}

			if (btnOrientation != null){
				btnOrientation.setRect( 0, bottom + GAP_TINY, width, BTN_HEIGHT );
				bottom = btnOrientation.bottom();
			}

			optBrightness.setRect(0, bottom + GAP_LRG, width, SLIDER_HEIGHT);
			optVisGrid.setRect(0, optBrightness.bottom() + GAP_TINY, width, SLIDER_HEIGHT);

			height = optVisGrid.bottom();
		}

	}

	private class UITab extends Component {

		RenderedTextBlock barDesc;
		RedButton btnSplit; RedButton btnGrouped; RedButton btnCentered;
		CheckBox chkFlipToolbar;
		CheckBox chkFlipTags;

		CheckBox chkFullscreen;
		CheckBox chkFont;

		RedButton btnKeyBindings;

		@Override
		protected void createChildren() {
			barDesc = PixelScene.renderTextBlock(Messages.get(this, "mode"), 9);
			add(barDesc);

			btnSplit = new RedButton(Messages.get(this, "split")){
				@Override
				protected void onClick() {
					SPDSettings.toolbarMode(Toolbar.Mode.SPLIT.name());
					Toolbar.updateLayout();
				}
			};
			add(btnSplit);

			btnGrouped = new RedButton(Messages.get(this, "group")){
				@Override
				protected void onClick() {
					SPDSettings.toolbarMode(Toolbar.Mode.GROUP.name());
					Toolbar.updateLayout();
				}
			};
			add(btnGrouped);

			btnCentered = new RedButton(Messages.get(this, "center")){
				@Override
				protected void onClick() {
					SPDSettings.toolbarMode(Toolbar.Mode.CENTER.name());
					Toolbar.updateLayout();
				}
			};
			add(btnCentered);

			chkFlipToolbar = new CheckBox(Messages.get(this, "flip_toolbar")){
				@Override
				protected void onClick() {
					super.onClick();
					SPDSettings.flipToolbar(checked());
					Toolbar.updateLayout();
				}
			};
			chkFlipToolbar.checked(SPDSettings.flipToolbar());
			add(chkFlipToolbar);

			chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")){
				@Override
				protected void onClick() {
					super.onClick();
					SPDSettings.flipTags(checked());
					GameScene.layoutTags();
				}
			};
			chkFlipTags.checked(SPDSettings.flipTags());
			add(chkFlipTags);

			chkFullscreen = new CheckBox( Messages.get(this, "fullscreen") ) {
				@Override
				protected void onClick() {
					super.onClick();
					SPDSettings.fullscreen(checked());
				}
			};
			chkFullscreen.checked(SPDSettings.fullscreen());
			chkFullscreen.enable(DeviceCompat.supportsFullScreen());
			add(chkFullscreen);

			chkFont = new CheckBox(Messages.get(this, "system_font")){
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.seamlessResetScene(new Game.SceneChangeCallback() {
						@Override
						public void beforeCreate() {
							SPDSettings.systemFont(checked());
						}

						@Override
						public void afterCreate() {
							//do nothing
						}
					});
				}
			};

			chkFont.checked(SPDSettings.systemFont());
			add(chkFont);

			if (DeviceCompat.hasHardKeyboard()){
				btnKeyBindings = new RedButton(Messages.get(this, "key_bindings")){
					@Override
					protected void onClick() {
						super.onClick();
						ShatteredPixelDungeon.scene().addToFront(new WndKeyBindings());
					}
				};

				add(btnKeyBindings);
			}
		}

		@Override
		protected void layout() {
			barDesc.setPos((width-barDesc.width())/2f, GAP_TINY);
			PixelScene.align(barDesc);

			int btnWidth = (int)(width - 2*GAP_TINY)/3;
			btnSplit.setRect(0, barDesc.bottom() + GAP_TINY, btnWidth, 16);
			btnGrouped.setRect(btnSplit.right()+GAP_TINY, btnSplit.top(), btnWidth, 16);
			btnCentered.setRect(btnGrouped.right()+GAP_TINY, btnSplit.top(), btnWidth, 16);

			chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP_TINY, width, BTN_HEIGHT);
			chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP_TINY, width, BTN_HEIGHT);

			chkFullscreen.setRect(0, chkFlipTags.bottom() + GAP_SML, width, BTN_HEIGHT);

			chkFont.setRect(0, chkFullscreen.bottom() + GAP_TINY, width, BTN_HEIGHT);

			if (btnKeyBindings != null){
				btnKeyBindings.setRect(0, chkFont.bottom() + GAP_SML, width, BTN_HEIGHT);
				height = btnKeyBindings.bottom();
			} else {
				height = chkFont.bottom();
			}
		}

	}

	private class AudioTab extends Component {

		OptionSlider optMusic;
		CheckBox chkMusicMute;

		OptionSlider optSFX;
		CheckBox chkMuteSFX;

		@Override
		protected void createChildren() {
			optMusic = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					SPDSettings.musicVol(getSelectedValue());
				}
			};
			optMusic.setSelectedValue(SPDSettings.musicVol());
			add(optMusic);

			chkMusicMute = new CheckBox(Messages.get(this, "music_mute")){
				@Override
				protected void onClick() {
					super.onClick();
					SPDSettings.music(!checked());
				}
			};
			chkMusicMute.checked(!SPDSettings.music());
			add(chkMusicMute);

			optSFX = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					SPDSettings.SFXVol(getSelectedValue());
					if (Random.Int(100) == 0){
						Sample.INSTANCE.play(Assets.Sounds.MIMIC);
					} else {
						Sample.INSTANCE.play(Random.oneOf(Assets.Sounds.GOLD,
								Assets.Sounds.HIT,
								Assets.Sounds.ITEM,
								Assets.Sounds.SHATTER,
								Assets.Sounds.EVOKE,
								Assets.Sounds.SECRET));
					}
				}
			};
			optSFX.setSelectedValue(SPDSettings.SFXVol());
			add(optSFX);

			chkMuteSFX = new CheckBox( Messages.get(this, "sfx_mute") ) {
				@Override
				protected void onClick() {
					super.onClick();
					SPDSettings.soundFx(!checked());
					Sample.INSTANCE.play( Assets.Sounds.CLICK );
				}
			};
			chkMuteSFX.checked(!SPDSettings.soundFx());
			add( chkMuteSFX );
		}

		@Override
		protected void layout() {
			optMusic.setRect(0, 0, width, SLIDER_HEIGHT);
			chkMusicMute.setRect(0, optMusic.bottom() + GAP_TINY, width, BTN_HEIGHT);

			optSFX.setRect(0, chkMusicMute.bottom() + GAP_LRG, width, SLIDER_HEIGHT);
			chkMuteSFX.setRect(0, optSFX.bottom() + GAP_TINY, width, BTN_HEIGHT);

			height = chkMuteSFX.bottom();
		}

	}
}
