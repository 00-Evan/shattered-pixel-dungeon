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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.VaultFlameTraps;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultDM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultDM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultGhoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultGolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultShaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultSkeleton;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.GridBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.AlternatingTrapsRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultCircleRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultCrossRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultEnemyCenterRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultFinalRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultLasersRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultLongRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure.VaultBookcaseTreasureRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure.VaultFlamePathRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure.VaultLaserTreasureRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure.VaultManyScansRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultQuadrantsRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultRingRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultRingsRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultSimpleEnemyTreasureRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure.VaultMultipleEnemyTreasureRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure.VaultSingleEnemyTreasureRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class VaultLevel extends CityLevel {

	@Override
	protected boolean build() {
		itemsToSpawn.clear();

		for (int i = 0; i < 8; i++){
			Item item = createEquipment(0);
			//TODO consumables
			addItemToSpawn(item);
		}
		addItemToSpawn(Generator.randomUsingDefaults(Generator.Category.FOOD));
		addItemToSpawn(Generator.randomUsingDefaults(Generator.Category.FOOD));
		addItemToSpawn(Generator.randomUsingDefaults(Generator.Category.FOOD));

		return super.build();
	}

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();

		initRooms.add(roomEntrance = new VaultEntranceRoom());

		initRooms.add(new VaultRingRoom());
		initRooms.add(new VaultRingRoom());
		initRooms.add(new VaultCircleRoom());
		initRooms.add(new VaultCircleRoom());
		initRooms.add(new VaultCrossRoom());
		initRooms.add(new VaultCrossRoom());
		initRooms.add(new VaultQuadrantsRoom());
		initRooms.add(new VaultQuadrantsRoom());
		initRooms.add(new VaultRingsRoom());
		initRooms.add(new VaultRingsRoom());

		initRooms.add(new VaultEnemyCenterRoom());
		initRooms.add(new VaultEnemyCenterRoom());
		initRooms.add(new VaultSimpleEnemyTreasureRoom());
		initRooms.add(new AlternatingTrapsRoom());
		initRooms.add(new VaultLasersRoom());

		initRooms.add(new VaultLaserTreasureRoom());
		initRooms.add(new VaultFlamePathRoom());

		initRooms.add(new VaultBookcaseTreasureRoom());
		initRooms.add(new VaultSingleEnemyTreasureRoom());

		initRooms.add(new VaultMultipleEnemyTreasureRoom());
		initRooms.add(new VaultManyScansRoom());

		initRooms.add(new VaultLongRoom());
		initRooms.add(new VaultLongRoom());

		initRooms.add(new VaultFinalRoom());
		return initRooms;
	}

	@Override
	public float levelExplorePercent(int depth) {
		//very simple for now, we just look at all discoverable cells.
		// Each 1% seen = 1.12% explored. 90% seem = 100% explored
		int seen = 0, total = 0;
		for (int i = 0; i < length; i++){
			if (discoverable[i]) total++;
			if (visited[i]) seen++;
		}
		return Math.min(1, (seen*1.12f)/total);
	}

	@Override
	protected Builder builder() {
		return new GridBuilder();
	}

	@Override
	protected int nTraps() {
		return 0;
	}

	@Override
	public boolean activateTransition(Hero hero, LevelTransition transition) {
		//walking onto transitions does nothing, need to use crystal
		return false;
	}

	//only occurs in levelgen, no need to bundle these
	ArrayList<ArrayList<Item>> equipmentLoot = new ArrayList<>();
	{
		equipmentLoot.add(new ArrayList<>());
		equipmentLoot.add(new ArrayList<>());
		equipmentLoot.add(new ArrayList<>());
		equipmentLoot.add(new ArrayList<>());
	}

	public Item createEquipment(int lootTier) {

		ArrayList<Item> lootList = equipmentLoot.get(lootTier);

		if (lootList.isEmpty()) {
			Item loot;
			//first weapon (lower tier, more upgrades)
			switch (lootTier) {
				default:
				case 0:
					loot = Generator.randomUsingDefaults(Generator.Category.WEP_T2);
					break;
				case 1:
					loot = Generator.randomUsingDefaults(Generator.Category.WEP_T2);
					break;
				case 2:
					loot = Generator.randomUsingDefaults(Generator.Category.WEP_T3);
					break;
				case 3:
					loot = Generator.randomUsingDefaults(Generator.Category.WEP_T4);
					break;
			}
			if (lootTier == 0) { //always +0 at T0
				loot.level(lootTier);
			} else {
				loot.level(lootTier + Random.Int(2));
			}
			if (Random.Int(3) >= lootTier) {
				((Weapon) loot).enchant(null);
			} else {
				((Weapon) loot).enchant();
			}
			lootList.add(loot);

			//second weapon (higher tier, fewer upgrades)
			switch (lootTier) {
				default:
				case 0:
					loot = Generator.randomUsingDefaults(Generator.Category.WEP_T2);
					break;
				case 1:
					loot = Generator.randomUsingDefaults(Generator.Category.WEP_T3);
					break;
				case 2:
					loot = Generator.randomUsingDefaults(Generator.Category.WEP_T4);
					break;
				case 3:
					loot = Generator.randomUsingDefaults(Generator.Category.WEP_T5);
					break;
			}
			loot.level(Math.max(0, Random.Int(2) + lootTier - 1));
			if (Random.Int(3) >= lootTier) {
				((Weapon) loot).enchant(null);
			} else {
				((Weapon) loot).enchant();
			}
			lootList.add(loot);

			//missile weapon (same level/tiering as 2nd weapon)
			switch (lootTier) {
				default:
				case 0:
					loot = Generator.randomUsingDefaults(Generator.Category.MIS_T2);
					break;
				case 1:
					loot = Generator.randomUsingDefaults(Generator.Category.MIS_T3);
					break;
				case 2:
					loot = Generator.randomUsingDefaults(Generator.Category.MIS_T4);
					break;
				case 3:
					loot = Generator.randomUsingDefaults(Generator.Category.MIS_T5);
					break;
			}
			loot.level(Math.max(0, Random.Int(2) + lootTier - 1));
			if (Random.Int(3) >= lootTier) {
				((Weapon) loot).enchant(null);
			} else {
				((Weapon) loot).enchant();
			}
			lootList.add(loot);

			//armor (same level/tiering as 2nd weapon)
			switch (lootTier) {
				default:
				case 0:
					loot = new LeatherArmor();
					break;
				case 1:
					loot = new MailArmor();
					break;
				case 2:
					loot = new ScaleArmor();
					break;
				case 3:
					loot = new PlateArmor();
					break;
			}
			loot.level(Math.max(0, Random.Int(2) + lootTier - 1));
			if (Random.Int(3) >= lootTier) {
				((Armor) loot).inscribe(null);
			} else {
				((Armor) loot).inscribe();
			}
			lootList.add(loot);

			//wand (some wands are banned)
			do {
				loot = Generator.randomUsingDefaults(Generator.Category.WAND);
			} while (loot instanceof WandOfRegrowth || loot instanceof WandOfTransfusion || loot instanceof WandOfCorruption);
			loot.level(Math.max(0, Random.Int(2) + lootTier - 1));
			((Wand)loot).curCharges = ((Wand)loot).maxCharges;
			lootList.add(loot);

			//ring (no ring at T0, some rings are banned)
			if (lootTier > 0) {
				do {
					loot = Generator.randomUsingDefaults(Generator.Category.RING);
				} while (loot instanceof RingOfWealth || loot instanceof RingOfMight || loot instanceof RingOfForce);
				loot.level(Math.max(0, Random.Int(2) + lootTier - 1));
				lootList.add(loot);
			}

			Random.shuffle(lootList);
		}

		Item loot = lootList.remove(0);
		if (loot != null) {
			loot.cursed = false;
			loot.identify(false);
		}
		return loot;
	}

	public static Class<?extends Mob>[] T1Mobs = new Class[]{
			VaultSkeleton.class,
			VaultDM100.class
	};

	public static Class<?extends Mob>[] T2Mobs = new Class[]{
			VaultShaman.class,
			VaultDM200.class,
			VaultGhoul.class //only if solo
	};

	public static Class<?extends Mob>[] T3Mobs = new Class[]{
			//vault ghoul if more than one
			VaultElemental.class,
			VaultGolem.class
	};

	private ArrayList<Class<?extends Mob>> mobsToSpawn = new ArrayList<>();

	@Override
	public Mob createMob() {
		if (mobsToSpawn.isEmpty()){
			//rotation is a total of 4/3/2 mobs at T1/2/3 currently
			Collections.addAll(mobsToSpawn, T1Mobs);
			Collections.addAll(mobsToSpawn, T1Mobs);
			Collections.addAll(mobsToSpawn, T2Mobs);
			Collections.addAll(mobsToSpawn, T3Mobs);
			Random.shuffle(mobsToSpawn);
		}
		return Reflection.newInstance(mobsToSpawn.remove(0));
	}

	@Override
	protected void createMobs() {
		//mob creation handled by individual rooms
	}

	@Override
	public void occupyCell(Char ch) {
		super.occupyCell(ch);
		//extra check to ensure vault is left if quest is completed or old
		if (ch == Dungeon.hero && (Imp.Quest.isCompleted() || Imp.Quest.isOld())){
			beforeTransition();
			InterlevelScene.curTransition = new LevelTransition(Dungeon.level,
					Dungeon.hero.pos,
					LevelTransition.Type.BRANCH_ENTRANCE,
					Dungeon.depth,
					0,
					LevelTransition.Type.BRANCH_EXIT);
			InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
			Game.switchScene( InterlevelScene.class );
		}
	}

	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
		//copypasta from super.createItems
		for (Item item : itemsToSpawn) {
			int cell = randomDropCell();
			drop( item, cell ).type = Heap.Type.HEAP;
			if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
				map[cell] = Terrain.GRASS;
				losBlocking[cell] = false;
			}
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		return entrance()-width();
	}

	public static class VaultFlameTrap extends Trap {

		{
			color = BLACK;
			shape = DOTS;

			canBeHidden = false;
			active = false;
		}

		@Override
		public void activate() {
			//does nothing, this trap is just decoration and is always deactivated
		}

		public static void setupTrap(Level level, int cell, int initialCD, int afterTriggerCD, int triggers){
			VaultFlameTraps traps = Blob.seed(0, 0, VaultFlameTraps.class, level);
			traps.curCooldowns[cell] = initialCD;
			traps.afterTriggerCooldowns[cell] = afterTriggerCD;
			traps.triggersAfterCooldown[cell] = triggers;
			level.setTrap(new VaultLevel.VaultFlameTrap().reveal(), cell);
			Painter.set(level, cell, Terrain.INACTIVE_TRAP);
		}

	}

}
