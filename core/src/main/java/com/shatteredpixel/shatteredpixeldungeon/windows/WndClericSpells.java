/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.GuidingLight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class WndClericSpells extends Window {

	protected static final int WIDTH    = 120;

	public WndClericSpells(HolyTome tome, Hero cleric, boolean info){

		IconTitle title = new IconTitle(new ItemSprite(tome), info ? "Spell Info" : "Cast A Spell");

		title.setRect(0, 0, WIDTH, 0);
		add(title);

		IconButton btnInfo = new IconButton(Icons.INFO.get()){
			@Override
			protected void onClick() {
				GameScene.show(new WndClericSpells(tome, cleric, !info));
				hide();
			}
		};
		btnInfo.setRect(WIDTH-16, 0, 16, 16);
		add(btnInfo);

		//TODO we might want to intercept quickslot hotkeys and auto-cast the last spell if relevant
		RenderedTextBlock msg = PixelScene.renderTextBlock(  info ? "Select a spell to learn about it, or press the info button to switch to cast mode." : "Select a spell to cast it, or press the info button to switch to info mode.", 6);
		msg.maxWidth(WIDTH);
		msg.setPos(0, title.bottom()+4);
		add(msg);

		//TODO build spell list
		ArrayList<ClericSpell> spells = new ArrayList<>();
		spells.add(GuidingLight.INSTANCE);

		ArrayList<IconButton> spellBtns = new ArrayList<>();

		for (ClericSpell spell : spells){
			IconButton spellBtn = new IconButton(new HeroIcon(spell)){
				@Override
				protected void onClick() {
					if (info){
						ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(new HeroIcon(spell), spell.name(), spell.desc()));
					} else {
						hide();
						spell.use(tome, cleric);

						//TODO, probably need targeting logic here
						if (spell.useTargeting() && Dungeon.quickslot.contains(tome)){
							QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(tome));
						}
					}
				}
			};
			if (!info && !tome.canCast(cleric, spell)){
				spellBtn.enable(false);
			}
			add(spellBtn);
			spellBtns.add(spellBtn);
		}

		//TODO rows?
		int left = 0;
		for (IconButton btn : spellBtns){
			btn.setRect(left, msg.bottom()+4, 20, 20);
			left += btn.width()+4;
		}

		resize(WIDTH, (int)spellBtns.get(0).bottom());

	}

	//TODO we probably want to offset this window for mobile so it appears closer to quickslots

}
