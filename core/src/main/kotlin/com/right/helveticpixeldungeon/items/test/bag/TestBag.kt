package com.right.helveticpixeldungeon.items.test.bag

import com.right.helveticpixeldungeon.items.test.TestTool
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.watabou.noosa.Image

class TestBag : Bag() {
    init {
        identify()
        image = ItemSpriteSheet.BEACON
    }

    override fun canHold(item: Item?): Boolean {
        if (item !is TestTool) {
            return false
        }
        return super.canHold(item)
    }

    override fun capacity(): Int = 35

    override fun value(): Int = 0

    override fun icon(): Image? {
        return ItemSprite(ItemSpriteSheet.BEACON)
    }
}