package com.shatteredpixel.shatteredpixeldungeon.items.rings;

public class RingOfFuror extends Ring {

	{
		name = "Ring of Furor";
	}

	@Override
	protected RingBuff buff( ) {
		return new Furor();
	}

	@Override
	public String desc() {
		return isKnown() ?
				"This ring grants the wearer an inner fury, allowing them to attack more rapidly. " +
				"This fury works best in large bursts, so slow weapons benefit far more than fast ones. " +
				"A degraded ring will instead slow the wearer's speed of attack." :
				super.desc();
	}

	public class Furor extends RingBuff {
	}
}
