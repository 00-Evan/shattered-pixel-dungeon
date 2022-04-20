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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoTalent;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

import java.util.LinkedHashMap;

public class TalentButton extends Button {

	public static final int WIDTH = 20;
	public static final int HEIGHT = 26;

	int tier;
	Talent talent;
	int pointsInTalent;
	Mode mode;

	TalentIcon icon;
	Image bg;

	ColorBlock fill;

	public enum Mode {
		INFO,
		UPGRADE,
		METAMORPH_CHOOSE,
		METAMORPH_REPLACE
	}

	public TalentButton(int tier, Talent talent, int points, Mode mode){
		super();
		hotArea.blockLevel = PointerArea.NEVER_BLOCK;

		this.tier = tier;
		this.talent = talent;
		this.pointsInTalent = points;
		this.mode = mode;

		bg.frame(20*(talent.maxPoints()-1), 0, WIDTH, HEIGHT);

		icon = new TalentIcon( talent );
		add(icon);
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		fill = new ColorBlock(0, 4, 0xFFFFFF44);
		add(fill);

		bg = new Image(Assets.Interfaces.TALENT_BUTTON);
		add(bg);
	}

	@Override
	protected void layout() {
		width = WIDTH;
		height = HEIGHT;

		super.layout();

		fill.x = x+2;
		fill.y = y + WIDTH - 1;
		fill.size( pointsInTalent/(float)talent.maxPoints() * (WIDTH-4), 5);

		bg.x = x;
		bg.y = y;

		icon.x = x + 2;
		icon.y = y + 2;
		PixelScene.align(icon);
	}

	@Override
	protected void onClick() {
		super.onClick();

		Window toAdd;
		if (mode == Mode.UPGRADE
				&& Dungeon.hero != null
				&& Dungeon.hero.isAlive()
				&& Dungeon.hero.talentPointsAvailable(tier) > 0
				&& Dungeon.hero.pointsInTalent(talent) < talent.maxPoints()){
			toAdd = new WndInfoTalent(talent, pointsInTalent, new WndInfoTalent.TalentButtonCallback() {

				@Override
				public String prompt() {
					return Messages.titleCase(Messages.get(WndInfoTalent.class, "upgrade"));
				}

				@Override
				public void call() {
					upgradeTalent();
				}
			});
		} else if (mode == Mode.METAMORPH_CHOOSE && Dungeon.hero != null && Dungeon.hero.isAlive()) {
			toAdd = new WndInfoTalent(talent, pointsInTalent, new WndInfoTalent.TalentButtonCallback() {

				@Override
				public String prompt() {
					return Messages.titleCase(Messages.get(ScrollOfMetamorphosis.class, "metamorphose_talent"));
				}

				@Override
				public boolean metamorphDesc() {
					return true;
				}

				@Override
				public void call() {
					if (ScrollOfMetamorphosis.WndMetamorphChoose.INSTANCE != null){
						ScrollOfMetamorphosis.WndMetamorphChoose.INSTANCE.hide();
					}
					GameScene.show(new ScrollOfMetamorphosis.WndMetamorphReplace(talent, tier));
				}
			});
		} else if (mode == Mode.METAMORPH_REPLACE && Dungeon.hero != null && Dungeon.hero.isAlive()) {
			toAdd = new WndInfoTalent(talent, pointsInTalent, new WndInfoTalent.TalentButtonCallback() {

				@Override
				public String prompt() {
					return Messages.titleCase(Messages.get(ScrollOfMetamorphosis.class, "metamorphose_talent"));
				}

				@Override
				public boolean metamorphDesc() {
					return true;
				}

				@Override
				public void call() {
					Talent replacing = ScrollOfMetamorphosis.WndMetamorphReplace.INSTANCE.replacing;

					for (LinkedHashMap<Talent, Integer> tier : Dungeon.hero.talents){
						if (tier.containsKey(replacing)){
							LinkedHashMap<Talent, Integer> newTier = new LinkedHashMap<>();
							for (Talent t : tier.keySet()){
								if (t == replacing){
									newTier.put(talent, tier.get(replacing));

									if (!Dungeon.hero.metamorphedTalents.containsValue(replacing)){
										Dungeon.hero.metamorphedTalents.put(replacing, talent);

									//if what we're replacing is already a value, we need to simplify the data structure
									} else {
										//a->b->a, we can just remove the entry entirely
										if (Dungeon.hero.metamorphedTalents.get(talent) == replacing){
											Dungeon.hero.metamorphedTalents.remove(talent);

										//a->b->c, we need to simplify to a->c
										} else {
											for (Talent t2 : Dungeon.hero.metamorphedTalents.keySet()){
												if (Dungeon.hero.metamorphedTalents.get(t2) == replacing){
													Dungeon.hero.metamorphedTalents.put(t2, talent);
												}
											}
										}
									}

								} else {
									newTier.put(t, tier.get(t));
								}
							}
							Dungeon.hero.talents.set(ScrollOfMetamorphosis.WndMetamorphReplace.INSTANCE.tier-1, newTier);
							break;
						}
					}

					ScrollOfMetamorphosis.onMetamorph(replacing, talent);

					if (ScrollOfMetamorphosis.WndMetamorphReplace.INSTANCE != null){
						ScrollOfMetamorphosis.WndMetamorphReplace.INSTANCE.hide();
					}

				}
			});
		} else {
			toAdd = new WndInfoTalent(talent, pointsInTalent, null);
		}

		if (ShatteredPixelDungeon.scene() instanceof GameScene){
			GameScene.show(toAdd);
		} else {
			ShatteredPixelDungeon.scene().addToFront(toAdd);
		}
	}

	@Override
	protected void onPointerDown() {
		icon.brightness( 1.5f );
		bg.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.Sounds.CLICK );
	}

	@Override
	protected void onPointerUp() {
		icon.resetColor();
		bg.resetColor();
	}

	@Override
	protected String hoverText() {
		return Messages.titleCase(talent.title());
	}

	public void enable(boolean value ) {
		active = value;
		icon.alpha( value ? 1.0f : 0.3f );
		bg.alpha( value ? 1.0f : 0.3f );
	}

	public void upgradeTalent(){
		if (Dungeon.hero.talentPointsAvailable(tier) > 0 && parent != null) {
			Dungeon.hero.upgradeTalent(talent);
			float oldWidth = fill.width();
			pointsInTalent++;
			layout();
			Sample.INSTANCE.play(Assets.Sounds.LEVELUP, 0.7f, 1.2f);
			Emitter emitter = (Emitter) parent.recycle(Emitter.class);
			emitter.revive();
			emitter.pos(fill.x + (fill.width() + oldWidth) / 2f, fill.y + fill.height() / 2f);
			emitter.burst(Speck.factory(Speck.STAR), 12);
		}
	}
}
