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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;




import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.PHYSICIST;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoTrap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CrowBar extends MeleeWeapon {
	public static final String AC_PICKLOCK = "PICKLOCK";
	public static final float TIME_TO_PICKLOCK = 1f;

	{
		image = ItemSpriteSheet.CROWBAR;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1f;

		defaultAction = AC_PICKLOCK;

		tier = 2;
	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //12 base, down from 15	最大伤害从15降低到12
				lvl*(tier+1);   //scaling unchanged	升级增益不变
	}

	@Override
	public int STRReq(){
		int req = STRReq(level());
		if ( masteryPotionBonus | hero.heroClass == PHYSICIST ) {
			req -= 2;
		}
		return req;
	}

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_PICKLOCK );
		return actions;
	}

	public static ArrayList picklocked = new ArrayList();

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		if ( action.equals(AC_PICKLOCK) ) {

			curUser = hero;
			curItem = this;
			GameScene.selectCell(new CellSelector.Listener() {

				@Override
				public void onSelect( Integer cell ) {

					if ( cell != null ) {
						Heap heap = Dungeon.level.heaps.get( cell );
						if (!(Dungeon.level.adjacent(curUser.pos, cell)) || cell == curUser.pos) {

							if (cell == curUser.pos) {

							} else {
								GLog.i(Messages.get(CrowBar.class, "too_far"));
							}

						} else if ( Dungeon.level.map[cell] == Terrain.CRYSTAL_DOOR ) {

							if( picklocked.contains( cell ) ){
								GLog.i(Messages.get(CrowBar.class,"had_try"));
							} else if ( Random.Int(0,100) < Math.round( 30f+ level()*15 )) {
								GLog.w(Messages.get(CrowBar.class,"trylock_success"));

								hero.sprite.operate(hero.pos);
								hero.busy();

								hero.spend( TIME_TO_PICKLOCK );
								hero.spendAndNext( TIME_TO_PICKLOCK );

								Level.set( cell, Terrain.EMPTY );
								Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
								CellEmitter.get( cell ).start( Speck.factory( Speck.DISCOVER ), 0.025f, 20 );
								GameScene.updateMap(cell);

								GameScene.updateKeyDisplay();

							}else {
								GLog.w(Messages.get(CrowBar.class, "trylock_failed"));
								picklocked.add( cell );
							}

						} else if ( Dungeon.level.map[cell] == Terrain.DOOR ) {
							GLog.i(Messages.get(CrowBar.class, "only_crystal"));
						} else if ( heap != null ) {
							if ( heap.type == Heap.Type.CRYSTAL_CHEST ) {

								if( picklocked.contains( cell ) ){
									GLog.i(Messages.get(CrowBar.class,"had_try"));
								} else if ( Random.Int(0,100) < Math.round( 30f+ level()*15 )) {
									GLog.w(Messages.get(CrowBar.class,"trylock_success"));

									hero.sprite.operate(hero.pos);
									hero.busy();

									hero.spend( TIME_TO_PICKLOCK );
									hero.spendAndNext( TIME_TO_PICKLOCK );

									GameScene.updateKeyDisplay();
									heap.open( curUser );
									GameScene.updateMap(cell);
									GameScene.updateKeyDisplay();
								} else {
									GLog.w(Messages.get(CrowBar.class, "trylock_failed"));
									picklocked.add( cell );
								}

							} else if ( heap.type == Heap.Type.CHEST ) {
								GLog.i(Messages.get(CrowBar.class, "only_crystal"));
							}
						} else if( !Dungeon.level.mobs.isEmpty() ){
							Mob target = null;
							Char ch = Actor.findChar(cell);
								if ( ch != null && ch instanceof Mimic ){
									target = (Mob)ch;
									damageRoll( target );
									GLog.i(Messages.get(CrowBar.class, "mimic"));
								}
						} else {
							GLog.i(Messages.get(CrowBar.class, "cannot_picklock"));
						}
					}
				}

				@Override
				public String prompt() {
					return Messages.get(CrowBar.class, "prompt");
				}
			});


		}
	}

	@Override
	public String statsInfo() {
		if( hero.heroClass == PHYSICIST ){
			return  Messages.get(CrowBar.class, "mastery");
		} return null;
	}

//对怪物额外伤害
	@Override
	public int proc(Char attacker, Char ch, int damage ){
		if (ch instanceof Mimic){
			damage =999;
			}
		return super.proc(attacker, ch, damage);
		}

	@Override
	public String targetingPrompt() {
		return Messages.get(CrowBar.class, "maceprompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Mace.heavyBlowAbility(hero, target, 1.65f, this);
	}

}
