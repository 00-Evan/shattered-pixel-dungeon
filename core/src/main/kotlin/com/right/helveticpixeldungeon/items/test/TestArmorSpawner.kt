package com.right.helveticpixeldungeon.items.test


import com.right.helveticpixeldungeon.windows.WndGetEmlement
import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane
import com.watabou.utils.Reflection


class TestArmorSpawner : TestItemSpawner<Armor>(ClothArmor::class.java, ItemSpriteSheet.ARMOR_HOLDER) {
    init {
        itemQuantity = null
        itemLvl = 0
    }

    var glyph: Armor.Glyph?= null

    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }

    override fun spawnItem(): Armor {
        val item= super.spawnItem()
        glyph?.let {
            item.glyph = Reflection.newInstance(it.javaClass)
        }
        return item
    }


    class WndSetting(val that: TestArmorSpawner) : WndItemSpawnerSetting<Armor>(that) {
        override fun getItemClasses(): Iterable<Class<out Item>> {
            val classes = Catalog.ARMOR.items()
            return classes.asIterable() as Iterable<Class<out Item>>
        }

        init {
            initializeWithoutTier()
        }

        override fun initSettingBtn() {
            settingBtn0 = SettingLvlButton(that)
            add(settingBtn0)
            settingBtn1 = SettingGlyphButton(that)
            add(settingBtn1)
        }
    }


    class  SettingGlyphButton(that: TestArmorSpawner): WndItemSpawnerSetting.SettingButton<Armor>(that,that.glyph?.name()?:"NONE") {
        override fun onClick() {
            val glyphWnd= object :WndGetGlyph(spawner as TestArmorSpawner){
                override fun onEnsure() {
                    newGlyph?.let {
                        spawner.glyph=it
                    }
                    this@SettingGlyphButton.updateTxt()
                }
            }
            GameScene.show(glyphWnd)
        }
        override fun updateTxt() {
            this.text((spawner as TestArmorSpawner).glyph?.name()?:"NONE")
        }

    }

    open class WndGetGlyph(val spawner: TestArmorSpawner) : WndGetEmlement(spawner.glyph?.name()?:"NONE") {
        var newGlyph: Armor.Glyph?= null
        override fun addElement(list: ScrollingListPane) {
            val thisWnd = this
            for(glyphCls in Catalog.GLYPHS.items()) {
                val glyph  = Reflection.newInstance(glyphCls as Class<Armor.Glyph>)
                val ele = object :ScrollingListPane.ListItem(null,null,glyph.name(),6){
                    override fun onClick(x: Float, y: Float): Boolean {
                        if(!inside(x,y))return false
                        thisWnd.title?.text(glyph.name())
                        thisWnd.newGlyph = glyph
                        return true
                    }
                }
                ele.setSize(WIDTH -2f*MARGINS,16f)
                list.addItem(ele)
            }
        }

    }

}
