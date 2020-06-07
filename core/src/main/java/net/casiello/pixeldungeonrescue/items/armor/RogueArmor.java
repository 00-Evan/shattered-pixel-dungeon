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

package net.casiello.pixeldungeonrescue.items.armor;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.Actor;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Blindness;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Invisibility;
import net.casiello.pixeldungeonrescue.actors.mobs.Mob;
import net.casiello.pixeldungeonrescue.effects.CellEmitter;
import net.casiello.pixeldungeonrescue.effects.Speck;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfTeleportation;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.scenes.CellSelector;
import net.casiello.pixeldungeonrescue.scenes.GameScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class RogueArmor extends ClassArmor {
	
	{
		image = ItemSpriteSheet.ARMOR_ROGUE;
	}
	
	@Override
	public void doSpecial() {
		GameScene.selectCell( teleporter );
	}
	
	protected CellSelector.Listener teleporter = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				
				PathFinder.buildDistanceMap(curUser.pos, Dungeon.level.passable, 8);
				
				if ( PathFinder.distance[target] == Integer.MAX_VALUE ||
					!Dungeon.level.heroFOV[target] ||
					!(Dungeon.level.passable[target] || Dungeon.level.avoid[target]) ||
					Actor.findChar( target ) != null) {
					
					GLog.w( Messages.get(RogueArmor.class, "fov") );
					return;
				}

				charge -= 35;
				updateQuickslot();
				
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (Dungeon.level.adjacent(mob.pos, curUser.pos) && mob.alignment != Char.Alignment.ALLY) {
						Buff.prolong( mob, Blindness.class, 5 );
						if (mob.state == mob.HUNTING) mob.state = mob.WANDERING;
						mob.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
					}
				}
				Buff.affect(curUser, Invisibility.class, 10f);

				CellEmitter.get( curUser.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
				ScrollOfTeleportation.appear( curUser, target );
				Sample.INSTANCE.play( Assets.SND_PUFF );
				Dungeon.level.occupyCell(curUser );
				Dungeon.observe();
				GameScene.updateFog();
				
				curUser.spendAndNext( Actor.TICK );
			}
		}
		
		@Override
		public String prompt() {
			return Messages.get(RogueArmor.class, "prompt");
		}
	};
}