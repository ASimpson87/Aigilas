﻿using OGUR.Creatures;
using OGUR.Entities;
using SPX.Core;
using SPX.Entities;

namespace OGUR.Skills
{
    public class SkillBehavior
    {
        protected SideEffects _sideEffects;
        protected ISkill _parent;
        protected bool _used = false;
        protected Stats _cost;

        public SkillBehavior(int effectGraphic, int animation,ISkill parentSkill)
        {
            _parent = parentSkill;
            _sideEffects = new SideEffects(effectGraphic, animation,_parent);
            _cost = new Stats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
        public int GetSpriteType() { return _sideEffects.GetSpriteType(); }
        public virtual void Activate(ICreature target) { }
        public virtual void Cleanup(Entity target, SkillEffect source) { }
        public bool IsActive()
        {
            return !_used;
        }
        public SkillEffect GetGraphic()
        {
            return _sideEffects.GetFirstGraphic();
        }
        public void AddCost(string stat, float cost)
        {
            _cost.AddBuff(new StatBuff(stat, cost));
        }
        protected bool SubtractCost(ICreature owner)
        {
            bool costPaid = false;
            foreach (string stat in StatType.Values)
            {
                if (stat != StatType.REGEN)
                {
                    if (owner.LowerStat(stat, _cost.Get(stat)))
                    {
                        costPaid = true;
                    }
                }
            }
            return costPaid;
        }
        private IEntity hitTarget;
        private ICreature hitCreature;
        public virtual bool AffectTarget(ICreature source,SkillEffect graphic)
        {
            hitTarget = source.GetTargets().GetCollidedTarget(graphic);
            if (null != hitTarget && hitTarget!=source)
            {
                _parent.Affect(hitTarget);
                hitCreature = hitTarget.IsCreature();
                if (hitCreature != null)
                {
                    hitCreature.Combo(_parent.GetElements());
                    hitCreature.React(_parent.GetSkillId());
                }
                if (!_parent.IsPersistent())
                {
                    return false;
                }
            }
            return true;
        }
        public int GetAnimationType()
        {
            return _sideEffects.GetAnimationType();
        }

        public float GetCost()
        {
            return _cost.Get(StatType.MANA);
        }
    }
    public class RangedBehavior: SkillBehavior
    {
        public RangedBehavior(int effectGraphic, ISkill parentSkill) : base(effectGraphic, AnimationType.RANGED, parentSkill) { }
        public override void Activate(ICreature target) { if (SubtractCost(target)) { _sideEffects.Generate(target.GetLocation().Add(target.GetSkillVector()), target.GetSkillVector(), target); } }
    }
    public class SelfBehavior:SkillBehavior
    {
        public SelfBehavior(int effectGraphic, ISkill parentSkill) : base(effectGraphic, AnimationType.SELF, parentSkill) { }
        public override void Activate(ICreature target) { if (SubtractCost(target)) { _sideEffects.Generate(target.GetLocation(), new Point2(0, 0), target); } }
        public override bool AffectTarget(ICreature source, SkillEffect graphic)
        {
            if (!_used)
            {                
                source.React(_parent.GetSkillId());
                _parent.Affect(source);
                _used = true;
            }
            return true;
        }
    }
    public class StationaryBehavior : SkillBehavior
    {
        public StationaryBehavior(int effectGraphic, ISkill parentSkill) : base(effectGraphic, AnimationType.STATIONARY, parentSkill) { }
        public override void Activate(ICreature target) 
        {
            if(SubtractCost(target))
            {
                if (_parent.StartOffCenter)
                {
                    var location = new Point2(target.GetLocation().GridX + target.GetSkillVector().GridX, target.GetLocation().GridY + target.GetSkillVector().GridY);
                    _sideEffects.Generate(location, new Point2(0,0), target);
                }
                else
                {
                    _sideEffects.Generate(target.GetLocation(), new Point2(0,0), target);
                }
            }
        }
    }
    public class CloudBehavior:SkillBehavior
    {
        public CloudBehavior(int effectGraphic, ISkill parentSkill) : base(effectGraphic, AnimationType.CLOUD, parentSkill) { }
        public override void Activate(ICreature target)
        {
            if (SubtractCost(target))
            {
                var referencePoint = target.GetLocation();
                for (var ii = -1; ii < 2; ii++)
                {
                    for (var jj = -1; jj < 2; jj++)
                    {
                        if (ii != 0 || jj != 0)
                        {
                            var cloudPosition = new Point2(referencePoint.GridX + ii, referencePoint.GridY + jj);
                            _sideEffects.Generate(cloudPosition, new Point2(0, 0), target);
                        }
                    }
                }
            }
        }
    }
    public class RotateBehavior : SkillBehavior
    {
        public RotateBehavior(int effectGraphic, ISkill parentSkill) : base(effectGraphic, AnimationType.ROTATE, parentSkill) { }
        public override void Activate(ICreature target)
        {
            _sideEffects.Generate(target.GetLocation(), new Point2(0, 0), target);
        }
    }
}
