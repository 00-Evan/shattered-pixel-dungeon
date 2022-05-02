.class Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;
.super Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$Hunting;
.source "Piranha.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x2
    name = "Hunting"
.end annotation


# instance fields
.field final synthetic this$0:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;


# direct methods
.method private constructor <init>(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;)V
    .registers 2

    .prologue
    .line 182
    iput-object p1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;->this$0:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;

    invoke-direct {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$Hunting;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob;)V

    return-void
.end method

.method synthetic constructor <init>(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$1;)V
    .registers 3
    .param p1, "x0"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;
    .param p2, "x1"    # Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$1;

    .prologue
    .line 182
    invoke-direct {p0, p1}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;-><init>(Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;)V

    return-void
.end method


# virtual methods
.method public act(ZZ)Z
    .registers 6
    .param p1, "enemyInFOV"    # Z
    .param p2, "justAlerted"    # Z

    .prologue
    .line 186
    if-eqz p1, :cond_21

    .line 187
    iget-object v0, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;->this$0:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;

    iget-object v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->enemy:Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;

    iget v0, v0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/Char;->pos:I

    sget-object v1, Lcom/shatteredpixel/shatteredpixeldungeon/Dungeon;->level:Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;

    iget-object v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/levels/Level;->water:[Z

    iget-object v2, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;->this$0:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;

    iget v2, v2, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->viewDistance:I

    invoke-static {v0, v1, v2}, Lcom/watabou/utils/PathFinder;->buildDistanceMap(I[ZI)V

    .line 188
    sget-object v0, Lcom/watabou/utils/PathFinder;->distance:[I

    iget-object v1, p0, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha$Hunting;->this$0:Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;

    iget v1, v1, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Piranha;->pos:I

    aget v0, v0, v1

    const v1, 0x7fffffff

    if-eq v0, v1, :cond_26

    const/4 p1, 0x1

    .line 191
    :cond_21
    :goto_21
    invoke-super {p0, p1, p2}, Lcom/shatteredpixel/shatteredpixeldungeon/actors/mobs/Mob$Hunting;->act(ZZ)Z

    move-result v0

    return v0

    .line 188
    :cond_26
    const/4 p1, 0x0

    goto :goto_21
.end method
