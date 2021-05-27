package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.watabou.utils.Bundle;

public class DirectableAlly extends NPC {

	{
		alignment = Char.Alignment.ALLY;
		intelligentAlly = true;
		WANDERING = new Wandering();
		state = WANDERING;

		//before other mobs
		actPriority = MOB_PRIO + 1;

	}

	protected boolean attacksAutomatically = true;

	protected int defendingPos = -1;
	protected boolean movingToDefendPos = false;

	public void defendPos( int cell ){
		aggro(null);
		state = WANDERING;
		defendingPos = cell;
		movingToDefendPos = true;
	}

	public void clearDefensingPos(){
		defendingPos = -1;
		movingToDefendPos = false;
	}

	public void followHero(){
		aggro(null);
		state = WANDERING;
		defendingPos = -1;
		movingToDefendPos = false;
	}

	public void targetChar( Char ch ){
		aggro(ch);
		target = ch.pos;
		defendingPos = -1;
		movingToDefendPos = false;
	}

	public void directTocell( int cell ){
		if (!Dungeon.level.heroFOV[cell]
				|| Actor.findChar(cell) == null
				|| (Actor.findChar(cell) != Dungeon.hero && Actor.findChar(cell).alignment != Char.Alignment.ENEMY)){
			defendPos( cell );
			return;
		}

		//TODO commenting this out for now, it should be pointless??
		/*if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );*/

		if (Actor.findChar(cell) == Dungeon.hero){
			followHero();

		} else if (Actor.findChar(cell).alignment == Char.Alignment.ENEMY){
			targetChar(Actor.findChar(cell));

		}
	}

	private static final String DEFEND_POS = "defend_pos";
	private static final String MOVING_TO_DEFEND = "moving_to_defend";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEFEND_POS, defendingPos);
		bundle.put(MOVING_TO_DEFEND, movingToDefendPos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(DEFEND_POS)) defendingPos = bundle.getInt(DEFEND_POS);
		movingToDefendPos = bundle.getBoolean(MOVING_TO_DEFEND);
	}

	private class Wandering extends Mob.Wandering {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if ( enemyInFOV && !movingToDefendPos && attacksAutomatically) {

				enemySeen = true;

				notice();
				alerted = true;
				state = HUNTING;
				target = enemy.pos;

			} else {

				enemySeen = false;

				int oldPos = pos;
				target = defendingPos != -1 ? defendingPos : Dungeon.hero.pos;
				//always move towards the hero when wandering
				if (getCloser( target )) {
					spend( 1 / speed() );
					if (pos == defendingPos) movingToDefendPos = false;
					return moveSprite( oldPos, pos );
				} else {
					//if it can't move closer to defending pos, then give up and defend current position
					if (movingToDefendPos){
						defendingPos = pos;
						movingToDefendPos = false;
					}
					spend( TICK );
				}

			}
			return true;
		}

	}

}
