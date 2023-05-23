/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BadgesGrid;
import com.shatteredpixel.shatteredpixeldungeon.ui.BadgesList;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class WndRanking extends WndTabbed {
	
	private static final int WIDTH			= 115;
	private static final int HEIGHT			= 144;
	
	private static WndRanking INSTANCE;
	
	private String gameID;
	private Rankings.Record record;
	
	public WndRanking( final Rankings.Record rec ) {
		
		super();
		resize( WIDTH, HEIGHT );

		if (INSTANCE != null){
			INSTANCE.hide();
		}
		INSTANCE = this;

		this.gameID = rec.gameID;
		this.record = rec;

		try {
			Badges.loadGlobal();
			Rankings.INSTANCE.loadGameData( rec );
			createControls();
		} catch ( Exception e ) {
			Game.reportException(e);
			Dungeon.hero = null;
			createControls();
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if (INSTANCE == this){
			INSTANCE = null;
		}
	}
	
	private void createControls() {

		if (Dungeon.hero != null) {
			Icons[] icons =
					{Icons.RANKINGS, Icons.TALENT, Icons.BACKPACK_LRG, Icons.BADGES, Icons.CHALLENGE_ON};
			Group[] pages =
					{new StatsTab(), new TalentsTab(), new ItemsTab(), new BadgesTab(), null};

			if (Dungeon.challenges != 0) pages[4] = new ChallengesTab();

			for (int i = 0; i < pages.length; i++) {

				if (pages[i] == null) {
					break;
				}

				add(pages[i]);

				Tab tab = new RankingTab(icons[i], pages[i]);
				add(tab);
			}

			layoutTabs();

			select(0);
		} else {
			StatsTab tab = new StatsTab();
			add(tab);

		}
	}

	private class RankingTab extends IconTab {
		
		private Group page;
		
		public RankingTab( Icons icon, Group page ) {
			super( Icons.get(icon) );
			this.page = page;
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}
	
	private class StatsTab extends Group {

		private int GAP	= 4;
		
		public StatsTab() {
			super();
			
			String heroClass = record.heroClass.name();
			if (Dungeon.hero != null){
				heroClass = Dungeon.hero.className();
			}
			
			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar( record.heroClass, record.armorTier ) );
			title.label( Messages.get(this, "title", record.herolevel, heroClass ).toUpperCase( Locale.ENGLISH ) );
			title.color(Window.TITLE_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add( title );

			if (Dungeon.hero != null && Dungeon.seed != -1){
				GAP--;
			}
			
			float pos = title.bottom() + 1;

			RenderedTextBlock date = PixelScene.renderTextBlock(record.date, 7);
			date.hardlight(0xCCCCCC);
			date.setPos(0, pos);
			add(date);

			RenderedTextBlock version = PixelScene.renderTextBlock(record.version, 7);
			version.hardlight(0xCCCCCC);
			version.setPos(WIDTH-version.width(), pos);
			add(version);

			pos = date.bottom()+5;

			NumberFormat num = NumberFormat.getInstance(Locale.US);

			if (Dungeon.hero == null){
				pos = statSlot( this, Messages.get(this, "score"), num.format( record.score ), pos );
				pos += GAP;

				Image errorIcon = Icons.WARNING.get();
				errorIcon.y = pos;
				add(errorIcon);

				RenderedTextBlock errorText = PixelScene.renderTextBlock(Messages.get(WndRanking.class, "error"), 6);
				errorText.maxWidth((int)(WIDTH-errorIcon.width()-GAP));
				errorText.setPos(errorIcon.width()+GAP, pos + (errorIcon.height()-errorText.height())/2);
				add(errorText);

			} else {

				pos = statSlot(this, Messages.get(this, "score"), num.format(Statistics.totalScore), pos);

				IconButton scoreInfo = new IconButton(Icons.get(Icons.INFO)) {
					@Override
					protected void onClick() {
						super.onClick();
						ShatteredPixelDungeon.scene().addToFront(new WndScoreBreakdown());
					}
				};
				scoreInfo.setSize(16, 16);
				scoreInfo.setPos(WIDTH - scoreInfo.width(), pos - 10 - GAP);
				add(scoreInfo);

				pos += GAP;

				int strBonus = Dungeon.hero.STR() - Dungeon.hero.STR;
				if (strBonus > 0)
					pos = statSlot(this, Messages.get(this, "str"), Dungeon.hero.STR + " + " + strBonus, pos);
				else if (strBonus < 0)
					pos = statSlot(this, Messages.get(this, "str"), Dungeon.hero.STR + " - " + -strBonus, pos);
				else
					pos = statSlot(this, Messages.get(this, "str"), Integer.toString(Dungeon.hero.STR), pos);
				pos = statSlot(this, Messages.get(this, "duration"), num.format((int) Statistics.duration), pos);
				if (Statistics.highestAscent == 0) {
					pos = statSlot(this, Messages.get(this, "depth"), num.format(Statistics.deepestFloor), pos);
				} else {
					pos = statSlot(this, Messages.get(this, "ascent"), num.format(Statistics.highestAscent), pos);
				}
				if (Dungeon.seed != -1) {
					if (Dungeon.daily) {
						if (Dungeon.dailyReplay) {
							pos = statSlot(this, Messages.get(this, "replay_for"), "_" + Dungeon.customSeedText + "_", pos);
						} else {
							pos = statSlot(this, Messages.get(this, "daily_for"), "_" + Dungeon.customSeedText + "_", pos);
						}
					} else if (!Dungeon.customSeedText.isEmpty()) {
						pos = statSlot(this, Messages.get(this, "custom_seed"), "_" + Dungeon.customSeedText + "_", pos);
					} else {
						pos = statSlot(this, Messages.get(this, "seed"), DungeonSeed.convertToCode(Dungeon.seed), pos);
					}
				} else {
					pos += GAP + 5;
				}

				pos += GAP;

				pos = statSlot(this, Messages.get(this, "enemies"), num.format(Statistics.enemiesSlain), pos);
				pos = statSlot(this, Messages.get(this, "gold"), num.format(Statistics.goldCollected), pos);
				pos = statSlot(this, Messages.get(this, "food"), num.format(Statistics.foodEaten), pos);
				pos = statSlot(this, Messages.get(this, "alchemy"), num.format(Statistics.itemsCrafted), pos);
			}

			int buttontop = HEIGHT - 16;

			if (Dungeon.hero != null && Dungeon.seed != -1 && !Dungeon.daily &&
					(DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY))){
				final Image icon = Icons.get(Icons.SEED);
				RedButton btnSeed = new RedButton(Messages.get(this, "copy_seed")){
					@Override
					protected void onClick() {
						super.onClick();
						ShatteredPixelDungeon.scene().addToFront(new WndOptions(new Image(icon),
								Messages.get(WndRanking.StatsTab.this, "copy_seed"),
								Messages.get(WndRanking.StatsTab.this, "copy_seed_desc"),
								Messages.get(WndRanking.StatsTab.this, "copy_seed_copy"),
								Messages.get(WndRanking.StatsTab.this, "copy_seed_cancel")){
							@Override
							protected void onSelect(int index) {
								super.onSelect(index);
								if (index == 0){
									SPDSettings.customSeed(DungeonSeed.convertToCode(Dungeon.seed));
									icon.hardlight(1f, 1.5f, 0.67f);
								}
							}
						});
					}
				};
				if (DungeonSeed.convertFromText(SPDSettings.customSeed()) == Dungeon.seed){
					icon.hardlight(1f, 1.5f, 0.67f);
				}
				btnSeed.icon(icon);
				btnSeed.setRect(0, buttontop, 115, 16);
				add(btnSeed);
			}

		}
		
		private float statSlot( Group parent, String label, String value, float pos ) {
			
			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
			txt.setPos(0, pos);
			parent.add( txt );
			
			txt = PixelScene.renderTextBlock( value, 7 );
			txt.setPos(WIDTH * 0.6f, pos);
			PixelScene.align(txt);
			parent.add( txt );
			
			return pos + GAP + txt.height();
		}
	}

	private class TalentsTab extends Group{

		public TalentsTab(){
			super();

			camera = WndRanking.this.camera;

			int tiers = 1;
			if (Dungeon.hero.lvl >= 6) tiers++;
			if (Dungeon.hero.lvl >= 12 && Dungeon.hero.subClass != HeroSubClass.NONE) tiers++;
			if (Dungeon.hero.lvl >= 20 && Dungeon.hero.armorAbility != null) tiers++;
			while (Dungeon.hero.talents.size() > tiers){
				Dungeon.hero.talents.remove(Dungeon.hero.talents.size()-1);
			}

			TalentsPane p = new TalentsPane(TalentButton.Mode.INFO);
			add(p);
			p.setPos(0, 0);
			p.setSize(WIDTH, HEIGHT);
			p.setPos(0, 0);

		}

	}

	private class ItemsTab extends Group {
		
		private float pos;
		
		public ItemsTab() {
			super();
			
			Belongings stuff = Dungeon.hero.belongings;
			if (stuff.weapon != null) {
				addItem( stuff.weapon );
			}
			if (stuff.armor != null) {
				addItem( stuff.armor );
			}
			if (stuff.artifact != null) {
				addItem( stuff.artifact );
			}
			if (stuff.misc != null) {
				addItem( stuff.misc );
			}
			if (stuff.ring != null) {
				addItem( stuff.ring );
			}

			pos = 0;

			int slotsActive = 0;
			for (int i = 0; i < QuickSlot.SIZE; i++){
				if (Dungeon.quickslot.isNonePlaceholder(i)){
					slotsActive++;
				}
			}

			float slotWidth = Math.min(28, ((WIDTH - slotsActive + 1) / (float)slotsActive));

			for (int i = 0; i < QuickSlot.SIZE; i++){
				if (Dungeon.quickslot.isNonePlaceholder(i)){
					QuickSlotButton slot = new QuickSlotButton(Dungeon.quickslot.getItem(i));

					slot.setRect( pos, 120, slotWidth, 23 );
					PixelScene.align(slot);

					add(slot);

					pos += slotWidth + 1;

				}
			}
		}
		
		private void addItem( Item item ) {
			ItemButton slot = new ItemButton( item );
			slot.setRect( 0, pos, width, ItemButton.HEIGHT );
			add( slot );
			
			pos += slot.height() + 1;
		}
	}
	
	private class BadgesTab extends Group {
		
		public BadgesTab() {
			super();
			
			camera = WndRanking.this.camera;

			Component badges;
			if (Badges.filterReplacedBadges(false).size() <= 8){
				badges = new BadgesList(false);
			} else {
				badges = new BadgesGrid(false);
			}
			add(badges);
			badges.setSize( WIDTH, HEIGHT );
		}
	}

	private class ChallengesTab extends Group{

		public ChallengesTab(){
			super();

			camera = WndRanking.this.camera;

			float pos = 0;

			for (int i=0; i < Challenges.NAME_IDS.length; i++) {

				final String challenge = Challenges.NAME_IDS[i];

				CheckBox cb = new CheckBox( Messages.titleCase(Messages.get(Challenges.class, challenge)) );
				cb.checked( (Dungeon.challenges & Challenges.MASKS[i]) != 0 );
				cb.active = false;

				if (i > 0) {
					pos += 1;
				}
				cb.setRect( 0, pos, WIDTH-16, 15 );

				add( cb );

				IconButton info = new IconButton(Icons.get(Icons.INFO)){
					@Override
					protected void onClick() {
						super.onClick();
						ShatteredPixelDungeon.scene().add(
								new WndMessage(Messages.get(Challenges.class, challenge+"_desc"))
						);
					}
				};
				info.setRect(cb.right(), pos, 16, 15);
				add(info);

				pos = cb.bottom();
			}
		}

	}

	private class ItemButton extends Button {
		
		public static final int HEIGHT	= 23;
		
		private Item item;
		
		private ItemSlot slot;
		private ColorBlock bg;
		private RenderedTextBlock name;
		
		public ItemButton( Item item ) {
			
			super();

			this.item = item;
			
			slot.item( item );
			if (item.cursed && item.cursedKnown) {
				bg.ra = +0.2f;
				bg.ga = -0.1f;
			} else if (!item.isIdentified()) {
				bg.ra = 0.1f;
				bg.ba = 0.1f;
			}
		}
		
		@Override
		protected void createChildren() {
			
			bg = new ColorBlock( 28, HEIGHT, 0x9953564D );
			add( bg );
			
			slot = new ItemSlot();
			add( slot );
			
			name = PixelScene.renderTextBlock( 7 );
			add( name );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			slot.setRect( x, y, 28, HEIGHT );
			PixelScene.align(slot);
			
			name.maxWidth((int)(width - slot.width() - 2));
			name.text(Messages.titleCase(item.name()));
			name.setPos(
					slot.right()+2,
					y + (height - name.height()) / 2
			);
			PixelScene.align(name);
			
			super.layout();
		}
		
		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
		}
		
		protected void onPointerUp() {
			bg.brightness( 1.0f );
		}
		
		@Override
		protected void onClick() {
			Game.scene().add( new WndInfoItem( item ) );
		}
	}

	private class QuickSlotButton extends ItemSlot{

		private Item item;
		private ColorBlock bg;

		QuickSlotButton(Item item){
			super(item);
			this.item = item;

			if (item.cursed && item.cursedKnown) {
				bg.ra = +0.2f;
				bg.ga = -0.1f;
			} else if (!item.isIdentified()) {
				bg.ra = 0.1f;
				bg.ba = 0.1f;
			}
		}

		@Override
		protected void createChildren() {
			bg = new ColorBlock( 1, 1, 0x9953564D );
			add( bg );

			super.createChildren();
		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;

			bg.size( width(), height() );

			super.layout();
		}

		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
		}

		protected void onPointerUp() {
			bg.brightness( 1.0f );
		}

		@Override
		protected void onClick() {
			Game.scene().add(new WndInfoItem(item));
		}
	}
}
