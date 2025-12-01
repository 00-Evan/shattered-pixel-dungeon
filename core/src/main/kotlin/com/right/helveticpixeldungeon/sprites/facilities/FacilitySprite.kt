package com.right.helveticpixeldungeon.sprites.facilities

import com.right.helveticpixeldungeon.actors.facilities.Facility
import com.shatteredpixel.shatteredpixeldungeon.Dungeon
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap
import com.watabou.noosa.Camera
import com.watabou.noosa.MovieClip
import com.watabou.utils.PointF

open class FacilitySprite: MovieClip() , MovieClip.Listener{
    var facility: Facility? = null



    enum class State{Idle,Working,Completed,Breaking,Broken}

    var idle: Animation? = null
    var working: Animation? = null
    var completed: Animation? = null
    var breaking: Animation? = null
    var broken: Animation? = null

    override fun play(anim: Animation?) {
        //Shouldn't interrupt the breaking animation,just like CharSprite
        if (curAnim == null || curAnim !== breaking) {
            super.play(anim)
        }
    }


    open fun link(facility: Facility) {
        linkVisuals(facility)

        this.facility = facility
        facility.sprite = this

        place(facility.pos)



    }

    override fun destroy() {
        super.destroy()
        facility?.let {
            if (  it.sprite === this) {
                it.sprite = null
            }
        }

    }

    //used for just updating a sprite based on a given facility, not linking them or placing in the game
    // similar to CharSprite
    open fun linkVisuals(facility: Facility) {
        //do nothing by default
    }

    fun worldToCamera(cell: Int): PointF {
        val csize = DungeonTilemap.SIZE

        return PointF(
            PixelScene.align(Camera.main, ((cell % Dungeon.level.width()) + 0.5f) * csize - width() * 0.5f),
            PixelScene.align(
                Camera.main,
                ((cell / Dungeon.level.width()) + 1.0f) * csize - height() - 6
            )
        )
    }

    open fun place(cell: Int) {
        point(worldToCamera(cell))
    }

    override fun onComplete(anim: Animation?) {

    }

    open fun die(){
        parent?.remove(this)

    }


}