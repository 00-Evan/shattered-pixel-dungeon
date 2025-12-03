/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.GuidingLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.FerretTuft;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FloatingText extends RenderedTextBlock {

	private static final float LIFESPAN	= 1f;
	private static final float DISTANCE	= DungeonTilemap.SIZE;

	public static final int ICON_WIDTH = 7;
	public static final int ICON_HEIGHT = 8;
	public static TextureFilm iconFilm = new TextureFilm( Assets.Effects.TEXT_ICONS, ICON_WIDTH, ICON_HEIGHT );

	public static int NO_ICON   = -1;

	//combat damage icons
	public static int PHYS_DMG          = 0;
	public static int PHYS_DMG_NO_BLOCK = 1;
	public static int MAGIC_DMG         = 2;
	public static int PICK_DMG          = 3;

	//debuff/dot damage icons
	public static int HUNGER    = 5;
	public static int BURNING   = 6;
	public static int SHOCKING  = 7;
	public static int FROST     = 8;
	public static int WATER     = 9;
	public static int BLEEDING  = 10;
	public static int TOXIC     = 11;
	public static int CORROSION = 12;
	public static int POISON    = 13;
	public static int OOZE      = 14;
	public static int DEFERRED  = 15;
	public static int CORRUPTION= 16;
	public static int AMULET    = 17;

	//positive icons
	public static int HEALING   = 18;
	public static int SHIELDING = 19;
	public static int EXPERIENCE= 20;
	public static int STRENGTH  = 21;

	//currency icons
	public static int GOLD      = 23;
	public static int ENERGY    = 24;

	//hit reason icons
	public static int HIT_WEP   = 36;
	public static int HIT_ARM   = 37;
	public static int HIT_BLS   = 38;
	public static int HIT_HEX   = 39;
	public static int HIT_DAZE  = 40;
	public static int HIT_ACC   = 41;
	public static int HIT_EVA   = 42;
	public static int HIT_LIQ   = 43;
	public static int HIT_DANCE = 44;
	public static int HIT_SUPR  = 45;
	public static int HIT_PRES  = 46;
	public static int HIT_MOMEN = 47;

	//extra row for hit icons that are armor-piercing

	//miss reason icons
	public static int MISS_WEP  = 72;
	public static int MISS_ARM  = 73;
	public static int MISS_BLS  = 74;
	public static int MISS_HEX  = 75;
	public static int MISS_DAZE = 76;
	public static int MISS_ACC  = 77;
	public static int MISS_EVA  = 78;
	public static int MISS_LIQ  = 79;
	public static int MISS_DEF  = 80;
	public static int MISS_TUFT = 81;
	public static int MISS_RUN  = 82;

	private Image icon;
	private boolean iconLeft;

	private float timeLeft;
	
	private int key = -1;

	private static final SparseArray<ArrayList<FloatingText>> stacks = new SparseArray<>();
	
	public FloatingText() {
		super(9*PixelScene.defaultZoom);
		setHightlighting(false);
	}
	
	@Override
	public void update() {
		super.update();
		
		if (timeLeft >= 0) {
			if ((timeLeft -= Game.elapsed) <= 0) {
				kill();
			} else {
				float p = timeLeft / LIFESPAN;
				alpha( p > 0.5f ? 1 : p * 2 );
				
				float yMove = (DISTANCE / LIFESPAN) * Game.elapsed;
				y -= yMove;
				for (RenderedText t : words){
					t.y -= yMove;
				}

				if (icon != null){
					icon.alpha(p > 0.5f ? 1 : p * 2);
					icon.y -= yMove;
				}
			}
		}
	}

	@Override
	protected synchronized void layout() {
		super.layout();
		if (icon != null){
			if (iconLeft){
				icon.x = left();
			} else {
				icon.x = left() + width() - icon.width();
			}
			icon.x = PixelScene.align(Camera.main, icon.x);
			icon.y = PixelScene.align(Camera.main, top());
		}
	}

	@Override
	public float width() {
		float width = super.width();
		if (icon != null){
			width += icon.width()-0.5f;
		}
		return width;
	}

	@Override
	public void kill() {
		if (key != -1) {
			synchronized (stacks) {
				stacks.get(key).remove(this);
			}
			key = -1;
		}
		super.kill();
	}
	
	@Override
	public void destroy() {
		kill();
		super.destroy();
	}
	
	public void reset( float x, float y, String text, int color, int iconIdx, boolean left ) {
		
		revive();
		
		zoom( 1 / (float)PixelScene.defaultZoom );

		text( text );
		hardlight( color );

		if (iconIdx != NO_ICON){
			icon = new Image( Assets.Effects.TEXT_ICONS);
			icon.frame(iconFilm.get(iconIdx));
			add(icon);
			iconLeft = left;
			if (iconLeft){
				align(RIGHT_ALIGN);
			}
		} else {
			icon = null;
		}

		setPos(
			PixelScene.align( Camera.main, x - width() / 2),
			PixelScene.align( Camera.main, y - height())
		);
		
		timeLeft = LIFESPAN;
	}
	
	/* STATIC METHODS */

	public static void show( float x, float y, String text, int color) {
		show(x, y, -1, text, color, -1, false);
	}
	
	public static void show( float x, float y, int key, String text, int color) {
		show(x, y, key, text, color, -1, false);
	}
	
	public static void show( float x, float y, int key, String text, int color, int iconIdx, boolean left ) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				FloatingText txt = GameScene.status();
				if (txt != null){
					txt.reset(x, y, text, color, iconIdx, left);
					if (key != -1) push(txt, key);
				}
			}
		});
	}
	
	private static void push( FloatingText txt, int key ) {
		
		synchronized (stacks) {
			txt.key = key;
			
			ArrayList<FloatingText> stack = stacks.get(key);
			if (stack == null) {
				stack = new ArrayList<>();
				stacks.put(key, stack);
			}
			
			if (stack.size() > 0) {
				FloatingText below = txt;
				int aboveIndex = stack.size() - 1;
				int numBelow = 0;
				while (aboveIndex >= 0) {
					numBelow++;
					FloatingText above = stack.get(aboveIndex);
					if (above.bottom() + 4 > below.top()) {
						above.setPos(above.left(), below.top() - above.height() - 4);

						//reduce remaining time on texts being nudged up, to prevent spam
						above.timeLeft = Math.min(above.timeLeft, LIFESPAN-(numBelow/5f));
						above.timeLeft = Math.max(above.timeLeft, 0);
						
						below = above;
						aboveIndex--;
					} else {
						break;
					}
				}
			}
			
			stack.add(txt);
		}
	}

	// *** These are for custom icons that show impact of effects on top of base acc/eve ***
	// This logic is honestly a mess. Rather than tracking these modifiers as we
	// perform acc/eva calculation, we check and 'unwind' the modifiers from the final rolls.
	// This results in a lot of duplicated logic/numbers and awkward workarounds.
	// It does work though... mostly.

	public static int getHitReasonIcon(Char attacker, float accRoll, Char defender, float defRoll){
		HashMap<Integer, Float> hitReasons = new HashMap<>();

		//go through some garunteed hit interactions first
		if (defRoll == 0 && defender.buff(GuidingLight.Illuminated.class) != null){
			return HIT_BLS;
		}
		if (accRoll == Char.INFINITE_ACCURACY && attacker.invisible > 0){
			return HIT_SUPR;
		}
		if (defRoll == 0 && defender instanceof Mob && ((Mob) defender).surprisedBy(attacker)){
			return HIT_SUPR;
		}
		if (accRoll == Char.INFINITE_ACCURACY
				&& attacker.buff(Talent.PreciseAssaultTracker.class) != null){
			return HIT_PRES;
		}
		if (accRoll == Char.INFINITE_ACCURACY
				&& attacker.buff(Talent.LiquidAgilACCTracker.class) != null){
			return HIT_LIQ;
		}

		KindOfWeapon wep = null;
		if (attacker instanceof Hero) wep = ((Hero) attacker).belongings.attackingWeapon();
		if (attacker instanceof MirrorImage) wep = Dungeon.hero.belongings.weapon();
		if (attacker instanceof Statue) wep = ((Statue)attacker).weapon();
		if (attacker instanceof DriedRose.GhostHero) wep = ((DriedRose.GhostHero)attacker).weapon();

		Armor arm = null;
		if (defender instanceof Hero) arm = ((Hero) defender).belongings.armor();
		if (defender instanceof PrismaticImage) arm = Dungeon.hero.belongings.armor();
		if (defender instanceof ArmoredStatue) arm = ((ArmoredStatue)defender).armor();
		if (defender instanceof DriedRose.GhostHero) arm = ((DriedRose.GhostHero)defender).armor();

		//one last check
		if (defRoll == 0 && arm != null && arm.hasGlyph(Stone.class, defender)){
			return HIT_ARM;
		}

		//accuracy boosts (always > 1)
		if (wep != null && wep.accuracyFactor(attacker, defender) > 1){
			hitReasons.put( HIT_WEP, wep.accuracyFactor(attacker, defender));
		}
		float blessBoost = 1; //a few different sources contribute to this icon
		if (attacker.buff(ChampionEnemy.class) != null
			&& attacker.buff(ChampionEnemy.class).evasionAndAccuracyFactor() > 1){
			blessBoost *= attacker.buff(ChampionEnemy.class).evasionAndAccuracyFactor();
		}
		if (attacker.buff(Bless.class) != null) blessBoost *= 1.25f;
		if (Dungeon.hero.heroClass != HeroClass.CLERIC
				&& Dungeon.hero.hasTalent(Talent.BLESS)
				&& attacker.alignment == Char.Alignment.ALLY){
			// + 3%/5%
			blessBoost *= 1.01f + 0.02f*Dungeon.hero.pointsInTalent(Talent.BLESS);
		}
		if (blessBoost > 1f) hitReasons.put(HIT_BLS, blessBoost);
		if (RingOfAccuracy.accuracyMultiplier(attacker) > 1)    hitReasons.put(HIT_ACC, RingOfAccuracy.accuracyMultiplier(attacker));
		if (attacker.buff(Scimitar.SwordDance.class) != null)   hitReasons.put(HIT_DANCE, 1.5f);
		if (!(wep instanceof MissileWeapon)) {
			if (attacker instanceof Hero && ((Hero) attacker).hasTalent(Talent.PRECISE_ASSAULT) && ((Hero) attacker).heroClass != HeroClass.DUELIST){
				hitReasons.put(HIT_PRES, 0.1f * Dungeon.hero.pointsInTalent(Talent.PRECISE_ASSAULT));
			}
			if (attacker.buff(Talent.PreciseAssaultTracker.class) != null){
				hitReasons.put(HIT_PRES, Dungeon.hero.pointsInTalent(Talent.PRECISE_ASSAULT) == 2 ? 5f : 2f);
			} else if (attacker.buff(Talent.LiquidAgilACCTracker.class) != null) {
				hitReasons.put(HIT_LIQ, 3f);
			}
		} else {
			if (attacker.buff(Momentum.class) != null
					&& attacker.buff(Momentum.class).freerunning()
					&& ((Hero)attacker).hasTalent(Talent.PROJECTILE_MOMENTUM)) {
				hitReasons.put(HIT_MOMEN, 1f + ((Hero) attacker).pointsInTalent(Talent.PROJECTILE_MOMENTUM) / 2f);
			}
		}

		//evasion reductions (always < 1)
		if (defender.buff(Hex.class) != null)                   hitReasons.put(HIT_HEX, 0.8f);
		if (defender.buff(Daze.class) != null)                  hitReasons.put(HIT_DAZE, 0.5f);
		if (RingOfEvasion.evasionMultiplier(defender) < 1)      hitReasons.put(HIT_EVA, RingOfEvasion.evasionMultiplier(defender));
		if (arm != null && arm.evasionFactor(defender, 100) < 100) {
			//we express armor's normally flat evasion boost as a %, yes this is very awkward
			Armor.testingNoArmDefSkill = true;
			int baseDef = defender.defenseSkill(attacker);
			Armor.testingNoArmDefSkill = false;
			hitReasons.put(HIT_ARM, defender.defenseSkill(attacker)/(float)baseDef);
		}
		//hero specifically gets 1/2 eva when stunned, for mobs its a garunteed hit
		if (defender.paralysed > 0)  {
			if (defender instanceof Hero)   hitReasons.put(HIT_SUPR, 0.5f);
			else                            return HIT_SUPR;
		}

		//sort from largest modifier to smallest one
		ArrayList<Integer> sortedReasons = new ArrayList<>(hitReasons.keySet());
		Collections.sort(sortedReasons, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				float a1 = hitReasons.get(a) >= 1f ? hitReasons.get(a) : 1 / hitReasons.get(a);
				float b1 = hitReasons.get(b) >= 1f ? hitReasons.get(b) : 1 / hitReasons.get(b);
				return (int)Math.signum(b1 - a1);
			}
		});

		for (Integer reason : sortedReasons){
			if (hitReasons.get(reason) >= 1f) {
				accRoll /= hitReasons.get(reason);
			} else {
				defRoll /= hitReasons.get(reason);
			}
			if (accRoll < defRoll){
				return reason;
			}
		}
		return -1;
	}

	public static int getMissReasonIcon(Char attacker, float accRoll, Char defender, float defRoll){
		HashMap<Integer, Float> missReasons = new HashMap<>();

		//some guaranteed dodged first
		if (defRoll == Char.INFINITE_EVASION && defender.buff(Talent.LiquidAgilEVATracker.class) != null){
			return MISS_LIQ;
		}

		KindOfWeapon wep = null;
		if (attacker instanceof Hero) wep = ((Hero) attacker).belongings.attackingWeapon();
		if (attacker instanceof MirrorImage) wep = Dungeon.hero.belongings.weapon();
		if (attacker instanceof Statue) wep = ((Statue)attacker).weapon();
		if (attacker instanceof DriedRose.GhostHero) wep = ((DriedRose.GhostHero)attacker).weapon();

		Armor arm = null;
		if (defender instanceof Hero) arm = ((Hero) defender).belongings.armor();
		if (defender instanceof PrismaticImage) arm = Dungeon.hero.belongings.armor();
		if (defender instanceof ArmoredStatue) arm = ((ArmoredStatue)defender).armor();
		if (defender instanceof DriedRose.GhostHero) arm = ((DriedRose.GhostHero)defender).armor();

		//evasion boosts (always > 1)
		float blessBoost = 1; //a few different sources contribute to this icon
		if (defender.buff(ChampionEnemy.class) != null
				&& defender.buff(ChampionEnemy.class).evasionAndAccuracyFactor() > 1){
			blessBoost *= defender.buff(ChampionEnemy.class).evasionAndAccuracyFactor();
		}
		if (defender.buff(Bless.class) != null) blessBoost *= 1.25f;
		if (Dungeon.hero.heroClass != HeroClass.CLERIC
				&& Dungeon.hero.hasTalent(Talent.BLESS)
				&& defender.alignment == Char.Alignment.ALLY){
			// + 3%/5%
			blessBoost *= 1.01f + 0.02f*Dungeon.hero.pointsInTalent(Talent.BLESS);
		}
		if (blessBoost > 1f)                                    missReasons.put(MISS_BLS, blessBoost);
		if (FerretTuft.evasionMultiplier() > 1)                 missReasons.put(MISS_TUFT, FerretTuft.evasionMultiplier());
		if (RingOfEvasion.evasionMultiplier(defender) > 1)      missReasons.put(MISS_EVA, RingOfEvasion.evasionMultiplier(defender));
		if (defender.buff(Quarterstaff.DefensiveStance.class) != null)  missReasons.put(MISS_DEF, 3f);
		if (arm != null && arm.evasionFactor(defender, 100) > 100) {
			//we express armor's normally flat evasion boost as a %, yes this is very awkward
			Armor.testingNoArmDefSkill = true;
			int baseDef = defender.defenseSkill(attacker);
			Armor.testingNoArmDefSkill = false;
			if (defender.buff(Momentum.class) != null){
				//this is cheating a little, as evasion aug gets wrapped into this too
				missReasons.put(MISS_RUN, defender.defenseSkill(attacker) / (float) baseDef);
			} else {
				missReasons.put(MISS_ARM, defender.defenseSkill(attacker) / (float) baseDef);
			}
		}
		if (defender.buff(Talent.LiquidAgilEVATracker.class) != null)   missReasons.put(MISS_LIQ, 3f);

		//accuracy reductions (always < 1)
		if (wep != null && wep.accuracyFactor(attacker, defender) < 1){
			missReasons.put( MISS_WEP, wep.accuracyFactor(attacker, defender));
		}
		if (attacker.buff(Hex.class) != null)                   missReasons.put(MISS_HEX, 0.8f);
		if (attacker.buff(Daze.class) != null)                  missReasons.put(MISS_DAZE, 0.5f);
		if (RingOfAccuracy.accuracyMultiplier(attacker) < 1)    missReasons.put(MISS_ACC, RingOfAccuracy.accuracyMultiplier(attacker));

		//sort from largest modifier to smallest one
		ArrayList<Integer> sortedReasons = new ArrayList<>(missReasons.keySet());
		Collections.sort(sortedReasons, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				float a1 = missReasons.get(a) >= 1f ? missReasons.get(a) : 1 / missReasons.get(a);
				float b1 = missReasons.get(b) >= 1f ? missReasons.get(b) : 1 / missReasons.get(b);
				return (int)Math.signum(b1 - a1);
			}
		});

		for (Integer reason : sortedReasons){
			if (missReasons.get(reason) >= 1f) {
				defRoll /= missReasons.get(reason);
			} else {
				accRoll /= missReasons.get(reason);
			}
			if (defRoll < accRoll){
				return reason;
			}
		}
		return -1;
	}
}
