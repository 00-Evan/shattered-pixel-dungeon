package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CausticSlimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300AttackSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireGhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KagenoNusujinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MurdererSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlylSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SzSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class S_Changes {

    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        V0_d_XChanges(changeInfos);
        V0_c_XChanges(changeInfos);
        V0_b_XChanges(changeInfos);
        V0_a_XChanges(changeInfos);
        V0_0_XChanges(changeInfos);
        V0_1_XChanges(changeInfos);
        V0_2_XChanges(changeInfos);
        V0_4_XChanges(changeInfos);
        V0_4_6Changes(changeInfos);
        V0_4_0Changes(changeInfos);
        V0_0_2Changes4(changeInfos);
        V0_0_2Changes3(changeInfos);
        V0_0_2Changes2(changeInfos);
        V0_0_2Changes1(changeInfos);
        V0_0_1Changes(changeInfos);
        Dev_Changes4(changeInfos);
        Dev_Changes3(changeInfos);
        Dev_Changes2(changeInfos);
        Dev_Changes(changeInfos);
        V0_0_2Changes(changeInfos);
    }

    public static void  V0_d_XChanges( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.4.8.0-Release", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton (new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                "BUG修复",
                "_-_ _2021-6-5_\n" +
                        "_-_ 1.修复了一堆BUG 包括以下\n" +
                        "_-_ A.修复了饱腹状态仍然会饥饿\n" +
                        "_-_ B.修复部分怪物的生成权值" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), "新武器",
                "_-_ A.丛林毒刺\n" +
                        "_-_ B.风筝盾牌" +
                        "\n" ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        Image i = new Image(new MolotovHuntsmanSprite());
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton( new ChangeButton(new MolotovHuntsmanSprite(), "血月火把猎人",
                "削弱 _血月火把猎人_ ！\n" +
                        "血量 _169_降到_60_\n" +
                        "攻击由_4x_变成_2x_)\n" +
                        "Dev:JDSA Ling"));

        Image a = new Image(new DM300AttackSprite());
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton( new ChangeButton(new DM300AttackSprite(), "DM-720",
                "削弱 _DM-720_！\n" +
                        "血量 _3000_降到_200_\n" +
                        "攻击由_2x_变成_1.3x_\n" +
                        "_电击伤害_削弱\n" +
                        "Dev:JDSA Ling"));

    }

    public static void  V0_c_XChanges( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.4.8.4 CS版", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton (new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                "BUG修复",
                "_-_ _2021-6-4_\n" +
                        "_-_ 1.修复了一堆BUG 包括以下\n" +
                        "_-_ A.修复了跳楼崩溃 （但不排除意外情况）\n" +
                        "_-_ B.监狱的小偷卡其脱离太修复" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), "战前记录",
                "BOSS层现在有战前记录"));

    }

    public static void  V0_b_XChanges( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.4.8.3 CS版", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), "BUG修复",
                "_-_ _2021-6-4_\n" +
                        "_-_ 1.修复了一堆BUG 包括以下\n" +
                        "_-_ A.修复了幽灵任务\n" +
                        "_-_ B.部分怪物经验掉落调整" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DM300AttackSprite(), "DM720",
                "将DM300重新进行了优化和强化\n\n" +
                        "并改名为DM-720\n\n" +
                        "DM720新特性\n" +
                        "1.在水上移动时可以回血\n" +
                        "2.较强大的_击退_\n" +
                        "3._血量低_的时候会自我修复\n" +
                        "4._奥术护盾_的力量_更加强大_\n" +
                        "5.启动_歼击模式_的_能源塔_获得_额外血量_\n\n" +
                        "PS:这里的_素材_在游戏不会出现 仅仅是为了_好看_"));

        changes.addButton(new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 48, 16
                , 16), "15层改动",
                "15层进行了一些改动\n" +
                        "详情请参考群的更新说明\n\n" +
                        "_-_ MLPD\n\n"));
    }

    public static void  V0_a_XChanges( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.4.8.2 CS版", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "BUG修复",
                "_-_ _2021-6-3_\n" +
                        "_-_ 1.修复了一堆BUG 包括以下\n" +
                        "_-_ 2.DM300修正\n" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

    }

    public static void  V0_0_XChanges( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.4.8.1 CS版", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "BUG修复",
                "_-_ _2021-6-3_\n" +
                        "_-_ 1.修复了一堆BUG\n" +
                        "_-_ 2.DM201现在在矿洞层\n" +
                        "_-_ 3.删除红色史莱姆\n" +
                        "_-_ 4.开发者模式修正" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new GhostSprite(), "幽灵任务",
                "幽灵现在任务没有巨蟹了，同时，巨蟹移动到一个特殊房间里面。同时将幽灵的奖励更加丰厚。"));

    }

    public static void  V0_1_XChanges( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.8.0 CS版", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo( Messages.get( ChangesScene.class, "changes"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "开发日志",
                "_-_ _2021-6-2_\n" +
                        "_-_ 1.修复了一堆BUG\n" +
                        "_-_ 2.界面修正\n" +
                        "_-_ 3.地牢层数调整\n" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON),
                "新挑战 饥荒合约",
                "\n"+
                        "人们常说，人是铁，饭是钢。一顿不吃要暴毙。欢迎来到饥荒合约！\n" +
                        "合约详情：极度饥饿状态下，\n" +
                        "每过一段时间以角色血量的50%的真实伤害扣血。\n" +
                        "同时开局送6个应急口粮"+
                        "\n"));
    }

    public static void  V0_2_XChanges( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.2.0 正式版", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo( Messages.get( ChangesScene.class, "changes"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "开发日志",
                "_-_ _2021-3-29_\n" +
                        "_-_ 1.修复了一堆BUG\n" +
                        "_-_ 2.界面美化\n" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
                "每个角色初始物品现在有:\n"+
                        "1.7瓶治疗药水\n"+
                        "2.5瓶灵视药水\n"+
                        "3.3个鉴定卷轴\n"+
                        "4.1个升级卷轴\n"+
                        "5.12个口粮"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        Image i = new Image(new ShieldHuntsmanSprite());
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton( new ChangeButton(new ShieldHuntsmanSprite(), "血月魔盾赏金猎人",
                "极其扭曲的生物，在矮人层开始出现！\n"+
                        "请小心，此怪物可以 _击飞你的武器_ ！\n"+
                        "并且可能将你 _定身_ ！"));

    }
    public static void  V0_4_XChanges( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.0.X-Beta", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo( Messages.get( ChangesScene.class, "changes"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "开发日志",
                "_-_ _2021-3-28_\n" +
                        "_-_ 1.修复了31层的闪退BUG\n" +
                        "_-_ 2.界面大改动\n" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        Image i = new Image(new SzSprites());
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton( new ChangeButton(new RedSprites(), "_史莱姆血红守卫者",
                "削弱 _史莱姆血红守卫者_ ！"));

    }
    public static void  V0_4_6Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.0.6-Release", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo( Messages.get( ChangesScene.class, "changes"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "开发日志",
                "_-_ _2021-3-25_\n" +
                        "_-_ 1.修复了无数的BUG\n" +
                        "_-_ 2.新BOSS 史莱姆王\n" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        Image i = new Image(new SzSprites());
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton( new ChangeButton(new SzSprites(), "史莱姆守卫者",
                "史莱姆守卫者 10层BOSS用！"));

        Image a = new SlimeKingSprite();
        a.scale.set(PixelScene.align(0.42f));
        changes.addButton(new ChangeButton(a,  "史莱姆王",
                "10层BOSS！"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(new RatSprite()), "老鼠加强",
                "老鼠的_攻击和生存能力_都得到了提升"));

        changes.addButton( new ChangeButton(new Image(new SnakeSprite()), "下水道巨蛇加强",
                "巨蛇的_攻击和生存能力_都得到了提升"));

    }

    public static void  V0_4_0Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.0-Release", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo( Messages.get( ChangesScene.class, "new"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "开发日志",
                "_-_ _2021-3-24_\n" +
                        "_-_ 距离上次更新7天前-上一个版本是_ 0.3.0.8D _\n" +
                        "_-_ 距离上次更新18天前-上一个版本是_ 0.3.0.1 _\n" +
                        "\n" +
                        "Dev:_JDSA-Ling_"));

        changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "新挑战",
                "_新挑战更新如下:\n" +
                        "_-_ 1._恶魔之水_：\n" +
                        "_-_ 你将会因为踩水受到伤害，但是你会开局送你十个石灰！石灰源码来自古明地觉地牢！\n\n" +
                        "_-_ 2._精英战场_:\n" +
                        "_怪物也会成长！所以请小心为妙！\n\n" +
                        "_3._两袖清风&负债累累_\n"+
                        "_一个是禁止地牢的_金币生成_，另一个则是_移除金币_"));

        changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), "文本渲染",
                "文本现在可以渲染成RGB各拉满的三种颜色\n\n红色：_红_ \n\n蓝色：∮蓝∮ \n\n绿色：_绿_"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.DG8), "_红宝石魔袋_",
                "矮人国王的宝贝，现在在_DM300_那里有可能获得！\n\n或者去_商人那里_也有可能获得！"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.DG7), "_食物袋_",
                "在_粘咕_那里有几率获得该物品\n\n或者在_商人那里_也可能得到"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight( CharSprite.WARNING );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RedBloodMoon), "猩红血月魔剑",
                "现在它可以召唤血量为_3滴血_的_血月傀儡_，傀儡根据您的武器而自动成长！"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.DG1), "炸弹匕首",
                "炸弹匕首现在掉的武器不会_瞬间爆炸_！"));

        changes.addButton( new ChangeButton(new MurdererSprite(), "血月小偷",
                "_现在移除！_"));

        changes.addButton( new ChangeButton(new FireGhostSprite(), "火焰幽灵",
                "_现在移除！_"));

        changes.addButton( new ChangeButton(new Image( Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "反正就是修复了 _无数的BUG_ ，我已经_懒得写了_！"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(new CausticSlimeSprite()), "粘咕加强",
                "粘咕的_血量_得到了提升"));

        changes.addButton( new ChangeButton(new TenguSprite(), "天狗加强",
                "天狗的_血量_得到了提升"));

        changes.addButton( new ChangeButton(new KingSprite(), "矮人国王加强",
                "国王的_血量和攻击_得到了提升"));

        changes.addButton( new ChangeButton(new YogSprite(), "Yog加强",
                "Yog的_血量_得到了提升"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton( new  ChangeButton(new KagenoNusujinSprite(),"影子盗贼",
                "影子盗贼的_攻击力_被削弱"));
    }

    public static void V0_0_2Changes4(ArrayList<ChangeInfo> arrayList) {
        ChangeInfo changeInfo = new ChangeInfo("V0.3.0.8-9 HotFixed 热修复", true, "");
        changeInfo.hardlight(16776960);
        arrayList.add(changeInfo);
        changeInfo.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG6), "移除", "移除通知：\n\n" +
                "由于此东西BUG百出，我决定暂时移除！以后会回归的，敬请期待！\n\n"));
        changeInfo.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                "BUG修复", "0.3.0.7-0.3.0.8合计修复以下问题:\n1._15层_楼层崩溃问题\n2.新NPC可以被_法师的法杖_杀死的问题\n3._DM300_的Act崩溃问题\n4.修复_灵壤法杖_的炸档问题\n5.修复_新NPC_的_帧数_错误切割问题\n\n"));
        ChangeInfo changeInfo2 = new ChangeInfo("V0.3.0.5 新NPC", true, "");
        changeInfo2.hardlight(65280);
        arrayList.add(changeInfo2);
        changeInfo2.addButton(new ChangeButton(new Image("ren.png", 0, 0, 13, 16), "REN",
                "似曾相识的面孔，你一定哪里见过他的。\n\n"));
        changeInfo2.addButton( new ChangeButton(new SlylSprite(), "霜落雨凉", "_寒冰圣都_的女帝，没有人知道她的真正力量！\n" +
                "\n"));
        changeInfo2.addButton(new ChangeButton(new Image("rt.png", 0, 0, 16, 16), "Observe_Sir", "来自另一个世界的旅行者。中型礼帽是他的护身武器。\n\n"));
        ChangeInfo changeInfo3 = new ChangeInfo("V0.3.0.4 HotFixed 热修复", true, "");
        changeInfo3.hardlight(16711680);
        arrayList.add(changeInfo3);
        changeInfo3.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                "BUG修复 _Bug Fixed_", "修复炸弹匕首的错误_Private_变量导致的崩溃。现在是_Public_变量。\n\n"));
        ChangeInfo changeInfo4 = new ChangeInfo("New Font! 新字体包！", true, "");
        changeInfo4.hardlight(65280);
        arrayList.add(changeInfo4);
        changeInfo4.addButton(new ChangeButton(new Image("ttf.png", 0, 0, 16, 16), "新字体", "字体更新！字体名称_Black Ruyt_\n\n若要使用 请到游戏设置关闭系统字体\n\n"));
        ChangeInfo changeInfo5 = new ChangeInfo("V0.3.0.3", true, "");
        changeInfo5.hardlight(16711680);
        arrayList.add(changeInfo5);
        changeInfo5.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), "BUG修复", "修复武器嬗变崩溃"));
        changeInfo5.addButton(new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "改动", "猩红血月魔剑现在是_4阶武器_\n音乐进行了优化和替换\n\n特别加入了_BilBovDev_技巧地牢的音乐【作者已经授权】\n\n界面贴图修改美化了一些\n\n"));
        ChangeInfo changeInfo6 = new ChangeInfo("v0.3.0.1b", true, "");
        changeInfo6.hardlight(16711935);
        arrayList.add(changeInfo6);
        ChangeInfo changeInfo7 = new ChangeInfo(Messages.get(ChangesScene.class, "new", new Object[0]), false, (String) null);
        changeInfo7.hardlight(16711935);
        arrayList.add(changeInfo7);
        changeInfo7.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGIC_INFUSE, (ItemSprite.Glowing) null), "新怪物", "1.添加了一个怪物\n\n"));
        changeInfo7.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CURSE_INFUSE, (ItemSprite.Glowing) null), "新机制", "地牢的一些机制改变了\n\n"));
        ChangeInfo changeInfo8 = new ChangeInfo(Messages.get(ChangesScene.class, "changes", new Object[0]), false, (String) null);
        changeInfo8.hardlight(16745258);
        arrayList.add(changeInfo8);
        changeInfo8.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHEST, (ItemSprite.Glowing) null), "Bug修复", "修复了以下问题\n\n1.修复了上个版本楼层26层闪退问题\n\n2.修复了界面问题\n\n"));
        changeInfo8.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_OPAL, (ItemSprite.Glowing) null), "武器改动详情", "武器名称:_血红魔剑_\n-首个版本属于_5阶武器_，现在已经升级了_8阶武器_\n\n"));
        ChangeInfo changeInfo9 = new ChangeInfo(Messages.get(ChangesScene.class, "buffs", new Object[0]), false, (String) null);
        changeInfo9.hardlight(16711935);
        arrayList.add(changeInfo9);
        changeInfo9.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HOLSTER, (ItemSprite.Glowing) null), "游戏物品增强记录A", "0.2.7 加强蓝色剑\n\n0.2.8 加强蓝焰法杖\n\n"));
        changeInfo9.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_HOURGLASS, (ItemSprite.Glowing) null), "游戏物品增强记录B", "Up\n\n"));
        ChangeInfo changeInfo10 = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs", new Object[0]), false, (String) null);
        changeInfo10.hardlight(16711680);
        arrayList.add(changeInfo10);
        changeInfo10.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_FROST, (ItemSprite.Glowing) null), "武器削弱记录A", "1.蔚蓝审判之剑\n-此版本已削弱精准和伤害，现在它的伤害20-35伤害左右\n\n"));
        changeInfo10.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_LIGHTNING, (ItemSprite.Glowing) null), "武器削弱记录B", "Up"));
        ChangeInfo changeInfo11 = new ChangeInfo("V0.3.0.0 重大更新", true, "");
        changeInfo11.hardlight(65280);
        arrayList.add(changeInfo11);
        changeInfo11.addButton(new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "0.0.3 脱胎换骨", "V0.0.3更新日志如下:\nBug修复:\n1.修复窗口_变量闪退_问题\n新内容:\n1.地牢0层加入了 -_技术指导:红龙_\n2.0层NPC【那些回忆】\n3.楼层数暂时_变的更多_\n改动:\n1._时空道标回归_\n2.省电模式改为超频模式\n3.背包UI布局优化\n4.初始物品更改\n\n开发者的话:\n这里是开发者_JDSA-Ling_,那么以上就是本次版本更新的内容。出于学业原因，下次更新等我高三结束。放心，不会好久的。我是高职单招呢。那么祝大家新版本玩的愉快，单招完了后，我们再相见吧。ByeBye\n开发日期:2021-2-28 JDSA-Ling\n\n"));
        ChangeInfo changeInfo12 = new ChangeInfo("V0.2.9.0", true, "");
        changeInfo12.hardlight(65280);
        arrayList.add(changeInfo12);
        changeInfo12.addButton(new ChangeButton(new Image("Npcs/Nxhy.png", 0, 0, 16, 16), "新NPC", "那些回忆商人现在可以在_一层发现他_\n\n并且可以和他交易\n\n注:在即将到来的_0.3_版本里面会直接_加入到0层_"));
        ChangeInfo changeInfo13 = new ChangeInfo("V0.2.8.0-5", true, "");
        changeInfo13.hardlight(16711680);
        arrayList.add(changeInfo13);
        changeInfo13.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), "BUG修复", "修复生成器代码错误崩溃的问题\n\n-0.2.8.3"));
        changeInfo13.addButton(new ChangeButton(new Image("SRPD/BlackGhost.png", 0, 0, 16, 16), "新怪物", "_黑色怨灵_\n\n_监狱开始刷新_\n\n-0.2.8.5"));
        ChangeInfo changeInfo14 = new ChangeInfo("V0.2.7.0", true, "");
        changeInfo14.hardlight(16736000);
        arrayList.add(changeInfo14);
        changeInfo14.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16), "BUG修复", "1.修复_酸蚀法杖_崩溃闪退问题\n\n2.修复_钥匙剑_变量问题[修复者:_Ren_]\n\n3.修复楼层_重置闪退_问题，理论上得到解决"));
        changeInfo14.addButton(new ChangeButton(new Image("sprites/snake.png", 0, 0, 14, 16), "新怪物", "_下水道巨蛇_\n\n_下水道3层开始刷新_"));
        changeInfo14.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG6), "新法杖", "幻蓝_焰浪_神杖\n\n这根法杖由_蓝磷元素_制成，饰以_魔紫树叶_这使它看起来相当霸气。它的顶端噼啪作响嘶嘶而鸣，渴望着释放其_强大的魔法。_"));
        ChangeInfo changeInfo15 = new ChangeInfo("V0.2.5-6", true, "");
        changeInfo15.hardlight(65535);
        arrayList.add(changeInfo15);
        changeInfo15.addButton(new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "开发日志", "本来今天不属于_更新的日子，_但是我把手机取下来了 所以简单更新一波。\n\n特别感谢_Ren_加的_新武器_和_新怪物_\n\n更新时间:_2021-2-23_"));
        changeInfo15.addButton(new ChangeButton(new Image("SRPD/MolotovHuntsman.png", 0, 0, 16, 16), "新怪物", "_血月火把赏金猎人_\n\n_矿洞层_刷新"));
        changeInfo15.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MagicBlueSword), "武器改动", "猩红血月神剑现在是_7阶_武器，并且攻击缓慢\n\n幻蓝审判神剑_削弱伤害_。"));
        ChangeInfo changeInfo16 = new ChangeInfo("V0.0.2.c-Ren-Create", true, "");
        changeInfo16.hardlight(65535);
        arrayList.add(changeInfo16);
        changeInfo16.addButton(new ChangeButton(new Image("ren.png", 0, 0, 13, 16), "接替任务", "...." +
                "...........\n.......\n\n.....你好....\n  我是MR.REN。\n我的LING助手哟，将要以完成一项任务的指令而匆匆离去；\n  为了完成来自远方库页岛的密语，他将环绕着负熵与居里点极限的磁场、而又向大喝彩盒里祈祷着铜线圈的拜礼...\n苦难的孩子，潜姿于月之上空气流层的你的身影啊，将身后的一切，身后的一切、都将托付于我一阵吧，互唤我的名字吧!!!...\n......信号中断....\n...........\n......"));
        changeInfo16.addButton(new ChangeButton(new Image("sprites/king.png", 0, 0, 13, 16),
                "怪物更改", "怪物修改了一些问题，具体如下:\n-_矮人国亡\n-_小粘咕-实验体\n-_DM-3000\n-天狗-火冽烽_-_ MLPD\n均在24层刷新"));
        changeInfo16.addButton(new ChangeButton(new Image("sprites/zei.png", 0, 0, 12, 13), "新增怪物", "新增了几个怪物，如下:\n-_红色怨灵\n-_影子盗贼"));
        changeInfo16.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG2), "新增武器", "新增武器如下:\n-_蛮人战斧\n-_炸弹匕首\n-_钥匙剑"));
        ChangeInfo changeInfo17 = new ChangeInfo("V0.3.0.0", true, "");
        changeInfo17.hardlight(65535);
        arrayList.add(changeInfo17);
        changeInfo17.addButton(new ChangeButton(new Image("sprites/king.png", 0, 0, 14, 16), "新怪物", "怪物新增如下:\n-_矮人国亡\n-_小粘咕-实验体\n-_DM-3000\n-天狗-火冽烽_-_ MLPD\n均在24层刷新"));
        changeInfo17.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POUCH), "改动", "初始物品改动如下:\n-_40瓶灵视药水\n-_40个口粮\n-_10个升级&鉴定卷轴_-_ MLPD"));
    }

    public static void V0_0_2Changes3(ArrayList<ChangeInfo> var0) {
        ChangeInfo var1 = new ChangeInfo("V0.0.1.7-9", true, "");
        var1.hardlight(65535);
        var0.add(var1);
        var1.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POUCH), "改动", "初始物品新增如下:\n-_5瓶_灵视药水\n-_两个_升级卷轴\n-_两个_鉴定卷轴\n-初始金币_6000__-_ MLPD\n\n"));
        var1.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RedBloodMoon), "新武器", "武器新增如下:\n-_蔚蓝审判神剑\n-_黑狗爪\n-_猩红血月神剑_-_ MLPD\n\n"));
    }

    public static void  V0_0_2Changes2( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("新增怪物&BUG修复", false, "");
        changes.hardlight(0x00ff00);
        changeInfos.add(changes);
        changes.addButton( new ChangeButton(new Image("SRPD/TorchHuntsman.png", 0, 0, 16, 16), "怪物新改动",
                "新怪物！！！\n" +
                        "-下水道层：\n-_棕色老鼠_，\n-_灰黑老鼠_，\n-_老年老鼠_，\n（来自OGPD)" +
                        "\n-监狱层：_赏金猎人_\n（来自SRPD)"+
                        "\n-矿洞层：_Flame-B01_\n（来自OGPD)"+
                        "\n后续层数正在开发,敬请期待\n\n"+
                        "_-_ MLPD\n\n"));

        changes.addButton( new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下错误:\n" +
                        "_-_ 因为算法问题，某些时刻跳楼会崩溃\n" +
                        "_-_ 修复了棕色老鼠将你一击必杀的问题\n" +
                        "_-_ 音乐接口优化\n\n" ));

    }

    public static void V0_0_2Changes1( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("地牢改动", false, "");
        changes.hardlight(0x00ffff);
        changeInfos.add(changes);
        changes.addButton( new ChangeButton(new Image(Assets.Sprites.WARRIOR, 0, 15, 12, 15), "角色属性改动",
                "\n-最高等级为100，再也不愁只有30级而不强大了\n\n"+
                        "_-_ MLPD"));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 48, 16
                , 16), "房间改动",
                "房间数量得到了调整,具体改动如下表\n" +
                        "-下水道层：房间数量_9个_，默认刷怪数量_20个_，额外刷怪_7个_\n" +
                        "-监狱层：房间数量_8个_，默认刷怪数量_26个_，额外刷怪_11个_\n"+
                        "-矿洞层：房间数量_10个_，默认刷怪数量_30个_，额外刷怪_16个_\n"+
                        "-矮人层：房间数量_12个_，默认刷怪数量_21个_，额外刷怪_6个_\n"+
                        "-恶魔层：房间数量_6个_，默认刷怪数量_10个_，额外刷怪_26个_\n"+
                        "后续层数正在开发,敬请期待\n\n"+
                        "_-_ MLPD\n\n"));
    }

    public static void V0_0_1Changes(ArrayList<ChangeInfo> changeInfos){
        ChangeInfo changes = new ChangeInfo("v0.0.1-6", true, "");
        changes.hardlight(0x00ff00 );
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight( 0xff00ff );
        changeInfos.add(changes);
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGIC_INFUSE, null), "全新内容A",
                "1.每一层都有自己的BGM，和Boss战斗有专属BGM\n-2.崩溃界面加入，这可能是最可爱的崩溃界面了\n-3.界面修改，默认配色为天蓝+紫色\n\n"));
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CURSE_INFUSE, null), "全新内容B",
                "1.蔚蓝审判之剑加入\n-2.血月死神之剑加入\n-3.小黑的爪子加入\n-4.老鼠忍者加入\n-5.背包格数大小升级，现在是36格\n\n"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight( 0xff832a );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_BEACON, null), "特别鸣谢",
                "特别鸣谢:\n-感谢_Alexstrasza_的大力帮助\n-_Ren_的代码指导\n-_FwのFang_的素材提供\n-_kemosquito_的logo优化\n-2021★MLPD\n\n"));
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_OPAL, null), "武器改动详情",
                "武器名称:_蔚蓝审判之剑_\n-首个版本属于_5阶武器_，现在已经降到了_3阶武器_\n\n"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( 0x00ff00);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.HOLSTER, null), "游戏物品增强记录A",
                "暂无记录\n\n"));
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_HOURGLASS, null), "游戏物品增强记录B",
                "暂无记录\n\n"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( 0xff0000);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_FROST, null), "武器削弱记录A",
                "1.蔚蓝审判之剑\n-此版本已削弱精准和伤害，现在它的伤害20-35伤害左右\n\n"));
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_LIGHTNING, null), "武器削弱记录B",
                "暂无记录\n\n"));
    }

    public static void Dev_Changes4( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.0.1.4", false, "");
        changes.hardlight(0xff00ff);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.GOLD), "开发日志",
                "_-_ 2021-2-16\n" +
                        "_-_今天因为隔几天要补课回到了城里\n" +
                        "_-_于是我就打开了电脑安装了AS，并且成功导入了工程包\n" +
                        "_-_并且在_红龙_的指点下开始用_AS_编写地牢了\n" +
                        "_-_我以前都看不懂地牢代码，现在居然能在_AS_里面看的懂一些代码了\n" +
                        "_-_倘若当初不是_Ren_手把手教我制作地牢，也许我现在依然是一个_地牢玩家_\n" +
                        "_-_在这里，非常感谢_Ren_和_红龙_两位的指导和帮助\n" +
                        "_-_没有你们,_MLPD_或许很快就会消失，甚至不复存在\n" +
                        "_-_还有几天我就要补课了，等我_高三结束_,_MLPD_项目正式开始！\n" +
                        "_2021年2月16日_,感谢_红龙_和_Ren_\n\n"));
    }

    public static void Dev_Changes3( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.0.1.3", false, "");
        changes.hardlight(0xff0000);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.GOLD), "开发日志",
                "_-_ 2021-2-15\n" +
                        "_-_啊啊啊，我只是想改个_语言界面_，为什么这么麻烦啊！_SMAIL_这边改了就闪退！\n" +
                        "_-_于是万般无奈的我联系了_红龙_，没想到红龙哥很爽快的了帮了我，还帮我升级了_MLPD_\n" +
                        "_2021年2月15日_,感谢_红龙_的AS升级\n\n"));
    }

    public static void Dev_Changes2( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.0.1.2", false, "");
        changes.hardlight(0x00ff00);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.GOLD), "开发日志",
                "_-_ 2021-2-14\n" +
                        "_-_由于我的眼瞎，加个武器加了两天,而且还因为_脸黑_,一直刷不到武器\n" +
                        "_-_最后终于还是_Ren_帮忙才见到我的第一个武器啊啊啊，在这里特别感谢_Ren_!\n" +
                        "_2021年2月14日_,初学者地牢开发者在成长！\n\n"));
    }

    public static void Dev_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.0.1.1", false, "");
        changes.hardlight(0x00ffff);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.GOLD), "开发日志",
                "_-_ 2021-2-12\n" +
                        "_-_从REN那里开始我的_MLPD_地牢计划\n" +
                        "_-_这是全新而又美好的一天，我在这里对REN表示，非常感谢你对我地牢初学的大力指导\n" +
                        "_2021年2月12日_,一个新的地牢开发者正在萌芽生长！\n\n"));
    }

    public static void V0_0_2Changes(ArrayList<ChangeInfo> changeInfos){
        ChangeInfo changes = new ChangeInfo("V0.0.0", true, "");
        changes.hardlight( 0xff0000 );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.GOLD), "更新纪念",
                "2021-2-12\n" +
                        "从_REN_那里开始了我的地牢编码生涯\n" +
                        "_-_ 版本纪念,感谢所有支持我的人\n\n"));


    }
}