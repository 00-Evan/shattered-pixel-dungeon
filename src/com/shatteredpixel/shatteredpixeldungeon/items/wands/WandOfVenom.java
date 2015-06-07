package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.VenomGas;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

/**
 * Created by Evan on 12/04/2015.
 */
public class WandOfVenom extends Wand {

    {
        name = "Wand of Venom";
        image = ItemSpriteSheet.WAND_VENOM;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;
    }

    @Override
    protected void onZap(Ballistica bolt) {
        Blob venomGas = Blob.seed(bolt.collisionPos, 50 + 10 * level, VenomGas.class);
        ((VenomGas)venomGas).setStrength(level+1);
        GameScene.add(venomGas);
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.poison(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        new Poison().proc(staff, attacker, defender, damage);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0x8844FF ); particle.am = 0.6f;
        particle.setLifespan(0.6f);
        particle.acc.set(0, 40);
        particle.setSize( 0f, 3f);
        particle.shuffleXY(2f);
    }

    @Override
    public String desc() {
        return
            "This wand has a purple body which opens to a brilliant green gem. " +
            "A small amount of foul smelling gas leaks from the gem.\n\n" +
            "This wand shoots a bolt which explodes into a cloud of vile venomous gas at a targeted location. " +
            "Anything caught inside this cloud will take continual damage, increasing with time.";
    }
}
