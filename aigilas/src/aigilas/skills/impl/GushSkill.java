package aigilas.skills.impl;

import aigilas.creatures.BaseCreature;
import aigilas.skills.AnimationType;
import aigilas.skills.BaseSkill;
import aigilas.skills.SkillId;

public class GushSkill extends BaseSkill {
    public GushSkill()

    {
        super(SkillId.GUSH, AnimationType.RANGED);


    }

    @Override
    public void affect(BaseCreature target)

    {
        target.applyDamage(10, _source, true);

    }

}
