package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;

public class VaultMob extends Mob {

	{
		WANDERING = new Wandering();
		INVESTIGATING = new Investigating();
		SLEEPING = new Sleeping();

		state = SLEEPING;
	}

	//used to track
	private int previousPos;

	@Override
	public void move(int step, boolean travelling) {
		previousPos = pos;
		super.move(step, travelling);
	}

	private static final String PREV_POS = "prev_pos";
	private static final String INVEST_TURNS = "invest_turns";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PREV_POS, previousPos);
		bundle.put(INVEST_TURNS, investigatingTurns);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		previousPos = bundle.getInt(PREV_POS);
		investigatingTurns = bundle.getInt(INVEST_TURNS);
	}

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
			//classic wandering detection if enemy is within a 90 degree cone of vision
			if (Math.abs(enemyDir - movementDir) <= 45f){
				return 1 / (distance( enemy ) / 2f + enemy.stealth());
			//classic sleeping (i.e. default) detection if enemy is within a 170 degree vision cone
			} else if (Math.abs(enemyDir - movementDir) <= 85f){
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
			return true;
		}
	}

	public class Sleeping extends Mob.Sleeping{

		protected void awaken(boolean enemyInFOV) {
			super.awaken(enemyInFOV);
			if (state == HUNTING){
				alerted = false;
				state = INVESTIGATING;
				investigatingTurns = 0;
				sprite.showInvestigate();
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
