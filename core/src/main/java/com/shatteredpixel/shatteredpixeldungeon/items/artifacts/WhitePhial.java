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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.BETTER_ALCHEMY;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.CONTACTLESS_TREATMENT;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.POTIONYST_INTUITION;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Plague;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.potionystbrews.PotionOfDisguise;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.potionystbrews.PotionOfGoldenLiquid;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class WhitePhial extends Artifact {

	private Potion potion;

	public static final String AC_BOTTLE = "BOTTLE";

	public static final String AC_SHOOT	= "SHOOT";



	{
		image = ItemSpriteSheet.VIAL;

		defaultAction = AC_SHOOT;

		unique = true;
		bones = false;

		charge = 100;
		chargeCap = 100;
		partialCharge = 0;

	}



	private final ArrayList<Class> potions = new ArrayList<>();
	public Class<?extends Potion> potionClass(){
		return potion != null ? potion.getClass() : null;
	}

	public WhitePhial() {
		super();
		potion = null;

		Class<?>[] potionClasses = Generator.Category.POTION.classes;
		float[] probs = Generator.Category.POTION.defaultProbs.clone(); //array of primitives, clone gives deep copy.
		int i = Random.chances(probs);

		while (i != -1){
			potions.add(potionClasses[i]);
			probs[i] = 0;

			i = Random.chances(probs);
		}
		potions.add(PotionOfStrength.class);

		potions.add(PotionOfGoldenLiquid.class);
		potions.add(PotionOfDisguise.class);
	}
//	public WhitePhial() {		potion = null;	}

	public WhitePhial(Potion potion){
		this();
		if (potion != null) potion.identify();
		this.potion = potion;
		updatePhial();
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.remove(AC_EQUIP);
		actions.remove(AC_THROW);

		actions.add(AC_BOTTLE);
		if ( potion != null ) {
			actions.add(AC_SHOOT);
		}

		return actions;
	}



	@Override
	public void activate( Char ch ) {
		Potion pot = (Potion) Reflection.newInstance(potionClass());
		if (pot != null) {
			if (pot.isIdentified()) {
				icon = pot.icon;
			}
			if (!pot.isIdentified() && Dungeon.hero != null) {
				if (Dungeon.hero.hasTalent(POTIONYST_INTUITION)) {
					if (Dungeon.hero.pointsInTalent(POTIONYST_INTUITION) == 1) {
						if (pot instanceof PotionOfFrost
								|| pot instanceof PotionOfLiquidFlame
								|| pot instanceof PotionOfParalyticGas
								|| pot instanceof PotionOfFrost) {
							icon = ItemSpriteSheet.Icons.POTION_DOWN;
						} else {
							icon = ItemSpriteSheet.Icons.POTION_UP;
						}
					}if (Dungeon.hero.pointsInTalent(POTIONYST_INTUITION) == 2) {
						pot.setKnown();
					}
				} else {
					icon = ItemSpriteSheet.Icons.SCROLL_IDENTIFY;
				}
			}
		} else {
			icon = -1;
		}
		super.activate(ch);

		updateQuickslot();
	}


	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_BOTTLE)) {

			curUser = hero;
			GameScene.selectItem(itemSelector);

		} else if (action.equals(AC_SHOOT)){
			if (charge < 100){
				usesTargeting = false;
				GLog.w(Messages.get(WhitePhial.class, "no_charge"));
			}

			else if (charge >= 100) {
				if (potion == null) {
					GameScene.show(new WndUseItem(null, this));
					return;
				}
				usesTargeting = true;
				curUser = hero;
				curItem = this;
				GameScene.selectCell(shooter);
			}
		}
	}


	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)) {
			if (container.owner != null && container.owner instanceof Hero) {
				activate((Hero)container.owner);
			}
			if (cursed) cursed = false;
			return true;
		} else {
			return false;
		}
	}



	public Item bottlePotion (Potion potion, Char owner){

		if (owner == Dungeon.hero && Dungeon.hero.hasTalent(Talent.POTION_PRESERVATION)){
			Potion pot = (Potion) Reflection.newInstance(potionClass());
			if (pot != null){
				if (charge >= 100 && Dungeon.hero.pointsInTalent(Talent.POTION_PRESERVATION) >= 2) {

					pot.collect();
					if (!pot.collect()) {
						Dungeon.level.drop(pot, owner.pos);
					}
					GLog.newLine();
					GLog.p(Messages.get(WhitePhial.class, "preserved"));

				} else if (charge < 100 && Dungeon.hero.pointsInTalent(Talent.POTION_PRESERVATION) == 1) {
					// 아무것도 없음
				} else {
					Plant.Seed seed;
					seed = (Plant.Seed) Generator.random(Generator.Category.SEED);
					if (!seed.collect()) {
						Dungeon.level.drop(seed, owner.pos);
					}
					GLog.newLine();
					GLog.p(Messages.get(WhitePhial.class, "preserved_seeds"), seed.name());
				}
			}
		}

		this.potion = potion;
		updatePhial();

		int slot = Dungeon.quickslot.getSlot(this);
		if (slot != -1){
			Dungeon.quickslot.clearSlot(slot);
			Dungeon.quickslot.setSlot( slot, this );
			updateQuickslot();
		}

		return this;
	}

	
	public void updatePhial(){
		if (potion != null) {
			if (potion.image == ItemSpriteSheet.POTION_CRIMSON){
				image = ItemSpriteSheet.PHIAL_CRIMSON;
			}
			if (potion.image == ItemSpriteSheet.POTION_AMBER){
				image = ItemSpriteSheet.PHIAL_AMBER;
			}
			if (potion.image == ItemSpriteSheet.POTION_GOLDEN
					|| potion.image == ItemSpriteSheet.LIQUID_GOLDEN){ // Golden
				image = ItemSpriteSheet.PHIAL_GOLDEN;
			}
			if (potion.image == ItemSpriteSheet.POTION_JADE){
				image = ItemSpriteSheet.PHIAL_JADE;
			}
			if (potion.image == ItemSpriteSheet.POTION_TURQUOISE
					|| potion.image == ItemSpriteSheet.LIQUID_DISGUISE){ // Disguise
				image = ItemSpriteSheet.PHIAL_TURQUOISE;
			}
			if (potion.image == ItemSpriteSheet.POTION_AZURE){
				image = ItemSpriteSheet.PHIAL_AZURE;
			}
			if (potion.image == ItemSpriteSheet.POTION_INDIGO){
				image = ItemSpriteSheet.PHIAL_INDIGO;
			}
			if (potion.image == ItemSpriteSheet.POTION_MAGENTA){
				image = ItemSpriteSheet.PHIAL_MAGENTA;
			}
			if (potion.image == ItemSpriteSheet.POTION_BISTRE){
				image = ItemSpriteSheet.PHIAL_BISTRE;
			}
			if (potion.image == ItemSpriteSheet.POTION_CHARCOAL){
				image = ItemSpriteSheet.PHIAL_CHARCOAL;
			}
			if (potion.image == ItemSpriteSheet.POTION_SILVER){
				image = ItemSpriteSheet.PHIAL_SILVER;
			}
			if (potion.image == ItemSpriteSheet.POTION_IVORY){
				image = ItemSpriteSheet.PHIAL_IVORY;
			}
		}else{
			image = ItemSpriteSheet.VIAL;
		}
		updateQuickslot();
	}



	@Override
	public String name() {
		Potion pot = (Potion) Reflection.newInstance(potionClass());
		if (potion == null) {
			return super.name();
		} else if ( potion.isIdentified()) {
			String name = Messages.get(potion, "phial_name");
			icon = pot.icon;
			updateQuickslot();
			return name;
		} else {
			String name = Messages.get(WhitePhial.class, "unknown_name", potion.name());
			return name;
		}
	}

	@Override
	public String info() {
		String info = super.info();

		if (potion != null ){
			info += "\n\n" + Messages.get(WhitePhial.class, "has_potion", potion.name());
		}

		return info;
	}


	@Override
	protected ArtifactBuff passiveBuff() {
		return new Charger();
	}

	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;
		if (charge < chargeCap && potion != null){
			partialCharge += 1f * amount;

			if (partialCharge >= 1f) {
				// 50턴마다 완충
				charge += partialCharge;
				partialCharge =0;
				updateQuickslot();
			}
		}

		if (charge == chargeCap){
			partialCharge = 0;
			return;
		}
		updateQuickslot();
	}

	public class Charger extends ArtifactBuff {

		public boolean act() {
			LockedFloor lock = target.buff(LockedFloor.class);

			if (!cursed
					&& target.buff(MagicImmune.class) == null
					&& (lock == null || lock.regenOn())
			) {

				if (charge < chargeCap) {
					float chargeGain = 2f;
					chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);

					partialCharge += chargeGain;

					if (partialCharge >= 1f ) {
						charge += partialCharge;
						partialCharge = 0;

					}
				} else if (charge == chargeCap) {
					partialCharge = 0;

				} else if ( charge >= 100) {
					charge = 100;
				}
			}
			updateQuickslot();
			spend( TICK );
			return true;
		}
	}


	private static final String POTION = "potion";
	private static final String ICON = "icon";


	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(POTION, potion);
		bundle.put(ICON, icon);

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		potion = (Potion) bundle.get(POTION);
		icon =  bundle.getInt(ICON);
		if (potion != null) {
			updatePhial();
		}

	}


	@Override
	public int value() {
		return 0;
	}

	@Override
	public String status() {
		return null;
	}

	@Override
	public String percent() {

		if (chargeCap == 100)
			return Messages.format( "%d%%", charge );

		if (charge != 0)
			return Messages.format( "%d", charge );

		return null;
	}

	private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(WhitePhial.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return PotionBandolier.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Potion && potions.contains(item.getClass());
		}

		@Override
		public void onSelect( final Item item ) {
			if (item != null) {

				if (potion == null){
					applyPotion((Potion) item);
				} else {

					String bodyText = Messages.get(WhitePhial.class, "bottle_desc");
					if (Dungeon.hero.pointsInTalent(Talent.POTION_PRESERVATION) >= 2){
						bodyText += "\n\n" + Messages.get(WhitePhial.class, "bottle_talent");
					} else if (Dungeon.hero.pointsInTalent(Talent.POTION_PRESERVATION) == 1){
						bodyText += "\n\n" + Messages.get(WhitePhial.class, "bottle_seed");
					} else {
						bodyText += "\n\n" + Messages.get(WhitePhial.class, "bottle_lost");
					}

					GameScene.show(
							new WndOptions(new ItemSprite(item),
									Messages.titleCase(item.name()),
									bodyText,
									Messages.get(WhitePhial.class, "yes"),
									Messages.get(WhitePhial.class, "no")) {
								@Override
								protected void onSelect(int index) {
									if (index == 0) {
										applyPotion((Potion)item);
										curUser.spendAndNext(1f);
									}
								}
							}
					);
				}
			}
		}

		private void applyPotion(Potion potion){
			Sample.INSTANCE.play(Assets.Sounds.DRINK);
			curUser.sprite.emitter().burst( ElmoParticle.FACTORY, 12 );
			evoke(curUser);

			if (Dungeon.hero.hasTalent(Talent.BETTER_ALCHEMY)){
				Buff.affect(Dungeon.hero,BetterUsedPhial.class);
			}

			potion.detach(curUser.belongings.backpack);

			GLog.p( Messages.get(WhitePhial.class, "bottle", potion.name()));
			bottlePotion( potion, curUser );

			activate(Dungeon.hero);
			charge = 100;

			updateQuickslot();
		}
	};

	public WhitePhial Phial(){
		return new WhitePhial();
	}

	protected void drink( Hero hero , Potion pot) {

		Sample.INSTANCE.play( Assets.Sounds.DRINK );
		hero.sprite.operate( hero.pos );
		pot.apply(hero);

		hero.spendAndNext(Actor.TICK);
		updateQuickslot();

		if (Dungeon.hero.hasTalent(Talent.POTIONYST_BARRIER)){
			Buff.affect(Dungeon.hero, Talent.ProtectivePotionTracker.class);
		}

		if (Dungeon.hero.hasTalent(Talent.POTIONYST_STRIKE)){
			Buff.affect(Dungeon.hero, Talent.PotionStrikeTracker.class);
		}
	}

	protected void teat( Char enemy , Potion pot) {
		final HashSet<Char> targets = new HashSet<>();

		Char onemore = findChar( enemy.pos, Dungeon.hero, targets);

		if (onemore == null ){
	//		GLog.w(Messages.get(this, "no_target"));
			return;
		}

		targets.add(onemore);

		while (targets.size() > 2 ){
			Char furthest = null;
			for (Char ch : targets){
				if (furthest == null){
					furthest = ch;
				} else if (Dungeon.level.trueDistance(onemore.pos, ch.pos) >=
						Dungeon.level.trueDistance(onemore.pos, furthest.pos)){
					furthest = ch;
				}
			}
			targets.remove(furthest);
		}

		((MissileSprite) onemore.sprite.parent.recycle(MissileSprite.class)).
					reset(enemy.sprite,
							onemore.pos,
							this,
							new Callback() {
								@Override
								public void call() {
									shatter(onemore,pot,curUser,onemore.pos);
								}
							});

	}


	private Char findChar( int pos, Hero hero, HashSet<Char> existingTargets){
		int dist = Dungeon.hero.pointsInTalent(Talent.BOUNCING_PHIAL);
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 1 + dist );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Char ch = Actor.findChar(i);
				if (ch != null) {
					if (ch == hero || existingTargets.contains(ch) || ch.buff(BouncedPhial.class) != null) {
						continue;
					} else {
						return ch;
					}
				}
			}
		}
		return null;
	}

	private void shatter(Char enemy, Potion pot, final Hero user, final int dst ) {

		if (curUser.subClass == HeroSubClass.DR_PLAGUE){ //역병
			if (enemy != null ){
				Buff.affect(enemy, Plague.class).set(Plague.DURATION);
			}
		}

		if (enemy.alignment == Char.Alignment.ALLY
					&& user.hasTalent(CONTACTLESS_TREATMENT)
					&& user.buff(Talent.AllyPhialCooldown.class) == null) {

			pot.applyChar(enemy);
			Buff.affect(user, Talent.AllyPhialCooldown.class, 20f);

		} else {
			pot.shatter(dst);
			Dungeon.level.pressCell(dst);
		}

		Buff.affect(enemy,BouncedPhial.class,1f);
	}



	public void cast( final Hero user, final int dst ) {
		Dungeon.hero.busy();

		throwSound();

		Char enemy = Actor.findChar(dst);
		QuickSlotButton.target(enemy);

		final float delay = castDelay(user, dst);


		Class<? extends Potion> potionCls = null;
		if (curUser.belongings.getItem(WhitePhial.class) != null) {
			potionCls = curUser.belongings.getItem(WhitePhial.class).potionClass();
		}

		if (potionCls == null){
			return;
		}

		if (Dungeon.hero.hasTalent(Talent.POTIONYST_STRIKE)){
			Buff.affect(Dungeon.hero, Talent.PotionStrikeTracker.class);
		}

		Class<? extends Potion> finalPotionCls = potionCls;
		((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
					reset(user.sprite,
							dst,
							this,
							new Callback() {
								@Override
								public void call() {
									Potion pot = (Potion) Reflection.newInstance(finalPotionCls);

									if (enemy != curUser) {
										shatter(enemy, pot, user, dst);
										if (Dungeon.hero.hasTalent(Talent.BOUNCING_PHIAL)) {
											teat(enemy, pot);
										}
									}
									if (Dungeon.hero.hasTalent(Talent.POTIONYST_BARRIER)){
										Buff.affect(Dungeon.hero, Talent.ProtectivePotionTracker.class);
									}

									Dungeon.hero.spendAndNext(delay);
									updateQuickslot();
								}
							});
	}

	private CellSelector.Listener shooter = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {

			if (target != null) {
				final int cell = throwPos(curUser, target);
				Char ch = Actor.findChar(cell);
				if (ch == curUser){

					drink(curUser,potion);

					if (potion instanceof PotionOfExperience
							|| potion instanceof PotionOfHealing
							|| potion instanceof PotionOfStrength) {
						// 힘의, 회복의, 경험의 물약은 사용시 소모됨
						potion = null;
						GLog.w(Messages.get(WhitePhial.class, "no_potion"));
					}

					activate(curUser);
					updatePhial();

				} else {
					curUser.sprite.zap(cell);
					Phial().cast(curUser, cell);

				}
				charge -= 100;
				if (Dungeon.hero.buff(BetterUsedPhial.class) != null){
					charge += 5 * Dungeon.hero.pointsInTalent(BETTER_ALCHEMY);
					Dungeon.hero.buff(BetterUsedPhial.class).detach();
				}
			}

		}
		@Override
		public String prompt() {
			return Messages.get(WhitePhial.class, "prompt");
		}
	};


	@Override
	public Emitter emitter() {
		if (potion == null || Dungeon.hero.buff(Talent.PlagueResists.class) == null) return null;
		Emitter emitter = new Emitter();
		emitter.pos(6, 4.7f);
		emitter.fillTarget = false;
		emitter.pour(PlagueParticleFactory, 0.8f);
		return emitter;
	}

	private final Emitter.Factory PlagueParticleFactory = new Emitter.Factory() {
		@Override
		//reimplementing this is needed as instance creation of new staff particles must be within this class.
		public void emit( Emitter emitter, int index, float x, float y ) {
			PlagueParticle c = (PlagueParticle)emitter.getFirstAvailable(PlagueParticle.class);
			if (c == null) {
				c = new PlagueParticle();
				emitter.add(c);
			}
			c.reset(x, y);
		}

		@Override
		//some particles need light mode, others don't
		public boolean lightMode() {
			return true;
		}
	};

	//determines particle effects to use based on wand the staff owns.
	public class PlagueParticle extends PixelParticle {

		private float minSize;
		private float maxSize;
		public float sizeJitter = 0;

		public PlagueParticle(){
			super();
		}

		public void reset( float x, float y ) {
			revive();

			speed.set(0);

			this.x = x;
			this.y = y;

			staffFx( this );
		}

		public void staffFx( PlagueParticle particle ){
			particle.color(0x008D13); particle.am = 0.85f;
			particle.setLifespan( 1.5f);
			particle.speed.polar( Random.Float(PointF.PI2), 2f );
			particle.setSize( 1f, 3f );
			particle.radiateXY(0.5f);
		}

		public void setSize( float minSize, float maxSize ){
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public void setLifespan( float life ){
			lifespan = left = life;
		}

		public void radiateXY(float amt){
			float hypot = (float)Math.hypot(speed.x, speed.y);
			this.x += speed.x/hypot*amt;
			this.y += speed.y/hypot*amt;
		}

		@Override
		public void update() {
			super.update();
			size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
		}
	}
	public static class BetterUsedPhial extends Buff {};
	public static class BouncedPhial extends FlavourBuff {};
}
