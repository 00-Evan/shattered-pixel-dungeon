/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHeroInfo;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.GameMath;

import java.util.ArrayList;

public class HeroSelectScene extends PixelScene {

	private Image background;
	private RenderedTextBlock prompt;

	//fading UI elements
	private ArrayList<StyledButton> heroBtns = new ArrayList<>();
	private StyledButton startBtn;
	private IconButton infoButton;
	private IconButton btnOptions;
	private GameOptions optionsPane;
	private IconButton btnExit;

	@Override
	public void create() {
		super.create();

		Dungeon.hero = null;

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

		startBtn = new StyledButton(Chrome.Type.GREY_BUTTON_TR, ""){
			@Override
			protected void onClick() {
				super.onClick();

				if (GamesInProgress.selectedClass == null) return;

				Dungeon.hero = null;
				Dungeon.daily = false;
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

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "hero_info"));
			}
		};
		infoButton.visible = false;
		infoButton.setSize(20, 21);
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

		optionsPane = new GameOptions();
		optionsPane.visible = optionsPane.active = false;
		optionsPane.layout();
		optionsPane.setPos(heroBtnleft, 0);
		add(optionsPane);

		btnOptions = new IconButton(Icons.get(Icons.PREFS)){
			@Override
			protected void onClick() {
				super.onClick();
				optionsPane.visible = !optionsPane.visible;
				optionsPane.active = !optionsPane.active;
			}

			@Override
			protected void onPointerDown() {
				super.onPointerDown();
			}

			@Override
			protected void onPointerUp() {
				updateOptionsColor();
			}

			@Override
			protected String hoverText() {
				return Messages.get(HeroSelectScene.class, "options");
			}
		};
		btnOptions.setRect(heroBtnleft + 16, Camera.main.height-HeroBtn.HEIGHT-16, 20, 21);
		updateOptionsColor();
		btnOptions.visible = false;

		if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)){
			add(btnOptions);
		} else {
			Dungeon.challenges = 0;
			SPDSettings.challenges(0);
			SPDSettings.customSeed("");
		}

		btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		btnExit.visible = !SPDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;

		prompt = PixelScene.renderTextBlock(Messages.get(this, "title"), 12);
		prompt.hardlight(Window.TITLE_COLOR);
		prompt.setPos( (Camera.main.width - prompt.width())/2f, (Camera.main.height - HeroBtn.HEIGHT - prompt.height() - 4));
		PixelScene.align(prompt);
		add(prompt);

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

	private void updateOptionsColor(){
		if (!SPDSettings.customSeed().isEmpty()){
			btnOptions.icon().hardlight(1f, 1.5f, 0.67f);
		} else if (SPDSettings.challenges() != 0){
			btnOptions.icon().hardlight(2f, 1.33f, 0.5f);
		} else {
			btnOptions.icon().resetColor();
		}
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

		btnOptions.visible = true;
		btnOptions.setPos(startBtn.left()-btnOptions.width(), startBtn.top());

		optionsPane.setPos(optionsPane.left(), startBtn.top() - optionsPane.height() - 2);
		PixelScene.align(optionsPane);
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
			optionsPane.alpha(alpha);
			btnOptions.icon().alpha(alpha);
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

	private class GameOptions extends Component {

		private NinePatch bg;

		private ArrayList<StyledButton> buttons;
		private ArrayList<ColorBlock> spacers;

		@Override
		protected void createChildren() {

			bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
			add(bg);

			buttons = new ArrayList<>();
			spacers = new ArrayList<>();
			if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)){
				StyledButton seedButton = new StyledButton(Chrome.Type.BLANK, Messages.get(HeroSelectScene.class, "custom_seed"), 6){
					@Override
					protected void onClick() {
						String existingSeedtext = SPDSettings.customSeed();
						ShatteredPixelDungeon.scene().addToFront( new WndTextInput(Messages.get(HeroSelectScene.class, "custom_seed_title"),
								Messages.get(HeroSelectScene.class, "custom_seed_desc"),
								existingSeedtext,
								20,
								false,
								Messages.get(HeroSelectScene.class, "custom_seed_set"),
								Messages.get(HeroSelectScene.class, "custom_seed_clear")){
							@Override
							public void onSelect(boolean positive, String text) {
								text = DungeonSeed.formatText(text);
								long seed = DungeonSeed.convertFromText(text);

								if (positive && seed != -1){

									for (GamesInProgress.Info info : GamesInProgress.checkAll()){
										if (info.customSeed.isEmpty() && info.seed == seed){
											SPDSettings.customSeed("");
											icon.resetColor();
											ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(HeroSelectScene.class, "custom_seed_duplicate")));
											return;
										}
									}

									SPDSettings.customSeed(text);
									icon.hardlight(1f, 1.5f, 0.67f);
								} else {
									SPDSettings.customSeed("");
									icon.resetColor();
								}
								updateOptionsColor();
							}
						});
					}
				};
				seedButton.leftJustify = true;
				seedButton.icon(Icons.get(Icons.SEED));
				if (!SPDSettings.customSeed().isEmpty()) seedButton.icon().hardlight(1f, 1.5f, 0.67f);;
				buttons.add(seedButton);
				add(seedButton);

				StyledButton dailyButton = new StyledButton(Chrome.Type.BLANK, Messages.get(HeroSelectScene.class, "daily"), 6){

					private static final long SECOND = 1000;
					private static final long MINUTE = 60 * SECOND;
					private static final long HOUR = 60 * MINUTE;
					private static final long DAY = 24 * HOUR;

					@Override
					protected void onClick() {
						super.onClick();

						long diff = (SPDSettings.lastDaily() + DAY) - Game.realTime;
						if (diff > 0){
							if (diff > 30*HOUR){
								ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(HeroSelectScene.class, "daily_unavailable_long", (diff / DAY)+1)));
							} else {
								ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(HeroSelectScene.class, "daily_unavailable")));
							}
							return;
						}

						for (GamesInProgress.Info game : GamesInProgress.checkAll()){
							if (game.daily){
								ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(HeroSelectScene.class, "daily_existing")));
								return;
							}
						}

						Image icon = Icons.get(Icons.CALENDAR);
						icon.hardlight(0.5f, 1f, 2f);
						ShatteredPixelDungeon.scene().addToFront(new WndOptions(
								icon,
								Messages.get(HeroSelectScene.class, "daily"),
								Messages.get(HeroSelectScene.class, "daily_desc"),
								Messages.get(HeroSelectScene.class, "daily_yes"),
								Messages.get(HeroSelectScene.class, "daily_no")){
							@Override
							protected void onSelect(int index) {
								if (index == 0){
									long time = Game.realTime - (Game.realTime % DAY);

									//earliest possible daily for v1.3.0 is June 20 2022
									//which is 19,163 days after Jan 1 1970
									time = Math.max(time, 19_163 * DAY);

									SPDSettings.lastDaily(time);

									Dungeon.hero = null;
									Dungeon.daily = true;
									ActionIndicator.action = null;
									InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

									Game.switchScene( InterlevelScene.class );
								}
							}
						});
					}

					private long timeToUpdate = 0;

					@Override
					public void update() {
						super.update();

						if (Game.realTime > timeToUpdate){
							long diff = (SPDSettings.lastDaily() + DAY) - Game.realTime;
							if (diff > 0){
								//<1 minute
								if (diff < MINUTE){
									text(Messages.get(HeroSelectScene.class, "daily_seconds", (diff / SECOND)+1));
									timeToUpdate = Game.realTime + SECOND;
								//<1 hour
								} else if (diff < HOUR){
									text(Messages.get(HeroSelectScene.class, "daily_minutes", (diff / MINUTE)+1));
									timeToUpdate = Game.realTime + 5*SECOND;
								//<30 hours (a few extra in case of timezone shenanigans)
								} else if (diff < (DAY + 6*HOUR)) {
									text(Messages.get(HeroSelectScene.class, "daily_hours", (diff / HOUR)+1));
									timeToUpdate = Game.realTime + 5*MINUTE;
								//>30 hours, probably a cheater!
								} else {
									text(Messages.get(HeroSelectScene.class, "daily_30_hours"));
									timeToUpdate = Game.realTime + 20*MINUTE;
								}
								textColor(0x888888);
							} else {
								text(Messages.get(HeroSelectScene.class, "daily"));
								textColor(0xFFFFFF);
								timeToUpdate = Long.MAX_VALUE;
							}
						}

					}
				};
				dailyButton.leftJustify = true;
				dailyButton.icon(Icons.get(Icons.CALENDAR));
				add(dailyButton);
				buttons.add(dailyButton);

				StyledButton challengeButton = new StyledButton(Chrome.Type.BLANK, Messages.get(WndChallenges.class, "title"), 6){
					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront(new WndChallenges(SPDSettings.challenges(), true) {
							public void onBackPressed() {
								super.onBackPressed();
								icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
								updateOptionsColor();
							}
						} );
					}
				};
				challengeButton.leftJustify = true;
				challengeButton.icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
				add(challengeButton);
				buttons.add(challengeButton);
			}

			for (int i = 1; i < buttons.size(); i++){
				ColorBlock spc = new ColorBlock(1, 1, 0xFF000000);
				add(spc);
				spacers.add(spc);
			}
		}

		@Override
		protected void layout() {
			super.layout();

			bg.x = x;
			bg.y = y;

			int width = 0;
			for (StyledButton btn : buttons){
				if (width < btn.reqWidth()) width = (int)btn.reqWidth();
			}
			width += bg.marginHor();

			int top = (int)y + bg.marginTop() - 1;
			int i = 0;
			for (StyledButton btn : buttons){
				btn.setRect(x+bg.marginLeft(), top, width - bg.marginHor(), 16);
				top = (int)btn.bottom();
				if (i < spacers.size()) {
					spacers.get(i).size(btn.width(), 1);
					spacers.get(i).x = btn.left();
					spacers.get(i).y = PixelScene.align(btn.bottom()-0.5f);
					i++;
				}
			}

			this.width = width;
			this.height = top+bg.marginBottom()-y-1;
			bg.size(this.width, this.height);

		}

		private void alpha( float value ){
			bg.alpha(value);

			for (StyledButton btn : buttons){
				btn.alpha(value);
			}

			for (ColorBlock spc : spacers){
				spc.alpha(value);
			}
		}
	}

}
