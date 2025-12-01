package com.right.helveticpixeldungeon.windows

import com.right.helveticpixeldungeon.actors.facilities.Facility
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage
import com.watabou.noosa.ui.Component
import kotlin.math.max

class WndInfoFacility(facility: Facility) : WndTitledMessage(FacilityTitle(facility), facility.info()) {
    private class FacilityTitle(facility: Facility) : Component() {
        companion object {
            const val GAP = 2
        }

        val image = facility.makeSprite()
        val name = PixelScene.renderTextBlock(Messages.titleCase(facility.name()), 9)

        init {
            add(image)
            add(name)
        }

        override fun layout() {
            image!!.x = 0f
            image.y = max(0f, name.height() - image.height())

            name.setPos(
                x + image.width() + GAP,
                if (image.height() > name.height()) y + (image.height() - name.height()) / 2 else y
            )



            height = max(image.y + image.height(), name.bottom())
        }
    }
}