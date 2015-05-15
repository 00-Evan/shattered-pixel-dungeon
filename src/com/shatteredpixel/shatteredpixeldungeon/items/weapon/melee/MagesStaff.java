package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.*;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
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

	private static final float STAFF_SCALE_FACTOR = 0.75f;

	{
		name = "staff";
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
		if (wand != null && Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE)
			wand.onHit( this, attacker, defender, damage );
		super.proc(attacker, defender, damage);
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

		GLog.p("You imbue your staff with the " + wand.name());

		if (enchantment != null) {
			GLog.w("The conflicting magics erase the enchantment on your staff.");
			enchant(null);
		}

		//syncs the level of the two items.
		int targetLevel = Math.max(this.level, wand.level);

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
			return "mage's staff";
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
		wand = (Wand) bundle.get(WAND);
	}

	@Override
	public String desc() {
		String result = "Crafted by the mage himself, this extraordinary staff is one of a kind multi-purpose magical weapon.\n" +
				"\n" +
				"Rather than having an innate magic in it, this staff is instead imbued with magical energy from a wand, permanently granting it new power.\n" +
				"\n";

		if (wand instanceof WandOfMagicMissile){
			result += "The staff radiates consistent magical energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfFireblast){
			result += "The staff burns and sizzles with fiery energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfLightning){
			result += "The staff fizzes and crackles with electrical energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfDisintegration){
			result += "The staff hums with deep and powerful energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfVenom){
			result += "The staff drips and hisses with corrosive energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfPrismaticLight){
			result += "The staff glows and shimmers with bright energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfFrost){
			result += "The staff chills the air with the cold energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfBlastWave){
			result += "The staff pops and crackles with explosive energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfRegrowth){
			result += "The staff flourishes and grows with natural energy from the wand it is imbued with.";
		} else if (wand instanceof WandOfTransfusion){
			result += "The staff Courses and Flows with life energy from the wand it is imbued with.";
		}

		return result;
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( final Item item ) {
			if (item != null) {

				if (!item.isIdentified()) {
					GLog.w("You'll need to know more about this wand first.");
					return;
				} else if (item.cursed){
					GLog.w("You can't use a cursed wand.");
					return;
				}

				GameScene.show(
						new WndOptions("",
								"Are you sure you want to imbue your staff with this " + item.name() + "?\n\n" +
										"Your staff will inherit the highest level between it and the wand, " +
										"and all magic currently affecting the staff will be lost.",
								"Yes, i'm sure.",
								"No, I changed my mind") {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									Sample.INSTANCE.play(Assets.SND_EVOKE);
									ScrollOfUpgrade.upgrade(curUser);
									evoke(curUser);

									Dungeon.quickslot.clearItem(item);

									item.detach(curUser.belongings.backpack);

									imbueWand((Wand) item, curUser);

									curUser.spendAndNext(2f);

									updateQuickslot();
								}
							}

							;
						}
				);



			}
		}
	};
}
