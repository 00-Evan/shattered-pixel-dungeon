package com.shatteredpixel.shatteredpixeldungeon.capstone;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

public class Data {

    public ArrayList<boolean[]> statusAbnormals;
    public HashSet<Mob> mobs;
    public List<Trap> traps;

    // spawn mobs information
    public ArrayList<Integer> spawnMobsHT;
    public ArrayList<Integer> spawnMobsATT;
    public ArrayList<Integer> spawnMobsDEF;
    public ArrayList<Integer> spawnMobsEXP;

    // player information
    public int hp;
    public int ht;
    public int damaged;
    public int attackDamage;
    public int healPoint;
    public int killMonster;
    public int moving;
    public int earnEXP;

    // 변수 저장을 쉽게 하기 위해 임시 저장
    static private int totalDamaged;
    static private int totalEXP;
    
    // level.java 편집
    // flamable, solid, avoid
    public void addStatusAbnormal(boolean[] statusAbnormal){
        if(statusAbnormals == null) statusAbnormals = new ArrayList<boolean[]>();

        statusAbnormals.add(statusAbnormal);
    }

    public void storeMobs(HashSet<Mob> mobs){
        this.mobs = mobs;

        Iterator<Mob> it = mobs.iterator();
        storeSpawnMobsHT(it);
        storeSpawnMobsATT(it);
        storeSpawnMobsDEF(it);
        storeSpawnMobsEXP(it);
    }

    public void storeTraps(List<Trap> traps){
        this.traps = traps;
    }

    // 스폰된 mob들의 정보 추출
    public void storeSpawnMobsHT(Iterator<Mob> it){
        if(spawnMobsHT == null) spawnMobsHT = new ArrayList<Integer>();
        else spawnMobsHT.clear();

        while (it.hasNext())
            spawnMobsHT.add(it.next().HT);
    }

    public void storeSpawnMobsATT(Iterator<Mob> it){
        if(spawnMobsATT == null) spawnMobsATT = new ArrayList<Integer>();
        else spawnMobsATT.clear();

        while (it.hasNext())
            spawnMobsATT.add((it.next().MAX_ATT + it.next().MIN_ATT) / 2);
    }

    public void storeSpawnMobsDEF(Iterator<Mob> it){
        if(spawnMobsDEF == null) spawnMobsDEF = new ArrayList<Integer>();
        else spawnMobsDEF.clear();

        while (it.hasNext())
            spawnMobsDEF.add((it.next().MAX_DEF + it.next().MIN_DEF) / 2);
    }

    public void storeSpawnMobsEXP(Iterator<Mob> it){
        if(spawnMobsEXP == null) spawnMobsEXP = new ArrayList<Integer>();
        else spawnMobsEXP.clear();

        while (it.hasNext())
            spawnMobsEXP.add(it.next().EXP);
    }

    // Hero.java 편집
    public void storeHP(int HP){
        this.hp = HP;
    }

    public void storeHT(int HT){
        this.ht = HT;
    }

    public void storeDamaged(int damaged){
        this.damaged = damaged - totalDamaged;
        totalDamaged = damaged;
    }

//    public void storeAttackDamage(int attackDamage){
//        this.attackDamage = attackDamage;
//    }
//
//    public void storeHealPoint(int healPoint){
//        this.healPoint = healPoint;
//    }
//
//    public void storeKillMonster(int killMonster){
//        this.killMonster = killMonster;
//    }
//
//    public void storeMoving(int moving){
//        this.moving = moving;
//    }

    public void storeEarnEXP(int EXP){
        this.earnEXP = EXP - totalEXP;
        totalEXP = EXP;
    }
}