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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class SentryRoom extends SpecialRoom {

	@Override
	public int minWidth() { return 7; }
	public int minHeight() { return 7; }

	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );

		Door entrance = entrance();

		Point center;
		do {
			center = center();
		} while (center.x == entrance.x || center.y == entrance.y);

		Point sentryPos = new Point();
		Point treasurePos = new Point();

		//length of dangerous path from entrance to treasure and back
		int dangerDist = 0;

		//determine position of sentry, treasure, and paint safe tiles / statues
		if (entrance.x == left){
			sentryPos.set(right-1, center.y);
			Painter.fill(level, left+1, top+1, 1, height()-2, Terrain.EMPTY);
			if (entrance.y > center.y){
				treasurePos.set(left+1, (top + 1 + center.y)/2);
				Painter.fill(level, left+1, top+1, 2, center.y-top-1, Terrain.EMPTY);
			} else {
				treasurePos.set(left+1, (bottom + center.y)/2);
				Painter.fill(level, left+1, center.y+1, 2, bottom-center.y-1, Terrain.EMPTY);
			}
			for (int x = right-3; x > left; x--){
				if (level.map[x + (center.y * level.width())] == Terrain.EMPTY_SP){
					Painter.set(level, x, center.y, Terrain.STATUE_SP);
				} else {
					Painter.set(level, x, center.y, Terrain.STATUE);
				}
			}
			dangerDist = 2*(width()-5);
		} else if (entrance.x == right){
			sentryPos.set(left+1, center.y);
			Painter.fill(level, right-1, top+1, 1, height()-2, Terrain.EMPTY);
			if (entrance.y > center.y){
				treasurePos.set(right-1, (top + 1 + center.y)/2);
				Painter.fill(level, right-2, top+1, 2, center.y-top-1, Terrain.EMPTY);
			} else {
				treasurePos.set(right-1, (bottom + 1 + center.y)/2);
				Painter.fill(level, right-2, center.y+1, 2, bottom-center.y-1, Terrain.EMPTY);
			}
			for (int x = left+3; x < right; x++){
				if (level.map[x + (center.y * level.width())] == Terrain.EMPTY_SP){
					Painter.set(level, x, center.y, Terrain.STATUE_SP);
				} else {
					Painter.set(level, x, center.y, Terrain.STATUE);
				}
			}
			dangerDist = 2*(width()-5);
		} else if (entrance.y == top){
			sentryPos.set(center.x, bottom-1);
			Painter.fill(level, left+1, top+1, width()-2, 1, Terrain.EMPTY);
			if (entrance.x > center.x){
				treasurePos.set((left + 1 + center.x)/2, top+1);
				Painter.fill(level, left+1, top+1, center.x-left-1, 2, Terrain.EMPTY);
			} else {
				treasurePos.set((right + center.x)/2, top+1);
				Painter.fill(level, center.x+1, top+1, right - center.x-1, 2, Terrain.EMPTY);
			}
			for (int y = bottom-3; y > top; y--){
				if (level.map[center.x + (y * level.width())] == Terrain.EMPTY_SP){
					Painter.set(level, center.x, y, Terrain.STATUE_SP);
				} else {
					Painter.set(level, center.x, y, Terrain.STATUE);
				}
			}
			dangerDist = 2*(height()-5);
 		} else  if (entrance.y == bottom){
			sentryPos.set(center.x, top+1);
			Painter.fill(level, left+1, bottom-1, width()-2, 1, Terrain.EMPTY);
			if (entrance.x > center.x){
				treasurePos.set((left + 1 + center.x)/2, bottom-1);
				Painter.fill(level, left+1, bottom-2, center.x-left-1, 2, Terrain.EMPTY);
			} else {
				treasurePos.set((right + center.x)/2, bottom-1);
				Painter.fill(level, center.x+1, bottom-2, right - center.x-1, 2, Terrain.EMPTY);
			}
			for (int y = top+3; y < bottom; y++){
				if (level.map[center.x + (y * level.width())] == Terrain.EMPTY_SP){
					Painter.set(level, center.x, y, Terrain.STATUE_SP);
				} else {
					Painter.set(level, center.x, y, Terrain.STATUE);
				}
			}
			dangerDist = 2*(height()-5);
		}

		Painter.set(level, sentryPos, Terrain.PEDESTAL);
		Sentry sentry = new Sentry();
		sentry.pos = level.pointToCell(sentryPos);
		sentry.room = new EmptyRoom();
		sentry.room.set((Rect)this);
		sentry.initialChargeDelay = sentry.curChargeDelay = dangerDist / 3f + 0.1f;
		level.mobs.add( sentry );

		Painter.set(level, treasurePos, Terrain.PEDESTAL);
		level.drop( prize( level ), level.pointToCell(treasurePos) ).type = Heap.Type.CHEST;

		level.addItemToSpawn(new PotionOfHaste());

		entrance.set( Door.Type.REGULAR );
	}

	private static Item prize(Level level ) {

		Item prize;

		//50% chance for prize item
		if (Random.Int(2) == 0){
			prize = level.findPrizeItem();
			if (prize != null)
				return prize;
		}

		//1 floor set higher in probability, never cursed
		do {
			if (Random.Int(2) == 0) {
				prize = Generator.randomWeapon((Dungeon.depth / 5) + 1);
			} else {
				prize = Generator.randomArmor((Dungeon.depth / 5) + 1);
			}
		} while (prize.cursed || Challenges.isItemBlocked(prize));
		prize.cursedKnown = true;

		//33% chance for an extra update.
		if (Random.Int(3) == 0){
			prize.upgrade();
		}

		return prize;
	}

	@Override
	public boolean canConnect(Point p) {
		if (!super.canConnect(p)){
			return false;
		}
		//don't place door in the exact center, if that exists
		if (width() % 2 == 1 && p.x == center().x){
			return false;
		}
		if (height() % 2 == 1 && p.y == center().y){
			return false;
		}
		return true;
	}

	public static class Sentry extends NPC {

		{
			spriteClass = SentrySprite.class;

			properties.add(Property.IMMOVABLE);
		}

		private float initialChargeDelay;
		private float curChargeDelay;

		private EmptyRoom room;

		@Override
		protected boolean act() {
			if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
				fieldOfView = new boolean[Dungeon.level.length()];
			}
			Dungeon.level.updateFieldOfView( this, fieldOfView );

			if (properties().contains(Property.IMMOVABLE)){
				throwItems();
			}

			if (Dungeon.hero != null){
				if (fieldOfView[Dungeon.hero.pos]
						&& Dungeon.level.map[Dungeon.hero.pos] == Terrain.EMPTY_SP
						&& room.inside(Dungeon.level.cellToPoint(Dungeon.hero.pos))
						&& Dungeon.hero.buff(LostInventory.class) == null){

					if (curChargeDelay > 0.001f){ //helps prevent rounding errors
						if (curChargeDelay == initialChargeDelay) {
							((SentrySprite) sprite).charge();
						}
						curChargeDelay -= Dungeon.hero.cooldown();
						//pity mechanic so mistaps don't get people instakilled
						if (Dungeon.hero.cooldown() >= 0.34f){
							Dungeon.hero.interrupt();
						}
					}

					if (curChargeDelay <= .001f){
						curChargeDelay = 1f;
						sprite.zap(Dungeon.hero.pos);
						((SentrySprite) sprite).charge();
					}

					spend(Dungeon.hero.cooldown());
					return true;

				} else {
					curChargeDelay = initialChargeDelay;
					sprite.idle();
				}

				spend(Dungeon.hero.cooldown());
			} else {
				spend(1f);
			}
			return true;
		}

		public void onZapComplete(){
			if (hit(this, Dungeon.hero, true)) {
				Dungeon.hero.damage(Random.NormalIntRange(2 + Dungeon.depth / 2, 4 + Dungeon.depth), new Eye.DeathGaze());
				if (!Dungeon.hero.isAlive()) {
					Badges.validateDeathFromEnemyMagic();
					Dungeon.fail(getClass());
					GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name())));
				}
			} else {
				Dungeon.hero.sprite.showStatus( CharSprite.NEUTRAL,  Dungeon.hero.defenseVerb() );
			}
		}

		@Override
		public int attackSkill(Char target) {
			return 20 + Dungeon.depth * 2;
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
			return true;
		}

		private static final String INITIAL_DELAY = "initial_delay";
		private static final String CUR_DELAY = "cur_delay";
		private static final String ROOM = "room";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(INITIAL_DELAY, initialChargeDelay);
			bundle.put(CUR_DELAY, curChargeDelay);
			bundle.put(ROOM, room);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			initialChargeDelay = bundle.getFloat(INITIAL_DELAY);
			curChargeDelay = bundle.getFloat(CUR_DELAY);
			room = (EmptyRoom) bundle.get(ROOM);
		}
	}

	public static class SentrySprite extends MobSprite {

		private Animation charging;
		private Emitter chargeParticles;

		public SentrySprite(){
			texture( Assets.Sprites.RED_SENTRY );

			idle = new Animation(1, true);
			idle.frames(texture.uvRect(0, 0, 8, 15));

			run = idle.clone();
			attack = idle.clone();
			charging = idle.clone();
			die = idle.clone();
			zap = idle.clone();

			play( idle );
		}

		@Override
		public void zap( int pos ) {
			idle();
			flash();
			emitter().burst(MagicMissile.WardParticle.UP, 2);
			if (Actor.findChar(pos) != null){
				parent.add(new Beam.DeathRay(center(), Actor.findChar(pos).sprite.center()));
			} else {
				parent.add(new Beam.DeathRay(center(), DungeonTilemap.raisedTileCenterToWorld(pos)));
			}
			((Sentry)ch).onZapComplete();
		}

		@Override
		public void link(Char ch) {
			super.link(ch);

			chargeParticles = centerEmitter();
			chargeParticles.autoKill = false;
			chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
			chargeParticles.on = false;

			if (((Sentry)ch).curChargeDelay != ((Sentry) ch).initialChargeDelay){
				play(charging);
			}
		}

		@Override
		public void die() {
			super.die();
			if (chargeParticles != null){
				chargeParticles.on = false;
			}
		}

		@Override
		public void kill() {
			super.kill();
			if (chargeParticles != null){
				chargeParticles.killAndErase();
			}
		}

		public void charge(){
			play(charging);
			if (visible) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
		}

		@Override
		public void play(Animation anim) {
			if (chargeParticles != null) chargeParticles.on = anim == charging;
			super.play(anim);
		}

		private float baseY = Float.NaN;

		@Override
		public void place(int cell) {
			super.place(cell);
			baseY = y;
		}

		@Override
		public void update() {
			super.update();
			if (chargeParticles != null){
				chargeParticles.pos( center() );
				chargeParticles.visible = visible;
			}

			if (!paused){
				if (Float.isNaN(baseY)) baseY = y;
				y = baseY + (float) Math.sin(Game.timeTotal);
				shadowOffset = 0.25f - 0.8f*(float) Math.sin(Game.timeTotal);
			}
		}

	}

}
