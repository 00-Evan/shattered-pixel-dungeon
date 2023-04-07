package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.watabou.utils.Callback;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.watabou.noosa.Game;

public class NPCInteract extends NPC {
    public void tell( String text ) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndQuest( new Imp(), text ));
			}
		});
	}
}
