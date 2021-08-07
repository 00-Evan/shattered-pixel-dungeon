/*
 * Pixel Dungeon
 * Copyright (C) 2021 saqfish
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

import com.saqfish.spdnet.Badges;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.Statistics;
import com.saqfish.spdnet.actors.mobs.Mob;
import com.saqfish.spdnet.effects.particles.SmokeParticle;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.net.events.Receive;
import com.saqfish.spdnet.net.sprites.PlayerSprite;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.sprites.CharSprite;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

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
	private Receive.NetItems items;

	public Player(String socketid, String nick, int playerClass, int depth, Receive.NetItems items){
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

	@Override
	public void destroy() {

		super.destroy();
		Dungeon.level.players.remove( this );
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

	public Receive.NetItems items(){
		return this.items;
	}

	public String info(){
		return "";
	}

	public static Player getPlayer(String id){
		Player lp = null;
		for (Player p : Dungeon.level.players){
			if(p.socketid().equals(id)) {
				lp = p;
			}
		}
		return lp;
	}


	public static Player getPlayerAtCell(int cell){
		for (Mob mp : Dungeon.level.players){
			if(mp instanceof Player){
				Player p = (Player)mp;
				if(p.pos == cell) {
					return p;
				}
			}
		}
		return null;
	}

	public static void addPlayer(String id, String nick, int playerClass, int pos, int depth, Receive.NetItems items){
		Player p = new Player(id, nick, playerClass, depth, items);
		p.pos = pos;
		if(Dungeon.level.players != null) {
			for (Player op : Dungeon.level.players){
				if(op.socketid().equals(id)) {
					op.sprite.destroy();
					op.destroy();
				}
			}
			GameScene.add(p);
			p.join();
		}
	}

	public static void movePlayer(Player p, int pos, int pc){
		if(p != null && p.sprite != null) {
		    if(p.sprite.parent == null){
		    	p.sprite.destroy();
		    	GameScene.addSprite(p);
			}
			p.move(pos);
		}
	}
}
