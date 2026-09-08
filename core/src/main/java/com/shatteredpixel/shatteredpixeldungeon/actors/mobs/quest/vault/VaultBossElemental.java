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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VaultBossElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class VaultBossElemental extends Mob {

	{
		HP = HT = 600;
		spriteClass = VaultBossElementalSprite.class;

		EXP = 30;
		defenseSkill = 20;

		properties.add(Property.BOSS);
	}

	public VaultBossElemental(){
		super();
		form = ElementalForm.values()[Random.Int(3)];
		formChances[form.ordinal()]--;
	}

	@Override
	public int damageRoll() {
		//frost form does less melee damage, as you're meant to fight it up-close
		if (form == ElementalForm.FROST){
			return Random.NormalIntRange( 15, 20 );
		} else {
			return Random.NormalIntRange( 20, 25 );
		}
	}

	@Override
	public int attackSkill( Char target ) {
		return 28;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 10);
	}

	@Override
	public int defenseSkill(Char enemy) {
		if (form == ElementalForm.FIRE
				&& enemy instanceof Hero
				&& ((Hero) enemy).belongings.attackingWeapon() instanceof MissileWeapon
				&& !Dungeon.level.adjacent(pos, enemy.pos)){
			//always gets hit by a thrown weapon attack when not adjacent
			return 0;
		} else if (form == ElementalForm.FROST
				&& enemy instanceof Hero
				&& ((Hero) enemy).belongings.attackingWeapon() instanceof MeleeWeapon) {
			//halved evasion in frost form vs. melee attacks
			return super.defenseSkill(enemy)/2;
		} else {
			return super.defenseSkill(enemy);
		}
	}

	public enum ElementalForm {
		FIRE,
		FROST,
		SHOCK,
		UNSTABLE //currently unused
	}
	private ElementalForm form = null; //only initially
	private float[] formChances = new float[]{2, 2, 2}; //up to 2 uses of each form per fight

	protected int envAttackCooldown = Random.NormalIntRange( 10, 15 );
	protected int spAttackCooldown = Random.NormalIntRange( 6, 10 );
	protected int spTargetCell = -1;

	protected int lastEnemyPos = -1; //used for tracking targeting on some attacks

	public void changeForm(){
		ElementalForm newForm;
		do {
			newForm = ElementalForm.values()[Random.chances(formChances)];
		} while (newForm == form);
		formChances[newForm.ordinal()]--;
		setElementalForm(newForm);
	}

	public ElementalForm curForm(){
		return form;
	}

	public void setElementalForm( ElementalForm form ){
		//always remove pincushion as we're either leaving or entering frost form
		Buff.affect(this, PinCushionRemover.class).preferGrouping = this.form == ElementalForm.FIRE;

		this.form = form;
		boolean wasTurned = sprite.flipHorizontal;

		((VaultBossElementalSprite)sprite).updateForm();
		AttackIndicator.target(this);
		Emitter e = sprite.emitter();
		//centered a bit, but not totally
		e.fillTarget = false;
		e.pos(sprite, 4, 4, 24, 24);
		if (form == ElementalForm.FIRE){
			e.burst(FlameParticle.FACTORY, 50);

			for (Buff b : buffs()){
				if (b instanceof Chill || b instanceof Frost){
					b.detach();
				}
			}
		} else if (form == ElementalForm.FROST){
			e.burst(MagicMissile.MagicParticle.FACTORY, 50);

			for (Buff b : buffs()){
				if (b instanceof Burning){
					b.detach();
				}
			}
		} else if (form == ElementalForm.SHOCK){
			e.burst(SparkParticle.FACTORY, 50);
		}

		//don't want to follow through now that form changed, so force a new sp attack instead
		if (spTargetCell != -1){
			spTargetCell = -1;
			spAttackCooldown = 0;
		}

		//significanlty reduce environment attack cooldown
		envAttackCooldown /= 2;

		sprite.flipHorizontal = wasTurned;
		BossHealthBar.assignBoss(this, true);
		weakAnnounced = false;
	}

	@Override
	public void aggro(Char ch) {
		super.aggro(ch);
		enemySeen = true; //to prevent opening surprise attack
	}

	@Override
	protected boolean act() {
		if (spTargetCell != -1 && paralysed == 0){
			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( spTargetCell );
				lastEnemyPos = enemy.pos;
				return false;
			} else {
				zap();
				lastEnemyPos = enemy.pos;
				return true;
			}
		}

		spAttackCooldown--;
		envAttackCooldown--;
		if (state == HUNTING && paralysed == 0){
			if (spAttackCooldown <= 0){
				spend(GameMath.gate(attackDelay(), (int)Math.ceil(Dungeon.hero.cooldown()), 3*attackDelay()));
				if (form == ElementalForm.FIRE){
					setupFireBall(enemy);
				} else if (form == ElementalForm.FROST){
					setupFrostCone(enemy);
				} else if (form == ElementalForm.SHOCK){
					setupLightningBolt(enemy);
				}

				Dungeon.hero.interrupt();
				lastEnemyPos = enemy.pos;
				return true;
			} else if (envAttackCooldown <= 0){
				spend(TICK);
				if (form == ElementalForm.FIRE){
					setupFireWall();
				} else if (form == ElementalForm.FROST){
					setupFrostVortex();
				} else if (form == ElementalForm.SHOCK){
					setupLightningChase();
				}
				//not an actual attack, do nothing
				sprite.operate(enemy.pos);
				envAttackCooldown = Random.NormalIntRange( 10, 15 );
				//shock form gets faster abilities
				if (form == ElementalForm.SHOCK){
					spAttackCooldown = (int) (spAttackCooldown*0.67f);
				}

				Dungeon.hero.interrupt();
				lastEnemyPos = enemy.pos;
				return true;
			}
		}


		AiState lastState = state;
		boolean result = super.act();

		//if state changed from wandering to hunting, we haven't acted yet, don't update.
		if (!(lastState == WANDERING && state == HUNTING)) {
			if (enemy != null) {
				lastEnemyPos = enemy.pos;
			} else {
				lastEnemyPos = Dungeon.hero.pos;
			}
		}

		return result;
	}

	protected void zap() {
		spend( Actor.TICK );

		Invisibility.dispel(this);
		if (form == ElementalForm.FIRE){
			doFireBall(spTargetCell);
		} else if (form == ElementalForm.FROST){
			doFrostCone(spTargetCell);
		} else if (form == ElementalForm.SHOCK){
			doLightningBolt(spTargetCell);
		}

		spAttackCooldown = Random.NormalIntRange( 6, 10 );
		//shock form gets faster abilities
		if (form == ElementalForm.SHOCK){
			spAttackCooldown = (int) (spAttackCooldown*0.67f);
		}
		spTargetCell = -1;
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		if (form == ElementalForm.SHOCK && enemy == Dungeon.hero && !(Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
			enemy.sprite.parent.addToFront( new Lightning( sprite.center(), enemy.sprite.center(), null ) );
			enemy.damage( Random.IntRange(5, 10), new Shocking() );
			Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
			PixelScene.shake( 2, 0.3f );
			enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			enemy.sprite.flash();
			if (!enemy.isAlive()){
				Badges.validateDeathFromEnemyMagic();
				Dungeon.fail(this);
			} else {
				GLog.w(Messages.get(this, "shock_resist"));
			}
		}
		return super.defenseProc(enemy, damage);
	}

	private boolean weakAnnounced = false;

	@Override
	public void damage(int dmg, Object src) {
		//fire form is resistant to magic and weak to thrown weapons
		if (form == ElementalForm.FIRE){
			if (AntiMagic.RESISTS.contains(src.getClass())){
				dmg /= 4;
				//prompts faster attacks, only do this if it's from the hero
				if (src instanceof Wand || src instanceof ClericSpell){
					GLog.w(Messages.get(this, "fire_resist"));
					spAttackCooldown -= 3;
					envAttackCooldown -= 5;
				}
			} else if (src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon){
				if (!weakAnnounced){
					GLog.p(Messages.get(this, "fire_weak"));
					weakAnnounced = true;
				}
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				dmg += 10;
			}
		//frost form is resistant to thrown weapons and weak to melee (only from the hero though!)
		} else if ( form == ElementalForm.FROST ){
			if (src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon){
				GLog.w(Messages.get(this, "frost_resist"));
				//penalty is that the weapon sticks
				dmg /= 4;
			} else if (src == Dungeon.hero && !(Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				if (!weakAnnounced){
					GLog.p(Messages.get(this, "frost_weak"));
					weakAnnounced = true;
				}
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				dmg += 10;
			}
		//shock form is resistant to melee and weak to magic
		} else if ( form == ElementalForm.SHOCK ){
			if (AntiMagic.RESISTS.contains(src.getClass())){
				if (!weakAnnounced){
					GLog.p(Messages.get(this, "shock_weak"));
					weakAnnounced = true;
				}
				//all wands get a little charge
				if (src instanceof Wand || src instanceof ClericSpell){
					Dungeon.hero.belongings.charge(0.2f);
				}
				Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				dmg += 10;
			} else if (src instanceof Char && !(src == Dungeon.hero && Dungeon.hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				//resisted text already in defenseproc, as well as shock penalty (only for the hero)
				dmg /= 4;
			}
		}

		int hpBracket = HT / 5; //120 HP
		int curbracket = (int) Math.ceil(HP / (float)hpBracket);

		int preHP = HP;
		super.damage(dmg, src);

		int dmgTaken = preHP - HP;
		if (dmgTaken > 0) {
			envAttackCooldown -= dmgTaken/24f;
			spAttackCooldown -= dmgTaken/12f;
		}

		if (HP <= (curbracket-1)*hpBracket){

			if (isAlive()) {
				//cannot be hit through multiple brackets at a time
				HP = Math.max(HP, (curbracket-2)*hpBracket);

				//changes forms!
				ElementalForm newForm;
				do {
					newForm = ElementalForm.values()[Random.Int(3)];
				} while (newForm == form);
				changeForm();
				spend(TICK);
			}
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		Dungeon.level.unseal();
		GameScene.bossSlain();
	}

	@Override
	public CharSprite sprite() {
		if (form == null){
			changeForm();
		}
		CharSprite sprite = super.sprite();
		if (form != null) {
			((VaultBossElementalSprite)sprite).setForm(form);
		}
		return sprite;
	}

	@Override
	public boolean add(Buff buff) {
		if (buff instanceof PinCushion && form != ElementalForm.FROST){
			Buff.affect(this, PinCushionRemover.class).preferGrouping = this.form == ElementalForm.FIRE;
		}

		boolean harmful = false;
		if (form == ElementalForm.FIRE){
			harmful = buff instanceof Frost || buff instanceof Chill;
		} else if (form == ElementalForm.FROST){
			harmful = buff instanceof Burning;
		}

		//damaged by these, but much less so than regular elementals
		if (harmful){
			damage( Random.NormalIntRange( 5, 10 ), buff );
			return false;
		}

		return super.add(buff);
	}

	@Override
	public HashSet<Property> properties() {
		HashSet<Property> props = new HashSet<>(properties);
		if (form == ElementalForm.FIRE){
			props.add(Property.FIERY);
		} else if (form == ElementalForm.FROST){
			props.add(Property.ICY);
		} else if (form == ElementalForm.SHOCK){
			props.add(Property.ELECTRIC);
		}
		return props;
	}

	@Override
	public float resist(Class effect) {
		if (form == ElementalForm.SHOCK && effect == WandOfLightning.class){
			return 1; //shock form doesn't resist wand of lightning as it's a wand, and wands are effective vs. shock form
		}
		return super.resist(effect);
	}

	@Override
	public String description() {
		String desc = super.description();
		if (form != null){
			switch (form) {
				default:
				case FIRE:
					return desc + "\n\n" + Messages.get(this, "desc_fire");
				case FROST:
					return desc + "\n\n" + Messages.get(this, "desc_frost");
				case SHOCK:
					return desc + "\n\n" + Messages.get(this, "desc_shock");
			}
		} else {
			return desc;
		}
	}

	private static final String FORM = "elemental_form";

	private static final String ENV_ATK_COOLDOWN = "env_atk_cooldown";
	private static final String SP_ATK_COOLDOWN = "sp_atk_cooldown";
	private static final String SP_TARGET_CELL = "sp_target_cell";

	private static final String LAST_ENEMY_POS = "last_enemy_pos";
	private static final String LIGHTNING_OFS = "lightning_ofs";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(FORM, form);

		bundle.put(ENV_ATK_COOLDOWN, envAttackCooldown);
		bundle.put(SP_ATK_COOLDOWN, spAttackCooldown);
		bundle.put(SP_TARGET_CELL, spTargetCell);

		bundle.put(LAST_ENEMY_POS, lastEnemyPos);
		bundle.put(LIGHTNING_OFS, lightningOfs);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		form = bundle.getEnum(FORM, ElementalForm.class);

		envAttackCooldown = bundle.getInt(ENV_ATK_COOLDOWN);
		spAttackCooldown = bundle.getInt(SP_ATK_COOLDOWN);
		spTargetCell = bundle.getInt(SP_TARGET_CELL);

		lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
		lightningOfs = bundle.getInt(LIGHTNING_OFS);

		BossHealthBar.assignBoss(this);
	}

	//used to forceably remove pincushion after its applied
	public static class PinCushionRemover extends Buff{

		{
			actPriority = VFX_PRIO;
		}

		//triggered when elemental is currently in or leaving fire form, making collecting easier
		public boolean preferGrouping = false;

		@Override
		public boolean act() {

			if (target.buff(PinCushion.class) == null){
				detach();
				return true;
			}

			PathFinder.buildDistanceMap(target.pos, Dungeon.level.passable, 2);
			ArrayList<Integer> candidates = new ArrayList<>();
			int furthestDist = 1;
			int closestDist = 100;
			for (int i = 0; i < Dungeon.level.length(); i++){
				if (PathFinder.distance[i] == 2){
					int dist = Dungeon.level.distance(i, Dungeon.hero.pos);
					if (dist > 1) {
						if (dist < closestDist){
							closestDist = dist;
						} if (dist > furthestDist){
							furthestDist = dist;
						}
						candidates.add(i);
					}
				}
			}

			//prevent overlap if hero is close
			if (Math.abs(furthestDist - closestDist) <= 1){
				furthestDist++;
				closestDist--;
			}

			ArrayList<Integer> existingStacks = new ArrayList<>();

			for (int i : candidates.toArray(new Integer[0])){
				int dist = Dungeon.level.distance(i, Dungeon.hero.pos);
				if (dist <= closestDist || dist >= furthestDist){
					candidates.remove((Integer)i);
				} else {
					if (Dungeon.level.heaps.get(i) != null){
						existingStacks.add(i);
					}
				}
			}

			//always place thrown weapons onto each other if possible, but only in fire form
			if (preferGrouping && !existingStacks.isEmpty()) {
				candidates = existingStacks;
			}

			while (target.buff(PinCushion.class) != null) {
				Item item = target.buff(PinCushion.class).grabOne();

				Dungeon.level.drop(item, Random.element(candidates)).sprite.drop(target.pos);
			}
			detach();
			return true;
		}
	}

	/***************************
	 *** Fire Form Abilities ***
	 **************************/

	public void setupFireBall( Char enemy ){
		//aim at direction enemy is moving
		if (Dungeon.level.adjacent(enemy.pos, lastEnemyPos)){
			spTargetCell = enemy.pos + (enemy.pos - lastEnemyPos);
		} else {
			//random otherwise
			spTargetCell = enemy.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		}

		//if there's a firewall, fiddle with fireball position so that it cannot be inside a firewall cell
		FireWall wall = buff(FireWall.class);
		if (wall != null){
			int ofs = 0;
			boolean valid;
			do {
				valid = true;
				for (int i : wall.cells){
					if (i == spTargetCell+ofs){
						do {
							ofs = PathFinder.NEIGHBOURS8[Random.Int(4)];
						} while (Actor.findChar(ofs) != null);
						valid = false;
						break;
					}
				}
			} while (!valid);
			spTargetCell += ofs;
		}

		for (int i : PathFinder.NEIGHBOURS9){
			if (!Dungeon.level.solid[spTargetCell+i]) {
				GameScene.targetedCell(spTargetCell + i, cooldown());
			}
		}
	}

	public void doFireBall( int cell ){

		Sample.INSTANCE.play(Assets.Sounds.BURNING);
		for (int i : PathFinder.NEIGHBOURS9){
			if (Dungeon.level.solid[cell+i]) {
				continue;
			}

			CellEmitter.get(cell+i).burst(FlameParticle.FACTORY, 10);
			GameScene.add(Blob.seed(cell+i, 2, Fire.class));

			Char ch = Actor.findChar(cell+i);
			if (ch != null && !(ch instanceof VaultBossElemental)){
				//at this depth fire deals ~32 damage, already loads
				Buff.affect(ch, Burning.class).reignite(ch);
				if (ch == Dungeon.hero){
					Statistics.questScores[3] -= 100;
				}
			}
		}

	}

	public void setupFireWall(){
		FireWall wall = Buff.append(this, FireWall.class);
		wall.cells = new int[10];
		Room r = ((RegularLevel)Dungeon.level).room(pos);
		Point c = r.center();
		int i = 0;

		ArrayList<Integer> wallDistances = new ArrayList<>();

		//0,1,2,3 for left,top,right,bottom
		Point heroPos = Dungeon.level.cellToPoint(Dungeon.hero.pos);
		wallDistances.add(0, heroPos.x - (c.x-6));
		wallDistances.add(1, heroPos.y - (c.y-6));
		wallDistances.add(2, (c.x+6) - heroPos.x);
		wallDistances.add(3, (c.y+6) - heroPos.y);

		ArrayList<Integer> sortedDistances = (ArrayList<Integer>) wallDistances.clone();
		Collections.shuffle(sortedDistances);
		Collections.sort(sortedDistances);

		int wallFrom = 0;
		int minSkipDist, maxSkipDist;

		//always pick the furthest wall when above half HP
		if (HP > HT/2){
			minSkipDist = 1;
			maxSkipDist = 3;
			do {
				wallFrom = Random.Int(4);
			} while (wallDistances.get(wallFrom) < sortedDistances.get(1));
		//otherwise always pick between 2nd and 3rd furthest
		} else {
			minSkipDist = 4;
			maxSkipDist = 6;
			//in the specific cases of 2x2 walls being equidistant (or all 4 walls equidistant) just pick a random wall
			if (sortedDistances.get(0).equals(sortedDistances.get(1))
					&& sortedDistances.get(2).equals(sortedDistances.get(3))){
				wallFrom = Random.Int(4);
			} else{
				do {
					wallFrom = Random.Int(4);
				} while (wallDistances.get(wallFrom).equals(sortedDistances.get(0))
						|| wallDistances.get(wallFrom).equals(sortedDistances.get(3)));
			}
		}

		if (wallFrom == 1 || wallFrom == 3){
			int y;
			if (wallFrom == 1){
				y = c.y - 6;
				wall.direction = Dungeon.level.width();
			} else {
				y = c.y + 6;
				wall.direction = -Dungeon.level.width();
			}
			int skip;
			do {
				skip = Random.IntRange(c.x-5, c.x+5);
			} while (Math.abs(skip - heroPos.x) > maxSkipDist || Math.abs(skip - heroPos.x) < minSkipDist);
			for (int x = c.x-5; x <= c.x+5; x++){
				if (x == skip) continue;
				wall.cells[i] = x + (y*Dungeon.level.width());
				i++;
			}
		} else {
			int x;
			if (wallFrom == 0){
				x = c.x - 6;
				wall.direction = 1;
			} else {
				x = c.x + 6;
				wall.direction = -1;
			}
			int skip;
			do {
				skip = Random.IntRange(c.y-5, c.y+5);
			} while (Math.abs(skip - heroPos.y) > maxSkipDist || Math.abs(skip - heroPos.y) < minSkipDist);
			for (int y = c.y-5; y <= c.y+5; y++){
				if (y == skip) continue;
				wall.cells[i] = x + (y*Dungeon.level.width());
				i++;
			}
		}
	}

	public static class FireWall extends Buff {

		private int[] cells = new int[0];
		private int direction;

		private int left = 11; //always the same amount

		private ArrayList<Emitter> emitters = new ArrayList<>();

		@Override
		public boolean act() {

			for (int i = 0; i < cells.length; i++){

				for (int j = 0; j < 2; j++) {
					if (Dungeon.level.insideMap(cells[i]+j*direction) && !Dungeon.level.solid[cells[i]+j*direction]) {
						CellEmitter.get(cells[i]+j*direction).burst(FlameParticle.FACTORY, 10);
						Char ch = Actor.findChar(cells[i]+j*direction);
						if (ch != null && !(ch instanceof VaultBossElemental)){
							Buff.affect(ch, Burning.class).reignite(ch, 5); //~20 effective damage
							if (ch == Dungeon.hero){
								Sample.INSTANCE.play(Assets.Sounds.BURNING);
								Statistics.questScores[3] -= 100;
							}
						}
					}
				}

				cells[i] += direction;
			}

			Sample.INSTANCE.play(Assets.Sounds.BURNING, 0.5f);

			if (left-- <= 0){
				detach();
			} else {
				updateFX();
			}

			spend( TICK );
			return true;
		}

		private void updateFX(){
			for (Emitter e : emitters){
				e.on = false;
			}
			emitters.clear();

			for (int cell : cells) {
				boolean oneOpen = false;
				for (int j = 0; j < 2; j++) {
					if (Dungeon.level.insideMap(cell + j * direction) && !Dungeon.level.solid[cell + j * direction]) {
						Emitter pour = CellEmitter.get(cell + j * direction);
						pour.pour(FlameParticle.FACTORY, 0.1f);

						emitters.add(pour);
						oneOpen = true;
					}
				}

				if (!oneOpen) {
					//do a tiny flame further ahead to show player the entire wall pattern
					for (int j = 1; j <= left; j++) {
						if (Dungeon.level.insideMap(cell + j * direction) && !Dungeon.level.solid[cell + direction * j]) {
							Emitter pour = CellEmitter.center(cell + direction * j);
							pour.pour(FlameParticle.FACTORY, 0.5f);
							emitters.add(pour);
							break;
						}
					}
				}
			}

		}

		@Override
		public void fx(boolean on) {
			if (on) {
				updateFX();
			} else {
				for (Emitter e : emitters){
					e.on = false;
				}
				emitters.clear();
			}
		}

		private static String CELLS = "cells";
		private static String DIRECTION = "direction";
		private static String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(CELLS, cells);
			bundle.put(DIRECTION, direction);
			bundle.put(LEFT, left);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			cells = bundle.getIntArray( CELLS );
			direction = bundle.getInt( DIRECTION );
			left = bundle.getInt(LEFT);
		}
	}

	/****************************
	 *** Frost Form Abilities ***
	 ***************************/

	public void setupFrostCone( Char enemy ){

		spTargetCell = enemy.pos;
		Ballistica core = new Ballistica(pos, enemy.pos, Ballistica.WONT_STOP);

		ConeAOE cone = new ConeAOE(core, 10, 50, Ballistica.STOP_SOLID);
		for (int cell : cone.cells){
			if (Dungeon.level.trueDistance(cell, pos) <= 2){
				GameScene.targetedCell(cell, cooldown());
			}
		}

	}

	public void doFrostCone( int cell ){

		FrostCone cone = Buff.append(this, FrostCone.class);

		Ballistica core = new Ballistica(pos, cell, Ballistica.WONT_STOP);
		cone.cells = new ConeAOE(core, 10, 50, Ballistica.STOP_SOLID).cells;
		cone.startPos = pos;

	}

	//tracker buff to ensure that hero gets a chance to act after freezing
	public static class FrostResist extends Buff{

		{
			actPriority = Actor.BUFF_PRIO-1; //after other buffs
		}

		@Override
		public boolean act() {
			if (target.buff(Frost.class) != null){
				spend(target.cooldown());
				return true;
			} else {
				detach();
				return true;
			}
		}
	}

	public static class FrostCone extends Buff {

		private int startPos;
		private HashSet<Integer> cells = new HashSet<>();
		private int distance = 2;

		private HashSet<Emitter> emitters = new HashSet<>();

		@Override
		public boolean act() {

			for (int cell : cells.toArray(new Integer[0])){
				if (Dungeon.level.trueDistance(cell, startPos) <= distance){
					CellEmitter.get(cell).burst(MagicMissile.WhiteParticle.FACTORY, 10);
					Char ch = Actor.findChar(cell);
					if (ch != null && !(ch instanceof VaultBossElemental) && ch.buff(FrostResist.class) == null){
						ch.damage(Random.NormalIntRange(10, 15), new Frost());
						Buff.affect(ch, Frost.class, 5f);
						Buff.affect(ch, FrostResist.class);
						if (ch == Dungeon.hero){
							Statistics.questScores[3] -= 100;
							Sample.INSTANCE.play(Assets.Sounds.SHATTER);
							if (!ch.isAlive()){
								Badges.validateDeathFromEnemyMagic();
								Dungeon.fail(target);
							}
						}
					}
					if (Dungeon.level.trueDistance(cell, startPos) <= distance-4){
						cells.remove(cell);
					}
				}
			}

			updateFX();
			distance += 2;

			if (cells.isEmpty()){
				detach();
			} else {
				spend(TICK);
				for (int cell : cells){
					if (Dungeon.level.trueDistance(cell, startPos) <= distance
						&& Dungeon.level.trueDistance(cell, startPos) > distance-2){
						GameScene.targetedCell(cell, cooldown());
					}
				}
			}

			return true;
		}

		private void updateFX(){
			for (Emitter e : emitters){
				e.on = false;
			}
			emitters.clear();

			for (int cell : cells) {
				if (Dungeon.level.trueDistance(cell, startPos) <= distance){
						Emitter e = CellEmitter.get(cell);
						e.pour(SnowParticle.FACTORY, 0.1f);
						emitters.add(e);
					}
				}
			}

		@Override
		public void fx(boolean on) {
			if (on) {
				updateFX();
			} else {
				for (Emitter e : emitters){
					e.on = false;
				}
				emitters.clear();
			}
		}

		public static final String START_POS = "start_pos";
		public static final String CELLS = "cells";
		public static final String DISTANCE = "distance";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(START_POS, startPos);

			int[] bundleCells = new int[cells.size()];
			int i = 0;
			for (int cell : cells){
				bundleCells[i] = cell;
				i++;
			}
			bundle.put(CELLS, bundleCells);
			bundle.put(DISTANCE, distance);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			startPos = bundle.getInt(START_POS);
			for (int cell : bundle.getIntArray(CELLS)){
				cells.add(cell);
			}
			distance = bundle.getInt(DISTANCE);
		}

	}

	public void setupFrostVortex(){
		FrostVortex vortex = Buff.append(this, FrostVortex.class);
		vortex.targetCell = Dungeon.hero.pos;
	}

	public static class FrostVortex extends Buff {

		private int targetCell = -1;
		private int distance = -1000;

		private boolean fullVortex = false;
		private boolean altDirection = false;

		private ArrayList<Emitter> emitters = new ArrayList<>();

		@Override
		public boolean act() {
			if (distance == -1000){
				int furthestDist = 0;
				boolean valid = true;
				int dist = getFurthestValid(targetCell, -1, -1);
				if (dist > furthestDist) furthestDist = dist;
				dist = getFurthestValid(targetCell, -1, 1);
				if (dist > furthestDist) furthestDist = dist;
				dist = getFurthestValid(targetCell, 1, 1);
				if (dist > furthestDist) furthestDist = dist;
				dist = getFurthestValid(targetCell, 1, -1);
				if (dist > furthestDist) furthestDist = dist;
				distance = Math.min(furthestDist, 5); //won't it always be 4 now?

				fullVortex = target.HP <= target.HT/2;
				altDirection = Random.Int(2) == 0;
			}

			HashSet<Integer> cells = new HashSet<>();
			if (fullVortex) {
				cells.addAll(getCells(targetCell, distance, -1, -1));
				cells.addAll(getCells(targetCell, distance, -1, 1));
				cells.addAll(getCells(targetCell, distance, 1, 1));
				cells.addAll(getCells(targetCell, distance, 1, -1));
			} else {
				if (altDirection) {
					cells.addAll(getCells(targetCell, distance, -1, -1));
					cells.addAll(getCells(targetCell, distance, 1, 1));
				} else {
					cells.addAll(getCells(targetCell, distance, -1, 1));
					cells.addAll(getCells(targetCell, distance, 1, -1));
				}
			}

			distance--;

			updateFX();

			if (cells.isEmpty()){
				detach();
				return true;
			} else {
				for (Integer cell : cells){
					CellEmitter.get(cell).burst(MagicMissile.WhiteParticle.FACTORY, 10);
					Char ch = Actor.findChar(cell);
					if (ch != null && !(ch instanceof VaultBossElemental) && ch.buff(FrostResist.class) == null){
						Buff.affect(ch, Frost.class, 5f);
						Buff.affect(ch, FrostResist.class);
						if (ch == Dungeon.hero){
							Sample.INSTANCE.play(Assets.Sounds.SHATTER);
							Statistics.questScores[3] -= 100;
						}
					}
				}
				Sample.INSTANCE.play(Assets.Sounds.GAS, 0.25f);
				spend(TICK);
				return true;
			}
		}

		private void updateFX(){
			for (Emitter e : emitters){
				e.on = false;
			}
			emitters.clear();

			if (targetCell != -1) {
				HashSet<Integer> cells = new HashSet<>();
				if (fullVortex) {
					cells.addAll(getCells(targetCell, distance, -1, -1));
					cells.addAll(getCells(targetCell, distance, -1, 1));
					cells.addAll(getCells(targetCell, distance, 1, 1));
					cells.addAll(getCells(targetCell, distance, 1, -1));
				} else {
					if (altDirection) {
						cells.addAll(getCells(targetCell, distance, -1, -1));
						cells.addAll(getCells(targetCell, distance, 1, 1));
					} else {
						cells.addAll(getCells(targetCell, distance, -1, 1));
						cells.addAll(getCells(targetCell, distance, 1, -1));
					}
				}

				for (Integer cell : cells) {
					Emitter pour = CellEmitter.get(cell);
					pour.pour(SnowParticle.FACTORY, 0.1f);
					emitters.add(pour);
				}
			}
		}

		@Override
		public void fx(boolean on) {
			if (on) {
				updateFX();
			} else {
				for (Emitter e : emitters){
					e.on = false;
				}
				emitters.clear();
			}
		}

		private int getFurthestValid(int start, int dirX, int dirY){
			int dist = 0;
			if (Dungeon.level.solid[start]) return dist;
			do {
				if (dist % 2 == 0){
					start += dirX;
				} else {
					start += dirY*Dungeon.level.width();
				}
				dist++;
			} while (Dungeon.level.insideMap(start) && !Dungeon.level.solid[start]);
			return dist;
		}

		private HashSet<Integer> getCells(int start, int dist, int dirX, int dirY){
			dist = Math.abs(dist);
			if (getFurthestValid(start, dirX, dirY) < dist){
				return new HashSet<>();
			}
			int xOfs = (dirX*(dist+1)/2);
			int yOfs = (dirY*dist/2)*Dungeon.level.width();
			int initialOfsCell = start + xOfs + yOfs;
			HashSet<Integer> cells = new HashSet<>();
			int cell = initialOfsCell;
			do {
				cells.add(cell);
				cell += -dirX + dirY*Dungeon.level.width();
			} while (Dungeon.level.insideMap(cell) && !Dungeon.level.solid[cell]);
			cell = initialOfsCell;
			do {
				cells.add(cell);
				cell += +dirX - dirY*Dungeon.level.width();
			} while (Dungeon.level.insideMap(cell) && !Dungeon.level.solid[cell]);

			return cells;
		}

		private static String TARGET_CELL = "target_cell";
		private static String DISTANCE = "distance";

		private static String FULL_VORTEX = "full_fortex";
		private static String ALT_DIR = "alt_direction";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TARGET_CELL, targetCell);
			bundle.put(DISTANCE, distance);
			bundle.put(FULL_VORTEX, fullVortex);
			bundle.put(ALT_DIR, altDirection);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			targetCell = bundle.getInt(TARGET_CELL);
			distance = bundle.getInt(DISTANCE);
			fullVortex = bundle.getBoolean(FULL_VORTEX);
			altDirection = bundle.getBoolean(ALT_DIR);
		}

	}

	/****************************
	 *** Shock Form Abilities ***
	 ***************************/

	private int lightningOfs;

	public void setupLightningBolt(Char enemy){

		spTargetCell = enemy.pos;
		lightningOfs = Random.Int(2);

		Ballistica bolt = new Ballistica(pos, spTargetCell, Ballistica.STOP_SOLID);
		for (int cell : bolt.subPath(0, bolt.dist)){
			GameScene.targetedCell(cell, cooldown());
		}

		for (int i = lightningOfs % 2; i < PathFinder.CIRCLE8.length; i+=2){
			GameScene.targetedCell(spTargetCell + PathFinder.CIRCLE8[i], cooldown());
		}

	}

	public void doLightningBolt(int cell){

		HashSet<Integer> affectedCells = new HashSet<>();

		Ballistica bolt = new Ballistica(pos, cell, Ballistica.STOP_SOLID);
		for (int c : bolt.subPath(0, bolt.dist)){
			CellEmitter.get(c).burst(SparkParticle.FACTORY, 5);
			affectedCells.add(c);
		}

		for (int i = lightningOfs % 2; i < PathFinder.CIRCLE8.length; i+=2){
			CellEmitter.get(cell + PathFinder.CIRCLE8[i]).burst(SparkParticle.FACTORY, 5);
			affectedCells.add(cell + PathFinder.CIRCLE8[i]);
		}

		for (int c : affectedCells){
			Char ch = Actor.findChar(c);
			if (ch != null && !(ch instanceof VaultBossElemental) && ch.buff(ShockResist.class) == null){
				ch.damage(Random.NormalIntRange(20, 30), new Electricity());
				Buff.prolong(ch, Paralysis.class, 1f);
				Buff.affect(ch, ShockResist.class);
				ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				ch.sprite.flash();
				if (ch == Dungeon.hero){
					Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
					PixelScene.shake( 2, 0.3f );
					Statistics.questScores[3] -= 100;
					if (!ch.isAlive()){
						Badges.validateDeathFromEnemyMagic();
						Dungeon.fail(this);
					}
				}
			}
		}

		sprite.parent.add(new Lightning(cell + PathFinder.CIRCLE8[lightningOfs],
				cell + PathFinder.CIRCLE8[lightningOfs+4], null));
		sprite.parent.add(new Lightning(cell + PathFinder.CIRCLE8[lightningOfs+2],
				cell + PathFinder.CIRCLE8[lightningOfs+6], null));

	}

	//tracker buff to ensure that hero gets a chance to act after freezing
	public static class ShockResist extends Buff {

		{
			actPriority = Actor.BUFF_PRIO-1; //after other buffs
		}

		@Override
		public boolean act() {
			if (target.buff(Paralysis.class) != null){
				spend(target.cooldown());
				return true;
			} else {
				detach();
				return true;
			}
		}
	}

	//TODO these aren't working great atm, perhaps it's better to use more but always go straight?
	public void setupLightningChase(){
		Room r = ((RegularLevel)Dungeon.level).room(pos);
		Point c = r.center();

		boolean alt = Random.Int(2) == 0;

		if (alt || HP <= HT/2) {
			LightningChase chase = Buff.append(this, LightningChase.class);
			chase.curCell = c.x + (c.y - 5) * Dungeon.level.width();

			chase = Buff.append(this, LightningChase.class);
			chase.curCell = c.x + (c.y + 5) * Dungeon.level.width();
		}

		if (!alt || HP <= HT/2) {

			LightningChase chase = Buff.append(this, LightningChase.class);
			chase.curCell = c.x - 5 + (c.y) * Dungeon.level.width();

			chase = Buff.append(this, LightningChase.class);
			chase.curCell = c.x + 5 + (c.y) * Dungeon.level.width();

		}
	}

	public static class LightningChase extends Buff {

		private static float lastSFXTime = -1;

		float direction = -1;

		int curCell = -1;
		int midCell = -1;
		int endCell = -1;

		ArrayList<Emitter> emitters = new ArrayList<>();

		@Override
		public boolean act() {

			PointF curPos;

			if (direction == -1){
				curPos = new PointF(Dungeon.level.cellToPoint(curCell));
				curPos.x += 0.5f;
				curPos.y += 0.5f;

				direction = PointF.angle(curPos, new PointF(Dungeon.level.cellToPoint(Dungeon.hero.pos)));

				//just started, so do initial bolt visuals

				int curCell = Dungeon.level.pointToCell(curPos.floor());
				CellEmitter.get(curCell).burst(SparkParticle.FACTORY, 10);

			} else {

				CellEmitter.get(curCell).burst(SparkParticle.FACTORY, 10);
				Char ch = Actor.findChar(curCell);
				if (ch != null && !(ch instanceof VaultBossElemental) && ch.buff(ShockResist.class) == null){
					shockChar(ch);
				}
				if ( Dungeon.level.insideMap(midCell) && !Dungeon.level.solid[midCell]) {
					CellEmitter.get(midCell).burst(SparkParticle.FACTORY, 10);
					ch = Actor.findChar(midCell);
					if (ch != null && !(ch instanceof VaultBossElemental) && ch.buff(ShockResist.class) == null){
						shockChar(ch);
					}
					if (Dungeon.level.insideMap(endCell) && !Dungeon.level.solid[endCell]) {
						CellEmitter.get(endCell).burst(SparkParticle.FACTORY, 10);
						ch = Actor.findChar(endCell);
						if (ch != null && !(ch instanceof VaultBossElemental) && ch.buff(ShockResist.class) == null){
							shockChar(ch);
						}
					} else {
						detach();
						return true;
					}
				} else {
					detach();
					return true;
				}

				curPos = new PointF(Dungeon.level.cellToPoint(endCell));
				curPos.x += 0.5f;
				curPos.y += 0.5f;

				float targetAngle = PointF.angle(curPos, new PointF(Dungeon.level.cellToPoint(Dungeon.hero.pos)));

				if (Math.abs(direction - targetAngle) > PointF.PI){
					if (direction > targetAngle){
						targetAngle += PointF.PI2;
					} else {
						targetAngle -= PointF.PI2;
					}
				}

				float maxMove = Random.Float(PointF.PI/8, PointF.PI/6);
				if (direction > targetAngle){
					direction -= Math.min(direction - targetAngle, maxMove);
				} else {
					direction += Math.min(targetAngle - direction, maxMove);
				}

				if (direction > PointF.PI) {
					direction -= PointF.PI2;
				} else if (direction < -PointF.PI){
					direction += PointF.PI2;
				}

			}

			PointF endPos = new PointF(curPos);
			endPos.offset(new PointF(curPos).polar(direction, 2));
			//always snap to the middle of the cell
			endPos.x = Math.round(2 * endPos.x) / 2f;
			endPos.y = Math.round(2 * endPos.y) / 2f;

			PointF midPos = PointF.inter(curPos, endPos, 0.5f);

			endCell = Dungeon.level.pointToCell(endPos.floor());
			midCell = Dungeon.level.pointToCell(midPos.floor());
			curCell = Dungeon.level.pointToCell(curPos.floor());

			//prevents many instances from all making their sfx at once
			if (Actor.now() > lastSFXTime) {
				Sample.INSTANCE.play(Assets.Sounds.LIGHTNING, 0.5f);
				lastSFXTime = Actor.now();
			}

			updateFX();

			spend(TICK);
			return true;
		}

		private void shockChar(Char ch){
			ch.damage(Random.NormalIntRange(10, 15), new Electricity());
			Buff.prolong(ch, Paralysis.class, 1f);
			Buff.affect(ch, ShockResist.class);
			ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			ch.sprite.flash();
			if (ch == Dungeon.hero){
				Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
				PixelScene.shake( 2, 0.3f );
				Statistics.questScores[3] -= 100;
				if (!ch.isAlive()){
					Badges.validateDeathFromEnemyMagic();
					Dungeon.fail(target);
				}
			}
		}

		private void updateFX(){
			for (Emitter e : emitters){
				e.on = false;
			}
			emitters.clear();

			if (endCell != -1 && Dungeon.level.insideMap(endCell) && !Dungeon.level.solid[endCell]){
				Emitter e = CellEmitter.get(endCell);
				e.pour(SparkParticle.STATIC, 0.1f);
				emitters.add(e);
			}

			if (midCell != -1 && Dungeon.level.insideMap(midCell) && !Dungeon.level.solid[midCell]){
				Emitter e = CellEmitter.get(midCell);
				e.pour(SparkParticle.STATIC, 0.1f);
				emitters.add(e);
			}

			if (curCell != -1 && Dungeon.level.insideMap(curCell) && !Dungeon.level.solid[curCell]){
				Emitter e = CellEmitter.get(curCell);
				e.pour(SparkParticle.STATIC, 0.1f);
				emitters.add(e);
			}

		}

		@Override
		public void fx(boolean on) {
			if (on){
				updateFX();
			} else {
				for (Emitter e : emitters){
					e.on = false;
				}
				emitters.clear();
			}
		}

		private static String DIRECTION = "direction";
		private static String CUR_CELL = "cur_cell";
		private static String MID_CELL = "mid_cell";
		private static String END_CELL = "end_cell";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(DIRECTION, direction);
			bundle.put(CUR_CELL, curCell);
			bundle.put(MID_CELL, midCell);
			bundle.put(END_CELL, endCell);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			direction = bundle.getInt(DIRECTION);
			curCell = bundle.getInt(CUR_CELL);
			midCell = bundle.getInt(MID_CELL);
			endCell = bundle.getInt(END_CELL);
			lastSFXTime = -1;
		}
	}

}
