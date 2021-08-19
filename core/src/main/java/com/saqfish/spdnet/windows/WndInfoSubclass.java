package com.saqfish.spdnet.windows;

import com.saqfish.spdnet.actors.hero.HeroClass;
import com.saqfish.spdnet.actors.hero.HeroSubClass;
import com.saqfish.spdnet.actors.hero.Talent;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.ui.HeroIcon;
import com.saqfish.spdnet.ui.TalentsPane;
import com.saqfish.spdnet.windows.WndHeroInfo;
import com.saqfish.spdnet.windows.WndTitledMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndInfoSubclass extends WndTitledMessage {

	public WndInfoSubclass(HeroClass cls, HeroSubClass subCls){
		super( new HeroIcon(subCls), Messages.titleCase(subCls.title()), subCls.desc());

		ArrayList<LinkedHashMap<Talent, Integer>> talentList = new ArrayList<>();
		Talent.initClassTalents(cls, talentList);
		Talent.initSubclassTalents(subCls, talentList);

		TalentsPane.TalentTierPane talentPane = new TalentsPane.TalentTierPane(talentList.get(2), 3, false);
		talentPane.title.text( Messages.titleCase(Messages.get(WndHeroInfo.class, "talents")));
		talentPane.setRect(0, height + 5, width, talentPane.height());
		add(talentPane);
		resize(width, (int) talentPane.bottom());

	}

}
