package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class BrokenSigil extends Item {

	public static final String AC_AFFIX = "AFFIX";

	{
		image = ItemSpriteSheet.SIGIL;

		cursedKnown = levelKnown = true;
		unique = true;
		bones = false;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions =  super.actions(hero);
		actions.add(AC_AFFIX);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_AFFIX)){
			curItem = this;
			GameScene.selectItem(armorSelector, WndBag.Mode.ARMOR, Messages.get(this, "prompt"));
		} else {
			super.execute(hero, action);
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	protected static WndBag.Listener armorSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof Armor) {
				Armor armor = (Armor)item;
				if (!armor.levelKnown){
					GLog.w(Messages.get(BrokenSigil.class, "unknown_armor"));
				} else if (armor.cursed || armor.level() < 0){
					GLog.w(Messages.get(BrokenSigil.class, "degraded_armor"));
				} else {
					GLog.p(Messages.get(BrokenSigil.class, "affix"));
					Dungeon.hero.sprite.operate(Dungeon.hero.pos);
					Sample.INSTANCE.play(Assets.SND_UNLOCK);
					armor.affixSigil((BrokenSigil)curItem);
					curItem.detach(Dungeon.hero.belongings.backpack);
				}
			}
		}
	};

	public static class SigilShield extends Buff {

		private Armor armor;
		private float partialShield;

		@Override
		public boolean act() {
			if (armor == null) detach();
			else if (armor.isEquipped((Hero)target)) {
				if (target.SHLD < maxShield()){
					partialShield += 1/(30*Math.pow(0.9f, (maxShield() - target.SHLD)));
				}
			}
			while (partialShield >= 1){
				target.SHLD++;
				partialShield--;
			}
			spend(TICK);
			return true;
		}

		public void setArmor(Armor arm){
			armor = arm;
		}

		public int maxShield() {
			return 1 + armor.tier + armor.level();
		}
	}
}
