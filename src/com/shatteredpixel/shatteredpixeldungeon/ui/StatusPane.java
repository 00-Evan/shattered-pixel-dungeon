/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHero;

public class StatusPane extends Component {
	
	private NinePatch shield;
	private Image avatar;
	private Emitter blood;
	
	private int lastTier = 0;
	
	private Image hp;
	private Image exp;
	
	private int lastLvl = -1;
	private int lastKeys = -1;
	
	private BitmapText level;
	private BitmapText depth;
	private BitmapText keys;
	
	private DangerIndicator danger;
	private LootIndicator loot;
	private BuffIndicator buffs;
	private Compass compass;
	
	@Override
	protected void createChildren() {
		
		shield = new NinePatch( Assets.STATUS, 80, 0, 30, 0 );
		add( shield );
		
		add( new TouchArea( 0, 1, 30, 30 ) {
			@Override
			protected void onClick( Touch touch ) {
				Image sprite = Dungeon.hero.sprite;
				if (!sprite.isVisible()) {
					Camera.main.focusOn( sprite );
				}
				GameScene.show( new WndHero() );
			};			
		} );
		
		avatar = HeroSprite.avatar( Dungeon.hero.heroClass, lastTier );
		add( avatar );
		
		blood = new Emitter();
		blood.pos( avatar );
		blood.pour( BloodParticle.FACTORY, 0.3f );
		blood.autoKill = false;
		blood.on = false;
		add( blood );
		
		compass = new Compass( Dungeon.level.exit );
		add( compass );
		
		hp = new Image( Assets.HP_BAR );	
		add( hp );
		
		exp = new Image( Assets.XP_BAR );
		add( exp );
		
		level = new BitmapText( PixelScene.font1x );
		level.hardlight( 0xFFEBA4 );
		add( level );
		
		depth = new BitmapText( Integer.toString( Dungeon.depth ), PixelScene.font1x );
		depth.hardlight( 0xCACFC2 );
		depth.measure();
		add( depth );
		
		Dungeon.hero.belongings.countIronKeys();
		keys = new BitmapText( PixelScene.font1x );
		keys.hardlight( 0xCACFC2 );
		add( keys );
		
		danger = new DangerIndicator();
		add( danger );
		
		loot = new LootIndicator();
		add( loot );
		
		buffs = new BuffIndicator( Dungeon.hero );
		add( buffs );
	}
	
	@Override
	protected void layout() {
		
		height = 32;
		
		shield.size( width, shield.height );
		
		avatar.x = PixelScene.align( camera(), shield.x + 15 - avatar.width / 2 );
		avatar.y = PixelScene.align( camera(), shield.y + 16 - avatar.height / 2 );
		
		compass.x = avatar.x + avatar.width / 2 - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2 - compass.origin.y;
		
		hp.x = 30;
		hp.y = 3;
		
		depth.x = width - 24 - depth.width();
		depth.y = 6;
		
		keys.y = 6;
		
		danger.setPos( width - danger.width(), 20 );
		
		loot.setPos( width - loot.width(),  danger.bottom() + 2 );
		
		buffs.setPos( 32, 11 );
	}
	
	@Override
	public void update() {
		super.update();
		
		float health = (float)Dungeon.hero.HP / Dungeon.hero.HT;
		
		if (health == 0) {
			avatar.tint( 0x000000, 0.6f );
			blood.on = false;
		} else if (health < 0.25f) {
			avatar.tint( 0xcc0000, 0.4f );
			blood.on = true;
		} else {
			avatar.resetColor();
			blood.on = false;
		}
		
		hp.scale.x = health;
		exp.scale.x = (width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();
		
		if (Dungeon.hero.lvl != lastLvl) {
			
			if (lastLvl != -1) {
				Emitter emitter = (Emitter)recycle( Emitter.class );
				emitter.revive();
				emitter.pos( 27, 27 );
				emitter.burst( Speck.factory( Speck.STAR ), 12 );
			}
			
			lastLvl = Dungeon.hero.lvl;
			level.text( Integer.toString( lastLvl ) );
			level.measure();
			level.x = PixelScene.align( 27.0f - level.width() / 2 );
			level.y = PixelScene.align( 27.5f - level.baseLine() / 2 );
		}
		
		int k = IronKey.curDepthQunatity;
		if (k != lastKeys) {
			lastKeys = k;
			keys.text( Integer.toString( lastKeys ) );
			keys.measure();
			keys.x = width - 8 - keys.width();
		}
		
		int tier = Dungeon.hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy( HeroSprite.avatar( Dungeon.hero.heroClass, tier ) );
		}
	}
}
