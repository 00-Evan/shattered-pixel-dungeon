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

package com.saqfish.spdnet.net.actor;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.mobs.Mob;
import com.saqfish.spdnet.effects.particles.EarthParticle;
import com.saqfish.spdnet.effects.particles.FlameParticle;
import com.saqfish.spdnet.effects.particles.SmokeParticle;
import com.saqfish.spdnet.net.events.send.action.items.NetItems;
import com.saqfish.spdnet.net.sprites.PlayerSprite;
import com.saqfish.spdnet.scenes.GameScene;
import com.watabou.utils.Bundle;

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
	private int depth;
	private NetItems items;

	public Player(String socketid, String nick, int playerClass, int depth, NetItems items){
		this.socketid = socketid;
		this.nick = nick;
		this.depth = depth;
		this.playerClass = playerClass;
		this.items = items;
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

	public void leave(){
	    destroy();
		if( sprite != null) {
			((PlayerSprite)sprite).leave();
			sprite.emitter().burst( SmokeParticle.FACTORY, 6 );
		}
	}

	public void join(){
		if( sprite != null) {
			((PlayerSprite)sprite).join();
			sprite.emitter().burst( SmokeParticle.FACTORY, 6 );
		}
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

	public int depth(){
		return this.depth;
	}

	public NetItems items(){
		return this.items;
	}

	public String info(){
		return "";
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

	public static void addPlayer(String id, String nick, int playerClass, int pos, int depth, NetItems items){
		Player p = new Player(id, nick, playerClass, depth, items);
		p.pos = pos;
		GameScene.add( p );
		p.join();
	}

	public static void movePlayer(Player p, int pos, int pc){
		if(p != null) {
			if(p.sprite != null) {
				p.sprite.turnTo(p.pos, pos);
				p.sprite.move(p.pos, pos);
			}
			p.move(pos);
		}
	}
}
