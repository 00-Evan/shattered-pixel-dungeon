/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.SpiritHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Mjornil extends Artifact {


	{
		image = ItemSpriteSheet.ARTIFACT_MJORNIL;

		levelCap = 10;

		charge = 0;
		chargeCap = 100;
		usedCharge = 20;
		partialCharge = 0;

		bones = true;

		defaultAction = AC_THROWHAMMER;

	}

	private int volume = 1;
	private final int MAX_VOLUME = 1;

	public boolean isFull() {
		return volume >= MAX_VOLUME;
	}

	private int usedCharge;


	public int Mlevel(){
		return level();
	}

	

	public static final String AC_THROWHAMMER = "THROWHAMMER";

	public static final String AC_LVLUP = "LVLUP";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (hero.buff(MagicImmune.class) != null) return actions;
		if (isEquipped(hero)) {
			actions.add(AC_THROWHAMMER);
		}

		if (isEquipped(hero) && level() < levelCap && !cursed) {
			actions.add(AC_LVLUP);
		}
		return actions;
	}


	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_THROWHAMMER)) {
			if (!isEquipped(hero)) {
				GLog.i(Messages.get(Artifact.class, "need_to_equip"));
				usesTargeting = false;
			} else if (cursed) {
				GLog.w(Messages.get(this, "cursed"));
				usesTargeting = false;
			} else if (volume<=0 && returnDepth != Dungeon.depth){
				GLog.i(Messages.get(Mjornil.class, "need_to_hammer"));
				usesTargeting = false;
			} else {
				usesTargeting = true;
				curUser = hero;
				curItem = this;
				GameScene.selectCell(shooter);

				if(volume<=0) {image = ItemSpriteSheet.ARTIFACT_MJORNIL2;}
				else if(volume>=1) {image = ItemSpriteSheet.ARTIFACT_MJORNIL;}
				returnDepth = Dungeon.depth;
				updateQuickslot();
			}
		}else if (action.equals(AC_LVLUP)){
			GameScene.selectItem(itemSelector);
		}
	}



	@Override
	public String desc() {
		String desc = super.desc();

		if (isEquipped(hero)) {
			if (!cursed) {
				if (level() < levelCap)
					desc += "\n\n" + Messages.get(this, "desc_hint"); // 강화 설명
			} else {
				desc += "\n\n" + Messages.get(this, "desc_cursed"); // 저주 받은 상태 설명
			}
		}


		return desc;
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new chargeThunder();
	}

	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;
		if (charge < chargeCap){
			partialCharge += 1f * amount;

			if (partialCharge >= 1) {
				charge++;
				partialCharge--;
				updateQuickslot();
			}
		}
		if (charge == 10) {
			GLog.p(Messages.get(Mjornil.class, "ready_to_thunder"));
		}
		if (charge == chargeCap){
			partialCharge = 0;
			GLog.p( Messages.get(Mjornil.class, "full") );
		}
		updateQuickslot();
	}

	public void collectHam ( SpiritHammer ham ) {

		volume ++;
		returnDepth = -1;
		if (volume >= MAX_VOLUME) {
			volume = MAX_VOLUME;
			GLog.p( Messages.get(this, "grabed") );
		}

		updateQuickslot();
	}

	public void fill() {
		volume = MAX_VOLUME;
		updateQuickslot();
	}



	@Override
	public Item upgrade() {

		chargeCap = 100;
		return super.upgrade();
	} // 임시방편

	private int storedMissile = 0;

	public void gainMissile( MissileWeapon missile ){
		if (level() >= 10) return;

		storedMissile += 5*(missile.tier+1);//*(missile.durabilityLeft()*0.0075f + 0.25f);
		if (storedMissile >= 20+(level()*2)){
			int upgrades = storedMissile / (20+(level()*2));
			upgrades = Math.min(upgrades, 20 - level());
			upgrade(upgrades);
			storedMissile -= upgrades * (20+(level()*2));
			if (level() == 10){
				storedMissile = 0;
				GLog.p( Messages.get(this, "maxlevel") );
			} else {
				GLog.p( Messages.get(this, "levelup"),
						(int)(3.5f+(level()*0.5f)),(int)(3.5f+(level()*0.5f)) );
			}
		} else {
			GLog.i( Messages.get(this, "feed") );
		}
	}

	@Override
	public String info() {
		String info = desc();

		info += "\n\n" + Messages.get(Mjornil.class, "stats",
				Math.round(1 + (int)( 0.7f * Mlevel())
						+RingOfSharpshooting.levelDamageBonus(hero)),
				Math.round(6 + (int)( 0.3f * Mlevel())
						+RingOfSharpshooting.levelDamageBonus(hero)),
				Math.round(2 *(Mlevel()+1)), Math.round(3 * (Mlevel()+1))
		        );
		if (9 > Dungeon.hero.STR()) {
			info += " " + Messages.get(Mjornil.class, "too_heavy");
		} else if (Dungeon.hero.STR() > 9) {
			info += " " + Messages.get(Mjornil.class, "excess_str", Dungeon.hero.STR() - 9);
		}
		info += "\n\n" + Messages.get(Mjornil.class, "distance");
		return info;
	}




	@Override
	public int targetingPos(Hero user, int dst) {
		return knockHammer().targetingPos(user, dst);
	}


	private int targetPos;


	public Mjornil.AtacktoHammer knockHammer() {
		return new Mjornil.AtacktoHammer();
	}






	public class AtacktoHammer extends MissileWeapon {



		{
			image = ItemSpriteSheet.SPIRIT_HAMMER;

			hitSound = Assets.Sounds.HIT_CRUSH;
			hitSoundPitch = 0.8f;

		}


		public int culCharge(){
			if (charge >= 10) {
				return  1;
			}else {
				return 0;
			}
		}


		@Override
		public int min(int lvl) {
			int dmg = 1 + (int)( 0.7f * Mlevel())
					+RingOfSharpshooting.levelDamageBonus(hero);
			return Math.max(0, dmg);
		}

		@Override
		public int max(int lvl) {
			int dmg = 6 + (int)( 0.3f * Mlevel())
					+2 * RingOfSharpshooting.levelDamageBonus(hero);
			return Math.max(0, dmg);
		}

		@Override
		public int proc(Char attacker, Char defender, int damage) {
			if (charge>=10) {

				defender.damage(Random.NormalIntRange(2 * (Mlevel()+1) * culCharge(), 3 * (Mlevel()+1) * culCharge()), this);
				defender.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				defender.sprite.flash();

				CharSprite s = defender.sprite;
				if (s != null && s.parent != null) {
					ArrayList<Lightning.Arc> arcs = new ArrayList<>();
					arcs.add(new Lightning.Arc(new PointF(s.x, s.y + s.height / 2), new PointF(s.x + s.width, s.y + s.height / 2)));
					arcs.add(new Lightning.Arc(new PointF(s.x + s.width / 2, s.y), new PointF(s.x + s.width / 2, s.y + s.height)));
					s.parent.add(new Lightning(arcs, null));
					Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
				}
			}

			return super.proc(attacker, defender, damage);
		}



		//떨어진 해머의 위치 찾기
		protected void droptoEnemy (Char ch , Item item, int cell) {

			if(ch == null || ch == curUser){
				Dungeon.level.drop(item, cell).sprite.drop();
				startPos = cell;
			}
			// 고정 적에 던질 때 떨어지는 해머의 위치 찾기
			else if (ch.properties().contains(Char.Property.IMMOVABLE)) {
				int newPlace;
				do {
					newPlace = cell + PathFinder.NEIGHBOURS8[Random.Int(8)];
				} while (!Dungeon.level.passable[newPlace] && !Dungeon.level.avoid[newPlace]);
				Dungeon.level.drop(item, newPlace).sprite.drop(cell);
				startPos = newPlace;

			} else {
				Dungeon.level.drop(item, cell).sprite.drop();
				startPos = cell;
			}

		}


		public void droptoLock (Item item, int cell) {
			Heap Hpos = Dungeon.level.heaps.get(cell);
			if (Hpos == null){
				Dungeon.level.drop(item, cell).sprite.drop(cell);
				startPos = cell;

			}
            //잠긴 상자에 던질 때 떨어지는 해머의 위치 찾기
			else if (Hpos.type == Heap.Type.LOCKED_CHEST || Hpos.type == Heap.Type.CRYSTAL_CHEST){
				int newPlace;
				do {
					newPlace = cell + PathFinder.NEIGHBOURS8[Random.Int(8)];
				} while (!Dungeon.level.passable[newPlace] && !Dungeon.level.avoid[newPlace]);
				Dungeon.level.drop(item, newPlace).sprite.drop(cell);
				startPos = newPlace;

			}else {
				Dungeon.level.drop(item, cell).sprite.drop(cell);
				startPos = cell;
			}

		}



		@Override
		protected void rangedHit(Char enemy, int cell) {
			droptoEnemy(enemy,new SpiritHammer(),cell);
		}

		@Override
		protected void rangedMiss( int cell ) {
			Dungeon.level.drop(new SpiritHammer(),cell).sprite.drop(cell);
			super.onThrow(cell);
		}




		@Override
		protected void onThrow(int cell) {
			Char enemy = Actor.findChar(cell);
			volume--;
			if (enemy == null || enemy == curUser) {
				droptoLock(new SpiritHammer(),cell);
			} else {
				if (!curUser.shoot(enemy, this)) {
					rangedMiss(cell);
				} else {
					rangedHit(enemy, cell);
					if (charge >= 10){
						charge -= usedCharge;
					}
				}
			}
			if (charge <= 0){ charge = 0; }
			updateQuickslot();
		}



		@Override
		protected float adjacentAccFactor(Char owner, Char target) {
			if (circleBackhit){
				circleBackhit = false;
				return 1.0f;
			}
			return super.adjacentAccFactor(owner, target);
		}
		boolean circleBackhit = false;



		@Override
		public void cast(final Hero user, final int dst) {
			final int cell = throwPos(user, dst);
			Mjornil.this.targetPos = cell;
			if (volume >= 1) {

				final Char enemy = Actor.findChar(cell);

				user.busy();

				user.sprite.zap(cell, new Callback() {
					@Override
					public void call() {
						Actor.add(new Actor() {

							{
								actPriority = VFX_PRIO - 1;
							}

							@Override
							protected boolean act() {
								int target = QuickSlotButton.autoAim(enemy, Mjornil.AtacktoHammer.this);
								if (target == -1) target = cell;
								cast(user, target);
								return false;
							}
						});
						curUser.next();
					}
				});
				super.cast(user, dst);
			}
		}


		public void recast ( final Hero user, final int spos, final int epos) {
			final int cell = throwStartPos( spos, epos);
			if (volume <= 0) {
				if (returnDepth == Dungeon.depth) {

					knockHammer().targetingStartPos( startPos, cell);

					final Char endTarget = Actor.findChar(cell);

			MissileSprite visual = ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class));
				visual.reset(startPos,
							cell,
							new AtacktoHammer(),
							new Callback() {
						@Override
						public void call() {

							if (endTarget == null) {
								droptoLock(new SpiritHammer(),cell);
							} else if (endTarget != null) {
								circleBackhit = true;
								if (endTarget == curUser) {
									new SpiritHammer().doPickUp(curUser);
									user.spend(-TIME_TO_PICK_UP);
								} else {
									if (curUser.shoot(endTarget, new AtacktoHammer())) {
										droptoEnemy(endTarget,new SpiritHammer(),cell);

										if (charge >= 10) {
											charge -= usedCharge;
										}
										if (charge <= 0) {
											charge = 0;
										}

										updateQuickslot();
									} else {
										Dungeon.level.drop(new SpiritHammer(), cell).sprite.drop(cell);
										startPos = cell;
									}
								}
							}
							curUser.next();
						}
					});
					float duration = Dungeon.level.trueDistance(startPos, cell) / 20f;
					curUser.sprite.parent.add(new AlphaTweener(visual, 1f, duration)); //투척 모션 비주얼 지속

					hero.sprite.zap(cell);
					hero.busy();
					curUser.spend(Actor.TICK);
				}
				return;
			}
		}
	} // AtacktoHammer 마무리







	private int returnDepth = -1;
	private int startPos;



	private CellSelector.Listener shooter = new CellSelector.Listener() {


		@Override
		public void onSelect(Integer target) {


			if (target == null) {

				return;
			}

			if (target != null) {
				if (volume >= 1) {
					knockHammer().cast(curUser, target);

				} else if (volume <= 0) {
					for (int n : PathFinder.NEIGHBOURS8) {

						Heap Hdropedpos = Dungeon.level.heaps.get(startPos);

						if (Hdropedpos != null && Hdropedpos.peek() instanceof SpiritHammer) {

							float hammerDst = Dungeon.level.distance(curUser.pos, startPos);
								//망치와 영웅과의 사이 거리
							if ( hammerDst >(int) (3.5f + (level() * 0.5f))) {
								// 망치 조종 불가능
								Hdropedpos.pickUp();
								Dungeon.level.drop(new SpiritHammer(), startPos).sprite.drop();
								GLog.w(Messages.get(Mjornil.class, "out_of_range_hammer",
										(int) (3.5f + (level() * 0.5f)),(int) (3.5f + (level() * 0.5f))));
								return;
							}
							if ( hammerDst <= (int) (3.5f + (level() * 0.5f))) {
								// 망치 조종 가능
								Hdropedpos.pickUp();
								knockHammer().recast(curUser, startPos, target);
								GLog.w(Messages.get(Mjornil.class, "control"));
								return;
							}
						} else {
							//망치 안보임
							GLog.w(Messages.get(Mjornil.class, "no_hammer"));
							return;
						}
					}
				}
			}
		}
					/*if (charge < 100) {
						PathFinder.buildDistanceMap(curUser.pos, BArray.not(Dungeon.level.solid, null),
								(int) (3.5f + (level() * 0.5f)));
						if (PathFinder.distance[target] == Integer.MAX_VALUE) {
							Dungeon.level.drop(new SpiritHammer(), startPos).sprite.drop();
							GLog.w(Messages.get(Mjornil.class, "out_of_range",(int) (3.5f + (level() * 0.5f))));

						} else
							knockHammer().recast(curUser, startPos, target);
					}*/

		@Override
		public String prompt() {
			return Messages.get(Mjornil.class, "prompt");
		}

	};





	public class chargeThunder extends ArtifactBuff {

		public boolean act() {
			LockedFloor lock = target.buff(LockedFloor.class);

			if (!cursed
					&& target.buff(MagicImmune.class) == null
			        && volume >= 1
					&& (lock == null || lock.regenOn())
					) {

				if (charge < chargeCap) {
					float chargeGain = 0.1f;
					chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);

					partialCharge += chargeGain;

					if (partialCharge >= 1) {
						charge ++;
						partialCharge --;

					}

				} else if (charge == chargeCap) {
					GLog.p(Messages.get(Mjornil.class, "full"));
					partialCharge = 0;

				}
			}
			updateQuickslot();
			spend( TICK );
			return true;
		}
	}

	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(Mjornil.class, "missile_prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return MagicalHolster.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof MissileWeapon;
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof MissileWeapon) {

				Hero hero = Dungeon.hero;
				hero.sprite.operate( hero.pos );
				Sample.INSTANCE.play( Assets.Sounds.EVOKE );
				hero.busy();
				hero.spend( Actor.TICK );

				((Mjornil)curItem).gainMissile(((MissileWeapon)item));
				item.detach(hero.belongings.backpack);
			}
		}
	};
	private static final String START_POS = "startpos";
	private static final String RETURN_DEPTH = "return_depth";
	private static final String STORED = "stored";
	private static final String VOLUME = "volume";


	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put( START_POS , startPos );
		bundle.put( RETURN_DEPTH , returnDepth );

		bundle.put( STORED, storedMissile );
		bundle.put( VOLUME, volume );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		storedMissile = bundle.getInt(STORED);
		volume = bundle.getInt(VOLUME);

		startPos = bundle.getInt(START_POS);
		returnDepth = bundle.getInt(RETURN_DEPTH);

	}

}
