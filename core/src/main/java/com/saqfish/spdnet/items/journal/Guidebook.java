package com.saqfish.spdnet.items.journal;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.journal.Document;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.windows.WndJournal;
import com.saqfish.spdnet.windows.WndStory;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Guidebook extends Item {

	{
		image = ItemSpriteSheet.MASTERY;
	}

	@Override
	public final boolean doPickUp(Hero hero) {
		GameScene.pickUpJournal(this, hero.pos);
		String page = Document.GUIDE_INTRO;
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndStory(WndJournal.GuideTab.iconForPage(page),
						Document.ADVENTURERS_GUIDE.pageTitle(page),
						Document.ADVENTURERS_GUIDE.pageBody(page)){

					float elapsed = 0;

					@Override
					public void update() {
						elapsed += Game.elapsed;
						super.update();
					}

					@Override
					public void hide() {
						//prevents accidentally closing
						if (elapsed >= 1) {
							super.hide();
						}
					}
				});
			}
		});
		Document.ADVENTURERS_GUIDE.readPage(Document.GUIDE_INTRO);
		Sample.INSTANCE.play( Assets.Sounds.ITEM );
		hero.spendAndNext( TIME_TO_PICK_UP );
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

}
