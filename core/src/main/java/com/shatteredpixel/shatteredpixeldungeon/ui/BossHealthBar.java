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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;

public class BossHealthBar extends Component {

	private Image bar;

	private Image rawShielding;
	private Image shieldedHP;
	private Image hp;

	private static Mob boss;

	private Image skull;
	private Emitter blood;

	private static String asset = Assets.Interfaces.BOSSHP;

	private static BossHealthBar instance;
	private static boolean bleeding;

	BossHealthBar() {
		super();
		visible = active = (boss != null);
		instance = this;
	}

	@Override
	protected void createChildren() {
		bar = new Image(asset, 0, 0, 64, 16);
		add(bar);

		width = bar.width;
		height = bar.height;

		rawShielding = new Image(asset, 15, 25, 47, 4);
		rawShielding.alpha(0.5f);
		add(rawShielding);

		shieldedHP = new Image(asset, 15, 25, 47, 4);
		add(shieldedHP);

		hp = new Image(asset, 15, 19, 47, 4);
		add(hp);

		skull = new Image(asset, 5, 18, 6, 6);
		add(skull);

		blood = new Emitter();
		blood.pos(skull);
		blood.pour(BloodParticle.FACTORY, 0.3f);
		blood.autoKill = false;
		blood.on = false;
		add( blood );
	}

	@Override
	protected void layout() {
		bar.x = x;
		bar.y = y;

		hp.x = shieldedHP.x = rawShielding.x = bar.x+15;
		hp.y = shieldedHP.y = rawShielding.y = bar.y+6;

		skull.x = bar.x+5;
		skull.y = bar.y+5;
	}

	@Override
	public void update() {
		super.update();
		if (boss != null){
			if (!boss.isAlive() || !Dungeon.level.mobs.contains(boss)){
				boss = null;
				visible = active = false;
			} else {

				float health = boss.HP;
				float shield = boss.shielding();
				float max = boss.HT;

				hp.scale.x = Math.max( 0, (health-shield)/max);
				shieldedHP.scale.x = health/max;
				rawShielding.scale.x = shield/max;

				if (hp.scale.x < 0.25f) bleed( true );

				if (bleeding != blood.on){
					if (bleeding)   skull.tint( 0xcc0000, 0.6f );
					else            skull.resetColor();
					blood.on = bleeding;
				}
			}
		}
	}

	public static void assignBoss(Mob boss){
		BossHealthBar.boss = boss;
		bleed(false);
		if (instance != null) {
			instance.visible = instance.active = true;
		}
	}
	
	public static boolean isAssigned(){
		return boss != null && boss.isAlive() && Dungeon.level.mobs.contains(boss);
	}

	public static void bleed(boolean value){
		bleeding = value;
	}

}
