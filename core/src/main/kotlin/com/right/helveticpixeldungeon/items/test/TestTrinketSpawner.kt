package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Trinket
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestTrinketSpawner: TestItemSpawner<Trinket>(TrinketCatalyst::class.java, ItemSpriteSheet.TRINKET_HOLDER) {
    init {
        itemQuantity = null
        itemLvl = 0
    }
    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }
    class WndSetting(val that: TestTrinketSpawner): WndItemSpawnerSetting<Trinket>(that){
        override fun getItemClasses(): Iterable<Class<out Trinket>> {

            val classes= Generator.Category.TRINKET.classes.toMutableList()

            classes.add(TrinketCatalyst::class.java)

            return classes.asIterable() as Iterable<Class<out Trinket>>
        }

        init {
            initializeWithoutTier()
        }

        override fun initSettingBtn() {
            settingBtn0 = SettingLvlButton(spawner)
            add(settingBtn0)
        }

        override fun getItemGridHeight(): Float = 53f
    }
}

