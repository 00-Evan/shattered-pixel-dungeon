package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class TestScrollSpawner: TestItemSpawner<Scroll>(ScrollOfIdentify::class.java, ItemSpriteSheet.SCROLL_HOLDER) {
    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }
    class WndSetting(val that: TestScrollSpawner): WndItemSpawnerSetting<Scroll>(that){
        override fun getItemClasses(): Iterable<Class<out Scroll>> {

            val classes= Generator.Category.SCROLL.classes.toMutableList()
            classes.addAll(Catalog.EXOTIC_SCROLLS.items())
            return classes.asIterable() as Iterable<Class<out Scroll>>
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

