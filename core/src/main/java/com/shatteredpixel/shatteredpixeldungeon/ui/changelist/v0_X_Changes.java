package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_X_Changes {
    public static void addAllChanges ( ArrayList<ChangeInfo> changeInfos ){
        add_v0_1_Changes(changeInfos);
    }

    public static void add_v0_1_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v0.1", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
                "_-_ Released MONTH DAY, 2020\n" +
                        "_-_ FIrst release!\n" +
                        "\n" +
                        "What is dev commentary?"));

        changes.addButton( new ChangeButton(new Image(Assets.WARRIOR, 0, 15, 12, 15), "New Leader class!",
                "The Leader is a new ally-centric class with a unique companion whom you must protect. " +
                        "She may be a burden in the beginning, but by the time you get to the Leader's subclasses you'll be glad you brought her along."));

        changes.addButton( new ChangeButton(new Image(Assets.SPECKS, 65, 0, 4, 5), "New music!",
                "A total of five new music tracks have been added: four dungeon tracks and a boss track. This means that each chapter now has its own theme."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight( CharSprite.WARNING );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
                "To help offset the massive file size increase brought by the new music, all existing compatibility code has been removed."));
    }
}
