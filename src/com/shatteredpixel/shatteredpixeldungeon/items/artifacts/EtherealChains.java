package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

import java.util.ArrayList;

/**
 * Created by Evan on 23/05/2015.
 */
public class EtherealChains extends Artifact {

	public static final String AC_CAST       = "CAST";

	{
		name = "ethereal chains";
		image = ItemSpriteSheet.ARTIFACT_CHAINS;

		level = 0;
		levelCap = 5;
		exp = 0;

		charge = 0;

		defaultAction = AC_CAST;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped(hero) && charge > 0 && !cursed)
			actions.add(AC_CAST);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_CAST)){

			curUser = hero;

			if      (!isEquipped( hero ))       GLog.i("You need to equip the chains to do that.");
			else if (charge < 1)                GLog.i("Your chains do not have any available charge.");
			else if (cursed)                    GLog.w("You can't use cursed chains.");
			else {
				GameScene.selectCell(caster);
			}

		} else
			super.execute(hero, action);
	}

	private CellSelector.Listener caster = new CellSelector.Listener(){

		@Override
		public void onSelect(Integer target) {
			if (target != null && (Dungeon.level.visited[target] || Dungeon.level.mapped[target])){
				final Ballistica chain = new Ballistica(curUser.pos, target, Ballistica.STOP_CHARS | Ballistica.STOP_TARGET);

				//determine if we're grabbing an enemy, pulling to a location, or doing nothing.
				if (Actor.findChar( chain.collisionPos ) != null){
					int newPos = -1;
					for (int i : chain.subPath(1, chain.dist)){
						if (!Level.solid[i] && Actor.findChar(i) == null){
							newPos = i;
							break;
						}
					}
					if (newPos == -1){
						GLog.w("That won't do anything");
					} else {
						final int newMobPos = newPos;
						final Char affected = Actor.findChar( chain.collisionPos );
						int chargeUse = Level.distance(affected.pos, newMobPos);
						if (chargeUse > charge){
							GLog.w("Your chains do not have enough charge.");
							return;
						} else {
							charge -= chargeUse;
							updateQuickslot();
						}
						curUser.busy();
						curUser.sprite.parent.add(new Chains(curUser.pos, affected.pos, new Callback() {
							public void call() {
								Actor.add(new Pushing(affected, affected.pos, newMobPos));
								affected.pos = newMobPos;
								Dungeon.observe();
								curUser.spendAndNext(1f);
								Dungeon.level.press(newMobPos, affected);
							}
						}));
					}

				} else if (Level.solid[chain.path.get(chain.dist)]
						|| (chain.dist > 0 && Level.solid[chain.path.get(chain.dist-1)])
						|| (chain.path.size() > chain.dist+1 && Level.solid[chain.path.get(chain.dist+1)])
						//if the player is trying to grapple the edge of the map, let them.
						|| (chain.path.size() == chain.dist+1)) {
					int newPos = -1;
					for (int i : chain.subPath(1, chain.dist)){
						if (!Level.solid[i] && Actor.findChar(i) == null) newPos = i;
						}
					if (newPos == -1) {
						GLog.w("That won't do anything");
					} else {
						final int newHeroPos = newPos;
						int chargeUse = Level.distance(curUser.pos, newHeroPos);
						if (chargeUse > charge){
							GLog.w("Your chains do not have enough charge.");
							return;
						} else {
							charge -= chargeUse;
							updateQuickslot();
						}
						curUser.busy();
						curUser.sprite.parent.add(new Chains(curUser.pos, target, new Callback() {
							public void call() {
								Actor.add(new Pushing(curUser, curUser.pos, newHeroPos, new Callback() {
									public void call() {
										Dungeon.level.press(newHeroPos, curUser);
									}
								}));
								curUser.spendAndNext(1f);
								curUser.pos = newHeroPos;
								Dungeon.observe();
							}
						}));
					}

				} else {
					GLog.i("There is nothing to grab there");
				}

			}

		}

		@Override
		public String prompt() {
			return "Choose a location to target";
		}
	};

	@Override
	protected ArtifactBuff passiveBuff() {
		return new chainsRecharge();
	}

	//TODO: description
	@Override
	public String desc() {
		return super.desc();
	}

	public class chainsRecharge extends ArtifactBuff{

		@Override
		public boolean act() {
			int chargeTarget = 5+level;
			if (charge < chargeTarget) {
				partialCharge += 1 / (40f - (chargeTarget - charge)*3f);
			}

			if (partialCharge >= 1) {
				partialCharge --;
				charge ++;
			}

			updateQuickslot();

			spend( TICK );

			return true;
		}

		public void gainExp( float levelPortion ) {
			exp += Math.round(levelPortion*100);
			partialCharge += levelPortion*10f;

			if (exp > 100+level*50){
				exp -= 100+level*50;
				GLog.p("Your chains grow stronger!");
				upgrade();
			}

		}
	}
}
