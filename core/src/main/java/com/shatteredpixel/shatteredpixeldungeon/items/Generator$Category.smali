.class public final enum Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
.super Ljava/lang/Enum;
.source "Generator.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x4019
    name = "Category"
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Enum",
        "<",
        "Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;",
        ">;"
    }
.end annotation


# static fields
.field private static final synthetic $VALUES:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum ARMOR:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum FOOD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum GOLD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum MISSILE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum MIS_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum MIS_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum MIS_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum MIS_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum MIS_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum POTION:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum RING:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum SCROLL:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum SEED:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum STONE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum WAND:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum WEAPON:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum WEP_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum WEP_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum WEP_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum WEP_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum WEP_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final enum WEP_T6:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;


# instance fields
.field public classes:[Ljava/lang/Class;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "[",
            "Ljava/lang/Class",
            "<*>;"
        }
    .end annotation
.end field

.field public defaultProbs:[F

.field public prob:F

.field public probs:[F

.field public superClass:Ljava/lang/Class;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/lang/Class",
            "<+",
            "Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;",
            ">;"
        }
    .end annotation
.end field


# direct methods
.method static constructor <clinit>()V
    .registers 10

    .prologue
    const/4 v9, 0x5

    const/4 v8, 0x2

    const/4 v7, 0x1

    const/4 v6, 0x3

    const/4 v5, 0x0

    .line 191
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "WEAPON"

    const/high16 v2, 0x40800000    # 4.0f

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    invoke-direct {v0, v1, v5, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEAPON:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 192
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "WEP_T1"

    const/4 v2, 0x0

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    invoke-direct {v0, v1, v7, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 193
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "WEP_T2"

    const/4 v2, 0x0

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    invoke-direct {v0, v1, v8, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 194
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "WEP_T3"

    const/4 v2, 0x0

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    invoke-direct {v0, v1, v6, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 195
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "WEP_T4"

    const/4 v2, 0x4

    const/4 v3, 0x0

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 196
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "WEP_T5"

    const/4 v2, 0x0

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    invoke-direct {v0, v1, v9, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 197
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "WEP_T6"

    const/4 v2, 0x6

    const/4 v3, 0x0

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T6:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 199
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "ARMOR"

    const/4 v2, 0x7

    const/high16 v3, 0x40400000    # 3.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARMOR:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 201
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "MISSILE"

    const/16 v2, 0x8

    const/high16 v3, 0x40400000    # 3.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MISSILE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 202
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "MIS_T1"

    const/16 v2, 0x9

    const/4 v3, 0x0

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 203
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "MIS_T2"

    const/16 v2, 0xa

    const/4 v3, 0x0

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 204
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "MIS_T3"

    const/16 v2, 0xb

    const/4 v3, 0x0

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 205
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "MIS_T4"

    const/16 v2, 0xc

    const/4 v3, 0x0

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 206
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "MIS_T5"

    const/16 v2, 0xd

    const/4 v3, 0x0

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 208
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "WAND"

    const/16 v2, 0xe

    const/high16 v3, 0x40000000    # 2.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/Wand;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WAND:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 209
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "RING"

    const/16 v2, 0xf

    const/high16 v3, 0x3f800000    # 1.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/Ring;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->RING:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 210
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "ARTIFACT"

    const/16 v2, 0x10

    const/high16 v3, 0x3f800000    # 1.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 212
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "FOOD"

    const/16 v2, 0x11

    const/4 v3, 0x0

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/food/Food;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->FOOD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 214
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "POTION"

    const/16 v2, 0x12

    const/high16 v3, 0x41800000    # 16.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/Potion;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->POTION:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 215
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "SEED"

    const/16 v2, 0x13

    const/high16 v3, 0x40000000    # 2.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Plant$Seed;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SEED:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 217
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "SCROLL"

    const/16 v2, 0x14

    const/high16 v3, 0x41800000    # 16.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/Scroll;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SCROLL:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 219
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "STONE"

    const/16 v2, 0x15

    const/high16 v3, 0x40000000    # 2.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/Runestone;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->STONE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 221
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const-string v1, "GOLD"

    const/16 v2, 0x16

    const/high16 v3, 0x41a00000    # 20.0f

    const-class v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/Gold;

    invoke-direct {v0, v1, v2, v3, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;-><init>(Ljava/lang/String;IFLjava/lang/Class;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->GOLD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 190
    const/16 v0, 0x17

    new-array v0, v0, [Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEAPON:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v5

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v7

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v8

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v6

    const/4 v1, 0x4

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v9

    const/4 v1, 0x6

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T6:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/4 v1, 0x7

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARMOR:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x8

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MISSILE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x9

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0xa

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0xb

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0xc

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0xd

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0xe

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WAND:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0xf

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->RING:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x10

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x11

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->FOOD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x12

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->POTION:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x13

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SEED:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x14

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SCROLL:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x15

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->STONE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    const/16 v1, 0x16

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->GOLD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->$VALUES:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 252
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->GOLD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v7, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Gold;

    aput-object v2, v1, v5

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 254
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->GOLD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v7, [F

    const/high16 v2, 0x3f800000    # 1.0f

    aput v2, v1, v5

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 256
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->POTION:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xd

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfStrength;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfHealing;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfMindVision;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfFrost;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLiquidFlame;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfToxicGas;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfHaste;

    aput-object v3, v1, v2

    const/4 v2, 0x7

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfInvisibility;

    aput-object v3, v1, v2

    const/16 v2, 0x8

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLevitation;

    aput-object v3, v1, v2

    const/16 v2, 0x9

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfParalyticGas;

    aput-object v3, v1, v2

    const/16 v2, 0xa

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfPurity;

    aput-object v3, v1, v2

    const/16 v2, 0xb

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfExperience;

    aput-object v3, v1, v2

    const/16 v2, 0xc

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLiquidFlameX;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 270
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->POTION:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xd

    new-array v1, v1, [F

    fill-array-data v1, :array_632

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    .line 271
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->POTION:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->POTION:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    invoke-virtual {v0}, [F->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [F

    iput-object v0, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 273
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SEED:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xd

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Rotberry$Seed;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Sungrass$Seed;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Fadeleaf$Seed;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Icecap$Seed;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Firebloom$Seed;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/plants/SkyBlueFireBloom$Seed;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Sorrowmoss$Seed;

    aput-object v3, v1, v2

    const/4 v2, 0x7

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Swiftthistle$Seed;

    aput-object v3, v1, v2

    const/16 v2, 0x8

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Blindweed$Seed;

    aput-object v3, v1, v2

    const/16 v2, 0x9

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Stormvine$Seed;

    aput-object v3, v1, v2

    const/16 v2, 0xa

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Earthroot$Seed;

    aput-object v3, v1, v2

    const/16 v2, 0xb

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Dreamfoil$Seed;

    aput-object v3, v1, v2

    const/16 v2, 0xc

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/plants/Starflower$Seed;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 287
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SEED:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xd

    new-array v1, v1, [F

    fill-array-data v1, :array_650

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    .line 288
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SEED:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SEED:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    invoke-virtual {v0}, [F->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [F

    iput-object v0, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 290
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SCROLL:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xc

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfUpgrade;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfIdentify;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRemoveCurse;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfMirrorImage;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRecharging;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfTeleportation;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfLullaby;

    aput-object v3, v1, v2

    const/4 v2, 0x7

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfMagicMapping;

    aput-object v3, v1, v2

    const/16 v2, 0x8

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRage;

    aput-object v3, v1, v2

    const/16 v2, 0x9

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRetribution;

    aput-object v3, v1, v2

    const/16 v2, 0xa

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfTerror;

    aput-object v3, v1, v2

    const/16 v2, 0xb

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfTransmutation;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 304
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SCROLL:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xc

    new-array v1, v1, [F

    fill-array-data v1, :array_66e

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    .line 305
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SCROLL:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->SCROLL:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    invoke-virtual {v0}, [F->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [F

    iput-object v0, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 307
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->STONE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xc

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfEnchantment;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfIntuition;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfDisarming;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfFlock;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfShock;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfBlink;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfDeepenedSleep;

    aput-object v3, v1, v2

    const/4 v2, 0x7

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfClairvoyance;

    aput-object v3, v1, v2

    const/16 v2, 0x8

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfAggression;

    aput-object v3, v1, v2

    const/16 v2, 0x9

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfBlast;

    aput-object v3, v1, v2

    const/16 v2, 0xa

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfAffection;

    aput-object v3, v1, v2

    const/16 v2, 0xb

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/stones/StoneOfAugmentation;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 321
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->STONE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xc

    new-array v1, v1, [F

    fill-array-data v1, :array_68a

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    .line 322
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->STONE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->STONE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    invoke-virtual {v0}, [F->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [F

    iput-object v0, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 324
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WAND:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xe

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfMagicMissile;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfLightning;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfDisintegration;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfFireblast;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfCorrosion;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfBlastWave;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfLivingEarth;

    aput-object v3, v1, v2

    const/4 v2, 0x7

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfFrost;

    aput-object v3, v1, v2

    const/16 v2, 0x8

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfPrismaticLight;

    aput-object v3, v1, v2

    const/16 v2, 0x9

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfWarding;

    aput-object v3, v1, v2

    const/16 v2, 0xa

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfTransfusion;

    aput-object v3, v1, v2

    const/16 v2, 0xb

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfCorruption;

    aput-object v3, v1, v2

    const/16 v2, 0xc

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfRegrowth;

    aput-object v3, v1, v2

    const/16 v2, 0xd

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfScale;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 339
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WAND:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xe

    new-array v1, v1, [F

    fill-array-data v1, :array_6a6

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 342
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEAPON:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v5, [Ljava/lang/Class;

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 343
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEAPON:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v5, [F

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 345
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v9, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/WornShortsword;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Gloves;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Dagger;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MagesStaff;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/BlackDog;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 352
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v9, [F

    fill-array-data v1, :array_6c6

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 354
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v9, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Shortsword;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/HandAxe;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Spear;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Quarterstaff;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Dirk;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 361
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v9, [F

    fill-array-data v1, :array_6d4

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 363
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xa

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Sword;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Mace;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Scimitar;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/RoundShield;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Sai;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Whip;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/SkyShield;

    aput-object v3, v1, v2

    const/4 v2, 0x7

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/BoomSword;

    aput-object v3, v1, v2

    const/16 v2, 0x8

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Dairikyan;

    aput-object v3, v1, v2

    const/16 v2, 0x9

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/GreenSword;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 375
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xa

    new-array v1, v1, [F

    fill-array-data v1, :array_6e2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 377
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/4 v1, 0x7

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Longsword;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/BattleAxe;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Flail;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/RunicBlade;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/AssassinsBlade;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Crossbow;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Sai;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 386
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/4 v1, 0x7

    new-array v1, v1, [F

    fill-array-data v1, :array_6fa

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 388
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/4 v1, 0x6

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Greatsword;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/WarHammer;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Glaive;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Greataxe;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Greatshield;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Gauntlet;

    aput-object v2, v1, v9

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 396
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/4 v1, 0x6

    new-array v1, v1, [F

    fill-array-data v1, :array_70c

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 398
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T6:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v8, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/FireFishSword;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/IceFishSword;

    aput-object v2, v1, v7

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 402
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T6:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v8, [F

    fill-array-data v1, :array_71c

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 405
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARMOR:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v9, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/ClothArmor;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/LeatherArmor;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/MailArmor;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/ScaleArmor;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/PlateArmor;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 411
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARMOR:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v9, [F

    fill-array-data v1, :array_724

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 414
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MISSILE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v5, [Ljava/lang/Class;

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 415
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MISSILE:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v5, [F

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 417
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v8, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingStone;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingKnife;

    aput-object v2, v1, v7

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 421
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v8, [F

    fill-array-data v1, :array_732

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 423
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/FishingSpear;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingClub;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/Shuriken;

    aput-object v2, v1, v8

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 428
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [F

    fill-array-data v1, :array_73a

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 430
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingSpear;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/Kunai;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/Bolas;

    aput-object v2, v1, v8

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 435
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [F

    fill-array-data v1, :array_744

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 437
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/Javelin;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/Tomahawk;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/HeavyBoomerang;

    aput-object v2, v1, v8

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 442
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [F

    fill-array-data v1, :array_74e

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 444
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/Trident;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingHammer;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ForceCube;

    aput-object v2, v1, v8

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 449
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [F

    fill-array-data v1, :array_758

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 451
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->FOOD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/food/Food;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/food/Pasty;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/food/MysteryMeat;

    aput-object v2, v1, v8

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 455
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->FOOD:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    new-array v1, v6, [F

    fill-array-data v1, :array_762

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 458
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->RING:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xb

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfAccuracy;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfEvasion;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfElements;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfForce;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfFuror;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfHaste;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfEnergy;

    aput-object v3, v1, v2

    const/4 v2, 0x7

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfMight;

    aput-object v3, v1, v2

    const/16 v2, 0x8

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfSharpshooting;

    aput-object v3, v1, v2

    const/16 v2, 0x9

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfTenacity;

    aput-object v3, v1, v2

    const/16 v2, 0xa

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/rings/RingOfWealth;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 470
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->RING:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xb

    new-array v1, v1, [F

    fill-array-data v1, :array_76c

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 472
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xd

    new-array v1, v1, [Ljava/lang/Class;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/CapeOfThorns;

    aput-object v2, v1, v5

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/ChaliceOfBlood;

    aput-object v2, v1, v7

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/CloakOfShadows;

    aput-object v2, v1, v8

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/HornOfPlenty;

    aput-object v2, v1, v6

    const/4 v2, 0x4

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/MasterThievesArmband;

    aput-object v3, v1, v2

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/SandalsOfNature;

    aput-object v2, v1, v9

    const/4 v2, 0x6

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/TalismanOfForesight;

    aput-object v3, v1, v2

    const/4 v2, 0x7

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/TimekeepersHourglass;

    aput-object v3, v1, v2

    const/16 v2, 0x8

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/UnstableSpellbook;

    aput-object v3, v1, v2

    const/16 v2, 0x9

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/AlchemistsToolkit;

    aput-object v3, v1, v2

    const/16 v2, 0xa

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/DriedRose;

    aput-object v3, v1, v2

    const/16 v2, 0xb

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/LloydsBeacon;

    aput-object v3, v1, v2

    const/16 v2, 0xc

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/EtherealChains;

    aput-object v3, v1, v2

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    .line 487
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    const/16 v1, 0xd

    new-array v1, v1, [F

    fill-array-data v1, :array_786

    iput-object v1, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    .line 488
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    invoke-virtual {v0}, [F->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [F

    iput-object v0, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 489
    return-void

    .line 270
    nop

    :array_632
    .array-data 4
        0x0
        0x40c00000    # 6.0f
        0x40800000    # 4.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40000000    # 2.0f
        0x40000000    # 2.0f
        0x40000000    # 2.0f
        0x40000000    # 2.0f
        0x40000000    # 2.0f
        0x3f800000    # 1.0f
        0x40400000    # 3.0f
    .end array-data

    .line 287
    :array_650
    .array-data 4
        0x0
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x41100000    # 9.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40000000    # 2.0f
    .end array-data

    .line 304
    :array_66e
    .array-data 4
        0x0
        0x40c00000    # 6.0f
        0x40800000    # 4.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40000000    # 2.0f
        0x40000000    # 2.0f
        0x40000000    # 2.0f
        0x40000000    # 2.0f
        0x40000000    # 2.0f
        0x3f800000    # 1.0f
    .end array-data

    .line 321
    :array_68a
    .array-data 4
        0x0
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x0
    .end array-data

    .line 339
    :array_6a6
    .array-data 4
        0x40800000    # 4.0f
        0x40800000    # 4.0f
        0x40800000    # 4.0f
        0x40800000    # 4.0f
        0x40800000    # 4.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x40400000    # 3.0f
        0x3f800000    # 1.0f
    .end array-data

    .line 352
    :array_6c6
    .array-data 4
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x0
        0x40800000    # 4.0f
    .end array-data

    .line 361
    :array_6d4
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40800000    # 4.0f
        0x40800000    # 4.0f
    .end array-data

    .line 375
    :array_6e2
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40800000    # 4.0f
        0x0
        0x40800000    # 4.0f
        0x40400000    # 3.0f
        0x40800000    # 4.0f
        0x41000000    # 8.0f
        0x0
    .end array-data

    .line 386
    :array_6fa
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40800000    # 4.0f
        0x40800000    # 4.0f
        0x40800000    # 4.0f
        0x40400000    # 3.0f
    .end array-data

    .line 396
    :array_70c
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
        0x40a00000    # 5.0f
        0x40800000    # 4.0f
        0x40800000    # 4.0f
        0x40800000    # 4.0f
    .end array-data

    .line 402
    :array_71c
    .array-data 4
        0x0
        0x0
    .end array-data

    .line 411
    :array_724
    .array-data 4
        0x0
        0x0
        0x0
        0x0
        0x0
    .end array-data

    .line 421
    :array_732
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
    .end array-data

    .line 428
    :array_73a
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
        0x40800000    # 4.0f
    .end array-data

    .line 435
    :array_744
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
        0x40800000    # 4.0f
    .end array-data

    .line 442
    :array_74e
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
        0x40800000    # 4.0f
    .end array-data

    .line 449
    :array_758
    .array-data 4
        0x40c00000    # 6.0f
        0x40a00000    # 5.0f
        0x40800000    # 4.0f
    .end array-data

    .line 455
    :array_762
    .array-data 4
        0x40800000    # 4.0f
        0x3f800000    # 1.0f
        0x0
    .end array-data

    .line 470
    :array_76c
    .array-data 4
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x0
    .end array-data

    .line 487
    :array_786
    .array-data 4
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x0
        0x3f800000    # 1.0f
        0x0
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x3f800000    # 1.0f
        0x0
        0x3f800000    # 1.0f
    .end array-data
.end method

.method private constructor <init>(Ljava/lang/String;IFLjava/lang/Class;)V
    .registers 6
    .param p3, "prob"    # F
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(F",
            "Ljava/lang/Class",
            "<+",
            "Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;",
            ">;)V"
        }
    .end annotation

    .prologue
    .line 236
    .local p4, "superClass":Ljava/lang/Class;, "Ljava/lang/Class<+Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;>;"
    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    .line 231
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    .line 237
    iput p3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->prob:F

    .line 238
    iput-object p4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->superClass:Ljava/lang/Class;

    .line 239
    return-void
.end method

.method public static order(Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;)I
    .registers 3
    .param p0, "item"    # Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .prologue
    .line 242
    const/4 v0, 0x0

    .local v0, "i":I
    :goto_1
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->values()[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    move-result-object v1

    array-length v1, v1

    if-ge v0, v1, :cond_1a

    .line 243
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->values()[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    move-result-object v1

    aget-object v1, v1, v0

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->superClass:Ljava/lang/Class;

    invoke-virtual {v1, p0}, Ljava/lang/Class;->isInstance(Ljava/lang/Object;)Z

    move-result v1

    if-eqz v1, :cond_17

    .line 248
    .end local v0    # "i":I
    :goto_16
    return v0

    .line 242
    .restart local v0    # "i":I
    :cond_17
    add-int/lit8 v0, v0, 0x1

    goto :goto_1

    .line 248
    :cond_1a
    instance-of v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/bags/Bag;

    if-eqz v1, :cond_23

    const v1, 0x7fffffff

    :goto_21
    move v0, v1

    goto :goto_16

    :cond_23
    const v1, 0x7ffffffe

    goto :goto_21
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    .registers 2
    .param p0, "name"    # Ljava/lang/String;

    .prologue
    .line 190
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    return-object v0
.end method

.method public static values()[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    .registers 1

    .prologue
    .line 190
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->$VALUES:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    invoke-virtual {v0}, [Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    return-object v0
.end method
