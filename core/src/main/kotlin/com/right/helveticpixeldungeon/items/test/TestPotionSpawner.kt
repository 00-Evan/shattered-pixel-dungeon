package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestPotionSpawner: TestItemSpawner<Potion>(PotionOfHealing::class.java, ItemSpriteSheet.POTION_HOLDER) {

    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }
    class WndSetting(val that: TestPotionSpawner): WndItemSpawnerSetting<Potion>(that){
        override fun getItemClasses(): Iterable<Class<out Item>> {
            val classes= Generator.Category.POTION.classes.toMutableList()
            classes.addAll(Catalog.BREWS_ELIXIRS.items())
            classes.addAll(Catalog.EXOTIC_POTIONS.items())

            return classes.asIterable() as Iterable<Class<out Item>>
        }

        init {
            initializeWithoutTier()
        }

        override fun initSettingBtn() {
            settingBtn0 = SettingQuantityButton(spawner)
            add(settingBtn0)
        }

        override fun getItemGridHeight(): Float = 53f
    }
}

