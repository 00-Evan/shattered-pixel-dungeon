package com.shatteredpixel.shatteredpixeldungeon.custom.ch;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class StrengthAndSacrifice extends Buff implements Hero.Doom{

    {
        announced = true;
        type = buffType.NEUTRAL;
    }

    private float count = 200f;
    private static final String COUNT= "count";
    private float deathCount = 0f;
    private static final String DEATH_COUNT = "death_count";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( COUNT, count );
        bundle.put( DEATH_COUNT, deathCount );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        count = bundle.getFloat( COUNT );
        deathCount = bundle.getFloat( DEATH_COUNT );
    }

    private float StrValue(){
        float str = 0f;
        str += 1.2f* Statistics.enemiesSlain / (Statistics.enemiesSlain + 150);
        str += Statistics.ankhsUsed *0.4f;
        str += 1.2f* Statistics.foodEaten / (Statistics.foodEaten + 20);
        str += Statistics.upgradesUsed / 12f;
        str += Dungeon.hero.lvl / 10f;

        return str;
    }

    public float StrengthFactor(){
        return 1f+StrValue()*0.1f+ 0.49f*(float)(Math.sqrt(StrValue()*StrValue()+6.25f)-2.5f);
    }

    private float IncomingDmgValue(){
        float dmg = 0f;
        dmg += 1.2f* Statistics.enemiesSlain / (Statistics.enemiesSlain + 150);
        dmg += Statistics.deepestFloor / 15f;
        dmg += deathCount / 7000f;
        dmg += Dungeon.hero.lvl / 12f;
        dmg -= Statistics.ankhsUsed / 3f;
        dmg += Statistics.upgradesUsed / 12f;

        if(dmg<0) dmg = 0f;
        return dmg;
    }

    public float IncomingDamageFactor(){
        return 1f + IncomingDmgValue()*0.1f + 0.49f*((float)Math.sqrt(IncomingDmgValue()*IncomingDmgValue() + 6.25f)-2.5f);
    }

    //Note that there are 4 phases
    //1: do nothing 2: start to influence HP 3: enemy attack kill 4: death count
    //4 gets priority to 3 and so is to 3, 2, 1
    public boolean IsDeathCount(){return deathCount>8000f;}

    public boolean CanOneHitKill(){
        return IncomingDamageFactor() > 2.4f;
    }

    public boolean CanDamageHPDirectly(){ return IncomingDamageFactor() > 1.6f;}

    public float HPDamagePercentage(){return (float) (0.16 + 0.8*(IncomingDamageFactor()-1.6f)*Math.sqrt(IncomingDamageFactor()-1.6f));}



    public float EvasionCorruptionFactor(){
        if(IsDeathCount()) return 0f;
        else return Math.max(0f,1f-0.25f*((float)Math.sqrt(IncomingDmgValue()*StrValue() + 9f)-3f));
    }

    public float AccuracyBonusFactor(){
        if(IsDeathCount()) return 2.0f;
        else return 1f+0.25f*((float)Math.sqrt(IncomingDmgValue()*StrValue() + 6.25f)-2.5f);
    }

    public float BlobAndBuffDamageHPFactor(){
        if(IncomingDamageFactor()<1.5f) return 0f;
        else if(IncomingDamageFactor()<2.4f) return 0.111f*(IncomingDamageFactor()-1.5f);
        else return 0.2f;
    }

    @Override
    public boolean act(){
        count -= 1f;
        if(count < 0){
            Buff.prolong(target, Blindness.class, deathCount / 400f + 4f);
            count += 200f - deathCount / 80f;
        }

        deathCount += 1f;
        if(deathCount < Statistics.duration ){
            deathCount = Statistics.duration;
        }
        if(deathCount>9500f){
            target.damage(target.HT * 64 + target.HP * 64, this);
        }


        spend(TICK);
        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.COMBO;
    }

    @Override
    public void tintIcon(Image icon){
        if(IsDeathCount()) icon.hardlight(1f,0f,0f);
        else if(CanOneHitKill()) icon.hardlight(1f,1f,0f);
        else if(CanDamageHPDirectly()){
            icon.hardlight(1f,0f,1f);
        }else
            icon.hardlight(0f,1f,0f);
    }

    @Override
    public float iconFadePercent(){
        if(IsDeathCount()){
            return Math.max(1f-(deathCount-8000f)/1500f,0f);
        }else if(CanOneHitKill()){
            return Math.max(1f-(deathCount-5000f)/3000f, 0f);
        }else if(CanDamageHPDirectly()){
            return Math.max(1f-(IncomingDamageFactor()-1.6f)/0.8f, 0f);
        }else
            return Math.max(1f-(IncomingDamageFactor()-1f)/0.6f, 0f);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        if(IsDeathCount()){
            return Messages.get(this, "desc_death", StrengthFactor()*100f, AccuracyBonusFactor()*100f, EvasionCorruptionFactor()*100f, 9500f-deathCount);
        }else if(CanOneHitKill()){
            return Messages.get(this, "desc_kill", StrengthFactor()*100f, IncomingDamageFactor()*100f, BlobAndBuffDamageHPFactor()*100f, AccuracyBonusFactor()*100f, EvasionCorruptionFactor()*100f);
        }else if(CanDamageHPDirectly()){
            return Messages.get(this, "desc_danger", StrengthFactor()*100f, IncomingDamageFactor()*100f, HPDamagePercentage()*100, BlobAndBuffDamageHPFactor()*100f, AccuracyBonusFactor()*100f, EvasionCorruptionFactor()*100f);
        }else{
            return Messages.get(this, "desc_normal", StrengthFactor()*100f, IncomingDamageFactor()*100f, AccuracyBonusFactor()*100f,  EvasionCorruptionFactor()*100f);
        }
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "ondeath") );
    }
}