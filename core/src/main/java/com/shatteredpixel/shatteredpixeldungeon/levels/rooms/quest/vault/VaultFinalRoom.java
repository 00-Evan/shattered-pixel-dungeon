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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultBossElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.ImpStatue;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.custom.Carpet;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class VaultFinalRoom extends SpecialRoom {

	@Override
	public int minWidth() {
		return 21;
	}

	@Override
	public int maxWidth() {
		return 21;
	}

	@Override
	public int minHeight() {
		return 21;
	}

	@Override
	public int maxHeight(){
		return 21;
	}

	@Override
	public boolean isExit() {
		return true;
	}

	@Override
	public boolean canConnect(Point p) {
		return (Math.abs(p.x - center().x) <= 5 || Math.abs(p.y - center().y) <= 5);
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return false;
	}

	private Point entryDoor;
	private Point lockedDoor;

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fillEllipse( level, this, 5, Terrain.EMPTY_SP );

		Point c = center();

		Door entrance = entrance();
		entrance.set( Door.Type.REGULAR );
		Room entry = new EmptyRoom();
		Room treasure = new EmptyRoom();;
		if (entrance.x == left) {
			entry.set(left + 1, top + 5, left + 3, bottom - 5);
			entryDoor = new Point(left+4, c.y);
			treasure.set(right - 3,  top + 5, right - 1, bottom - 5);
			lockedDoor = new Point(right-4, c.y);
		} else if (entrance.x == right){
			treasure.set(left + 1, top + 5, left + 3, bottom - 5);
			lockedDoor = new Point(left+4, c.y);
			entry.set(right - 3,  top + 5, right - 1, bottom - 5);
			entryDoor = new Point(right-4, c.y);
		} else if (entrance.y == top) {
			entry.set(left + 5, top + 1, right-5, top + 3);
			entryDoor = new Point(c.x, top+4);
			treasure.set(left + 5, bottom - 3, right-5, bottom - 1);
			lockedDoor = new Point(c.x, bottom-4);
		} else {
			treasure.set(left + 5, top + 1, right-5, top + 3);
			lockedDoor = new Point(c.x, top+4);
			entry.set(left + 5, bottom - 3, right-5, bottom - 1);
			entryDoor = new Point(c.x, bottom-4);
		}
		//TODO if we're going to use locked doors in the final impl make sure the hero can't use
		// keys from branch 0 to open them
		Painter.set(level, entryDoor, Terrain.DOOR);
		Painter.set(level, lockedDoor, Terrain.LOCKED_DOOR);
		Painter.fill(level, entry, Terrain.CUSTOM_DECO_EMPTY);

		Carpet carpet = new Carpet();
		carpet.setRect(entry.left, entry.top, entry.width(), entry.height());
		level.customTiles.add(carpet);

		if (entry.width() > entry.height()){
			Painter.set(level, entry.left+1, entry.top+1, Terrain.REGION_DECO);
			carpet.overrideTile(level, entry.left+1, entry.top+1, Carpet.CITY_PEDESTAL);
			Painter.set(level, entry.left+3, entry.top+1, Terrain.REGION_DECO);
			carpet.overrideTile(level, entry.left+3, entry.top+1, Carpet.CITY_PEDESTAL);
			Painter.set(level, entry.left+7, entry.top+1, Terrain.REGION_DECO);
			carpet.overrideTile(level, entry.left+7, entry.top+1, Carpet.CITY_PEDESTAL);
			Painter.set(level, entry.left+9, entry.top+1, Terrain.REGION_DECO);
			carpet.overrideTile(level, entry.left+9, entry.top+1, Carpet.CITY_PEDESTAL);
		} else {
			Painter.set(level, entry.left+1, entry.top+1, Terrain.REGION_DECO);
			carpet.overrideTile(level, entry.left+1, entry.top+1, Carpet.CITY_PEDESTAL);
			Painter.set(level, entry.left+1, entry.top+3, Terrain.REGION_DECO);
			carpet.overrideTile(level, entry.left+1, entry.top+3, Carpet.CITY_PEDESTAL);
			Painter.set(level, entry.left+1, entry.top+7, Terrain.REGION_DECO);
			carpet.overrideTile(level, entry.left+1, entry.top+7, Carpet.CITY_PEDESTAL);
			Painter.set(level, entry.left+1, entry.top+9, Terrain.REGION_DECO);
			carpet.overrideTile(level, entry.left+1, entry.top+9, Carpet.CITY_PEDESTAL);
		}

		Painter.fill(level, treasure, Terrain.EMPTY_SP);

		level.drop(new ImpStatue(), level.pointToCell(treasure.random(1)));

		//These items are meant to be taken out with you and so use levelgen logic
		Artifact artif = Generator.randomArtifact();
		if (artif != null){
			artif.identify();
			artif.transferUpgrade(5);
			level.drop(artif, level.pointToCell(treasure.random(1)));
		}
		//TODO more options

	}

	private int warnState = 0;
	private boolean lockTriggered = false;

	public void processHeroStep(Hero hero){
		if (!lockTriggered){
			Point heroPos = Dungeon.level.cellToPoint(hero.pos);
			int distance = Math.max(Math.abs(heroPos.x - lockedDoor.x), Math.abs(heroPos.y - lockedDoor.y));
			//clear warned state if hero leaves
			if (distance <= 3){
				Level.set(Dungeon.level.pointToCell(entryDoor), Terrain.LOCKED_DOOR);
				GameScene.updateMap(Dungeon.level.pointToCell(entryDoor));
				GLog.w("fight start!");
				VaultBossElemental boss = new VaultBossElemental();
				boss.state = boss.WANDERING;
				boss.pos = Dungeon.level.pointToCell(center());
				GameScene.add(boss);
				boss.setElementalForm(VaultBossElemental.ElementalForm.values()[Random.Int(3)]);
				lockTriggered = true;
			} else if (distance == 4 && warnState < 2) {
				GLog.n(Messages.get(VaultFinalRoom.class, "final_warning"));
				Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
				hero.interrupt();
				warnState = 2;
			} else if (distance >= 5 && warnState == 2){
				warnState = 1;
			} else if (distance <= 10 && warnState < 1){
				hero.interrupt();
				ShatteredPixelDungeon.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						//TODO vary based on quest score
						int score = 0;
						if (score >= 1600) {
							GameScene.show(new WndTitledMessage(new ImpSprite(),
									Messages.titleCase(Messages.get(Imp.class, "name")),
									Messages.get(VaultFinalRoom.class, "imp_warning_prepared")));
						} else {
							GameScene.show(new WndTitledMessage(new ImpSprite(),
									Messages.titleCase(Messages.get(Imp.class, "name")),
									Messages.get(VaultFinalRoom.class, "imp_warning_unprepared")));
						}
					}
				});
				warnState = 1;
			} else if (distance > 12){
				warnState = 0;
			}
		}
	}

	public void unlock(){
		Level.set(Dungeon.level.pointToCell(entryDoor), Terrain.DOOR);
		GameScene.updateMap(Dungeon.level.pointToCell(entryDoor));
		Level.set(Dungeon.level.pointToCell(lockedDoor), Terrain.DOOR);
		GameScene.updateMap(Dungeon.level.pointToCell(lockedDoor));
	}

	private static final String ENTRY_DOOR_X = "entry_door_x";
	private static final String ENTRY_DOOR_Y = "entry_door_y";
	private static final String LOCKED_DOOR_X = "locked_door_x";
	private static final String LOCKED_DOOR_Y = "locked_door_y";
	private static final String WARN_STATE = "warn_state";
	private static final String LOCK_TRIGGERED = "lock_triggered";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(ENTRY_DOOR_X, entryDoor.x);
		bundle.put(ENTRY_DOOR_Y, entryDoor.y);

		bundle.put(LOCKED_DOOR_X, lockedDoor.x);
		bundle.put(LOCKED_DOOR_Y, lockedDoor.y);

		bundle.put(WARN_STATE, warnState);
		bundle.put(LOCK_TRIGGERED, lockTriggered);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		entryDoor = new Point();
		entryDoor.x = bundle.getInt(ENTRY_DOOR_X);
		entryDoor.y = bundle.getInt(ENTRY_DOOR_Y);

		lockedDoor = new Point();
		lockedDoor.x = bundle.getInt(LOCKED_DOOR_X);
		lockedDoor.y = bundle.getInt(LOCKED_DOOR_Y);

		warnState = bundle.getInt(WARN_STATE);
		lockTriggered = bundle.getBoolean(LOCK_TRIGGERED);
	}
}
