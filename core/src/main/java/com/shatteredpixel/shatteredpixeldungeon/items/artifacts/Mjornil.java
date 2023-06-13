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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
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
/*			} else if (volume<=0 && returnDepth != Dungeon.depth){
				startPos = curUser.pos;
				GLog.i(Messages.get(Mjornil.class, "need_to_hammer"));
				usesTargeting = false;
*/			} else {
				usesTargeting = true;
				curUser = hero;
				curItem = this;
				GameScene.selectCell(shooter);

				updateQuickslot();
			}
		}else if (action.equals(AC_LVLUP)){
			GameScene.selectItem(itemSelector);
		}
	}



	@Override
	public String desc() {
		String desc = super.desc();

		if (isEquipped(Dungeon.hero)) {
			if (!cursed) {
				if (level() < levelCap){
					desc += "\n\n" + Messages.get(this, "desc_hint",
							3+(int)(Math.sqrt(8 * level() + 1) - 1)/2,3+(int)(Math.sqrt(8 * level() + 1) - 1)/2);
				}
							// 강화 설명

			} else {
				desc += "\n\n" + Messages.get(this, "desc_cursed"); // 저주 받은 상태 설명
			}
		}else if (!isEquipped(Dungeon.hero)) {

			return desc;

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

		if (charge == chargeCap){
			partialCharge = 0;
			return;
		}
		updateQuickslot();
	}

	public void collectHam ( SpiritHammer ham ) {

		volume ++;
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
	}

	//업그래이드를 위한 단계
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
						3+(int)(Math.sqrt(8 * level() + 1) - 1)/2,3+(int)(Math.sqrt(8 * level() + 1) - 1)/2);
			}
		} else {
			GLog.i( Messages.get(this, "feed") );
		}
	}


	@Override
	public String info() {
		String info = desc();
		if (isEquipped(Dungeon.hero)) {
			info += "\n\n" + Messages.get(Mjornil.class, "stats",
					Math.round(1 + (int) (0.2f * level())
							+ RingOfSharpshooting.levelDamageBonus(hero)),
					Math.round(5 + (int) (0.2f * level())
							+ (int) 1.5f * RingOfSharpshooting.levelDamageBonus(hero)),
					Math.round(2 * (level() + 1)), Math.round(3 * (level() + 1))
			);
			if (9 > Dungeon.hero.STR()) {
				info += " " + Messages.get(Mjornil.class, "too_heavy");
			} else if (Dungeon.hero.STR() > 9) {
				info += " " + Messages.get(Mjornil.class, "excess_str", Dungeon.hero.STR() - 9);
			}
			info += "\n\n" + Messages.get(Mjornil.class, "distance");

		}return info;
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
			image = ItemSpriteSheet.ARTIFACT_MJORNIL;

			hitSound = Assets.Sounds.HIT_CRUSH;
			hitSoundPitch = 0.8f;

		}


		public int culCharge(){
			if (charge >= 20) {
				return  1;
			}else {
				return 0;
			}
		}


		@Override
		public int min(int lvl) {
			int dmg = 1 + (int)( 0.2f * Mlevel())
					+RingOfSharpshooting.levelDamageBonus(hero);
			return Math.max(0, dmg);
		}

		@Override
		public int max(int lvl) {
			int dmg = 5 + (int)( 0.2f * Mlevel())
					+(int)1.5f * RingOfSharpshooting.levelDamageBonus(hero);
			return Math.max(0, dmg);
		}


		@Override
		public int proc(Char attacker, Char defender, int damage) {
			if (charge>=20) {

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
				if (defender != Dungeon.hero &&
						Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
						//흑마법사의 영혼의 표식 부여 (완드의 계산 방식의 2배)
						Random.Float() > 0.5f*(Math.pow(0.92f, (Mlevel()*culCharge())+1) - 0.07f)){
					SoulMark.prolong(defender, SoulMark.class, SoulMark.DURATION + Mlevel());
				}
				if (charge >= 20) {
					charge -= 20f*culCharge();
				}
				if (charge <= 0) {
					charge = 0;
				}
			}

			return super.proc(attacker, defender, damage);
		}


		@Override
		protected void rangedHit(Char enemy, int cell) {
			Dungeon.level.drop(new SpiritHammer(),cell).sprite.drop();

		}

		@Override
		protected void rangedMiss( int cell ) {
			Dungeon.level.drop(new SpiritHammer(),cell).sprite.drop(cell);

		}


		@Override
		protected void onThrow(int cell) {
			Char enemy = Actor.findChar(cell);
			volume--;
			if (enemy == null || enemy == curUser) {

				Dungeon.level.drop(new SpiritHammer(),cell).sprite.drop();
			} else {
				if (!curUser.shoot(enemy, this)) {
					rangedMiss(cell);

				} else {
					rangedHit(enemy, cell);
				}
			}
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


		public void recast ( final Hero user, final int spos, final int epos) {
			final int cell = throwStartPos( spos, epos);
			if (volume <= 0) {

				startPos = spos;

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
								Dungeon.level.drop(new SpiritHammer(),cell).sprite.drop();
							} else if (endTarget != null) {
								circleBackhit = true;
								if (endTarget == curUser) {
									new SpiritHammer().doPickUp(curUser);
									user.spend(-TIME_TO_PICK_UP);

								} else {
									if (curUser.shoot(endTarget, new AtacktoHammer())) {
										Dungeon.level.drop(new SpiritHammer(),cell).sprite.drop();
										updateQuickslot();
									} else {

										rangedMiss(cell);

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
				return;
			}
		}
	} // AtacktoHammer 마무리



	private int startPos;


	private CellSelector.Listener shooter = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {

			if (target == null) {return;}

			if (target != null) {
				if (volume >= 1) {
					knockHammer().cast(curUser, target);
				//떨어진 해머 투척
				} else if (volume <= 0) {
					if (Dungeon.level.heroFOV[target]) {
						for (int i = 0; i < Dungeon.level.length(); i++) {
							for (Heap heap : Dungeon.level.heaps.valueList()) {
								for (Item item : heap.items.toArray(new Item[0])) {
									if (item instanceof SpiritHammer) {
										int p = heap.pos;

										Heap Hdropedpos = Dungeon.level.heaps.get(p);

										float hammerDst = Dungeon.level.distance(curUser.pos, p);
										//망치와 영웅과의 사이 거리
										if (Hdropedpos != null && Hdropedpos.peek() instanceof SpiritHammer) {

											if (hammerDst > 3f + (int) (Math.sqrt(8 * level() + 1) - 1) / 2) {
												// 망치 사거리 밖 조종 불가
												Dungeon.level.drop(new SpiritHammer(), p).sprite.drop();
												GLog.w(Messages.get(Mjornil.class, "out_of_range_hammer"));
												return;
											}
											if (hammerDst <= 3 + (int) (Math.sqrt(8 * level() + 1) - 1) / 2) {
												// 망치 조종 가능
												Hdropedpos.pickUp();
												knockHammer().recast(curUser, p, target);
												GLog.w(Messages.get(Mjornil.class, "control"));
												return;
											}

										} else {
											//망치 위 다른 아이템 , 망치의 위치 변경시 문구
											GLog.w(Messages.get(Mjornil.class, "no_hammer"));
											return;
										}
									}
								}
							}
						}
					}else if (Dungeon.level.heroFOV[target] == false) {
						// 시야 밖
						GLog.w(Messages.get(Mjornil.class, "no_sign"));
					}
				}
			}
		}
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
				Sample.INSTANCE.play( Assets.Sounds.DRINK );
				hero.busy();
				hero.spend( Actor.TICK );

				((Mjornil)curItem).gainMissile(((MissileWeapon)item));
				item.detach(hero.belongings.backpack);
			}
		}
	};
	private static final String START_POS = "startpos";
	private static final String STORED = "stored";
	private static final String VOLUME = "volume";


	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put( START_POS , startPos );
		bundle.put( STORED, storedMissile );
		bundle.put( VOLUME, volume );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		storedMissile = bundle.getInt(STORED);
		volume = bundle.getInt(VOLUME);
		startPos = bundle.getInt(START_POS);

	}

}
