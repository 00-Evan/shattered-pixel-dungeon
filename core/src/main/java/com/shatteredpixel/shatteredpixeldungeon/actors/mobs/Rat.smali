.class public Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;
.super Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;
.source "Rat.java"


# static fields
.field private static final RAT_ALLY:Ljava/lang/String; = "rat_ally"


# direct methods
.method public constructor <init>()V
    .registers 2

    .prologue
    .line 32
    invoke-direct {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;-><init>()V

    .line 35
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/RatSprite;

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->spriteClass:Ljava/lang/Class;

    .line 37
    const/16 v0, 0x8

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->HT:I

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->HP:I

    .line 38
    const/4 v0, 0x2

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->defenseSkill:I

    .line 39
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/potions/PotionOfHealing;

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->loot:Ljava/lang/Object;

    .line 40
    const/high16 v0, 0x3f800000    # 1.0f

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->lootChance:F

    .line 42
    const/4 v0, 0x5

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->maxLvl:I

    .line 43
    return-void
.end method


# virtual methods
.method protected act()Z
    .registers 3

    .prologue
    .line 47
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->heroFOV:[Z

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->pos:I

    aget-boolean v0, v0, v1

    if-eqz v0, :cond_20

    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->armorAbility:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/ArmorAbility;

    instance-of v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/abilities/Ratmogrify;

    if-eqz v0, :cond_20

    .line 48
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;->ALLY:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->alignment:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;

    .line 49
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->state:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->SLEEPING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    if-ne v0, v1, :cond_20

    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->WANDERING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->state:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    .line 51
    :cond_20
    invoke-super {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->act()Z

    move-result v0

    return v0
.end method

.method public attackSkill(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)I
    .registers 3
    .param p1, "target"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;

    .prologue
    .line 61
    const/16 v0, 0x8

    return v0
.end method

.method public damageRoll()I
    .registers 3

    .prologue
    .line 56
    const/4 v0, 0x1

    const/4 v1, 0x4

    invoke-static {v0, v1}, Lcom/watabou/utils/Random;->NormalIntRange(II)I

    move-result v0

    return v0
.end method

.method public drRoll()I
    .registers 3

    .prologue
    .line 66
    const/4 v0, 0x0

    const/4 v1, 0x1

    invoke-static {v0, v1}, Lcom/watabou/utils/Random;->NormalIntRange(II)I

    move-result v0

    return v0
.end method

.method public restoreFromBundle(Lcom/watabou/utils/Bundle;)V
    .registers 3
    .param p1, "bundle"    # Lcom/watabou/utils/Bundle;

    .prologue
    .line 79
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->restoreFromBundle(Lcom/watabou/utils/Bundle;)V

    .line 80
    const-string v0, "rat_ally"

    invoke-virtual {p1, v0}, Lcom/watabou/utils/Bundle;->contains(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_f

    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;->ALLY:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->alignment:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;

    .line 81
    :cond_f
    return-void
.end method

.method public storeInBundle(Lcom/watabou/utils/Bundle;)V
    .registers 4
    .param p1, "bundle"    # Lcom/watabou/utils/Bundle;

    .prologue
    .line 73
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->storeInBundle(Lcom/watabou/utils/Bundle;)V

    .line 74
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Rat;->alignment:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;->ALLY:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Alignment;

    if-ne v0, v1, :cond_f

    const-string v0, "rat_ally"

    const/4 v1, 0x1

    invoke-virtual {p1, v0, v1}, Lcom/watabou/utils/Bundle;->put(Ljava/lang/String;Z)V

    .line 75
    :cond_f
    return-void
.end method
