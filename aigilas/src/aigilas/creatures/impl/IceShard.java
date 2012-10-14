package aigilas.creatures.impl;

import aigilas.entities.Elements;
import aigilas.skills.SkillId;
import aigilas.strategies.Strategy;
import aigilas.strategies.StrategyFactory;
import sps.bridge.ActorType;

public class IceShard extends Minion {
    public IceShard() {
        super(ActorType.Minion);
        _strategy = StrategyFactory.create(Strategy.MinionOneUse, this);
        add(SkillId.Ice_Shard);
        _composition.add(Elements.Water);
    }
}