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

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.TargetedClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.NinePatch;

import java.util.ArrayList;

public class WndClericSpells extends Window {

	protected static final int WIDTH    = 120;

	public static int BTN_SIZE = 20;

	public WndClericSpells(HolyTome tome, Hero cleric, boolean info){

		IconTitle title = new IconTitle(new ItemSprite(tome), Messages.titleCase(Messages.get( this, info ? "info_title" : "cast_title")));

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
		RenderedTextBlock msg = PixelScene.renderTextBlock( Messages.get( this, info ? "info_desc" : "cast_desc"), 6);
		msg.maxWidth(WIDTH);
		msg.setPos(0, title.bottom()+4);
		add(msg);

		ArrayList<ClericSpell> spells = ClericSpell.getSpellList(cleric);

		ArrayList<IconButton> spellBtns = new ArrayList<>();

		for (ClericSpell spell : spells){
			IconButton spellBtn = new SpellButton(spell, tome, info);
			add(spellBtn);
			spellBtns.add(spellBtn);
		}

		//TODO rows? Maybe based on spell tiers?
		int left = 2 + (WIDTH-spellBtns.size()*(BTN_SIZE+4))/2;
		for (IconButton btn : spellBtns){
			btn.setRect(left, msg.bottom()+4, BTN_SIZE, BTN_SIZE);
			left += btn.width()+4;
		}

		resize(WIDTH, (int)spellBtns.get(0).bottom());

	}

	//TODO we probably want to offset this window for mobile so it appears closer to quickslots

	public class SpellButton extends IconButton {

		ClericSpell spell;
		HolyTome tome;
		boolean info;

		NinePatch bg;

		public SpellButton(ClericSpell spell, HolyTome tome, boolean info){
			super(new HeroIcon(spell));

			this.spell = spell;
			this.tome = tome;
			this.info = info;

			if (!info && !tome.canCast(Dungeon.hero, spell)){
				enable(false);
			}

			bg = Chrome.get(Chrome.Type.TOAST);
			addToBack(bg);
		}

		@Override
		protected void layout() {
			super.layout();

			if (bg != null) {
				bg.size(width, height);
				bg.x = x;
				bg.y = y;
			}
		}

		@Override
		protected void onClick() {
			if (info){
				GameScene.show(new WndTitledMessage(new HeroIcon(spell), Messages.titleCase(spell.name()), spell.desc()));
			} else {
				hide();
				spell.onCast(tome, Dungeon.hero);

				//TODO, probably need targeting logic here
				if (spell instanceof TargetedClericSpell && Dungeon.quickslot.contains(tome)){
					QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(tome));
				}
			}
		}

		@Override
		protected String hoverText() {
			return "_" + Messages.titleCase(spell.name()) + "_\n" + spell.shortDesc();
		}
	}

}
