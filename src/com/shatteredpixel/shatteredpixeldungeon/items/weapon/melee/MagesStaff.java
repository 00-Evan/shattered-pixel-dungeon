package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by debenhame on 13/03/2015.
 */
//TODO: lots of refinements to make here still.
public class MagesStaff extends MeleeWeapon {

	private Wand wand;

	public static final String AC_IMBUE = "IMBUE";
	public static final String AC_ZAP	= "ZAP";

	private static final String TXT_SELECT_WAND	= "Select a wand to consume";

	//TODO: decide on balancing
	private static final float STAFF_SCALE_FACTOR = 0.75f;

	{
		name = "Mage's Staff";
		image = ItemSpriteSheet.MAGES_STAFF;

		defaultAction = AC_ZAP;

		bones = false;
	}

	public MagesStaff() {

		//tier 1 weapon with really poor base stats.
		super(1, 1f, 1f);
		MIN = 1;
		MAX = 4;

		wand = null;
	}

	public MagesStaff(Wand wand){
		this();
        wand.identify();
        wand.cursed = false;
		this.wand = wand;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_IMBUE);
		if (wand!= null && wand.curCharges > 0) {
			actions.add( AC_ZAP );
		}
		return actions;
	}

	@Override
	public void activate( Hero hero ) {
		if(wand != null) wand.charge( hero, STAFF_SCALE_FACTOR );
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_IMBUE)) {

			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.WAND, TXT_SELECT_WAND);

		} else if (action.equals(AC_ZAP)){
			if (wand == null)
				return;

			wand.execute(hero, AC_ZAP);
		} else
			super.execute(hero, action);
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		super.proc(attacker, defender, damage);
		if (wand != null && Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE)
			wand.onHit( this, attacker, defender, damage );
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			if (container.owner != null && wand != null) {
				wand.charge(container.owner, STAFF_SCALE_FACTOR);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach( ) {
		if (wand != null) wand.stopCharging();
	}

	public Item imbueWand(Wand wand, Char owner){

		this.wand = null;

		GLog.w("TODO");
		enchant( null );

		//syncs the level of the two items.
		//TODO: decide on balancing for this
		int targetLevel = Math.round((this.level*2 + wand.level)/3f);

		int staffLevelDiff = targetLevel - this.level;
		if (staffLevelDiff > 0)
			this.upgrade(staffLevelDiff);
		else if (staffLevelDiff < 0)
			this.degrade(Math.abs(staffLevelDiff));

		int wandLevelDiff = targetLevel - wand.level;
		if (wandLevelDiff > 0)
			wand.upgrade(wandLevelDiff);
		else if (wandLevelDiff < 0)
			wand.degrade(Math.abs(wandLevelDiff));

		GLog.p("TODO");

		this.wand = wand;
		wand.identify();
		wand.cursed = false;
		wand.charge(owner);

		updateQuickslot();

		return this;

	}

	@Override
	public Item upgrade() {
		if (wand != null) wand.upgrade();
		return super.upgrade();
	}

	@Override
	public Item degrade() {
		if (wand != null) wand.degrade();
		return super.degrade();
	}

	@Override
	public String status() {
		if (wand == null) return super.status();
		else return wand.status();
	}

	@Override
	public String name(){
		if (wand == null)
			return super.name();
		else {
			String name = wand.name().replace("Wand", "Staff");
			return enchantment == null ? name : enchantment.name( name );
		}
	}

	@Override
	public String info() {
		return super.info();
	}

	private static final String WAND = "wand";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND, wand);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		//TODO: error check here?
		wand = (Wand) bundle.get(WAND);
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {

				Sample.INSTANCE.play( Assets.SND_EVOKE );
				ScrollOfUpgrade.upgrade(curUser);
				evoke( curUser );

				GLog.w( "TODO" );

				Dungeon.quickslot.clearItem(item);

				item.detach( curUser.belongings.backpack );

				imbueWand((Wand) item, curUser);

				curUser.spendAndNext( 2f );

				updateQuickslot();

			}
		}
	};
}
