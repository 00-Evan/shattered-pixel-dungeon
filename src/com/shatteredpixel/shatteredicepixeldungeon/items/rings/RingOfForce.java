package com.shatteredpixel.shatteredicepixeldungeon.items.rings;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;

/**
 * Created by debenhame on 10/09/2014.
 */
public class RingOfForce extends Ring {

    {
        name = "Ring of Force";
    }

    @Override
    protected RingBuff buff( ) {
        return new Force();
    }

    @Override
    public String desc() {
	    if (isKnown()){
		    String desc = "This ring enhances the force of the wearer's blows. " +
				    "This extra power is largely wasted when wielding weapons, " +
				    "but an unarmed attack will be made much stronger. " +
				    "A degraded ring will instead weaken the wearer's blows.\n\n" +
				    "When unarmed, at your current strength, ";
		    int str = Dungeon.hero.STR() - 8;
		    desc += levelKnown ?
				    "average damage with this ring is " + (str/2+level + (int)(str*0.5f*level) + str*2)/2 + " points per hit.":
				    "typical average damage with this ring is" + (str/2+1 + (int)(str*0.5f) + str*2)/2 + " points per hit.";
		    desc += " Wearing a second ring of force would enhance this.";
		    return desc;
	    } else
	        return super.desc();
    }

    public class Force extends RingBuff {
    }
}

