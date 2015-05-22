package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
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

	public static void cursedZap(final Wand wand, final Hero user, final Ballistica bolt){
		switch (Random.chances(new float[]{COMMON_CHANCE, UNCOMMON_CHANCE, RARE_CHANCE, VERY_RARE_CHANCE})){
			case 0:
			default:
				commonEffect(wand, user, bolt);
				break;
			case 1:
				uncommonEffect(wand, user, bolt);
				break;
			case 2:
				rareEffect(wand, user, bolt);
				break;
			case 3:
				veryRareEffect(wand, user, bolt);
				break;
		}
	}

	private static void commonEffect(final Wand wand, final Hero user, final Ballistica bolt){
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
									Buff.affect(user, Frost.class, Frost.duration(user) * Random.Float(3f, 5f));
									break;
								case 1:
									Buff.affect(user, Burning.class).reignite(user);
									if (target != null)
										Buff.affect(target, Frost.class, Frost.duration(target) * Random.Float(3f, 5f));
									break;
							}
							wand.wandUsed();
						}
					});
				break;

			//spawns some regrowth
			case 1:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						int c = Dungeon.level.map[bolt.collisionPos];
						if (c == Terrain.EMPTY ||
								c == Terrain.EMBERS ||
								c == Terrain.EMPTY_DECO ||
								c == Terrain.GRASS ||
								c == Terrain.HIGH_GRASS) {
							GameScene.add( Blob.seed(bolt.collisionPos, 30, Regrowth.class));
						}
						wand.wandUsed();
					}
				});
				break;

			//random teleportation
			case 2:
				switch(Random.Int(2)){
					case 0:
						ScrollOfTeleportation.teleportHero(user);
						wand.wandUsed();
						break;
					case 1:
						cursedFX(user, bolt, new Callback() {
							public void call() {
								Char ch = Actor.findChar( bolt.collisionPos );
								if (ch != null) {
									int count = 10;
									int pos;
									do {
										pos = Dungeon.level.randomRespawnCell();
										if (count-- <= 0) {
											break;
										}
									} while (pos == -1);
									if (pos == -1) {
										GLog.w(ScrollOfTeleportation.TXT_NO_TELEPORT);
									} else {
										ch.pos = pos;
										ch.sprite.place(ch.pos);
										ch.sprite.visible = Dungeon.visible[pos];
									}
								}
								wand.wandUsed();
							}
						});
						break;
				}
				break;

			//random gas at location
			case 3:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						switch(Random.Int(3)){
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
						wand.wandUsed();
					}
				});
				break;
		}

	}

	private static void uncommonEffect(final Wand wand, final Hero user, final Ballistica bolt){
		//TODO: implement
		switch(Random.Int(4)){
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
		}
		wand.wandUsed();
		GLog.i("uncommon effect");
	}

	private static void rareEffect(final Wand wand, final Hero user, final Ballistica bolt){
		//TODO: implement
		switch(Random.Int(4)){
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
		}
		wand.wandUsed();
		GLog.i("rare effect");
	}

	private static void veryRareEffect(final Wand wand, final Hero user, final Ballistica bolt){
		switch(Random.Int(4)){

			//great forest fire!
			case 0:
				for (int i = 0; i < Level.LENGTH; i++){
					int c = Dungeon.level.map[i];
					if (c == Terrain.EMPTY ||
							c == Terrain.EMBERS ||
							c == Terrain.EMPTY_DECO ||
							c == Terrain.GRASS ||
							c == Terrain.HIGH_GRASS) {
						GameScene.add( Blob.seed(i, 15, Regrowth.class));
					}
				}
				do {
					GameScene.add(Blob.seed(Dungeon.level.randomDestination(), 10, Fire.class));
				} while (Random.Int(5) != 0);
				new Flare(8, 32).color(0xFFFF66, true).show(user.sprite, 2f);
				Sample.INSTANCE.play(Assets.SND_TELEPORT);
				GLog.p("Grass explodes around you!");
				GLog.w("You smell burning...");
				wand.wandUsed();
				break;

			//superpowered mimic
			case 1:
				cursedFX(user, bolt, new Callback() {
					public void call() {
						Mimic mimic = Mimic.spawnAt(bolt.collisionPos, new ArrayList<Item>());
						mimic.adjustStats(Dungeon.depth + 10);
						mimic.HP = mimic.HT;
						Item reward;
						do {
							reward = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
									Generator.Category.RING, Generator.Category.WAND));
						} while (reward.level < 2 && !(reward instanceof MissileWeapon));
						Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1, 0.5f);
						mimic.items.clear();
						mimic.items.add(reward);

						wand.wandUsed();
					}
				});
				break;

			//crashes the game, yes, really.
			case 2:
				try {
					Dungeon.saveAll();
					//TODO: consider a more elegant way to accomplish this effect.
					throw new RuntimeException("critical wand exception");
				} catch(IOException e){
					//oookay maybe don't kill the game if the save failed.
					GLog.i("nothing happens");
					wand.wandUsed();
				}
				break;

			//random transmogrification
			case 3:
				wand.wandUsed();
				wand.detach(user.belongings.backpack);
				Item result;
				do {
					result = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
							Generator.Category.RING, Generator.Category.ARTIFACT));
				} while (result.level < 0 && !(result instanceof MissileWeapon));
				if (result.isUpgradable()) result.upgrade();
				result.cursed = result.cursedKnown = true;
				GLog.w("your wand transmogrifies into a different item!");
				Dungeon.level.drop(result, user.pos).sprite.drop();
				wand.wandUsed();
				break;
		}
	}

	private static void cursedFX(final Hero user, final Ballistica bolt, final Callback callback){
		MagicMissile.shadow(user.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

}
