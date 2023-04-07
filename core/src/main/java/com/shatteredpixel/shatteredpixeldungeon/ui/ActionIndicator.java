/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.input.GameAction;
import com.watabou.noosa.Image;

public class ActionIndicator extends Tag {

	Image icon;
	Image secondIcon;

	public static Action action;
	public static ActionIndicator instance;

	public ActionIndicator() {
		super( 0 );

		instance = this;

		setSize( SIZE, SIZE );
		visible = false;
	}
	
	@Override
	public GameAction keyAction() {
		return SPDAction.TAG_ACTION;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}
	
	@Override
	protected synchronized void layout() {
		super.layout();
		
		if (icon != null){
			if (!flipped)   icon.x = x + (SIZE - icon.width()) / 2f + 1;
			else            icon.x = x + width - (SIZE + icon.width()) / 2f - 1;
			icon.y = y + (height - icon.height()) / 2f;
			PixelScene.align(icon);
			if (secondIcon != null){
				secondIcon.x = icon.center().x + 8 - secondIcon.width();
				secondIcon.y = icon.y + icon.height() - secondIcon.height();
			}
		}
	}
	
	private boolean needsLayout = false;
	
	@Override
	public synchronized void update() {
		super.update();

		if (!Dungeon.hero.ready){
			if (icon != null) icon.alpha(0.5f);
			if (secondIcon != null) secondIcon.alpha(0.5f);
		} else {
			if (icon != null) icon.alpha(1f);
			if (secondIcon != null) secondIcon.alpha(1f);
		}

		if (!visible && action != null){
			visible = true;
			refresh();
			flash();
		} else {
			visible = action != null;
		}
		
		if (needsLayout){
			layout();
			needsLayout = false;
		}
	}

	@Override
	protected void onClick() {
		if (action != null && Dungeon.hero.ready) {
			action.doAction();
		}
	}

	@Override
	protected String hoverText() {
		String text = (action == null ? null : action.actionName());
		if (text != null){
			return Messages.titleCase(text);
		} else {
			return null;
		}
	}

	public static void setAction(Action action){
		ActionIndicator.action = action;
		refresh();
	}

	public static void clearAction(Action action){
		if (ActionIndicator.action == action) {
			ActionIndicator.action = null;
		}
	}

	public static void refresh(){
		if (instance != null){
			synchronized (instance) {
				if (instance.icon != null) {
					instance.icon.killAndErase();
					instance.icon = null;
				}
				if (instance.secondIcon != null){
					instance.secondIcon.killAndErase();
					instance.secondIcon = null;
				}
				if (action != null) {
					instance.icon = action.actionIcon();
					instance.add(instance.icon);

					Image secondIco = action.secondIcon();
					if (secondIco != null){
						instance.secondIcon = secondIco;
						instance.add(instance.secondIcon);
					}

					instance.needsLayout = true;
					instance.setColor(action.actionColor());
				}
			}
		}
	}

	public interface Action{

		public String actionName();

		public Image actionIcon();

		//TODO more variable than an icon maybe
		public Image secondIcon();

		public int actionColor();

		public void doAction();

	}

}
