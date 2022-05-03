.class public Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;
.super Ljava/lang/Object;
.source "Generator.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    }
.end annotation


# static fields
.field private static final CATEGORY_PROBS:Ljava/lang/String; = "_probs"

.field private static final GENERAL_PROBS:Ljava/lang/String; = "general_probs"

.field private static categoryProbs:Ljava/util/HashMap;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/HashMap",
            "<",
            "Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;",
            "Ljava/lang/Float;",
            ">;"
        }
    .end annotation
.end field

.field private static final floorSetTierProbs:[[F

.field public static final misTiers:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

.field public static final wepTiers:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;


# direct methods
.method static constructor <clinit>()V
    .registers 8

    .prologue
    const/4 v7, 0x3

    const/4 v6, 0x2

    const/4 v5, 0x1

    const/4 v4, 0x0

    const/4 v3, 0x5

    .line 492
    new-array v0, v3, [[F

    new-array v1, v3, [F

    fill-array-data v1, :array_6c

    aput-object v1, v0, v4

    new-array v1, v3, [F

    fill-array-data v1, :array_7a

    aput-object v1, v0, v5

    new-array v1, v3, [F

    fill-array-data v1, :array_88

    aput-object v1, v0, v6

    new-array v1, v3, [F

    fill-array-data v1, :array_96

    aput-object v1, v0, v7

    const/4 v1, 0x4

    new-array v2, v3, [F

    fill-array-data v2, :array_a4

    aput-object v2, v0, v1

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->floorSetTierProbs:[[F

    .line 500
    new-instance v0, Ljava/util/LinkedHashMap;

    invoke-direct {v0}, Ljava/util/LinkedHashMap;-><init>()V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->categoryProbs:Ljava/util/HashMap;

    .line 578
    const/4 v0, 0x6

    new-array v0, v0, [Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v4

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v5

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v6

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v7

    const/4 v1, 0x4

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->WEP_T6:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v3

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->wepTiers:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 601
    new-array v0, v3, [Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T1:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v4

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T2:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v5

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T3:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v6

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T4:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v1, v0, v7

    const/4 v1, 0x4

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->MIS_T5:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    aput-object v2, v0, v1

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->misTiers:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    return-void

    .line 492
    :array_6c
    .array-data 4
        0x0
        0x42960000    # 75.0f
        0x41a00000    # 20.0f
        0x40800000    # 4.0f
        0x3f800000    # 1.0f
    .end array-data

    :array_7a
    .array-data 4
        0x0
        0x41c80000    # 25.0f
        0x42480000    # 50.0f
        0x41a00000    # 20.0f
        0x40a00000    # 5.0f
    .end array-data

    :array_88
    .array-data 4
        0x0
        0x0
        0x42200000    # 40.0f
        0x42480000    # 50.0f
        0x41200000    # 10.0f
    .end array-data

    :array_96
    .array-data 4
        0x0
        0x0
        0x41a00000    # 20.0f
        0x42200000    # 40.0f
        0x42200000    # 40.0f
    .end array-data

    :array_a4
    .array-data 4
        0x0
        0x0
        0x0
        0x41a00000    # 20.0f
        0x42a00000    # 80.0f
    .end array-data
.end method

.method public constructor <init>()V
    .registers 1

    .prologue
    .line 188
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static fullReset()V
    .registers 4

    .prologue
    .line 503
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->generalReset()V

    .line 504
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->values()[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    move-result-object v2

    array-length v3, v2

    const/4 v1, 0x0

    .local v0, "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    :goto_9
    if-ge v1, v3, :cond_13

    aget-object v0, v2, v1

    .line 505
    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->reset(Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;)V

    .line 504
    add-int/lit8 v1, v1, 0x1

    goto :goto_9

    .line 507
    :cond_13
    return-void
.end method

.method public static generalReset()V
    .registers 6

    .prologue
    .line 510
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->values()[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    move-result-object v2

    array-length v3, v2

    const/4 v1, 0x0

    .local v0, "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    :goto_6
    if-ge v1, v3, :cond_18

    aget-object v0, v2, v1

    .line 511
    sget-object v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->categoryProbs:Ljava/util/HashMap;

    iget v5, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->prob:F

    invoke-static {v5}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v5

    invoke-virtual {v4, v0, v5}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    .line 510
    add-int/lit8 v1, v1, 0x1

    goto :goto_6

    .line 513
    :cond_18
    return-void
.end method

.method public static random()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
    .registers 4

    .prologue
    .line 520
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->categoryProbs:Ljava/util/HashMap;

    invoke-static {v1}, Lcom/watabou/utils/Random;->chances(Ljava/util/HashMap;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 521
    .local v0, "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    if-nez v0, :cond_15

    .line 522
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->generalReset()V

    .line 523
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->categoryProbs:Ljava/util/HashMap;

    invoke-static {v1}, Lcom/watabou/utils/Random;->chances(Ljava/util/HashMap;)Ljava/lang/Object;

    move-result-object v0

    .end local v0    # "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 525
    .restart local v0    # "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    :cond_15
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->categoryProbs:Ljava/util/HashMap;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->categoryProbs:Ljava/util/HashMap;

    invoke-virtual {v1, v0}, Ljava/util/HashMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Float;

    invoke-virtual {v1}, Ljava/lang/Float;->floatValue()F

    move-result v1

    const/high16 v3, 0x3f800000    # 1.0f

    sub-float/2addr v1, v3

    invoke-static {v1}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v1

    invoke-virtual {v2, v0, v1}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    .line 526
    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->random(Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v1

    return-object v1
.end method

.method public static random(Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
    .registers 6
    .param p0, "cat"    # Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .prologue
    .line 530
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$items$Generator$Category:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ordinal()I

    move-result v3

    aget v2, v2, v3

    packed-switch v2, :pswitch_data_56

    .line 542
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    invoke-static {v2}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v0

    .line 543
    .local v0, "i":I
    const/4 v2, -0x1

    if-ne v0, v2, :cond_1d

    .line 544
    invoke-static {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->reset(Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;)V

    .line 545
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    invoke-static {v2}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v0

    .line 547
    :cond_1d
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    if-eqz v2, :cond_2a

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    aget v3, v2, v0

    const/high16 v4, 0x3f800000    # 1.0f

    sub-float/2addr v3, v4

    aput v3, v2, v0

    .line 548
    :cond_2a
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    aget-object v2, v2, v0

    invoke-static {v2}, Lcom/watabou/utils/Reflection;->newInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;->random()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v1

    .end local v0    # "i":I
    :cond_38
    :goto_38
    return-object v1

    .line 532
    :pswitch_39
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->randomArmor()Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;

    move-result-object v1

    goto :goto_38

    .line 534
    :pswitch_3e
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->randomWeapon()Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    move-result-object v1

    goto :goto_38

    .line 536
    :pswitch_43
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->randomMissile()Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    move-result-object v1

    goto :goto_38

    .line 538
    :pswitch_48
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->randomArtifact()Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;

    move-result-object v1

    .line 540
    .local v1, "item":Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
    if-nez v1, :cond_38

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->RING:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    invoke-static {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->random(Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v1

    goto :goto_38

    .line 530
    nop

    :pswitch_data_56
    .packed-switch 0x1
        :pswitch_39
        :pswitch_3e
        :pswitch_43
        :pswitch_48
    .end packed-switch
.end method

.method public static random(Ljava/lang/Class;)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
    .registers 2
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/Class",
            "<+",
            "Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;",
            ">;)",
            "Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;"
        }
    .end annotation

    .prologue
    .line 562
    .local p0, "cl":Ljava/lang/Class;, "Ljava/lang/Class<+Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;>;"
    invoke-static {p0}, Lcom/watabou/utils/Reflection;->newInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;->random()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v0

    return-object v0
.end method

.method public static randomArmor()Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;
    .registers 1

    .prologue
    .line 566
    sget v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    div-int/lit8 v0, v0, 0x5

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->randomArmor(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;

    move-result-object v0

    return-object v0
.end method

.method public static randomArmor(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;
    .registers 5
    .param p0, "floorSet"    # I

    .prologue
    .line 571
    const/4 v1, 0x0

    int-to-float v2, p0

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->floorSetTierProbs:[[F

    array-length v3, v3

    add-int/lit8 v3, v3, -0x1

    int-to-float v3, v3

    invoke-static {v1, v2, v3}, Lcom/watabou/utils/GameMath;->gate(FFF)F

    move-result v1

    float-to-int p0, v1

    .line 573
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARMOR:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->floorSetTierProbs:[[F

    aget-object v2, v2, p0

    invoke-static {v2}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v2

    aget-object v1, v1, v2

    invoke-static {v1}, Lcom/watabou/utils/Reflection;->newInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;

    .line 574
    .local v0, "a":Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;
    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;->random()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 575
    return-object v0
.end method

.method public static randomArtifact()Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;
    .registers 5

    .prologue
    .line 626
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 627
    .local v0, "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    invoke-static {v2}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v1

    .line 630
    .local v1, "i":I
    const/4 v2, -0x1

    if-ne v1, v2, :cond_d

    .line 631
    const/4 v2, 0x0

    .line 635
    :goto_c
    return-object v2

    .line 634
    :cond_d
    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    aget v3, v2, v1

    const/high16 v4, 0x3f800000    # 1.0f

    sub-float/2addr v3, v4

    aput v3, v2, v1

    .line 635
    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    aget-object v2, v2, v1

    invoke-static {v2}, Lcom/watabou/utils/Reflection;->newInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;->random()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v2

    check-cast v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;

    goto :goto_c
.end method

.method public static randomMissile()Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;
    .registers 1

    .prologue
    .line 610
    sget v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    div-int/lit8 v0, v0, 0x5

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->randomMissile(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    move-result-object v0

    return-object v0
.end method

.method public static randomMissile(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;
    .registers 6
    .param p0, "floorSet"    # I

    .prologue
    .line 615
    const/4 v2, 0x0

    int-to-float v3, p0

    sget-object v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->floorSetTierProbs:[[F

    array-length v4, v4

    add-int/lit8 v4, v4, -0x1

    int-to-float v4, v4

    invoke-static {v2, v3, v4}, Lcom/watabou/utils/GameMath;->gate(FFF)F

    move-result v2

    float-to-int p0, v2

    .line 617
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->misTiers:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->floorSetTierProbs:[[F

    aget-object v3, v3, p0

    invoke-static {v3}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v3

    aget-object v0, v2, v3

    .line 618
    .local v0, "c":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    iget-object v3, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    invoke-static {v3}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v3

    aget-object v2, v2, v3

    invoke-static {v2}, Lcom/watabou/utils/Reflection;->newInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;

    .line 619
    .local v1, "w":Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;
    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/MissileWeapon;->random()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 620
    return-object v1
.end method

.method public static randomUsingDefaults(Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
    .registers 3
    .param p0, "cat"    # Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .prologue
    .line 554
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    if-nez v0, :cond_9

    .line 555
    invoke-static {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->random(Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v0

    .line 557
    :goto_8
    return-object v0

    :cond_9
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    invoke-static {v1}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v1

    aget-object v0, v0, v1

    invoke-static {v0}, Lcom/watabou/utils/Reflection;->newInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;->random()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v0

    goto :goto_8
.end method

.method public static randomWeapon()Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;
    .registers 1

    .prologue
    .line 588
    sget v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    div-int/lit8 v0, v0, 0x5

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->randomWeapon(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    move-result-object v0

    return-object v0
.end method

.method public static randomWeapon(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;
    .registers 6
    .param p0, "floorSet"    # I

    .prologue
    .line 593
    const/4 v2, 0x0

    int-to-float v3, p0

    sget-object v4, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->floorSetTierProbs:[[F

    array-length v4, v4

    add-int/lit8 v4, v4, -0x1

    int-to-float v4, v4

    invoke-static {v2, v3, v4}, Lcom/watabou/utils/GameMath;->gate(FFF)F

    move-result v2

    float-to-int p0, v2

    .line 595
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->wepTiers:[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->floorSetTierProbs:[[F

    aget-object v3, v3, p0

    invoke-static {v3}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v3

    aget-object v0, v2, v3

    .line 596
    .local v0, "c":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    iget-object v3, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    invoke-static {v3}, Lcom/watabou/utils/Random;->chances([F)I

    move-result v3

    aget-object v2, v2, v3

    invoke-static {v2}, Lcom/watabou/utils/Reflection;->newInstance(Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;

    .line 597
    .local v1, "w":Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;
    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MeleeWeapon;->random()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 598
    return-object v1
.end method

.method public static removeArtifact(Ljava/lang/Class;)Z
    .registers 5
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/Class",
            "<+",
            "Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;",
            ">;)Z"
        }
    .end annotation

    .prologue
    .local p0, "artifact":Ljava/lang/Class;, "Ljava/lang/Class<+Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;>;"
    const/4 v3, 0x0

    .line 640
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 641
    .local v0, "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    const/4 v1, 0x0

    .local v1, "i":I
    :goto_4
    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    array-length v2, v2

    if-ge v1, v2, :cond_24

    .line 642
    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    aget-object v2, v2, v1

    invoke-virtual {v2, p0}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v2

    if-eqz v2, :cond_21

    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    aget v2, v2, v1

    cmpl-float v2, v2, v3

    if-lez v2, :cond_21

    .line 643
    iget-object v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    aput v3, v2, v1

    .line 644
    const/4 v2, 0x1

    .line 647
    :goto_20
    return v2

    .line 641
    :cond_21
    add-int/lit8 v1, v1, 0x1

    goto :goto_4

    .line 647
    :cond_24
    const/4 v2, 0x0

    goto :goto_20
.end method

.method public static reset(Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;)V
    .registers 2
    .param p0, "cat"    # Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .prologue
    .line 516
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    if-eqz v0, :cond_e

    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    invoke-virtual {v0}, [F->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [F

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 517
    :cond_e
    return-void
.end method

.method public static restoreFromBundle(Lcom/watabou/utils/Bundle;)V
    .registers 11
    .param p0, "bundle"    # Lcom/watabou/utils/Bundle;

    .prologue
    const/4 v4, 0x0

    .line 678
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->fullReset()V

    .line 680
    const-string v5, "general_probs"

    invoke-virtual {p0, v5}, Lcom/watabou/utils/Bundle;->contains(Ljava/lang/String;)Z

    move-result v5

    if-eqz v5, :cond_2a

    .line 681
    const-string v5, "general_probs"

    invoke-virtual {p0, v5}, Lcom/watabou/utils/Bundle;->getFloatArray(Ljava/lang/String;)[F

    move-result-object v3

    .line 682
    .local v3, "probs":[F
    const/4 v2, 0x0

    .local v2, "i":I
    :goto_13
    array-length v5, v3

    if-ge v2, v5, :cond_2a

    .line 683
    sget-object v5, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->categoryProbs:Ljava/util/HashMap;

    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->values()[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    move-result-object v6

    aget-object v6, v6, v2

    aget v7, v3, v2

    invoke-static {v7}, Ljava/lang/Float;->valueOf(F)Ljava/lang/Float;

    move-result-object v7

    invoke-virtual {v5, v6, v7}, Ljava/util/HashMap;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    .line 682
    add-int/lit8 v2, v2, 0x1

    goto :goto_13

    .line 687
    .end local v2    # "i":I
    .end local v3    # "probs":[F
    :cond_2a
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->values()[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    move-result-object v6

    array-length v7, v6

    move v5, v4

    :goto_30
    if-ge v5, v7, :cond_83

    aget-object v1, v6, v5

    .line 688
    .local v1, "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->name()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v9}, Ljava/lang/String;->toLowerCase()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, "_probs"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lcom/watabou/utils/Bundle;->contains(Ljava/lang/String;)Z

    move-result v8

    if-eqz v8, :cond_80

    .line 689
    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->name()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v9}, Ljava/lang/String;->toLowerCase()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, "_probs"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-virtual {p0, v8}, Lcom/watabou/utils/Bundle;->getFloatArray(Ljava/lang/String;)[F

    move-result-object v3

    .line 690
    .restart local v3    # "probs":[F
    iget-object v8, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    if-eqz v8, :cond_80

    array-length v8, v3

    iget-object v9, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    array-length v9, v9

    if-ne v8, v9, :cond_80

    .line 691
    iput-object v3, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    .line 687
    .end local v3    # "probs":[F
    :cond_80
    add-int/lit8 v5, v5, 0x1

    goto :goto_30

    .line 697
    .end local v1    # "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    :cond_83
    const-string v5, "spawned_artifacts"

    invoke-virtual {p0, v5}, Lcom/watabou/utils/Bundle;->contains(Ljava/lang/String;)Z

    move-result v5

    if-eqz v5, :cond_b3

    .line 698
    const-string v5, "spawned_artifacts"

    invoke-virtual {p0, v5}, Lcom/watabou/utils/Bundle;->getClassArray(Ljava/lang/String;)[Ljava/lang/Class;

    move-result-object v5

    array-length v6, v5

    :goto_92
    if-ge v4, v6, :cond_b3

    aget-object v0, v5, v4

    .line 699
    .local v0, "artifact":Ljava/lang/Class;, "Ljava/lang/Class<+Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;>;"
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->ARTIFACT:Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    .line 700
    .restart local v1    # "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    const/4 v2, 0x0

    .restart local v2    # "i":I
    :goto_99
    iget-object v7, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    array-length v7, v7

    if-ge v2, v7, :cond_b0

    .line 701
    iget-object v7, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->classes:[Ljava/lang/Class;

    aget-object v7, v7, v2

    invoke-virtual {v7, v0}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v7

    if-eqz v7, :cond_ad

    .line 702
    iget-object v7, v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    const/4 v8, 0x0

    aput v8, v7, v2

    .line 700
    :cond_ad
    add-int/lit8 v2, v2, 0x1

    goto :goto_99

    .line 698
    :cond_b0
    add-int/lit8 v4, v4, 0x1

    goto :goto_92

    .line 708
    .end local v0    # "artifact":Ljava/lang/Class;, "Ljava/lang/Class<+Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;>;"
    .end local v1    # "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    .end local v2    # "i":I
    :cond_b3
    return-void
.end method

.method public static storeInBundle(Lcom/watabou/utils/Bundle;)V
    .registers 11
    .param p0, "bundle"    # Lcom/watabou/utils/Bundle;

    .prologue
    const/4 v5, 0x0

    .line 654
    sget-object v6, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator;->categoryProbs:Ljava/util/HashMap;

    invoke-virtual {v6}, Ljava/util/HashMap;->values()Ljava/util/Collection;

    move-result-object v6

    new-array v7, v5, [Ljava/lang/Float;

    invoke-interface {v6, v7}, Ljava/util/Collection;->toArray([Ljava/lang/Object;)[Ljava/lang/Object;

    move-result-object v1

    check-cast v1, [Ljava/lang/Float;

    .line 655
    .local v1, "genProbs":[Ljava/lang/Float;
    array-length v6, v1

    new-array v4, v6, [F

    .line 656
    .local v4, "storeProbs":[F
    const/4 v2, 0x0

    .local v2, "i":I
    :goto_13
    array-length v6, v4

    if-ge v2, v6, :cond_21

    .line 657
    aget-object v6, v1, v2

    invoke-virtual {v6}, Ljava/lang/Float;->floatValue()F

    move-result v6

    aput v6, v4, v2

    .line 656
    add-int/lit8 v2, v2, 0x1

    goto :goto_13

    .line 659
    :cond_21
    const-string v6, "general_probs"

    invoke-virtual {p0, v6, v4}, Lcom/watabou/utils/Bundle;->put(Ljava/lang/String;[F)V

    .line 661
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->values()[Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;

    move-result-object v6

    array-length v7, v6

    :goto_2b
    if-ge v5, v7, :cond_70

    aget-object v0, v6, v5

    .line 662
    .local v0, "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    iget-object v8, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    if-nez v8, :cond_36

    .line 661
    :cond_33
    :goto_33
    add-int/lit8 v5, v5, 0x1

    goto :goto_2b

    .line 663
    :cond_36
    const/4 v3, 0x0

    .line 664
    .local v3, "needsStore":Z
    const/4 v2, 0x0

    :goto_38
    iget-object v8, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    array-length v8, v8

    if-ge v2, v8, :cond_4a

    .line 665
    iget-object v8, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    aget v8, v8, v2

    iget-object v9, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->defaultProbs:[F

    aget v9, v9, v2

    cmpl-float v8, v8, v9

    if-eqz v8, :cond_6d

    .line 666
    const/4 v3, 0x1

    .line 671
    :cond_4a
    if-eqz v3, :cond_33

    .line 672
    new-instance v8, Ljava/lang/StringBuilder;

    invoke-direct {v8}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->name()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v9}, Ljava/lang/String;->toLowerCase()Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    const-string v9, "_probs"

    invoke-virtual {v8, v9}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    iget-object v9, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;->probs:[F

    invoke-virtual {p0, v8, v9}, Lcom/watabou/utils/Bundle;->put(Ljava/lang/String;[F)V

    goto :goto_33

    .line 664
    :cond_6d
    add-int/lit8 v2, v2, 0x1

    goto :goto_38

    .line 675
    .end local v0    # "cat":Lcom/shatteredpixel/shatteredpixeldungeon/items/Generator$Category;
    .end local v3    # "needsStore":Z
    :cond_70
    return-void
.end method
