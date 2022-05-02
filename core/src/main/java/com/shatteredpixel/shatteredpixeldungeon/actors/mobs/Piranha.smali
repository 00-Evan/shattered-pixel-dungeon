.class public Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;
.super Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;
.source "Piranha.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;,
        Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Wandering;,
        Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Sleeping;
    }
.end annotation


# direct methods
.method public constructor <init>()V
    .registers 4

    .prologue
    const/4 v2, 0x0

    .line 59
    invoke-direct {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;-><init>()V

    .line 41
    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/PiranhaSprite;

    iput-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->spriteClass:Ljava/lang/Class;

    .line 43
    const/high16 v1, 0x40000000    # 2.0f

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->baseSpeed:F

    .line 45
    const/4 v1, 0x0

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->EXP:I

    .line 47
    const-class v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/food/MysteryMeat;

    iput-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->loot:Ljava/lang/Object;

    .line 48
    const/high16 v1, 0x3f800000    # 1.0f

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->lootChance:F

    .line 50
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Sleeping;

    invoke-direct {v1, p0, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Sleeping;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$1;)V

    iput-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->SLEEPING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    .line 51
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Wandering;

    invoke-direct {v1, p0, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Wandering;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$1;)V

    iput-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->WANDERING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    .line 52
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;

    invoke-direct {v1, p0, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$1;)V

    iput-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->HUNTING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    .line 54
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->SLEEPING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    iput-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->state:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    .line 149
    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/BlobImmunity;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/BlobImmunity;-><init>()V

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/BlobImmunity;->immunities()Ljava/util/HashSet;

    move-result-object v1

    invoke-virtual {v1}, Ljava/util/HashSet;->iterator()Ljava/util/Iterator;

    move-result-object v1

    :cond_3d
    :goto_3d
    invoke-interface {v1}, Ljava/util/Iterator;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_57

    invoke-interface {v1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Class;

    .line 150
    .local v0, "c":Ljava/lang/Class;
    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/blobs/Electricity;

    if-eq v0, v2, :cond_3d

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/blobs/Freezing;

    if-eq v0, v2, :cond_3d

    .line 151
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->immunities:Ljava/util/HashSet;

    invoke-virtual {v2, v0}, Ljava/util/HashSet;->add(Ljava/lang/Object;)Z

    goto :goto_3d

    .line 154
    .end local v0    # "c":Ljava/lang/Class;
    :cond_57
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->immunities:Ljava/util/HashSet;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Burning;

    invoke-virtual {v1, v2}, Ljava/util/HashSet;->add(Ljava/lang/Object;)Z

    .line 61
    sget v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    mul-int/lit8 v1, v1, 0x5

    add-int/lit8 v1, v1, 0xa

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->HT:I

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->HP:I

    .line 62
    sget v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    mul-int/lit8 v1, v1, 0x2

    add-int/lit8 v1, v1, 0xa

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->defenseSkill:I

    .line 63
    return-void
.end method


# virtual methods
.method protected act()Z
    .registers 3

    .prologue
    .line 68
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->water:[Z

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->pos:I

    aget-boolean v0, v0, v1

    if-nez v0, :cond_10

    .line 69
    const/4 v0, 0x0

    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->die(Ljava/lang/Object;)V

    .line 70
    const/4 v0, 0x1

    .line 72
    :goto_f
    return v0

    :cond_10
    invoke-super {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->act()Z

    move-result v0

    goto :goto_f
.end method

.method public attackSkill(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)I
    .registers 3
    .registers 3
    .param p1, "target"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;

    .prologue
    .line 83
    sget v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    mul-int/lit8 v0, v0, 0x2

    add-int/lit8 v0, v0, 0x14

    return v0
.end method

.method public damageRoll()I
    .registers 3

    .prologue
    .line 78
    sget v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    sget v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    mul-int/lit8 v1, v1, 0x2

    add-int/lit8 v1, v1, 0x4

    invoke-static {v0, v1}, Lcom/watabou/utils/Random;->NormalIntRange(II)I

    move-result v0

    return v0
.end method

.method public die(Ljava/lang/Object;)V
    .registers 3
    .param p1, "cause"    # Ljava/lang/Object;

    .prologue
    .line 105
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->die(Ljava/lang/Object;)V

    .line 107
    sget v0, Lcom/shatteredpixel/shatteredpixeldungeon/Statistics;->piranhasKilled:I

    add-int/lit8 v0, v0, 0x1

    sput v0, Lcom/shatteredpixel/shatteredpixeldungeon/Statistics;->piranhasKilled:I

    .line 108
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/Badges;->validatePiranhasKilled()V

    .line 109
    return-void
.end method

.method public drRoll()I
    .registers 3

    .prologue
    .line 88
    const/4 v0, 0x0

    sget v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->depth:I

    invoke-static {v0, v1}, Lcom/watabou/utils/Random;->NormalIntRange(II)I

    move-result v0

    return v0
.end method

.method protected getCloser(I)Z
    .registers 7
    .param p1, "target"    # I

    .prologue
    const/4 v2, 0x1

    const/4 v1, 0x0

    .line 124
    iget-boolean v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->rooted:Z

    if-eqz v3, :cond_7

    .line 133
    :cond_6
    :goto_6
    return v1

    .line 128
    :cond_7
    sget-object v3, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v3, v3, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->water:[Z

    iget-object v4, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->fieldOfView:[Z

    invoke-static {p0, p1, v3, v4, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->findStep(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;I[Z[ZZ)I

    move-result v0

    .line 129
    .local v0, "step":I
    const/4 v3, -0x1

    if-eq v0, v3, :cond_6

    .line 130
    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->move(I)V

    move v1, v2

    .line 131
    goto :goto_6
.end method

.method protected getFurther(I)Z
    .registers 6
    .param p1, "target"    # I

    .prologue
    const/4 v1, 0x1

    .line 139
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v2, v2, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->water:[Z

    iget-object v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->fieldOfView:[Z

    invoke-static {p0, p1, v2, v3, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->flee(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;I[Z[ZZ)I

    move-result v0

    .line 140
    .local v0, "step":I
    const/4 v2, -0x1

    if-eq v0, v2, :cond_12

    .line 141
    invoke-virtual {p0, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->move(I)V

    .line 144
    :goto_11
    return v1

    :cond_12
    const/4 v1, 0x0

    goto :goto_11
.end method

.method public reset()Z
    .registers 2

    .prologue
    .line 118
    const/4 v0, 0x1

    return v0
.end method

.method public spawningWeight()F
    .registers 2

    .prologue
    .line 113
    const/4 v0, 0x0

    return v0
.end method

.method public surprisedBy(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;Z)Z
    .registers 5
    .param p1, "enemy"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;
    .param p2, "attacking"    # Z

    .prologue
    .line 93
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    if-ne p1, v0, :cond_45

    if-eqz p2, :cond_f

    move-object v0, p1

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->canSurpriseAttack()Z

    move-result v0

    if-eqz v0, :cond_45

    .line 94
    :cond_f
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->fieldOfView:[Z

    if-eqz v0, :cond_1e

    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->fieldOfView:[Z

    array-length v0, v0

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->length()I

    move-result v1

    if-eq v0, v1, :cond_2f

    .line 95
    :cond_1e
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->length()I

    move-result v0

    new-array v0, v0, [Z

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->fieldOfView:[Z

    .line 96
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->fieldOfView:[Z

    invoke-virtual {v0, p0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->updateFieldOfView(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;[Z)V

    .line 98
    :cond_2f
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->state:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->SLEEPING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    if-eq v0, v1, :cond_41

    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->fieldOfView:[Z

    iget v1, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->pos:I

    aget-boolean v0, v0, v1

    if-eqz v0, :cond_41

    iget v0, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->invisible:I

    if-lez v0, :cond_43

    :cond_41
    const/4 v0, 0x1

    .line 100
    :goto_42
    return v0

    .line 98
    :cond_43
    const/4 v0, 0x0

    goto :goto_42

    .line 100
    :cond_45
    invoke-super {p0, p1, p2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->surprisedBy(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;Z)Z

    move-result v0

    goto :goto_42
.end method
