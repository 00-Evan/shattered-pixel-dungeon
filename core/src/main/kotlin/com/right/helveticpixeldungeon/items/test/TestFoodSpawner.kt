package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestFoodSpawner : TestItemSpawner<Food>(Food::class.java, ItemSpriteSheet.FOOD_HOLDER) {
    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }
    class WndSetting(val that: TestFoodSpawner) : WndItemSpawnerSetting<Food>(that) {
        override fun getItemClasses(): Iterable<Class<out Item>> {

            return Catalog.FOOD.items().asIterable() as Iterable<Class<out Item>>
        }

        init {
            initializeWithoutTier()
        }

        override fun initSettingBtn() {
            settingBtn0 = SettingQuantityButton(spawner)
            add(settingBtn0)
        }

    }
}

