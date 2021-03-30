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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndHeroInfo extends WndTabbed {

	private RenderedTextBlock title;
	private RenderedTextBlock info;

	private TalentsPane talents;
	private RedButton firstSub;
	private RedButton secondSub;

	private int WIDTH = 120;
	private int HEIGHT = 125;
	private int MARGIN = 2;
	private int INFO_WIDTH = WIDTH - MARGIN*2;

	private static boolean secondSubclass = false;

	public WndHeroInfo( HeroClass cl ){

		title = PixelScene.renderTextBlock(9);
		title.hardlight(TITLE_COLOR);
		add(title);

		info = PixelScene.renderTextBlock(6);
		add(info);

		ArrayList<LinkedHashMap<Talent, Integer>> talentList = new ArrayList<>();
		Talent.initClassTalents(cl, talentList);
		Talent.initSubclassTalents(cl.subClasses()[secondSubclass ? 1 : 0], talentList);
		talents = new TalentsPane(false, talentList);
		add(talents);

		boolean subsAvailable = Badges.isUnlocked(Badges.Badge.LEVEL_REACHED_2) && Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_2);

		firstSub = new RedButton(Messages.titleCase(cl.subClasses()[0].title()), 7){
			@Override
			protected void onClick() {
				super.onClick();
				if (secondSubclass){
					secondSubclass = false;
					hide();
					WndHeroInfo newWindow = new WndHeroInfo(cl);
					newWindow.talents.scrollTo(0, talents.content().camera.scroll.y);
					newWindow.select(2);
					ShatteredPixelDungeon.scene().addToFront(newWindow);
				}
			}
		};
		if (!secondSubclass) firstSub.textColor(Window.TITLE_COLOR);
		firstSub.setSize(40, firstSub.reqHeight()+2);
		if (subsAvailable) add(firstSub);

		secondSub = new RedButton(Messages.titleCase(cl.subClasses()[1].title()), 7){
			@Override
			protected void onClick() {
				super.onClick();
				if (!secondSubclass){
					secondSubclass = true;
					hide();
					WndHeroInfo newWindow = new WndHeroInfo(cl);
					newWindow.talents.scrollTo(0, talents.content().camera.scroll.y);
					newWindow.select(2);
					ShatteredPixelDungeon.scene().addToFront(newWindow);
				}
			}
		};
		if (secondSubclass) secondSub.textColor(Window.TITLE_COLOR);
		secondSub.setSize(40, secondSub.reqHeight()+2);
		if (subsAvailable) add(secondSub);

		Tab tab;
		Image[] tabIcons;
		switch (cl){
			case WARRIOR: default:
				tabIcons = new Image[]{
						new ItemSprite(ItemSpriteSheet.SEAL, null),
						new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD, null)
				};
				break;
			case MAGE:
				tabIcons = new Image[]{
						new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null),
						new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null)
				};
				break;
			case ROGUE:
				tabIcons = new Image[]{
						new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null),
						new ItemSprite(ItemSpriteSheet.DAGGER, null)
				};
				break;
			case HUNTRESS:
				tabIcons = new Image[]{
						new ItemSprite(ItemSpriteSheet.SPIRIT_BOW, null),
						new ItemSprite(ItemSpriteSheet.GLOVES, null)
				};
				break;
		}

		tab = new IconTab( tabIcons[0] ){
			@Override
			protected void select(boolean value) {
				super.select(value);
				if (value){
					title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "innate_title")));
					info.text(Messages.get(cl, cl.name() + "_desc_innate"), INFO_WIDTH);
				}
			}
		};
		add(tab);

		tab = new IconTab( tabIcons[1] ){
			@Override
			protected void select(boolean value) {
				super.select(value);
				if (value){
					title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "loadout_title")));
					info.text(Messages.get(cl, cl.name() + "_desc_loadout"), INFO_WIDTH);
				}
			}
		};
		add(tab);

		tab = new IconTab( Icons.get(Icons.TALENT) ){
			@Override
			protected void select(boolean value) {
				super.select(value);
				if (value){
					title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "talents_title")));
					info.text(Messages.get(WndHeroInfo.class, "talents_desc"), INFO_WIDTH);
				}
				talents.visible = talents.active = value;
				firstSub.visible = firstSub.active = value;
				secondSub.visible = secondSub.active = value;
			}
		};
		add(tab);

		tab = new IconTab(new ItemSprite(ItemSpriteSheet.MASTERY, null)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				if (value){
					title.text(Messages.titleCase(Messages.get(WndHeroInfo.class, "subclasses_title")));
					String msg = Messages.get(cl, cl.name() + "_desc_subclasses");
					for (HeroSubClass sub : cl.subClasses()){
						msg += "\n\n" + sub.desc();
					}
					info.text(msg, INFO_WIDTH);
				}
			}
		};
		add(tab);

		resize(WIDTH, HEIGHT);
		select(0);

	}

	@Override
	public void select(Tab tab) {
		super.select(tab);

		title.setPos((WIDTH-title.width())/2, MARGIN);
		info.setPos(MARGIN, title.bottom()+2*MARGIN);

		firstSub.setPos((title.left() - firstSub.width())/2, 0);
		secondSub.setPos(title.right() + (WIDTH - title.right() - secondSub.width())/2, 0);

		talents.setRect(0, info.bottom()+MARGIN, WIDTH, HEIGHT - (info.bottom()+MARGIN));

		resize(WIDTH, Math.max(HEIGHT, (int)info.bottom()));

		layoutTabs();

	}
}
