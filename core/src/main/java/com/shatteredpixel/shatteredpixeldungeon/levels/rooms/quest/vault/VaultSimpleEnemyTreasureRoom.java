package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.VaultRat;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultSimpleEnemyTreasureRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		int ratPos = 0;
		int treasurePos = 0;
		switch (Random.Int(4)){
			case 0:
				Painter.fill(level, left+2, top+2, 6, 6, Terrain.WALL );
				Painter.fill(level, left+3, top+3, 4, 4, Terrain.EMPTY_SP );
				Painter.fill(level, left+4, top+7, 2, 1, Terrain.EMPTY_SP );
				Painter.fill(level, left+7, top+4, 1, 2, Terrain.EMPTY_SP );
				ratPos = level.pointToCell(new Point(left+4, top+4));
				treasurePos = level.pointToCell(new Point(left+3, top+3));
				break;
			case 1:
				Painter.fill(level, left+3, top+2, 6, 6, Terrain.WALL );
				Painter.fill(level, left+4, top+3, 4, 4, Terrain.EMPTY_SP );
				Painter.fill(level, left+5, top+7, 2, 1, Terrain.EMPTY_SP );
				Painter.fill(level, left+3, top+4, 1, 2, Terrain.EMPTY_SP );
				ratPos = level.pointToCell(new Point(right-4, top+4));
				treasurePos = level.pointToCell(new Point(right-3, top+3));
				break;
			case 2:
				Painter.fill(level, left+3, top+3, 6, 6, Terrain.WALL );
				Painter.fill(level, left+4, top+4, 4, 4, Terrain.EMPTY_SP );
				Painter.fill(level, left+5, top+3, 2, 1, Terrain.EMPTY_SP );
				Painter.fill(level, left+3, top+5, 1, 2, Terrain.EMPTY_SP );
				ratPos = level.pointToCell(new Point(right-4, bottom-4));
				treasurePos = level.pointToCell(new Point(right-3, bottom-3));
				break;
			case 3:
				Painter.fill(level, left+2, top+3, 6, 6, Terrain.WALL );
				Painter.fill(level, left+3, top+4, 4, 4, Terrain.EMPTY_SP );
				Painter.fill(level, left+4, top+3, 2, 1, Terrain.EMPTY_SP );
				Painter.fill(level, left+7, top+5, 1, 2, Terrain.EMPTY_SP );
				ratPos = level.pointToCell(new Point(left+4, bottom-4));
				treasurePos = level.pointToCell(new Point(left+3, bottom-3));
				break;
		}

		Item treasure = Generator.randomWeapon(true);
		level.drop(treasure, treasurePos).type = Heap.Type.CHEST;
		if (treasure.cursed){
			treasure.cursed = false;
			if (((MeleeWeapon) treasure).hasCurseEnchant()){
				((MeleeWeapon) treasure).enchant(null);
			}
		}
		//not true ID
		treasure.levelKnown = treasure.cursedKnown = true;

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		VaultRat rat = new VaultRat();
		rat.pos = ratPos;
		level.mobs.add(rat);

	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

}
