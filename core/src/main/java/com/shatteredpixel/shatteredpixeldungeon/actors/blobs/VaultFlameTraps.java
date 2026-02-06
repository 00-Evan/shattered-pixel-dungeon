package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

import java.util.Arrays;

//contains both blob logic and logic for seeding itself
public class VaultFlameTraps extends Blob {

	public int[] initialCooldowns;
	public int[] cooldowns;

	@Override
	public boolean act() {
		super.act();

		for (int i = 0; i < initialCooldowns.length; i++){
			if (initialCooldowns[i] > -1){
				if (cooldowns[i] <= 0){
					cooldowns[i] = initialCooldowns[i];
				}
				cooldowns[i]--;
				if (cooldowns[i] <= 0){
					seed(Dungeon.level, i, 1);
				}
			}
		}

		return true;
	}

	@Override
	protected void evolve() {
		int cell;

		for (int i = area.left; i < area.right; i++) {
			for (int j = area.top; j < area.bottom; j++) {
				cell = i + j* Dungeon.level.width();
				if (cur[cell] > 0) {

					//similar to fire.burn(), but Tengu is immune, and hero loses score
					Char ch = Actor.findChar( cell );
					if (ch == Dungeon.hero){
						Sample.INSTANCE.play(Assets.Sounds.BURNING);
						ch.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
					}
					/*if (ch != null && !ch.isImmune(Fire.class)) {
						Buff.affect( ch, Burning.class ).reignite( ch );
					}

					Heap heap = Dungeon.level.heaps.get( cell );
					if (heap != null) {
						heap.burn();
					}

					Plant plant = Dungeon.level.plants.get( cell );
					if (plant != null){
						plant.wither();
					}

					if (Dungeon.level.flamable[cell]){
						Dungeon.level.destroy( cell );

						GameScene.updateMap( cell );
					}*/

					if (Dungeon.level.heroFOV[cell]){
						CellEmitter.get(cell).start(ElmoParticle.FACTORY, 0.02f, 10);
					}

				}
			}
		}
	}

	public void seed(Level level, int cell, int amount ) {
		super.seed(level, cell, amount);
		if (initialCooldowns == null) {
			initialCooldowns = new int[level.length()];
			Arrays.fill(initialCooldowns, -1);
		}
		if (cooldowns == null){
			cooldowns = new int[level.length()];
		}
	}

	private static final String ONE	= "one";
	private static final String TWO	= "two";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ONE, initialCooldowns);
		bundle.put(TWO, cooldowns);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		initialCooldowns = bundle.getIntArray(ONE);
		cooldowns = bundle.getIntArray(TWO);
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.bound.set(0.4f, 0.4f, 0.6f, 0.6f);
		emitter.pour( ElmoParticle.FACTORY, 0.3f );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

}
