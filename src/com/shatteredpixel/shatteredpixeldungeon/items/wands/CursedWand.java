package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

//helper class to contain all the cursed wand zapping logic, so the main wand class doesn't get huge.
//TODO: fill this in
public class CursedWand {

	private static float COMMON_CHANCE = 0.6f;
	private static float UNCOMMON_CHANCE = 0.3f;
	private static float RARE_CHANCE = 0.09f;
	private static float VERY_RARE_CHANCE = 0.01f;

	public static void cursedZap(final Hero user, final Ballistica bolt){
		switch (Random.chances(new float[]{COMMON_CHANCE, UNCOMMON_CHANCE, RARE_CHANCE, VERY_RARE_CHANCE})){
			case 0:
			default:
				commonEffect(user, bolt);
				break;
			case 1:
				uncommonEffect(user, bolt);
				break;
			case 2:
				rareEffect(user, bolt);
				break;
			case 3:
				veryRareEffect(user, bolt);
				break;
		}
	}

	private static void commonEffect(final Hero user, final Ballistica bolt){
				//Random Fire!
				if (Random.Int(2) == 0){
					Buff.affect(user, Burning.class).reignite(user);
				} else {
					cursedFX(user, bolt, new Callback() {
						public void call() {
							Char target = Actor.findChar(bolt.collisionPos);
							if (target != null)
								Buff.affect(target, Burning.class).reignite(target);

						}
					});
				}
	}

	private static void uncommonEffect(final Hero user, final Ballistica bolt){

	}

	private static void rareEffect(final Hero user, final Ballistica bolt){

	}

	private static void veryRareEffect(final Hero user, final Ballistica bolt){

	}

	private static void cursedFX(final Hero user, final Ballistica bolt, final Callback callback){
		MagicMissile.shadow(user.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

}
