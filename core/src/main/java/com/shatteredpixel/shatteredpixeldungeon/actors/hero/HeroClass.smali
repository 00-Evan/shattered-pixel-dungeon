.class public final enum Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;
.super Ljava/lang/Enum;
.source "HeroClass.java"


# annotations
.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Enum",
        "<",
        "Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;",
        ">;"
    }
.end annotation


# static fields
.field private static final synthetic $VALUES:[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

.field public static final enum HUNTRESS:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

.field public static final enum MAGE:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

.field public static final enum ROGUE:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

.field public static final enum WARRIOR:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;


# instance fields
.field private subClasses:[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;


# direct methods
.method static constructor <clinit>()V
    .registers 8

    .prologue
    const/4 v7, 0x3

    const/4 v6, 0x2

    const/4 v5, 0x1

    const/4 v4, 0x0

    .line 72
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v1, "WARRIOR"

    new-array v2, v6, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;->BERSERKER:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    aput-object v3, v2, v4

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;->GLADIATOR:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    aput-object v3, v2, v5

    invoke-direct {v0, v1, v4, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;-><init>(Ljava/lang/String;I[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->WARRIOR:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    .line 73
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v1, "MAGE"

    new-array v2, v6, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;->BATTLEMAGE:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    aput-object v3, v2, v4

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;->WARLOCK:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    aput-object v3, v2, v5

    invoke-direct {v0, v1, v5, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;-><init>(Ljava/lang/String;I[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->MAGE:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    .line 74
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v1, "ROGUE"

    new-array v2, v6, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;->ASSASSIN:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    aput-object v3, v2, v4

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;->FREERUNNER:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    aput-object v3, v2, v5

    invoke-direct {v0, v1, v6, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;-><init>(Ljava/lang/String;I[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ROGUE:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    .line 75
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v1, "HUNTRESS"

    new-array v2, v6, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;->SNIPER:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    aput-object v3, v2, v4

    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;->WARDEN:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    aput-object v3, v2, v5

    invoke-direct {v0, v1, v7, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;-><init>(Ljava/lang/String;I[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;)V

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->HUNTRESS:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    .line 70
    const/4 v0, 0x4

    new-array v0, v0, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->WARRIOR:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    aput-object v1, v0, v4

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->MAGE:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    aput-object v1, v0, v5

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ROGUE:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    aput-object v1, v0, v6

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->HUNTRESS:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    aput-object v1, v0, v7

    sput-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->$VALUES:[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    return-void
.end method

.method private varargs constructor <init>(Ljava/lang/String;I[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;)V
    .registers 4
    .param p3, "subClasses"    # [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "([",
            "Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;",
            ")V"
        }
    .end annotation

    .prologue
    .line 79
    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    .line 80
    iput-object p3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->subClasses:[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    .line 81
    return-void
.end method

.method private static initHuntress(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V
    .registers 4
    .param p0, "hero"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    .prologue
    .line 190
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    new-instance v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Gloves;

    invoke-direct {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Gloves;-><init>()V

    iput-object v2, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->weapon:Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 191
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/SpiritBow;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/SpiritBow;-><init>()V

    .line 192
    .local v0, "bow":Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/SpiritBow;
    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/SpiritBow;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v1

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;->collect()Z

    .line 194
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->quickslot:Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;

    const/4 v2, 0x0

    invoke-virtual {v1, v2, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;->setSlot(ILcom/shatteredpixel/shatteredpixeldungeon/items/Item;)V

    .line 196
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfMindVision;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfMindVision;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfMindVision;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 197
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfLullaby;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfLullaby;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfLullaby;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 198
    return-void
.end method

.method private static initMage(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V
    .registers 4
    .param p0, "hero"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    .prologue
    .line 160
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MagesStaff;

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfMagicMissile;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/WandOfMagicMissile;-><init>()V

    invoke-direct {v0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MagesStaff;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/items/wands/Wand;)V

    .line 162
    .local v0, "staff":Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/MagesStaff;
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    iput-object v0, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->weapon:Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 163
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->weapon:Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;

    invoke-virtual {v1, p0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;->activate(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)V

    .line 165
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->quickslot:Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;

    const/4 v2, 0x0

    invoke-virtual {v1, v2, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;->setSlot(ILcom/shatteredpixel/shatteredpixeldungeon/items/Item;)V

    .line 167
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfUpgrade;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfUpgrade;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfUpgrade;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 168
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLiquidFlame;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLiquidFlame;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLiquidFlame;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

 new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLiquidFlame;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLiquidFlame;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfLiquidFlame;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;


    .line 169
    return-void
.end method

.method private static initRogue(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V
    .registers 5
    .param p0, "hero"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    .prologue
    .line 172
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    new-instance v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Dagger;

    invoke-direct {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/Dagger;-><init>()V

    iput-object v3, v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->weapon:Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;

    invoke-virtual {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 174
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/CloakOfShadows;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/CloakOfShadows;-><init>()V

    .line 175
    .local v0, "cloak":Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/CloakOfShadows;
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    iput-object v0, v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->artifact:Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 176
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    iget-object v2, v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->artifact:Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;

    invoke-virtual {v2, p0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/Artifact;->activate(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)V

    .line 178
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingKnife;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingKnife;-><init>()V

    .line 179
    .local v1, "knives":Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingKnife;
    const/4 v2, 0x3

    invoke-virtual {v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingKnife;->quantity(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v2

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;->collect()Z

    .line 181
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->quickslot:Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;

    const/4 v3, 0x0

    invoke-virtual {v2, v3, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;->setSlot(ILcom/shatteredpixel/shatteredpixeldungeon/items/Item;)V

    .line 182
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->quickslot:Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;

    const/4 v3, 0x1

    invoke-virtual {v2, v3, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;->setSlot(ILcom/shatteredpixel/shatteredpixeldungeon/items/Item;)V

    .line 184
    new-instance v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfMagicMapping;

    invoke-direct {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfMagicMapping;-><init>()V

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfMagicMapping;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 185
    new-instance v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfInvisibility;

    invoke-direct {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfInvisibility;-><init>()V

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfInvisibility;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 186
    return-void
.end method

.method private static initWarrior(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V
    .registers 4
    .param p0, "hero"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    .prologue
    .line 144
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    new-instance v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/WornShortsword;

    invoke-direct {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/melee/WornShortsword;-><init>()V

    iput-object v2, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->weapon:Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/KindOfWeapon;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 145
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingStone;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingStone;-><init>()V

    .line 146
    .local v0, "stones":Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingStone;
    const/4 v1, 0x3

    invoke-virtual {v0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/weapon/missiles/ThrowingStone;->quantity(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v1

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;->collect()Z

    .line 147
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->quickslot:Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;

    const/4 v2, 0x0

    invoke-virtual {v1, v2, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;->setSlot(ILcom/shatteredpixel/shatteredpixeldungeon/items/Item;)V

    .line 149
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->armor:Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;

    if-eqz v1, :cond_31

    .line 150
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->armor:Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;

    new-instance v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/BrokenSeal;

    invoke-direct {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/BrokenSeal;-><init>()V

    invoke-virtual {v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;->affixSeal(Lcom/shatteredpixel/shatteredpixeldungeon/items/BrokenSeal;)V

    .line 153
    :cond_31
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfHealing;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfHealing;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfHealing;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 154
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRage;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRage;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRage;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 155
    return-void
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;
    .registers 2
    .param p0, "name"    # Ljava/lang/String;

    .prologue
    .line 70
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    return-object v0
.end method

.method public static values()[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;
    .registers 1

    .prologue
    .line 70
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->$VALUES:[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    invoke-virtual {v0}, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    return-object v0
.end method


# virtual methods
.method public armorAbilities()[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/ArmorAbility;
    .registers 7

    .prologue
    const/4 v5, 0x3

    const/4 v4, 0x2

    const/4 v3, 0x1

    const/4 v2, 0x0

    .line 213
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$actors$hero$HeroClass:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ordinal()I

    move-result v1

    aget v0, v0, v1

    packed-switch v0, :pswitch_data_70

    .line 215
    new-array v0, v5, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/ArmorAbility;

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/warrior/HeroicLeap;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/warrior/HeroicLeap;-><init>()V

    aput-object v1, v0, v2

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/warrior/Shockwave;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/warrior/Shockwave;-><init>()V

    aput-object v1, v0, v3

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/warrior/Endure;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/warrior/Endure;-><init>()V

    aput-object v1, v0, v4

    .line 221
    :goto_26
    return-object v0

    .line 217
    :pswitch_27
    new-array v0, v5, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/ArmorAbility;

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/mage/ElementalBlast;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/mage/ElementalBlast;-><init>()V

    aput-object v1, v0, v2

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/mage/WildMagic;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/mage/WildMagic;-><init>()V

    aput-object v1, v0, v3

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/mage/WarpBeacon;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/mage/WarpBeacon;-><init>()V

    aput-object v1, v0, v4

    goto :goto_26

    .line 219
    :pswitch_3f
    new-array v0, v5, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/ArmorAbility;

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/rogue/SmokeBomb;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/rogue/SmokeBomb;-><init>()V

    aput-object v1, v0, v2

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/rogue/DeathMark;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/rogue/DeathMark;-><init>()V

    aput-object v1, v0, v3

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/rogue/ShadowClone;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/rogue/ShadowClone;-><init>()V

    aput-object v1, v0, v4

    goto :goto_26

    .line 221
    :pswitch_57
    new-array v0, v5, [Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/ArmorAbility;

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/huntress/SpectralBlades;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/huntress/SpectralBlades;-><init>()V

    aput-object v1, v0, v2

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/huntress/NaturesPower;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/huntress/NaturesPower;-><init>()V

    aput-object v1, v0, v3

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/huntress/SpiritHawk;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/huntress/SpiritHawk;-><init>()V

    aput-object v1, v0, v4

    goto :goto_26

    .line 213
    nop

    :pswitch_data_70
    .packed-switch 0x2
        :pswitch_27
        :pswitch_3f
        :pswitch_57
    .end packed-switch
.end method

.method public desc()Ljava/lang/String;
    .registers 4

    .prologue
    .line 205
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->name()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    const-string v2, "_desc"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    const/4 v2, 0x0

    new-array v2, v2, [Ljava/lang/Object;

    invoke-static {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    return-object v0
.end method

.method public initHero(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V
    .registers 7
    .param p1, "hero"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    .prologue
    .line 85
    iput-object p0, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->heroClass:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    .line 86
    invoke-static {p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Talent;->initClassTalents(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V

    .line 88
    new-instance v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/ClothArmor;

    invoke-direct {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/ClothArmor;-><init>()V

    invoke-virtual {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/ClothArmor;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v0

    .line 89
    .local v0, "i":Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/Challenges;->isItemBlocked(Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;)Z

    move-result v3

    if-nez v3, :cond_1a

    iget-object v3, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->belongings:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/ClothArmor;
PotionOfLiquidFlame
    .end local v0    # "i":Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
    iput-object v0, v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Belongings;->armor:Lcom/shatteredpixel/shatteredpixeldungeon/items/armor/Armor;

    .line 91
    :cond_1a
    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/food/Food;

    invoke-direct {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/food/Food;-><init>()V

    .line 92
    .restart local v0    # "i":Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;
    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/Challenges;->isItemBlocked(Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;)Z

    move-result v3

    if-nez v3, :cond_28

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;->collect()Z

    .line 94
    :cond_28
    new-instance v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/bags/VelvetPouch;

    invoke-direct {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/bags/VelvetPouch;-><init>()V

    invoke-virtual {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/bags/VelvetPouch;->collect()Z

    .line 95
    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon$LimitedDrops;->VELVET_POUCH:Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon$LimitedDrops;

    invoke-virtual {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon$LimitedDrops;->drop()V

    .line 97
    new-instance v2, Lcom/shatteredpixel/shatteredpixeldungeon/items/Waterskin;

    invoke-direct {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Waterskin;-><init>()V

    .line 98
    .local v2, "waterskin":Lcom/shatteredpixel/shatteredpixeldungeon/items/Waterskin;
    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/items/Waterskin;->collect()Z

    .line 100
    new-instance v3, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfIdentify;

    invoke-direct {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfIdentify;-><init>()V

    invoke-virtual {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfIdentify;->identify()Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    .line 102
    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$actors$hero$HeroClass:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ordinal()I

    move-result v4

    aget v3, v3, v4

    packed-switch v3, :pswitch_data_76

    .line 120
    :goto_50
    const/4 v1, 0x0

    .local v1, "s":I
    :goto_51
    sget v3, Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;->SIZE:I

    if-ge v1, v3, :cond_62

    .line 121
    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->quickslot:Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;

    invoke-virtual {v3, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;->getItem(I)Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;

    move-result-object v3

    if-nez v3, :cond_73

    .line 122
    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->quickslot:Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;

    invoke-virtual {v3, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/QuickSlot;->setSlot(ILcom/shatteredpixel/shatteredpixeldungeon/items/Item;)V

    .line 127
    :cond_62
    return-void

    .line 104
    .end local v1    # "s":I
    :pswitch_63
    invoke-static {p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->initWarrior(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V

    goto :goto_50

    .line 108
    :pswitch_67
    invoke-static {p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->initMage(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V

    goto :goto_50

    .line 112
    :pswitch_6b
    invoke-static {p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->initRogue(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V

    goto :goto_50

    .line 116
    :pswitch_6f
    invoke-static {p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->initHuntress(Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;)V

    goto :goto_50

    .line 120
    .restart local v1    # "s":I
    :cond_73
    add-int/lit8 v1, v1, 0x1

    goto :goto_51

    .line 102
    :pswitch_data_76
    .packed-switch 0x1
        :pswitch_63
        :pswitch_67
        :pswitch_6b
        :pswitch_6f
    .end packed-switch
.end method

.method public isUnlocked()Z
    .registers 4

    .prologue
    const/4 v0, 0x1

    .line 290
    invoke-static {}, Lcom/watabou/utils/DeviceCompat;->isDebug()Z

    move-result v1

    if-eqz v1, :cond_8

    .line 300
    :goto_7
    return v0

    .line 292
    :cond_8
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$actors$hero$HeroClass:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ordinal()I

    move-result v2

    aget v1, v1, v2

    packed-switch v1, :pswitch_data_2a

    goto :goto_7

    .line 296
    :pswitch_14
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;->UNLOCK_MAGE:Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/Badges;->isUnlocked(Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;)Z

    move-result v0

    goto :goto_7

    .line 298
    :pswitch_1b
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;->UNLOCK_ROGUE:Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/Badges;->isUnlocked(Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;)Z

    move-result v0

    goto :goto_7

    .line 300
    :pswitch_22
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;->UNLOCK_HUNTRESS:Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/Badges;->isUnlocked(Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;)Z

    move-result v0

    goto :goto_7

    .line 292
    nop

    :pswitch_data_2a
    .packed-switch 0x2
        :pswitch_14
        :pswitch_1b
        :pswitch_22
    .end packed-switch
.end method

.method public masteryBadge()Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;
    .registers 3

    .prologue
    .line 130
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$actors$hero$HeroClass:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ordinal()I

    move-result v1

    aget v0, v0, v1

    packed-switch v0, :pswitch_data_1a

    .line 140
    const/4 v0, 0x0

    :goto_c
    return-object v0

    .line 132
    :pswitch_d
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;->MASTERY_WARRIOR:Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;

    goto :goto_c

    .line 134
    :pswitch_10
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;->MASTERY_MAGE:Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;

    goto :goto_c

    .line 136
    :pswitch_13
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;->MASTERY_ROGUE:Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;

    goto :goto_c

    .line 138
    :pswitch_16
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;->MASTERY_HUNTRESS:Lcom/shatteredpixel/shatteredpixeldungeon/Badges$Badge;

    goto :goto_c

    .line 130
    nop

    :pswitch_data_1a
    .packed-switch 0x1
        :pswitch_d
        :pswitch_10
        :pswitch_13
        :pswitch_16
    .end packed-switch
.end method

.method public perks()[Ljava/lang/String;
    .registers 10

    .prologue
    const/4 v8, 0x4

    const/4 v7, 0x3

    const/4 v6, 0x2

    const/4 v5, 0x1

    const/4 v4, 0x0

    .line 252
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$actors$hero$HeroClass:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ordinal()I

    move-result v1

    aget v0, v0, v1

    packed-switch v0, :pswitch_data_112

    .line 254
    const/4 v0, 0x5

    new-array v0, v0, [Ljava/lang/String;

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "warrior_perk1"

    new-array v3, v4, [Ljava/lang/Object;

    .line 255
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v4

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "warrior_perk2"

    new-array v3, v4, [Ljava/lang/Object;

    .line 256
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v5

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "warrior_perk3"

    new-array v3, v4, [Ljava/lang/Object;

    .line 257
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v6

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "warrior_perk4"

    new-array v3, v4, [Ljava/lang/Object;

    .line 258
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v7

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "warrior_perk5"

    new-array v3, v4, [Ljava/lang/Object;

    .line 259
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v8

    .line 278
    :goto_4f
    return-object v0

    .line 262
    :pswitch_50
    const/4 v0, 0x5

    new-array v0, v0, [Ljava/lang/String;

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "mage_perk1"

    new-array v3, v4, [Ljava/lang/Object;

    .line 263
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v4

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "mage_perk2"

    new-array v3, v4, [Ljava/lang/Object;

    .line 264
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v5

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "mage_perk3"

    new-array v3, v4, [Ljava/lang/Object;

    .line 265
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v6

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "mage_perk4"

    new-array v3, v4, [Ljava/lang/Object;

    .line 266
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v7

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "mage_perk5"

    new-array v3, v4, [Ljava/lang/Object;

    .line 267
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v8

    goto :goto_4f

    .line 270
    :pswitch_90
    const/4 v0, 0x5

    new-array v0, v0, [Ljava/lang/String;

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "rogue_perk1"

    new-array v3, v4, [Ljava/lang/Object;

    .line 271
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v4

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "rogue_perk2"

    new-array v3, v4, [Ljava/lang/Object;

    .line 272
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v5

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "rogue_perk3"

    new-array v3, v4, [Ljava/lang/Object;

    .line 273
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v6

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "rogue_perk4"

    new-array v3, v4, [Ljava/lang/Object;

    .line 274
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v7

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "rogue_perk5"

    new-array v3, v4, [Ljava/lang/Object;

    .line 275
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v8

    goto :goto_4f

    .line 278
    :pswitch_d0
    const/4 v0, 0x5

    new-array v0, v0, [Ljava/lang/String;

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "huntress_perk1"

    new-array v3, v4, [Ljava/lang/Object;

    .line 279
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v4

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "huntress_perk2"

    new-array v3, v4, [Ljava/lang/Object;

    .line 280
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v5

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "huntress_perk3"

    new-array v3, v4, [Ljava/lang/Object;

    .line 281
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v6

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "huntress_perk4"

    new-array v3, v4, [Ljava/lang/Object;

    .line 282
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v7

    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v2, "huntress_perk5"

    new-array v3, v4, [Ljava/lang/Object;

    .line 283
    invoke-static {v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    aput-object v1, v0, v8

    goto/16 :goto_4f

    .line 252
    nop

    :pswitch_data_112
    .packed-switch 0x2
        :pswitch_50
        :pswitch_90
        :pswitch_d0
    .end packed-switch
.end method

.method public splashArt()Ljava/lang/String;
    .registers 3

    .prologue
    .line 239
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$actors$hero$HeroClass:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ordinal()I

    move-result v1

    aget v0, v0, v1

    packed-switch v0, :pswitch_data_18

    .line 241
    const-string v0, "splashes/warrior.jpg"

    .line 247
    :goto_d
    return-object v0

    .line 243
    :pswitch_e
    const-string v0, "splashes/mage.jpg"

    goto :goto_d

    .line 245
    :pswitch_11
    const-string v0, "splashes/rogue.jpg"

    goto :goto_d

    .line 247
    :pswitch_14
    const-string v0, "splashes/huntress.jpg"

    goto :goto_d

    .line 239
    nop

    :pswitch_data_18
    .packed-switch 0x2
        :pswitch_e
        :pswitch_11
        :pswitch_14
    .end packed-switch
.end method

.method public spritesheet()Ljava/lang/String;
    .registers 3

    .prologue
    .line 226
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$actors$hero$HeroClass:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ordinal()I

    move-result v1

    aget v0, v0, v1

    packed-switch v0, :pswitch_data_18

    .line 228
    const-string v0, "sprites/warrior.png"

    .line 234
    :goto_d
    return-object v0

    .line 230
    :pswitch_e
    const-string v0, "sprites/mage.png"

    goto :goto_d

    .line 232
    :pswitch_11
    const-string v0, "sprites/rogue.png"

    goto :goto_d

    .line 234
    :pswitch_14
    const-string v0, "sprites/huntress.png"

    goto :goto_d

    .line 226
    nop

    :pswitch_data_18
    .packed-switch 0x2
        :pswitch_e
        :pswitch_11
        :pswitch_14
    .end packed-switch
.end method

.method public subClasses()[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;
    .registers 2

    .prologue
    .line 209
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->subClasses:[Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroSubClass;

    return-object v0
.end method

.method public title()Ljava/lang/String;
    .registers 4

    .prologue
    .line 201
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->name()Ljava/lang/String;

    move-result-object v1

    const/4 v2, 0x0

    new-array v2, v2, [Ljava/lang/Object;

    invoke-static {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    return-object v0
.end method

.method public unlockMsg()Ljava/lang/String;
    .registers 4

    .prologue
    const/4 v2, 0x0

    .line 305
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass$1;->$SwitchMap$com$shatteredpixel$shatteredpixeldungeon$actors$hero$HeroClass:[I

    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;->ordinal()I

    move-result v1

    aget v0, v0, v1

    packed-switch v0, :pswitch_data_30

    .line 307
    const-string v0, ""

    .line 313
    :goto_e
    return-object v0

    .line 309
    :pswitch_f
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v1, "mage_unlock"

    new-array v2, v2, [Ljava/lang/Object;

    invoke-static {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    goto :goto_e

    .line 311
    :pswitch_1a
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v1, "rogue_unlock"

    new-array v2, v2, [Ljava/lang/Object;

    invoke-static {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    goto :goto_e

    .line 313
    :pswitch_25
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/HeroClass;

    const-string v1, "huntress_unlock"

    new-array v2, v2, [Ljava/lang/Object;

    invoke-static {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v0

    goto :goto_e

    .line 305
    :pswitch_data_30
    .packed-switch 0x2
        :pswitch_f
        :pswitch_1a
        :pswitch_25
    .end packed-switch
.end method
