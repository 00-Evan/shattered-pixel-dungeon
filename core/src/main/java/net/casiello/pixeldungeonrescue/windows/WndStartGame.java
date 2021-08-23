/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package net.casiello.pixeldungeonrescue.windows;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.Badges;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.GamesInProgress;
import net.casiello.pixeldungeonrescue.SPDSettings;
import net.casiello.pixeldungeonrescue.ShatteredPixelDungeon;
import net.casiello.pixeldungeonrescue.actors.hero.HeroClass;
import net.casiello.pixeldungeonrescue.actors.hero.HeroSubClass;
import net.casiello.pixeldungeonrescue.journal.Journal;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.scenes.InterlevelScene;
import net.casiello.pixeldungeonrescue.scenes.IntroScene;
import net.casiello.pixeldungeonrescue.scenes.PixelScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.ui.ActionIndicator;
import net.casiello.pixeldungeonrescue.ui.IconButton;
import net.casiello.pixeldungeonrescue.ui.Icons;
import net.casiello.pixeldungeonrescue.ui.RedButton;
import net.casiello.pixeldungeonrescue.ui.RenderedTextBlock;
import net.casiello.pixeldungeonrescue.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import static net.casiello.pixeldungeonrescue.actors.hero.HeroClass.HUNTER_F;
import static net.casiello.pixeldungeonrescue.actors.hero.HeroClass.MAGE_F;
import static net.casiello.pixeldungeonrescue.actors.hero.HeroClass.ROGUE_F;
import static net.casiello.pixeldungeonrescue.actors.hero.HeroClass.WARRIOR_F;

public class WndStartGame extends Window {
	
	private static final int WIDTH    = 120;
	private static final int HEIGHT   = 160;

	public WndStartGame(final int slot){
		
		Badges.loadGlobal();
		Journal.loadGlobal();
		
		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 12 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos( (WIDTH - title.width())/2f, 3);
		PixelScene.align(title);
		add(title);
		
		float heroBtnSpacing = (WIDTH - 4*HeroBtn.WIDTH)/5f;
		
		float curX = heroBtnSpacing;
		float curY = title.height() + 7;
		int index = 0;
		for (HeroClass cl : HeroClass.values()){
			HeroBtn button = new HeroBtn(cl);
			button.setRect(curX, curY, HeroBtn.WIDTH, HeroBtn.HEIGHT);
			curX += HeroBtn.WIDTH + heroBtnSpacing;
			add(button);
			++index;
			if ((index % 4) == 0) {
				curX = heroBtnSpacing;
				curY += 20;
			}
		}

		ColorBlock separator = new ColorBlock(1, 1, 0xFF222222);
		separator.size(WIDTH, 1);
		separator.x = 0;
		separator.y = title.bottom() + 12 + HeroBtn.HEIGHT * 2;
		add(separator);
		
		HeroPane ava = new HeroPane();
		ava.setRect(20, separator.y + 2, WIDTH-30, 80);
		add(ava);
		
		RedButton start = new RedButton(Messages.get(this, "start")){
			@Override
			protected void onClick() {
				if (GamesInProgress.selectedClass == null) return;
				
				super.onClick();
				
				GamesInProgress.curSlot = slot;
				Dungeon.hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
				
//				if (SPDSettings.intro()) {
//					SPDSettings.intro( false );
					Game.switchScene( IntroScene.class );
//				} else {
//					Game.switchScene( InterlevelScene.class );
//				}
			}
			
			@Override
			public void update() {
				if( !visible && GamesInProgress.selectedClass != null){
					visible = true;
				}
				super.update();
			}
		};
		start.visible = false;
		start.setRect(0, HEIGHT - 20, WIDTH, 20);
		add(start);
		
		if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)){
			IconButton challengeButton = new IconButton(
					Icons.get( SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndChallenges(SPDSettings.challenges(), true) {
						public void onBackPressed() {
							super.onBackPressed();
							if (parent != null) {
								icon(Icons.get(SPDSettings.challenges() > 0 ?
										Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
							}
						}
					} );
				}
				
				@Override
				public void update() {
					if( !visible && GamesInProgress.selectedClass != null){
						visible = true;
					}
					super.update();
				}
			};
			challengeButton.setRect(WIDTH - 20, HEIGHT - 20, 20, 20);
			challengeButton.visible = false;
			add(challengeButton);
			
		} else {
			Dungeon.challenges = 0;
			SPDSettings.challenges(0);
		}
		
		resize(WIDTH, HEIGHT);
		
	}
	
	private static class HeroBtn extends Button {
		
		private HeroClass cl;
		
		private Image hero;
		
		private static final int WIDTH = 24;
		private static final int HEIGHT = 16;
		
		HeroBtn ( HeroClass cl ){
			super();
			
			this.cl = cl;

			add(hero = new Image(cl.spritesheet(), 0, 90, 12, 15));
			
		}
		
		@Override
		protected void layout() {
			super.layout();
			if (hero != null){
				hero.x = x + (width - hero.width()) / 2f;
				hero.y = y + (height - hero.height()) / 2f;
				PixelScene.align(hero);
			}
		}
		
		@Override
		public void update() {
			super.update();
			if (cl != GamesInProgress.selectedClass){
				if (!cl.isUnlocked()){
					hero.brightness(0.3f);
				} else {
					hero.brightness(0.6f);
				}
			} else {
				hero.brightness(1f);
			}
		}
		
		@Override
		protected void onClick() {
			super.onClick();
			
			if( !cl.isUnlocked() ){
				ShatteredPixelDungeon.scene().addToFront( new WndMessage(cl.unlockMsg()));
			} else {
				GamesInProgress.selectedClass = cl;
			}
		}
	}
	
	private class HeroPane extends Component {
		
		private HeroClass cl;
		
		private Image avatar;
		
		private IconButton heroItem;
		private IconButton heroLoadout;
		private IconButton heroMisc;
		private IconButton heroSubclass;
		
		private RenderedTextBlock name;
		
		private static final int BTN_SIZE = 20;
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			avatar = new Image(Assets.AVATARS);
			avatar.scale.set(2f);
			add(avatar);

			heroItem = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_item")));
				}
			};
			heroItem.setSize(BTN_SIZE, BTN_SIZE);
			add(heroItem);

			heroLoadout = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_loadout")));
				}
			};
			heroLoadout.setSize(BTN_SIZE, BTN_SIZE);
			add(heroLoadout);

			heroMisc = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_misc")));
				}
			};
			heroMisc.setSize(BTN_SIZE, BTN_SIZE);
			add(heroMisc);

			heroSubclass = new IconButton(new ItemSprite(ItemSpriteSheet.MASTERY, null)){
				@Override
				protected void onClick() {
					if (cl == null) return;
					String msg = Messages.get(cl, cl.name() + "_desc_subclasses");
					for (HeroSubClass sub : cl.subClasses()){
						msg += "\n\n" + sub.desc();
					}
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(msg));
				}
			};
			heroSubclass.setSize(BTN_SIZE, BTN_SIZE);
			add(heroSubclass);

			name = PixelScene.renderTextBlock(12);
			add(name);
			
			visible = false;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			avatar.x = x;
			avatar.y = y + (height - avatar.height() - name.height() - 4)/2f;
			PixelScene.align(avatar);

			name.setPos(
					x + (avatar.width() - name.width())/2f,
					avatar.y + avatar.height() + 3
			);
			PixelScene.align(name);
			
			heroItem.setPos(x + width - BTN_SIZE, y);
			heroLoadout.setPos(x + width - BTN_SIZE, heroItem.bottom());
			heroMisc.setPos(x + width - BTN_SIZE, heroLoadout.bottom());
			heroSubclass.setPos(x + width - BTN_SIZE, heroMisc.bottom());
		}
		
		@Override
		public synchronized void update() {
			super.update();
			if (GamesInProgress.selectedClass != cl){
				cl = GamesInProgress.selectedClass;
				if (cl != null) {
					avatar.frame((cl.ordinal() % 4) * 24, (cl.ordinal() / 4) * 32, 24, 32);

					switch(cl.heroType()){
						case WARRIOR:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SEAL, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.RATION, null));
							name.text(Messages.capitalize((cl == WARRIOR_F) ? "Sonja" : "Conan")); // Red Sonja / Conan the Cimmerian
							break;
						case MAGE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.HOLDER, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE, null));
							name.text(Messages.capitalize((cl == MAGE_F) ? "Megan" : "Stephen"));  // Pixie / Dr. Strange
							break;
						case ROGUE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.DAGGER, null));
							heroMisc.icon(Icons.get(Icons.DEPTH));
							name.text(Messages.capitalize((cl == ROGUE_F) ? "Anna" : "Kurt"));  // Rogue / Nightcrawler
							break;
						case HUNTER:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SPIRIT_BOW, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.GLOVES, null));
							heroMisc.icon(new Image(Assets.TILES_SEWERS, 112, 96, 16, 16 ));
							name.text(Messages.capitalize((cl == HUNTER_F) ? "Helena" : "Oliver"));  // Huntress / Green Arrow
							break;
					}
					
					layout();
					
					visible = true;
				} else {
					visible = false;
				}
			}
		}
	}

}
