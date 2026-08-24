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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
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
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDeepSleep;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFear;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFlock;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfShock;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.GridBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CityPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultFinalRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultSimpleEnemyTreasureRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultTokensRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure.VaultTreasureRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class VaultLevel extends CityLevel {

	@Override
	public void playLevelMusic() {
		Music.INSTANCE.play(Assets.Music.CITY_TENSE, true);
	}

	@Override
	protected boolean build() {
		itemsToSpawn.clear();

		for (int i = 0; i < 4; i++){
			addItemToSpawn(createEquipment(0));
		}
		addItemToSpawn(new Dart());
		for (int i = 0; i < 5; i++){
			addItemToSpawn(createConsumabe(0));
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
		VaultRoom.setupChances();

		int i = 0;
		while (i < 9){
			VaultRoom r = VaultRoom.createRoom();
			i += r.sizeFactor();
			initRooms.add(r);
		}
		initRooms.add( new VaultTokensRoom() );
		initRooms.add( new VaultSimpleEnemyTreasureRoom() );

		VaultTreasureRoom.generateRoomList();
		for (i = 0; i < 7; i++){
			initRooms.add(VaultTreasureRoom.nextRoom());
		}

		initRooms.add(new VaultFinalRoom());
		return initRooms;
	}

	@Override
	public float levelExplorePercent(int depth) {
		// Each 1% of tiles seen = 1.25% explored. 80% seen = 100% explored
		int seen = 0, total = 0;
		for (int i = 0; i < length; i++){
			if (discoverable[i]) total++;
			if (visited[i]) seen++;
		}
		return Math.min(1, (seen*1.25f)/total);
	}

	@Override
	protected Builder builder() {
		return new GridBuilder();
	}

	@Override
	protected Painter painter() {
		return new CityPainter(){
			public float hiddenDoorChance( Level l ){
				return 0; //no hidden doors in the vault
			}
		}.setWater(0.15f, 12) //water is less common and more clustered (a few leaks with lack of maintenance)
				.setGrass(0.30f, 3) //grass is a little more common (overgrowth over time)
				.setTraps(nTraps(), trapClasses(), trapChances());
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
	// use arrays here as we want to be able to track and access indices
	// this lets us garuntee an even distribution of loot
	// more specifically, every 6 items generated from T2/3 and T0/1 are garunteed to be:
	// 2x melee weapon, 1x armor, 1x thrown weapon, 1x wand, 1x ring
	Item[][] equipmentLoot = new Item[4][];
	int higherTierIdx = 0;
	int lowerTierIdx = 0;

	public void setupEquipment(){
		for (int i = 0; i < equipmentLoot.length; i++){
			boolean empty = true;
			if (equipmentLoot[i] != null) {
				for (int j = 0; j < equipmentLoot[i].length; j++) {
					if (equipmentLoot[i][j] != null) {
						empty = false;
					}
				}
			}
			if (empty){
				setupEquipmentAtTier(i);
			}
		}
	}

	//cannot generate two of the same equipment item (except armor)
	// note that this does currently result in infinite loops if too many items are generated
	// currently we're designing around a theoretical max of about 12 genereated from each tier, which is almost 2x the actual max
	private HashSet<Class<? extends Item>> generatedClasses = new HashSet<>();

	public void setupEquipmentAtTier(int lootTier){

		ArrayList<Item> lootList = new ArrayList<>();

		Item loot;
		//first weapon (lower tier, more upgrades)
		do {
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
		//T2 weapon duplicates allowed, because so many can be generated
		} while (lootTier > 1 && generatedClasses.contains(loot.getClass()));
		generatedClasses.add(loot.getClass());
		if (lootTier == 0) { //always +0 at T0
			loot.level(lootTier);
		} else {
			loot.level(lootTier+1);
		}
		if (Random.Int(3) >= lootTier) {
			((Weapon) loot).enchant(null);
		} else {
			((Weapon) loot).enchant();
		}
		lootList.add(loot);

		//second weapon (higher tier, fewer upgrades)
		do {
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
		//T2 weapon duplicates allowed, because so many can be generated
		} while (lootTier > 0 && generatedClasses.contains(loot.getClass()));
		generatedClasses.add(loot.getClass());
		loot.level(lootTier);
		if (Random.Int(3) >= lootTier) {
			((Weapon) loot).enchant(null);
		} else {
			((Weapon) loot).enchant();
		}
		lootList.add(loot);

		//missile weapon (same level/tiering as 2nd weapon)
		do {
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
		} while (generatedClasses.contains(loot.getClass()));
		generatedClasses.add(loot.getClass());
		loot.level(lootTier);
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
		//skip duplicate check, only 1 armor per tier atm anyway
		generatedClasses.add(loot.getClass());
		loot.level(lootTier);
		if (Random.Int(3) >= lootTier) {
			((Armor) loot).inscribe(null);
		} else {
			((Armor) loot).inscribe();
		}
		lootList.add(loot);

		//wand (some wands are banned)
		do {
			loot = Generator.randomUsingDefaults(Generator.Category.WAND);
		} while (generatedClasses.contains(loot.getClass()) || loot instanceof WandOfRegrowth
				|| loot instanceof WandOfTransfusion || loot instanceof WandOfCorruption);
		generatedClasses.add(loot.getClass());
		loot.level(lootTier);
		((Wand)loot).curCharges = ((Wand)loot).maxCharges;
		lootList.add(loot);

		//ring (some rings are banned)
		do {
			loot = Generator.randomUsingDefaults(Generator.Category.RING);
		} while (generatedClasses.contains(loot.getClass()) || loot instanceof RingOfWealth
				|| loot instanceof RingOfMight || loot instanceof RingOfForce);
		generatedClasses.add(loot.getClass());
		loot.level(lootTier);
		lootList.add(loot);

		equipmentLoot[lootTier] = lootList.toArray(new Item[0]);
	}

	public Item createEquipment(int lootTier) {

		//ensure we don't have any empty loot lists
		setupEquipment();

		int idx;
		if (lootTier >= 2){
			idx = higherTierIdx;
			if (idx >= equipmentLoot[lootTier].length){
				idx = 0;
				higherTierIdx = 0;
			} else {
				higherTierIdx++;
			}

		} else {
			idx = lowerTierIdx;
			if (idx >= equipmentLoot[lootTier].length){
				idx = 0;
				lowerTierIdx = 0;
			} else {
				lowerTierIdx++;
			}

		}

		while(equipmentLoot[lootTier][idx] ==  null){
			idx++;
			if (idx >= equipmentLoot[lootTier].length){
				idx = 0;
			}
		}

		Item loot = equipmentLoot[lootTier][idx];
		equipmentLoot[lootTier][idx] = null;

		loot.cursed = false;
		if (loot instanceof Ring){
			//rings in the vault get 20% of ID for each defeated enemy. See Mob.destroy()
			loot.levelKnown = loot.cursedKnown = true;
		} else {
			loot.identify(false);
		}

		return loot;
	}

	//only occurs in levelgen, no need to bundle these
	ArrayList<ArrayList<Item>> consumableLoot = new ArrayList<>();

	private void setupConsumables(){
		if (consumableLoot.isEmpty()) {
			consumableLoot.add(new ArrayList<>());
			consumableLoot.add(new ArrayList<>());
			consumableLoot.add(new ArrayList<>());
			consumableLoot.add(new ArrayList<>());
		}

		//T0, floor loot
		if (consumableLoot.get(0).isEmpty()){
			consumableLoot.get(0).addAll(Arrays.asList(
					Reflection.newInstance(Random.oneOf(PotionOfFrost.class, PotionOfLevitation.class)),
					Reflection.newInstance(Random.oneOf(Mageroyal.Seed.class, Icecap.Seed.class, Stormvine.Seed.class)),
					Reflection.newInstance(Random.oneOf(ScrollOfMirrorImage.class, ScrollOfTeleportation.class)),
					Reflection.newInstance(Random.oneOf(StoneOfFlock.class, StoneOfShock.class, StoneOfFear.class))));
			Collections.shuffle(consumableLoot.get(0));
			//first item in each tier is always a potion of healing (except T3, which has one randomly)
			consumableLoot.get(0).add(0, new PotionOfHealing());
		}

		//T1
		if (consumableLoot.get(1).isEmpty()){
			consumableLoot.get(1).addAll(Arrays.asList(
					Reflection.newInstance(Random.oneOf(PotionOfToxicGas.class, PotionOfParalyticGas.class)),
					Reflection.newInstance(Random.oneOf(Firebloom.Seed.class, Sorrowmoss.Seed.class, Blindweed.Seed.class)),
					Reflection.newInstance(Random.oneOf(ScrollOfRecharging.class, ScrollOfTerror.class)),
					Reflection.newInstance(Random.oneOf(StoneOfDeepSleep.class, StoneOfClairvoyance.class, StoneOfAggression.class))));
			Collections.shuffle(consumableLoot.get(1));
			consumableLoot.get(1).add(0, new PotionOfHealing());
		}

		//T2
		if (consumableLoot.get(2).isEmpty()){
			consumableLoot.get(2).addAll(Arrays.asList(
					Reflection.newInstance(Random.oneOf(PotionOfMindVision.class, PotionOfLiquidFlame.class)),
					Reflection.newInstance(Random.oneOf(Swiftthistle.Seed.class, Sungrass.Seed.class)),
					Reflection.newInstance(Random.oneOf(ScrollOfLullaby.class, ScrollOfMagicMapping.class)),
					Reflection.newInstance(Random.oneOf(StoneOfBlast.class, StoneOfBlink.class))));
			Collections.shuffle(consumableLoot.get(2));
			consumableLoot.get(2).add(0, new PotionOfHealing());
		}

		//T3
		if (consumableLoot.get(3).isEmpty()){
			consumableLoot.get(3).addAll(Arrays.asList(
					Reflection.newInstance(Random.oneOf(PotionOfExperience.class, PotionOfInvisibility.class)),
					Reflection.newInstance(Random.oneOf(Earthroot.Seed.class, Starflower.Seed.class)),
					Reflection.newInstance(Random.oneOf(ScrollOfRetribution.class, ScrollOfTransmutation.class)),
					Reflection.newInstance(Random.oneOf(StoneOfEnchantment.class, StoneOfAugmentation.class)),
					new PotionOfHealing()));
			Collections.shuffle(consumableLoot.get(3));
		}
	}

	public Item createConsumabe(int tier){
		if (consumableLoot.isEmpty() || consumableLoot.get(tier).isEmpty()){
			setupConsumables();
		}
		Item result = consumableLoot.get(tier).remove(0);
		return result;
	}

	static Class<?extends Item>[] T3SolveItems = new Class[]{
			StoneOfBlink.class,
			PotionOfInvisibility.class
	};

	public Item findT3SolveItem(){
		Random.shuffle(T3SolveItems);
		Item result = null;
		for (Class<?extends Item> itemCls : T3SolveItems){
			result = findPrizeItem(itemCls);
			if (result != null){
				return result;
			}
		}
		return null;
	}

	static Class<?extends Item>[] T2SolveItems = new Class[]{
			StoneOfBlink.class,
			PotionOfInvisibility.class
	};

	public Item findT2SolveItem(){
		Random.shuffle(T2SolveItems);
		Item result = null;
		for (Class<?extends Item> itemCls : T2SolveItems){
			result = findPrizeItem(itemCls);
			if (result != null){
				return result;
			}
		}
		//if we can't find a T2 solve, try to place a T3 solve instead (instead of having it be floor loot)
		return findT3SolveItem();
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
			//rotation is 3 mobs at each tier
			Collections.addAll(mobsToSpawn, T1Mobs);
			mobsToSpawn.add(Random.oneOf(T1Mobs));
			Collections.addAll(mobsToSpawn, T2Mobs);
			Collections.addAll(mobsToSpawn, T3Mobs);
			mobsToSpawn.add(Random.oneOf(T3Mobs));
			Random.shuffle(mobsToSpawn);
		}
		Class<? extends Mob> cls = mobsToSpawn.remove(0);
		if (cls == VaultElemental.class){
			cls = VaultElemental.random();
		}
		return Reflection.newInstance(cls);
	}

	//important to try and preserve mobs that can't spawn in a certain place (e.g. corridors)
	public void returnMob( Class<?extends Mob> cls){
		mobsToSpawn.add(0, cls);
	}

	@Override
	protected void createMobs() {
		//mob creation handled by individual rooms
	}

	@Override
	public void occupyCell(Char ch) {
		super.occupyCell(ch);
		//extra check to ensure vault is left if quest is completed
		if (ch == Dungeon.hero && (Imp.Quest.isCompleted() && !Imp.Quest.isOld())){
			beforeTransition();
			InterlevelScene.curTransition = new LevelTransition(Dungeon.level,
					Dungeon.hero.pos,
					LevelTransition.Type.BRANCH_ENTRANCE,
					Dungeon.depth,
					0,
					LevelTransition.Type.BRANCH_EXIT);
			InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
			Game.switchScene( InterlevelScene.class );
		} else if (ch == Dungeon.hero) {
			Room r = room(ch.pos);
			if (r instanceof VaultFinalRoom){
				((VaultFinalRoom) r).processHeroStep((Hero) ch);
			}
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

		//also generate 2 torches if into darkness is enabled. Separate seed to avoid affecting other parts of levelgen
		if (Dungeon.isChallenged(Challenges.DARKNESS)){
			Random.pushGenerator(Random.Long());
			for (int i = 0; i < 2; i++){
				int cell = randomDropCell();
				drop( new Torch(), cell ).type = Heap.Type.HEAP;
				if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
					map[cell] = Terrain.GRASS;
					losBlocking[cell] = false;
				}
			}
			Random.popGenerator();
		}

	}

	@Override
	public void seal() {
		if (!locked) {
			locked = true;
			//don't apply locked floor buff here
			// no need to restrict regen and the player IS able to leave
		}
	}

	@Override
	public void unseal() {
		super.unseal();
		for (Room r : rooms){
			if (r instanceof VaultFinalRoom){
				((VaultFinalRoom) r).unlock();
			}
		}
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				Music.INSTANCE.fadeOut(5f, new Callback() {
					@Override
					public void call() {
						Music.INSTANCE.end();
					}
				});
			}
		});
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

		@Override
		public String desc() {
			return Messages.get(this, "desc");
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
