package aigilas.skills.impl;

import aigilas.creatures.ICreature;
import aigilas.creatures.StatType;
import aigilas.entities.Elements;
import aigilas.skills.AnimationType;
import aigilas.skills.ISkill;
import aigilas.skills.SkillId;
import aigilas.statuses.Status;
import aigilas.statuses.StatusFactory;

public class BoilSkill extends ISkill {
	public BoilSkill()

	{
		super(SkillId.BOIL, AnimationType.SELF);

		Add(Elements.AIR);
		AddCost(StatType.MANA, 10);

	}

	@Override
	public void Activate(ICreature source) {
		StatusFactory.Apply(source, Status.Boil);

	}

}