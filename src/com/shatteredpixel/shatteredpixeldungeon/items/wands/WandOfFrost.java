package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Slow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

/**
 * Created by debenhame on 23/04/2015.
 */
public class WandOfFrost extends Wand {

	{
		image = ItemSpriteSheet.WAND_FROST;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null){


			int damage = Random.NormalIntRange(5+level, 10+(level*level/3));

			if (ch.buff(Frost.class) != null){
				return; //do nothing, can't affect a frozen target
			}
			if (ch.buff(Chill.class) != null){
				damage = Math.round(damage * ch.buff(Chill.class).speedFactor());
			} else {
				ch.sprite.burst(0x99FFFFFF, level / 2 + 2);
			}

			ch.damage(damage, this);

			if (ch.isAlive()){
				if (Level.water[ch.pos]){
					//20+(10*level)% chance
					if (Random.Int(10) >= 8-level )
						Buff.affect(ch, Frost.class, Frost.duration(ch)*Random.Float(2f, 4f));
					else
						Buff.prolong(ch, Chill.class, 6+level);
				} else {
					Buff.prolong(ch, Chill.class, 4+level);
				}
			}
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.blueLight(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		new Slow().proc(staff, attacker, defender, damage);
	}

	@Override
	public String desc() {
		return "This wand seems to be made out of some kind of magical ice. It grows brighter towards its " +
				"rounded tip. It feels very cold when held, but somehow your hand stays warm.\n\n" +
				"This wand shoots blasts of icy energy toward your foes, dealing significant damage and chilling, " +
				"which reduces speed. The effect seems stronger in water. Chilled and frozen enemies " +
				"take less damage from this wand, as they are already cold.";
	}
}
