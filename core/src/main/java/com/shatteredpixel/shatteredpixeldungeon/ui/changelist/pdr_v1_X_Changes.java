package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class pdr_v1_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v1_0_Changes(changeInfos);
	}

	public static void add_v1_0_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v1.1.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changes.addButton(new ChangeButton(Icons.get(Icons.BRIAN), Messages.get(ChangesScene.class, "new"),
				"_-_ PDR is now up-to-date with Shattered 1.1.2!\n" +
						"_-_ Bugfix: Bandit, Acidic Scorpio, Albino Rat now use correct beam-up sprite\n" +
						"_-_ Bugfix: Game Over scene has correct avatars\n" +
						"_-_ Assassins are now Ninjas\n" +
						"_-_ Corpse dust is now bone dust\n" +
						"_-_ Chalice of blood is now goblet of life\n" +
						"_-_ Goo (level 5 boss) is now Licorice Jello\n" +
						"_-_ Corruption is now Mesmerization\n"));
		changeInfos.add(changes);

		changes = new ChangeInfo("v1.0.1", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changes.addButton(new ChangeButton(Icons.get(Icons.BRIAN), Messages.get(ChangesScene.class, "new"),
						"_-_ New news feed! (Used to pull Shattered's news.)\n" +
						"_-_ Bugfix: Bandit now uses its own beam-up sprite\n" +
						"_-_ Bugfix: Game Over scene has correct avatars\n" +
						"_-_ Assassins are now Ninjas\n" +
						"_-_ Corpse dust is now bone dust\n" +
						"_-_ Corruption is now Mesmerization\n"));
		changeInfos.add(changes);

		changes = new ChangeInfo("v1.0.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changes.addButton(new ChangeButton(Icons.get(Icons.BRIAN), Messages.get(ChangesScene.class, "new"),
				"Up-to-date with ShatteredPD 1.0.3.\n\n" +
				"Pixel Dungeon Rescue changes:\n" +
				"_-_ You don't kill creatures, you cure them from their dark magic\n" +
				"_-_ All classes can be played as either male or female\n" +
				"_--_ (There are no gameplay differences between male and female)\n" +
				"_-_ Item and potion silliness - main ration is pizza, etc.\n" +
				"_-_ Slimes are now jellos\n" +
				"_-_ Bookshelves can be bashed and might drop scrolls or the spellbook\n" +
				"_-_ Slightly more generous food and healing potion drops\n\n" +
				"_-_ In debug builds, saved games are not deleted on defeat (to ease debugging)\n"));
		changeInfos.add(changes);
	}

}
