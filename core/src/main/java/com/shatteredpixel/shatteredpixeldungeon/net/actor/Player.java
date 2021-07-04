/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.net.actor;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.net.sprites.PlayerSprite;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Player extends Mob {

	{
		spriteClass = PlayerSprite.class;
		EXP = 0;
		state = PASSIVE;
		alignment = Alignment.NEUTRAL;
	}

	public static final int POSITIVE	= 0x00FF00;

	private String socketid;
	private String nick;
	private int playerClass;

	public Player(String socketid, String nick, int playerClass){
		this.socketid = socketid;
		this.nick = nick;
		this.playerClass = playerClass;
	}

	public String socketid(){
		return this.socketid;
	}

	public String nick(){
		return this.nick;
	}

	public String name(){
		return this.nick;
	}
	public int playerClass(){
	    return this.playerClass;
	}

	public String info(){
		return "";
	}

	@Override
	protected boolean act() {
		return true;
	}

	@Override
	public void move(int step) {
		sprite.move(pos, step);
		pos = step;
		sprite.visible = Dungeon.level.heroFOV[pos];
	}

	@Override
	public void storeInBundle(Bundle bundle) {
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
	}

	@Override
	public void onMotionComplete() {
		super.onMotionComplete();
		sprite.idle();
	}

	public static Player getPlayer(String id){
		for (Mob mp : Dungeon.level.mobs){
			if(mp instanceof Player){
				Player p = (Player)mp;
				if(p.socketid().contains(id)) {
					return p;
				}
			}
		}
		return null;
	}


	public static Player getPlayerAtCell(int cell){
		for (Mob mp : Dungeon.level.mobs){
			if(mp instanceof Player){
				Player p = (Player)mp;
				if(p.pos == cell) {
					return p;
				}
			}
		}
		return null;
	}

	public static void addPlayer(String id, String nick, int playerClass, int pos){
		Player p = new Player(id, nick, playerClass);
		p.pos = pos;
		GameScene.add( p );
	}

	public static void movePlayer(Player p, int pos, int pc){
		if(p != null) {
			p.sprite.turnTo( p.pos, pos);
			p.sprite.move(p.pos, pos);
			p.move(pos);
		}
	}
}
