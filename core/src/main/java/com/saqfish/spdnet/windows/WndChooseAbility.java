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

package com.saqfish.spdnet.windows;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.actors.hero.abilities.ArmorAbility;
import com.saqfish.spdnet.items.KingsCrown;
import com.saqfish.spdnet.items.armor.Armor;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.sprites.ItemSprite;
import com.saqfish.spdnet.ui.HeroIcon;
import com.saqfish.spdnet.ui.IconButton;
import com.saqfish.spdnet.ui.Icons;
import com.saqfish.spdnet.ui.RedButton;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.ui.Window;
import com.watabou.noosa.Image;

public class WndChooseAbility extends Window {

	private static final int WIDTH		= 130;
	private static final float GAP		= 2;

	public WndChooseAbility(final KingsCrown crown, final Armor armor, final Hero hero){

		super();

		//crown can be null if hero is choosing from armor, pre-0.9.3 saves
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( crown == null ? armor.image() : crown.image(), null ) );
		titlebar.label( Messages.titleCase(crown == null ? armor.name() : crown.name()) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextBlock body = PixelScene.renderTextBlock( 6 );
		if (crown != null) {
			body.text(Messages.get(this, "message"), WIDTH);
		} else {
			body.text(Messages.get(this, "message_no_crown"), WIDTH);
		}
		body.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( body );

		float pos = body.bottom() + 3*GAP;
		for (ArmorAbility ability : hero.heroClass.armorAbilities()) {

			RedButton abilityButton = new RedButton(ability.shortDesc(), 6){
				@Override
				protected void onClick() {
					GameScene.show(new WndOptions( new HeroIcon( ability ),
							Messages.titleCase(ability.name()),
							Messages.get(WndChooseAbility.this, "are_you_sure"),
							Messages.get(WndChooseAbility.this, "yes"),
							Messages.get(WndChooseAbility.this, "no")){

						@Override
						protected void onSelect(int index) {
							hide();
							if (index == 0 && WndChooseAbility.this.parent != null){
								WndChooseAbility.this.hide();
								if (crown != null) {
									crown.upgradeArmor(hero, armor, ability);
								} else {
									new KingsCrown().upgradeArmor(hero, null, ability);
								}
							}
						}
					});
				}
			};
			abilityButton.leftJustify = true;
			abilityButton.multiline = true;
			abilityButton.setSize(WIDTH-20, abilityButton.reqHeight()+2);
			abilityButton.setRect(0, pos, WIDTH-20, abilityButton.reqHeight()+2);
			add(abilityButton);

			IconButton abilityInfo = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					GameScene.show(new WndInfoArmorAbility(Dungeon.hero.heroClass, ability));
				}
			};
			abilityInfo.setRect(WIDTH-20, abilityButton.top() + (abilityButton.height()-20)/2, 20, 20);
			add(abilityInfo);

			pos = abilityButton.bottom() + GAP;
		}

		RedButton cancelButton = new RedButton(Messages.get(this, "cancel")){
			@Override
			protected void onClick() {
				hide();
			}
		};
		cancelButton.setRect(0, pos, WIDTH, 18);
		add(cancelButton);
		pos = cancelButton.bottom() + GAP;

		resize(WIDTH, (int)pos);

	}


}
