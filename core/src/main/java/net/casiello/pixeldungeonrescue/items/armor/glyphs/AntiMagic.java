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

package net.casiello.pixeldungeonrescue.items.armor.glyphs;

import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Charm;
import net.casiello.pixeldungeonrescue.actors.buffs.Degrade;
import net.casiello.pixeldungeonrescue.actors.buffs.Hex;
import net.casiello.pixeldungeonrescue.actors.buffs.Vulnerable;
import net.casiello.pixeldungeonrescue.actors.buffs.Weakness;
import net.casiello.pixeldungeonrescue.actors.mobs.Eye;
import net.casiello.pixeldungeonrescue.actors.mobs.DM100;
import net.casiello.pixeldungeonrescue.actors.mobs.Shaman;
import net.casiello.pixeldungeonrescue.actors.mobs.Warlock;
import net.casiello.pixeldungeonrescue.actors.mobs.Yog;
import net.casiello.pixeldungeonrescue.actors.mobs.YogFist;
import net.casiello.pixeldungeonrescue.items.armor.Armor;
import net.casiello.pixeldungeonrescue.items.wands.WandOfBlastWave;
import net.casiello.pixeldungeonrescue.items.wands.WandOfDisintegration;
import net.casiello.pixeldungeonrescue.items.wands.WandOfFireblast;
import net.casiello.pixeldungeonrescue.items.wands.WandOfFrost;
import net.casiello.pixeldungeonrescue.items.wands.WandOfLightning;
import net.casiello.pixeldungeonrescue.items.wands.WandOfLivingEarth;
import net.casiello.pixeldungeonrescue.items.wands.WandOfMagicMissile;
import net.casiello.pixeldungeonrescue.items.wands.WandOfPrismaticLight;
import net.casiello.pixeldungeonrescue.items.wands.WandOfTransfusion;
import net.casiello.pixeldungeonrescue.items.wands.WandOfWarding;
import net.casiello.pixeldungeonrescue.levels.traps.DisintegrationTrap;
import net.casiello.pixeldungeonrescue.levels.traps.GrimTrap;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class AntiMagic extends Armor.Glyph {

	private static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x88EEFF );
	
	public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( Charm.class );
		RESISTS.add( Weakness.class );
		RESISTS.add( Vulnerable.class );
		RESISTS.add( Hex.class );
		RESISTS.add( Degrade.class );
		
		RESISTS.add( DisintegrationTrap.class );
		RESISTS.add( GrimTrap.class );

		RESISTS.add( WandOfBlastWave.class );
		RESISTS.add( WandOfDisintegration.class );
		RESISTS.add( WandOfFireblast.class );
		RESISTS.add( WandOfFrost.class );
		RESISTS.add( WandOfLightning.class );
		RESISTS.add( WandOfLivingEarth.class );
		RESISTS.add( WandOfMagicMissile.class );
		RESISTS.add( WandOfPrismaticLight.class );
		RESISTS.add( WandOfTransfusion.class );
		RESISTS.add( WandOfWarding.Ward.class );
		
		RESISTS.add( DM100.LightningBolt.class );
		RESISTS.add( Shaman.EarthenBolt.class );
		RESISTS.add( Warlock.DarkBolt.class );
		RESISTS.add( Eye.DeathGaze.class );
		RESISTS.add( Yog.BurningFist.DarkBolt.class );
		RESISTS.add( YogFist.BrightFist.LightBeam.class );
		RESISTS.add( YogFist.DarkFist.DarkBolt.class );
	}
	
	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, see Hero.damage
		return damage;
	}
	
	public static int drRoll( int level ){
		return Random.NormalIntRange(level, 4 + (level*2));
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return TEAL;
	}

}