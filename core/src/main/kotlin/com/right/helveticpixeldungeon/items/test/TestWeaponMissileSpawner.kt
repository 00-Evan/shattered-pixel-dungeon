package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestWeaponMissileSpawner : TestItemSpawner<MissileWeapon>(ThrowingStone::class.java, ItemSpriteSheet.MISSILE_HOLDER) {
    init {
        itemLvl = 0
    }

    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }

    class WndSetting(val that: TestWeaponMissileSpawner) : WndItemSpawnerSetting<MissileWeapon>(that) {
        override fun getItemClasses(): Iterable<Class<out Item>> {
            val classes = when(spawner.tier){
                2 -> {
                    val classes= Generator.Category.MIS_T2.classes.toMutableList()
                    classes.addAll(Catalog.TIPPED_DARTS.items())
                    classes.asIterable()
                }
                3 -> Generator.Category.MIS_T3.classes.asIterable()
                4 -> Generator.Category.MIS_T4.classes.asIterable()
                5 -> Generator.Category.MIS_T5.classes.asIterable()
                6 -> Generator.Category.MIS_T6.classes.asIterable()
                else  -> Generator.Category.MIS_T1.classes.asIterable()
            }
            return classes.asIterable() as Iterable<Class<out Item>>
        }

        init {
            initialize()
        }

        override fun initSettingBtn() {
            settingBtn0 = SettingLvlButton(spawner)
            add(settingBtn0)

            settingBtn1 = SettingQuantityButton(spawner)
            add(settingBtn1)
        }

    }

}

