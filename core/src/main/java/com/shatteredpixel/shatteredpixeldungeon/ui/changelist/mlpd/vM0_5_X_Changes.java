package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShopGuardEye;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlueNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BombGnollTricksterSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CausticSlimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM150Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM275Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300AttackSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300DeathBallSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300SpiderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM720Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM75Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireAcidicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireGhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireMagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceCryStalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceFireScorpioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceGolemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KagenoNusujinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MurdererSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NxhySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyzSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSwarmSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDICLRTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkullShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlylSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ViewASprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WandmakerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class vM0_5_X_Changes {

    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        add_v0_6_0_Changes(changeInfos);
        add_v0_5_5_Changes(changeInfos);
        add_v0_5_4_Changes(changeInfos);
        add_v0_5_2_Changes(changeInfos);
        add_v0_5_1_Changes(changeInfos);
    }

    public static void add_v0_6_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes2 = new ChangeInfo("即将推出-下半段更新", true, "");
        changes2.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes2);

        ChangeInfo changes = new ChangeInfo("风暴袭来，未完待续", true, "");
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        Image fr = new FireMagicGirlSprite();
        fr.scale.set(PixelScene.align(1f));
        changes.addButton(new ChangeButton(fr, (Messages.get(vM0_5_X_Changes.class,
                "fr")),
                Messages.get(vM0_5_X_Changes.class, "frlogs")));

        ChangeInfo changes21= new ChangeInfo("V0.5.X.X-ENDUPDATE", true, "");
        changes21.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes21);

        changes = new ChangeInfo("新模式：BossRush!!!", true, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新BOSS:DM920", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        Image dm920 = new DM300Sprite();
        dm920.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(dm920, (Messages.get(vM0_5_X_Changes.class,
                "dm920")),
                Messages.get(vM0_5_X_Changes.class, "dm920logs")));

        Image dm9201 = new DM300AttackSprite();
        dm9201.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(dm9201, (Messages.get(vM0_5_X_Changes.class,
                "dm9201")),
                Messages.get(vM0_5_X_Changes.class, "dm9201logs")));

        Image dm9202 = new DM300SpiderSprite();
        dm9202.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(dm9202, (Messages.get(vM0_5_X_Changes.class,
                "dm9202")),
                Messages.get(vM0_5_X_Changes.class, "dm9202logs")));

        Image dm9203 = new DM300DeathBallSprite();
        dm9203.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(dm9203, (Messages.get(vM0_5_X_Changes.class,
                "dm9203")),
                Messages.get(vM0_5_X_Changes.class, "dm9203logs")));

        Image dm9204 = new DM275Sprite();
        dm9204.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(dm9204, (Messages.get(vM0_5_X_Changes.class,
                "dm9204")),
                Messages.get(vM0_5_X_Changes.class, "dm9204logs")));

        changes.addButton(new ChangeButton(new DM75Sprite(), (Messages.get(vM0_5_X_Changes.class,
                "dm75")),
                Messages.get(vM0_5_X_Changes.class, "dm75logs")));

        changes.addButton(new ChangeButton(new DM150Sprite(), (Messages.get(vM0_5_X_Changes.class,
                "dm150")),
                Messages.get(vM0_5_X_Changes.class, "dm150logs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.INFO), (Messages.get(vM0_5_X_Changes.class, "dm920")),
                Messages.get(vM0_5_X_Changes.class, "dm920logos")));

        ChangeInfo changes20= new ChangeInfo("V0.5.X.9.6-Complete!", true, "");
        changes20.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes20);

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI,
                new ItemSprite.Glowing( 0xFF0000 )), ("吸血鬼刀改善"),
                ("1.吸血鬼刀改进 感谢@A神 的建议\n" +
                        "公式为：角色最大血量*0.01+武器等级*0.5+0.8\n" +
                        "温馨提示：是取整 6.9=9 而不是四舍五入\n\n" +
                        "举例：0级武器 1级角色\n" +
                        "20x0.01+0*0.5+0.8=0.2+0+0.8=1\n" +
                        "10级武器 10级角色\n" +
                        "70x0.01+10x0.5+0.8=0.7+5+0.8=6.5=6")));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X18")));

        ChangeInfo changes19 = new ChangeInfo("V0.5.X.9.5-ReFixed", true, "");
        changes19.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes19);

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("动态情报"),
                ("动态情报追加按钮，以后大家可以在这里面获取最新版！！！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI,
                new ItemSprite.Glowing( 0xFF0000 )), ("吸血鬼刀"),
                ("原3阶武器，现在迁移到4阶武器")));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X17")));

        ChangeInfo changes18 = new ChangeInfo("V0.5.X.9.3-ReFixed", true, "");
        changes18.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes18);

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.AMULET,
                new ItemSprite.Glowing( 0x00FFFF )), ("护身符调整"),
                ("结束游戏改为返回主城！")));


        changes.addButton(new ChangeButton(new NyzSprites(), ("奈亚子商店正式入驻"),
               ("看脸还是运气？这是个问题！随机购买商店奈亚子已入驻！并且修复卡顿问题！")));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X16")));

        ChangeInfo changes17 = new ChangeInfo("V0.5.X.9-ReFixed", true, "");
        changes17.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes17);

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);


        changes.addButton(new ChangeButton(new NyzSprites(), ("奈亚子商店正式入驻"),
                ("看脸还是运气？这是个问题！随机购买商店奈亚子已入驻！")));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X15")));

        ChangeInfo changes16 = new ChangeInfo("V0.5.X.8-ReFixed", true, "");
        changes16.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes16);

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);



        changes.addButton(new ChangeButton(new MagicGirlSprite(), (Messages.get(vM0_5_X_Changes.class,
                "wids2")),
                Messages.get(vM0_5_X_Changes.class, "wids2logs")));

        changes.addButton(new ChangeButton(new ShopkeeperSprite(), (Messages.get(vM0_5_X_Changes.class, "shop12")),
                Messages.get(vM0_5_X_Changes.class, "shop12logs")));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X14")));

        ChangeInfo changes15 = new ChangeInfo("V0.5.X.7-FV", true, "");
        changes15.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes15);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATAXE,
                new ItemSprite.Glowing( 0x00FFFF )), "新附魔：鬼磷",
                "这个附魔会使磷火从武器中喷薄而出，能够使用点燃敌人并对正在燃烧的敌人造成中毒的额外伤害。"));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X13")));

        ChangeInfo changes14 = new ChangeInfo("V0.5.X.6-FV", true, "");
        changes14.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes14);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), (Messages.get(vM0_5_X_Changes.class, "exsg")),
                Messages.get(vM0_5_X_Changes.class, "exsglogs")));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X12")));



        ChangeInfo changes13 = new ChangeInfo("V0.5.X.0-HVA", true, "");
        changes13.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes13);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.SEED_POUCH), (Messages.get(vM0_5_X_Changes.class, "seed")),
                Messages.get(vM0_5_X_Changes.class, "seedlogs")));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X10")));

        ChangeInfo changes12 = new ChangeInfo("V0.5.X.0-RV", true, "");
        changes12.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes12);

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), (Messages.get(vM0_5_X_Changes.class, "audio5")),
                Messages.get(vM0_5_X_Changes.class, "audio5logs")));

        changes.addButton(new ChangeButton(new NxhySprite(), (Messages.get(vM0_5_X_Changes.class,
                "nxhyshopchanges")),
                Messages.get(vM0_5_X_Changes.class, "nxhyshopchangeslogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG19), (Messages.get(vM0_5_X_Changes.class,
                "icewandgod2")),
                Messages.get(vM0_5_X_Changes.class, "icewandgod2logs")));

        changes = new ChangeInfo("修复", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_05X9")));

        changes = new ChangeInfo("平衡", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new SRPDICLRTT(), (Messages.get(vM0_5_X_Changes.class,
                "iclr")),
                Messages.get(vM0_5_X_Changes.class, "iclrlogs")));

        Image yogs = new YogSprite();
        yogs.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(yogs, (Messages.get(vM0_5_X_Changes.class, "superyog")),
                Messages.get(vM0_5_X_Changes.class, "superyoglogs")));

        changes.addButton(new ChangeButton(new MagicGirlSprite(), (Messages.get(vM0_5_X_Changes.class,
                "wids")),
                Messages.get(vM0_5_X_Changes.class, "widslogs")));

        changes.addButton(new ChangeButton(new RatSprite(), (Messages.get(vM0_5_X_Changes.class,
                "ratpo")),
                Messages.get(vM0_5_X_Changes.class, "ratpologs")));

        changes.addButton(new ChangeButton(new KagenoNusujinSprite(), (Messages.get(vM0_5_X_Changes.class,
                "kage2")),
                Messages.get(vM0_5_X_Changes.class, "kage2logs")));

        changes = new ChangeInfo("开发", false, null);
        changes.hardlight(Window.SHPX_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.LING), (Messages.get(vM0_5_X_Changes.class, "dev9")),
                Messages.get(vM0_5_X_Changes.class, "dev9logs")));

        ChangeInfo changes8 = new ChangeInfo("V0.5.X.0", true, "");
        changes8.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes8);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.LING), (Messages.get(vM0_5_X_Changes.class, "dev8")),
                Messages.get(vM0_5_X_Changes.class, "dev8logs")));

        changes.addButton(new ChangeButton(new WhiteGirlSprites(), (Messages.get(vM0_5_X_Changes.class,
                "wdsx")),
                Messages.get(vM0_5_X_Changes.class, "wdsxlogs")));

        changes.addButton(new ChangeButton(new NyzSprites(), (Messages.get(vM0_5_X_Changes.class,
                "nyzspd")),
                Messages.get(vM0_5_X_Changes.class, "nyzspdlogs")));

        changes.addButton(new ChangeButton(new BlueNecromancerSprite(), (Messages.get(vM0_5_X_Changes.class,
                "superzero")),
                Messages.get(vM0_5_X_Changes.class, "superzerologs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), (Messages.get(vM0_5_X_Changes.class, "s6")),
                Messages.get(vM0_5_X_Changes.class, "s6logs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.INFO), (Messages.get(vM0_5_X_Changes.class, "info2")),
                Messages.get(vM0_5_X_Changes.class, "info2logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ICEFISHSWORD), (Messages.get(vM0_5_X_Changes.class,
                "new6w2")),
                Messages.get(vM0_5_X_Changes.class, "new6w2logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FIREFISHSWORD), (Messages.get(vM0_5_X_Changes.class,
                "new6w")),
                Messages.get(vM0_5_X_Changes.class, "new6wlogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGICGIRLBOOKS), (Messages.get(vM0_5_X_Changes.class,
                "newbooks")),
                Messages.get(vM0_5_X_Changes.class, "newbookslogs")));

        Image ice = new IceFireScorpioSprite();
        ice.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(ice, (Messages.get(vM0_5_X_Changes.class, "icescorpio")),
                Messages.get(vM0_5_X_Changes.class, "icescorpiologs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG19), (Messages.get(vM0_5_X_Changes.class,
                "icewandgod")),
                Messages.get(vM0_5_X_Changes.class, "icewandgodlogs")));

        Image fire = new FireAcidicSprite();
        fire.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(fire, (Messages.get(vM0_5_X_Changes.class, "firescorpio")),
                Messages.get(vM0_5_X_Changes.class, "firescorpiologs")));

        changes.addButton(new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 32, 16, 16
                , 16), (Messages.get(vM0_5_X_Changes.class, "sjd")),
                Messages.get(vM0_5_X_Changes.class, "sjdlogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), (Messages.get(vM0_5_X_Changes.class, "musipa")),
                Messages.get(vM0_5_X_Changes.class, "musipalogs")));

        changes.addButton(new ChangeButton(new MimicSprite.Dimand(), (Messages.get(vM0_5_X_Changes.class, "mimmc")),
                Messages.get(vM0_5_X_Changes.class, "mimmclogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG21), (Messages.get(vM0_5_X_Changes.class,
                "goldbao")),
                Messages.get(vM0_5_X_Changes.class, "goldbaologs")));

        changes.addButton(new ChangeButton(new RedDragonSprite(), (Messages.get(vM0_5_X_Changes.class, "reddragon")),
                Messages.get(vM0_5_X_Changes.class, "reddragonlogs")));

        changes.addButton(new ChangeButton(new MurdererSprite.RedMuderer(), (Messages.get(vM0_5_X_Changes.class, "red_murderer")),
                Messages.get(vM0_5_X_Changes.class, "red_murdererlogs")));

        changes.addButton(new ChangeButton(new MagicGirlSprite(), (Messages.get(vM0_5_X_Changes.class, "newking")),
                Messages.get(vM0_5_X_Changes.class, "newkinglogs")));

        changes.addButton(new ChangeButton(new BombGnollTricksterSprites(), (Messages.get(vM0_5_X_Changes.class, "bomb")),
                Messages.get(vM0_5_X_Changes.class, "bomblogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG6), (Messages.get(vM0_5_X_Changes.class, "blues")),
                Messages.get(vM0_5_X_Changes.class, "blueslogs")));

        changes.addButton(new ChangeButton(new SRPDICLRTT(),
                (Messages.get(vM0_5_X_Changes.class, "shjt")),
                Messages.get(vM0_5_X_Changes.class, "shjtlogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_SKYBLUE), (Messages.get(vM0_5_X_Changes.class,
                "bluefire")),
                Messages.get(vM0_5_X_Changes.class, "bluefirelogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_BLUEFIREX), (Messages.get(vM0_5_X_Changes.class,
                "blueexfire")),
                Messages.get(vM0_5_X_Changes.class, "blueexfirelogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REDDRAGON), (Messages.get(vM0_5_X_Changes.class,
                "nukecole")),
                Messages.get(vM0_5_X_Changes.class, "nukecolelogs")));

        changes.addButton(new ChangeButton(new ViewASprite(), (Messages.get(vM0_5_X_Changes.class, "luzus")),
                Messages.get(vM0_5_X_Changes.class, "luzuslogs")));

        changes.addButton(new ChangeButton(new BatSprite.BatEDSprite(), (Messages.get(vM0_5_X_Changes.class, "bat")),
                Messages.get(vM0_5_X_Changes.class, "batlogs")));

        changes.addButton(new ChangeButton(new ShopGuardEye.ShopGuardianBlueSprite(), (Messages.get(vM0_5_X_Changes.class, "guard")),
                Messages.get(vM0_5_X_Changes.class, "guardlogs")));

        changes.addButton(new ChangeButton(new IceGolemSprite(), (Messages.get(vM0_5_X_Changes.class, "icegolem")),
                Messages.get(vM0_5_X_Changes.class, "icegolemlogs")));

        changes.addButton(new ChangeButton(new ShopkeeperSprite(), (Messages.get(vM0_5_X_Changes.class, "shop")),
                Messages.get(vM0_5_X_Changes.class, "shoplogs")));

        changes.addButton(new ChangeButton(new NxhySprite(), (Messages.get(vM0_5_X_Changes.class, "shop2")),
                Messages.get(vM0_5_X_Changes.class, "shop2logs")));

        changes.addButton(new ChangeButton(new IceCryStalSprite(), (Messages.get(vM0_5_X_Changes.class, "fireball")),
                Messages.get(vM0_5_X_Changes.class, "fireballlogs")));

        changes.addButton(new ChangeButton(new Image(Assets.Environment.TERRAIN_FEATURES, 0, 128, 16, 16),
                (Messages.get(vM0_5_X_Changes.class, "bluefirebloom")),
                Messages.get(vM0_5_X_Changes.class, "bluefirebloomlogs")));

        Image issxsas =new Image(Assets.Interfaces.BUFFS_LARGE, 80, 48, 16, 16);
        issxsas.scale.set(PixelScene.align(0.80f));
        changes.addButton(new ChangeButton(issxsas,
                (Messages.get(vM0_5_X_Changes.class, "newbuffs")),
                Messages.get(vM0_5_X_Changes.class, "newbuffslogs")));

        Image yog = new YogSprite();
        yog.scale.set(PixelScene.align(0.75f));
        changes.addButton(new ChangeButton(yog, (Messages.get(vM0_5_X_Changes.class, "yogdrawf")),
                Messages.get(vM0_5_X_Changes.class, "yogdrawflogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), (Messages.get(vM0_5_X_Changes.class, "bosstz")),
                Messages.get(vM0_5_X_Changes.class, "bosstzlogs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GDX_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ColdRatSprite(), (Messages.get(vM0_5_X_Changes.class, "coldrat2")),
                Messages.get(vM0_5_X_Changes.class, "coldratlogs2")));

        changes.addButton(new ChangeButton(new RedSwarmSprite(), (Messages.get(vM0_5_X_Changes.class, "redc")),
                Messages.get(vM0_5_X_Changes.class, "redclogs")));

        changes.addButton(new ChangeButton(new KagenoNusujinSprite(), (Messages.get(vM0_5_X_Changes.class, "kagenu")),
                Messages.get(vM0_5_X_Changes.class, "kagenulogs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), (Messages.get(vM0_5_X_Changes.class, "displayml")),
                Messages.get(vM0_5_X_Changes.class, "displaymllogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.INFO), (Messages.get(vM0_5_X_Changes.class, "info")),
                Messages.get(vM0_5_X_Changes.class, "infologs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), (Messages.get(vM0_5_X_Changes.class, "add")),
                Messages.get(vM0_5_X_Changes.class, "addlogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), (Messages.get(vM0_5_X_Changes.class, "ankh2")),
                Messages.get(vM0_5_X_Changes.class, "ankh2logs")));

        changes.addButton(new ChangeButton(new Image(Assets.Environment.WATER_COLD,0, 0, 16
                , 16),
                (Messages.get(vM0_5_X_Changes.class, "watermove")),
                Messages.get(vM0_5_X_Changes.class, "watermovelogs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        Image issxa = new SlimeKingSprite();
        issxa.scale.set(PixelScene.align(0.89f));
        changes.addButton(new ChangeButton(issxa, (Messages.get(vM0_5_X_Changes.class, "kingno")),
                Messages.get(vM0_5_X_Changes.class, "kingnologs")));

        changes.addButton(new ChangeButton(new ShieldHuntsmanSprite(), (Messages.get(vM0_5_X_Changes.class, "hxblr")),
                Messages.get(vM0_5_X_Changes.class, "hxblrlogs")));
        changes.addButton(new ChangeButton(new SRPDHBLRTT(), (Messages.get(vM0_5_X_Changes.class, "hblr")),
                Messages.get(vM0_5_X_Changes.class, "hblrlogs")));

        changes.addButton(new ChangeButton(new MolotovHuntsmanSprite(), (Messages.get(vM0_5_X_Changes.class, "mohblr")),
                Messages.get(vM0_5_X_Changes.class, "mohblrlogs")));

        ChangeInfo changes9 = new ChangeInfo("V0.5.4.1-QIV", true, "");
        changes9.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes9);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        Image i = new Image(new BombGnollTricksterSprites());
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton( new ChangeButton(new BombGnollTricksterSprites(), "豺狼炼药长老",
                "豺狼中的高智能，会使用炼药来摧残每一个入侵者。"));

        Image iii = new Image(new ShopGuardEye.ShopGuardianRedSprite());
        iii.scale.set(PixelScene.align(0.74f));
        changes.addButton( new ChangeButton(new ShopGuardEye.ShopGuardianRedSprite(), "守卫更新",
                "添加裁决者，7回合后审判0元购者"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.WATA_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.DG1), "炸弹匕首",
                "炸弹匕首生成的炸弹将立刻爆炸"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), "十字架改动",
                "十字架现在是死亡后直接返回0层，进行新一轮的冒险。"));

        ChangeInfo changes7 = new ChangeInfo("V0.5.3.6-SPV", true, "");
        changes7.hardlight(Window.Pink_COLOR);
        changeInfos.add(changes7);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG26), (Messages.get(vM0_5_X_Changes.class, "dg26")),
                Messages.get(vM0_5_X_Changes.class, "dg26logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG24), (Messages.get(vM0_5_X_Changes.class, "dg24")),
                Messages.get(vM0_5_X_Changes.class, "dg24logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG12), (Messages.get(vM0_5_X_Changes.class, "dg12")),
                Messages.get(vM0_5_X_Changes.class, "dg12logs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_0536")));

        ChangeInfo changes6 = new ChangeInfo("V0.5.3.5-SPV", true, "");
        changes6.hardlight(Window.Pink_COLOR);
        changeInfos.add(changes6);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), true, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), (Messages.get(vM0_5_X_Changes.class, "ankh")),
                Messages.get(vM0_5_X_Changes.class, "ankhlogs")));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_0535")));

        ChangeInfo changes5 = new ChangeInfo("V0.5.3.0-SPV", true, "");
        changes5.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes5);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), true, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GAUNTLETS), (Messages.get(vM0_5_X_Changes.class, "newwepaon1")),
                Messages.get(vM0_5_X_Changes.class, "newwepaon1logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WHIP), (Messages.get(vM0_5_X_Changes.class, "newwepaon2")),
                Messages.get(vM0_5_X_Changes.class, "newwepaon2logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAR_HAMMER), (Messages.get(vM0_5_X_Changes.class, "newwepaon3")),
                Messages.get(vM0_5_X_Changes.class, "newwepaon3logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG12), (Messages.get(vM0_5_X_Changes.class, "challs2")),
                Messages.get(vM0_5_X_Changes.class, "challs2logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG18), (Messages.get(vM0_5_X_Changes.class, "challs3")),
                Messages.get(vM0_5_X_Changes.class, "challs3logs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), (Messages.get(vM0_5_X_Changes.class, "crashsx")),
                Messages.get(vM0_5_X_Changes.class, "crashsxlogs")));

        ChangeInfo changes4 = new ChangeInfo("V0.5.2.6-SPV", true, "");
        changes4.hardlight(Window.Pink_COLOR);
        changeInfos.add(changes4);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RedBloodMoon), (Messages.get(vM0_5_X_Changes.class, "newwepaon")),
                Messages.get(vM0_5_X_Changes.class, "newwepaonlogs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_0510")));

        ChangeInfo changes3 = new ChangeInfo("V0.5.2.5-SPV", true, "");
        changes3.hardlight(Window.RED_COLOR);
        changeInfos.add(changes3);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new SpectralNecromancerSprite(), (Messages.get(vM0_5_X_Changes.class, "dead")),
                Messages.get(vM0_5_X_Changes.class, "deadlogs")));

        changes.addButton(new ChangeButton(new SkullShamanSprite(), (Messages.get(vM0_5_X_Changes.class, "sm")),
                Messages.get(vM0_5_X_Changes.class, "smlogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), (Messages.get(vM0_5_X_Changes.class, "zlps")),
                Messages.get(vM0_5_X_Changes.class, "zlpslogs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_0509")));

    }

    public static void add_v0_5_5_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.5.2.0-Release", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_OFF), (Messages.get(vM0_5_X_Changes.class, "new1css")),
                Messages.get(vM0_5_X_Changes.class, "new1csslogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE), (Messages.get(vM0_5_X_Changes.class, "alks")),
                Messages.get(vM0_5_X_Changes.class, "alkslogs")));

        changes.addButton(new ChangeButton(new ElementalSprite.Fire(), (Messages.get(vM0_5_X_Changes.class, "eme")),
                Messages.get(vM0_5_X_Changes.class, "emelogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), (Messages.get(vM0_5_X_Changes.class, "bagsd")),
                Messages.get(vM0_5_X_Changes.class, "bagsdlogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG18), (Messages.get(vM0_5_X_Changes.class, "newitems")),
                Messages.get(vM0_5_X_Changes.class, "newitemslogs")));

        Image isa = new SlimeKingSprite();
        isa.scale.set(PixelScene.align(0.89f));
        changes.addButton(new ChangeButton(isa, (Messages.get(vM0_5_X_Changes.class, "king")),
                Messages.get(vM0_5_X_Changes.class, "kinglogs")));

        Image is = new DM720Sprite();
        is.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(is, (Messages.get(vM0_5_X_Changes.class, "dm7204")),
                Messages.get(vM0_5_X_Changes.class, "dm7204logs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG12), (Messages.get(vM0_5_X_Changes.class, "sca")),
                Messages.get(vM0_5_X_Changes.class, "scalogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), (Messages.get(vM0_5_X_Changes.class, "ksx")),
                Messages.get(vM0_5_X_Changes.class, "ksxlogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), (Messages.get(vM0_5_X_Changes.class, "musica")),
                Messages.get(vM0_5_X_Changes.class, "musicalogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.SCROLL_HOLDER), (Messages.get(vM0_5_X_Changes.class, "dev7")),
                Messages.get(vM0_5_X_Changes.class, "dev7logs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.LING), (Messages.get(vM0_5_X_Changes.class, "dev6")),
                Messages.get(vM0_5_X_Changes.class, "dev6logs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), (Messages.get(vM0_5_X_Changes.class, "j1")),
                Messages.get(vM0_5_X_Changes.class, "j1logs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), (Messages.get(vM0_5_X_Changes.class, "new1cs")),
                Messages.get(vM0_5_X_Changes.class, "new1cslogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), (Messages.get(vM0_5_X_Changes.class, "new2cs")),
                Messages.get(vM0_5_X_Changes.class, "new2cslogs")));

        changes.addButton(new ChangeButton(new TenguSprite(), (Messages.get(vM0_5_X_Changes.class, "bossc1")),
                Messages.get(vM0_5_X_Changes.class, "bossc1logs")));

        changes.addButton(new ChangeButton(new CausticSlimeSprite(), (Messages.get(vM0_5_X_Changes.class, "goo3")),
                Messages.get(vM0_5_X_Changes.class, "goo4logs")));

        Image i = new DM300Sprite();
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(i, (Messages.get(vM0_5_X_Changes.class, "dm3004")),
                Messages.get(vM0_5_X_Changes.class, "dm3004logs")));

        changes.addButton(new ChangeButton(new WandmakerSprite(), (Messages.get(vM0_5_X_Changes.class, "newsc")),
                Messages.get(vM0_5_X_Changes.class, "newsclogs")));

        changes.addButton(new ChangeButton(new RedNecromancerSprite(), (Messages.get(vM0_5_X_Changes.class, "newsc1")),
                Messages.get(vM0_5_X_Changes.class, "newsc1logs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.KOTLIN), (Messages.get(vM0_5_X_Changes.class, "kotlinss")),
                Messages.get(vM0_5_X_Changes.class, "kotlinsslogs")));

        changes.addButton(new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 48, 16
                        , 16), (Messages.get(vM0_5_X_Changes.class, "sss")),
                Messages.get(vM0_5_X_Changes.class, "ssslogs")));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_0520")));

        changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), (Messages.get(vM0_5_X_Changes.class, "newmisc1")),
                Messages.get(vM0_5_X_Changes.class, "newmisc1logs")));

    }

    public static void add_v0_5_4_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.5.0.8-9", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ColdRatSprite(), (Messages.get(vM0_5_X_Changes.class, "coldrat")),
                Messages.get(vM0_5_X_Changes.class, "coldratlogs")));

        changes.addButton(new ChangeButton(new RedSwarmSprite(), (Messages.get(vM0_5_X_Changes.class, "redswarm")),
                Messages.get(vM0_5_X_Changes.class, "redswarmlogs")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DM300AttackSprite(), (Messages.get(vM0_5_X_Changes.class, "dm300")),
                Messages.get(vM0_5_X_Changes.class, "dm300logs")));

        changes.addButton(new ChangeButton(new CausticSlimeSprite(), (Messages.get(vM0_5_X_Changes.class, "goo3")),
                Messages.get(vM0_5_X_Changes.class, "goo3logs")));

    }

    public static void add_v0_5_3_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.5.0.7", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Environment.TILES_COLD, 48, 48, 16
                , 16), (Messages.get(vM0_5_X_Changes.class, "cold")),
                Messages.get(vM0_5_X_Changes.class, "coldlogs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_0507")));

    }

    public static void add_v0_5_2_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.5.0.6", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new CausticSlimeSprite(), (Messages.get(vM0_5_X_Changes.class, "cau1")),
                Messages.get(vM0_5_X_Changes.class, "caulogs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new KagenoNusujinSprite(), (Messages.get(vM0_5_X_Changes.class, "kage1")),
                Messages.get(vM0_5_X_Changes.class, "kagelogs")));

    }

    public static void add_v0_5_1_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.5.0.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.LING), (Messages.get(vM0_5_X_Changes.class, "dev5")),
                Messages.get(vM0_5_X_Changes.class, "dev5logs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.KOTLIN), (Messages.get(vM0_5_X_Changes.class, "kotlinsi")),
                Messages.get(vM0_5_X_Changes.class, "kotlinlogs")));

        changes.addButton(new ChangeButton(new RenSprite(), (Messages.get(vM0_5_X_Changes.class, "newnpc1")),
                Messages.get(vM0_5_X_Changes.class, "renlogs")));

        changes.addButton(new ChangeButton(new Image("Npcs/rt.png", 0, 0, 16, 16), (Messages.get(vM0_5_X_Changes.class, "newnpc2")),
                Messages.get(vM0_5_X_Changes.class, "obsirlogs")));

        changes.addButton(new ChangeButton(new SlylSprite(), (Messages.get(vM0_5_X_Changes.class, "newnpc3")),
                Messages.get(vM0_5_X_Changes.class, "slyllogs")));

        changes.addButton(new ChangeButton(new NxhySprite(), (Messages.get(vM0_5_X_Changes.class, "newnpc4")),
                Messages.get(vM0_5_X_Changes.class, "nxhylogs")));

        changes.addButton(new ChangeButton(new NyzSprites(), (Messages.get(vM0_5_X_Changes.class, "newnpc5")),
                Messages.get(vM0_5_X_Changes.class, "nyzlogs")));

        changes.addButton(new ChangeButton(new MimicSprite.Dimand(), (Messages.get(vM0_5_X_Changes.class, "newmimic")),
                Messages.get(vM0_5_X_Changes.class, "newmimiclogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG17), (Messages.get(vM0_5_X_Changes.class, "newwand")),
                Messages.get(vM0_5_X_Changes.class, "newwandlogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG10), (Messages.get(vM0_5_X_Changes.class, "newfood")),
                Messages.get(vM0_5_X_Changes.class, "newfoodlogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG20), (Messages.get(vM0_5_X_Changes.class, "newstory")),
                Messages.get(vM0_5_X_Changes.class, "newstorylogs")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG21), (Messages.get(vM0_5_X_Changes.class, "newitem")),
                Messages.get(vM0_5_X_Changes.class, "newitem")));

        changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), (Messages.get(vM0_5_X_Changes.class, "newsp")),
                Messages.get(vM0_5_X_Changes.class, "newsplogs")));

        changes.addButton(new ChangeButton(new FireGhostSprite(), (Messages.get(vM0_5_X_Changes.class, "jq2")),
                Messages.get(vM0_5_X_Changes.class, "jq2logs")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), (Messages.get(ChangesScene.class, "bugfixes")),
                Messages.get(vM0_5_X_Changes.class, "bug_0505")));

        changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), (Messages.get(vM0_5_X_Changes.class, "newmisc")),
                Messages.get(vM0_5_X_Changes.class, "newmisclogs")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), (Messages.get(vM0_5_X_Changes.class, "challengegd")),
                Messages.get(vM0_5_X_Changes.class, "challengegdlogs")));

        changes.addButton(new ChangeButton(new KagenoNusujinSprite(), (Messages.get(vM0_5_X_Changes.class, "jq1")),
                Messages.get(vM0_5_X_Changes.class, "jq1logs")));

        changes.addButton(new ChangeButton(new PylonSprite(), (Messages.get(vM0_5_X_Changes.class, "xr1")),
                Messages.get(vM0_5_X_Changes.class, "xr1logs")));


    }

}
