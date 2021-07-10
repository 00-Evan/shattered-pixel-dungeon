/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.items.wands;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.blobs.Blob;
import com.saqfish.spdnet.actors.blobs.CorrosiveGas;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Ooze;
import com.saqfish.spdnet.effects.CellEmitter;
import com.saqfish.spdnet.effects.MagicMissile;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.effects.particles.CorrosionParticle;
import com.saqfish.spdnet.items.weapon.melee.MagesStaff;
import com.saqfish.spdnet.mechanics.Ballistica;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfCorrosion extends Wand {

	{
		image = ItemSpriteSheet.WAND_CORROSION;

		collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
	}

	@Override
	public void onZap(Ballistica bolt) {
		CorrosiveGas gas = Blob.seed(bolt.collisionPos, 50 + 10 * buffedLvl(), CorrosiveGas.class);
		CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.CORROSION), 10 );
		gas.setStrength(2 + buffedLvl());
		GameScene.add(gas);
		Sample.INSTANCE.play(Assets.Sounds.GAS);

		for (int i : PathFinder.NEIGHBOURS9) {
			Char ch = Actor.findChar(bolt.collisionPos + i);
			if (ch != null) {
				wandProc(ch, chargesPerCast());
			}
		}
		
		if (Actor.findChar(bolt.collisionPos) == null){
			Dungeon.level.pressCell(bolt.collisionPos);
		}
	}

	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(
				curUser.sprite.parent,
				MagicMissile.CORROSION,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		if (Random.Int( buffedLvl() + 3 ) >= 2) {
			
			Buff.affect( defender, Ooze.class ).set( Ooze.DURATION );
			CellEmitter.center(defender.pos).burst( CorrosionParticle.SPLASH, 5 );
			
		}
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( ColorMath.random( 0xAAAAAA, 0xFF8800) );
		particle.am = 0.6f;
		particle.setLifespan( 1f );
		particle.acc.set(0, 20);
		particle.setSize( 0.5f, 3f );
		particle.shuffleXY( 1f );
	}

	@Override
	public String statsDesc() {
		if (levelKnown)
			return Messages.get(this, "stats_desc", 2+buffedLvl());
		else
			return Messages.get(this, "stats_desc", 2);
	}

}
