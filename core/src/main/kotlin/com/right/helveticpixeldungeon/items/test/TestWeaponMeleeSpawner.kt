package com.right.helveticpixeldungeon.items.test

import com.right.helveticpixeldungeon.windows.WndGetEmlement
import com.right.helveticpixeldungeon.windows.test.WndItemSpawnerSetting
import com.shatteredpixel.shatteredpixeldungeon.items.Generator
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane
import com.watabou.utils.Reflection

class TestWeaponMeleeSpawner : TestItemSpawner<MeleeWeapon>(WornShortsword::class.java, ItemSpriteSheet.WEAPON_HOLDER) {
    var itemEnchantment: Weapon.Enchantment? = null
    init {
        itemQuantity = null
        itemLvl = 0
    }

    override fun showSetting() {
        GameScene.show(WndSetting(this))
    }

    override fun spawnItem(): MeleeWeapon {
        val item= super.spawnItem()
        itemEnchantment?.let {
            item.enchantment = Reflection.newInstance(it.javaClass)
        }
        return item
    }

    class WndSetting(val that: TestWeaponMeleeSpawner) : WndItemSpawnerSetting<MeleeWeapon>(that) {
        override fun getItemClasses(): Iterable<Class<out Item>> {
            val classes = when(spawner.tier){
                2 -> Generator.Category.WEP_T2.classes
                3 -> Generator.Category.WEP_T3.classes
                4 -> Generator.Category.WEP_T4.classes
                5 -> Generator.Category.WEP_T5.classes
                6 -> Generator.Category.WEP_T6.classes
                else  -> Generator.Category.WEP_T1.classes
            }
            return classes.asIterable() as Iterable<Class<out Item>>
        }

        init {
            initialize()
        }

        override fun initSettingBtn() {
            settingBtn0 = SettingLvlButton(spawner)
            add(settingBtn0)
            settingBtn1 = SettingEnchantmentButton(that)
            add(settingBtn1)
        }

    }

    class  SettingEnchantmentButton(that: TestWeaponMeleeSpawner): WndItemSpawnerSetting.SettingButton<MeleeWeapon>(that,that.itemEnchantment?.name()?:"NONE") {
        override fun onClick() {
            val glyphWnd= object :WndGetEnchantment(spawner as TestWeaponMeleeSpawner){
                override fun onEnsure() {
                    newEnchantment?.let {
                        spawner.itemEnchantment=it
                    }
                    this@SettingEnchantmentButton.updateTxt()
                }
            }
            GameScene.show(glyphWnd)
        }
        override fun updateTxt() {
            this.text((spawner as TestWeaponMeleeSpawner).itemEnchantment?.name()?:"NONE")
        }

    }

    open class WndGetEnchantment(val spawner: TestWeaponMeleeSpawner) : WndGetEmlement(spawner.itemEnchantment?.name()?:"NONE") {
        var newEnchantment: Weapon.Enchantment?= null
        override fun addElement(list: ScrollingListPane) {
            val thisWnd = this
            for(enchantmentCls in Catalog.ENCHANTMENTS.items()) {
                val enchantment  = Reflection.newInstance( enchantmentCls as Class<Weapon.Enchantment>)
                val ele = object :ScrollingListPane.ListItem(null,null,enchantment.name(),6){
                    override fun onClick(x: Float, y: Float): Boolean {
                        if(!inside(x,y))return false
                        thisWnd.title?.text(enchantment.name())
                        thisWnd.newEnchantment = enchantment
                        return true
                    }
                }
                ele.setSize(WIDTH -2f*MARGINS,16f)
                list.addItem(ele)
            }
        }

    }


}


