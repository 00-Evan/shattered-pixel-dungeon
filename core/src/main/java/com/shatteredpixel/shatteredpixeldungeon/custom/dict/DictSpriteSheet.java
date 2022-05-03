package com.shatteredpixel.shatteredpixeldungeon.custom.dict;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlameBoiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KagenoNusujinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OGPDLLSTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OGPDNQHZTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.OGPDZSLSTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PoltergeistSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSwarmSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.watabou.noosa.Image;

public class DictSpriteSheet {
    public static Image createImage(int sheet){
        if(sheet<10000) {
            return new ItemSprite(sheet);
        }
        return miscImages(sheet);
    }
    
    public static Image miscImages(int sheet){
        switch (sheet){
            case AREA_SEWER:
                return new Image(Assets.Environment.TILES_SEWERS, 16, 64, 16, 16);
            case AREA_PRISON:
                return new Image(Assets.Environment.TILES_PRISON, 16, 64, 16, 16);
            case AREA_CAVE:
                return new Image(Assets.Environment.TILES_CAVES, 16, 64, 16, 16);
            case AREA_CITY:
                return new Image(Assets.Environment.TILES_CITY, 16, 64, 16, 16);
            case AREA_HALL:
                return new Image(Assets.Environment.TILES_HALLS, 16, 64, 16, 16);

            case BOSS_CHAPTER1:
                return new Image(Assets.Sprites.GOO, 60, 0, 20, 14);
            case BOSS_CHAPTER2:
                return new Image(Assets.Sprites.TENGU, 0, 0, 14, 16);
            case BOSS_CHAPTER3:
                return new Image(Assets.Sprites.DM300, 0, 0, 25, 22);
            case BOSS_CHAPTER4:
                return new Image(Assets.Sprites.KING, 0, 0, 16, 16);
            case BOSS_CHAPTER5:
                return new Image(Assets.Sprites.YOG, 0, 0, 20, 19);

            case RAT:
                return new Image(Assets.Sprites.RAT, 0, 0, 16, 15);
            case OGPDNQHZ:
                return new OGPDNQHZTT();
            case OGPDLLS:
                return new OGPDLLSTT();
            case OGPDZSLS:
                return new OGPDZSLSTT();
            case COLD:
                return new ColdRatSprite();
            case RED:
                return new RedSwarmSprite();
            case SHOW:
                return new KagenoNusujinSprite();
            case HBLR:
                return new SRPDHBLRTT();
            case BLACK:
                return new PoltergeistSprite();
            case MOLO:
                return new MolotovHuntsmanSprite();
            case FLAME:
                return new FlameBoiSprite();
            case GNOLL:
                return new Image(Assets.Sprites.GNOLL, 0, 0, 12, 15);
            case XTG:
                 return new TenguSprite();
            case REDBS:
                return new RedNecromancerSprite();
            case SLXJ:
                return new SpectralNecromancerSprite();
            case BMHR:
                return new ShieldHuntsmanSprite();
            case SNAKE:
                return new Image(Assets.Sprites.SNAKE, 0, 0, 12, 11);
            case ALBINO:
                return new Image(Assets.Sprites.RAT, 0, 15, 16, 15);
            case CRAB:
                return new Image(Assets.Sprites.CRAB, 0, 0, 16, 16);
            case SWARM:
                return new Image(Assets.Sprites.SWARM, 0, 0, 16, 16);
            case SLIME:
                return new Image(Assets.Sprites.SLIME, 14, 0, 14, 12);
            case F_RAT:
                return new Image(Assets.Sprites.RAT, 0, 30, 16, 15);
            case GNOLL_DARTER:
                return new Image(Assets.Sprites.GNOLL, 0, 15, 12, 15);
            case CAUSTIC_SLIME:
                return new Image(Assets.Sprites.SLIME, 14, 12, 14, 12);
            case GREAT_CRAB:
                return new Image(Assets.Sprites.CRAB, 0, 16, 16, 16);

            case THIEF:
                return new Image(Assets.Sprites.THIEF, 0, 0, 12, 13);
            case BANDIT:
                return new Image(Assets.Sprites.THIEF, 0, 13, 12, 13);
            case SKELETON:
                return new Image(Assets.Sprites.SKELETON, 0, 0, 12, 15);
            case DM100:
                return new Image(Assets.Sprites.DM100, 0, 0, 16, 14);
            case GUARD:
                return new Image(Assets.Sprites.GUARD, 0, 0, 12, 16);
            case NECROMANCER:
                return new Image(Assets.Sprites.NECRO, 0, 0, 16, 16);
            case ROT_HEART:
                return new Image(Assets.Sprites.ROT_HEART, 0, 0, 16, 16);
            case ROT_LASHER:
                return new Image(Assets.Sprites.ROT_LASH, 0, 0, 12, 16);
            case NEW_FIRE_ELE:
                return new Image(Assets.Sprites.ELEMENTAL, 168, 0, 12, 14);

            case BAT:
                return new Image(Assets.Sprites.BAT, 0, 0, 15, 15);
            case BRUTE:
                return new Image(Assets.Sprites.BRUTE, 0, 0, 12, 16);
            case ARMORED_BRUTE:
                return new Image(Assets.Sprites.BRUTE, 0, 16, 12, 16);
            case SHAMAN:
                return new Image(Assets.Sprites.SHAMAN, 0, 0, 12, 15);
            case SPINNER:
                return new Image(Assets.Sprites.SPINNER, 0, 0, 16, 16);
            case DM200:
                return new Image(Assets.Sprites.DM200, 0, 0, 21, 18);
            case DM201:
                return new Image(Assets.Sprites.DM200, 0, 18, 21, 18);
            case PYLON:
                return new Image(Assets.Sprites.PYLON, 10, 0, 10, 20);

            case GHOUL:
                return new Image(Assets.Sprites.GHOUL, 0, 0, 12, 14);
            case WARLOCK:
                return new Image(Assets.Sprites.WARLOCK, 0, 0, 12, 15);
            case MONK:
                return new Image(Assets.Sprites.MONK, 0, 0, 15, 14);
            case SENIOR:
                return new Image(Assets.Sprites.MONK, 0, 14, 15, 14);
            case GOLEM:
                return new Image(Assets.Sprites.GOLEM, 0, 0, 17, 19);
            case ELEMENTAL_FIRE:
                return new Image(Assets.Sprites.ELEMENTAL, 0, 0, 12, 14);
            case ELEMENTAL_FROST:
                return new Image(Assets.Sprites.ELEMENTAL, 336, 0, 12, 14);
            case ELEMENTAL_SHOCK:
                return new Image(Assets.Sprites.ELEMENTAL, 0, 14, 12, 14);
            case ELEMENTAL_CHAOS:
                return new Image(Assets.Sprites.ELEMENTAL, 168, 14, 12, 14);

            case RIPPER:
                return new Image(Assets.Sprites.RIPPER, 0, 0, 15, 14);
            case SPAWNER:
                return new Image(Assets.Sprites.SPAWNER, 0, 0, 16, 16);
            case SUCCUBUS:
                return new Image(Assets.Sprites.SUCCUBUS, 0, 0, 12, 15);
            case EYE:
                return new Image(Assets.Sprites.EYE, 0, 0, 16, 18);
            case SCORPIO:
                return new Image(Assets.Sprites.SCORPIO, 0, 0, 18, 17);
            case AICDIC:
                return new Image(Assets.Sprites.SCORPIO, 0, 17, 18, 17);
            case LARVA:
                return new Image(Assets.Sprites.LARVA, 36, 0, 12, 8);
            case FIST_1:
                return new Image(Assets.Sprites.FISTS, 0, 0, 24, 17);
            case FIST_2:
                return new Image(Assets.Sprites.FISTS, 0, 34, 24, 17);
            case FIST_3:
                return new Image(Assets.Sprites.FISTS, 0, 68, 24, 17);

            case FISH:
                return new Image(Assets.Sprites.PIRANHA, 0, 0, 12, 16);
            case STATUE:
                return new Image(Assets.Sprites.STATUE, 0, 0, 12, 15);
            case ARMORED_STATUE:
                return new Image(Assets.Sprites.STATUE, 24, 45, 12, 15);
            case MIMIC:
                return new Image(Assets.Sprites.MIMIC, 0, 0, 16, 16);
            case MIMIC_GOLDEN:
                return new Image(Assets.Sprites.MIMIC, 0, 16, 16, 16);
            case MIMIC_CRYSTAL:
                return new Image(Assets.Sprites.MIMIC, 0, 32, 16, 16);
            case WRAITH:
                return new Image(Assets.Sprites.WRAITH, 0, 0, 14, 15);
            case BEE:
                return new Image(Assets.Sprites.BEE, 0, 0, 16, 16);

            case SAD_GHOST:
                return new Image(Assets.Sprites.GHOST, 0, 0, 14, 15);
            case WAND_MAKER:
                return new Image(Assets.Sprites.MAKER, 0, 0, 12, 14);
            case BLACKSMITH:
                return new Image(Assets.Sprites.TROLL, 0, 0, 13, 16);
            case IMP:
                return new Image(Assets.Sprites.IMP, 0, 0, 12, 14);
            case IMAGE:
                return new Image(Assets.Sprites.WARRIOR, 0, 15, 12, 15);
            case PRISMATIC_IMAGE:
                return new Image(Assets.Sprites.WARRIOR, 0, 15, 12, 15);
            case RAT_KING:
                return new Image(Assets.Sprites.RATKING, 0, 17, 16, 17);
            case SHEEP:
                return new Image(Assets.Sprites.SHEEP, 0, 0, 16, 15);
            case HERO:
                return new Image(Assets.Sprites.ROGUE, 0, 15, 12, 15);
            case TRAP_GREEN_RECT:
                return new Image(Assets.Environment.TERRAIN_FEATURES, 48, 64, 16, 16);
            case LOCKED_FLOOR:
                return new Image(Assets.Environment.TILES_CAVES, 64, 80, 16, 16);
            case CHASM:
                return new Image(Assets.Environment.TILES_CAVES, 48, 48, 16, 16);
            case BUFF_POSITIVE:
                return new Image(Assets.Interfaces.BUFFS_LARGE, 48 ,16, 16, 16);
            case BUFF_NEUTRAL:
                return new Image(Assets.Interfaces.BUFFS_LARGE, 112 ,32, 16, 16);
            case BUFF_NEGATIVE:
                return new Image(Assets.Interfaces.BUFFS_LARGE, 224 ,0, 16, 16);

        }
        return new ItemSprite(ItemSpriteSheet.SOMETHING);
    }




    public static final int AREA_SEWER      = 0 + 10000;
    public static final int AREA_PRISON     = 1 + 10000;
    public static final int AREA_CAVE       = 2 + 10000;
    public static final int AREA_CITY       = 3 + 10000;
    public static final int AREA_HALL       = 4 + 10000;
    public static final int LOCKED_FLOOR    = 11 + 10000;
    public static final int CHASM           = 12 + 10000;

    public static final int BOSS_CHAPTER1   = 100 + 10000;
    public static final int BOSS_CHAPTER2   = 101 + 10000;
    public static final int BOSS_CHAPTER3   = 102 + 10000;
    public static final int BOSS_CHAPTER4   = 103 + 10000;
    public static final int BOSS_CHAPTER5   = 104 + 10000;

    public static final int RAT             = 200 + 10000;
    public static final int ALBINO          = 201 + 10000;
    public static final int GNOLL           = 202 + 10000;
    public static final int SNAKE           = 203 + 10000;
    public static final int CRAB            = 204 + 10000;
    public static final int SWARM           = 205 + 10000;
    public static final int SLIME           = 206 + 10000;
    public static final int CAUSTIC_SLIME   = 207 + 10000;
    public static final int F_RAT           = 208 + 10000;
    public static final int GNOLL_DARTER    = 209 + 10000;
    public static final int GREAT_CRAB      = 210 + 10000;
    public static final int OGPDNQHZ        = 211 + 10000;
    public static final int OGPDLLS        =  212 + 10000;
    public static final int OGPDZSLS        = 213 + 10000;
    public static final int COLD        = 214 + 10000;
    public static final int RED      = 215 + 10000;
    public static final int SHOW      = 216 + 10000;
    public static final int HBLR      = 217 + 10000;
    public static final int BLACK      = 218 + 10000;
    public static final int MOLO      = 219 + 10000;
    public static final int FLAME      = 220 + 10000;
    public static final int XTG      = 221 + 10000;
    public static final int REDBS      = 222 + 10000;
    public static final int SLXJ      = 223 + 10000;
    public static final int BMHR      = 224 + 10000;

    public static final int THIEF           = 300 + 10000;
    public static final int BANDIT          = 301 + 10000;
    public static final int SKELETON        = 302 + 10000;
    public static final int GUARD           = 303 + 10000;
    public static final int DM100           = 304 + 10000;
    public static final int NECROMANCER     = 305 + 10000;
    public static final int ROT_HEART       = 306 + 10000;
    public static final int ROT_LASHER      = 307 + 10000;
    public static final int NEW_FIRE_ELE    = 308 + 10000;

    public static final int BAT             = 400 + 10000;
    public static final int BRUTE           = 401 + 10000;
    public static final int ARMORED_BRUTE   = 402 + 10000;
    public static final int SHAMAN          = 403 + 10000;
    public static final int SPINNER         = 404 + 10000;
    public static final int DM200           = 405 + 10000;
    public static final int DM201           = 406 + 10000;
    public static final int PYLON           = 407 + 10000;

    public static final int GHOUL           = 500 + 10000;
    public static final int ELEMENTAL_FIRE  = 501 + 10000;
    public static final int ELEMENTAL_FROST = 502 + 10000;
    public static final int ELEMENTAL_SHOCK = 503 + 10000;
    public static final int ELEMENTAL_CHAOS = 504 + 10000;
    public static final int WARLOCK         = 505 + 10000;
    public static final int MONK            = 506 + 10000;
    public static final int SENIOR          = 507 + 10000;
    public static final int GOLEM           = 508 + 10000;

    public static final int RIPPER          = 600 + 10000;
    public static final int SPAWNER         = 601 + 10000;
    public static final int SUCCUBUS        = 602 + 10000;
    public static final int EYE             = 603 + 10000;
    public static final int SCORPIO         = 604 + 10000;
    public static final int AICDIC          = 605 + 10000;
    public static final int LARVA           = 606 + 10000;
    public static final int FIST_1          = 607 + 10000;
    public static final int FIST_2          = 608 + 10000;
    public static final int FIST_3          = 609 + 10000;

    public static final int STATUE          = 700 + 10000;
    public static final int ARMORED_STATUE  = 701 + 10000;
    public static final int FISH            = 702 + 10000;
    public static final int MIMIC           = 703 + 10000;
    public static final int MIMIC_GOLDEN    = 704 + 10000;
    public static final int MIMIC_CRYSTAL   = 705 + 10000;
    public static final int WRAITH = 706 + 10000;
    public static final int BEE             = 707 + 10000;

    public static final int SAD_GHOST       = 800 + 10000;
    public static final int WAND_MAKER      = 801 + 10000;
    public static final int BLACKSMITH      = 802 + 10000;
    public static final int IMP             = 803 + 10000;
    public static final int IMAGE           = 804 + 10000;
    public static final int PRISMATIC_IMAGE = 805 + 10000;
    public static final int RAT_KING        = 806 + 10000;
    public static final int SHEEP           = 807 + 10000;

    public static final int HERO            = 2000 + 10000;


    public static final int TRAP_GREEN_RECT = 3000 + 10000;


    public static final int BUFF_POSITIVE   = 4000 + 10000;
    public static final int BUFF_NEUTRAL    = 4001 + 10000;
    public static final int BUFF_NEGATIVE   = 4002 + 10000;
}
