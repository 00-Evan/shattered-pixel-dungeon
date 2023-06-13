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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.potionyst;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Golden;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Scarecrow extends ArmorAbility {

	{
		baseChargeUse = 32f;
	}

	@Override
	public String targetingPrompt() {
		super.targetingPrompt();
		return Messages.get(this, "prompt");
	}

	@Override
	public boolean useTargeting(){
		return false;
	}


	@Override
	public float chargeUse(Hero hero) {

		return super.chargeUse(hero);

	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		ScareCrowAlly ally = getScareCrowAlly();

		if (target != null) {

			PathFinder.buildDistanceMap(hero.pos, BArray.not(Dungeon.level.solid, null), 1);
			Char ch = Actor.findChar(target);

			if (PathFinder.distance[target] == Integer.MAX_VALUE || ch != null) {
				GLog.w(Messages.get(Scarecrow.class, "no_space"));
			} else {
				armor.charge -= chargeUse(hero);
				armor.updateQuickslot();

				ally = new ScareCrowAlly(Dungeon.hero.pointsInTalent(Talent.SCARECROW_HP));
				ally.pos = target;
				GameScene.add(ally);

				ScareCrowAlly.appear(ally, ally.pos);
				ally.allycount++;

				Invisibility.dispel();
				hero.spendAndNext(Actor.TICK);

			}
		}
	}

	@Override
	public int icon() {
		return HeroIcon.SCARECROW;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.SCARECROW_HP, Talent.SCARECROW_FEAR, Talent.SCARECROW_TELE, Talent.HEROIC_ENERGY};
	}

	private static ScareCrowAlly getScareCrowAlly(){
		for (Char ch : Actor.chars()){
			if (ch instanceof ScareCrowAlly){
				return (ScareCrowAlly) ch;
			}
		}
		return null;
	}



	public static class ScareCrowAlly extends NPC {

		{
			spriteClass = ScarecrowSprite.class;

			HP = HT = 42 ;

			immunities.add(AllyBuff.class);
			immunities.add(Golden.class);

			properties.add(Property.IMMOVABLE);
			properties.add(Property.INORGANIC);

			alignment = Alignment.ALLY;
		}

		public int allycount = 5;

		public ScareCrowAlly(){
			super();
		}

		public ScareCrowAlly(int i ){
			super();
			float hpBonus = 12*i;
			if (hpBonus > 0){
				HT += hpBonus;
				HP += hpBonus;
			}
			defenseSkill = i ;
		}

		@Override
		protected boolean act() {

			int oldPos = pos;
			boolean result = super.act();

			if ((pos == target || oldPos == pos) && sprite.looping()){
				sprite.idle();
			}

			allycount--;
			if (allycount <= 0) {
				HP--;
				allycount = 5;
			}

			if (HP <=0) {
				die(null);
			}

			return result;
		}


		@Override
		public int attackSkill(Char target) {
			return defenseSkill+5;
		}


		private boolean jumpscare(int target ) {

			Ballistica route = new Ballistica( pos, target, Ballistica.STOP_TARGET);

			ArrayList<Integer> spawnPoints = new ArrayList<>();

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = route.collisionPos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
					spawnPoints.add(p);
				}
			}
			if (!spawnPoints.isEmpty()) {
				appear(this, Random.element(spawnPoints));
			}else {
					return false;
			}
			//점프 스케어!
			for (int i : PathFinder.NEIGHBOURS9) {
				Char ch = Actor.findChar(pos+i);
				if (ch != null && ch.alignment != Alignment.ALLY && ((Mob)ch).state != ((Mob) ch).SLEEPING) {
					Buff.affect(ch, Terror.class, 2 + Dungeon.hero.pointsInTalent(Talent.SCARECROW_FEAR)).object = this.id();
					if (Dungeon.level.heroFOV[pos]) {
						SpellSprite.show( this, SpellSprite.TERROR);
					}
					if (Dungeon.hero.pointsInTalent(Talent.SCARECROW_FEAR) > 2) {
						Buff.affect(ch, Vertigo.class, Dungeon.hero.pointsInTalent(Talent.SCARECROW_FEAR) -1);
					}
				}
			}

			return true;
		}
		@Override
		protected boolean getFurther(int target) {
			return false;
		}



		@Override
		public boolean isImmune(Class effect) {


			return super.isImmune(effect);
		}

		@Override
		public int defenseProc(Char enemy, int damage) {
			damage = super.defenseProc(enemy, damage);

			damage = jumptoenemy(enemy, damage);

			for (int i : PathFinder.NEIGHBOURS9) {
				Char ch = Actor.findChar(pos+i);
				if (ch != null && ch.alignment != Alignment.ALLY  && ((Mob)ch).state != ((Mob) ch).SLEEPING) {
					Buff.affect(ch, Terror.class, 2 + Dungeon.hero.pointsInTalent(Talent.SCARECROW_FEAR)).object = this.id();
					if (Dungeon.level.heroFOV[pos]) {
						SpellSprite.show( this, SpellSprite.TERROR);
					}
					if (Dungeon.hero.pointsInTalent(Talent.SCARECROW_FEAR) > 2) {
						Buff.affect(ch, Vertigo.class, Dungeon.hero.pointsInTalent(Talent.SCARECROW_FEAR) -1);
					}
				}
			}

			return damage;

		}

		public int jumptoenemy(Char attacker, int damage ) {

			if ( Dungeon.hero.hasTalent(Talent.SCARECROW_TELE)
					&& attacker != null && attacker instanceof Mob
					&& Dungeon.level.distance( pos, attacker.pos ) > 1
					&& Dungeon.level.distance( pos, attacker.pos ) <= 2 * Dungeon.hero.pointsInTalent(Talent.SCARECROW_TELE) ) {

				if (jumpscare( attacker.pos )) {
					spend(-1 / speed());
					sprite.turnTo(pos, attacker.pos);;
				}
			}

			return damage;
		}

		@Override
		public int drRoll() {
			int dr = super.drRoll();
			int herolvl = Dungeon.hero.lvl - 20;
			int drRoll =  Random.NormalIntRange(herolvl, 5*(2+herolvl)); // 판금 갑옷, 영웅레벨-20 수치로 강화됨
			drRoll = Math.round( 0.8f * drRoll );
			if (drRoll > 0){
				dr += drRoll;
			}
			return dr;
		}

		@Override
		public void damage(int dmg, Object src) {

			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob.buff(ScareTracker.class) != null){
					dmg = jumptoenemy(mob,dmg);
					mob.buff(ScareTracker.class).detach();
				}
			}

			if (AntiMagic.RESISTS.contains(src.getClass())){
				dmg -= Random.NormalIntRange(
						Math.round( Dungeon.hero.pointsInTalent(Talent.SCARECROW_HP) ),
						Math.round( 2 + Dungeon.hero.pointsInTalent(Talent.SCARECROW_HP)) );
			}
			super.damage(dmg, src);
		}


		@Override
		public boolean canInteract(Char c) {
			if (super.canInteract(c)){
				return true;
			} else if (Dungeon.level.distance(pos, c.pos) <= 2 * Dungeon.hero.pointsInTalent(Talent.RATFORCEMENTS)) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean interact(Char c) {
			if ( !Dungeon.hero.hasTalent(Talent.RATFORCEMENTS)){
				return super.interact(c);
			}

			if (!Dungeon.level.passable[pos] && !c.flying){
				return true;
			}

			if (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[c.pos]
					|| c.properties().contains(Property.LARGE) && !Dungeon.level.openSpace[pos]){
				return true;
			}

			int curPos = pos;

			PathFinder.buildDistanceMap(c.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
			if (PathFinder.distance[pos] == Integer.MAX_VALUE){
				return true;
			}
			appear(this, Dungeon.hero.pos);
			appear(Dungeon.hero, curPos);
			Dungeon.observe();
			GameScene.updateFog();
			return true;
		}

		private static void appear( Char ch, int pos ) {

			ch.sprite.interruptMotion();

			if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[ch.pos]){
				Sample.INSTANCE.play(Assets.Sounds.BONES);
				Sample.INSTANCE.play(Assets.Sounds.TOMB);
				((ScarecrowSprite)ch.sprite).build();
			}

			ch.move( pos );
			if (ch.pos == pos) {
				ch.sprite.place(pos);
				Sample.INSTANCE.play(Assets.Sounds.BONES);
				Sample.INSTANCE.play(Assets.Sounds.TOMB);
			}


			if (Dungeon.level.heroFOV[pos] || ch == Dungeon.hero ) {
				ch.sprite.emitter().burst(SmokeParticle.FACTORY, 10);
			}
		}

		@Override
		public void die( Object cause ) {

			super.die(cause);
		}

		private static final String DEF_SKILL = "def_skill";
		private static final String ALLY_COUNT = "ally_count";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(DEF_SKILL, defenseSkill);
			bundle.put(ALLY_COUNT, allycount);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			defenseSkill = bundle.getInt(DEF_SKILL);
			allycount = bundle.getInt(ALLY_COUNT);
		}
	}

	public static class ScarecrowSprite extends MobSprite {

		private Emitter smoke;

		private Animation build;

		public ScarecrowSprite() {
			super();

			texture( Assets.Sprites.SCARECROW );

			TextureFilm film = new TextureFilm( texture, 16, 18 );

			idle = new Animation( 5, true );
			idle.frames( film, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0);

			run = new Animation( 5, false );
			run.frames( film,  0 );

			build = new Animation( 20, false );
			build.frames( film, 7, 6, 6, 5, 5, 4, 4 ,3 ,3, 0, 0 );

			die = new Animation( 20, false );
			die.frames( film, 0, 3, 3, 4, 4, 5, 6, 6, 7 ,7 ,7 );

			attack = new Animation( 15, false );
			attack.frames( film, 13, 14, 15, 0 );

			idle();
		}

		public void build(){
			hideEmo();
			play(build);
		}

		@Override
		public void onComplete(Tweener tweener) {
			super.onComplete(tweener);
		}

		@Override
		public void showAlert() {
			//do nothing
		}

		@Override
		public void link( Char ch ) {
			super.link( ch );
			renderShadow = false;

			if (smoke == null) {
				smoke = emitter();
				smoke.pour( CityLevel.RedSmoke.factory, 0.2f );
			}
		}

		@Override
		public void update() {

			super.update();

			if (smoke != null) {
				smoke.visible = visible;
			}
		}

		@Override
		public void kill() {
			super.kill();

			if (smoke != null) {
				smoke.on = false;
			}
		}
	}
}
