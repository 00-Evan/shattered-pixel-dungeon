package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.AlphaTweener;

public class WardSprite extends MobSprite {

	private Animation tierIdles[] = new Animation[7];

	public WardSprite(){
		super();

		texture(Assets.WARDS);

		tierIdles[1] = new Animation( 1, true );
		tierIdles[1].frames(texture.uvRect(0, 0, 9, 10));

		tierIdles[2] = new Animation( 1, true );
		tierIdles[2].frames(texture.uvRect(10, 0, 21, 12));

		tierIdles[3] = new Animation( 1, true );
		tierIdles[3].frames(texture.uvRect(22, 0, 37, 16));

		tierIdles[4] = new Animation( 1, true );
		tierIdles[4].frames(texture.uvRect(38, 0, 44, 13));

		tierIdles[5] = new Animation( 1, true );
		tierIdles[5].frames(texture.uvRect(45, 0, 51, 15));

		tierIdles[6] = new Animation( 1, true );
		tierIdles[6].frames(texture.uvRect(52, 0, 60, 15));

	}

	@Override
	public void zap( int pos ) {
		idle();
		flash();
		emitter().burst(MagicMissile.WardParticle.UP, 2);
		if (Actor.findChar(pos) != null){
			parent.add(new Beam.DeathRay(center(), Actor.findChar(pos).sprite.center()));
		} else {
			parent.add(new Beam.DeathRay(center(), DungeonTilemap.raisedTileCenterToWorld(pos)));
		}
		((WandOfWarding.Ward)ch).onZapComplete();
	}
	
	@Override
	public void turnTo(int from, int to) {
		//do nothing
	}
	
	@Override
	public void die() {
		super.die();
		//cancels die animation and fades out immediately
		play(idle, true);
		emitter().burst(MagicMissile.WardParticle.UP, 10);
		parent.add( new AlphaTweener( this, 0, 2f ) {
			@Override
			protected void onComplete() {
				WardSprite.this.killAndErase();
				parent.erase( this );
			}
		} );
	}

	public void updateTier(int tier){

		idle = tierIdles[tier];
		run = idle.clone();
		attack = idle.clone();
		die = idle.clone();

		//always render first
		if (parent != null) {
			parent.sendToBack(this);
		}

		idle();

		if (tier <= 3){
			shadowWidth     = shadowHeight    = 1f;
			perspectiveRaise = (16 - height()) / 32f; //center of the cell
		} else {
			shadowWidth     = 1.2f;
			shadowHeight    = 0.25f;
			perspectiveRaise = 6 / 16f; //6 pixels
		}

		if (ch != null) {
			place(ch.pos);
		}
	}

	private float baseY = Float.NaN;

	@Override
	public void place(int cell) {
		super.place(cell);
		baseY = y;
	}

	@Override
	public void update() {
		super.update();
		//if tier is greater than 3
		if (perspectiveRaise >= 6 / 16f){
			if (Float.isNaN(baseY)) baseY = y;
			y = baseY + (float) Math.sin(Game.timeTotal);
			shadowOffset = 0.25f - 0.8f*(float) Math.sin(Game.timeTotal);
		}
	}

}
