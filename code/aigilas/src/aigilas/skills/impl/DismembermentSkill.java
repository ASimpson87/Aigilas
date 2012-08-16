package aigilas.skills.impl;import aigilas.creatures.AigilasActorType;import aigilas.creatures.CreatureFactory;import aigilas.creatures.ICreature;import aigilas.creatures.StatType;import aigilas.entities.Elements;import aigilas.skills.AnimationType;import aigilas.skills.ISkill;import aigilas.skills.SkillId;import spx.core.GameManager;import spx.core.Point2;import spx.core.RNG;public class DismembermentSkill extends ISkill {	public DismembermentSkill()	{		super(SkillId.DISMEMBERMENT, AnimationType.SELF);		Add(Elements.PHYSICAL);		AddCost(StatType.MANA, 3);	}	@Override	public void Activate(ICreature target)	{		super.Activate(target);		int openCell = RNG.Next(1, GameManager.TileMapWidth - 1);		for (int ii = 1; ii < GameManager.TileMapWidth - 1; ii++) {			if (ii != openCell) {				CreatureFactory						.Create(AigilasActorType.HAND, new Point2(ii, 1));			}		}	}}