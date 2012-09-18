package aigilas.strategies.impl;

import aigilas.creatures.BaseCreature;
import aigilas.creatures.StatType;
import aigilas.strategies.BaseStrategy;
import aigilas.strategies.Strategy;

public class MinionFireStrategy extends BaseStrategy {
    public MinionFireStrategy(BaseCreature parent)

    {
        super(parent, Strategy.MinionFire);

        parent.setSkillVector(parent.getSkillVector());
    }

    @Override
    public void act() {
        if (_parent.isCooledDown()) {
            _parent.useActiveSkill();
            _parent.applyDamage(5, null, false);
            _parent.set(StatType.MOVE_COOL_DOWN, 0);
        }
    }
}
