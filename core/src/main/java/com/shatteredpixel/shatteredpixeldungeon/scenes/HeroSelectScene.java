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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTabbed;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.GameMath;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HeroSelectScene extends PixelScene {

	private Image background;
	private RenderedTextBlock prompt;

	//fading UI elements
	private ArrayList<StyledButton> heroBtns = new ArrayList<>();
	private StyledButton startBtn;
	private IconButton infoButton;
	private IconButton challengeButton;
	private IconButton btnExit;

	@Override
	public void create() {
		super.create();

		Badges.loadGlobal();
		Journal.loadGlobal();

		background = new Image(HeroClass.WARRIOR.splashArt()){
			@Override
			public void update() {
				if (rm > 1f){
					rm -= Game.elapsed;
					gm = bm = rm;
				} else {
					rm = gm = bm = 1;
				}
			}
		};
		background.scale.set(Camera.main.height/background.height);

		background.x = (Camera.main.width - background.width())/2f;
		background.y = (Camera.main.height - background.height())/2f;
		background.visible = false;
		PixelScene.align(background);
		add(background);

		if (background.x > 0){
			Image fadeLeft = new Image(TextureCache.createGradient(0xFF000000, 0x00000000));
			fadeLeft.x = background.x-2;
			fadeLeft.scale.set(4, background.height());
			add(fadeLeft);

			Image fadeRight = new Image(fadeLeft);
			fadeRight.x = background.x + background.width() + 2;
			fadeRight.y = background.y + background.height();
			fadeRight.angle = 180;
			add(fadeRight);
		}

		prompt = PixelScene.renderTextBlock(Messages.get(this, "title"), 12);
		prompt.hardlight(Window.TITLE_COLOR);
		prompt.setPos( (Camera.main.width - prompt.width())/2f, (Camera.main.height - HeroBtn.HEIGHT - prompt.height() - 4));
		PixelScene.align(prompt);
		add(prompt);

		startBtn = new StyledButton(Chrome.Type.GREY_BUTTON_TR, ""){
			@Override
			protected void onClick() {
				super.onClick();

				if (GamesInProgress.selectedClass == null) return;

				Dungeon.hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

				if (SPDSettings.intro()) {
					SPDSettings.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					Game.switchScene( InterlevelScene.class );
				}
			}
		};
		startBtn.icon(Icons.get(Icons.ENTER));
		startBtn.setSize(80, 21);
		startBtn.setPos((Camera.main.width - startBtn.width())/2f, (Camera.main.height - HeroBtn.HEIGHT + 2 - startBtn.height()));
		add(startBtn);
		startBtn.visible = false;

		infoButton = new IconButton(Icons.get(Icons.INFO)){
			@Override
			protected void onClick() {
				super.onClick();
				ShatteredPixelDungeon.scene().addToFront(new WndHeroInfo(GamesInProgress.selectedClass));
			}
		};
		infoButton.visible = false;
		infoButton.setSize(21, 21);
		add(infoButton);

		HeroClass[] classes = HeroClass.values();

		int btnWidth = HeroBtn.MIN_WIDTH;
		int curX = (Camera.main.width - btnWidth * classes.length)/2;
		if (curX > 0){
			btnWidth += Math.min(curX/(classes.length/2), 15);
			curX = (Camera.main.width - btnWidth * classes.length)/2;
		}

		int heroBtnleft = curX;
		for (HeroClass cl : classes){
			HeroBtn button = new HeroBtn(cl);
			button.setRect(curX, Camera.main.height-HeroBtn.HEIGHT+3, btnWidth, HeroBtn.HEIGHT);
			curX += btnWidth;
			add(button);
			heroBtns.add(button);
		}

		challengeButton = new IconButton(
				Icons.get( SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront(new WndChallenges(SPDSettings.challenges(), true) {
					public void onBackPressed() {
						super.onBackPressed();
						icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
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
		challengeButton.setRect(heroBtnleft + 16, Camera.main.height-HeroBtn.HEIGHT-16, 21, 21);
		challengeButton.visible = false;

		if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)){
			add(challengeButton);
		} else {
			Dungeon.challenges = 0;
			SPDSettings.challenges(0);
		}

		btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		btnExit.visible = !SPDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;

		PointerArea fadeResetter = new PointerArea(0, 0, Camera.main.width, Camera.main.height){
			@Override
			public boolean onSignal(PointerEvent event) {
				resetFade();
				return false;
			}
		};
		add(fadeResetter);
		resetFade();

		if (GamesInProgress.selectedClass != null){
			setSelectedHero(GamesInProgress.selectedClass);
		}

		fadeIn();

	}

	private void setSelectedHero(HeroClass cl){
		GamesInProgress.selectedClass = cl;

		background.texture( cl.splashArt() );
		background.visible = true;
		background.hardlight(1.5f,1.5f,1.5f);

		prompt.visible = false;
		startBtn.visible = true;
		startBtn.text(Messages.titleCase(cl.title()));
		startBtn.textColor(Window.TITLE_COLOR);
		startBtn.setSize(startBtn.reqWidth() + 8, 21);
		startBtn.setPos((Camera.main.width - startBtn.width())/2f, startBtn.top());
		PixelScene.align(startBtn);

		infoButton.visible = true;
		infoButton.setPos(startBtn.right(), startBtn.top());

		challengeButton.visible = true;
		challengeButton.setPos(startBtn.left()-challengeButton.width(), startBtn.top());
	}

	private float uiAlpha;

	@Override
	public void update() {
		super.update();
		btnExit.visible = !SPDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;
		//do not fade when a window is open
		for (Object v : members){
			if (v instanceof Window) resetFade();
		}
		if (GamesInProgress.selectedClass != null) {
			if (uiAlpha > 0f){
				uiAlpha -= Game.elapsed/4f;
			}
			float alpha = GameMath.gate(0f, uiAlpha, 1f);
			for (StyledButton b : heroBtns){
				b.alpha(alpha);
			}
			startBtn.alpha(alpha);
			btnExit.icon().alpha(alpha);
			challengeButton.icon().alpha(alpha);
			infoButton.icon().alpha(alpha);
		}
	}

	private void resetFade(){
		//starts fading after 4 seconds, fades over 4 seconds.
		uiAlpha = 2f;
	}

	@Override
	protected void onBackPressed() {
		if (btnExit.visible){
			ShatteredPixelDungeon.switchScene(TitleScene.class);
		} else {
			super.onBackPressed();
		}
	}

	private class HeroBtn extends StyledButton {

		private HeroClass cl;

		private static final int MIN_WIDTH = 20;
		private static final int HEIGHT = 24;

		HeroBtn ( HeroClass cl ){
			super(Chrome.Type.GREY_BUTTON_TR, "");

			this.cl = cl;

			icon(new Image(cl.spritesheet(), 0, 90, 12, 15));

		}

		@Override
		public void update() {
			super.update();
			if (cl != GamesInProgress.selectedClass){
				if (!cl.isUnlocked()){
					icon.brightness(0.1f);
				} else {
					icon.brightness(0.6f);
				}
			} else {
				icon.brightness(1f);
			}
		}

		@Override
		protected void onClick() {
			super.onClick();

			if( !cl.isUnlocked() ){
				ShatteredPixelDungeon.scene().addToFront( new WndMessage(cl.unlockMsg()));
			} else if (GamesInProgress.selectedClass == cl) {
				ShatteredPixelDungeon.scene().add(new WndHeroInfo(cl));
			} else {
				setSelectedHero(cl);
			}
		}
	}

	private static class WndHeroInfo extends WndTabbed {

		private RenderedTextBlock title;
		private RenderedTextBlock info;

		private TalentsPane talents;
		private RedButton firstSub;
		private RedButton secondSub;

		private int WIDTH = 120;
		private int HEIGHT = 120;
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
			add(firstSub);

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
			add(secondSub);

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
							new ItemSprite(ItemSpriteSheet.HOLDER, null)
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
}
