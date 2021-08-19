package com.saqfish.spdnet.items;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.LostInventory;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.actors.hero.Talent;
import com.saqfish.spdnet.items.artifacts.CloakOfShadows;
import com.saqfish.spdnet.items.bags.MagicalHolster;
import com.saqfish.spdnet.items.wands.Wand;
import com.saqfish.spdnet.items.weapon.melee.MagesStaff;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.sprites.HeroSprite;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class LostBackpack extends Item {

	{
		image = ItemSpriteSheet.BACKPACK;

		unique = true;
	}

	@Override
	public boolean doPickUp(Hero hero) {
		if (hero.buff(LostInventory.class) != null){
			hero.buff(LostInventory.class).detach();
		}

		MagicalHolster holster = hero.belongings.getItem(MagicalHolster.class);
		for (Item i : hero.belongings){
			if (i.keptThoughLostInvent){
				i.keptThoughLostInvent = false; //don't reactivate, was previously activated
			} else {
				if (i instanceof EquipableItem && i.isEquipped(hero)){
					((EquipableItem) i).activate(hero);
				} else if ( i instanceof CloakOfShadows && hero.hasTalent(Talent.LIGHT_CLOAK)){
					((CloakOfShadows) i).activate(hero);
				} else if (i instanceof Wand){
					if (holster != null && holster.contains(i)){
						((Wand) i).charge(hero, MagicalHolster.HOLSTER_SCALE_FACTOR);
					} else {
						((Wand) i).charge(hero);
					}
				} else if (i instanceof MagesStaff){
					((MagesStaff) i).applyWandChargeBuff(hero);
				}
			}
		}

		Item.updateQuickslot();
		Sample.INSTANCE.play( Assets.Sounds.DEWDROP );
		hero.spendAndNext(TIME_TO_PICK_UP);
		GameScene.pickUp( this, hero.pos );
		((HeroSprite)hero.sprite).updateArmor();
		return true;
	}
}
