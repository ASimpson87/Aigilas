package com.aigilas.strategies.impl;import com.xna.wrapper.*;import java.util.*;import com.aigilas.creatures.*;import com.spx.core.*;    public class MinionFireStrategy  extends  IStrategy    {        public MinionFireStrategy(ICreature parent)            { super(parent,Strategy.MinionFire);            parent.SetSkillVector(parent.GetSkillVector());        }@Override        public  void Act()        {            if (_parent.IsCooledDown())            {                _parent.UseActiveSkill();                _parent.ApplyDamage(5, null, false);                _parent.Set(StatType.MOVE_COOL_DOWN, 0);            }        }    }