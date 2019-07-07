package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WardSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfWarding extends Wand {

	{
		collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;

		image = ItemSpriteSheet.WAND_WARDING;
	}

	@Override
	protected void onZap(Ballistica bolt) {

		int currentWardLevels = 0;
		for (Char ch : Actor.chars()){
			if (ch instanceof Ward){
				currentWardLevels += ((Ward) ch).tier;
			}
		}
		boolean canPlaceMore = currentWardLevels < level()+2;

		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null){
			if (ch instanceof Ward){
				if (canPlaceMore) {
					((Ward) ch).upgrade(level());
				} else {
					if (((Ward) ch).tier <= 3){
						GLog.w( Messages.get(this, "no_more_wards"));
					} else {
						((Ward) ch).wandHeal( level() );
					}
				}
				ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, ((Ward) ch).tier);
			} else {
				GLog.w( Messages.get(this, "bad_location"));
			}
		} else if (canPlaceWard(bolt.collisionPos)){
			if (canPlaceMore) {
				Ward ward = new Ward();
				ward.pos = bolt.collisionPos;
				ward.wandLevel = level();
				GameScene.add(ward, 1f);
				ward.sprite.emitter().burst(MagicMissile.WardParticle.UP, ward.tier);
				QuickSlotButton.target(ward);
			} else {
				GLog.w( Messages.get(this, "no_more_wards"));
			}
		} else {
			GLog.w( Messages.get(this, "bad_location"));
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.WARD,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//TODO
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		//TODO
		super.staffFx(particle);
	}

	public static boolean canPlaceWard(int pos){

		int adjacentBlockedCells = 0;
		int adjacentCellGroups = 0;
		boolean prevOpen = openCell(pos + PathFinder.CIRCLE8[PathFinder.CIRCLE8.length-1]);

		for (int i : PathFinder.CIRCLE8){
			if (!openCell(pos + i)){
				adjacentBlockedCells++;
			}
			if (prevOpen != openCell(pos + i)){
				prevOpen = !prevOpen;
				adjacentCellGroups++;
			}
		}

		switch (adjacentBlockedCells){
			case 0: case 1:
				return true;
			case 2:
				return (openCell(pos + PathFinder.CIRCLE4[0]) || openCell( pos + PathFinder.CIRCLE4[2]))
						&& (openCell(pos + PathFinder.CIRCLE4[1]) || openCell( pos + PathFinder.CIRCLE4[3]));
			case 3:
				return adjacentCellGroups <= 2;
			default:
				return false;
		}

	}

	private static boolean openCell(int pos){
		//a cell is considered blocked if it isn't passable or a ward is there
		return Dungeon.level.passable[pos] && !(Actor.findChar(pos) instanceof Ward);
	}

	public static class Ward extends NPC {

		private int tier = 1;
		private int wandLevel = 1;

		private int totalZaps = 0;

		{
			spriteClass = WardSprite.class;

			alignment = Alignment.ALLY;

			properties.add(Property.IMMOVABLE);

			viewDistance = 3;
			state = WANDERING;

			name = Messages.get(this, "name_" + tier );
		}

		public void upgrade( int wandLevel ){
			if (this.wandLevel < wandLevel){
				this.wandLevel = wandLevel;
			}

			wandHeal(0);

			switch (tier){
				case 1: case 2: default:
					break; //do nothing
				case 3:
					HP = HT = 30;
					break;
				case 4:
					HT = 48;
					HP = Math.round(48*(HP/30f));
					break;
				case 5:
					HT = 72;
					HP = Math.round(72*(HP/48f));
					break;
			}

			if (tier < 6){
				tier++;
				viewDistance++;
				name = Messages.get(this, "name_" + tier );
				updateSpriteState();
			}
		}

		private void wandHeal( int wandLevel ){
			if (this.wandLevel < wandLevel){
				this.wandLevel = wandLevel;
			}

			switch(tier){
				default:
					break;
				case 4:
					HP = Math.min(HT, HP+6);
					break;
				case 5:
					HP = Math.min(HT, HP+8);
					break;
				case 6:
					HP = Math.min(HT, HP+12);
					break;
			}
		}

		@Override
		public int defenseSkill(Char enemy) {
			if (tier > 3){
				defenseSkill = 4 + Dungeon.depth;
			}
			return super.defenseSkill(enemy);
		}

		@Override
		public int drRoll() {
			if (tier > 3){
				return Math.round(Random.NormalIntRange(0, 3 + Dungeon.depth/2) / (7f - tier));
			} else {
				return 0;
			}
		}

		@Override
		protected float attackDelay() {
			switch (tier){
				case 1: case 2: default:
					return 2f;
				case 3: case 4:
					return 1.5f;
				case 5: case 6:
					return 1f;
			}
		}

		@Override
		protected boolean canAttack( Char enemy ) {
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		}

		@Override
		protected boolean doAttack(Char enemy) {
			boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap( enemy.pos );
			} else {
				zap();
			}

			return !visible;
		}

		private void zap() {
			spend( 1f );

			//always hits
			int dmg = Random.Int( 2 + wandLevel, 8 + 4*wandLevel );
			enemy.damage( dmg, WandOfWarding.class );
			if (enemy.isAlive()){
				Wand.processSoulMark(enemy, wandLevel, 1);
			}

			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail( getClass() );
			}

			totalZaps++;
			switch(tier){
				default:
					if (totalZaps >= tier){
						die(this);
					}
					break;
				case 3:
					if (totalZaps >= 4){
						die(this);
					}
					break;
				case 4:
					damage(6, this);
					break;
				case 5:
					damage(8, this);
					break;
				case 6:
					damage(9, this);
					break;
			}
		}

		public void onZapComplete() {
			zap();
			next();
		}

		@Override
		protected boolean getCloser(int target) {
			return false;
		}

		@Override
		protected boolean getFurther(int target) {
			return false;
		}

		@Override
		public CharSprite sprite() {
			WardSprite sprite = (WardSprite) super.sprite();
			sprite.updateTier(tier);
			return sprite;
		}

		@Override
		public void updateSpriteState() {
			super.updateSpriteState();
			((WardSprite)sprite).updateTier(tier);
		}

		@Override
		public boolean canInteract(Hero h) {
			return true;
		}

		@Override
		public boolean interact() {
			GameScene.show(new WndOptions( Messages.get(this, "dismiss_title"),
					Messages.get(this, "dismiss_body"),
					Messages.get(this, "dismiss_confirm"),
					Messages.get(this, "dismiss_cancel") ){
				@Override
				protected void onSelect(int index) {
					if (index == 0){
						die(null);
					}
				}
			});
			return true;
		}

		@Override
		public String description() {
			return Messages.get(this, "desc_" + tier, 2+wandLevel, 8 + 4*wandLevel );
		}

		private static final String TIER = "tier";
		private static final String WAND_LEVEL = "wand_level";
		private static final String TOTAL_ZAPS = "total_zaps";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TIER, tier);
			bundle.put(WAND_LEVEL, wandLevel);
			bundle.put(TOTAL_ZAPS, totalZaps);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			tier = bundle.getInt(TIER);
			name = Messages.get(this, "name_" + tier );
			wandLevel = bundle.getInt(WAND_LEVEL);
			totalZaps = bundle.getInt(TOTAL_ZAPS);
		}
	}
}
