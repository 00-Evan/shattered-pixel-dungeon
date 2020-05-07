/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Reflection;

import java.text.DecimalFormat;
import java.util.HashSet;

public class Buff extends Actor {
	
	public Char target;

	{
		actPriority = BUFF_PRIO; //low priority, towards the end of a turn
	}

	//determines how the buff is announced when it is shown.
	public enum buffType {POSITIVE, NEGATIVE, NEUTRAL}
	public buffType type = buffType.NEUTRAL;
	
	//whether or not the buff announces its name
	public boolean announced = false;
	
	protected HashSet<Class> resistances = new HashSet<>();
	
	public HashSet<Class> resistances() {
		return new HashSet<>(resistances);
	}
	
	protected HashSet<Class> immunities = new HashSet<>();
	
	public HashSet<Class> immunities() {
		return new HashSet<>(immunities);
	}
	
	public boolean attachTo( Char target ) {

		if (target.isImmune( getClass() )) {
			return false;
		}
		
		this.target = target;
		target.add( this );

		if (target.buffs().contains(this)){
			if (target.sprite != null) fx( true );
			return true;
		} else {
			this.target = null;
			return false;
		}
	}
	
	public void detach() {
		if (target.sprite != null) fx( false );
		target.remove( this );
	}
	
	@Override
	public boolean act() {
		diactivate();
		return true;
	}
	
	public int icon() {
		return BuffIndicator.NONE;
	}

	//some buffs may want to tint the base texture color of their icon
	public void tintIcon( Image icon ){
		//do nothing by default
	}

	//percent (0-1) to fade out out the buff icon, usually if buff is expiring
	public float iconFadePercent(){
		return 0;
	}

	//visual effect usually attached to the sprite of the character the buff is attacked to
	public void fx(boolean on) {
		//do nothing by default
	}

	public String heroMessage(){
		return null;
	}

	public String desc(){
		return "";
	}

	//to handle the common case of showing how many turns are remaining in a buff description.
	protected String dispTurns(float input){
		return new DecimalFormat("#.##").format(input);
	}

	//buffs act after the hero, so it is often useful to use cooldown+1 when display buff time remaining
	public float visualcooldown(){
		return cooldown()+1f;
	}

	//creates a fresh instance of the buff and attaches that, this allows duplication.
	public static<T extends Buff> T append( Char target, Class<T> buffClass ) {
		T buff = Reflection.newInstance(buffClass);
		buff.attachTo( target );
		return buff;
	}

	public static<T extends FlavourBuff> T append( Char target, Class<T> buffClass, float duration ) {
		T buff = append( target, buffClass );
		buff.spend( duration * target.resist(buffClass) );
		return buff;
	}

	//same as append, but prevents duplication.
	public static<T extends Buff> T affect( Char target, Class<T> buffClass ) {
		T buff = target.buff( buffClass );
		if (buff != null) {
			return buff;
		} else {
			return append( target, buffClass );
		}
	}
	
	public static<T extends FlavourBuff> T affect( Char target, Class<T> buffClass, float duration ) {
		T buff = affect( target, buffClass );
		buff.spend( duration * target.resist(buffClass) );
		return buff;
	}

	//postpones an already active buff, or creates & attaches a new buff and delays that.
	public static<T extends FlavourBuff> T prolong( Char target, Class<T> buffClass, float duration ) {
		T buff = affect( target, buffClass );
		buff.postpone( duration * target.resist(buffClass) );
		return buff;
	}
	
	public static void detach( Char target, Class<? extends Buff> cl ) {
		for ( Buff b : target.buffs( cl )){
			b.detach();
		}
	}
}
