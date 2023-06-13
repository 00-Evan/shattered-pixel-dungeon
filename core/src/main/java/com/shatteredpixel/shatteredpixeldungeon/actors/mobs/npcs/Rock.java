/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.CorgSeed;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RockSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Rock extends NPC {
	// 황금씨앗의 단독 퀘스트

	{
		spriteClass = RockSprite.class;

		properties.add(Property.IMMOVABLE);
	}


	@Override
	protected boolean act() {
		// 아뮬렛 회수시 삭제
		if (Dungeon.hero.buff(AscensionChallenge.class) != null){
			die(null);
			return true;
		}

		if (Dungeon.level.visited[pos] ){
			Notes.add( Notes.Landmark.ROCK );
		}

		if (Dungeon.level.pit[pos]){
			Chasm.mobFall(this);
			GLog.n(Messages.get(Rock.class, "fall"));
			return true;
		}


		return super.act();
	}



	@Override
	public void die( Object cause ) {
		super.die(cause);
		Dungeon.level.drop(new CorgSeed() , pos);
		Notes.remove(Notes.Landmark.ROCK);
	}


	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}


	@Override
	public void damage( int dmg, Object src ) {
		//do nothing
	}

	@Override
	public boolean add( Buff buff ) {
		return false;
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact(Char c) {

		Dungeon.hero.sprite.zap( pos );

		Ballistica moveto = new Ballistica(c.pos, pos, Ballistica.STOP_CHARS );

		int cell = moveto.collisionPos;

		int jumpto = moveto.dist;
		while (Actor.findChar(cell) != null ) {
			try{
				cell = moveto.path.get(jumpto);
				if( Actor.findChar(moveto.path.get(jumpto+1)) == null) {
					jumpto++;
				}else {
					break;
				}
			}
			catch (IndexOutOfBoundsException e){
				break;
			}
		}
		final int dest = cell;

		Rock rock = new Rock();
		if (Actor.findChar(dest) == null && Dungeon.level.passable[dest] || Dungeon.level.avoid[dest] ) {
			sprite.move(pos,dest);
			pos = dest;
			Dungeon.level.occupyCell(rock);
			Dungeon.level.pressCell(pos);
			Dungeon.hero.spendAndNext(TICK);

		} else if (!Dungeon.level.passable[jumpto] && Dungeon.level.solid[jumpto]){
			super.interact(c);
		} else {
			return true;
		}

		Sample.INSTANCE.play( Assets.Sounds.ROCKS );

		sprite.turnTo(pos-1,pos);
		return true;
	}

	public static class Quest {
		//황금빛 씨앗의 고유 퀘스트
		private static boolean spawned;
		private static int depth;

		public static void reset() {
			spawned = false;
		}

		private static final String NODE = "rock";
		private static final String SPAWNED = "spawned";
		private static final String DEPTH = "depth";

		public static void storeInBundle(Bundle bundle) {

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if (spawned) {

				node.put(DEPTH, depth);

			}
			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle) {

			Bundle node = bundle.getBundle(NODE);

			if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {

				depth = node.getInt(DEPTH);

			} else {
				reset();
			}
		}

		public static void spawn(RegularLevel level) {
			//지옥 전까지 랜덤 출연
			if (!spawned && Dungeon.depth > 1  && Random.Int(20 - Dungeon.depth) == 0
					&&( Dungeon.depth != 5
					|| Dungeon.depth != 10
					|| Dungeon.depth != 15
					|| Dungeon.depth != 20)) {

				Rock rock = new Rock();
				do {
					rock.pos = level.randomRespawnCell(rock);
				} while (rock.pos == -1 || level.traps.get(rock.pos) != null || level.findMob( rock.pos ) != null ||
						!(level.passable[rock.pos + PathFinder.CIRCLE4[0]] && level.passable[rock.pos + PathFinder.CIRCLE4[2]]) ||
						!(level.passable[rock.pos + PathFinder.CIRCLE4[1]] && level.passable[rock.pos + PathFinder.CIRCLE4[3]]));
				level.mobs.add(rock);

				spawned = true;
				depth = Dungeon.depth;

			}
		}
	}
}
