package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.items.test.bag.TestBag
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.Torch
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch

open class TestTool : Item() {


    init {
        stackable = false

        bones = false
        identify()

    }

    override fun isUpgradable(): Boolean = false
    override fun isIdentified(): Boolean = true

    companion object{
        /** should be called in HeroClass#initHero
         * */
        @JvmStatic
        fun collectTools(){
            TestBag().collect()

            VelvetPouch().collect()
            ScrollHolder().collect()
            MagicalHolster().collect()
            PotionBandolier().collect()

            TestArmorSpawner().collect()
            TestArtifactSpawner().collect()
            TestFoodSpawner().collect()
            TestPotionSpawner().collect()
            TestRingSpawner().collect()
            TestScrollSpawner().collect()
            TestSeedSpawner().collect()
            TestStoneSpawner().collect()
            TestTrinketSpawner().collect()
            TestWandSpawner().collect()
            TestWeaponMissileSpawner().collect()
            TestWeaponMeleeSpawner().collect()

            TestLevelTransport().collect()

            Torch().quantity(100).blessedType(BlessedType.CURSED).collect()
            Torch().quantity(100).blessedType(BlessedType.DIVINE).collect()
            Torch().quantity(100).blessedType(BlessedType.BLESSED).collect()

        }
    }
}