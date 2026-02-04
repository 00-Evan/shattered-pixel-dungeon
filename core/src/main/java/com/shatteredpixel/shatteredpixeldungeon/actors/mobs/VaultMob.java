package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;

public class VaultMob extends Mob {

	{
		WANDERING = new Wandering();
		INVESTIGATING = new Investigating();
		SLEEPING = new Sleeping();

		state = SLEEPING;
	}

	//used to track movement direction for wandering detection angles
	private int previousPos = -1;

	@Override
	public void move(int step, boolean travelling) {
		previousPos = pos;
		super.move(step, travelling);
		if (travelling && !sprite.visible && Dungeon.level.distance(pos, Dungeon.hero.pos) <= 6){
			if (state == HUNTING){
				WandOfBlastWave.BlastWave.blast(pos, 1f, 0xFF0000);
			} else if (state == INVESTIGATING){
				WandOfBlastWave.BlastWave.blast(pos, 1f, 0xFF8800);
			} else {
				WandOfBlastWave.BlastWave.blast(pos, 1f);
			}
		}
	}

	private static final String PREV_POS = "prev_pos";
	private static final String INVEST_TURNS = "invest_turns";
	private static final String WANDER_POSITIONS = "wander_positions";
	private static final String WANDER_POS_IDX = "wander_pos_idx";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PREV_POS, previousPos);
		bundle.put(INVEST_TURNS, investigatingTurns);
		if (wanderPositions != null) {
			bundle.put(WANDER_POSITIONS, wanderPositions);
			bundle.put(WANDER_POS_IDX, wanderPosIdx);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		previousPos = bundle.getInt(PREV_POS);
		investigatingTurns = bundle.getInt(INVEST_TURNS);
		if (bundle.contains(WANDER_POSITIONS)) {
			wanderPositions = bundle.getIntArray(WANDER_POSITIONS);
			wanderPosIdx = bundle.getInt(WANDER_POS_IDX);
		}
	}

	//detection chance is lower on the first turn of investigating
	private int investigatingTurns;

	public class Investigating extends Mob.Investigating {

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (enemyInFOV){
				investigatingTurns++;
			} else {
				investigatingTurns = 0;
			}
			return super.act(enemyInFOV, justAlerted);
		}

		@Override
		//TODO balance this
		//chance is 1 in (distance/2 + stealth) as base, but reduced if enemy was only just seen
		protected float detectionChance( Char enemy ){
			float chance = 1 / (distance( enemy ) / 2f + enemy.stealth());
			if (investigatingTurns == 1 && chance <= 1){
				chance -= 0.33f;
			}
			return chance;
		}
	}

	public int wanderPosIdx = 0;
	public int[] wanderPositions;

	public class Wandering extends Mob.Wandering {

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			return super.act(enemyInFOV, justAlerted);
		}

		@Override
		protected float detectionChance( Char enemy ){
			//defaults to 1 in (distance + stealth) (classic sleeping detection)
			if (!Dungeon.level.adjacent(pos, previousPos)){
				return 1 / (distance( enemy ) + enemy.stealth());
			}

			float movementDir = PointF.angle(Dungeon.level.cellToPoint(previousPos), Dungeon.level.cellToPoint(pos))/PointF.G2R;;
			float enemyDir = PointF.angle(Dungeon.level.cellToPoint(pos), Dungeon.level.cellToPoint(enemy.pos))/PointF.G2R;
			//classic wandering detection if enemy is within (or touching) a 90 degree cone of vision
			if (Math.abs(enemyDir - movementDir) <= 45f){
				return 1 / (distance( enemy ) / 2f + enemy.stealth());
			//classic sleeping (i.e. default) detection if enemy is within a 180 degree vision cone
			} else if (Math.abs(enemyDir - movementDir) < 90f){
				return 1 / (distance( enemy ) + enemy.stealth());
			//otherwise uses very low chance detection (1/8 at 2 tiles, 0% at 3+)
			} else {
				float chance = 1 / (float)Math.pow((distance( enemy ) + enemy.stealth()), 3);
				if (chance < 0.1f){
					return 0;
				} else {
					return chance;
				}
			}
		}

		@Override
		protected boolean noticeEnemy() {
			super.noticeEnemy();
			alerted = false;
			state = INVESTIGATING;
			investigatingTurns = 0;
			sprite.showInvestigate();
			spend(TICK);
			//hero must know if they are detected
			if (!Dungeon.level.heroFOV[pos]){
				Buff.affect(Dungeon.hero, TalismanOfForesight.CharAwareness.class, 1f).charID = id();
			}
			return true;
		}

		@Override
		protected int randomDestination() {
			//stay still by default if given no other wandering behaviour
			if (wanderPositions == null){
				wanderPositions = new int[1];
				wanderPositions[0] = pos;
			}

			int wanderPos = wanderPositions[wanderPosIdx];
			if (wanderPos == pos) {
				if (wanderPositions.length > 1) {
					wanderPosIdx++;
					if (wanderPosIdx == wanderPositions.length) {
						wanderPosIdx = 0;
					}
					wanderPos = wanderPositions[wanderPosIdx];
				} else {
					//reset this, representing the mob looking around in place
					previousPos = pos;
					sprite.idle();
				}
			}
			return wanderPos;
		}
	}

	public class Sleeping extends Mob.Sleeping{

		protected void awaken(boolean enemyInFOV) {
			super.awaken(enemyInFOV);
			//stay still by default if given no other wandering behaviour
			if (wanderPositions == null){
				wanderPositions = new int[1];
				wanderPositions[0] = pos;
			}
			if (state == HUNTING){
				alerted = false;
				state = INVESTIGATING;
				investigatingTurns = 0;
				sprite.showInvestigate();
				//hero must know if they are detected
				if (!Dungeon.level.heroFOV[pos]){
					Buff.affect(Dungeon.hero, TalismanOfForesight.CharAwareness.class, 1f).charID = id();
				}
			}
		}

		//chance is 1 in (distance + stealth)^2
		//set to 0 if below 10% (usually happens at 4+ distance)
		@Override
		protected float detectionChance( Char enemy ){
			float chance = 1 / (float)Math.pow((distance( enemy ) + enemy.stealth()), 2);
			if (chance < 0.1f){
				return 0;
			} else {
				return chance;
			}
		}
	}

}
