/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant.Seed;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Heap implements Bundlable {

	private static final int SEEDS_TO_POTION = 3;
	
	public enum Type {
		HEAP,
		FOR_SALE,
		CHEST,
		LOCKED_CHEST,
		CRYSTAL_CHEST,
		TOMB,
		SKELETON,
		REMAINS,
		MIMIC
	}
	public Type type = Type.HEAP;
	
	public int pos = 0;
	
	public ItemSprite sprite;
	public boolean seen = false;
	
	public LinkedList<Item> items = new LinkedList<Item>();
	
	public int image() {
		switch (type) {
		case HEAP:
		case FOR_SALE:
			return size() > 0 ? items.peek().image() : 0;
		case CHEST:
		case MIMIC:
			return ItemSpriteSheet.CHEST;
		case LOCKED_CHEST:
			return ItemSpriteSheet.LOCKED_CHEST;
		case CRYSTAL_CHEST:
			return ItemSpriteSheet.CRYSTAL_CHEST;
		case TOMB:
			return ItemSpriteSheet.TOMB;
		case SKELETON:
			return ItemSpriteSheet.BONES;
		case REMAINS:
			return ItemSpriteSheet.REMAINS;
		default:
			return 0;
		}
	}
	
	public ItemSprite.Glowing glowing() {
		return (type == Type.HEAP || type == Type.FOR_SALE) && items.size() > 0 ? items.peek().glowing() : null;
	}
	
	public void open( Hero hero ) {
		switch (type) {
		case MIMIC:
			if (Mimic.spawnAt(pos, items) != null) {
				destroy();
			} else {
				type = Type.CHEST;
			}
			break;
		case TOMB:
			Wraith.spawnAround( hero.pos );
			break;
		case REMAINS:
		case SKELETON:
			CellEmitter.center( pos ).start(Speck.factory(Speck.RATTLE), 0.1f, 3);
			for (Item item : items) {
				if (item.cursed) {
					if (Wraith.spawnAt( pos ) == null) {
						hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
						hero.damage( hero.HP / 2, this );
					}
					Sample.INSTANCE.play( Assets.SND_CURSED );
					break;
				}
			}
			break;
		default:
		}

		if (type != Type.MIMIC) {
			type = Type.HEAP;
			sprite.link();
			sprite.drop();
		}
	}
	
	public int size() {
		return items.size();
	}
	
	public Item pickUp() {
		
		Item item = items.removeFirst();
		if (items.isEmpty()) {
			destroy();
		} else if (sprite != null) {
			sprite.view( image(), glowing() );
			sprite.place( pos );
		}
		
		return item;
	}
	
	public Item peek() {
		return items.peek();
	}
	
	public void drop( Item item ) {
		
		if (item.stackable && type != Type.FOR_SALE) {
			
			for (Item i : items) {
				if (i.isSimilar( item )) {
					i.quantity += item.quantity;
					item = i;
					break;
				}
			}
			items.remove( item );
			
		}
		
		if (item instanceof Dewdrop && type != Type.FOR_SALE) {
			items.add( item );
		} else {
			items.addFirst( item );
		}
		
		if (sprite != null) {
			if (type == Type.HEAP || type == Type.FOR_SALE)
				sprite.view( items.peek() );
			else
				sprite.view( image(), glowing() );
			sprite.place( pos );
		}
	}
	
	public void replace( Item a, Item b ) {
		int index = items.indexOf( a );
		if (index != -1) {
			items.remove( index );
			items.add( index, b );
		}
	}
	
	public void burn() {

		if (type == Type.MIMIC) {
			Mimic m = Mimic.spawnAt( pos, items );
			if (m != null) {
				Buff.affect( m, Burning.class ).reignite( m );
				m.sprite.emitter().burst( FlameParticle.FACTORY, 5 );
				destroy();
			}
		}

		if (type != Type.HEAP) {
			return;
		}
		
		boolean burnt = false;
		boolean evaporated = false;
		
		for (Item item : items.toArray( new Item[0] )) {
			if (item instanceof Scroll
					&& !(item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion)) {
				items.remove( item );
				burnt = true;
			} else if (item instanceof Dewdrop) {
				items.remove( item );
				evaporated = true;
			} else if (item instanceof MysteryMeat) {
				replace( item, ChargrilledMeat.cook( (MysteryMeat)item ) );
				burnt = true;
			} else if (item instanceof Bomb) {
				items.remove( item );
				((Bomb) item).explode( pos );
				//stop processing the burning, it will be replaced by the explosion.
				return;
			}
		}
		
		if (burnt || evaporated) {
			
			if (Dungeon.visible[pos]) {
				if (burnt) {
					burnFX( pos );
				} else {
					evaporateFX( pos );
				}
			}
			
			if (isEmpty()) {
				destroy();
			} else if (sprite != null) {
				sprite.view( items.peek() );
			}
			
		}
	}

	//Note: should not be called to initiate an explosion, but rather by an explosion that is happening.
	public void explode() {

		//breaks open most standard containers, mimics die.
		if (type == Type.MIMIC || type == Type.CHEST || type == Type.SKELETON) {
			type = Type.HEAP;
			sprite.link();
			sprite.drop();
			return;
		}

		if (type != Type.HEAP) {

			return;

		} else {

			for (Item item : items.toArray( new Item[0] )) {

				if (item instanceof Potion) {
					items.remove( item );
					((Potion) item).shatter(pos);

				} else if (item instanceof Bomb) {
					items.remove( item );
					((Bomb) item).explode(pos);
					//stop processing current explosion, it will be replaced by the new one.
					return;

				//unique and upgraded items can endure the blast
				} else if (!(item.level() > 0 || item.unique))
					items.remove( item );

			}

			if (isEmpty()){
				destroy();
			} else if (sprite != null) {
				sprite.view( items.peek() );
			}
		}
	}
	
	public void freeze() {

		if (type == Type.MIMIC) {
			Mimic m = Mimic.spawnAt( pos, items );
			if (m != null) {
				Buff.prolong( m, Frost.class, Frost.duration( m ) * Random.Float( 1.0f, 1.5f ) );
				destroy();
			}
		}

		if (type != Type.HEAP) {
			return;
		}
		
		boolean frozen = false;
		for (Item item : items.toArray( new Item[0] )) {
			if (item instanceof MysteryMeat) {
				replace( item, FrozenCarpaccio.cook( (MysteryMeat)item ) );
				frozen = true;
			} else if (item instanceof Potion
					&& !(item instanceof PotionOfStrength || item instanceof PotionOfMight)) {
				items.remove(item);
				((Potion) item).shatter(pos);
				frozen = true;
			} else if (item instanceof Bomb){
				((Bomb) item).fuse = null;
				frozen = true;
			}
		}
		
		if (frozen) {
			if (isEmpty()) {
				destroy();
			} else if (sprite != null) {
				sprite.view( items.peek() );
			}
		}
	}
	
	public Item transmute() {
		
		CellEmitter.get( pos ).burst( Speck.factory( Speck.BUBBLE ), 3 );
		Splash.at( pos, 0xFFFFFF, 3 );
		
		float chances[] = new float[items.size()];
		int count = 0;


		if (items.size() == 2 && items.get(0) instanceof Seed && items.get(1) instanceof Blandfruit ) {

			Sample.INSTANCE.play( Assets.SND_PUFF );
			CellEmitter.center( pos ).burst( Speck.factory( Speck.EVOKE ), 3 );

			Blandfruit result = new Blandfruit();
			result.cook((Seed)items.get(0));

			destroy();

			return result;

		}
		
		int index = 0;
		for (Item item : items) {
			if (item instanceof Seed) {
				count += item.quantity;
				chances[index++] = item.quantity;
			}  else{
				count = 0;
				break;
			}
		}

		//alchemists toolkit gives a chance to cook a potion in two or even one seeds
		AlchemistsToolkit.alchemy alchemy = Dungeon.hero.buff(AlchemistsToolkit.alchemy.class);
		int bonus = alchemy != null ? alchemy.itemLevel() : -1;

		if (bonus != -1 ? alchemy.tryCook(count) : count >= SEEDS_TO_POTION) {

			CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
			Sample.INSTANCE.play( Assets.SND_PUFF );

			Item potion;

			if (Random.Int( count + bonus ) == 0) {

				CellEmitter.center( pos ).burst( Speck.factory( Speck.EVOKE ), 3 );

				destroy();

				Statistics.potionsCooked++;
				Badges.validatePotionsCooked();

				potion = Generator.random( Generator.Category.POTION );

			} else {

				Seed proto = (Seed)items.get( Random.chances( chances ) );
				Class<? extends Item> itemClass = proto.alchemyClass;

				destroy();

				Statistics.potionsCooked++;
				Badges.validatePotionsCooked();

				if (itemClass == null) {
					potion =  Generator.random( Generator.Category.POTION );
				} else {
					try {
						potion =  itemClass.newInstance();
					} catch (Exception e) {
						ShatteredPixelDungeon.reportException(e);
						return null;
					}
				}
			}

			//not a buff per-se, meant to cancel out higher potion accuracy when ppl are farming for potions of exp.
			if (bonus > 0)
				if (Random.Int(1000/bonus) == 0)
					return new PotionOfExperience();

			while (potion instanceof PotionOfHealing && Random.Int(10) < Dungeon.limitedDrops.cookingHP.count)
				potion = Generator.random( Generator.Category.POTION );

			if (potion instanceof PotionOfHealing)
				Dungeon.limitedDrops.cookingHP.count++;

			return potion;

		} else {
			return null;
		}
	}
	
	public static void burnFX( int pos ) {
		CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
		Sample.INSTANCE.play( Assets.SND_BURNING );
	}
	
	public static void evaporateFX( int pos ) {
		CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 5 );
	}
	
	public boolean isEmpty() {
		return items == null || items.size() == 0;
	}
	
	public void destroy() {
		Dungeon.level.heaps.remove( this.pos );
		if (sprite != null) {
			sprite.kill();
		}
		items.clear();
		items = null;
	}

	@Override
	public String toString(){
		switch(type){
			case CHEST:
			case MIMIC:
				return Messages.get(this, "chest");
			case LOCKED_CHEST:
				return Messages.get(this, "locked_chest");
			case CRYSTAL_CHEST:
				return Messages.get(this, "crystal_chest");
			case TOMB:
				return Messages.get(this, "tomb");
			case SKELETON:
				return Messages.get(this, "skeleton");
			case REMAINS:
				return Messages.get(this, "remains");
			default:
				return peek().toString();
		}
	}

	public String info(){
		switch(type){
			case CHEST:
			case MIMIC:
				return Messages.get(this, "chest_desc");
			case LOCKED_CHEST:
				return Messages.get(this, "locked_chest_desc");
			case CRYSTAL_CHEST:
				if (peek() instanceof Artifact)
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "artifact") );
				else if (peek() instanceof Wand)
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "wand") );
				else
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "ring") );
			case TOMB:
				return Messages.get(this, "tomb_desc");
			case SKELETON:
				return Messages.get(this, "skeleton_desc");
			case REMAINS:
				return Messages.get(this, "remains_desc");
			default:
				return peek().info();
		}
	}

	private static final String POS		= "pos";
	private static final String SEEN	= "seen";
	private static final String TYPE	= "type";
	private static final String ITEMS	= "items";
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos = bundle.getInt( POS );
		seen = bundle.getBoolean( SEEN );
		type = Type.valueOf( bundle.getString( TYPE ) );
		items = new LinkedList<Item>( (Collection<Item>) ((Collection<?>) bundle.getCollection( ITEMS )) );
		items.removeAll(Collections.singleton(null));
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( POS, pos );
		bundle.put( SEEN, seen );
		bundle.put( TYPE, type.toString() );
		bundle.put( ITEMS, items );
	}
	
}
