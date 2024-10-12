/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Image;
import com.watabou.utils.RectF;

public enum Icons {

	//slightly larger title screen icons, spacing for 17x16
	ENTER,
	GOLD,
	RANKINGS,
	BADGES,
	NEWS,
	CHANGES,
	PREFS,
	SHPX,
	JOURNAL,

	//grey icons, mainly used for buttons, spacing for 16x16
	EXIT,
	DISPLAY, //2 separate images, changes based on orientation
	DISPLAY_LAND,
	DISPLAY_PORT,
	DATA,
	AUDIO,
	LANGS,
	CONTROLLER,
	KEYBOARD,
	STATS,
	CHALLENGE_GREY,
	SCROLL_GREY,
	SEED,
	LEFTARROW,
	RIGHTARROW,
	CALENDAR,
	CHEVRON,

	//misc larger icons, mainly used for buttons, tabs, and journal, spacing for 16x16
	TARGET,
	INFO,
	WARNING,
	UNCHECKED,
	CHECKED,
	CLOSE,
	PLUS,
	REPEAT,
	ARROW,
	CHALLENGE_COLOR,
	SCROLL_COLOR,
	COPY,
	PASTE,

	BACKPACK_LRG,
	TALENT,
	MAGNIFY,
	SNAKE,
	BUFFS,
	CATALOG,
	ALCHEMY,
	GRASS,

	STAIRS,
	STAIRS_CHASM,
	STAIRS_WATER,
	STAIRS_GRASS,
	STAIRS_DARK,
	STAIRS_LARGE,
	STAIRS_TRAPS,
	STAIRS_SECRETS,
	WELL_HEALTH,
	WELL_AWARENESS,
	SACRIFICE_ALTAR,
	DISTANT_WELL,

	//smaller icons, variable spacing
	SKULL,
	BUSY,
	COMPASS,
	SLEEP,
	ALERT,
	LOST,
	DEPTH,      //depth icons have three variants, for regular, seeded, daily, and daily replay runs
	DEPTH_CHASM,
	DEPTH_WATER,
	DEPTH_GRASS,
	DEPTH_DARK,
	DEPTH_LARGE,
	DEPTH_TRAPS,
	DEPTH_SECRETS,
	CHAL_COUNT,
	COIN_SML,
	ENERGY_SML,
	BACKPACK,
	SEED_POUCH,
	SCROLL_HOLDER,
	WAND_HOLSTER,
	POTION_BANDOLIER,

	//icons that appear in the about screen, variable spacing
	LIBGDX,
	ALEKS,
	WATA,
	CELESTI,
	KRISTJAN,
	CUBE_CODE,
	PURIGRO,
	ARCNOR;

	public Image get() {
		return get( this );
	}
	
	public static Image get( Icons type ) {
		Image icon = new Image( Assets.Interfaces.ICONS );
		switch (type) {

			case ENTER:
				icon.frame( icon.texture.uvRectBySize( 0, 0, 16, 16 ) );
				break;
			case GOLD:
				icon.frame( icon.texture.uvRectBySize( 17, 0, 17, 16 ) );
				break;
			case RANKINGS:
				icon.frame( icon.texture.uvRectBySize( 34, 0, 17, 16 ) );
				break;
			case BADGES:
				icon.frame( icon.texture.uvRectBySize( 51, 0, 16, 16 ) );
				break;
			case NEWS:
				icon.frame( icon.texture.uvRectBySize( 68, 0, 16, 15 ) );
				break;
			case CHANGES:
				icon.frame( icon.texture.uvRectBySize( 85, 0, 15, 15 ) );
				break;
			case PREFS:
				icon.frame( icon.texture.uvRectBySize( 102, 0, 14, 14 ) );
				break;
			case SHPX:
				icon.frame( icon.texture.uvRectBySize( 119, 0, 16, 16 ) );
				break;
			case JOURNAL:
				icon.frame( icon.texture.uvRectBySize( 136, 0, 17, 15 ) );
				break;

			case EXIT:
				icon.frame( icon.texture.uvRectBySize( 0, 16, 15, 11 ) );
				break;
			case DISPLAY:
				if (!PixelScene.landscape()){
					return get(DISPLAY_PORT);
				} else {
					return get(DISPLAY_LAND);
				}
			case DISPLAY_PORT:
				icon.frame( icon.texture.uvRectBySize( 16, 16, 12, 16 ) );
				break;
			case DISPLAY_LAND:
				icon.frame( icon.texture.uvRectBySize( 32, 16, 16, 12 ) );
				break;
			case DATA:
				icon.frame( icon.texture.uvRectBySize( 48, 16, 14, 15 ) );
				break;
			case AUDIO:
				icon.frame( icon.texture.uvRectBySize( 64, 16, 14, 14 ) );
				break;
			case LANGS:
				icon.frame( icon.texture.uvRectBySize( 80, 16, 14, 11 ) );
				break;
			case CONTROLLER:
				icon.frame( icon.texture.uvRectBySize( 96, 16, 16, 12 ) );
				break;
			case KEYBOARD:
				icon.frame( icon.texture.uvRectBySize( 112, 16, 15, 12 ) );
				break;
			case STATS:
				icon.frame( icon.texture.uvRectBySize( 128, 16, 16, 13 ) );
				break;
			case CHALLENGE_GREY:
				icon.frame( icon.texture.uvRectBySize( 144, 16, 15, 12 ) );
				break;
			case SCROLL_GREY:
				icon.frame( icon.texture.uvRectBySize( 160, 16, 15, 14 ) );
				break;
			case SEED:
				icon.frame( icon.texture.uvRectBySize( 176, 16, 15, 10 ) );
				break;
			case LEFTARROW:
				icon.frame( icon.texture.uvRectBySize( 192, 16, 14, 9 ) );
				break;
			case RIGHTARROW:
				icon.frame( icon.texture.uvRectBySize( 208, 16, 14, 9 ) );
				break;
			case CALENDAR:
				icon.frame( icon.texture.uvRectBySize( 224, 16, 15, 12 ) );
				break;
			case CHEVRON:
				icon.frame( icon.texture.uvRectBySize( 240, 16, 13, 10 ) );
				break;

			case TARGET:
				icon.frame( icon.texture.uvRectBySize( 0, 32, 16, 16 ) );
				break;
			case INFO:
				icon.frame( icon.texture.uvRectBySize( 16, 32, 14, 14 ) );
				break;
			case WARNING:
				icon.frame( icon.texture.uvRectBySize( 32, 32, 14, 14 ) );
				break;
			case UNCHECKED:
				icon.frame( icon.texture.uvRectBySize( 48, 32, 12, 12 ) );
				break;
			case CHECKED:
				icon.frame( icon.texture.uvRectBySize( 64, 32, 12, 12 ) );
				break;
			case CLOSE:
				icon.frame( icon.texture.uvRectBySize( 80, 32, 11, 11 ) );
				break;
			case PLUS:
				icon.frame( icon.texture.uvRectBySize( 96, 32, 11, 11 ) );
				break;
			case REPEAT:
				icon.frame( icon.texture.uvRectBySize( 112, 32, 11, 11 ) );
				break;
			case ARROW:
				icon.frame( icon.texture.uvRectBySize( 128, 32, 11, 11 ) );
				break;
			case CHALLENGE_COLOR:
				icon.frame( icon.texture.uvRectBySize( 144, 32, 15, 12 ) );
				break;
			case SCROLL_COLOR:
				icon.frame( icon.texture.uvRectBySize( 160, 32, 15, 14 ) );
				break;
			case COPY:
				icon.frame( icon.texture.uvRectBySize( 176, 32, 13, 13 ) );
				break;
			case PASTE:
				icon.frame( icon.texture.uvRectBySize( 192, 32, 13, 13 ) );
				break;

			case BACKPACK_LRG:
				icon.frame( icon.texture.uvRectBySize( 0, 48, 16, 16 ) );
				break;
			case TALENT:
				icon.frame( icon.texture.uvRectBySize( 16, 48, 13, 13 ) );
				break;
			case MAGNIFY:
				icon.frame( icon.texture.uvRectBySize( 32, 48, 14, 14 ) );
				break;
			case SNAKE:
				icon.frame( icon.texture.uvRectBySize( 48, 48,  9, 13 ) );
				break;
			case BUFFS:
				icon.frame( icon.texture.uvRectBySize( 64, 48, 16, 15 ) );
				break;
			case CATALOG:
				icon.frame( icon.texture.uvRectBySize( 80, 48, 13, 16 ) );
				break;
			case ALCHEMY:
				icon.frame( icon.texture.uvRectBySize( 96, 48, 16, 16 ) );
				break;
			case GRASS:
				icon.frame( icon.texture.uvRectBySize( 112, 48, 16, 16 ) );
				break;

			case STAIRS:
				icon.frame( icon.texture.uvRectBySize( 0, 64, 15, 16 ) );
				break;
			case STAIRS_CHASM:
				icon.frame( icon.texture.uvRectBySize( 16, 64, 15, 16 ) );
				break;
			case STAIRS_WATER:
				icon.frame( icon.texture.uvRectBySize( 32, 64, 15, 16 ) );
				break;
			case STAIRS_GRASS:
				icon.frame( icon.texture.uvRectBySize( 48, 64, 15, 16 ) );
				break;
			case STAIRS_DARK:
				icon.frame( icon.texture.uvRectBySize( 64, 64, 15, 16 ) );
				break;
			case STAIRS_LARGE:
				icon.frame( icon.texture.uvRectBySize( 80, 64, 15, 16 ) );
				break;
			case STAIRS_TRAPS:
				icon.frame( icon.texture.uvRectBySize( 96, 64, 15, 16 ) );
				break;
			case STAIRS_SECRETS:
				icon.frame( icon.texture.uvRectBySize( 112, 64, 15, 16 ) );
				break;
			case WELL_HEALTH:
				icon.frame( icon.texture.uvRectBySize( 128, 64, 16, 16 ) );
				break;
			case WELL_AWARENESS:
				icon.frame( icon.texture.uvRectBySize( 144, 64, 16, 16 ) );
				break;
			case SACRIFICE_ALTAR:
				icon.frame( icon.texture.uvRectBySize( 160, 64, 16, 16 ) );
				break;
			case DISTANT_WELL:
				icon.frame( icon.texture.uvRectBySize( 176, 64, 16, 16 ) );
				break;

			case SKULL:
				icon.frame( icon.texture.uvRectBySize( 0, 80, 8, 8 ) );
				break;
			case BUSY:
				icon.frame( icon.texture.uvRectBySize( 8, 80, 8, 8 ) );
				break;
			case COMPASS:
				icon.frame( icon.texture.uvRectBySize( 0, 88, 7, 5 ) );
				break;
			case SLEEP:
				icon.frame( icon.texture.uvRectBySize( 16, 80, 9, 8 ) );
				break;
			case ALERT:
				icon.frame( icon.texture.uvRectBySize( 16, 88, 8, 8 ) );
				break;
			case LOST:
				icon.frame( icon.texture.uvRectBySize( 24, 88, 8, 8 ) );
				break;
			case DEPTH:
				icon.frame( icon.texture.uvRectBySize( 32 + runTypeOfsX(), 80 + runTypeOfsY(), 6, 7 ) );
				break;
			case DEPTH_CHASM:
				icon.frame( icon.texture.uvRectBySize( 40 + runTypeOfsX(), 80 + runTypeOfsY(), 7, 7 ) );
				break;
			case DEPTH_WATER:
				icon.frame( icon.texture.uvRectBySize( 48 + runTypeOfsX(), 80 + runTypeOfsY(), 7, 7 ) );
				break;
			case DEPTH_GRASS:
				icon.frame( icon.texture.uvRectBySize( 56 + runTypeOfsX(), 80 + runTypeOfsY(), 7, 7 ) );
				break;
			case DEPTH_DARK:
				icon.frame( icon.texture.uvRectBySize( 64 + runTypeOfsX(), 80 + runTypeOfsY(), 7, 7 ) );
				break;
			case DEPTH_LARGE:
				icon.frame( icon.texture.uvRectBySize( 72 + runTypeOfsX(), 80 + runTypeOfsY(), 7, 7 ) );
				break;
			case DEPTH_TRAPS:
				icon.frame( icon.texture.uvRectBySize( 80 + runTypeOfsX(), 80 + runTypeOfsY(), 7, 7 ) );
				break;
			case DEPTH_SECRETS:
				icon.frame( icon.texture.uvRectBySize( 88 + runTypeOfsX(), 80 + runTypeOfsY(), 7, 7 ) );
				break;
			case CHAL_COUNT:
				icon.frame( icon.texture.uvRectBySize( 160, 80, 7, 7 ) );
				break;
			case COIN_SML:
				icon.frame( icon.texture.uvRectBySize( 168, 80, 7, 7 ) );
				break;
			case ENERGY_SML:
				icon.frame( icon.texture.uvRectBySize( 168, 88, 8, 7 ) );
				break;
			case BACKPACK:
				icon.frame( icon.texture.uvRectBySize( 176, 80, 10, 10 ) );
				break;
			case SCROLL_HOLDER:
				icon.frame( icon.texture.uvRectBySize( 186, 80, 10, 10 ) );
				break;
			case SEED_POUCH:
				icon.frame( icon.texture.uvRectBySize( 196, 80, 10, 10 ) );
				break;
			case WAND_HOLSTER:
				icon.frame( icon.texture.uvRectBySize( 206, 80, 10, 10 ) );
				break;
			case POTION_BANDOLIER:
				icon.frame( icon.texture.uvRectBySize( 216, 80, 10, 10 ) );
				break;
		
			case LIBGDX:
				icon.frame( icon.texture.uvRectBySize( 0, 96, 16, 13 ) );
				break;
			case ALEKS:
				icon.frame( icon.texture.uvRectBySize( 16, 96, 16, 13 ) );
				break;
			case WATA:
				icon.frame( icon.texture.uvRectBySize( 0, 112, 17, 12 ) );
				break;

			//large icons are scaled down to match game's size
			case CELESTI:
				icon.frame( icon.texture.uvRectBySize( 32, 96, 32, 32 ) );
				icon.scale.set(PixelScene.align(0.49f));
				break;
			case KRISTJAN:
				icon.frame( icon.texture.uvRectBySize( 64, 96, 32, 32 ) );
				icon.scale.set(PixelScene.align(0.49f));
				break;
			case ARCNOR:
				icon.frame( icon.texture.uvRectBySize( 96, 96, 32, 32 ) );
				icon.scale.set(PixelScene.align(0.49f));
				break;
			case PURIGRO:
				icon.frame( icon.texture.uvRectBySize( 128, 96, 32, 32 ) );
				icon.scale.set(PixelScene.align(0.49f));
				break;
			case CUBE_CODE:
				icon.frame( icon.texture.uvRectBySize( 160, 96, 27, 30 ) );
				icon.scale.set(PixelScene.align(0.49f));
				break;

		}
		return icon;
	}

	private static int runTypeOfsX(){
		return Dungeon.daily ? 64 : 0;
	}

	private static int runTypeOfsY(){
		if ((Dungeon.daily && Dungeon.dailyReplay)
				|| (!Dungeon.daily && !Dungeon.customSeedText.isEmpty())){
			return 8;
		} else {
			return 0;
		}
	}
	
	public static Image get( HeroClass cl ) {
		switch (cl) {
			case WARRIOR:
				return new ItemSprite(ItemSpriteSheet.SEAL);
			case MAGE:
				//mage's staff normally has 2 pixels extra at the top for particle effects, we chop that off here
				Image result = new ItemSprite(ItemSpriteSheet.MAGES_STAFF);
				RectF frame = result.frame();
				frame.top += frame.height()/8f;
				result.frame(frame);
				return result;
			case ROGUE:
				return new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK);
			case HUNTRESS:
				return new ItemSprite(ItemSpriteSheet.SPIRIT_BOW);
			case DUELIST:
				return new ItemSprite(ItemSpriteSheet.RAPIER);
			case CLERIC:
				return new ItemSprite(ItemSpriteSheet.ARTIFACT_TOME);
			default:
				return null;
		}
	}

	public static Image get(Level.Feeling feeling){
		switch (feeling){
			case NONE: default:
				return get(DEPTH);
			case CHASM:
				return get(DEPTH_CHASM);
			case WATER:
				return get(DEPTH_WATER);
			case GRASS:
				return get(DEPTH_GRASS);
			case DARK:
				return get(DEPTH_DARK);
			case LARGE:
				return get(DEPTH_LARGE);
			case TRAPS:
				return get(DEPTH_TRAPS);
			case SECRETS:
				return get(DEPTH_SECRETS);
		}
	}

	public static Image getLarge(Level.Feeling feeling){
		switch (feeling){
			case NONE: default:
				return get(STAIRS);
			case CHASM:
				return get(STAIRS_CHASM);
			case WATER:
				return get(STAIRS_WATER);
			case GRASS:
				return get(STAIRS_GRASS);
			case DARK:
				return get(STAIRS_DARK);
			case LARGE:
				return get(STAIRS_LARGE);
			case TRAPS:
				return get(STAIRS_TRAPS);
			case SECRETS:
				return get(STAIRS_SECRETS);
		}
	}
}
