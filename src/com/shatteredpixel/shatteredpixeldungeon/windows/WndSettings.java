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

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Toolbar;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndSettings extends Window {
	
	private static final String TXT_ZOOM_IN			= "+";
	private static final String TXT_ZOOM_OUT		= "-";
	private static final String TXT_ZOOM_DEFAULT	= "Default Zoom";

	private static final String TXT_SCALE		= "UI Scale: %dX";
	private static final String TXT_IMMERSIVE		= "Immersive mode";
	
	private static final String TXT_MUSIC	= "Music";
	
	private static final String TXT_SOUND	= "Sound FX";
	
	private static final String TXT_BRIGHTNESS	= "Brightness: %s";

	private static final String TXT_QUICKSLOT = "QuickSlots: %s";
	
	private static final String TXT_SWITCH_PORT	= "Switch to portrait";
	private static final String TXT_SWITCH_LAND	= "Switch to landscape";
	
	private static final int WIDTH		= 112;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP 		= 2;
	
	private RedButton btnZoomOut;
	private RedButton btnZoomIn;

	private int setScale = PixelScene.defaultZoom;
	
	public WndSettings( boolean inGame ) {
		super();

		CheckBox btnImmersive = null;

		if (inGame) {
			int w = BTN_HEIGHT;
			
			// Zoom out
			btnZoomOut = new RedButton( TXT_ZOOM_OUT ) {
				@Override
				protected void onClick() {
					zoom( Camera.main.zoom - 1 );
				}
			};
			add( btnZoomOut.setRect( 0, 0, w, BTN_HEIGHT) );
			
			// Zoom in
			btnZoomIn = new RedButton( TXT_ZOOM_IN ) {
				@Override
				protected void onClick() {
					zoom( Camera.main.zoom + 1 );
				}
			};
			add( btnZoomIn.setRect( WIDTH - w, 0, w, BTN_HEIGHT) );
			
			// Default zoom
			add( new RedButton( TXT_ZOOM_DEFAULT ) {
				@Override
				protected void onClick() {
					zoom( PixelScene.defaultZoom );
				}
			}.setRect( btnZoomOut.right(), 0, WIDTH - btnZoomIn.width() - btnZoomOut.width(), BTN_HEIGHT ) );
			
		} else {
			
			RedButton btnScaleUp = new RedButton( Utils.format(TXT_SCALE, setScale) ) {
				@Override
				protected void onClick() {
					super.onClick();
					setScale++;
					if (setScale > PixelScene.maxDefaultZoom) setScale = (int)Math.ceil(Game.density);
					this.text(Utils.format(TXT_SCALE, setScale));
				}
			};
			btnScaleUp.setRect( 0, 0, WIDTH, BTN_HEIGHT );
			add( btnScaleUp );

			btnImmersive = new CheckBox( TXT_IMMERSIVE ) {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.immerse(checked());
				}
			};
			btnImmersive.setRect( 0, btnScaleUp.bottom() + GAP, WIDTH, BTN_HEIGHT );
			btnImmersive.checked( ShatteredPixelDungeon.immersed() );
			btnImmersive.enable( android.os.Build.VERSION.SDK_INT >= 19 );
			add( btnImmersive );

		}
		
		CheckBox btnMusic = new CheckBox( TXT_MUSIC ) {
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.music(checked());
			}
		};
		btnMusic.setRect( 0, (btnImmersive != null ? btnImmersive.bottom() : BTN_HEIGHT) + GAP, WIDTH, BTN_HEIGHT );
		btnMusic.checked( ShatteredPixelDungeon.music() );
		add( btnMusic );
		
		CheckBox btnSound = new CheckBox( TXT_SOUND ) {
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.soundFx(checked());
				Sample.INSTANCE.play( Assets.SND_CLICK );
			}
		};
		btnSound.setRect( 0, btnMusic.bottom() + GAP, WIDTH, BTN_HEIGHT );
		btnSound.checked( ShatteredPixelDungeon.soundFx() );
		add( btnSound );
		
		if (!inGame) {
			
			RedButton btnOrientation = new RedButton( orientationText() ) {
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.landscape(!ShatteredPixelDungeon.landscape());
				}
			};
			btnOrientation.setRect( 0, btnSound.bottom() + GAP, WIDTH, BTN_HEIGHT );
			add( btnOrientation );
			
			resize( WIDTH, (int)btnOrientation.bottom() );
			
		} else {

			RedButton btnBrightness = new RedButton(  Utils.format(TXT_BRIGHTNESS, ShatteredPixelDungeon.brightness()) ) {
				@Override
				protected void onClick() {
					super.onClick();
					int brightness = ShatteredPixelDungeon.brightness()+1;
					if (brightness == 5) brightness = -2;
					ShatteredPixelDungeon.brightness(brightness);
					this.text(Utils.format(TXT_BRIGHTNESS, brightness));
				}
			};
			btnBrightness.setRect(0, btnSound.bottom() + GAP, WIDTH, BTN_HEIGHT);
			add(btnBrightness);

			RedButton btnQuickSlot = new RedButton( Utils.format(TXT_QUICKSLOT, ShatteredPixelDungeon.quickSlots()) ) {
				@Override
				protected void onClick() {
					super.onClick();
					int quickslots = ShatteredPixelDungeon.quickSlots()+1;
					if (quickslots == 5) quickslots = 0;
					ShatteredPixelDungeon.quickSlots(quickslots);
					this.text(Utils.format(TXT_QUICKSLOT, quickslots));
					Toolbar.slots = quickslots;
				}
			};
			btnQuickSlot.setRect(0, btnBrightness.bottom() + GAP, WIDTH, BTN_HEIGHT);
			add(btnQuickSlot);

			CheckBox btnFlipUI = new CheckBox("Flip Toolbar") {
				@Override
				protected void onClick() {
					super.onClick();
					ShatteredPixelDungeon.flippedUI(checked());
					Toolbar.flipped = checked();
				}
			};
			btnFlipUI.setRect( 0, btnQuickSlot.bottom() + GAP, WIDTH, BTN_HEIGHT );
			btnFlipUI.checked(ShatteredPixelDungeon.flippedUI());
			add(btnFlipUI);
			
			resize( WIDTH, (int)btnFlipUI.bottom() );
			
		}
	}
	
	private void zoom( float value ) {

		Camera.main.zoom( value );
		ShatteredPixelDungeon.zoom((int) (value - PixelScene.defaultZoom));

		updateEnabled();
	}
	
	private void updateEnabled() {
		float zoom = Camera.main.zoom;
		btnZoomIn.enable( zoom < PixelScene.maxZoom );
		btnZoomOut.enable( zoom > PixelScene.minZoom );
	}
	
	private String orientationText() {
		return ShatteredPixelDungeon.landscape() ? TXT_SWITCH_PORT : TXT_SWITCH_LAND;
	}

	@Override
	public void hide() {
		super.hide();
		if (setScale != PixelScene.defaultZoom) {
			ShatteredPixelDungeon.scale(setScale);
			ShatteredPixelDungeon.switchScene(TitleScene.class);
		}
	}
}
