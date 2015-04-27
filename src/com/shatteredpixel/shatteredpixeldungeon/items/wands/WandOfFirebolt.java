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
package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

//TODO: rename to fireblast
public class WandOfFirebolt extends Wand {

	{
		name = "Wand of Firebolt";
		image = ItemSpriteSheet.WAND_FIREBOLT;

		collisionProperties = Ballistica.STOP_TERRAIN;
	}

	private HashSet<Integer> affectedCells;
	private int direction = 0;
	
	@Override
	protected void onZap( Ballistica bolt ) {

		int level = level();

		affectedCells = new HashSet<>();

		int maxDist = 1 + chargesPerCast()*2;
		int dist = Math.min(bolt.dist, maxDist);

		for (int i = 0; i < Level.NEIGHBOURS8.length; i++){
			if (bolt.sourcePos+Level.NEIGHBOURS8[i] == bolt.path.get(1)){
				direction = i;
				break;
			}
		}

		float strength = maxDist;
		for (int c : bolt.subPath(1, dist)) {
			strength--; //as we start at dist 1, not 0.
			affectedCells.add(c);
			spreadFlames(c + Level.NEIGHBOURS8[left(direction)], strength - 1);
			spreadFlames(c + Level.NEIGHBOURS8[direction], strength - 1);
			spreadFlames(c + Level.NEIGHBOURS8[right(direction)], strength - 1);
		}

		if (Level.flamable[bolt.sourcePos]){
			GameScene.add( Blob.seed( bolt.sourcePos, 2, Fire.class ) );
		}

		for( int cell : affectedCells){
			GameScene.add( Blob.seed( cell, 1+chargesPerCast(), Fire.class ) );
			Char ch = Actor.findChar( cell );
			if (ch != null) {

				ch.damage(Random.NormalIntRange(1, (int) (8 + (level * level * (1 + chargesPerCast()) / 6f))), this);
				Buff.affect( ch, Burning.class ).reignite( ch );
				switch(chargesPerCast()){
					case 1:
						Buff.affect(ch, Cripple.class, 3f); break;
					case 2:
						Buff.affect(ch, Cripple.class, 6f); break;
					case 3:
						Buff.affect(ch, Paralysis.class, 3f); break;
					case 4:
						Buff.affect(ch, Paralysis.class, 6f); break;
				}
			}
		}
	}

	//burn... BURNNNNN!.....
	private void spreadFlames(int cell, float strength){
		if (strength >= 0 && Level.passable[cell]){
			affectedCells.add(cell);
			if (strength >= 1.5f) {
				spreadFlames(cell + Level.NEIGHBOURS8[left(direction)], strength-1.5f);
				spreadFlames(cell + Level.NEIGHBOURS8[direction], strength-1.5f);
				spreadFlames(cell + Level.NEIGHBOURS8[right(direction)], strength-1.5f);
			}
		}
	}

	private int left(int direction){
		return direction == 0 ? 7 : direction-1;
	}

	private int right(int direction){
		return direction == 7 ? 0 : direction+1;
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//acts like blazing enchantment, package conflict.....
		new com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Fire()
				.proc( staff, attacker, defender, damage);
	}

	@Override
	protected void fx( Ballistica bolt, Callback callback ) {
		//TODO: add new effects
		//MagicMissile.fire( curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
		callback.call();
	}

	@Override
	protected int chargesPerCast() {
		//consumes 40% of current charges, rounded up, with a minimum of one.
		return Math.max(1, (int)Math.ceil(curCharges*0.4f));
	}

	@Override
	public String desc() {
		//TODO add new description
		return
			"This wand unleashes bursts of magical fire. It will ignite " +
			"flammable terrain, and will damage and burn a creature it hits.";
	}
}
