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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;

//helper class to contain all the cursed wand zapping logic, so the main wand class doesn't get huge.
public class CursedWand {

	private static float COMMON_CHANCE = 0.6f;
	private static float UNCOMMON_CHANCE = 0.3f;
	private static float RARE_CHANCE = 0.09f;
	private static float VERY_RARE_CHANCE = 0.01f;

	public static void cursedZap(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch (Random.chances(new float[]{COMMON_CHANCE, UNCOMMON_CHANCE, RARE_CHANCE, VERY_RARE_CHANCE})){
			case 0:
			default:
				commonEffect(origin, user, bolt, afterZap);
				break;
			case 1:
				uncommonEffect(origin, user, bolt, afterZap);
				break;
			case 2:
				rareEffect(origin, user, bolt, afterZap);
				break;
			case 3:
				veryRareEffect(origin, user, bolt, afterZap);
				break;
		}
	}

	private static void commonEffect(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch(Random.Int(4)){

			//anti-entropy
			case 0:
				cursedFX(user, bolt, new Callback() {
						public void call() {
							Char target = Actor.findChar(bolt.collisionPos);
							switch (Random.Int(2)){
								case 0:
									if (target != null)
										Buff.affect(target, Burning.class).reignite(target);
									Buff.affect(user, Frost.class, Frost.DURATION);
									break;
								case 1:
									Buff.affect(user, Burning.class).reignite(user);
									if (target != null)
										Buff.affect(target, Frost.class, Frost.DURATION);
									break;
							}
							afterZap.call();
						}
					});
				break;

			//spawns some regrowth
			case 1:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						GameScene.add( Blob.seed(bolt.collisionPos, 30, Regrowth.class));
						afterZap.call();
					}
				});
				break;

			//random teleportation
			case 2:
				switch(Random.Int(2)){
					case 0:
						ScrollOfTeleportation.teleportChar(user);
						afterZap.call();
						break;
					case 1:
						cursedFX(user, bolt, new Callback() {
							public void call() {
								Char ch = Actor.findChar( bolt.collisionPos );
								if (ch != null && !ch.properties().contains(Char.Property.IMMOVABLE)) {
									ScrollOfTeleportation.teleportChar(user);
								}
								afterZap.call();
							}
						});
						break;
				}
				break;

			//random gas at location
			case 3:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						switch (Random.Int(3)) {
							case 0:
								GameScene.add( Blob.seed( bolt.collisionPos, 800, ConfusionGas.class ) );
								break;
							case 1:
								GameScene.add( Blob.seed( bolt.collisionPos, 500, ToxicGas.class ) );
								break;
							case 2:
								GameScene.add( Blob.seed( bolt.collisionPos, 200, ParalyticGas.class ) );
								break;
						}
						Sample.INSTANCE.play( Assets.Sounds.GAS );
						afterZap.call();
					}
				});
				break;
		}

	}

	private static void uncommonEffect(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch(Random.Int(4)){

			//Random plant
			case 0:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						int pos = bolt.collisionPos;
						//place the plant infront of an enemy so they walk into it.
						if (Actor.findChar(pos) != null && bolt.dist > 1) {
							pos = bolt.path.get(bolt.dist - 1);
						}

						if (pos == Terrain.EMPTY ||
								pos == Terrain.EMBERS ||
								pos == Terrain.EMPTY_DECO ||
								pos == Terrain.GRASS ||
								pos == Terrain.HIGH_GRASS ||
								pos == Terrain.FURROWED_GRASS) {
							Dungeon.level.plant((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED), pos);
						}
						afterZap.call();
					}
				});
				break;

			//Health transfer
			case 1:
				final Char target = Actor.findChar( bolt.collisionPos );
				if (target != null) {
					cursedFX(user, bolt, new Callback() {
						public void call() {
							int damage = Dungeon.depth * 2;
							Char toHeal, toDamage;
							
							switch (Random.Int(2)) {
								case 0: default:
									toHeal = user;
									toDamage = target;
									break;
								case 1:
									toHeal = target;
									toDamage = user;
									break;
							}
							toHeal.HP = Math.min(toHeal.HT, toHeal.HP + damage);
							toHeal.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
							toDamage.damage(damage, origin == null ? toHeal : origin);
							toDamage.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
							
							if (toDamage == Dungeon.hero){
								Sample.INSTANCE.play(Assets.Sounds.CURSED);
								if (!toDamage.isAlive()) {
									if (origin != null) {
										Dungeon.fail( origin.getClass() );
										GLog.n( Messages.get( CursedWand.class, "ondeath", origin.name() ) );
									} else {
										Dungeon.fail( toHeal.getClass() );
									}
								}
							} else {
								Sample.INSTANCE.play(Assets.Sounds.BURNING);
							}
							afterZap.call();
						}
					});
				} else {
					GLog.i(Messages.get(CursedWand.class, "nothing"));
					afterZap.call();
				}
				break;

			//Bomb explosion
			case 2:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						new Bomb().explode(bolt.collisionPos);
						afterZap.call();
					}
				});
				break;

			//shock and recharge
			case 3:
				new ShockingTrap().set( user.pos ).activate();
				Buff.prolong(user, Recharging.class, Recharging.DURATION);
				ScrollOfRecharging.charge(user);
				SpellSprite.show(user, SpellSprite.CHARGE);
				afterZap.call();
				break;
		}

	}

	private static void rareEffect(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch(Random.Int(4)){

			//sheep transformation
			case 0:
				if (user != Dungeon.hero){
					cursedZap(origin, user, bolt, afterZap);
					return;
				}
				
				cursedFX(user, bolt, new Callback() {
					public void call() {
						Char ch = Actor.findChar( bolt.collisionPos );

						if (ch != null && ch != user
								&& !ch.properties().contains(Char.Property.BOSS)
								&& !ch.properties().contains(Char.Property.MINIBOSS)){
							Sheep sheep = new Sheep();
							sheep.lifespan = 10;
							sheep.pos = ch.pos;
							ch.destroy();
							ch.sprite.killAndErase();
							Dungeon.level.mobs.remove(ch);
							TargetHealthIndicator.instance.target(null);
							GameScene.add(sheep);
							CellEmitter.get(sheep.pos).burst(Speck.factory(Speck.WOOL), 4);
						} else {
							GLog.i(Messages.get(CursedWand.class, "nothing"));
						}
						afterZap.call();
					}
				});
				break;

			//curses!
			case 1:
				if (user instanceof Hero) CursingTrap.curse( (Hero) user );
				afterZap.call();
				break;

			//inter-level teleportation
			case 2:
				if (Dungeon.depth > 1 && !Dungeon.bossLevel() && user == Dungeon.hero) {

					//each depth has 1 more weight than the previous depth.
					float[] depths = new float[Dungeon.depth-1];
					for (int i = 1; i < Dungeon.depth; i++) depths[i-1] = i;
					int depth = 1+Random.chances(depths);

					Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
					if (buff != null) buff.detach();
					
					buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
					if (buff != null) buff.detach();

					InterlevelScene.mode = InterlevelScene.Mode.RETURN;
					InterlevelScene.returnDepth = depth;
					InterlevelScene.returnPos = -1;
					Game.switchScene(InterlevelScene.class);

				} else {
					ScrollOfTeleportation.teleportChar(user);

				}
				afterZap.call();
				break;

			//summon monsters
			case 3:
				new SummoningTrap().set( user.pos ).activate();
				afterZap.call();
				break;
		}
	}

	private static void veryRareEffect(final Item origin, final Char user, final Ballistica bolt, final Callback afterZap){
		switch(Random.Int(4)){

			//great forest fire!
			case 0:
				for (int i = 0; i < Dungeon.level.length(); i++){
					GameScene.add( Blob.seed(i, 15, Regrowth.class));
				}
				do {
					GameScene.add(Blob.seed(Dungeon.level.randomDestination(null), 10, Fire.class));
				} while (Random.Int(5) != 0);
				new Flare(8, 32).color(0xFFFF66, true).show(user.sprite, 2f);
				Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				GLog.p(Messages.get(CursedWand.class, "grass"));
				GLog.w(Messages.get(CursedWand.class, "fire"));
				afterZap.call();
				break;

			//golden mimic
			case 1:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						Mimic mimic = Mimic.spawnAt(bolt.collisionPos, new ArrayList<Item>(), GoldenMimic.class);
						if (mimic != null) {
							mimic.stopHiding();
							mimic.alignment = Char.Alignment.ENEMY;
							Item reward;
							do {
								reward = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
										Generator.Category.RING, Generator.Category.WAND));
							} while (reward.level() < 1);
							//play vfx/sfx manually as mimic isn't in the scene yet
							Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 0.85f);
							CellEmitter.get(mimic.pos).burst(Speck.factory(Speck.STAR), 10);
							mimic.items.clear();
							mimic.items.add(reward);
							GameScene.add(mimic);
						} else {
							GLog.i(Messages.get(CursedWand.class, "nothing"));
						}
						
						afterZap.call();
					}
				});
				break;

			//crashes the game, yes, really.
			case 2:
				
				if (user != Dungeon.hero){
					cursedZap(origin, user, bolt, afterZap);
					return;
				}
				
				try {
					Dungeon.saveAll();
					if(Messages.lang() != Languages.ENGLISH){
						//Don't bother doing this joke to none-english speakers, I doubt it would translate.
						GLog.i(Messages.get(CursedWand.class, "nothing"));
						afterZap.call();
					} else {
						GameScene.show(
								new WndOptions("CURSED WAND ERROR", "this application will now self-destruct", "abort", "retry", "fail") {
									
									@Override
									protected void onSelect(int index) {
										Game.instance.finish();
									}
									
									@Override
									public void onBackPressed() {
										//do nothing
									}
								}
						);
					}
				} catch(IOException e){
					ShatteredPixelDungeon.reportException(e);
					//oookay maybe don't kill the game if the save failed.
					GLog.i(Messages.get(CursedWand.class, "nothing"));
					afterZap.call();
				}
				break;

			//random transmogrification
			case 3:
				//skips this effect if there is no item to transmogrify
				if (origin == null || user != Dungeon.hero || !Dungeon.hero.belongings.contains(origin)){
					cursedZap(origin, user, bolt, afterZap);
					return;
				}
				origin.detach(Dungeon.hero.belongings.backpack);
				Item result;
				do {
					result = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
							Generator.Category.RING, Generator.Category.ARTIFACT));
				} while (result.cursed);
				if (result.isUpgradable()) result.upgrade();
				result.cursed = result.cursedKnown = true;
				if (origin instanceof Wand){
					GLog.w( Messages.get(CursedWand.class, "transmogrify_wand") );
				} else {
					GLog.w( Messages.get(CursedWand.class, "transmogrify_other") );
				}
				Dungeon.level.drop(result, user.pos).sprite.drop();
				afterZap.call();
				break;
		}
	}

	private static void cursedFX(final Char user, final Ballistica bolt, final Callback callback){
		MagicMissile.boltFromChar( user.sprite.parent,
				MagicMissile.RAINBOW,
				user.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

}
