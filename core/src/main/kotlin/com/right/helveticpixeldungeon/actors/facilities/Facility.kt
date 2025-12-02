package com.right.helveticpixeldungeon.actors.facilities

import com.right.helveticpixeldungeon.sprites.facilities.FacilitySprite
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.items.Heap
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon
import com.shatteredpixel.shatteredpixeldungeon.levels.Level
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog
import com.watabou.utils.Bundle
import com.watabou.utils.Reflection

open class Facility : Actor() {
    var level: Level? = null
    var sprite: FacilitySprite? = null
    var spriteClass: Class<out FacilitySprite>? = null
    var pos: Int = 0

    @JvmField
    var seen: Boolean = false

    override fun act(): Boolean {
        // do nothing by default
        return true
    }

    open fun description(): String {
        return Messages.get(this, DESC)
    }

    open fun name(): String {
        return Messages.get(this, NAME)
    }

    fun info(): String = description()

    open fun makeSprite(): FacilitySprite {
        return Reflection.newInstance(spriteClass)
    }

    open fun type(): Int {
        return 0
    }


    companion object {
        const val POS = "pos"
        const val DESC = "desc"

        const val NAME = "name"

        const val PASSABLE = 0x01

        // cannot see through an opaque facility
        const val OPAQUE = 0x02

        //        const val LOS_BLOCKING = 0x02
//
        const val FLAMABLE = 0x04
//
//        const val SECRET = 0x08
//
//        const val SOLID = 0x10
//
//        const val AVOID= 0x20
//
//        const val LIQUID = 0x40
//
//        const val PIT = 0x80
    }

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put(POS, pos)
    }

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)

        val pos = bundle.getInt(POS)
        this.pos = pos


    }

    open fun onPressed() {
        GLog.i("facility:%s pressed on pos:%d", this.name(), this.pos)
    }

    open fun onRemoveFromLevel() {
        sprite?.die()
        sprite?.destroy()
        Actor.remove(this)
    }

    open fun onBurn(fireOnThisPos: Int): Int {
        // do nothing by default
        return 0
    }

    open fun onBeShoot(Hero: Hero, wep: MissileWeapon): Boolean {
        return false
    }

    /** @return beStick?
     */
    open fun onStick(wep: MissileWeapon): Boolean {
        return false
    }

    open fun onDropItem(heap: Heap): Heap {
        return heap
    }

    open fun shouldHeroInteract(hero: Hero,isAdjacent: Boolean): Boolean {
        return false
    }

    open fun onOperate(hero: Hero): Boolean {
        return false
    }
}