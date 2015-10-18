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
package com.shatteredpixel.shatteredpixeldungeon.sprites;

public class ItemSpriteSheet {

	// Row definers
	private static final int ROW1 = 0*16;
	private static final int ROW2 = 1*16;
	private static final int ROW3 = 2*16;
	private static final int ROW4 = 3*16;
	private static final int ROW5 = 4*16;
	private static final int ROW6 = 5*16;
	private static final int ROW7 = 6*16;
	private static final int ROW8 = 7*16;
	private static final int ROW9 = 8*16;
	private static final int ROW10 = 9*16;
	private static final int ROW11 = 10*16;
	private static final int ROW12 = 11*16;
	private static final int ROW13 = 12*16;
	private static final int ROW14 = 13*16;
	private static final int ROW15 = 14*16;
	private static final int ROW16 = 15*16;

	//Row One: Items which can't be obtained
	//null warning occupies space 0, should only show up if there's a bug.
	public static final int NULLWARN    = ROW1+0;
	public static final int DEWDROP	    = ROW1+1;
	public static final int PETAL	    = ROW1+2;
	public static final int SANDBAG     = ROW1+3;
	// Heaps (containers)
	public static final int BONES			= ROW1+4;
	public static final int REMAINS         = ROW1+5;
	public static final int TOMB			= ROW1+6;
	public static final int GRAVE			= ROW1+7;
	public static final int CHEST			= ROW1+8;
	public static final int LOCKED_CHEST	= ROW1+9;
	public static final int CRYSTAL_CHEST	= ROW1+10;
	// Placeholders
	public static final int WEAPON	= ROW1+11;
	public static final int ARMOR	= ROW1+12;
	public static final int RING	= ROW1+13;
	public static final int SMTH	= ROW1+14;

	//Row Two: Miscellaneous single use items
	public static final int GOLD	    = ROW2+0;
	public static final int TORCH	    = ROW2+1;
	public static final int STYLUS	    = ROW2+2;
	public static final int ANKH	    = ROW2+3;
	// Keys
	public static final int IRON_KEY		= ROW2+4;
	public static final int GOLDEN_KEY		= ROW2+5;
	public static final int SKELETON_KEY	= ROW2+6;
	//Boss Rewards
	public static final int BEACON	= ROW2+7;
	public static final int MASTERY	= ROW2+8;
	public static final int KIT		= ROW2+9;
	public static final int AMULET	= ROW2+10;
	public static final int WEIGHT  = ROW2+11;
	public static final int BOMB    = ROW2+12;
	public static final int DBL_BOMB= ROW2+13;
	public static final int HONEYPOT= ROW2+14;
	public static final int SHATTPOT= ROW2+15;

	//Row Three: Melee weapons
	public static final int KNUCKLEDUSTER	= ROW3+0;
	public static final int DAGGER			= ROW3+1;
	public static final int SHORT_SWORD		= ROW3+2;
	public static final int MAGES_STAFF     = ROW3+3;
	public static final int QUARTERSTAFF	= ROW3+4;
	public static final int SPEAR			= ROW3+5;
	public static final int MACE			= ROW3+6;
	public static final int SWORD			= ROW3+7;
	public static final int BATTLE_AXE		= ROW3+8;
	public static final int LONG_SWORD		= ROW3+9;
	public static final int WAR_HAMMER		= ROW3+10;
	public static final int GLAIVE			= ROW3+11;

	//Row Four: Missile weapons
	public static final int DART			= ROW4+0;
	public static final int BOOMERANG		= ROW4+1;
	public static final int INCENDIARY_DART	= ROW4+2;
	public static final int SHURIKEN		= ROW4+3;
	public static final int CURARE_DART		= ROW4+4;
	public static final int JAVELIN			= ROW4+5;
	public static final int TOMAHAWK		= ROW4+6;

	//Row Five: Armors
	public static final int ARMOR_CLOTH		= ROW5+0;
	public static final int ARMOR_LEATHER	= ROW5+1;
	public static final int ARMOR_MAIL		= ROW5+2;
	public static final int ARMOR_SCALE		= ROW5+3;
	public static final int ARMOR_PLATE		= ROW5+4;
	public static final int ARMOR_WARRIOR	= ROW5+5;
	public static final int ARMOR_MAGE		= ROW5+6;
	public static final int ARMOR_ROGUE		= ROW5+7;
	public static final int ARMOR_HUNTRESS	= ROW5+8;

	//Row Six: Wands
	public static final int WAND_MAGIC_MISSILE	= ROW6+0;
	public static final int WAND_FIREBOLT		= ROW6+1;
	public static final int WAND_FROST			= ROW6+2;
	public static final int WAND_LIGHTNING		= ROW6+3;
	public static final int WAND_DISINTEGRATION	= ROW6+4;
	public static final int WAND_PRISMATIC_LIGHT= ROW6+5;
	public static final int WAND_VENOM			= ROW6+6;
	public static final int WAND_LIVING_EARTH	= ROW6+7;
	public static final int WAND_BLAST_WAVE		= ROW6+8;
	public static final int WAND_CORRUPTION		= ROW6+9;
	public static final int WAND_WARDING      	= ROW6+10;
	public static final int WAND_REGROWTH		= ROW6+11;
	public static final int WAND_TRANSFUSION	= ROW6+12;

	//Row Seven: Rings
	public static final int RING_GARNET		= ROW7+0;
	public static final int RING_RUBY		= ROW7+1;
	public static final int RING_TOPAZ		= ROW7+2;
	public static final int RING_EMERALD	= ROW7+3;
	public static final int RING_ONYX		= ROW7+4;
	public static final int RING_OPAL		= ROW7+5;
	public static final int RING_TOURMALINE	= ROW7+6;
	public static final int RING_SAPPHIRE	= ROW7+7;
	public static final int RING_AMETHYST	= ROW7+8;
	public static final int RING_QUARTZ		= ROW7+9;
	public static final int RING_AGATE		= ROW7+10;
	public static final int RING_DIAMOND	= ROW7+11;

	//Row Eight: Artifacts with Static Images
	public static final int ARTIFACT_CLOAK      = ROW8+0;
	public static final int ARTIFACT_ARMBAND    = ROW8+1;
	public static final int ARTIFACT_CAPE       = ROW8+2;
	public static final int ARTIFACT_TALISMAN   = ROW8+3;
	public static final int ARTIFACT_HOURGLASS  = ROW8+4;
	public static final int ARTIFACT_TOOLKIT    = ROW8+5;
	public static final int ARTIFACT_SPELLBOOK  = ROW8+6;
	public static final int ARTIFACT_BEACON     = ROW8+7;
	public static final int ARTIFACT_CHAINS     = ROW8+8;

	//Row Nine: Artifacts with Dynamic Images
	public static final int ARTIFACT_HORN1      = ROW9+0;
	public static final int ARTIFACT_HORN2      = ROW9+1;
	public static final int ARTIFACT_HORN3      = ROW9+2;
	public static final int ARTIFACT_HORN4      = ROW9+3;
	public static final int ARTIFACT_CHALICE1   = ROW9+4;
	public static final int ARTIFACT_CHALICE2   = ROW9+5;
	public static final int ARTIFACT_CHALICE3   = ROW9+6;
	public static final int ARTIFACT_SANDALS    = ROW9+7;
	public static final int ARTIFACT_SHOES      = ROW9+8;
	public static final int ARTIFACT_BOOTS      = ROW9+9;
	public static final int ARTIFACT_GREAVES    = ROW9+10;
	public static final int ARTIFACT_ROSE1      = ROW9+11;
	public static final int ARTIFACT_ROSE2      = ROW9+12;
	public static final int ARTIFACT_ROSE3      = ROW9+13;

	//Row Ten: Scrolls
	public static final int SCROLL_KAUNAN	= ROW10+0;
	public static final int SCROLL_SOWILO	= ROW10+1;
	public static final int SCROLL_LAGUZ	= ROW10+2;
	public static final int SCROLL_YNGVI	= ROW10+3;
	public static final int SCROLL_GYFU		= ROW10+4;
	public static final int SCROLL_RAIDO	= ROW10+5;
	public static final int SCROLL_ISAZ		= ROW10+6;
	public static final int SCROLL_MANNAZ	= ROW10+7;
	public static final int SCROLL_NAUDIZ	= ROW10+8;
	public static final int SCROLL_BERKANAN	= ROW10+9;
	public static final int SCROLL_ODAL		= ROW10+10;
	public static final int SCROLL_TIWAZ	= ROW10+11;

	//Row Eleven: Potions
	public static final int POTION_CRIMSON		= ROW11+0;
	public static final int POTION_AMBER		= ROW11+1;
	public static final int POTION_GOLDEN		= ROW11+2;
	public static final int POTION_JADE			= ROW11+3;
	public static final int POTION_TURQUOISE    = ROW11+4;
	public static final int POTION_AZURE		= ROW11+5;
	public static final int POTION_INDIGO		= ROW11+6;
	public static final int POTION_MAGENTA		= ROW11+7;
	public static final int POTION_BISTRE		= ROW11+8;
	public static final int POTION_CHARCOAL		= ROW11+9;
	public static final int POTION_SILVER		= ROW11+10;
	public static final int POTION_IVORY		= ROW11+11;

	//Row Twelve: Seeds
	public static final int SEED_ROTBERRY	= ROW12+0;
	public static final int SEED_FIREBLOOM	= ROW12+1;
	public static final int SEED_STARFLOWER	= ROW12+2;
	public static final int SEED_BLINDWEED	= ROW12+3;
	public static final int SEED_SUNGRASS	= ROW12+4;
	public static final int SEED_ICECAP		= ROW12+5;
	public static final int SEED_STORMVINE	= ROW12+6;
	public static final int SEED_SORROWMOSS	= ROW12+7;
	public static final int SEED_DREAMFOIL	= ROW12+8;
	public static final int SEED_EARTHROOT	= ROW12+9;
	public static final int SEED_FADELEAF	= ROW12+10;
	public static final int SEED_BLANDFRUIT	= ROW12+11;

	//Row Thirteen: Food
	public static final int MEAT		= ROW13+0;
	public static final int STEAK		= ROW13+1;
	public static final int OVERPRICED	= ROW13+2;
	public static final int CARPACCIO	= ROW13+3;
	public static final int BLANDFRUIT	= ROW13+4;
	public static final int RATION		= ROW13+5;
	public static final int PASTY		= ROW13+6;

	//Row Fourteen: Quest Items
	public static final int SKULL	= ROW14+0;
	public static final int DUST	= ROW14+1;
	public static final int CANDLE	= ROW14+2;
	public static final int EMBER	= ROW14+3;
	public static final int PICKAXE	= ROW14+4;
	public static final int ORE		= ROW14+5;
	public static final int TOKEN	= ROW14+6;

	//Row Fifteen: Containers/Bags
	public static final int VIAL	    = ROW15+0;
	public static final int POUCH	    = ROW15+1;
	public static final int HOLDER	    = ROW15+2;
	public static final int BANDOLIER   = ROW15+3;
	public static final int HOLSTER	    = ROW15+4;

	//Row Sixteen: Unused

}
