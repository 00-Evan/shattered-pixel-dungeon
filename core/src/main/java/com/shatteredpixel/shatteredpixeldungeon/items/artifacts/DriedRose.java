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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBlacksmith;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DriedRose extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_ROSE1;

		levelCap = 10;

		charge = 100;
		chargeCap = 100;

		defaultAction = AC_SUMMON;
	}

	private boolean talkedTo = false;
	private boolean firstSummon = false;
	
	private GhostHero ghost = null;
	private int ghostID = 0;
	
	private MeleeWeapon weapon = null;
	private Armor armor = null;

	public int droppedPetals = 0;

	public static final String AC_SUMMON = "SUMMON";
	public static final String AC_DIRECT = "DIRECT";
	public static final String AC_OUTFIT = "OUTFIT";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (!Ghost.Quest.completed()){
			return actions;
		}
		if (isEquipped( hero )
				&& charge == chargeCap
				&& !cursed
				&& hero.buff(MagicImmune.class) == null
				&& ghostID == 0) {
			actions.add(AC_SUMMON);
		}
		if (ghostID != 0){
			actions.add(AC_DIRECT);
		}
		if (isIdentified() && !cursed){
			actions.add(AC_OUTFIT);
		}
		
		return actions;
	}

	@Override
	public String defaultAction() {
		if (ghost != null){
			return AC_DIRECT;
		} else {
			return AC_SUMMON;
		}
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals(AC_SUMMON)) {

			if (hero.buff(MagicImmune.class) != null) return;

			if (!Ghost.Quest.completed())   GameScene.show(new WndUseItem(null, this));
			else if (ghost != null)         GLog.i( Messages.get(this, "spawned") );
			else if (!isEquipped( hero ))   GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (charge != chargeCap)   GLog.i( Messages.get(this, "no_charge") );
			else if (cursed)                GLog.i( Messages.get(this, "cursed") );
			else {
				ArrayList<Integer> spawnPoints = new ArrayList<>();
				for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
					int p = hero.pos + PathFinder.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					ghost = new GhostHero( this );
					ghostID = ghost.id();
					ghost.pos = Random.element(spawnPoints);

					GameScene.add(ghost, 1f);
					Dungeon.level.occupyCell(ghost);
					
					CellEmitter.get(ghost.pos).start( ShaftParticle.FACTORY, 0.3f, 4 );
					CellEmitter.get(ghost.pos).start( Speck.factory(Speck.LIGHT), 0.2f, 3 );

					hero.spend(1f);
					hero.busy();
					hero.sprite.operate(hero.pos);

					if (!firstSummon) {
						ghost.yell( Messages.get(GhostHero.class, "hello", Messages.titleCase(Dungeon.hero.name())) );
						Sample.INSTANCE.play( Assets.Sounds.GHOST );
						firstSummon = true;
						
					} else {
						if (BossHealthBar.isAssigned()) {
							ghost.sayBoss();
						} else {
							ghost.sayAppeared();
						}
					}

					Invisibility.dispel(hero);
					Talent.onArtifactUsed(hero);
					charge = 0;
					partialCharge = 0;
					updateQuickslot();

				} else
					GLog.i( Messages.get(this, "no_space") );
			}

		} else if (action.equals(AC_DIRECT)){
			if (ghost == null && ghostID != 0){
				Actor a = Actor.findById(ghostID);
				if (a != null){
					ghost = (GhostHero)a;
				} else {
					ghostID = 0;
				}
			}
			if (ghost != null) GameScene.selectCell(ghostDirector);
			
		} else if (action.equals(AC_OUTFIT)){
			GameScene.show( new WndGhostHero(this) );
		}
	}
	
	public int ghostStrength(){
		return 13 + level()/2;
	}

	@Override
	public String desc() {
		if (!Ghost.Quest.completed()
				&& (ShatteredPixelDungeon.scene() instanceof GameScene || ShatteredPixelDungeon.scene() instanceof AlchemyScene)){
			return Messages.get(this, "desc_no_quest");
		}
		
		String desc = super.desc();

		if (isEquipped( Dungeon.hero )){
			if (!cursed){

				if (level() < levelCap)
					desc+= "\n\n" + Messages.get(this, "desc_hint");

			} else {
				desc += "\n\n" + Messages.get(this, "desc_cursed");
			}
		}

		if (weapon != null || armor != null) {
			desc += "\n";

			if (weapon != null) {
				desc += "\n" + Messages.get(this, "desc_weapon", weapon.title());
			}

			if (armor != null) {
				desc += "\n" + Messages.get(this, "desc_armor", armor.title());
			}
		}
		
		return desc;
	}
	
	@Override
	public int value() {
		if (weapon != null){
			return -1;
		}
		if (armor != null){
			return -1;
		}
		return super.value();
	}

	@Override
	public String status() {
		if (ghost == null && ghostID != 0){
			try {
				Actor a = Actor.findById(ghostID);
				if (a != null) {
					ghost = (GhostHero) a;
				} else {
					ghostID = 0;
				}
			} catch ( ClassCastException e ){
				ShatteredPixelDungeon.reportException(e);
				ghostID = 0;
			}
		}
		if (ghost == null){
			return super.status();
		} else {
			return ((ghost.HP*100) / ghost.HT) + "%";
		}
	}
	
	@Override
	protected ArtifactBuff passiveBuff() {
		return new roseRecharge();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;

		if (ghost == null){
			if (charge < chargeCap) {
				charge += Math.round(4*amount);
				if (charge >= chargeCap) {
					charge = chargeCap;
					partialCharge = 0;
					GLog.p(Messages.get(DriedRose.class, "charged"));
				}
				updateQuickslot();
			}
		} else {
			int heal = Math.round((1 + level()/3f)*amount);
			ghost.HP = Math.min( ghost.HT, ghost.HP + heal);
			updateQuickslot();
		}
	}
	
	@Override
	public Item upgrade() {
		if (level() >= 9)
			image = ItemSpriteSheet.ARTIFACT_ROSE3;
		else if (level() >= 4)
			image = ItemSpriteSheet.ARTIFACT_ROSE2;

		//For upgrade transferring via well of transmutation
		droppedPetals = Math.max( level(), droppedPetals );
		
		if (ghost != null){
			ghost.updateRose();
		}

		return super.upgrade();
	}
	
	public Weapon ghostWeapon(){
		return weapon;
	}
	
	public Armor ghostArmor(){
		return armor;
	}

	private static final String TALKEDTO =      "talkedto";
	private static final String FIRSTSUMMON =   "firstsummon";
	private static final String GHOSTID =       "ghostID";
	private static final String PETALS =        "petals";
	
	private static final String WEAPON =        "weapon";
	private static final String ARMOR =         "armor";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);

		bundle.put( TALKEDTO, talkedTo );
		bundle.put( FIRSTSUMMON, firstSummon );
		bundle.put( GHOSTID, ghostID );
		bundle.put( PETALS, droppedPetals );
		
		if (weapon != null) bundle.put( WEAPON, weapon );
		if (armor != null)  bundle.put( ARMOR, armor );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);

		talkedTo = bundle.getBoolean( TALKEDTO );
		firstSummon = bundle.getBoolean( FIRSTSUMMON );
		ghostID = bundle.getInt( GHOSTID );
		droppedPetals = bundle.getInt( PETALS );
		
		if (bundle.contains(WEAPON)) weapon = (MeleeWeapon)bundle.get( WEAPON );
		if (bundle.contains(ARMOR))  armor = (Armor)bundle.get( ARMOR );
	}

	public class roseRecharge extends ArtifactBuff {

		@Override
		public boolean act() {
			
			spend( TICK );
			
			if (ghost == null && ghostID != 0){
				Actor a = Actor.findById(ghostID);
				if (a != null){
					ghost = (GhostHero)a;
				} else {
					ghostID = 0;
				}
			}

			if (ghost != null && !ghost.isAlive()){
				ghost = null;
			}
			
			//rose does not charge while ghost hero is alive
			if (ghost != null && !cursed && target.buff(MagicImmune.class) == null){
				
				//heals to full over 500 turns
				LockedFloor lock = target.buff(LockedFloor.class);
				if (ghost.HP < ghost.HT && (lock == null || lock.regenOn())) {
					partialCharge += (ghost.HT / 500f) * RingOfEnergy.artifactChargeMultiplier(target);
					updateQuickslot();
					
					if (partialCharge > 1) {
						ghost.HP++;
						partialCharge--;
					}
				} else {
					partialCharge = 0;
				}
				
				return true;
			}
			
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap
					&& !cursed
					&& target.buff(MagicImmune.class) == null
					&& (lock == null || lock.regenOn())) {
				//500 turns to a full charge
				partialCharge += (1/5f * RingOfEnergy.artifactChargeMultiplier(target));
				if (partialCharge > 1){
					charge++;
					partialCharge--;
					if (charge == chargeCap){
						partialCharge = 0f;
						GLog.p( Messages.get(DriedRose.class, "charged") );
					}
				}
			} else if (cursed && Random.Int(100) == 0) {

				ArrayList<Integer> spawnPoints = new ArrayList<>();

				for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
					int p = target.pos + PathFinder.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					Wraith.spawnAt(Random.element(spawnPoints));
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
				}

			}

			updateQuickslot();

			return true;
		}
	}
	
	public CellSelector.Listener ghostDirector = new CellSelector.Listener(){
		
		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			
			Sample.INSTANCE.play( Assets.Sounds.GHOST );

			ghost.directTocell(cell);

		}
		
		@Override
		public String prompt() {
			return  "\"" + Messages.get(GhostHero.class, "direct_prompt") + "\"";
		}
	};

	public static class Petal extends Item {

		{
			stackable = true;
			dropsDownHeap = true;
			
			image = ItemSpriteSheet.PETAL;
		}

		@Override
		public boolean doPickUp(Hero hero, int pos) {
			DriedRose rose = hero.belongings.getItem( DriedRose.class );

			if (rose == null){
				GLog.w( Messages.get(this, "no_rose") );
				return false;
			} if ( rose.level() >= rose.levelCap ){
				GLog.i( Messages.get(this, "no_room") );
				hero.spendAndNext(TIME_TO_PICK_UP);
				return true;
			} else {

				rose.upgrade();
				if (rose.level() == rose.levelCap) {
					GLog.p( Messages.get(this, "maxlevel") );
				} else
					GLog.i( Messages.get(this, "levelup") );

				Sample.INSTANCE.play( Assets.Sounds.DEWDROP );
				GameScene.pickUp(this, pos);
				hero.spendAndNext(TIME_TO_PICK_UP);
				return true;

			}
		}

		@Override
		public boolean isUpgradable() {
			return false;
		}

		@Override
		public boolean isIdentified() {
			return true;
		}

	}

	public static class GhostHero extends DirectableAlly {

		{
			spriteClass = GhostSprite.class;

			flying = true;
			
			state = HUNTING;
			
			properties.add(Property.UNDEAD);
		}
		
		private DriedRose rose = null;
		
		public GhostHero(){
			super();
		}

		public GhostHero(DriedRose rose){
			super();
			this.rose = rose;
			updateRose();
			HP = HT;
		}

		@Override
		public void defendPos(int cell) {
			yell(Messages.get(this, "directed_position_" + Random.IntRange(1, 5)));
			super.defendPos(cell);
		}

		@Override
		public void followHero() {
			yell(Messages.get(this, "directed_follow_" + Random.IntRange(1, 5)));
			super.followHero();
		}

		@Override
		public void targetChar(Char ch) {
			yell(Messages.get(this, "directed_attack_" + Random.IntRange(1, 5)));
			super.targetChar(ch);
		}

		private void updateRose(){
			if (rose == null) {
				rose = Dungeon.hero.belongings.getItem(DriedRose.class);
			}
			
			//same dodge as the hero
			defenseSkill = (Dungeon.hero.lvl+4);
			if (rose == null) return;
			HT = 20 + 8*rose.level();
		}

		@Override
		protected boolean act() {
			updateRose();
			if (rose == null
					|| !rose.isEquipped(Dungeon.hero)
					|| Dungeon.hero.buff(MagicImmune.class) != null){
				damage(1, this);
			}
			
			if (!isAlive()) {
				return true;
			}
			return super.act();
		}

		@Override
		public int attackSkill(Char target) {
			
			//same accuracy as the hero.
			int acc = Dungeon.hero.lvl + 9;
			
			if (rose != null && rose.weapon != null){
				acc *= rose.weapon.accuracyFactor( this, target );
			}
			
			return acc;
		}
		
		@Override
		public float attackDelay() {
			float delay = super.attackDelay();
			if (rose != null && rose.weapon != null){
				delay *= rose.weapon.delayFactor(this);
			}
			return delay;
		}
		
		@Override
		protected boolean canAttack(Char enemy) {
			return super.canAttack(enemy) || (rose != null && rose.weapon != null && rose.weapon.canReach(this, enemy.pos));
		}
		
		@Override
		public int damageRoll() {
			int dmg = 0;
			if (rose != null && rose.weapon != null){
				dmg += rose.weapon.damageRoll(this);
			} else {
				dmg += Random.NormalIntRange(0, 5);
			}
			
			return dmg;
		}
		
		@Override
		public int attackProc(Char enemy, int damage) {
			damage = super.attackProc(enemy, damage);
			if (rose != null && rose.weapon != null) {
				damage = rose.weapon.proc( this, enemy, damage );
				if (!enemy.isAlive() && enemy == Dungeon.hero){
					Dungeon.fail(getClass());
					GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
				}
			}
			return damage;
		}
		
		@Override
		public int defenseProc(Char enemy, int damage) {
			if (rose != null && rose.armor != null) {
				damage = rose.armor.proc( enemy, this, damage );
			}
			return super.defenseProc(enemy, damage);
		}
		
		@Override
		public void damage(int dmg, Object src) {
			//TODO improve this when I have proper damage source logic
			if (rose != null && rose.armor != null && rose.armor.hasGlyph(AntiMagic.class, this)
					&& AntiMagic.RESISTS.contains(src.getClass())){
				dmg -= AntiMagic.drRoll(this, rose.armor.buffedLvl());
			}
			
			super.damage( dmg, src );
			
			//for the rose status indicator
			Item.updateQuickslot();
		}
		
		@Override
		public float speed() {
			float speed = super.speed();
			
			if (rose != null && rose.armor != null){
				speed = rose.armor.speedFactor(this, speed);
			}

			//moves 2 tiles at a time when returning to the hero
			if (state == WANDERING
					&& defendingPos == -1
					&& Dungeon.level.distance(pos, Dungeon.hero.pos) > 1){
				speed *= 2;
			}
			
			return speed;
		}
		
		@Override
		public int defenseSkill(Char enemy) {
			int defense = super.defenseSkill(enemy);

			if (defense != 0 && rose != null && rose.armor != null ){
				defense = Math.round(rose.armor.evasionFactor( this, defense ));
			}
			
			return defense;
		}
		
		@Override
		public float stealth() {
			float stealth = super.stealth();
			
			if (rose != null && rose.armor != null){
				stealth = rose.armor.stealthFactor(this, stealth);
			}
			
			return stealth;
		}
		
		@Override
		public int drRoll() {
			int dr = super.drRoll();
			if (rose != null && rose.armor != null){
				dr += Random.NormalIntRange( rose.armor.DRMin(), rose.armor.DRMax());
			}
			if (rose != null && rose.weapon != null){
				dr += Random.NormalIntRange( 0, rose.weapon.defenseFactor( this ));
			}
			return dr;
		}

		//used in some glyph calculations
		public Armor armor(){
			if (rose != null){
				return rose.armor;
			} else {
				return null;
			}
		}

		@Override
		public boolean isImmune(Class effect) {
			if (effect == Burning.class
					&& rose != null
					&& rose.armor != null
					&& rose.armor.hasGlyph(Brimstone.class, this)){
				return true;
			}
			return super.isImmune(effect);
		}

		@Override
		public boolean interact(Char c) {
			updateRose();
			if (c == Dungeon.hero && rose != null && !rose.talkedTo){
				rose.talkedTo = true;
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndQuest(GhostHero.this, Messages.get(GhostHero.this, "introduce") ));
					}
				});
				return true;
			} else {
				return super.interact(c);
			}
		}

		@Override
		public void die(Object cause) {
			sayDefeated();
			super.die(cause);
		}

		@Override
		public void destroy() {
			updateRose();
			if (rose != null) {
				rose.ghost = null;
				rose.charge = 0;
				rose.partialCharge = 0;
				rose.ghostID = -1;
			}
			super.destroy();
		}
		
		public void sayAppeared(){
			if (Dungeon.hero.buff(AscensionChallenge.class) != null){
				yell( Messages.get( this, "dialogue_ascension_" + Random.IntRange(1, 6) ));

			} else {

				int depth = (Dungeon.depth - 1) / 5;

				//only some lines are said on the first floor of a depth
				int variant = Dungeon.depth % 5 == 1 ? Random.IntRange(1, 3) : Random.IntRange(1, 6);

				switch (depth) {
					case 0:
						yell(Messages.get(this, "dialogue_sewers_" + variant));
						break;
					case 1:
						yell(Messages.get(this, "dialogue_prison_" + variant));
						break;
					case 2:
						yell(Messages.get(this, "dialogue_caves_" + variant));
						break;
					case 3:
						yell(Messages.get(this, "dialogue_city_" + variant));
						break;
					case 4:
					default:
						yell(Messages.get(this, "dialogue_halls_" + variant));
						break;
				}
			}
			if (ShatteredPixelDungeon.scene() instanceof GameScene) {
				Sample.INSTANCE.play( Assets.Sounds.GHOST );
			}
		}
		
		public void sayBoss(){
			int depth = (Dungeon.depth - 1) / 5;
			
			switch(depth){
				case 0:
					yell( Messages.get( this, "seen_goo_" + Random.IntRange(1, 3) ));
					break;
				case 1:
					yell( Messages.get( this, "seen_tengu_" + Random.IntRange(1, 3) ));
					break;
				case 2:
					yell( Messages.get( this, "seen_dm300_" + Random.IntRange(1, 3) ));
					break;
				case 3:
					yell( Messages.get( this, "seen_king_" + Random.IntRange(1, 3) ));
					break;
				case 4: default:
					yell( Messages.get( this, "seen_yog_" + Random.IntRange(1, 3) ));
					break;
			}
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
		}
		
		public void sayDefeated(){
			if (BossHealthBar.isAssigned()){
				yell( Messages.get( this, "defeated_by_boss_" + Random.IntRange(1, 3) ));
			} else {
				yell( Messages.get( this, "defeated_by_enemy_" + Random.IntRange(1, 3) ));
			}
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
		}
		
		public void sayHeroKilled(){
			yell( Messages.get( this, "player_killed_" + Random.IntRange(1, 3) ));
			GLog.newLine();
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
		}
		
		public void sayAnhk(){
			yell( Messages.get( this, "blessed_ankh_" + Random.IntRange(1, 3) ));
			Sample.INSTANCE.play( Assets.Sounds.GHOST );
		}
		
		{
			immunities.add( ToxicGas.class );
			immunities.add( CorrosiveGas.class );
			immunities.add( Burning.class );
			immunities.add( ScrollOfRetribution.class );
			immunities.add( ScrollOfPsionicBlast.class );
			immunities.add( AllyBuff.class );
		}

	}
	
	private static class WndGhostHero extends Window{
		
		private static final int BTN_SIZE	= 32;
		private static final float GAP		= 2;
		private static final float BTN_GAP	= 12;
		private static final int WIDTH		= 116;
		
		private WndBlacksmith.ItemButton btnWeapon;
		private WndBlacksmith.ItemButton btnArmor;
		
		WndGhostHero(final DriedRose rose){
			
			IconTitle titlebar = new IconTitle();
			titlebar.icon( new ItemSprite(rose) );
			titlebar.label( Messages.get(this, "title") );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );
			
			RenderedTextBlock message =
					PixelScene.renderTextBlock(Messages.get(this, "desc", rose.ghostStrength()), 6);
			message.maxWidth( WIDTH );
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );
			
			btnWeapon = new WndBlacksmith.ItemButton(){
				@Override
				protected void onClick() {
					if (rose.weapon != null){
						item(new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
						if (!rose.weapon.doPickUp(Dungeon.hero)){
							Dungeon.level.drop( rose.weapon, Dungeon.hero.pos);
						}
						rose.weapon = null;
					} else {
						GameScene.selectItem(new WndBag.ItemSelector() {

							@Override
							public String textPrompt() {
								return Messages.get(WndGhostHero.class, "weapon_prompt");
							}

							@Override
							public Class<?extends Bag> preferredBag(){
								return Belongings.Backpack.class;
							}

							@Override
							public boolean itemSelectable(Item item) {
								return item instanceof MeleeWeapon;
							}

							@Override
							public void onSelect(Item item) {
								if (!(item instanceof MeleeWeapon)) {
									//do nothing, should only happen when window is cancelled
								} else if (item.unique) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_unique"));
									hide();
								} else if (!item.isIdentified()) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_unidentified"));
									hide();
								} else if (item.cursed) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_cursed"));
									hide();
								} else if (((MeleeWeapon)item).STRReq() > rose.ghostStrength()) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_strength"));
									hide();
								} else {
									if (item.isEquipped(Dungeon.hero)){
										((MeleeWeapon) item).doUnequip(Dungeon.hero, false, false);
									} else {
										item.detach(Dungeon.hero.belongings.backpack);
									}
									rose.weapon = (MeleeWeapon) item;
									item(rose.weapon);
								}
								
							}
						});
					}
				}
			};
			btnWeapon.setRect( (WIDTH - BTN_GAP) / 2 - BTN_SIZE, message.top() + message.height() + GAP, BTN_SIZE, BTN_SIZE );
			if (rose.weapon != null) {
				btnWeapon.item(rose.weapon);
			} else {
				btnWeapon.item(new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
			}
			add( btnWeapon );
			
			btnArmor = new WndBlacksmith.ItemButton(){
				@Override
				protected void onClick() {
					if (rose.armor != null){
						item(new WndBag.Placeholder(ItemSpriteSheet.ARMOR_HOLDER));
						if (!rose.armor.doPickUp(Dungeon.hero)){
							Dungeon.level.drop( rose.armor, Dungeon.hero.pos);
						}
						rose.armor = null;
					} else {
						GameScene.selectItem(new WndBag.ItemSelector() {

							@Override
							public String textPrompt() {
								return Messages.get(WndGhostHero.class, "armor_prompt");
							}

							@Override
							public Class<?extends Bag> preferredBag(){
								return Belongings.Backpack.class;
							}

							@Override
							public boolean itemSelectable(Item item) {
								return item instanceof Armor;
							}

							@Override
							public void onSelect(Item item) {
								if (!(item instanceof Armor)) {
									//do nothing, should only happen when window is cancelled
								} else if (item.unique || ((Armor) item).checkSeal() != null) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_unique"));
									hide();
								} else if (!item.isIdentified()) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_unidentified"));
									hide();
								} else if (item.cursed) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_cursed"));
									hide();
								} else if (((Armor)item).STRReq() > rose.ghostStrength()) {
									GLog.w( Messages.get(WndGhostHero.class, "cant_strength"));
									hide();
								} else {
									if (item.isEquipped(Dungeon.hero)){
										((Armor) item).doUnequip(Dungeon.hero, false, false);
									} else {
										item.detach(Dungeon.hero.belongings.backpack);
									}
									rose.armor = (Armor) item;
									item(rose.armor);
								}
								
							}
						});
					}
				}
			};
			btnArmor.setRect( btnWeapon.right() + BTN_GAP, btnWeapon.top(), BTN_SIZE, BTN_SIZE );
			if (rose.armor != null) {
				btnArmor.item(rose.armor);
			} else {
				btnArmor.item(new WndBag.Placeholder(ItemSpriteSheet.ARMOR_HOLDER));
			}
			add( btnArmor );
			
			resize(WIDTH, (int)(btnArmor.bottom() + GAP));
		}
	
	}
}
