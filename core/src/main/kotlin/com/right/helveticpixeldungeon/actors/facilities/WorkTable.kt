package com.right.helveticpixeldungeon.actors.facilities

import com.right.helveticpixeldungeon.sprites.facilities.WorkTableSprite
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.items.Heap
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Katana
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword

class WorkTable : Facility() {
    init {
        sprite = WorkTableSprite()
        spriteClass= WorkTableSprite::class.java
    }

    override fun type(): Int{
        return PASSABLE or OPAQUE;
    }

    override fun onDropItem(heap: Heap): Heap {
        val item= Katana().identify()!!
        item.level(256)

        heap.drop(item)
        return heap
    }

    override fun shouldHeroInteract(hero: Hero,isAdjacent: Boolean): Boolean {
       return hero.belongings?.weapon!=null
    }

    override fun onOperate(hero: Hero): Boolean {
        hero.belongings?.weapon?.let {
            val level=it.level()
            it.level(level+1)
        }

        return false
    }
}