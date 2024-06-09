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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickRecipe;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collection;

public class WndJournal extends WndTabbed {
	
	public static final int WIDTH_P     = 126;
	public static final int HEIGHT_P    = 180;
	
	public static final int WIDTH_L     = 216;
	public static final int HEIGHT_L    = 130;
	
	private static final int ITEM_HEIGHT	= 18;
	
	private GuideTab guideTab;
	private AlchemyTab alchemyTab;
	private NotesTab notesTab;
	private CatalogTab catalogTab;
	private LoreTab loreTab;
	
	public static int last_index = 0;
	
	public WndJournal(){
		
		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;
		int height = PixelScene.landscape() ? HEIGHT_L : HEIGHT_P;
		
		resize(width, height);
		
		guideTab = new GuideTab();
		add(guideTab);
		guideTab.setRect(0, 0, width, height);
		guideTab.updateList();
		
		alchemyTab = new AlchemyTab();
		add(alchemyTab);
		alchemyTab.setRect(0, 0, width, height);
		
		notesTab = new NotesTab();
		add(notesTab);
		notesTab.setRect(0, 0, width, height);
		notesTab.updateList();
		
		catalogTab = new CatalogTab();
		add(catalogTab);
		catalogTab.setRect(0, 0, width, height);
		catalogTab.updateList();

		loreTab = new LoreTab();
		add(loreTab);
		loreTab.setRect(0, 0, width, height);
		loreTab.updateList();
		
		Tab[] tabs = {
				new IconTab( new ItemSprite(ItemSpriteSheet.MASTERY, null) ) {
					protected void select( boolean value ) {
						super.select( value );
						guideTab.active = guideTab.visible = value;
						if (value) last_index = 0;
					}
				},
				new IconTab( new ItemSprite(ItemSpriteSheet.ALCH_PAGE, null) ) {
					protected void select( boolean value ) {
						super.select( value );
						alchemyTab.active = alchemyTab.visible = value;
						if (value) last_index = 1;
					}
				},
				new IconTab( Icons.get(Icons.STAIRS) ) {
					protected void select( boolean value ) {
						super.select( value );
						notesTab.active = notesTab.visible = value;
						if (value) last_index = 2;
					}
				},
				new IconTab( new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER, null) ) {
					protected void select( boolean value ) {
						super.select( value );
						catalogTab.active = catalogTab.visible = value;
						if (value) last_index = 3;
					}
				},
				new IconTab( new ItemSprite(ItemSpriteSheet.GUIDE_PAGE, null) ) {
					protected void select( boolean value ) {
						super.select( value );
						loreTab.active = loreTab.visible = value;
						if (value) last_index = 4;
					}
				}
		};
		
		for (Tab tab : tabs) {
			add( tab );
		}
		
		layoutTabs();
		
		select(last_index);
	}

	@Override
	public void offset(int xOffset, int yOffset) {
		super.offset(xOffset, yOffset);
		guideTab.layout();
		alchemyTab.layout();
		notesTab.layout();
		catalogTab.layout();
		loreTab.layout();
	}
	
	public static class GuideTab extends Component {

		private ScrollingListPane list;
		
		@Override
		protected void createChildren() {
			list = new ScrollingListPane();
			add( list );
		}
		
		@Override
		protected void layout() {
			super.layout();
			list.setRect( 0, 0, width, height);
		}
		
		private void updateList(){
			list.addTitle(Document.ADVENTURERS_GUIDE.title());

			for (String page : Document.ADVENTURERS_GUIDE.pageNames()){
				boolean found = Document.ADVENTURERS_GUIDE.isPageFound(page);
				ScrollingListPane.ListItem item = new ScrollingListPane.ListItem(
						Document.ADVENTURERS_GUIDE.pageSprite(page),
						null,
						found ? Messages.titleCase(Document.ADVENTURERS_GUIDE.pageTitle(page)) : Messages.titleCase(Messages.get( this, "missing" ))
				){
					@Override
					public boolean onClick(float x, float y) {
						if (inside( x, y ) && found) {
							GameScene.show( new WndStory( Document.ADVENTURERS_GUIDE.pageSprite(page),
									Document.ADVENTURERS_GUIDE.pageTitle(page),
									Document.ADVENTURERS_GUIDE.pageBody(page) ));
							Document.ADVENTURERS_GUIDE.readPage(page);
							return true;
						} else {
							return false;
						}
					}
				};
				if (!found){
					item.hardlight(0x999999);
					item.hardlightIcon(0x999999);
				}
				list.addItem(item);
			}

			list.setRect(x, y, width, height);
		}

	}
	
	public static class AlchemyTab extends Component {
		
		private RedButton[] pageButtons;
		private static final int NUM_BUTTONS = 9;
		
		private static final int[] sprites = {
				ItemSpriteSheet.SEED_HOLDER,
				ItemSpriteSheet.STONE_HOLDER,
				ItemSpriteSheet.FOOD_HOLDER,
				ItemSpriteSheet.POTION_HOLDER,
				ItemSpriteSheet.SCROLL_HOLDER,
				ItemSpriteSheet.BOMB_HOLDER,
				ItemSpriteSheet.MISSILE_HOLDER,
				ItemSpriteSheet.ELIXIR_HOLDER,
				ItemSpriteSheet.SPELL_HOLDER
		};
		
		public static int currentPageIdx   = 0;
		
		private IconTitle title;
		private RenderedTextBlock body;
		
		private ScrollPane list;
		private ArrayList<QuickRecipe> recipes = new ArrayList<>();
		
		@Override
		protected void createChildren() {
			pageButtons = new RedButton[NUM_BUTTONS];
			for (int i = 0; i < NUM_BUTTONS; i++){
				final int idx = i;
				pageButtons[i] = new RedButton( "" ){
					@Override
					protected void onClick() {
						currentPageIdx = idx;
						updateList();
					}
				};
				if (Document.ALCHEMY_GUIDE.isPageFound(i)) {
					pageButtons[i].icon(new ItemSprite(sprites[i], null));
				} else {
					pageButtons[i].icon(new ItemSprite(ItemSpriteSheet.SOMETHING, null));
					pageButtons[i].enable(false);
				}
				add( pageButtons[i] );
			}
			
			title = new IconTitle();
			title.icon( new ItemSprite(ItemSpriteSheet.ALCH_PAGE));
			title.visible = false;

			body = PixelScene.renderTextBlock(6);
			
			list = new ScrollPane(new Component());
			add(list);
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (PixelScene.landscape()){
				float buttonWidth = width()/pageButtons.length;
				for (int i = 0; i < NUM_BUTTONS; i++) {
					pageButtons[i].setRect(i*buttonWidth, 0, buttonWidth, ITEM_HEIGHT);
					PixelScene.align(pageButtons[i]);
				}
			} else {
				//for first row
				float buttonWidth = width()/5;
				float y = 0;
				float x = 0;
				for (int i = 0; i < NUM_BUTTONS; i++) {
					pageButtons[i].setRect(x, y, buttonWidth, ITEM_HEIGHT);
					PixelScene.align(pageButtons[i]);
					x += buttonWidth;
					if (i == 4){
						y += ITEM_HEIGHT;
						x = 0;
						buttonWidth = width()/4;
					}
				}
			}
			
			list.setRect(0, pageButtons[NUM_BUTTONS-1].bottom() + 1, width,
					height - pageButtons[NUM_BUTTONS-1].bottom() - 1);
			
			updateList();
		}
		
		private void updateList() {

			if (currentPageIdx != -1 && !Document.ALCHEMY_GUIDE.isPageFound(currentPageIdx)){
				currentPageIdx = -1;
			}

			for (int i = 0; i < NUM_BUTTONS; i++) {
				if (i == currentPageIdx) {
					pageButtons[i].icon().color(TITLE_COLOR);
				} else {
					pageButtons[i].icon().resetColor();
				}
			}
			
			if (currentPageIdx == -1){
				return;
			}
			
			for (QuickRecipe r : recipes){
				if (r != null) {
					r.killAndErase();
					r.destroy();
				}
			}
			recipes.clear();
			
			Component content = list.content();
			
			content.clear();
			
			title.visible = true;
			title.label(Document.ALCHEMY_GUIDE.pageTitle(currentPageIdx));
			title.setRect(0, 0, width(), 10);
			content.add(title);
			
			body.maxWidth((int)width());
			body.text(Document.ALCHEMY_GUIDE.pageBody(currentPageIdx));
			body.setPos(0, title.bottom());
			content.add(body);

			Document.ALCHEMY_GUIDE.readPage(currentPageIdx);
			
			ArrayList<QuickRecipe> toAdd = QuickRecipe.getRecipes(currentPageIdx);
			
			float left;
			float top = body.bottom()+2;
			int w;
			ArrayList<QuickRecipe> toAddThisRow = new ArrayList<>();
			while (!toAdd.isEmpty()){
				if (toAdd.get(0) == null){
					toAdd.remove(0);
					top += 6;
				}
				
				w = 0;
				while(!toAdd.isEmpty() && toAdd.get(0) != null
						&& w + toAdd.get(0).width() <= width()){
					toAddThisRow.add(toAdd.remove(0));
					w += toAddThisRow.get(0).width();
				}
				
				float spacing = (width() - w)/(toAddThisRow.size() + 1);
				left = spacing;
				while (!toAddThisRow.isEmpty()){
					QuickRecipe r = toAddThisRow.remove(0);
					r.setPos(left, top);
					left += r.width() + spacing;
					if (!toAddThisRow.isEmpty()) {
						ColorBlock spacer = new ColorBlock(1, 16, 0xFF222222);
						spacer.y = top;
						spacer.x = left - spacing / 2 - 0.5f;
						PixelScene.align(spacer);
						content.add(spacer);
					}
					recipes.add(r);
					content.add(r);
				}
				
				if (!toAdd.isEmpty() && toAdd.get(0) == null){
					toAdd.remove(0);
				}
				
				if (!toAdd.isEmpty() && toAdd.get(0) != null) {
					ColorBlock spacer = new ColorBlock(width(), 1, 0xFF222222);
					spacer.y = top + 16;
					spacer.x = 0;
					content.add(spacer);
				}
				top += 17;
				toAddThisRow.clear();
			}
			top -= 1;
			content.setSize(width(), top);
			list.setSize(list.width(), list.height());
			list.scrollTo(0, 0);
		}
	}
	
	private static class NotesTab extends Component {
		
		private ScrollingListPane list;
		
		@Override
		protected void createChildren() {
			list = new ScrollingListPane();
			add( list );
		}
		
		@Override
		protected void layout() {
			super.layout();
			list.setRect( x, y, width, height);
		}
		
		private void updateList(){
			//Keys
			ArrayList<Notes.KeyRecord> keys = Notes.getRecords(Notes.KeyRecord.class);
			if (!keys.isEmpty()){
				list.addTitle(Messages.get(this, "keys"));

				for(Notes.Record rec : keys){
					ScrollingListPane.ListItem item = new ScrollingListPane.ListItem( Icons.get(Icons.STAIRS),
							Integer.toString(rec.depth()),
							Messages.titleCase(rec.desc()));
					if (Dungeon.depth == rec.depth()) item.hardlight(TITLE_COLOR);
					list.addItem(item);
				}
			}
			
			//Landmarks
			ArrayList<Notes.LandmarkRecord> landmarks = Notes.getRecords(Notes.LandmarkRecord.class);
			if (!landmarks.isEmpty()){

				list.addTitle(Messages.get(this, "landmarks"));

				for (Notes.Record rec : landmarks) {
					ScrollingListPane.ListItem item = new ScrollingListPane.ListItem( Icons.get(Icons.STAIRS),
							Integer.toString(rec.depth()),
							Messages.titleCase(rec.desc()));
					if (Dungeon.depth == rec.depth()) item.hardlight(TITLE_COLOR);
					list.addItem(item);
				}

			}

			list.setRect(x, y, width, height);
		}
		
	}
	
	private static class CatalogTab extends Component{
		
		private RedButton[] itemButtons;
		private static final int NUM_BUTTONS = 2;
		
		private static int currentItemIdx   = 0;
		
		//sprite locations
		private static final int EQUIP_IDX = 0;
		private static final int CONSUM_IDX = 1;
		
		private static final int spriteIndexes[] =
				{ItemSpriteSheet.WEAPON_HOLDER,
						ItemSpriteSheet.POTION_HOLDER};

		private ScrollingGridPane grid;
		
		@Override
		protected void createChildren() {
			itemButtons = new RedButton[NUM_BUTTONS];
			for (int i = 0; i < NUM_BUTTONS; i++){
				final int idx = i;
				itemButtons[i] = new RedButton( "" ){
					@Override
					protected void onClick() {
						currentItemIdx = idx;
						updateList();
					}
				};
				itemButtons[i].icon(new ItemSprite(spriteIndexes[i], null));
				add( itemButtons[i] );
			}

			grid = new ScrollingGridPane();
			add( grid );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			int perRow = NUM_BUTTONS;
			float buttonWidth = width()/perRow;
			
			for (int i = 0; i < NUM_BUTTONS; i++) {
				itemButtons[i].setRect((i%perRow) * (buttonWidth), (i/perRow) * (ITEM_HEIGHT ),
						buttonWidth, ITEM_HEIGHT);
				PixelScene.align(itemButtons[i]);
			}
			
			grid.setRect(0, itemButtons[NUM_BUTTONS-1].bottom() + 1, width,
					height - itemButtons[NUM_BUTTONS-1].bottom() - 1);
		}
		
		private void updateList() {
			
			grid.clear();
			
			for (int i = 0; i < NUM_BUTTONS; i++){
				if (i == currentItemIdx){
					itemButtons[i].icon().color(TITLE_COLOR);
				} else {
					itemButtons[i].icon().resetColor();
				}
			}
			
			grid.scrollTo( 0, 0 );

			if (currentItemIdx == EQUIP_IDX) {
				int totalItems = Catalog.WEAPONS.totalItems() + Catalog.ARMOR.totalItems() + Catalog.WANDS.totalItems() + Catalog.RINGS.totalItems() + Catalog.ARTIFACTS.totalItems();
				int totalSeen = Catalog.WEAPONS.totalSeen() + Catalog.ARMOR.totalSeen() + Catalog.WANDS.totalSeen() + Catalog.RINGS.totalSeen() + Catalog.ARTIFACTS.totalSeen();
				grid.addHeader("_" + Messages.get(this, "title_equipment") + "_ (" + totalSeen + "/" + totalItems + ")", 9, true);

				grid.addHeader("_" + Messages.capitalize(Catalog.WEAPONS.title()) + "_ (" + Catalog.WEAPONS.totalSeen() + "/" + Catalog.WEAPONS.totalItems() + "):");
				addGridItems(grid, Catalog.WEAPONS.items());

				grid.addHeader("_" + Messages.capitalize(Catalog.ARMOR.title()) + "_ (" + Catalog.ARMOR.totalSeen() + "/" + Catalog.ARMOR.totalItems() + "):");
				addGridItems(grid, Catalog.ARMOR.items());

				grid.addHeader("_" + Messages.capitalize(Catalog.WANDS.title()) + "_ (" + Catalog.WANDS.totalSeen() + "/" + Catalog.WANDS.totalItems() + "):");
				addGridItems(grid, Catalog.WANDS.items());

				grid.addHeader("_" + Messages.capitalize(Catalog.RINGS.title()) + "_ (" + Catalog.RINGS.totalSeen() + "/" + Catalog.RINGS.totalItems() + "):");
				addGridItems(grid, Catalog.RINGS.items());

				grid.addHeader("_" + Messages.capitalize(Catalog.ARTIFACTS.title()) + "_ (" + Catalog.ARTIFACTS.totalSeen() + "/" + Catalog.ARTIFACTS.totalItems() + "):");
				addGridItems(grid, Catalog.ARTIFACTS.items());

			} else if (currentItemIdx == CONSUM_IDX){
				int totalItems = Catalog.POTIONS.totalItems() + Catalog.SCROLLS.totalItems();
				int totalSeen = Catalog.POTIONS.totalSeen() + Catalog.SCROLLS.totalSeen();
				grid.addHeader("_" + Messages.get(this, "title_consumables") + "_ (" + totalSeen + "/" + totalItems + ")", 9, true);

				grid.addHeader("_" + Messages.capitalize(Catalog.POTIONS.title()) + "_ (" + Catalog.POTIONS.totalSeen() + "/" + Catalog.POTIONS.totalItems() + "):");
				addGridItems(grid, Catalog.POTIONS.items());

				grid.addHeader("_" + Messages.capitalize(Catalog.SCROLLS.title()) + "_ (" + Catalog.SCROLLS.totalSeen() + "/" + Catalog.SCROLLS.totalItems() + "):");
				addGridItems(grid, Catalog.SCROLLS.items());

			}

			grid.setRect(x, itemButtons[NUM_BUTTONS-1].bottom() + 1, width,
					height - itemButtons[NUM_BUTTONS-1].bottom() - 1);
		}
		
	}

	private static void addGridItems( ScrollingGridPane grid, Collection<Class<? extends Item>> itemClasses) {
		for (Class<? extends Item> itemClass : itemClasses) {
			Item item = Reflection.newInstance(itemClass);
			boolean itemSeen = Catalog.isSeen(itemClass);

			if (itemSeen) {
				if (item instanceof Ring) {
					((Ring) item).anonymize();
				} else if (item instanceof Potion) {
					((Potion) item).anonymize();
				} else if (item instanceof Scroll) {
					((Scroll) item).anonymize();
				}
			}

			Image sprite = new ItemSprite(item);
			if (!itemSeen) sprite.lightness(0);
			ScrollingGridPane.GridItem gridItem = new ScrollingGridPane.GridItem(
					sprite) {
				@Override
				public boolean onClick(float x, float y) {
					if (inside(x, y)) {
						Image sprite = new Image(icon);
						if (itemSeen) {
							GameScene.show(new WndTitledMessage(sprite,
									Messages.titleCase(item.trueName()),
									item instanceof ClassArmor ? item.desc() : item.info()));
						} else {
							sprite.lightness(0);
							GameScene.show(new WndTitledMessage(sprite,
									"???",
									Messages.get(CatalogTab.class, "not_seen")));
						}
						return true;
					} else {
						return false;
					}
				}
			};
			if (itemSeen) {
				if (item instanceof Potion || item instanceof Scroll || item instanceof Ring) {
					Image icon = new Image(Assets.Sprites.ITEM_ICONS);
					icon.frame(ItemSpriteSheet.Icons.film.get(item.icon));
					gridItem.addSecondIcon(icon);
				}
			} else {
				gridItem.hardLightBG(2f, 1f, 2f);
			}
			grid.addItem(gridItem);
		}
	}

	public static class LoreTab extends Component{

		private ScrollingListPane list;

		@Override
		protected void createChildren() {
			list = new ScrollingListPane();
			add( list );
		}

		@Override
		protected void layout() {
			super.layout();
			list.setRect( 0, 0, width, height);
		}

		private void updateList(){
			list.addTitle(Messages.get(this, "title"));

			for (Document doc : Document.values()){
				if (!doc.isLoreDoc()) continue;

				boolean found = doc.anyPagesFound();
				ScrollingListPane.ListItem item = new ScrollingListPane.ListItem(
						doc.pageSprite(),
						null,
						found ? Messages.titleCase(doc.title()) : "???"
				){
					@Override
					public boolean onClick(float x, float y) {
						if (inside( x, y ) && found) {
							ShatteredPixelDungeon.scene().addToFront( new WndDocument( doc ));
							return true;
						} else {
							return false;
						}
					}
				};
				if (!found){
					item.hardlight(0x999999);
					item.hardlightIcon(0x999999);
				}
				list.addItem(item);
			}

			list.setRect(x, y, width, height);
		}

	}
	
}
