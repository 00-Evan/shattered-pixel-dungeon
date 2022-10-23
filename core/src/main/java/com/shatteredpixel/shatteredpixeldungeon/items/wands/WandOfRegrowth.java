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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LotusSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class WandOfRegrowth extends Wand {

	{
		image = ItemSpriteSheet.WAND_REGROWTH;

		//only used for targeting, actual projectile logic is Ballistica.STOP_SOLID
		collisionProperties = Ballistica.WONT_STOP;
	}
	
	private int totChrgUsed = 0;
	private int chargesOverLimit = 0;

	ConeAOE cone;
	int target;

	@Override
	public boolean tryToZap(Hero owner, int target) {
		if (super.tryToZap(owner, target)){
			this.target = target;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onZap(Ballistica bolt) {

		ArrayList<Integer> cells = new ArrayList<>(cone.cells);

		float furrowedChance = 0;
		if (totChrgUsed >= chargeLimit(Dungeon.hero.lvl)){
			furrowedChance = (chargesOverLimit+1)/5f;
		}

		int chrgUsed = chargesPerCast();
		int grassToPlace = Math.round((3.67f+buffedLvl()/3f)*chrgUsed);

		//ignore cells which can't have anything grow in them.
		for (Iterator<Integer> i = cells.iterator(); i.hasNext();) {
			int cell = i.next();
			int terr = Dungeon.level.map[cell];
			if (!(terr == Terrain.EMPTY || terr == Terrain.EMBERS || terr == Terrain.EMPTY_DECO ||
					terr == Terrain.GRASS || terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS)) {
				i.remove();
			} else if (Char.hasProp(Actor.findChar(cell), Char.Property.IMMOVABLE)) {
				i.remove();
			} else if (Dungeon.level.plants.get(cell) != null){
				i.remove();
			} else {
				if (terr != Terrain.HIGH_GRASS && terr != Terrain.FURROWED_GRASS) {
					Level.set(cell, Terrain.GRASS);
					GameScene.updateMap( cell );
				}
				Char ch = Actor.findChar(cell);
				if (ch != null){
					if (ch instanceof DwarfKing){
						Statistics.qualifiedForBossChallengeBadge = false;
					}
					wandProc(ch, chargesPerCast());
					Buff.prolong( ch, Roots.class, 4f * chrgUsed );
				}
			}
		}

		Random.shuffle(cells);

		if (chargesPerCast() >= 3){
			Lotus l = new Lotus();
			l.setLevel(buffedLvl());
			if (cells.contains(target) && Actor.findChar(target) == null){
				cells.remove((Integer)target);
				l.pos = target;
				GameScene.add(l);
			} else {
				for (int i = bolt.path.size()-1; i >= 0; i--){
					int c = bolt.path.get(i);
					if (cells.contains(c) && Actor.findChar(c) == null){
						cells.remove((Integer)c);
						l.pos = c;
						GameScene.add(l);
						break;
					}
				}
			}
		}

		//places grass along center of cone
		for (int cell : bolt.path){
			if (grassToPlace > 0 && cells.contains(cell)){
				if (Random.Float() > furrowedChance) {
					Level.set(cell, Terrain.HIGH_GRASS);
				} else {
					Level.set(cell, Terrain.FURROWED_GRASS);
				}
				GameScene.updateMap( cell );
				grassToPlace--;
				//moves cell to the back
				cells.remove((Integer)cell);
				cells.add(cell);
			}
		}

		if (!cells.isEmpty() && Random.Float() > furrowedChance &&
				(Random.Int(6) < chrgUsed)){ // 16%/33%/50% chance to spawn a seed pod or dewcatcher
			int cell = cells.remove(0);
			Dungeon.level.plant( Random.Int(2) == 0 ? new Seedpod.Seed() : new Dewcatcher.Seed(), cell);
		}

		if (!cells.isEmpty() && Random.Float() > furrowedChance &&
				(Random.Int(3) < chrgUsed)){ // 33%/66%/100% chance to spawn a plant
			int cell = cells.remove(0);
			Dungeon.level.plant((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED), cell);
		}

		for (int cell : cells){
			if (grassToPlace <= 0 || bolt.path.contains(cell)) break;

			if (Dungeon.level.map[cell] == Terrain.HIGH_GRASS) continue;

			if (Random.Float() > furrowedChance) {
				Level.set(cell, Terrain.HIGH_GRASS);
			} else {
				Level.set(cell, Terrain.FURROWED_GRASS);
			}
			GameScene.updateMap( cell );
			grassToPlace--;
		}

		if (totChrgUsed < chargeLimit(Dungeon.hero.lvl)) {
			chargesOverLimit = 0;
			totChrgUsed += chrgUsed;
			if (totChrgUsed > chargeLimit(Dungeon.hero.lvl)){
				chargesOverLimit = totChrgUsed - chargeLimit(Dungeon.hero.lvl);
				totChrgUsed = chargeLimit(Dungeon.hero.lvl);
			}
		} else {
			chargesOverLimit += chrgUsed;
		}

	}
	
	private int chargeLimit( int heroLvl ){
		if (level() >= 10){
			return Integer.MAX_VALUE;
		} else {
			//8 charges at base, plus:
			//2/3.33/5/7/10/14/20/30/50/110/infinite charges per hero level, based on wand level
			float lvl = level();
			return Math.round(8 + heroLvl * (2+lvl) * (1f + (lvl/(10 - lvl))));
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//like pre-nerf vampiric enchantment, except with herbal healing buff, only in grass
		boolean grass = false;
		int terr = Dungeon.level.map[attacker.pos];
		if (terr == Terrain.GRASS || terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS){
			grass = true;
		}
		terr = Dungeon.level.map[defender.pos];
		if (terr == Terrain.GRASS || terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS){
			grass = true;
		}

		if (grass) {
			int level = Math.max(0, staff.buffedLvl());

			// lvl 0 - 16%
			// lvl 1 - 21%
			// lvl 2 - 25%
			int healing = Math.round(damage * (level + 2f) / (level + 6f) / 2f);
			healing = Math.round(healing * procChanceMultiplier(attacker));
			Buff.affect(attacker, Sungrass.Health.class).boost(healing);
		}

	}

	public void fx(Ballistica bolt, Callback callback) {

		// 4/6/8 distance
		int maxDist = 2 + 2*chargesPerCast();
		int dist = Math.min(bolt.dist, maxDist);

		cone = new ConeAOE( bolt,
				maxDist,
				20 + 10*chargesPerCast(),
				Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);

		//cast to cells at the tip, rather than all cells, better performance.
		for (Ballistica ray : cone.outerRays){
			((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
					MagicMissile.FOLIAGE_CONE,
					curUser.sprite,
					ray.path.get(ray.dist),
					null
			);
		}

		//final zap at half distance, for timing of the actual wand effect
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.FOLIAGE_CONE,
				curUser.sprite,
				bolt.path.get(dist/2),
				callback );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	protected int chargesPerCast() {
		if (charger != null && charger.target.buff(WildMagic.WildMagicTracker.class) != null){
			return 1;
		}
		//consumes 30% of current charges, rounded up, with a min of 1 and a max of 3.
		return (int) GameMath.gate(1, (int)Math.ceil(curCharges*0.3f), 3);
	}

	@Override
	public String statsDesc() {
		String desc = Messages.get(this, "stats_desc", chargesPerCast());
		if (isIdentified()){
			int chargeLeft = chargeLimit(Dungeon.hero.lvl) - totChrgUsed;
			if (chargeLeft < 10000) desc += " " + Messages.get(this, "degradation", Math.max(chargeLeft, 0));
		}
		return desc;
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( ColorMath.random(0x004400, 0x88CC44) );
		particle.am = 1f;
		particle.setLifespan(1f);
		particle.setSize( 1f, 1.5f);
		particle.shuffleXY(0.5f);
		float dst = Random.Float(11f);
		particle.x -= dst;
		particle.y += dst;
	}
	
	private static final String TOTAL = "totChrgUsed";
	private static final String OVER = "chargesOverLimit";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( TOTAL, totChrgUsed );
		bundle.put( OVER, chargesOverLimit);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		totChrgUsed = bundle.getInt(TOTAL);
		chargesOverLimit = bundle.getInt(OVER);
	}
	
	public static class Dewcatcher extends Plant{

		{
			image = 13;
		}

		@Override
		public void activate( Char ch ) {

			int nDrops = Random.NormalIntRange(3, 6);

			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				if (Dungeon.level.passable[pos+i]
						&& pos+i != Dungeon.level.entrance()
						&& pos+i != Dungeon.level.exit()){
					candidates.add(pos+i);
				}
			}

			for (int i = 0; i < nDrops && !candidates.isEmpty(); i++){
				Integer c = Random.element(candidates);
				if (Dungeon.level.heaps.get(c) == null) {
					Dungeon.level.drop(new Dewdrop(), c).sprite.drop(pos);
				} else {
					Dungeon.level.drop(new Dewdrop(), c).sprite.drop(c);
				}
				candidates.remove(c);
			}

		}

		//seed is never dropped, only care about plant class
		public static class Seed extends Plant.Seed {
			{
				plantClass = Dewcatcher.class;
			}
		}
	}

	public static class Seedpod extends Plant{

		{
			image = 14;
		}

		@Override
		public void activate( Char ch ) {

			int nSeeds = Random.NormalIntRange(2, 4);

			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				if (Dungeon.level.passable[pos+i]
						&& pos+i != Dungeon.level.entrance()
						&& pos+i != Dungeon.level.exit()){
					candidates.add(pos+i);
				}
			}

			for (int i = 0; i < nSeeds && !candidates.isEmpty(); i++){
				Integer c = Random.element(candidates);
				Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.SEED), c).sprite.drop(pos);
				candidates.remove(c);
			}

		}

		//seed is never dropped, only care about plant class
		public static class Seed extends Plant.Seed {
			{
				plantClass = Seedpod.class;
			}
		}

	}

	public static class Lotus extends NPC {

		{
			alignment = Alignment.NEUTRAL;
			properties.add(Property.IMMOVABLE);

			spriteClass = LotusSprite.class;

			viewDistance = 1;
		}

		private int wandLvl = 0;

		private void setLevel( int lvl ){
			wandLvl = lvl;
			HP = HT = 25 + 3*lvl;
		}

		public boolean inRange(int pos){
			return Dungeon.level.trueDistance(this.pos, pos) <= wandLvl;
		}

		public float seedPreservation(){
			return 0.40f + 0.04f*wandLvl;
		}

		@Override
		public boolean canInteract(Char c) {
			return false;
		}

		@Override
		protected boolean act() {
			super.act();

			if (--HP <= 0){
				destroy();
				sprite.die();
			}

			return true;
		}

		@Override
		public void damage( int dmg, Object src ) {
		}

		@Override
		public void add( Buff buff ) {
		}

		@Override
		public void destroy() {
			super.destroy();
			Dungeon.observe();
			GameScene.updateFog(pos, viewDistance+1);
		}

		@Override
		public boolean isInvulnerable(Class effect) {
			return true;
		}

		{
			immunities.add( Paralysis.class );
			immunities.add( Amok.class );
			immunities.add( Sleep.class );
			immunities.add( Terror.class );
			immunities.add( Dread.class );
			immunities.add( Vertigo.class );
			immunities.add( AllyBuff.class );
			immunities.add( Doom.class );
		}

		@Override
		public String description() {
			int preservation = Math.round(seedPreservation()*100);
			return Messages.get(this, "desc", wandLvl, preservation, preservation);
		}

		private static final String WAND_LVL = "wand_lvl";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(WAND_LVL, wandLvl);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			wandLvl = bundle.getInt(WAND_LVL);
		}
	}

}
