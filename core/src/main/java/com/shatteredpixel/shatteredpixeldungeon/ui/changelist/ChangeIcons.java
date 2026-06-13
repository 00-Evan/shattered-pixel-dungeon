/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;

//separate sprite assets for icons in the changelist, so that old visuals are preserved here after changes
public enum ChangeIcons {

	PD_AMULET,
	PD_BADGE_ASCENT,
	PD_GHOST,
	PD_EYE,
	PD_MONK,
	PD_BAT,
	PD_SKELETON,
	PD_RAT,

	V010_SHPX,
	V010_EARTHROOT,
	V010_POTION_SILVER,
	V010_SCROLL_BERKANAN,

	V011_BLANDFRUIT,
	V011_ANKH,
	V011_SCROLL_LAGUZ,

	V020_HORN,
	V020_RING_DIAMOND,
	V020_MISC,

	V021_SHPX,
	V021_GOO,
	V021_SIGNPOST,

	V022_WEIGHTSTONE,
	V022_DREAMFOIL,
	V022_REMAINS,

	V023_HOURGLASS,
	V023_POTION_CRIMSON,

	V024_HONEYPOT,
	V024_BANDOLIER,

	V030_MAGE_CLOTH,
	V030_DISINTEGRATION,

	V031_GRIM_TRAP,
	V031_MENU_BUTTON,

	V032_TENGU,
	V032_MASTERY,

	V033_PLAYGAMES,

	V034_TRANSLATIONS,

	V035_WARRIOR_CLOTH,
	V035_WARRIOR_HEROARM,

	V040_LONGSWORD,
	V040_STYLUS,

	V041_PLATE,
	V041_RUNICBLADE,

	V042_GLAIVE,

	V043_FLAIL,

	V050_STAIRS,
	V050_QUARTERSTAFF,

	V060_TORCH,
	V060_RATION,
	V060_GREATAXE,

	V061_PAGE,
	V061_BADGE,
	V061_STAIRS,
	V061_RING_TOPAZ,
	V061_CHEST,
	V061_RING_AMETHYST,
	V061_DEW_VIAL,
	V061_STATUE,
	V061_BUGFIX,
	V061_SPELLBOOK,
	V061_ROSE,
	V061_INVENTORY,
	V061_RING_GARNET,
	V061_ETHEREAL_CHAINS,

	V062_ROGUE_CLOTH,
	V062_ROGUE_HEROARM,
	V062_FIRE_TRAP,
	V062_GOLD_CHEST,
	V062_IRON_KEY,
	V062_CORRUPTION,
	V062_CLOAK,
	V062_DAGGER,
	V062_POTION_GOLDEN,
	V062_REMAINS,

	V063_TRIDENT,
	V063_HUNTRESS_CLOTH,
	V063_DART_ICECAP,
	V063_BUFFS_BURNING,
	V063_CORROSION,
	V063_RING_EMERALD,
	V063_MAGE_HEROARM,
	V063_MAGICMISSILE,
	V063_RING_ONYX,

	V064_CHALLENGES,
	V064_INFO,
	V064_CROSSBOW,
	V064_HOLSTER,
	V064_TALISMAN,
	V064_PARALYSIS,
	V064_CHASM,
	V064_SEED_SUNGRASS,

	V065_CLOTH_CURSED,
	V065_BADGE_CHAMPION,
	V065_SWORD_LUCKY,
	V065_SHORTSWORD_CURSED,
	V065_ARMOR_KIT,

	V074_SHPX,

	V081_MISC,
	V081_TRANSLATIONS,

	V250_DEPTH,

	;


	private static int texW = 128/16;
	int w, h;

	ChangeIcons(){
		w = h = 16;
	}

	ChangeIcons(int w, int h){
		this.w = w;
		this.h = h;
	}

	public Image get(){
		return get(this);
	}

	public static Image get( ChangeIcons type ) {
		Image icon = new Image( Assets.Interfaces.CHANGE_ICONS );
		int x = type.ordinal()%texW;
		int y = type.ordinal()/texW;
		icon.frame(16*x, 16*y, type.w, type.h);
		return icon;
	}

}
