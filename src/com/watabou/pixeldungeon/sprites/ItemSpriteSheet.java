/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.sprites;

public class ItemSpriteSheet {
	
	// Placeholders
	public static final int WEAPON	= 5;
	public static final int ARMOR	= 6;
	public static final int RING	= 7;
	public static final int SMTH	= 127;
	
	// Keys
	public static final int SKELETON_KEY	= 8;
	public static final int IRON_KEY		= 9;
	public static final int GOLDEN_KEY		= 10;
	
	// Melee weapons
	public static final int SHORT_SWORD		= 2;
	public static final int KNUCKLEDUSTER	= 16;
	public static final int QUARTERSTAFF	= 17;
	public static final int MACE			= 18;
	public static final int DAGGER			= 19;
	public static final int SWORD			= 20;
	public static final int LONG_SWORD		= 21;
	public static final int BATTLE_AXE		= 22;
	public static final int WAR_HAMMER		= 23;
	public static final int SPEAR			= 29;
	public static final int GLAIVE			= 30;
	
	// Missile weapons
	public static final int SHURIKEN		= 15;
	public static final int DART			= 31;
	public static final int BOOMERANG		= 106;
	public static final int TOMAHAWK		= 107;
	public static final int INCENDIARY_DART	= 108;
	public static final int CURARE_DART		= 109;
	public static final int JAVELIN			= 110;
	
	// Armors
	public static final int ARMOR_CLOTH		= 24;
	public static final int ARMOR_LEATHER	= 25;
	public static final int ARMOR_MAIL		= 26;
	public static final int ARMOR_SCALE		= 27;
	public static final int ARMOR_PLATE		= 28;
	public static final int ARMOR_ROGUE		= 96;
	public static final int ARMOR_WARRIOR	= 97;
	public static final int ARMOR_MAGE		= 98;
	public static final int ARMOR_HUNTRESS	= 99;
	
	// Wands
	public static final int WAND_MAGIC_MISSILE	= 3;
	public static final int WAND_HOLLY			= 48;
	public static final int WAND_YEW			= 49;
	public static final int WAND_EBONY			= 50;
	public static final int WAND_CHERRY			= 51;
	public static final int WAND_TEAK			= 52;
	public static final int WAND_ROWAN			= 53;
	public static final int WAND_WILLOW			= 54;
	public static final int WAND_MAHOGANY		= 55;
	public static final int WAND_BAMBOO			= 68;
	public static final int WAND_PURPLEHEART	= 69;
	public static final int WAND_OAK			= 70;
	public static final int WAND_BIRCH			= 71;
	
	// Rings
	public static final int RING_DIAMOND	= 32;
	public static final int RING_OPAL		= 33;
	public static final int RING_GARNET		= 34;
	public static final int RING_RUBY		= 35;
	public static final int RING_AMETHYST	= 36;
	public static final int RING_TOPAZ		= 37;
	public static final int RING_ONYX		= 38;
	public static final int RING_TOURMALINE	= 39;
	public static final int RING_EMERALD	= 72;
	public static final int RING_SAPPHIRE	= 73;
	public static final int RING_QUARTZ		= 74;
	public static final int RING_AGATE		= 75;
	
	// Potions
	public static final int POTION_TURQUOISE	= 56;
	public static final int POTION_CRIMSON		= 57;
	public static final int POTION_AZURE		= 58;
	public static final int POTION_JADE			= 59;
	public static final int POTION_GOLDEN		= 60;
	public static final int POTION_MAGENTA		= 61;
	public static final int POTION_CHARCOAL		= 62;
	public static final int POTION_IVORY		= 63;
	public static final int POTION_AMBER		= 64;
	public static final int POTION_BISTRE		= 65;
	public static final int POTION_INDIGO		= 66;
	public static final int POTION_SILVER		= 67;
	
	// Scrolls
	public static final int SCROLL_KAUNAN	= 40;
	public static final int SCROLL_SOWILO	= 41;
	public static final int SCROLL_LAGUZ	= 42;
	public static final int SCROLL_YNGVI	= 43;
	public static final int SCROLL_GYFU		= 44;
	public static final int SCROLL_RAIDO	= 45;
	public static final int SCROLL_ISAZ		= 46;
	public static final int SCROLL_MANNAZ	= 47;
	public static final int SCROLL_NAUDIZ	= 76;
	public static final int SCROLL_BERKANAN	= 77;
	public static final int SCROLL_ODAL		= 78;
	public static final int SCROLL_TIWAZ	= 79;
	
	// Seeds
	public static final int SEED_FIREBLOOM	= 88;
	public static final int SEED_ICECAP		= 89;
	public static final int SEED_SORROWMOSS	= 90;
	public static final int SEED_BLINDWEED	= 91;
	public static final int SEED_SUNGRASS	= 92;
	public static final int SEED_EARTHROOT	= 93;
	public static final int SEED_FADELEAF	= 94;
	public static final int SEED_ROTBERRY	= 95;
	
	// Quest items
	public static final int ROSE	= 100;
	public static final int PICKAXE	= 101;
	public static final int ORE		= 102;
	public static final int SKULL	= 103;
	public static final int DUST	= 121;
	public static final int TOKEN	= 122;
	
	// Heaps (containers)
	public static final int BONES			= 0;
	public static final int CHEST			= 11;
	public static final int LOCKED_CHEST	= 12;
	public static final int TOMB			= 13;
	public static final int CRYSTAL_CHEST	= 105;
	
	// Food
	public static final int RATION		= 4;
	public static final int PASTY		= 112;
	public static final int MEAT		= 113;
	public static final int STEAK		= 114;
	public static final int OVERPRICED	= 115;
	public static final int CARPACCIO	= 116;
	
	// Bags
	public static final int POUCH	= 83;
	public static final int HOLDER	= 104;
	public static final int HOLSTER	= 111;
	
	// Misc
	public static final int ANKH	= 1;
	public static final int GOLD	= 14;
	public static final int STYLUS	= 80;
	public static final int DEWDROP	= 81;
	public static final int MASTERY	= 82;
	public static final int TORCH	= 84;
	public static final int BEACON	= 85;
	public static final int KIT		= 86;
	public static final int AMULET	= 87;
	public static final int VIAL	= 120;
}
