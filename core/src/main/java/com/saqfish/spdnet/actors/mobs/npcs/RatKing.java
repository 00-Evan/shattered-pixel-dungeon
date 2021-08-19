/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.actors.mobs.npcs;

import com.saqfish.spdnet.Badges;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.hero.abilities.Ratmogrify;
import com.saqfish.spdnet.items.KingsCrown;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.sprites.RatKingSprite;
import com.saqfish.spdnet.windows.WndInfoArmorAbility;
import com.saqfish.spdnet.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class RatKing extends NPC {

	{
		spriteClass = RatKingSprite.class;
		
		state = SLEEPING;
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public float speed() {
		return 2f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	//***This functionality is for when rat king may be summoned by a distortion trap

	@Override
	protected void onAdd() {
		super.onAdd();
		if (Dungeon.depth != 5){
			yell(Messages.get(this, "confused"));
		}
	}

	@Override
	protected boolean act() {
		if (Dungeon.depth < 5){
			if (pos == Dungeon.level.exit){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.exit;
			}
		} else if (Dungeon.depth > 5){
			if (pos == Dungeon.level.entrance){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.entrance;
			}
		}
		return super.act();
	}

	//***

	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );

		if (c != Dungeon.hero){
			return super.interact(c);
		}

		KingsCrown crown = Dungeon.hero.belongings.getItem(KingsCrown.class);
		if (state == SLEEPING) {
			notice();
			yell( Messages.get(this, "not_sleeping") );
			state = WANDERING;
		} else if (crown != null){
			if (Dungeon.hero.belongings.armor() == null){
				yell( Messages.get(RatKing.class, "crown_clothes") );
			} else {
				Badges.validateRatmogrify();
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndOptions(
								sprite(),
								Messages.titleCase(name()),
								Messages.get(RatKing.class, "crown_desc"),
								Messages.get(RatKing.class, "crown_yes"),
								Messages.get(RatKing.class, "crown_info"),
								Messages.get(RatKing.class, "crown_no")
						){
							@Override
							protected void onSelect(int index) {
								if (index == 0){
									crown.upgradeArmor(Dungeon.hero, Dungeon.hero.belongings.armor(), new Ratmogrify());
									((RatKingSprite)sprite).resetAnims();
									yell(Messages.get(RatKing.class, "crown_thankyou"));
								} else if (index == 1) {
									GameScene.show(new WndInfoArmorAbility(Dungeon.hero.heroClass, new Ratmogrify()));
								} else {
									yell(Messages.get(RatKing.class, "crown_fine"));
								}
							}
						});
					}
				});
			}
		} else if (Dungeon.hero.armorAbility instanceof Ratmogrify) {
			yell( Messages.get(RatKing.class, "crown_after") );
		} else {
			yell( Messages.get(this, "what_is_it") );
		}
		return true;
	}
	
	@Override
	public String description() {
		return ((RatKingSprite)sprite).festive ?
				Messages.get(this, "desc_festive")
				: super.description();
	}
}
