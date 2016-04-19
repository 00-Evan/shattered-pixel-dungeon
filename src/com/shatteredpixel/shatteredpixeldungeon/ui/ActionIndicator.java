package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Image;

public class ActionIndicator extends Tag {

	Image icon;

	public static Action action;
	public static ActionIndicator instance;

	public ActionIndicator() {
		super( 0xFFFF4C );

		instance = this;

		setSize( 24, 24 );
		visible = false;
	}

	@Override
	public void destroy() {
		super.destroy();
		instance = null;
		action = null;
	}

	@Override
	protected void layout() {
		super.layout();

		if (icon != null){
			icon.x = x + (width - icon.width()) / 2;
			icon.y = y + (height - icon.height()) / 2;
			PixelScene.align(icon);
			if (!members.contains(icon))
				add(icon);
		}
	}

	@Override
	public void update() {
		super.update();

		if (!Dungeon.hero.ready){
			if (icon != null) icon.alpha(0.5f);
		} else {
			if (icon != null) icon.alpha(1f);
		}

		if (!visible && action != null){
			visible = true;
			updateIcon();
			flash();
		} else {
			visible = action != null;
		}
	}

	@Override
	protected void onClick() {
		if (action != null && Dungeon.hero.ready)
			action.doAction();
	}

	public static void setAction(Action action){
		ActionIndicator.action = action;
		updateIcon();
	}

	public static void clearAction(Action action){
		if (ActionIndicator.action == action)
			ActionIndicator.action = null;
	}

	public static void updateIcon(){
		if (instance != null){
			if (instance.icon != null){
				instance.icon.killAndErase();
				instance.icon = null;
			}
			if (action != null){
				instance.icon = action.getIcon();
				instance.layout();
			}
		}
	}

	public interface Action{

		public Image getIcon();

		public void doAction();

	}

}
