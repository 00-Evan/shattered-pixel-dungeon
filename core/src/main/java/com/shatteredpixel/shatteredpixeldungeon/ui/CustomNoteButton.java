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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournalItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

//this is contained in its own class as custom notes have a lot of messy window UI logic
public class CustomNoteButton extends IconButton {

	public CustomNoteButton () {
		super(Icons.PLUS.get());

		width = 11;
		height = 11;
	}

	@Override
	protected void onClick() {
		super.onClick();

		//TODO we want a selection window for note type here, atm it's only plaintext notes
		Notes.CustomRecord custom = new Notes.CustomRecord(Messages.get(this, "default_title_text"), "");
		Notes.add(custom);
		refreshScene(custom);
	}

	@Override
	protected String hoverText() {
		return Messages.get(this, "hover_text");
	}

	public static class CustomNoteWindow extends WndJournalItem {

		public CustomNoteWindow(Notes.CustomRecord rec) {
			super(rec.icon(), rec.title(), rec.desc());

			RedButton title = new RedButton( Messages.get(CustomNoteWindow.class, "edit_title") ){
				@Override
				protected void onClick() {
					GameScene.show(new WndTextInput(Messages.get(CustomNoteWindow.class, "edit_title"),
							"",
							rec.title(),
							50,
							false,
							Messages.get(CustomNoteWindow.class, "confirm"),
							Messages.get(CustomNoteWindow.class, "cancel")){
						@Override
						public void onSelect(boolean positive, String text) {
							if (positive){
								rec.editText(text, rec.desc());
								refreshScene(rec);
							}
						}
					});
				}
			};
			add(title);
			title.setRect(0, Math.min(height+2, PixelScene.uiCamera.height-50), width/2-1, 16);

			String editBodyText = rec.desc().isEmpty() ? Messages.get(CustomNoteWindow.class, "add_text") : Messages.get(CustomNoteWindow.class, "edit_text");
			RedButton body = new RedButton(editBodyText){
				@Override
				protected void onClick() {
					GameScene.show(new WndTextInput(editBodyText,
							"",
							rec.desc(),
							500,
							true,
							Messages.get(CustomNoteWindow.class, "confirm"),
							Messages.get(CustomNoteWindow.class, "cancel")){
						@Override
						public void onSelect(boolean positive, String text) {
							if (positive){
								rec.editText(rec.title(), text);
								refreshScene(rec);
							}
						}
					});
				}
			};
			add(body);
			body.setRect(title.right()+2, title.top(), width/2-1, 16);

			RedButton delete = new RedButton( Messages.get(CustomNoteWindow.class, "delete") ){
				@Override
				protected void onClick() {
					GameScene.show(new WndOptions(Icons.WARNING.get(),
							Messages.get(CustomNoteWindow.class, "delete"),
							Messages.get(CustomNoteWindow.class, "delete_warn"),
							Messages.get(CustomNoteWindow.class, "confirm"),
							Messages.get(CustomNoteWindow.class, "cancel")){
						@Override
						protected void onSelect(int index) {
							if (index == 0){
								Notes.remove(rec);
								refreshScene(null);
							}
						}
					});
				}
			};
			add(delete);
			delete.setRect(0, title.bottom()+1, width, 16);

			resize(width, (int)delete.bottom());
		}

	}

	private static void refreshScene(Notes.CustomRecord recToShow){
		if (recToShow == null){
			ShatteredPixelDungeon.seamlessResetScene();
		} else {
			ShatteredPixelDungeon.seamlessResetScene(new Game.SceneChangeCallback() {
				@Override
				public void beforeCreate() {

				}

				@Override
				public void afterCreate() {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							ShatteredPixelDungeon.scene().addToFront(new CustomNoteWindow(recToShow));
						}
					});
				}
			});
		}
	}
}
