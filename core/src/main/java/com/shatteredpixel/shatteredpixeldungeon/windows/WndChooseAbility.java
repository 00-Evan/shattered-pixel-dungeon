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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndChooseAbility extends Window {

	private static final int WIDTH		= 120;
	private static final float GAP		= 2;

	public WndChooseAbility(final KingsCrown crown, final Armor armor, final Hero hero){

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( crown.image(), null ) );
		titlebar.label( crown.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextBlock body = PixelScene.renderTextBlock( 6 );
		body.text( Messages.get(this, "message"), WIDTH );
		body.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( body );

		float pos = body.bottom() + 3*GAP;
		for (ArmorAbility ability : hero.heroClass.armorAbilities()) {

			RedButton abilityButton = new RedButton("_" + Messages.titleCase(ability.name()) + ":_ " + ability.desc(), 6){
				@Override
				protected void onClick() {
					super.onClick();
					hide();
					crown.upgradeArmor(hero, armor, ability);
				}
			};
			abilityButton.leftJustify = true;
			abilityButton.multiline = true;
			abilityButton.setSize(WIDTH, abilityButton.reqHeight()+2);
			abilityButton.setRect(0, pos, WIDTH, abilityButton.reqHeight()+2);
			add(abilityButton);
			pos = abilityButton.bottom() + GAP;
		}

		resize(WIDTH, (int)pos);

	}


}
