package aigilas.skills.impl;

import aigilas.creatures.ICreature;
import aigilas.creatures.StatType;
import aigilas.entities.Elements;
import aigilas.skills.AnimationType;
import aigilas.skills.ISkill;
import aigilas.skills.SkillId;
import aigilas.statuses.Status;

public class CavalrySkill extends ISkill {
	public CavalrySkill()

	{
		super(SkillId.CAVALRY, AnimationType.SELF);

		Add(Elements.DARK, Elements.EARTH);
		AddCost(StatType.MANA, 10);

	}

	@Override
	public void Activate(ICreature source) {
		super.Activate(source);
		ApplyToPlayers(Status.DefenseUp);
	}
}