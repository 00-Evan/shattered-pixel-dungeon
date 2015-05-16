package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.King;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yog;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Evan on 14/05/2015.
 */
//TODO: balancing
public class WandOfCorruption extends Wand {

	{
		name = "Wand of Corruption";
		image = ItemSpriteSheet.WAND_CORRUPTION;
	}

	//FIXME: sloppy
	private static HashSet<Class> bosses = new HashSet<Class>(Arrays.asList(
			Goo.class, Tengu.class, DM300.class, King.class, Yog.class, Yog.BurningFist.class, Yog.RottingFist.class
	));

	@Override
	protected void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);

		if (ch != null){

			if (bosses.contains(ch.getClass())){
				GLog.w("Bosses are immune to corruption");
				return;
			}

			int basePower = 5 + 5*level;
			int mobPower = Random.NormalIntRange(0, ch.HT+ch.HP);

			int extraCharges = 0;
			//try to use extra charges to overpower the mob
			while (basePower <= mobPower){
				extraCharges++;
				basePower += 10 + 2.5*level;
			}

			//if we fail, lose all charges, remember we have 1 left to lose from using the wand.
			if (extraCharges >= curCharges){
				curCharges = 1;
				GLog.w("The corrupting power was not strong enough, nothing happens.");
				return;
			}

			//otherwise corrupt the mob & spend charges
			Buff.append(ch, Corruption.class);
			ch.HP = ch.HT;
			curCharges -= extraCharges;
			usagesToKnow -= extraCharges;
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		if (Random.Int( level + 4 ) >= 3){
			Buff.prolong( defender, Amok.class, 3+level);
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.shadow(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public String desc() {
		return "This wand radiates dark energy, if that weren't already obvious from the small decorative skull shaped onto its tip.\n" +
				"\n" +
				"This wand will release a blast of corrupting energy, attempting to bend enemies to your will. " +
				"The weaker an enemy is, the easier they are to corrupt. " +
				"Successfully corrupting an enemy restores them to full health.\n" +
				"\n" +
				"This wand uses at least one charge per cast, but will often use more in an attempt to overpower tougher enemies.";
	}
}
