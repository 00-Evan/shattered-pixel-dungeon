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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.JournalScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PlatformSupport;

import java.io.InputStream;
import java.io.OutputStream;

public class WndAndroidLocalBackupRestore extends Window {

	protected static final int WIDTH_P = 120;
	protected static final int WIDTH_L = 144;

	protected static final int MARGIN           = 2;
	protected static final int BUTTON_HEIGHT    = 18;

	public WndAndroidLocalBackupRestore(){
		super();

		Rankings.INSTANCE.load();
		Badges.loadGlobal();
		Journal.loadGlobal();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		IconTitle tfTitle = new IconTitle(Icons.DATA.get(), Messages.get(this, "title"));
		tfTitle.setRect(0, 0, width, 0);
		add(tfTitle);

		float pos = tfTitle.bottom() + 2*MARGIN;

		//shorthand variable, used a lot of messages
		Class thisCls = WndAndroidLocalBackupRestore.class;

		final String backupFolder = Game.platform.getExternalDataFolder("");

		if (backupFolder == null){
			RenderedTextBlock tfMesage = PixelScene.renderTextBlock( 6 );
			tfMesage.text(Messages.get(this, "intro_body"), width);
			tfMesage.setPos( 0, pos );
			add( tfMesage );

			pos = tfMesage.bottom() + 2*MARGIN;

			RedButton btnCreateFolder = new RedButton(Messages.get(this, "assign_folder")){
				@Override
				protected void onClick() {
					Game.platform.createExternalDataFolder("Shattered Backups", new PlatformSupport.FolderCreationCallback() {
						@Override
						public void call(String foldername) {
							ShatteredPixelDungeon.runOnRenderThread(new Callback() {
								@Override
								public void call() {
									if (foldername != null) {
										WndAndroidLocalBackupRestore.this.hide();
										ShatteredPixelDungeon.scene().addToFront(new WndAndroidLocalBackupRestore());
									}
								}
							});
						}
					});
				}
			};
			btnCreateFolder.setRect(0, pos, width, BUTTON_HEIGHT);
			add( btnCreateFolder );

			pos += BUTTON_HEIGHT;

		} else {

			String folderName = backupFolder;
			//have to trim excess from the URI
			folderName = folderName.substring(folderName.indexOf("%3A")+3);
			folderName = folderName.replaceAll("%20", " ");

			String message = Messages.get(this, "main_body", folderName);

			RenderedTextBlock tfMesage = PixelScene.renderTextBlock( 6 );
			tfMesage.text(message, width);
			tfMesage.setPos( 0, pos );
			add( tfMesage );

			pos = tfMesage.bottom() + 2*MARGIN;

			RedButton btnBackup = new RedButton(Messages.get(this, "backup")){
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndHardNotification(
							Icons.INFO.get(),
							Messages.get(thisCls, "title"),
							Messages.get(thisCls, "backup_confirm_body"),
							Messages.get(thisCls, "backup"),
							3
							){

						@Override
						public void buttonPressed() {
							try (OutputStream out = Game.platform.writeExternalDataFile(backupFolder, "backup.dat")) {
								Rankings.INSTANCE.load();
								Badges.loadGlobal();
								Journal.loadGlobal();

								Bundle backup = new Bundle();
								if (FileUtils.fileExists(Rankings.RANKINGS_FILE))   backup.put(Rankings.RANKINGS_FILE, FileUtils.bundleFromFile(Rankings.RANKINGS_FILE));
								//no halls functionality in OSS but we check for the file anyway
								if (FileUtils.fileExists("hall.dat"))               backup.put("hall.dat", FileUtils.bundleFromFile("hall.dat"));
								if (FileUtils.fileExists(Badges.BADGES_FILE))       backup.put(Badges.BADGES_FILE, FileUtils.bundleFromFile(Badges.BADGES_FILE));
								if (FileUtils.fileExists(Journal.JOURNAL_FILE))     backup.put(Journal.JOURNAL_FILE, FileUtils.bundleFromFile(Journal.JOURNAL_FILE));
								backup.put("version", Game.versionCode);
								Bundle.write(backup, out);

								ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(Icons.INFO.get(),
										Messages.get(thisCls, "title"),
										Messages.get(thisCls, "backup_success")));
							} catch (Exception e){
								ShatteredPixelDungeon.scene().addToFront(new WndError(
										Messages.get(thisCls, "backup_failed")));
							}
						}

						@Override
						public void onBackPressed() {
							hide(); //always possible to back out
						}
					});
				}
			};
			btnBackup.setRect(0, pos, width, BUTTON_HEIGHT);
			add( btnBackup );

			pos += BUTTON_HEIGHT + MARGIN;

			RedButton btnRestore = new RedButton(Messages.get(this, "restore")){
				@Override
				protected void onClick() {
					try {
						final InputStream in = Game.platform.readExternalDataFile(backupFolder, "backup.dat");
						if (in == null){
							throw new RuntimeException();
						}
						final Bundle backup = Bundle.read(in);
						in.close();
						int backupVer = backup.getInt("version");
						boolean old = backupVer < ShatteredPixelDungeon.v3_1_1;

						ShatteredPixelDungeon.scene().addToFront(new WndHardNotification(
								Icons.WARNING.get(),
								Messages.get(thisCls, "title"),
								Messages.get(thisCls, "restore_confirm_body"),
								Messages.get(thisCls, "restore") + (old ? "\n\n" + Messages.get(thisCls, "restore_confirm_body_old") : "" ),
								3
						){

							@Override
							public void buttonPressed() {
								String failures = "";

								for (int i = 0; i < GamesInProgress.MAX_SLOTS; i++){
									if (GamesInProgress.gameExists(i)){
										Dungeon.deleteGame(i, true);
									}
								}

								try {
									Rankings.INSTANCE.unload();
									if (backup.contains(Rankings.RANKINGS_FILE)) {
										FileUtils.bundleToFile(Rankings.RANKINGS_FILE, backup.getBundle(Rankings.RANKINGS_FILE));
									} else {
										FileUtils.deleteFile(Rankings.RANKINGS_FILE);
									}
									Rankings.INSTANCE.load();
								} catch (Exception e){
									failures += " " + Messages.get(RankingsScene.class, "title");
								}

								try {
									if (backup.contains("hall.dat")) {
										FileUtils.bundleToFile("hall.dat", backup.getBundle("hall.dat"));
									} else {
										FileUtils.deleteFile("hall.dat");
									}
								} catch (Exception e){

								}

								try {
									Badges.unload();
									if (backup.contains(Badges.BADGES_FILE)) {
										FileUtils.bundleToFile(Badges.BADGES_FILE, backup.getBundle(Badges.BADGES_FILE));
									} else {
										FileUtils.deleteFile(Badges.BADGES_FILE);
									}
									Badges.loadGlobal();
								} catch (Exception e){
									failures += " " + Messages.get(WndJournal.BadgesTab.class, "title");
								}

								try {
									Journal.unload();
									if (backup.contains(Journal.JOURNAL_FILE)) {
										FileUtils.bundleToFile(Journal.JOURNAL_FILE, backup.getBundle(Journal.JOURNAL_FILE));
									} else {
										FileUtils.deleteFile(Journal.JOURNAL_FILE);
									}
									Journal.loadGlobal();
								} catch (Exception e){
									failures += Messages.get(JournalScene.class, "title");
								}

								String finalFailures = failures;
								ShatteredPixelDungeon.switchScene(TitleScene.class, new Game.SceneChangeCallback() {
									@Override
									public void beforeCreate() {

									}

									@Override
									public void afterCreate() {
										if (finalFailures.isEmpty()) {
											ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(
													Icons.INFO.get(),
													Messages.get(thisCls, "title"),
													Messages.get(thisCls, "restore_success")));
										} else {
											ShatteredPixelDungeon.scene().addToFront(new WndError(
													Messages.get(thisCls, "restore_fail" + finalFailures)));
										}
									}
								});

							}

							@Override
							public void onBackPressed() {
								hide(); //always possible to back out
							}
						});
					} catch (Exception e){
						ShatteredPixelDungeon.scene().addToFront(new WndError(
								Messages.get(thisCls, "restore_file_error")));
					}

				}
			};
			btnRestore.setRect(0, pos, width, BUTTON_HEIGHT);
			add( btnRestore );

			pos += BUTTON_HEIGHT;

		}

		resize( width, (int)pos );

	}

}
