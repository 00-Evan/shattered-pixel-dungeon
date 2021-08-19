package com.saqfish.spdnet.windows;

import com.saqfish.spdnet.actors.hero.HeroClass;
import com.saqfish.spdnet.actors.hero.Talent;
import com.saqfish.spdnet.actors.hero.abilities.ArmorAbility;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.sprites.ItemSprite;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.ui.HeroIcon;
import com.saqfish.spdnet.ui.TalentsPane;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndInfoArmorAbility extends WndTitledMessage {

	public WndInfoArmorAbility(HeroClass cls, ArmorAbility ability){
		super( new HeroIcon(ability), Messages.titleCase(ability.name()), ability.desc());

		ArrayList<LinkedHashMap<Talent, Integer>> talentList = new ArrayList<>();
		Talent.initArmorTalents(ability, talentList);

		TalentsPane.TalentTierPane talentPane = new TalentsPane.TalentTierPane(talentList.get(3), 4, false);
		talentPane.title.text( Messages.titleCase(Messages.get(WndHeroInfo.class, "talents")));
		talentPane.setRect(0, height + 5, width, talentPane.height());
		add(talentPane);
		resize(width, (int) talentPane.bottom());

	}

}
