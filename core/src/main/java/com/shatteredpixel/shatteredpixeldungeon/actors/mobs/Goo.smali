.class public Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;
.super Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;
.source "Goo.java"


# instance fields
.field private final HEALINC:Ljava/lang/String;

.field private final PUMPEDUP:Ljava/lang/String;

.field private healInc:I

.field private pumpedUp:I


# direct methods
.method public constructor <init>()V
    .registers 3

    .prologue
    .line 44
    invoke-direct {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;-><init>()V

    .line 47
    const/16 v0, 0x100

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->isChallenged(I)Z

    move-result v0

    if-eqz v0, :cond_41

    const/16 v0, 0x78

    :goto_d
    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    .line 48
    const/16 v0, 0xa

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->EXP:I

    .line 49
    const/16 v0, 0x8

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->defenseSkill:I

    .line 50
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->spriteClass:Ljava/lang/Class;

    .line 52
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->properties:Ljava/util/HashSet;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Property;->BOSS:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Property;

    invoke-virtual {v0, v1}, Ljava/util/HashSet;->add(Ljava/lang/Object;)Z

    .line 53
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->properties:Ljava/util/HashSet;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Property;->DEMONIC:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Property;

    invoke-virtual {v0, v1}, Ljava/util/HashSet;->add(Ljava/lang/Object;)Z

    .line 54
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->properties:Ljava/util/HashSet;

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Property;->ACIDIC:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char$Property;

    invoke-virtual {v0, v1}, Ljava/util/HashSet;->add(Ljava/lang/Object;)Z

    .line 57
    const/4 v0, 0x0

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    .line 58
    const/4 v0, 0x1

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    .line 264
    const-string v0, "pumpedup"

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->PUMPEDUP:Ljava/lang/String;

    .line 265
    const-string v0, "healinc"

    iput-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HEALINC:Ljava/lang/String;

    return-void

    .line 47
    :cond_41
    const/16 v0, 0x64

    goto :goto_d
.end method


# virtual methods
.method public act()Z
    .registers 6

    .prologue
    const/4 v4, 0x0

    .line 93
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->water:[Z

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    aget-boolean v1, v1, v2

    if-eqz v1, :cond_84

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-ge v1, v2, :cond_84

    .line 94
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    add-int/2addr v1, v2

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    .line 96
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    const-class v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;

    invoke-virtual {v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->buff(Ljava/lang/Class;)Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Buff;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;

    .line 97
    .local v0, "lock":Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;
    if-eqz v0, :cond_2c

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    mul-int/lit8 v1, v1, 0x2

    int-to-float v1, v1

    invoke-virtual {v0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;->removeTime(F)V

    .line 99
    :cond_2c
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->heroFOV:[Z

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    aget-boolean v1, v1, v2

    if-eqz v1, :cond_45

    .line 100
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;->emitter()Lcom/watabou/noosa/particles/Emitter;

    move-result-object v1

    invoke-static {v4}, Lcom/shatteredpixel/shatteredpixeldungeon/effects/Speck;->factory(I)Lcom/watabou/noosa/particles/Emitter$Factory;

    move-result-object v2

    iget v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    invoke-virtual {v1, v2, v3}, Lcom/watabou/noosa/particles/Emitter;->burst(Lcom/watabou/noosa/particles/Emitter$Factory;I)V

    .line 102
    :cond_45
    const/16 v1, 0x100

    invoke-static {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->isChallenged(I)Z

    move-result v1

    if-eqz v1, :cond_58

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    const/4 v2, 0x3

    if-ge v1, v2, :cond_58

    .line 103
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    .line 105
    :cond_58
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    mul-int/lit8 v1, v1, 0x2

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-le v1, v2, :cond_74

    .line 106
    invoke-static {v4}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossHealthBar;->bleed(Z)V

    .line 107
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    check-cast v1, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;

    invoke-virtual {v1, v4}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;->spray(Z)V

    .line 108
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    invoke-static {v1, v2}, Ljava/lang/Math;->min(II)I

    move-result v1

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    .line 114
    .end local v0    # "lock":Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;
    :cond_74
    :goto_74
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->state:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->SLEEPING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    if-eq v1, v2, :cond_7f

    .line 115
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->seal()V

    .line 118
    :cond_7f
    invoke-super {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->act()Z

    move-result v1

    return v1

    .line 111
    :cond_84
    const/4 v1, 0x1

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    goto :goto_74
.end method

.method public attack(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;FFF)Z
    .registers 7
    .param p1, "enemy"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;
    .param p2, "dmgMulti"    # F
    .param p3, "dmgBonus"    # F
    .param p4, "accMulti"    # F

    .prologue
    .line 209
    invoke-super {p0, p1, p2, p3, p4}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->attack(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;FFF)Z

    move-result v0

    .line 210
    .local v0, "result":Z
    const/4 v1, 0x0

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    .line 211
    return v0
.end method

.method public attackProc(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;I)I
    .registers 6
    .param p1, "enemy"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;
    .param p2, "damage"    # I

    .prologue
    .line 136
    invoke-super {p0, p1, p2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->attackProc(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;I)I

    move-result p2

    .line 137
    const/4 v0, 0x3

    invoke-static {v0}, Lcom/watabou/utils/Random;->Int(I)I

    move-result v0

    if-nez v0, :cond_1f

    .line 138
    const-class v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Ooze;

    invoke-static {p1, v0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Buff;->affect(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;Ljava/lang/Class;)Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Buff;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Ooze;

    const/high16 v1, 0x41a00000    # 20.0f

    invoke-virtual {v0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Ooze;->set(F)V

    .line 139
    iget-object v0, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    const/4 v1, 0x0

    const/4 v2, 0x5

    invoke-virtual {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;->burst(II)V

    .line 142
    :cond_1f
    iget v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-lez v0, :cond_2d

    .line 143
    sget-object v0, Lcom/watabou/noosa/Camera;->main:Lcom/watabou/noosa/Camera;

    const/high16 v1, 0x40400000    # 3.0f

    const v2, 0x3e4ccccd    # 0.2f

    invoke-virtual {v0, v1, v2}, Lcom/watabou/noosa/Camera;->shake(FF)V

    .line 146
    :cond_2d
    return p2
.end method

.method public attackSkill(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)I
    .registers 5
    .param p1, "target"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;

    .prologue
    .line 74
    const/16 v0, 0xa

    .line 75
    .local v0, "attack":I
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    mul-int/lit8 v1, v1, 0x2

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-gt v1, v2, :cond_c

    const/16 v0, 0xf

    .line 76
    :cond_c
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-lez v1, :cond_12

    mul-int/lit8 v0, v0, 0x2

    .line 77
    :cond_12
    return v0
.end method

.method protected canAttack(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)Z
    .registers 6
    .param p1, "enemy"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;

    .prologue
    const/4 v3, 0x7

    .line 123
    iget v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-lez v0, :cond_3c

    .line 126
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget v1, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->pos:I

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    invoke-virtual {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->distance(II)I

    move-result v0

    const/4 v1, 0x2

    if-gt v0, v1, :cond_3a

    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/mechanics/Ballistica;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    iget v2, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->pos:I

    invoke-direct {v0, v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/mechanics/Ballistica;-><init>(III)V

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/mechanics/Ballistica;->collisionPos:Ljava/lang/Integer;

    .line 127
    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    iget v1, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->pos:I

    if-ne v0, v1, :cond_3a

    new-instance v0, Lcom/shatteredpixel/shatteredpixeldungeon/mechanics/Ballistica;

    iget v1, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->pos:I

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    invoke-direct {v0, v1, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/mechanics/Ballistica;-><init>(III)V

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/mechanics/Ballistica;->collisionPos:Ljava/lang/Integer;

    .line 128
    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    if-ne v0, v1, :cond_3a

    const/4 v0, 0x1

    .line 130
    :goto_39
    return v0

    .line 128
    :cond_3a
    const/4 v0, 0x0

    goto :goto_39

    .line 130
    :cond_3c
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->canAttack(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)Z

    move-result v0

    goto :goto_39
.end method

.method public damage(ILjava/lang/Object;)V
    .registers 11
    .param p1, "dmg"    # I
    .param p2, "src"    # Ljava/lang/Object;

    .prologue
    const/4 v3, 0x1

    const/4 v4, 0x0

    .line 225
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossHealthBar;->isAssigned()Z

    move-result v2

    if-nez v2, :cond_10

    .line 226
    invoke-static {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossHealthBar;->assignBoss(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;)V

    .line 227
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    invoke-virtual {v2}, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->seal()V

    .line 229
    :cond_10
    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    mul-int/lit8 v2, v2, 0x2

    iget v5, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-gt v2, v5, :cond_5f

    move v0, v3

    .line 230
    .local v0, "bleeding":Z
    :goto_19
    invoke-super {p0, p1, p2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->damage(ILjava/lang/Object;)V

    .line 231
    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    mul-int/lit8 v2, v2, 0x2

    iget v5, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-gt v2, v5, :cond_4c

    if-nez v0, :cond_4c

    .line 232
    invoke-static {v3}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossHealthBar;->bleed(Z)V

    .line 233
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    const/high16 v5, 0xff0000

    const-string v6, "enraged"

    new-array v7, v4, [Ljava/lang/Object;

    invoke-static {p0, v6, v7}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v6

    new-array v7, v4, [Ljava/lang/Object;

    invoke-virtual {v2, v5, v6, v7}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;->showStatus(ILjava/lang/String;[Ljava/lang/Object;)V

    .line 234
    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    check-cast v2, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;

    invoke-virtual {v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;->spray(Z)V

    .line 235
    const-string v2, "gluuurp"

    new-array v3, v4, [Ljava/lang/Object;

    invoke-static {p0, v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {p0, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->yell(Ljava/lang/String;)V

    .line 237
    :cond_4c
    sget-object v2, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->hero:Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;

    const-class v3, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;

    invoke-virtual {v2, v3}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/hero/Hero;->buff(Ljava/lang/Class;)Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/Buff;

    move-result-object v1

    check-cast v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;

    .line 238
    .local v1, "lock":Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;
    if-eqz v1, :cond_5e

    mul-int/lit8 v2, p1, 0x2

    int-to-float v2, v2

    invoke-virtual {v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;->addTime(F)V

    .line 239
    :cond_5e
    return-void

    .end local v0    # "bleeding":Z
    .end local v1    # "lock":Lcom/shatteredpixel/shatteredpixeldungeon/actors/buffs/LockedFloor;
    :cond_5f
    move v0, v4

    .line 229
    goto :goto_19
.end method

.method public damageRoll()I
    .registers 5

    .prologue
    .line 62
    const/4 v1, 0x1

    .line 63
    .local v1, "min":I
    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    mul-int/lit8 v2, v2, 0x2

    iget v3, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-gt v2, v3, :cond_1a

    const/16 v0, 0xc

    .line 64
    .local v0, "max":I
    :goto_b
    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-lez v2, :cond_1d

    .line 65
    const/4 v2, 0x0

    iput v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    .line 66
    const/4 v2, 0x3

    mul-int/lit8 v3, v0, 0x3

    invoke-static {v2, v3}, Lcom/watabou/utils/Random;->NormalIntRange(II)I

    move-result v2

    .line 68
    :goto_19
    return v2

    .line 63
    .end local v0    # "max":I
    :cond_1a
    const/16 v0, 0x8

    goto :goto_b

    .line 68
    .restart local v0    # "max":I
    :cond_1d
    invoke-static {v1, v0}, Lcom/watabou/utils/Random;->NormalIntRange(II)I

    move-result v2

    goto :goto_19
.end method

.method public defenseSkill(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)I
    .registers 6
    .param p1, "enemy"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;

    .prologue
    .line 82
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->defenseSkill(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)I

    move-result v0

    int-to-double v2, v0

    iget v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    mul-int/lit8 v0, v0, 0x2

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-gt v0, v1, :cond_12

    const-wide/high16 v0, 0x3ff8000000000000L    # 1.5

    :goto_f
    mul-double/2addr v0, v2

    double-to-int v0, v0

    return v0

    :cond_12
    const-wide/high16 v0, 0x3ff0000000000000L    # 1.0

    goto :goto_f
.end method

.method public die(Ljava/lang/Object;)V
    .registers 5
    .param p1, "cause"    # Ljava/lang/Object;

    .prologue
    .line 244
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->die(Ljava/lang/Object;)V

    .line 246
    sget-object v0, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    new-instance v1, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRemoveCurse;

    invoke-direct {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/items/scrolls/ScrollOfRemoveCurse;-><init>()V

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    invoke-virtual {v0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->drop(Lcom/shatteredpixel/shatteredpixeldungeon/items/Item;I)Lcom/shatteredpixel/shatteredpixeldungeon/items/Heap;

    move-result-object v0

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/Heap;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/ItemSprite;

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/ItemSprite;->drop()V

    .line 247
    return-void
.end method

.method protected doAttack(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)Z
    .registers 9
    .param p1, "enemy"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;

    .prologue
    const/4 v2, 0x2

    const/4 v3, 0x1

    const/4 v4, 0x0

    .line 160
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-ne v1, v3, :cond_1e

    .line 161
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    .line 162
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    check-cast v1, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    invoke-virtual {v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;->pumpUp(I)V

    .line 164
    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->attackDelay()F

    move-result v1

    invoke-virtual {p0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->spend(F)V

    .line 203
    :goto_1d
    return v3

    .line 167
    :cond_1e
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-ge v1, v2, :cond_31

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    mul-int/lit8 v1, v1, 0x2

    iget v5, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-gt v1, v5, :cond_4b

    move v1, v2

    :goto_2b
    invoke-static {v1}, Lcom/watabou/utils/Random;->Int(I)I

    move-result v1

    if-lez v1, :cond_6d

    .line 169
    :cond_31
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->heroFOV:[Z

    iget v5, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    aget-boolean v0, v1, v5

    .line 171
    .local v0, "visible":Z
    if-eqz v0, :cond_55

    .line 172
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-lt v1, v2, :cond_4d

    .line 173
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    check-cast v1, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;->pumpAttack()V

    .line 185
    :goto_46
    if-nez v0, :cond_6b

    move v1, v3

    :goto_49
    move v3, v1

    goto :goto_1d

    .line 167
    .end local v0    # "visible":Z
    :cond_4b
    const/4 v1, 0x5

    goto :goto_2b

    .line 175
    .restart local v0    # "visible":Z
    :cond_4d
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    iget v2, p1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->pos:I

    invoke-virtual {v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;->attack(I)V

    goto :goto_46

    .line 178
    :cond_55
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-lt v1, v2, :cond_60

    .line 179
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    check-cast v1, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;->triggerEmitters()V

    .line 181
    :cond_60
    invoke-virtual {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->attack(Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;)Z

    .line 182
    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->attackDelay()F

    move-result v1

    invoke-virtual {p0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->spend(F)V

    goto :goto_46

    :cond_6b
    move v1, v4

    .line 185
    goto :goto_49

    .line 189
    .end local v0    # "visible":Z
    :cond_6d
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    .line 190
    const/16 v1, 0x100

    invoke-static {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->isChallenged(I)Z

    move-result v1

    if-eqz v1, :cond_81

    .line 191
    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    add-int/lit8 v1, v1, 0x1

    iput v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    .line 194
    :cond_81
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    check-cast v1, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    invoke-virtual {v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;->pumpUp(I)V

    .line 196
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->heroFOV:[Z

    iget v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pos:I

    aget-boolean v1, v1, v2

    if-eqz v1, :cond_b2

    .line 197
    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    const/high16 v2, 0xff0000

    const-string v5, "!!!"

    new-array v6, v4, [Ljava/lang/Object;

    invoke-static {p0, v5, v6}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v5

    new-array v6, v4, [Ljava/lang/Object;

    invoke-virtual {v1, v2, v5, v6}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;->showStatus(ILjava/lang/String;[Ljava/lang/Object;)V

    .line 198
    const-string v1, "pumpup"

    new-array v2, v4, [Ljava/lang/Object;

    invoke-static {p0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    new-array v2, v4, [Ljava/lang/Object;

    invoke-static {v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/utils/GLog;->n(Ljava/lang/String;[Ljava/lang/Object;)V

    .line 201
    :cond_b2
    invoke-virtual {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->attackDelay()F

    move-result v1

    invoke-virtual {p0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->spend(F)V

    goto/16 :goto_1d
.end method

.method public drRoll()I
    .registers 3

    .prologue
    .line 87
    const/4 v0, 0x0

    const/4 v1, 0x2

    invoke-static {v0, v1}, Lcom/watabou/utils/Random;->NormalIntRange(II)I

    move-result v0

    return v0
.end method

.method protected getCloser(I)Z
    .registers 3
    .param p1, "target"    # I

    .prologue
    .line 216
    iget v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-eqz v0, :cond_c

    .line 217
    const/4 v0, 0x0

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    .line 218
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;->idle()V

    .line 220
    :cond_c
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->getCloser(I)Z

    move-result v0

    return v0
.end method

.method public notice()V
    .registers 4

    .prologue
    .line 251
    invoke-super {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->notice()V

    .line 252
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossHealthBar;->isAssigned()Z

    move-result v1

    if-nez v1, :cond_3b

    .line 253
    invoke-static {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossHealthBar;->assignBoss(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;)V

    .line 254
    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    invoke-virtual {v1}, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->seal()V

    .line 255
    const-string v1, "notice"

    const/4 v2, 0x0

    new-array v2, v2, [Ljava/lang/Object;

    invoke-static {p0, v1, v2}, Lcom/shatteredpixel/shatteredpixeldungeon/messages/Messages;->get(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {p0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->yell(Ljava/lang/String;)V

    .line 256
    invoke-static {}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Actor;->chars()Ljava/util/HashSet;

    move-result-object v1

    invoke-virtual {v1}, Ljava/util/HashSet;->iterator()Ljava/util/Iterator;

    move-result-object v1

    :cond_25
    :goto_25
    invoke-interface {v1}, Ljava/util/Iterator;->hasNext()Z

    move-result v2

    if-eqz v2, :cond_3b

    invoke-interface {v1}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;

    .line 257
    .local v0, "ch":Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;
    instance-of v2, v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/DriedRose$GhostHero;

    if-eqz v2, :cond_25

    .line 258
    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/DriedRose$GhostHero;

    .end local v0    # "ch":Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;
    invoke-virtual {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/items/artifacts/DriedRose$GhostHero;->sayBoss()V

    goto :goto_25

    .line 262
    :cond_3b
    return-void
.end method

.method public restoreFromBundle(Lcom/watabou/utils/Bundle;)V
    .registers 4
    .param p1, "bundle"    # Lcom/watabou/utils/Bundle;

    .prologue
    .line 279
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->restoreFromBundle(Lcom/watabou/utils/Bundle;)V

    .line 281
    const-string v0, "pumpedup"

    invoke-virtual {p1, v0}, Lcom/watabou/utils/Bundle;->getInt(Ljava/lang/String;)I

    move-result v0

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    .line 282
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->state:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->SLEEPING:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$AiState;

    if-eq v0, v1, :cond_14

    invoke-static {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossHealthBar;->assignBoss(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;)V

    .line 283
    :cond_14
    iget v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HP:I

    mul-int/lit8 v0, v0, 0x2

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->HT:I

    if-gt v0, v1, :cond_20

    const/4 v0, 0x1

    invoke-static {v0}, Lcom/shatteredpixel/shatteredpixeldungeon/ui/BossHealthBar;->bleed(Z)V

    .line 286
    :cond_20
    const-string v0, "healinc"

    invoke-virtual {p1, v0}, Lcom/watabou/utils/Bundle;->getInt(Ljava/lang/String;)I

    move-result v0

    iput v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    .line 288
    return-void
.end method

.method public storeInBundle(Lcom/watabou/utils/Bundle;)V
    .registers 4
    .param p1, "bundle"    # Lcom/watabou/utils/Bundle;

    .prologue
    .line 270
    invoke-super {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->storeInBundle(Lcom/watabou/utils/Bundle;)V

    .line 272
    const-string v0, "pumpedup"

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    invoke-virtual {p1, v0, v1}, Lcom/watabou/utils/Bundle;->put(Ljava/lang/String;I)V

    .line 273
    const-string v0, "healinc"

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->healInc:I

    invoke-virtual {p1, v0, v1}, Lcom/watabou/utils/Bundle;->put(Ljava/lang/String;I)V

    .line 274
    return-void
.end method

.method public updateSpriteState()V
    .registers 3

    .prologue
    .line 151
    invoke-super {p0}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;->updateSpriteState()V

    .line 153
    iget v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    if-lez v0, :cond_10

    .line 154
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->sprite:Lcom/shatteredpixel/shatteredpixeldungeon/sprites/CharSprite;

    check-cast v0, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;

    iget v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Goo;->pumpedUp:I

    invoke-virtual {v0, v1}, Lcom/shatteredpixel/shatteredpixeldungeon/sprites/GooSprite;->pumpUp(I)V

    .line 156
    :cond_10
    return-void
.end method
