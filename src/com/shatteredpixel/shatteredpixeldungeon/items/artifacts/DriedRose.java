/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class DriedRose extends Artifact {

	{
		name = "Dried Rose";
		image = ItemSpriteSheet.ARTIFACT_ROSE1;

		level = 0;
		levelCap = 10;

		charge = 100;
		chargeCap = 100;

		defaultAction = AC_SUMMON;
	}

	protected static boolean talkedTo = false;
	protected static boolean firstSummon = false;
	protected static boolean spawned = false;

	public int droppedPetals = 0;

	public static final String AC_SUMMON = "SUMMON";

	public DriedRose(){
		super();
		talkedTo = firstSummon = spawned = false;
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge == chargeCap && !cursed)
			actions.add(AC_SUMMON);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals(AC_SUMMON)) {

			if (spawned)                    GLog.n("sad ghost: \"I'm already here\"");
			else if (!isEquipped( hero ))   GLog.i("You need to equip your rose to do that.");
			else if (charge != chargeCap)   GLog.i("Your rose isn't fully charged yet.");
			else if (cursed)                GLog.i("You cannot use a cursed rose.");
			else {
				ArrayList<Integer> spawnPoints = new ArrayList<Integer>();
				for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
					int p = hero.pos + Level.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					GhostHero ghost = new GhostHero( level );
					ghost.pos = Random.element(spawnPoints);

					GameScene.add(ghost, 1f);
					CellEmitter.get(ghost.pos).start( ShaftParticle.FACTORY, 0.3f, 4 );
					CellEmitter.get(ghost.pos).start( Speck.factory(Speck.LIGHT), 0.2f, 3 );

					hero.spend(1f);
					hero.busy();
					hero.sprite.operate(hero.pos);

					if (!firstSummon) {
						ghost.yell(ghost.VOICE_HELLO + Dungeon.hero.givenName());
						Sample.INSTANCE.play( Assets.SND_GHOST );
						firstSummon = true;
					} else
						ghost.saySpawned();

					spawned = true;
					charge = 0;
					updateQuickslot();

				} else
					GLog.i("There is no free space near you.");
			}

		} else{
			super.execute(hero, action);
		}
	}

	@Override
	public String desc() {
		String desc =
				"Is this the rose that the ghost mentioned before disappearing? It seems to hold some spiritual power,"+
				" perhaps it can be used to channel the energy of that lost warrior.";

		if (isEquipped( Dungeon.hero )){
			if (!cursed){
				desc += "\n\nThe rose rests in your hand, it feels strangely warm.";

				if (level < 5)
					desc+= "\n\nThe rose has lost most of its petals. It feels extremely frail, like it " +
							"could snap any moment.";
				else if (level < 10)
					desc+= "\n\nYou have reattached many petals and the rose has started to somehow come back to life."+
							" It almost looks like it's ready to bloom.";
				else
					desc+= "\n\nThe rose has blossomed again through some kind of magic, its connection to your spirit"+
							" friend is stronger than ever.";
			} else
				desc += "\n\nThe cursed rose is bound to you hand, it feels eerily cold.";
		}

		return desc;
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new roseRecharge();
	}

	@Override
	public Item upgrade() {
		if (level >= 9)
			image = ItemSpriteSheet.ARTIFACT_ROSE3;
		else if (level >= 4)
			image = ItemSpriteSheet.ARTIFACT_ROSE2;

		//For upgrade transferring via well of transmutation
		droppedPetals = Math.max( level, droppedPetals );

		return super.upgrade();
	}

	private static final String TALKEDTO =      "talkedto";
	private static final String FIRSTSUMMON =   "firstsummon";
	private static final String SPAWNED =       "spawned";
	private static final String PETALS =        "petals";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);

		bundle.put( TALKEDTO, talkedTo );
		bundle.put( FIRSTSUMMON, firstSummon );
		bundle.put( SPAWNED, spawned );
		bundle.put( PETALS, droppedPetals );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);

		talkedTo = bundle.getBoolean( TALKEDTO );
		firstSummon = bundle.getBoolean( FIRSTSUMMON );
		spawned = bundle.getBoolean( SPAWNED );
		droppedPetals = bundle.getInt( PETALS );
	}

	public class roseRecharge extends ArtifactBuff {

		@Override
		public boolean act() {

			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
				//TODO: investigate balancing on this.
				partialCharge += 10/75f;
				if (partialCharge > 1){
					charge++;
					partialCharge--;
					if (charge == chargeCap){
						partialCharge = 0f;
						GLog.p("Your rose is fully charged!");
					}
				}
			} else if (cursed && Random.Int(100) == 0) {

				ArrayList<Integer> spawnPoints = new ArrayList<Integer>();

				for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
					int p = target.pos + Level.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					Wraith.spawnAt(Random.element(spawnPoints));
					Sample.INSTANCE.play(Assets.SND_CURSED);
				}

			}

			updateQuickslot();

			spend( TICK );

			return true;
		}
	}

	public static class Petal extends Item {

		{
			name = "dried petal";
			stackable = true;
			image = ItemSpriteSheet.PETAL;
		}

		@Override
		public boolean doPickUp( Hero hero ) {
			DriedRose rose = hero.belongings.getItem( DriedRose.class );

			if (rose == null){
				GLog.w("You have no rose to add this petal to.");
				return false;
			} if ( rose.level >= rose.levelCap ){
				GLog.i("There is no room left for this petal, so you discard it.");
				return true;
			} else {

				rose.upgrade();
				if (rose.level == rose.levelCap) {
					GLog.p("The rose is completed!");
					Sample.INSTANCE.play( Assets.SND_GHOST );
					GLog.n("sad ghost: \"Thank you...\"");
				} else
					GLog.i("You add the petal to the rose.");

				Sample.INSTANCE.play( Assets.SND_DEWDROP );
				hero.spendAndNext(TIME_TO_PICK_UP);
				return true;

			}
		}

		@Override
		public String info() {
			return "A frail dried up petal, which has somehow survived this far into the dungeon.";
		}

	}

	public static class GhostHero extends NPC {

		{
			name = "sad ghost";
			spriteClass = GhostSprite.class;

			flying = true;

			state = WANDERING;
			enemy = null;

			ally = true;
		}

		public GhostHero() {
			super();

			//double heroes defence skill
			defenseSkill = (Dungeon.hero.lvl+4)*2;
		}

		public GhostHero(int roseLevel){
			this();
			HP = HT = 10+roseLevel*3;
		}

		public void saySpawned(){
			int i = (Dungeon.depth - 1) / 5;
			if (chooseEnemy() == null)
				yell( Random.element( VOICE_AMBIENT[i] ) );
			else
				yell( Random.element( VOICE_ENEMIES[i][ Dungeon.bossLevel() ? 1 : 0 ] ) );
			Sample.INSTANCE.play( Assets.SND_GHOST );
		}

		public void sayAnhk(){
			yell( Random.element( VOICE_BLESSEDANKH ) );
			Sample.INSTANCE.play( Assets.SND_GHOST );
		}

		public void sayDefeated(){
			yell( Random.element( VOICE_DEFEATED[ Dungeon.bossLevel() ? 1 : 0 ] ) );
			Sample.INSTANCE.play( Assets.SND_GHOST );
		}

		public void sayHeroKilled(){
			yell(Random.element(VOICE_HEROKILLED));
			Sample.INSTANCE.play( Assets.SND_GHOST );
		}

		public void sayBossBeaten(){
			yell( Random.element( VOICE_BOSSBEATEN[ Dungeon.depth==25 ? 1 : 0 ] ) );
			Sample.INSTANCE.play( Assets.SND_GHOST );
		}

		@Override
		public String defenseVerb() {
			return "evaded";
		}

		@Override
		protected boolean act() {
			if (Random.Int(10) == 0) damage(1 , this);
			if (!isAlive())
				return true;
			if (!Dungeon.hero.isAlive()){
				sayHeroKilled();
				sprite.die();
				destroy();
				return true;
			}
			return super.act();
		}

		@Override
		protected boolean getCloser( int target ) {
			if (state == WANDERING || Level.distance(target, Dungeon.hero.pos) > 6)
				this.target = target = Dungeon.hero.pos;
			return super.getCloser( target );
		}

		@Override
		protected Char chooseEnemy() {
			if (enemy == null || !enemy.isAlive() || state == WANDERING) {

				HashSet<Mob> enemies = new HashSet<Mob>();
				for (Mob mob : Dungeon.level.mobs) {
					if (mob.hostile && Level.fieldOfView[mob.pos] && mob.state != mob.PASSIVE) {
						enemies.add(mob);
					}
				}
				enemy = enemies.size() > 0 ? Random.element( enemies ) : null;
			}
			return enemy;
		}

		@Override
		public int attackSkill(Char target) {
			//same accuracy as the hero.
			return (defenseSkill/2)+5;
		}

		@Override
		public int damageRoll() {
			//equivalent to N/2 to 5+N, where N is rose level.
			int lvl = (HT-10)/3;
			return Random.NormalIntRange( lvl/2, 5+lvl);
		}

		@Override
		public int dr() {
			//defence is equal to the level of rose.
			return (HT-10)/3;
		}

		@Override
		public void add( Buff buff ) {
			//in other words, can't be directly affected by buffs/debuffs.
		}

		@Override
		public void interact() {
			if (!DriedRose.talkedTo){
				DriedRose.talkedTo = true;
				GameScene.show(new WndQuest(this, VOICE_INTRODUCE ));
			} else {
				int curPos = pos;

				moveSprite( pos, Dungeon.hero.pos );
				move( Dungeon.hero.pos );

				Dungeon.hero.sprite.move( Dungeon.hero.pos, curPos );
				Dungeon.hero.move( curPos );

				Dungeon.hero.spend( 1 / Dungeon.hero.speed() );
				Dungeon.hero.busy();
			}
		}

		@Override
		public void die(Object cause) {
			sayDefeated();
			super.die(cause);
		}

		@Override
		public void destroy() {
			DriedRose.spawned = false;
			super.destroy();
		}

		@Override
		public String description() {
			return
					"A frail looking ethereal figure with a humanoid shape. Its power seems tied to the rose I have." +
					"\n\nThis ghost may not be much, but it seems to be my only true friend down here.";
		}

		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( ToxicGas.class );
			IMMUNITIES.add( ScrollOfPsionicBlast.class );
		}

		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}

		//************************************************************************************
		//This is a bunch strings & string arrays, used in all of the sad ghost's voice lines.
		//************************************************************************************

		public static final String VOICE_HELLO = "Hello again ";

		private static final String VOICE_INTRODUCE = "My spirit is bound to this rose, it was very precious to me, a "+
			"gift from my love whom I left on the surface.\n\nI cannot return to him, but thanks to you I have a " +
			"second chance to complete my journey. When I am able I will respond to your call and fight with you.\n\n" +
			"hopefully you may succeed where I failed...";

		//1st index - depth type, 2nd index - specific line.
		public static final String[][] VOICE_AMBIENT = {
			{
					"These sewers were once safe, some even lived here in the winter...",
					"I wonder what happened to the guard patrols, did they give up?...",
					"I had family on the surface, I hope they are safe..."
			},{
					"I've heard stories about this place, nothing good...",
					"This place was always more of a dungeon than a prison...",
					"I can't imagine what went on when this place was abandoned..."
			},{
					"No human or dwarf has been here for a very long time...",
					"Something must have gone very wrong, for the dwarves to abandon a gold mine...",
					"I feel great evil lurking below..."
			},{
					"The dwarves were industrious, but greedy...",
					"I hope the surface never ends up like this place...",
					"So the dwarvern metropolis really has fallen..."
			},{
					"What is this place?...",
					"So the stories are true, we have to fight a demon god...",
					"I feel a great evil in this place..."
			},{
					"... I don't like this place... We should leave as soon as possible..."
			}
		};

		//1st index - depth type, 2nd index - boss or not, 3rd index - specific line.
		public static final String[][][] VOICE_ENEMIES = {
			{
				{
					"Let's make the sewers safe again...",
					"If the guards couldn't defeat them, perhaps we can...",
					"These crabs are extremely annoying..."
				},{
					"Beware Goo!...",
					"Many of my friends died to this thing, time for vengeance...",
					"Such an abomination cannot be allowed to live..."
				}
			},{
				{
					"What dark magic happened here?...",
					"To think the captives of this place are now its guardians...",
					"They were criminals before, now they are monsters..."
				},{
					"If only he would see reason, he doesn't seem insane...",
					"He assumes we are hostile, if only he would stop to talk...",
					"The one prisoner left sane is a deadly assassin. Of course..."
				}
			},{
				{
					"The creatures here are twisted, just like the sewers... ",
					"more gnolls, I hate gnolls...",
					"Even the bats are bloodthirsty here..."
				},{
					"Only dwarves would build a mining machine that kills looters...",
					"That thing is huge...",
					"How has it survived here for so long?..."
				}
			},{
				{
					"Dwarves aren't supposed to look that pale...",
					"I don't know what's worse, the dwarves, or their creations...",
					"They all obey their master without question, even now..."
				},{
					"When people say power corrupts, this is what they mean...",
					"He's more a Lich than a King now...",
					"Looks like he's more demon than dwarf now..."
				}
			},{
				{
					"What the heck is that thing?...",
					"This place is terrifying...",
					"What were the dwarves thinking, toying with power like this?..."
				},{
					"Oh.... this doesn't look good...",
					"So that's what a god looks like?...",
					"This is going to hurt..."
				}
			},{
				{
					"I don't like this place... we should leave as soon as we can..."
				},{
					"Hello source viewer, I'm writing this here as this line should never trigger. Have a nice day!"
				}
			}
		};

		//1st index - Yog or not, 2nd index - specific line.
		public static final String[][] VOICE_BOSSBEATEN = {
			{
					"Yes!",
					"Victory!"
			},{
					"It's over... we won...",
					"I can't believe it... We just killed a god..."
			}
		};

		//1st index - boss or not, 2nd index - specific line.
		public static final String[][] VOICE_DEFEATED = {
			{
					"Good luck...",
					"I will return...",
					"Tired... for now..."
			},{
					"No... I can't....",
					"I'm sorry.. good luck..",
					"Finish it off... without me..."
			}
		};

		public static final String[] VOICE_HEROKILLED = {
					"nooo...",
					"no...",
					"I couldn't help them..."
		};

		public static final String[] VOICE_BLESSEDANKH = {
					"Incredible!...",
					"Wish I had one of those...",
					"How did you survive that?..."
		};
	}
}
