/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VaultTokenDoorSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

//TODO might want to have an Interactable class for cases like this?
// would be weird for that to except Mob/NPC/Char but works well
public class VaultTokenDoor extends NPC {

	{
		spriteClass = VaultTokenDoorSprite.class;
	}

	@Override
	public boolean interact(Char c) {
		if (c instanceof Hero){
			Hero h = (Hero) c;

			Item tokens = h.belongings.getItem(DwarfToken.class);

			String descText = description();
			if (tokens == null){
				descText += "\n\n" + Messages.get(this, "no_tokens");
			} else if (tokens.quantity() < 10){
				descText += "\n\n" + Messages.get(this, "too_few_tokens");
			} else {
				descText += "\n\n" + Messages.get(this, "enough_tokens");
			}

			String finalDescText = descText;

			ShatteredPixelDungeon.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					if (tokens != null && tokens.quantity() >= 10) {
						GameScene.show(new WndOptions(sprite(),
								Messages.titleCase(name()),
								finalDescText,
								Messages.get(VaultTokenDoor.class, "open"),
								Messages.get(VaultTokenDoor.class, "not_yet")) {
							@Override
							protected void onSelect(int index) {
								super.onSelect(index);
								if (index == 0){
									c.sprite.operate(pos);
									Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
									Sample.INSTANCE.playDelayed(Assets.Sounds.UNLOCK, 0.25f);
									GLog.p(Messages.get(VaultTokenDoor.class, "unlocked"));
									VaultTokenDoor.this.destroy();
									ScrollOfMagicMapping.discover(pos);
									sprite.killAndErase();
									tokens.detachAll(h.belongings.backpack);
								}
							}
						});
					} else {
						GameScene.show(new WndTitledMessage(sprite(), name(), finalDescText));
					}
				}

			});
		}

		return false;
	}
}
